package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class DriveSubsystem extends SubsystemBase {
    private final DcMotorEx leftDrive, rightDrive;

    public DriveSubsystem(HardwareMap hardwareMap) {
        leftDrive = hardwareMap.get(DcMotorEx.class, "driveLeft");
        rightDrive = hardwareMap.get(DcMotorEx.class, "driveRight");
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPower(double leftPower, double rightPower) {
        leftDrive.setPower(clamp(leftPower));
        rightDrive.setPower(clamp(rightPower));
    }

    public void setPower(@NonNull double[] powers) {
        validatePotentials(powers);
        setPower(powers[0], powers[1]);
    }

    public double[] calculateTankComponents(double leftInput, double rightInput, double speedCoefficient) {
        return new double[]{
                clamp(leftInput) * speedCoefficient,
                clamp(rightInput) * speedCoefficient
        };
    }

    public double[] calculateArcadeComponents(double forward, double turn, double speedCoefficient) {
        double leftPower = forward + turn;
        double rightPower = forward - turn;

        double maxPower = Math.max(
                Math.abs(leftPower),
                Math.abs(rightPower)
        );

        if (maxPower > 1.0) {
            leftPower /= maxPower;
            rightPower /= maxPower;
        }

        return new double[]{
                clamp(leftPower) * speedCoefficient,
                clamp(rightPower) * speedCoefficient
        };
    }

    public void move(double forward, double turn, double speedMultiplier) {
        setPower(calculateArcadeComponents(forward, turn, speedMultiplier));
    }

    private static double clamp(double value) {
        return Math.max(-1.0, Math.min(1.0, value));
    }
    public void stop() {
        setPower(0.0, 0.0);
    }

    private static void validatePotentials(@NonNull double[] potentials) {
        if (potentials.length != 2) {
            throw new IllegalArgumentException("Tank drive requires exactly two motor values.");
        }
    }
}

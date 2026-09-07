package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardwares;

import java.util.function.DoubleSupplier;

/**
 * 双电机驱动系统。
 *
 * 功能：
 * 1. 控制左右两个驱动电机
 * 2. 提供坦克驱动和街机驱动计算
 * 3. 提供基础街机驱动命令
 */
public class Drive {

    private final DcMotorEx driveLeft;
    private final DcMotorEx driveRight;

    public Drive(@NonNull Hardwares hardwares) {
        this.driveLeft = hardwares.motors.driveLeft;
        this.driveRight = hardwares.motors.driveRight;
        init();
    }

    private void init() {
        driveLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        driveLeft.setDirection(DcMotor.Direction.REVERSE);
        driveRight.setDirection(DcMotor.Direction.FORWARD);

        driveLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setPower(double leftPower, double rightPower) {
        driveLeft.setPower(clamp(leftPower));
        driveRight.setPower(clamp(rightPower));
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

    public double[] calculateArcadeComponents(double drive, double turn, double speedCoefficient) {
        double leftPower = drive + turn;
        double rightPower = drive - turn;

        double maxMagnitude = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (maxMagnitude > 1.0) {
            leftPower /= maxMagnitude;
            rightPower /= maxMagnitude;
        }

        return new double[]{
                clamp(leftPower) * speedCoefficient,
                clamp(rightPower) * speedCoefficient
        };
    }

    public void stop() {
        setPower(0.0, 0.0);
    }

    private static void validatePotentials(@NonNull double[] potentials) {
        if (potentials.length != 2) {
            throw new IllegalArgumentException("Tank drive requires exactly two motor values.");
        }
    }

    private static double clamp(double value) {
        return Math.max(-1.0, Math.min(1.0, value));
    }

    public static class ArcadeDriveCommand extends CommandBase {
        private final Drive drive;
        private final DoubleSupplier driveSupplier;
        private final DoubleSupplier turnSupplier;
        private final double speedCoefficient;

        public ArcadeDriveCommand(
                Drive drive,
                DoubleSupplier driveSupplier,
                DoubleSupplier turnSupplier,
                double speedCoefficient
        ) {
            this.drive = drive;
            this.driveSupplier = driveSupplier;
            this.turnSupplier = turnSupplier;
            this.speedCoefficient = speedCoefficient;
        }

        @Override
        public void execute() {
            double[] potentials = drive.calculateArcadeComponents(
                    driveSupplier.getAsDouble(),
                    turnSupplier.getAsDouble(),
                    speedCoefficient
            );
            drive.setPower(potentials);
        }

        @Override
        public void end(boolean interrupted) {
            drive.stop();
        }
    }
}

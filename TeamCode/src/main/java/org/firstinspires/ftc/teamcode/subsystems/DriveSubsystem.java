package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class DriveSubsystem extends SubsystemBase {
    private final DcMotorEx leftDrive, rightDrive;
    public double speedMultiplier;

    public DriveSubsystem(HardwareMap hardwareMap) {
        leftDrive = hardwareMap.get(DcMotorEx.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotorEx.class, "rightDrive");
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

//    public void move(double forward, double turn, double speedMultiplier) {
//        double leftPower = (forward + turn) * speedMultiplier;
//        double rightPower = (forward - turn) * speedMultiplier;
//        double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
//        if(max > 1.0){
//            leftPower /= max;
//            rightPower /= max;
//        }
//        leftDrive.setPower(leftPower);
//        rightDrive.setPower(rightPower);
//    }
public void setPower(double leftPower, double rightPower) {
    leftDrive.setPower(clamp(leftPower));
    rightDrive.setPower(clamp(rightPower));
}
public void move(double forward, double turn, boolean isSlowMode) {
    double leftPower = forward + turn;
    double rightPower = forward - turn;

    double maxPower = Math.max(
            Math.abs(leftPower),
            Math.abs(rightPower)
    );

    // 防止 forward + turn 超过 [-1, 1]
    if (maxPower > 1.0) {
        leftPower /= maxPower;
        rightPower /= maxPower;
    }

    leftPower *= speedMultiplier;
    rightPower *= speedMultiplier;

    setPower(leftPower, rightPower);
}

    private static double clamp(double value) {
        return Math.max(-1.0, Math.min(1.0, value));
    }
    public void stop() {
        setPower(0.0, 0.0);
    }


}
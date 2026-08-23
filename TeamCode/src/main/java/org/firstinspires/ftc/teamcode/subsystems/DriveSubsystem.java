package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class DriveSubsystem extends SubsystemBase {
    private final DcMotorEx leftDrive;
    private final DcMotorEx rightDrive;

    public DriveSubsystem(final HardwareMap hmap, final String name) {
        leftDrive = hmap.get(DcMotorEx.class, name);
        rightDrive = hmap.get(DcMotorEx.class, name);
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void move(double forward, double turn, double speedMultiplier) {
        double leftPower = (forward + turn) * speedMultiplier;
        double rightPower = (forward - turn) * speedMultiplier;
        double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if(max > 1.0){
            leftPower /= max;
            rightPower /= max;
        }
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }


}
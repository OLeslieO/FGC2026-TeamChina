package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class ShooterSubsystem extends SubsystemBase {
    private final DcMotorEx shooterLeft, shooterRight, preShooter;
    public ShooterSubsystem(HardwareMap hardwareMap) {
        shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");
        preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        preShooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void accelerate() {
        shooterLeft.setPower(Constants.SHOOTER_SHOOT_POW.value);
        shooterRight.setPower(Constants.SHOOTER_SHOOT_POW.value);
    }
    public void idle() {
        shooterLeft.setPower(Constants.SHOOTER_IDLE_POW.value);
        shooterRight.setPower(Constants.SHOOTER_IDLE_POW.value);
    }
    public void stop() {
        shooterLeft.setPower(0);
        shooterRight.setPower(0);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void shoot(){
        preShooter.setPower(Constants.PRESHOOTER_SHOOT_POW.value);
    }
    public void stopShoot(){
        preShooter.setPower(0);
    }

}
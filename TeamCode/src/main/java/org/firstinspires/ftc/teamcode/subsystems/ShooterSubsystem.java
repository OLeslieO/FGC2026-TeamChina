package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    public final DcMotorEx shooterLeft, shooterRight, preShooter, ascentMotor;
    public final Servo blender;
    public ShooterSubsystem(HardwareMap hardwareMap) {
        shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");
        preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
        ascentMotor = hardwareMap.get(DcMotorEx.class, "ascentMotor");
        blender = hardwareMap.get(Servo.class, "blender");
        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        preShooter.setDirection(DcMotorSimple.Direction.FORWARD);
        ascentMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        preShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ascentMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        preShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ascentMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setShooterPIDF(
                Constants.SHOOTER_PIDF_P.value,
                Constants.SHOOTER_PIDF_I.value,
                Constants.SHOOTER_PIDF_D.value,
                Constants.SHOOTER_PIDF_F.value
        );
    }

    public void accelerate(double power) {
        shooterLeft.setPower(power);
        shooterRight.setPower(power);
    }
    public void idle(double power) {
        shooterLeft.setPower(power);
        shooterRight.setPower(power);
    }
    public void stopShooter() {
        shooterLeft.setPower(0);
        shooterRight.setPower(0);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setShooterVelocity(double velocity) {
        setShooterVelocity(velocity, velocity);
    }

    public void setShooterVelocity(double leftVelocity, double rightVelocity) {
        shooterLeft.setVelocity(leftVelocity);
        shooterRight.setVelocity(rightVelocity);
    }

    public void setShooterPIDF(double p, double i, double d, double f) {
        shooterLeft.setVelocityPIDFCoefficients(p, i, d, f);
        shooterRight.setVelocityPIDFCoefficients(p, i, d, f);
    }

    public void setTransferPower(double power) {
        preShooter.setPower(power);
        ascentMotor.setPower(power);
    }

    public void setTransferVelocity(double velocity) {
        preShooter.setVelocity(velocity);
        ascentMotor.setVelocity(velocity);
    }

    public void setTransWithBlendPower(double power){
        setTransferPower(power);
        blender.setPosition(1);
    }

    public void setTransWithBlendVel(double velocity) {
        setTransferVelocity(velocity);
        blender.setPosition(1);
    }

    public void ascent(double power) {
        setTransferPower(power);
    }

    public void descent(double power) {
        setTransferPower(-power);
    }

    public void stopTransfer() {
        setTransferVelocity(0);
        setTransferPower(0);
    }

    public void stopShoot(){
        stopTransfer();
        blender.setPosition(0.5);
    }
}

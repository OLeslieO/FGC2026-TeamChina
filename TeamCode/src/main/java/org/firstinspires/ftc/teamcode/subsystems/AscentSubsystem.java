package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class AscentSubsystem extends SubsystemBase {
    private final DcMotorEx ascentMotor;
    private final Servo ascentServo;
    public AscentSubsystem(HardwareMap hardwareMap) {
        ascentMotor = hardwareMap.get(DcMotorEx.class, "ascentMotor");
        ascentMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        ascentMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ascentServo = hardwareMap.get(Servo.class, "ascentServo");
    }

    public void ascent() {
        ascentMotor.setPower(Constants.ASCENT_POW.value);
    }
    public void descent() {
        ascentMotor.setPower(-Constants.ASCENT_POW.value);
    }
    public void servoOn(){
        ascentServo.setPosition(0.28);}//关闭爬升
    public void servoOff(){
        ascentServo.setPosition(0);//释放爬升
    }
    public void stop() {
        ascentMotor.setPower(0);
    }
}
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class AscentSubsystem extends SubsystemBase {
    private final DcMotorEx ascentMotor;
    public AscentSubsystem(HardwareMap hardwareMap) {
        ascentMotor = hardwareMap.get(DcMotorEx.class, "AscentMotor");
        ascentMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        ascentMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void ascent() {
        ascentMotor.setPower(Constants.ASCENT_POW.value);
    }
    public void descent() {
        ascentMotor.setPower(-Constants.ASCENT_POW.value);
    }
    public void stop() {
        ascentMotor.setPower(0);
    }
}
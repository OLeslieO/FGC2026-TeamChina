package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class IntakeSubsystem extends SubsystemBase {
    private final DcMotorEx intake, retract;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        retract = hardwareMap.get(DcMotorEx.class, "laLa");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        retract.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        retract.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void init(){
        intake.setPower(0);
    }

    public void setIntakePower(double power) {
        intake.setPower(power);
    }

    public void setRetractPower(double power) {
        retract.setPower(power);
    }
}

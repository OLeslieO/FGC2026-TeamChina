package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class IntakeSubsystem extends SubsystemBase {
    private final DcMotorEx intake;
    private static double INTAKE_PWR = 1;        // 吸球功率（向内）


    public IntakeSubsystem(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void init(){
        intake.setPower(0);
    }
    public void intakePower(double power) {
        INTAKE_PWR = power;
        intake.setPower(INTAKE_PWR);
    }

}
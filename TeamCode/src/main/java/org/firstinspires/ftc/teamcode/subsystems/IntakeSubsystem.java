package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class IntakeSubsystem extends SubsystemBase {
    private final DcMotorEx intake, laLa;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        laLa = hardwareMap.get(DcMotorEx.class, "laLa");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void init(){
        intake.setPower(0);
    }

    public void intakePower(double power) {
        intake.setPower(power);
    }


    public void laLaPower(double power) {
        laLa.setPower(power);
    }
}

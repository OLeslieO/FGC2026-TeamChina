package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class Intake extends SubsystemBase {
    private final DcMotorEx intake;

    public Intake(final HardwareMap hmap, final String name) {
        intake = hmap.get(DcMotorEx.class, name);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void intake() {
        intake.setPower(1);
    }
    public void stop() {
        intake.setPower(0);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

}
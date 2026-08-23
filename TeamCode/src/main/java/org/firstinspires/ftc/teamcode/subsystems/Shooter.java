package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class Shooter extends SubsystemBase {
    private final DcMotorEx shooterLeft;
    private final DcMotorEx shooterRight;

    public Shooter(final HardwareMap hmap, final String name) {
        shooterLeft = hmap.get(DcMotorEx.class, name);
        shooterRight = hmap.get(DcMotorEx.class, name);
        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void shoot() {
        shooterLeft.setPower(1);
        shooterRight.setPower(1);
    }
    public void stop() {
        shooterLeft.setPower(0);
        shooterRight.setPower(0);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

}
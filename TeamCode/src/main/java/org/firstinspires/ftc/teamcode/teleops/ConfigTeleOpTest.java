package org.firstinspires.ftc.teamcode.teleops;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.subsystems.Constants;

@Config
@TeleOp(name = "ConfigTeleOpTest", group = "test")
public class ConfigTeleOpTest extends TeleOpSolo {
    public static double shooterShootPower = Constants.SHOOTER_SHOOT_POW.value;
    public static double shooterIdlePower = Constants.SHOOTER_IDLE_POW.value;
    public static double transferVel = Constants.TRANSFER_VEL.value;
    public static double transferPower = Constants.TRANSFER_POW.value;
    public static double intakePower = Constants.INTAKE_PWR.value;
    public static double laLaPower = Constants.RETRACT_PWR.value;
    public static double driveSpeedMultiplier = Constants.DRIVE_SPEED_MULTIPLIER.value;

    @Override
    public void run() {
        addTelemetry();
        telemetry.update();
        CommandScheduler.getInstance().run();
    }

    @Override
    protected double getShooterShootPower() {
        return shooterShootPower;
    }

    @Override
    protected double getShooterIdlePower() {
        return shooterIdlePower;
    }

    @Override
    protected double getTransferVel() {
        return transferVel;
    }
    @Override
    protected double getTransferPower() {
        return transferPower;
    }

    @Override
    protected double getIntakePower() {
        return intakePower;
    }

    @Override
    protected double getRetractPower() {
        return laLaPower;
    }

    @Override
    protected double getDriveSpeedMultiplier() {
        return driveSpeedMultiplier;
    }

    @Override
    protected void addTelemetry() {
        super.addTelemetry();
        telemetry.addData("shooterLeftPower", shooterSubsystem.shooterLeft.getPower());
        telemetry.addData("shooterRightPower", shooterSubsystem.shooterRight.getPower());
        telemetry.addData("preShooterPower", shooterSubsystem.preShooter.getPower());
        telemetry.addData("ascentPower", shooterSubsystem.ascentMotor.getPower());
        telemetry.addData("configShooterShootPower", shooterShootPower);
        telemetry.addData("configShooterIdlePower", shooterIdlePower);
        telemetry.addData("configPreShooterPower", transferPower);
        telemetry.addData("configIntakePower", intakePower);
        telemetry.addData("configLaLaPower", laLaPower);
        telemetry.addData("configDriveSpeedMultiplier", driveSpeedMultiplier);
    }
}

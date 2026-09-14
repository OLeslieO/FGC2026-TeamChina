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
    public static double preShooterPower = Constants.TRANSFER_POW.value;
    public static double intakePower = Constants.INTAKE_PWR.value;
    public static double laLaPower = Constants.LALA_PWR.value;
    public static double driveFastMultiplier = Constants.DRIVE_FAST_MULTIPLIER.value;
    public static double driveSlowMultiplier = Constants.DRIVE_SLOW_MULTIPLIER.value;

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
    protected double getTransferPower() {
        return preShooterPower;
    }

    @Override
    protected double getIntakePower() {
        return intakePower;
    }

    @Override
    protected double getLaLaPower() {
        return laLaPower;
    }

    @Override
    protected double getDriveFastMultiplier() {
        return driveFastMultiplier;
    }

    @Override
    protected double getDriveSlowMultiplier() {
        return driveSlowMultiplier;
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
        telemetry.addData("configPreShooterPower", preShooterPower);
        telemetry.addData("configIntakePower", intakePower);
        telemetry.addData("configLaLaPower", laLaPower);
        telemetry.addData("configDriveFastMultiplier", driveFastMultiplier);
        telemetry.addData("configDriveSlowMultiplier", driveSlowMultiplier);
    }
}

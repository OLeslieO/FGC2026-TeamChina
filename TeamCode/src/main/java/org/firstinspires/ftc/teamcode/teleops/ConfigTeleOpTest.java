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
    public static double shooterP = Constants.SHOOTER_PIDF_P.value;
    public static double shooterI = Constants.SHOOTER_PIDF_I.value;
    public static double shooterD = Constants.SHOOTER_PIDF_D.value;
    public static double shooterF = Constants.SHOOTER_PIDF_F.value;
    public static double transferVel = Constants.TRANSFER_VEL.value;
    public static double transferPower = Constants.TRANSFER_POW.value;
    public static double intakePower = Constants.INTAKE_PWR.value;
    public static double laLaPower = Constants.RETRACT_PWR.value;
    public static double driveSpeedMultiplier = Constants.DRIVE_SPEED_MULTIPLIER.value;

    @Override
    public void run() {
        shooterSubsystem.setShooterPIDF(shooterP, shooterI, shooterD, shooterF);
        addTelemetry();
        telemetry.update();
        CommandScheduler.getInstance().run();
    }

    @Override
    protected double getDriveSpeedMultiplier() {
        return driveSpeedMultiplier;
    }

    @Override
    protected void addTelemetry() {
        super.addTelemetry();
        telemetry.addData("shooterLeftPower", shooterSubsystem.getLeftShooterPower());
        telemetry.addData("shooterRightPower", shooterSubsystem.getRightShooterPower());
        telemetry.addData("preShooterPower", shooterSubsystem.getPreShooterPower());
        telemetry.addData("ascentPower", shooterSubsystem.getAscentPower());
        telemetry.addData("configShooterShootPower", shooterShootPower);
        telemetry.addData("configShooterIdlePower", shooterIdlePower);
        telemetry.addData("configShooterPIDF", "P %.3f I %.3f D %.3f F %.3f",
                shooterP, shooterI, shooterD, shooterF);
        telemetry.addData("configPreShooterPower", transferPower);
        telemetry.addData("configIntakePower", intakePower);
        telemetry.addData("configLaLaPower", laLaPower);
        telemetry.addData("configDriveSpeedMultiplier", driveSpeedMultiplier);
    }
}

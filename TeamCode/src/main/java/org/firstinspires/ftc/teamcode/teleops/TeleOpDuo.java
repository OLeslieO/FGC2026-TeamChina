package org.firstinspires.ftc.teamcode.teleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.ButtonEx;
import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.Constants;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.utils.CommandOpmodeEx;


@TeleOp(group = "0-competition", name = "TeleOp Duo")
public class TeleOpDuo extends CommandOpmodeEx {

    protected GamepadEx gamepadEx1;
    protected GamepadEx gamepadEx2;
    protected DriveSubsystem driveSubsystem;
    protected ShooterSubsystem shooterSubsystem;
    protected IntakeSubsystem intakeSubsystem;


    @Override
    public void initialize() {

        CommandScheduler.getInstance().cancelAll();


        gamepadEx1 = new GamepadEx(gamepad1);
        gamepadEx2 = new GamepadEx(gamepad2);
        ToggleButtonReader toggleLeftBumperReader = new ToggleButtonReader(
                gamepadEx1, GamepadKeys.Button.LEFT_BUMPER
        );

        /* ---------- Subsystems ---------- */
        driveSubsystem = new DriveSubsystem(hardwareMap);
        shooterSubsystem = new ShooterSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);

        /* ---------- Drive Command ---------- */
        DriveCommand driveCommand = new DriveCommand(
                driveSubsystem,
                () -> -gamepadEx1.getLeftY(),
                () -> gamepadEx1.getRightX(),
                () -> gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER)
                        ? getDriveSlowMultiplier()
                        : getDriveFastMultiplier()
        );

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.clearAll();
        /* ---------- Schedule ---------- */
        CommandScheduler.getInstance().schedule(driveCommand);

        /* ---------- Match Timers ---------- */
//        new ButtonEx(() -> getRuntime() > 30).whenPressed(() -> gamepad1.rumble(500));
//        new ButtonEx(() -> getRuntime() > 60).whenPressed(() -> gamepad1.rumble(500));
//        new ButtonEx(() -> getRuntime() > 110).whenPressed(() -> gamepad1.rumble(1000));
    }

    @Override
    public void onStart() {
        resetRuntime();
    }

    @Override
    public void functionalButtons() {
        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.A))
                .whenPressed(new InstantCommand(() -> shooterSubsystem.accelerate(getShooterShootPower())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopShooter()));

        new ButtonEx(() -> gamepadEx2.getButton(GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(new InstantCommand(() -> shooterSubsystem.shoot(getTransferPower())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopShoot()));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(new InstantCommand(() -> intakeSubsystem.intakePower(getIntakePower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.intakePower(0)));

//        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP))
//                .whenPressed(new InstantCommand(() -> shooterSubsystem.setTransferPower(getTransferPower())))
//                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopTransfer()));
//
//        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN))
//                .whenPressed(new InstantCommand(() -> shooterSubsystem.setTransferPower(-getTransferPower())))
//                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopTransfer()));

        new ButtonEx(() -> gamepadEx2.getLeftY() > 0.5)
                .whenPressed(new InstantCommand(() -> intakeSubsystem.laLaPower(getLaLaPower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.laLaPower(0)));

        new ButtonEx(() -> gamepadEx2.getLeftY() < -0.5)
                .whenPressed(new InstantCommand(() -> intakeSubsystem.laLaPower(-getLaLaPower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.laLaPower(0)));
    }

    @Override
    public void run() {
        addTelemetry();
        telemetry.update();
        CommandScheduler.getInstance().run();
    }

    protected void addTelemetry() {
        telemetry.addLine("---");
        telemetry.addData("leftShooterVelocity", shooterSubsystem.shooterLeft.getVelocity());
        telemetry.addData("rightShooterVelocity", shooterSubsystem.shooterRight.getVelocity());
        telemetry.addData("preShooterVelocity", shooterSubsystem.preShooter.getVelocity());
        telemetry.addData("ascentVelocity", shooterSubsystem.ascentMotor.getVelocity());
    }

    protected double getShooterShootPower() {
        return Constants.SHOOTER_SHOOT_POW.value;
    }

    protected double getShooterIdlePower() {
        return Constants.SHOOTER_IDLE_POW.value;
    }

    protected double getTransferPower() {
        return Constants.TRANSFER_POW.value;
    }

    protected double getIntakePower() {
        return Constants.INTAKE_PWR.value;
    }

    protected double getLaLaPower() {
        return Constants.LALA_PWR.value;
    }

    protected double getDriveFastMultiplier() {
        return Constants.DRIVE_FAST_MULTIPLIER.value;
    }

    protected double getDriveSlowMultiplier() {
        return Constants.DRIVE_SLOW_MULTIPLIER.value;
    }
}

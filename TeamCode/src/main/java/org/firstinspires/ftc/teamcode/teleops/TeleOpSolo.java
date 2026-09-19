package org.firstinspires.ftc.teamcode.teleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.Constants;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem.ExtensionState;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem.IntakeState;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem.ShooterState;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem.TransferState;
import org.firstinspires.ftc.teamcode.utils.CommandOpmodeEx;


@TeleOp(group = "0-competition", name = "TeleOp Solo")
public class TeleOpSolo extends CommandOpmodeEx {

    protected GamepadEx gamepadEx1;
    protected GamepadEx gamepadEx2;
    protected DriveSubsystem driveSubsystem;
    protected ShooterSubsystem shooterSubsystem;
    protected IntakeSubsystem intakeSubsystem;


    @Override
    public void initialize() {

        CommandScheduler.getInstance().cancelAll();

        initializeGamepads();

        /* ---------- Subsystems ---------- */
        driveSubsystem = new DriveSubsystem(hardwareMap);
        shooterSubsystem = new ShooterSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);

        /* ---------- Drive Command ---------- */
        DriveCommand driveCommand = new DriveCommand(
                driveSubsystem,
                () -> -gamepadEx1.getLeftY(),
                () -> gamepadEx1.getRightX(),
                this::getDriveSpeedMultiplier
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
        shooterSubsystem.setShooterState(
                gamepadEx1.getButton(GamepadKeys.Button.A)
                        ? ShooterState.SHOOTING
                        : ShooterState.STOPPED
        );

        if (gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.4) {
            shooterSubsystem.setTransferState(TransferState.FEEDING);
        } else if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP)) {
            shooterSubsystem.setTransferState(TransferState.ASCENDING);
        } else if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN)) {
            shooterSubsystem.setTransferState(TransferState.DESCENDING);
        } else {
            shooterSubsystem.setTransferState(TransferState.STOPPED);
        }

        intakeSubsystem.setIntakeState(
                gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)
                        ? IntakeState.INTAKING
                        : IntakeState.STOPPED
        );

        if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_LEFT)) {
            intakeSubsystem.setExtensionState(ExtensionState.EXTENDING);
        } else if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_RIGHT)) {
            intakeSubsystem.setExtensionState(ExtensionState.RETRACTING);
        } else {
            intakeSubsystem.setExtensionState(ExtensionState.STOPPED);
        }
    }

    protected void initializeGamepads() {
        gamepadEx1 = new GamepadEx(gamepad1);
    }

    @Override
    public void run() {
        addTelemetry();
        telemetry.update();
        CommandScheduler.getInstance().run();
    }

    protected void addTelemetry() {
        telemetry.addLine("---");
        telemetry.addData("leftShooterVelocity", shooterSubsystem.getLeftShooterVelocity());
        telemetry.addData("rightShooterVelocity", shooterSubsystem.getRightShooterVelocity());
        telemetry.addData("preShooterVelocity", shooterSubsystem.getPreShooterVelocity());
        telemetry.addData("ascentVelocity", shooterSubsystem.getAscentVelocity());
    }

    protected double getShooterTargetVel() {
        return Constants.SHOOTER_TARGET_VEL.value;
    }

    protected double getDriveSpeedMultiplier() {
        return Constants.DRIVE_SPEED_MULTIPLIER.value;
    }
}

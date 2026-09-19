package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem.ExtensionState;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem.IntakeState;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem.ShooterState;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem.TransferState;

@TeleOp(group = "0-competition", name = "TeleOp Duo")
public class TeleOpDuo extends TeleOpSolo {

    @Override
    protected void initializeGamepads() {
        super.initializeGamepads();
        gamepadEx2 = new GamepadEx(gamepad2);
    }

    @Override
    public void functionalButtons() {
        shooterSubsystem.setShooterState(
                gamepadEx2.getButton(GamepadKeys.Button.RIGHT_BUMPER)
                        ? ShooterState.SHOOTING
                        : ShooterState.STOPPED
        );

        if (gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.4) {
            shooterSubsystem.setTransferState(TransferState.FEEDING);
        } else if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP)) {
            shooterSubsystem.setTransferState(TransferState.ASCENDING);
        } else if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN)) {
            shooterSubsystem.setTransferState(TransferState.DESCENDING);
        } else {
            shooterSubsystem.setTransferState(TransferState.STOPPED);
        }

        if (gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            intakeSubsystem.setIntakeState(IntakeState.INTAKING);
        } else if (gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            intakeSubsystem.setIntakeState(IntakeState.OUTTAKING);
        } else {
            intakeSubsystem.setIntakeState(IntakeState.STOPPED);
        }

        if (gamepadEx2.getLeftY() > 0.5) {
            intakeSubsystem.setExtensionState(ExtensionState.EXTENDING);
        } else if (gamepadEx2.getLeftY() < -0.5) {
            intakeSubsystem.setExtensionState(ExtensionState.RETRACTING);
        } else {
            intakeSubsystem.setExtensionState(ExtensionState.STOPPED);
        }

        if (shooterSubsystem.getLeftShooterVelocity() > getShooterTargetVel()
                || shooterSubsystem.getRightShooterVelocity() > getShooterTargetVel()) {
            gamepad1.rumble(3000);
            gamepad2.rumble(3000);
        }
    }
    @Override
    public void run() {
        telemetry.addData("Loop Times", elapsedtime.milliseconds());
        telemetry.update();
        elapsedtime.reset();
        CommandScheduler.getInstance().run();
    }
}

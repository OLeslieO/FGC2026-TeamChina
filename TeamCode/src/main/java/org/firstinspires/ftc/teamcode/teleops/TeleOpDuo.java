package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.ButtonEx;

@TeleOp(group = "0-competition", name = "TeleOp Duo")
public class TeleOpDuo extends TeleOpSolo {

    @Override
    protected void initializeGamepads() {
        super.initializeGamepads();
        gamepadEx2 = new GamepadEx(gamepad2);
    }

    @Override
    public void functionalButtons() {
        new ButtonEx(() -> gamepadEx2.getButton(GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(new InstantCommand(() -> shooterSubsystem.accelerate(getShooterShootPower())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopShooter()));

        new ButtonEx(() -> gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.4)
                .whenPressed(new InstantCommand(() -> shooterSubsystem.setTransWithBlendVel(getTransferVel())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopShoot()));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(new InstantCommand(() -> intakeSubsystem.setIntakePower(getIntakePower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.setIntakePower(0)));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER))
                .whenPressed(new InstantCommand(() -> intakeSubsystem.setIntakePower(-getIntakePower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.setIntakePower(0)));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP))
                .whenPressed(new InstantCommand(() -> shooterSubsystem.setTransferPower(getTransferPower())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopTransfer()));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(new InstantCommand(() -> shooterSubsystem.setTransferPower(-getTransferPower())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopTransfer()));

        new ButtonEx(() -> gamepadEx2.getLeftY() > 0.5)
                .whenPressed(new InstantCommand(() -> intakeSubsystem.setRetractPower(getRetractPower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.setRetractPower(0)));

        new ButtonEx(() -> gamepadEx2.getLeftY() < -0.5)
                .whenPressed(new InstantCommand(() -> intakeSubsystem.setRetractPower(-getRetractPower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.setRetractPower(0)));

        new ButtonEx(() -> shooterSubsystem.shooterLeft.getVelocity() > getShooterTargetVel() ||
                           shooterSubsystem.shooterRight.getVelocity() > getShooterTargetVel())
                .whenActive(() -> {
                    gamepad1.rumble(3000);
                    gamepad2.rumble(3000);
                });
    }
    @Override
    public void run() {
        telemetry.addData("Loop Times", elapsedtime.milliseconds());
        telemetry.update();
        elapsedtime.reset();
        CommandScheduler.getInstance().run();
    }
}

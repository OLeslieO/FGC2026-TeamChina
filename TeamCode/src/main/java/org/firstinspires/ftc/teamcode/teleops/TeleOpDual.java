package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

@TeleOp(group = "0-competition", name = "TeleOp Dual")
public class TeleOpDual extends TeleOpSolo {

    @Override
    protected void initializeGamepads() {
        super.initializeGamepads();
        gamepadEx2 = new GamepadEx(gamepad2);
    }

    @Override
    public void functionalButtons() {

        // Gamepad 2 - Right Bumper
        if (gamepadEx2.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            shooterSubsystem.accelerate(getShooterShootPower());
        } else {
            shooterSubsystem.stopShooter();
        }

        // Gamepad 1 - Right Trigger > 0.4
        if (gamepadEx1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.4) {
            shooterSubsystem.setTransWithBlendVel(getTransferVel());
        } else {
            shooterSubsystem.stopShoot();
        }

        // Gamepad 1 - Right Bumper
        if (gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            intakeSubsystem.setIntakePower(getIntakePower());
        }

        // Gamepad 1 - Left Bumper
        if (gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            intakeSubsystem.setIntakePower(-getIntakePower());
        }

        // Stop intake when neither bumper is pressed
        if (!gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)
                && !gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            intakeSubsystem.setIntakePower(0);
        }

        // Gamepad 1 - D-Pad Up
        if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP)) {
            shooterSubsystem.setTransferPower(getTransferPower());
        }

        // Gamepad 1 - D-Pad Down
        if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN)) {
            shooterSubsystem.setTransferPower(-getTransferPower());
        }

        // Stop transfer when neither D-Pad button is pressed
        if (!gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP)
                && !gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN)) {
            shooterSubsystem.stopTransfer();
        }

        // Gamepad 2 - Left Stick Y > 0.5
        if (gamepadEx2.getLeftY() > 0.5) {
            intakeSubsystem.setRetractPower(getRetractPower());
        } else if (gamepadEx2.getLeftY() < -0.5) {
            intakeSubsystem.setRetractPower(-getRetractPower());
        }else {
            intakeSubsystem.setRetractPower(0);
        }

        // Shooter ready -> rumble
        if (shooterSubsystem.shooterLeft.getVelocity() > getShooterTargetVel()
                || shooterSubsystem.shooterRight.getVelocity() > getShooterTargetVel()) {
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


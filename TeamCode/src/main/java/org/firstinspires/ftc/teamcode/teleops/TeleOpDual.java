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
        if (gamepadEx2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.4) {
            shooterSubsystem.setShooterVelocity(getShooterShootVelocity());
        } else {
            shooterSubsystem.stopShooter();
        }

        /* Gamepad 1 - Right Trigger > 0.4
         * Gamepad 1 - D-Pad Up  mm
         * Gamepad 1 - D-Pad Down
         */
        if (gamepadEx2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.4) {
            shooterSubsystem.setTransWithBlendVel(getTransferVel());
        } else if (gamepadEx2.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            shooterSubsystem.setTransferVelocity(-getTransferVel());
        } else if (gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.4) {
            shooterSubsystem.setTransferPower(getTransferPower());
        } else {
            shooterSubsystem.stopShoot();
        }

        // Gamepad 1 - Right Bumper
        // Gamepad 1 - Left Bumper
        if (gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            intakeSubsystem.setIntakePower(getIntakePower());
        } else if (gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            intakeSubsystem.setIntakePower(-getIntakePower());
        } else {
            intakeSubsystem.setIntakePower(0);
        }

        // Gamepad 2 - Left Stick Y > 0.5
        if (gamepadEx2.getLeftY() > 0.5) {
            intakeSubsystem.setRetractPower(getRetractPower());
        } else if (gamepadEx2.getLeftY() < -0.5) {
            intakeSubsystem.setRetractPower(-getRetractPower());
        } else {
            intakeSubsystem.setRetractPower(0);
        }

        // Shooter ready -> rumble
        if (shooterSubsystem.shooterLeft.getVelocity() > getRumbleTargetVel()
                || shooterSubsystem.shooterRight.getVelocity() > getRumbleTargetVel()) {
            gamepad1.rumble(100);
            gamepad2.rumble(100);
        }
    }

    @Override
    public void run() {
        telemetry.addData("Loop Times", elapsedtime.milliseconds());
        telemetry.addData("shooterLMode", shooterSubsystem.shooterLeft.getMode());
        telemetry.addData("shooterRMode", shooterSubsystem.shooterRight.getMode());

        telemetry.update();
        elapsedtime.reset();

        CommandScheduler.getInstance().run();
    }
}

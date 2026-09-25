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
        // Gamepad 1 - A
        if (gamepadEx1.getButton(GamepadKeys.Button.A)) {
            shooterSubsystem.accelerate(getShooterShootPower());
        } else {
            shooterSubsystem.stopShooter();
        }

        /* Gamepad 1 - Left Trigger > 0.4
         * Gamepad 1 - D-Pad Up
         * Gamepad 1 - D-Pad Down
         */
        if (gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.4) {
            shooterSubsystem.setTransWithBlendVel(getTransferVel());
        } else if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP)) {
            shooterSubsystem.setTransferPower(getTransferPower());
        } else if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN)) {
            shooterSubsystem.setTransferPower(-getTransferPower());
        } else {
            shooterSubsystem.stopShoot();
        }

        // Gamepad 1 - Right Bumper
        if (gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            intakeSubsystem.setIntakePower(getIntakePower());
        } else {
            intakeSubsystem.setIntakePower(0);
        }

        /* Gamepad 1 - D-Pad Left
         * Gamepad 1 - D-Pad Right
         */
        if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_LEFT)) {
            intakeSubsystem.setRetractPower(getRetractPower());
        } else if (gamepadEx1.getButton(GamepadKeys.Button.DPAD_RIGHT)) {
            intakeSubsystem.setRetractPower(-getRetractPower());
        } else {
            intakeSubsystem.setRetractPower(0);
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
        telemetry.addData("leftShooterVelocity", shooterSubsystem.shooterLeft.getVelocity());
        telemetry.addData("rightShooterVelocity", shooterSubsystem.shooterRight.getVelocity());
        telemetry.addData("preShooterVelocity", shooterSubsystem.preShooter.getVelocity());
        telemetry.addData("ascentVelocity", shooterSubsystem.ascentMotor.getVelocity());
    }

    protected double getShooterShootPower() {
        return Constants.SHOOTER_SHOOT_POW.value;
    }

    protected double getShooterShootVelocity(){
        return Constants.SHOOTER_SHOOT_VEL.value;
    }

    protected double getShooterIdlePower() {
        return Constants.SHOOTER_IDLE_POW.value;
    }

    protected double getRumbleTargetVel() {
        return Constants.RUMBLE_TARGET_VEL.value;
    }

    protected double getTransferVel() {
        return Constants.TRANSFER_VEL.value;
    }

    protected double getTransferPower() {
        return Constants.TRANSFER_POW.value;
    }

    protected double getIntakePower() {
        return Constants.INTAKE_PWR.value;
    }

    protected double getRetractPower() {
        return Constants.RETRACT_PWR.value;
    }

    protected double getDriveSpeedMultiplier() {
        return Constants.DRIVE_SPEED_MULTIPLIER.value;
    }
}

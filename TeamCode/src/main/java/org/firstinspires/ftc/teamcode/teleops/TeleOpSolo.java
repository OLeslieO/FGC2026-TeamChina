package org.firstinspires.ftc.teamcode.teleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.ButtonEx;
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
        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.A))
                .whenPressed(new InstantCommand(() -> shooterSubsystem.accelerate(getShooterShootPower())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopShooter()));

        new ButtonEx(() -> gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.4)
                .whenPressed(new InstantCommand(() -> shooterSubsystem.setTransWithBlendVel(getTransferVel())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopShoot()));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(new InstantCommand(() -> intakeSubsystem.setIntakePower(getIntakePower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.setIntakePower(0)));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP))
                .whenPressed(new InstantCommand(() -> shooterSubsystem.setTransferPower(getTransferPower())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopTransfer()));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(new InstantCommand(() -> shooterSubsystem.setTransferPower(-getTransferPower())))
                .whenReleased(new InstantCommand(() -> shooterSubsystem.stopTransfer()));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_LEFT))
                .whenPressed(new InstantCommand(() -> intakeSubsystem.setRetractPower(getRetractPower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.setRetractPower(0)));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_RIGHT))
                .whenPressed(new InstantCommand(() -> intakeSubsystem.setRetractPower(-getRetractPower())))
                .whenReleased(new InstantCommand(() -> intakeSubsystem.setRetractPower(0)));
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

    protected double getShooterTargetVel() {
        return Constants.SHOOTER_TARGET_VEL.value;
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

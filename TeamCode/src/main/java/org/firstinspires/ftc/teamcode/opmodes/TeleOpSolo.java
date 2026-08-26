package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.AscentSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.utils.ButtonEx;


@TeleOp(group = "0-competition", name = "TeleOp Solo")
public class TeleOpSolo extends CommandOpmodeEx {

    private GamepadEx gamepadEx1;
    private DriveSubsystem driveSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private IntakeSubsystem intakeSubsystem;
    private AscentSubsystem ascentSubsystem;


    @Override
    public void initialize() {

        CommandScheduler.getInstance().cancelAll();


        gamepadEx1 = new GamepadEx(gamepad1);
        ToggleButtonReader toggleLeftBumperReader = new ToggleButtonReader(
                gamepadEx1, GamepadKeys.Button.LEFT_BUMPER
        );

        /* ---------- Subsystems ---------- */
        driveSubsystem = new DriveSubsystem(hardwareMap);
        shooterSubsystem = new ShooterSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        ascentSubsystem = new AscentSubsystem(hardwareMap);

        /* ---------- Drive Command ---------- */
        DriveCommand driveCommand = new DriveCommand(
                driveSubsystem,
                () -> gamepadEx1.getLeftY(),
                () -> gamepadEx1.getRightX(),
                () -> gamepadEx1.getButton(GamepadKeys.Button.LEFT_BUMPER)
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
        shooterSubsystem.idle();
    }

    @Override
    public void functionalButtons() {
        new ButtonEx(()-> gamepadEx1.getButton(GamepadKeys.Button.A))
                .whenPressed(new InstantCommand(()-> shooterSubsystem.accelerate()))
                .whenReleased(new InstantCommand(()-> shooterSubsystem.idle()));

        new ButtonEx(()->gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>0.4)
                .whenPressed(new InstantCommand(()-> shooterSubsystem.shoot()))
                .whenReleased(new InstantCommand(()-> shooterSubsystem.stopShoot()));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(new InstantCommand(()->intakeSubsystem.intakePower(1)))
                .whenReleased(new InstantCommand(()-> intakeSubsystem.intakePower(0)));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_UP))
                .whenPressed(new InstantCommand(()->ascentSubsystem.ascent()))
                .whenReleased(new InstantCommand(()-> ascentSubsystem.stop()));

        new ButtonEx(() -> gamepadEx1.getButton(GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(new InstantCommand(()->ascentSubsystem.descent()))
                .whenReleased(new InstantCommand(()-> ascentSubsystem.stop()));
    }

    @Override
    public void run() {
        telemetry.addData("leftShooterVelocity", shooterSubsystem.shooterLeft.getVelocity());
        telemetry.addData("rightShooterVelocity", shooterSubsystem.shooterRight.getVelocity());
        telemetry.addData("preShooterVelocity", shooterSubsystem.preShooter.getVelocity());
        telemetry.update();
        CommandScheduler.getInstance().run();
    }
}

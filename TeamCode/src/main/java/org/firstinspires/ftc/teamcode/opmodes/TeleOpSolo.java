package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;
import com.seattlesolvers.solverslib.geometry.Vector2d;

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

    private ShooterSubsystem shooter;
    private IntakeSubsystem intakeSubsystem;
    private AscentSubsystem ascent;



    @Override
    public void initialize() {

        CommandScheduler.getInstance().cancelAll();


        gamepadEx1 = new GamepadEx(gamepad1);
        ToggleButtonReader toggleLeftBumperReader = new ToggleButtonReader(
                gamepadEx1, GamepadKeys.Button.LEFT_BUMPER
        );

        /* ---------- Subsystems ---------- */
        driveSubsystem = new DriveSubsystem(hardwareMap);
        shooter = new ShooterSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        ascent = new AscentSubsystem(hardwareMap);



        /* ---------- Drive Command ---------- */
        DriveCommand driveCommand = new DriveCommand(
                driveSubsystem,
                () -> gamepadEx1.getLeftX(),
                () -> gamepadEx1.getRightX(),
                () -> gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER)
        );



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
        shooter.idle();
    }

    @Override
    public void functionalButtons() {
        new ButtonEx(()-> gamepadEx1.getButton(GamepadKeys.Button.A))
                .whenPressed(new InstantCommand(()->shooter.accelerate()))
                .whenReleased(new InstantCommand(()->shooter.idle()));


        new ButtonEx(()->gamepadEx1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)>0.5)
                .whenPressed(new InstantCommand(()-> shooter.shoot()))
                .whenReleased(new InstantCommand(()-> shooter.stopShoot()));

        new ButtonEx(() ->
                gamepadEx1.getButton(GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(new InstantCommand(()->intakeSubsystem.intakePower(1)))
                .whenReleased(new InstantCommand(()-> intakeSubsystem.intakePower(0)));


    }

    @Override
    public void run() {



        CommandScheduler.getInstance().run();


    }
}

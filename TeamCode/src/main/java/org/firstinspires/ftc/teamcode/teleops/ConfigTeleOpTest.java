package org.firstinspires.ftc.teamcode.teleops;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ButtonEx;
import org.firstinspires.ftc.teamcode.Hardwares;
import org.firstinspires.ftc.teamcode.XKCommandOpmode;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Config
@TeleOp(name = "ConfigTeleOpTest", group = "test")
public class ConfigTeleOpTest extends XKCommandOpmode {
    public static double preShooterPower = 1.0;

    private Hardwares hardwares;
    private Shooter shooter;
    private Intake intake;
    private Gate gate;
    private Drive drive;
    private GamepadEx gamepad1;
    private Drive.ArcadeDriveCommand arcadeDriveCommand;
    private MultipleTelemetry multipleTelemetry;

    @Override
    public void initialize() {
        this.multipleTelemetry =  new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        this.gamepad1 = new GamepadEx(super.gamepad1);
        CommandScheduler.getInstance().reset();

        hardwares = new Hardwares(hardwareMap);
        shooter = new Shooter(hardwares);
        intake = new Intake(hardwares);
        gate = new Gate(hardwares);
        drive = new Drive(hardwares);

        arcadeDriveCommand = new Drive.ArcadeDriveCommand(
                drive,
                () -> -gamepad1.getLeftY(),
                () -> gamepad1.getRightX(),
                0.8
        );

        CommandScheduler.getInstance().schedule(arcadeDriveCommand);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void run() {
        CommandScheduler.getInstance().run();

        shooter.run();

        this.multipleTelemetry.addLine("---");
        Shooter.TelemetryState shooterState = shooter.getTelemetryState();
        this.multipleTelemetry.addData("shooter left power", shooterState.leftPower);
        this.multipleTelemetry.addData("shooter right power", shooterState.rightPower);
        this.multipleTelemetry.addData("shooter left current", shooterState.leftCurrent);
        this.multipleTelemetry.addData("shooter right current", shooterState.rightCurrent);

        this.multipleTelemetry.addData("pre-shooter velocity", shooterState.preShooterVelocity);
        this.multipleTelemetry.addData("pre-shooter current", shooterState.preShooterCurrent);

        this.multipleTelemetry.update();

        if (gamepad1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1) {
            shooter.setPreShooterPower(preShooterPower);
        } else {
            CommandScheduler.getInstance().schedule(shooter.stopPreShooter());
        }
    }


    @Override
    public void functionalButtons() {
        new ButtonEx(
                () -> gamepad1.getButton(GamepadKeys.Button.DPAD_UP)
        ).whenPressed(
                gate.forward()
        ).whenReleased(
                gate.stop()
        );

        new ButtonEx(
                () -> gamepad1.getButton(GamepadKeys.Button.DPAD_DOWN)
        ).whenPressed(
                gate.reverse()
        ).whenReleased(
                gate.stop()
        );

        new ButtonEx(
                () -> gamepad1.getButton(GamepadKeys.Button.A)
        ).toggleWhenPressed(
                shooter.runShooter(),
                shooter.stopShooter()
        );

//        new ButtonEx(
//                () -> gamepad1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1
//        ).whenPressed(
//                shooter.runPreShooter()
//        ).whenReleased(
//                shooter.stopPreShooter()
//        );

        new ButtonEx(
                () -> gamepad1.getButton(GamepadKeys.Button.RIGHT_BUMPER)
        ).whenPressed(
                intake.startIntake()
        ).whenReleased(
                intake.stopIntake()
        );
    }
}

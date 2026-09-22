package org.firstinspires.ftc.teamcode.algorithms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@Config
@TeleOp(name = "Shooter Bang-Bang Test", group = "algorithms")
public class ShooterBangBangTest extends LinearOpMode {
    public static double shooterTargetVelocity = Constants.SHOOTER_SHOOT_VEL.value;
    public static double shooterIdleVelocity = 1000;
    public static double fullPower = 1.0;
    public static double holdPower = 0.5;
    public static double deadband = 40;
    public static double transferVelocity = Constants.TRANSFER_VEL.value;
    public static double transferPower = Constants.TRANSFER_POW.value;
    public static double retractPower = Constants.RETRACT_PWR.value;
    private MultipleTelemetry telemetryM;
    private boolean previousDpadDown;
    private boolean recordingError;
    private double errorSum;
    private int errorSamples;
    private double averageError = Double.NaN;

    @Override
    public void runOpMode() {
        ShooterSubsystem shooterSubsystem = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap);

        telemetryM = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetryM.addLine("Bang-Bang shooter algorithm ready.");
        telemetryM.update();

        waitForStart();

        while (opModeIsActive()) {
            double targetVelocity = gamepad1.right_bumper ? shooterTargetVelocity : shooterIdleVelocity;
            double currentVelocity = getAverageShooterVelocity(shooterSubsystem);
            double error = targetVelocity - currentVelocity;
            updateErrorRecording(error);
            double output = currentVelocity < targetVelocity - deadband ? fullPower : holdPower;

            shooterSubsystem.accelerate(clipPower(output));
            runTransferBinding(shooterSubsystem);
            runRetractBinding(intakeSubsystem);
            addTelemetry(shooterSubsystem, targetVelocity, output, error);

            idle();
        }

        shooterSubsystem.stopShooter();
        shooterSubsystem.stopShoot();
        intakeSubsystem.setRetractPower(0);
    }

    private double getAverageShooterVelocity(ShooterSubsystem shooterSubsystem) {
        return (shooterSubsystem.shooterLeft.getVelocity()
                + shooterSubsystem.shooterRight.getVelocity()) / 2.0;
    }

    private void runTransferBinding(ShooterSubsystem shooterSubsystem) {
        if (gamepad1.right_trigger > 0.4) {
            shooterSubsystem.setTransWithBlendVel(transferVelocity);
        } else {
            shooterSubsystem.stopShoot();
        }
    }

    private void runRetractBinding(IntakeSubsystem intakeSubsystem) {
        if (gamepad1.left_stick_y > 0.5) {
            intakeSubsystem.setRetractPower(retractPower);
        } else if (gamepad1.left_stick_y < -0.5) {
            intakeSubsystem.setRetractPower(-retractPower);
        } else {
            intakeSubsystem.setRetractPower(0);
        }
    }

    private void addTelemetry(ShooterSubsystem shooterSubsystem, double targetVelocity,
                              double output, double error) {
        telemetryM.addData("Algorithm", "Bang-Bang");
        telemetryM.addData("Target velocity", targetVelocity);
        telemetryM.addData("Output power", output);
        telemetryM.addData("Left velocity", shooterSubsystem.shooterLeft.getVelocity());
        telemetryM.addData("Right velocity", shooterSubsystem.shooterRight.getVelocity());
        telemetryM.addData("Error", error);
        telemetryM.addData("Error recording", recordingError ? "Recording" : "Stopped");
        telemetryM.addData("Error samples", errorSamples);
        telemetryM.addData("Average error", errorSamples == 0 ? "N/A" : averageError);
        telemetryM.update();
    }

    private void updateErrorRecording(double error) {
        boolean dpadDownPressed = gamepad1.dpad_down && !previousDpadDown;
        previousDpadDown = gamepad1.dpad_down;

        if (dpadDownPressed) {
            recordingError = !recordingError;
            if (recordingError) {
                errorSum = 0;
                errorSamples = 0;
                averageError = Double.NaN;
            } else if (errorSamples > 0) {
                averageError = errorSum / errorSamples;
            }
        }

        if (recordingError) {
            errorSum += error;
            errorSamples++;
        }
    }

    private double clipPower(double power) {
        return Math.max(0.0, Math.min(1.0, power));
    }
}

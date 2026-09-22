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
@TeleOp(name = "Shooter TBH Test", group = "algorithms")
public class ShooterTakeBackHalfTest extends LinearOpMode {
    public static double shooterTargetVelocity = Constants.SHOOTER_SHOOT_VEL.value;
    public static double shooterIdleVelocity = 1000;
    public static double gain = 0.02;
    public static double shootInitialGuess = 0.85;
    public static double idleInitialGuess = 0.4;
    public static double transferVelocity = Constants.TRANSFER_VEL.value;
    public static double transferPower = Constants.TRANSFER_POW.value;
    public static double retractPower = Constants.RETRACT_PWR.value;

    private double output;
    private double tbh;
    private double lastError;
    private double lastTargetVelocity = Double.NaN;
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
        telemetryM.addLine("Take Back Half shooter algorithm ready.");
        telemetryM.update();

        waitForStart();

        while (opModeIsActive()) {
            double targetVelocity = gamepad1.right_bumper ? shooterTargetVelocity : shooterIdleVelocity;
            resetOnTargetChange(targetVelocity);

            double currentVelocity = getAverageShooterVelocity(shooterSubsystem);
            double error = targetVelocity - currentVelocity;
            updateErrorRecording(error);

            output += gain * error;
            output = clipPower(output);

            if (crossedZero(error, lastError)) {
                output = 0.5 * (output + tbh);
                tbh = output;
            }
            lastError = error;

            shooterSubsystem.accelerate(output);
            runTransferBinding(shooterSubsystem);
            runRetractBinding(intakeSubsystem);
            addTelemetry(shooterSubsystem, targetVelocity, output, tbh, error);

            idle();
        }

        shooterSubsystem.stopShooter();
        shooterSubsystem.stopShoot();
        intakeSubsystem.setRetractPower(0);
    }

    private void resetOnTargetChange(double targetVelocity) {
        if (targetVelocity == lastTargetVelocity) {
            return;
        }

        output = targetVelocity == shooterTargetVelocity ? shootInitialGuess : idleInitialGuess;
        tbh = output;
        lastError = 0;
        lastTargetVelocity = targetVelocity;
    }

    private boolean crossedZero(double error, double previousError) {
        return (error > 0 && previousError < 0) || (error < 0 && previousError > 0);
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
                              double output, double tbh, double error) {
        telemetryM.addData("Algorithm", "Take Back Half");
        telemetryM.addData("Target velocity", targetVelocity);
        telemetryM.addData("Output power", output);
        telemetryM.addData("TBH", tbh);
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

package org.firstinspires.ftc.teamcode.algorithms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

/** Shared hardware setup, driver bindings, and telemetry for shooter algorithm tests. */
public abstract class ShooterTestBase extends LinearOpMode {
    protected ShooterSubsystem shooterSubsystem;
    protected IntakeSubsystem intakeSubsystem;
    protected MultipleTelemetry telemetryM;

    private boolean previousRecordButton;
    private boolean recordingError;
    private double errorSum;
    private int errorSamples;
    private double averageError = Double.NaN;

    protected void initializeShooterTest(String readyMessage) {
        shooterSubsystem = new ShooterSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        telemetryM = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetryM.addLine(readyMessage);
        telemetryM.update();
    }

    protected double getAverageShooterVelocity() {
        return (shooterSubsystem.shooterLeft.getVelocity()
                + shooterSubsystem.shooterRight.getVelocity()) / 2.0;
    }

    protected void runTransferBinding(double transferVelocity) {
        if (gamepad1.left_trigger_pressed) {
            shooterSubsystem.setTransWithBlendVel(transferVelocity);
        } else if (gamepad1.left_bumper) {
            shooterSubsystem.setTransferVelocity(-transferVelocity);
        } else {
            shooterSubsystem.stopShoot();
        }
    }

    protected void runRetractBinding() {
        intakeSubsystem.setRetractPower(-gamepad1.left_stick_y * Constants.RETRACT_PWR.value);
    }

    protected void addCommonTelemetry(String algorithm, double targetVelocity, double output,
                                      double error) {
        telemetryM.addData("Algorithm", algorithm);
        telemetryM.addData("Target velocity", targetVelocity);
        telemetryM.addData("Output power", output);
        telemetryM.addData("Left velocity", shooterSubsystem.shooterLeft.getVelocity());
        telemetryM.addData("Right velocity", shooterSubsystem.shooterRight.getVelocity());
        telemetryM.addData("Error", error);
        telemetryM.addData("Error recording", recordingError ? "Recording" : "Stopped");
        telemetryM.addData("Error samples", errorSamples);
        telemetryM.addData("Average error", errorSamples == 0 ? "N/A" : averageError);
    }

    protected void updateErrorRecording(double error) {
        boolean recordButtonPressed = gamepad1.a && !previousRecordButton;
        previousRecordButton = gamepad1.a;

        if (recordButtonPressed) {
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

    protected void stopShooterTest() {
        shooterSubsystem.stopShooter();
        shooterSubsystem.stopShoot();
        intakeSubsystem.setRetractPower(0);
    }

    protected double clipPower(double power, double minimum) {
        return Math.max(minimum, Math.min(1.0, power));
    }
}

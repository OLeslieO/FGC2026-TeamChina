package org.firstinspires.ftc.teamcode.algorithms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Constants;

@Config
@TeleOp(name = "Shooter TBH Test", group = "algorithms")
public class ShooterTakeBackHalfTest extends ShooterTestBase {
    public static double shooterTargetVelocity = Constants.SHOOTER_SHOOT_VEL.value;
    public static double shooterIdleVelocity = 1000;
    public static double gain = 0.02;
    public static double shootInitialGuess = 0.85;
    public static double idleInitialGuess = 0.4;
    public static double transferVelocity = Constants.TRANSFER_VEL.value;
    public static double retractPower = Constants.RETRACT_PWR.value;

    private double output;
    private double tbh;
    private double lastError;
    private double lastTargetVelocity = Double.NaN;
    @Override
    public void runOpMode() {
        initializeShooterTest("Take Back Half shooter algorithm ready.");

        waitForStart();

        while (opModeIsActive()) {
            double targetVelocity = gamepad1.right_trigger_pressed ? shooterTargetVelocity : shooterIdleVelocity;
            resetOnTargetChange(targetVelocity);

            double currentVelocity = getAverageShooterVelocity();
            double error = targetVelocity - currentVelocity;
            updateErrorRecording(error);

            output += gain * error;
            output = clipPower(output, 0.0);

            if (crossedZero(error, lastError)) {
                output = 0.5 * (output + tbh);
                tbh = output;
            }
            lastError = error;

            shooterSubsystem.accelerate(output);
            runTransferBinding(transferVelocity);
            runRetractBinding();
            addCommonTelemetry("Take Back Half", targetVelocity, output, error);
            telemetryM.addData("TBH", tbh);
            telemetryM.update();

            idle();
        }

        stopShooterTest();
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

}

package org.firstinspires.ftc.teamcode.algorithms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Constants;

@Config
@TeleOp(name = "Shooter PID Test", group = "algorithms")
public class ShooterPIDTest extends ShooterTestBase {
    public static double shooterTargetVelocity = Constants.SHOOTER_SHOOT_VEL.value;
    public static double shooterIdleVelocity = 1000;
    public static double shooterP = 0.12;
    public static double shooterI = 0.008;
    public static double shooterD = 0.0;
    public static double transferVelocity = Constants.TRANSFER_VEL.value;

    private double integral;
    private double lastError;
    private double lastTargetVelocity = Double.NaN;
    @Override
    public void runOpMode() {
        ElapsedTime loopTimer = new ElapsedTime();
        initializeShooterTest("PID shooter algorithm ready.");

        waitForStart();
        loopTimer.reset();

        while (opModeIsActive()) {
            double targetVelocity = gamepad1.right_trigger_pressed ? shooterTargetVelocity : shooterIdleVelocity;
            resetOnTargetChange(targetVelocity);
            double currentVelocity = getAverageShooterVelocity();
            double dt = Math.max(loopTimer.seconds(), 0.001);
            loopTimer.reset();

            double error = (targetVelocity - currentVelocity) / 20;
            updateErrorRecording(error);
            integral += error * dt;
            double derivative = (error - lastError) / dt;
            lastError = error;

            double output = shooterP * error
                    + shooterI * integral
                    + shooterD * derivative;
            shooterSubsystem.accelerate(clipPower(output, -1.0));

            runTransferBinding(transferVelocity);
            runRetractBinding();
            addCommonTelemetry("PID", targetVelocity, clipPower(output, -1.0), error);
            telemetryM.update();

            idle();
        }

        stopShooterTest();
    }

    private void resetOnTargetChange(double targetVelocity) {
        if (targetVelocity == lastTargetVelocity) {
            return;
        }

        integral = 0;
        lastError = 0;
        lastTargetVelocity = targetVelocity;
    }

}

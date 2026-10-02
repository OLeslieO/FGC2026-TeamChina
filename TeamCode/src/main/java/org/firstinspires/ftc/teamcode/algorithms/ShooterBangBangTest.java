package org.firstinspires.ftc.teamcode.algorithms;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Constants;

@Config
@TeleOp(name = "Shooter Bang-Bang Test", group = "algorithms")
public class ShooterBangBangTest extends ShooterTestBase {
    public static double shooterTargetVelocity = Constants.SHOOTER_SHOOT_VEL.value;
    public static double shooterIdleVelocity = 1000;
    public static double fullPower = 1.0;
    public static double holdPower = 0.5;
    public static double deadband = 40;
    public static double transferVelocity = Constants.TRANSFER_VEL.value;
    public static double retractPower = Constants.RETRACT_PWR.value;
    @Override
    public void runOpMode() {
        initializeShooterTest("Bang-Bang shooter algorithm ready.");

        waitForStart();

        while (opModeIsActive()) {
            double targetVelocity = gamepad1.right_trigger_pressed ? shooterTargetVelocity : shooterIdleVelocity;
            double currentVelocity = getAverageShooterVelocity();
            double error = targetVelocity - currentVelocity;
            updateErrorRecording(error);
            double output = currentVelocity < targetVelocity - deadband ? fullPower : holdPower;

            shooterSubsystem.accelerate(clipPower(output, 0.0));
            runTransferBinding(transferVelocity);
            runRetractBinding();
            addCommonTelemetry("Bang-Bang", targetVelocity, output, error);
            telemetryM.update();

            idle();
        }

        stopShooterTest();
    }
}

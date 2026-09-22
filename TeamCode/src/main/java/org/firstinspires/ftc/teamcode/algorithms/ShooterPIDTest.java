package org.firstinspires.ftc.teamcode.algorithms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@Config
@TeleOp(name = "Shooter PID Test", group = "algorithms")
public class ShooterPIDTest extends LinearOpMode {
    public static double shooterTargetVelocity = Constants.SHOOTER_SHOOT_VEL.value;
    public static double shooterIdleVelocity = 700;
    public static double shooterP = 0.0062;
    public static double shooterI = 0.001;
    public static double shooterD = 0.00002;
    public static double transferVelocity = Constants.TRANSFER_VEL.value;
    public static double transferPower = Constants.TRANSFER_POW.value;
    public static double retractPower = Constants.RETRACT_PWR.value;

    private double integral;
    private double lastError;
    private double lastTargetVelocity = Double.NaN;
    private MultipleTelemetry telemetryM;

    @Override
    public void runOpMode() {
        ShooterSubsystem shooterSubsystem = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap);
        ElapsedTime loopTimer = new ElapsedTime();

        telemetryM = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetryM.addLine("PID shooter algorithm ready.");
        telemetryM.update();

        waitForStart();
        loopTimer.reset();

        while (opModeIsActive()) {
            double targetVelocity = gamepad1.right_bumper ? shooterTargetVelocity : shooterIdleVelocity;
            resetOnTargetChange(targetVelocity);
            double currentVelocity = getAverageShooterVelocity(shooterSubsystem);
            double dt = Math.max(loopTimer.seconds(), 0.001);
            loopTimer.reset();

            double error = targetVelocity - currentVelocity;
            integral += error * dt;
            double derivative = (error - lastError) / dt;
            lastError = error;

            double output = shooterP * error
                    + shooterI * integral
                    + shooterD * derivative;
            shooterSubsystem.accelerate(clipPower(output));

            runTransferBinding(shooterSubsystem);
            runRetractBinding(intakeSubsystem);
            addTelemetry(shooterSubsystem, "PID", targetVelocity, output);

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

    private void resetOnTargetChange(double targetVelocity) {
        if (targetVelocity == lastTargetVelocity) {
            return;
        }

        integral = 0;
        lastError = 0;
        lastTargetVelocity = targetVelocity;
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

    private void addTelemetry(ShooterSubsystem shooterSubsystem, String algorithm,
                              double targetVelocity, double output) {
        telemetryM.addData("Algorithm", algorithm);
        telemetryM.addData("Target velocity", targetVelocity);
        telemetryM.addData("Output power", output);
        telemetryM.addData("Left velocity", shooterSubsystem.shooterLeft.getVelocity());
        telemetryM.addData("Right velocity", shooterSubsystem.shooterRight.getVelocity());
        telemetryM.addData("Error", targetVelocity - getAverageShooterVelocity(shooterSubsystem));
        telemetryM.update();
    }

    private double clipPower(double power) {
        return Math.max(0.0, Math.min(1.0, power));
    }
}

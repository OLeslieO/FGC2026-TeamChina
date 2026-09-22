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
@TeleOp(name = "Shooter TBH Algorithm", group = "algorithms")
public class ShooterTakeBackHalfAlgorithm extends LinearOpMode {
    public static double shooterTargetVelocity = Constants.SHOOTER_SHOOT_VEL.value;
    public static double shooterIdleVelocity = 700;
    public static double gain = 0.00018;
    public static double shootInitialGuess = 0.75;
    public static double idleInitialGuess = 0.25;
    public static double transferVelocity = Constants.TRANSFER_VEL.value;
    public static double transferPower = Constants.TRANSFER_POW.value;
    public static double retractPower = Constants.RETRACT_PWR.value;

    private double output;
    private double tbh;
    private double lastError;
    private double lastTargetVelocity = Double.NaN;

    @Override
    public void runOpMode() {
        ShooterSubsystem shooterSubsystem = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addLine("Take Back Half shooter algorithm ready.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double targetVelocity = gamepad2.right_bumper ? shooterTargetVelocity : shooterIdleVelocity;
            resetOnTargetChange(targetVelocity);

            double currentVelocity = getAverageShooterVelocity(shooterSubsystem);
            double error = targetVelocity - currentVelocity;

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
            addTelemetry(shooterSubsystem, targetVelocity, output, tbh);

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
        } else if (gamepad1.left_trigger > 0.4) {
            shooterSubsystem.setTransferPower(transferPower);
        } else if (gamepad2.dpad_down) {
            shooterSubsystem.setTransferPower(-transferPower);
        } else {
            shooterSubsystem.stopShoot();
        }
    }

    private void runRetractBinding(IntakeSubsystem intakeSubsystem) {
        if (gamepad2.left_stick_y > 0.5) {
            intakeSubsystem.setRetractPower(retractPower);
        } else if (gamepad2.left_stick_y < -0.5) {
            intakeSubsystem.setRetractPower(-retractPower);
        } else {
            intakeSubsystem.setRetractPower(0);
        }
    }

    private void addTelemetry(ShooterSubsystem shooterSubsystem, double targetVelocity,
                              double output, double tbh) {
        telemetry.addData("Algorithm", "Take Back Half");
        telemetry.addData("Target velocity", targetVelocity);
        telemetry.addData("Output power", output);
        telemetry.addData("TBH", tbh);
        telemetry.addData("Left velocity", shooterSubsystem.shooterLeft.getVelocity());
        telemetry.addData("Right velocity", shooterSubsystem.shooterRight.getVelocity());
        telemetry.addData("Error", targetVelocity - getAverageShooterVelocity(shooterSubsystem));
        telemetry.update();
    }

    private double clipPower(double power) {
        return Math.max(0.0, Math.min(1.0, power));
    }
}

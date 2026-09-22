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
@TeleOp(name = "Shooter Bang-Bang Algorithm", group = "algorithms")
public class ShooterBangBangAlgorithm extends LinearOpMode {
    public static double shooterTargetVelocity = Constants.SHOOTER_SHOOT_VEL.value;
    public static double shooterIdleVelocity = 700;
    public static double fullPower = 1.0;
    public static double holdPower = 0.35;
    public static double deadband = 30;
    public static double transferVelocity = Constants.TRANSFER_VEL.value;
    public static double transferPower = Constants.TRANSFER_POW.value;
    public static double retractPower = Constants.RETRACT_PWR.value;

    @Override
    public void runOpMode() {
        ShooterSubsystem shooterSubsystem = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addLine("Bang-Bang shooter algorithm ready.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double targetVelocity = gamepad2.right_bumper ? shooterTargetVelocity : shooterIdleVelocity;
            double currentVelocity = getAverageShooterVelocity(shooterSubsystem);
            double output = currentVelocity < targetVelocity - deadband ? fullPower : holdPower;

            shooterSubsystem.accelerate(clipPower(output));
            runTransferBinding(shooterSubsystem);
            runRetractBinding(intakeSubsystem);
            addTelemetry(shooterSubsystem, targetVelocity, output);

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

    private void addTelemetry(ShooterSubsystem shooterSubsystem, double targetVelocity, double output) {
        telemetry.addData("Algorithm", "Bang-Bang");
        telemetry.addData("Target velocity", targetVelocity);
        telemetry.addData("Output power", output);
        telemetry.addData("Left velocity", shooterSubsystem.shooterLeft.getVelocity());
        telemetry.addData("Right velocity", shooterSubsystem.shooterRight.getVelocity());
        telemetry.addData("Error", targetVelocity - getAverageShooterVelocity(shooterSubsystem));
        telemetry.update();
    }

    private double clipPower(double power) {
        return Math.max(0.0, Math.min(1.0, power));
    }
}

package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Constants;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@Config
@TeleOp(name = "Shooter PIDF Test", group = "test")
public class ShooterPIDTest extends LinearOpMode {
    public static double shooterP = Constants.SHOOTER_PIDF_P.value;
    public static double shooterI = Constants.SHOOTER_PIDF_I.value;
    public static double shooterD = Constants.SHOOTER_PIDF_D.value;
    public static double shooterF = Constants.SHOOTER_PIDF_F.value;
    public static double shooterVelocity = Constants.SHOOTER_TARGET_VEL.value;
    public static double preShooterVelocity = Constants.TRANSFER_VEL.value;
    public static boolean runShooter = false;
    public static boolean runPreShooter = false;

    @Override
    public void runOpMode() {
        ShooterSubsystem shooterSubsystem = new ShooterSubsystem(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.addLine("Enable runShooter or runPreShooter in FTC Dashboard.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            shooterSubsystem.setShooterPIDF(shooterP, shooterI, shooterD, shooterF);

            if (runShooter) {
                shooterSubsystem.setShooterVelocity(shooterVelocity);
            } else {
                shooterSubsystem.stopShooter();
            }

            if (runPreShooter) {
                shooterSubsystem.preShooter.setVelocity(preShooterVelocity);
            } else {
                shooterSubsystem.preShooter.setPower(0);
            }

            telemetry.addData("Shooter target", shooterVelocity);
            telemetry.addData("Shooter left velocity", shooterSubsystem.shooterLeft.getVelocity());
            telemetry.addData("Shooter right velocity", shooterSubsystem.shooterRight.getVelocity());
            telemetry.addData("Pre-shooter target", preShooterVelocity);
            telemetry.addData("Pre-shooter velocity", shooterSubsystem.preShooter.getVelocity());
            telemetry.addData("Shooter PIDF", "P %.3f I %.3f D %.3f F %.3f",
                    shooterP, shooterI, shooterD, shooterF);
            telemetry.update();
            idle();
        }

        shooterSubsystem.stopShooter();
        shooterSubsystem.preShooter.setPower(0);
    }
}

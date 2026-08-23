package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "FGC2026teleop test", group = "Robot")
public class FGC2026TeleopTest extends LinearOpMode {

    // Motors
    public DcMotor laLa = null;

    public DcMotor preShooter = null;
    public DcMotor intake = null;
    public DcMotor driveLeft = null;
    public DcMotor driveRight = null;

    public DcMotorEx shooterLeft = null;
    public DcMotorEx shooterRight = null;

    // Limit Switch
    public DigitalChannel arm_down_limit;

    // Variables
    public boolean last_state_b = false;
    public boolean lock = false;

    @Override
    public void runOpMode() {

        // Hardware Mapping
        laLa = hardwareMap.get(DcMotor.class, "laLa");

        preShooter = hardwareMap.get(DcMotor.class, "preShooter");
        intake = hardwareMap.get(DcMotor.class, "intake");
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");

        shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");


        // Motor Directions
        laLa.setDirection(DcMotor.Direction.FORWARD);

        shooterLeft.setDirection(DcMotor.Direction.FORWARD);
        shooterRight.setDirection(DcMotor.Direction.REVERSE);

        intake.setDirection(DcMotor.Direction.REVERSE);
        preShooter.setDirection(DcMotor.Direction.REVERSE);

        driveLeft.setDirection(DcMotor.Direction.REVERSE);
        driveRight.setDirection(DcMotor.Direction.FORWARD);

        // Encoder Mode
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Robot Ready!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // =========================
            // Tank Drive
            // =========================
            double speedMultiplier;
            if(gamepad1.dpad_up){
                laLa.setPower(1);

            }else if(gamepad1.dpad_down){
                laLa.setPower(-1);
            }else{
                laLa.setPower(0);
            }

            if (gamepad1.left_bumper) {
                speedMultiplier = 0.4;      // Slow Mode
            } else {
                speedMultiplier = 1.0;      // Full Speed
            }

            // =========================
            // Arcade Drive
            // =========================

            double drive = -gamepad1.left_stick_y;   // 前后移动
            double turn = gamepad1.right_stick_x;    // 左右旋转


            // 慢速模式

            if (gamepad1.left_bumper) {
                speedMultiplier = 0.4;
            } else {
                speedMultiplier = 1.0;
            }


            // 混合计算
            double leftPower = (drive + turn) * speedMultiplier;
            double rightPower = (drive - turn) * speedMultiplier;


            // 防止超过最大功率
            double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));

            if(max > 1.0){
                leftPower /= max;
                rightPower /= max;
            }


            driveLeft.setPower(leftPower);
            driveRight.setPower(rightPower);

            // =========================
            // Shooter
            // =========================
            if (gamepad1.a) {
                shooterLeft.setPower(1.0);
                shooterRight.setPower(1.0);
            } else {
                shooterLeft.setPower(0);
                shooterRight.setPower(0);

                shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            // =========================
            // PreShooter
            // =========================
            if (gamepad1.right_trigger > 0.1) {
                preShooter.setPower(1.0);
            } else {
                preShooter.setPower(0);
            }

            // =========================
            // Intake
            // =========================
            if (gamepad1.right_bumper) {
                intake.setPower(1.0);
            } else {
                intake.setPower(0);
            }

            // =========================
            // Limit Switch
            // =========================

            // =========================
            // Telemetry
            // =========================
            telemetry.addData("Shooter Speed", "%.2f", shooterLeft.getVelocity());
            telemetry.addData("Shooter1 Speed", "%.2f", shooterRight.getVelocity());

            telemetry.addData("Left Drive Power", "%.2f", driveLeft.getPower());
            telemetry.addData("Right Drive Power", "%.2f", driveRight.getPower());


            telemetry.addData("gamepad1.b", gamepad1.b);
            telemetry.addData("lock", lock);
            telemetry.addData("last_state_b", last_state_b);

            telemetry.update();
        }
    }
}
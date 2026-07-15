package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "FGC2026teleop test", group = "Robot")
public class FGC2026TeleopTest extends LinearOpMode {

    // Motors
    public DcMotor lala = null;

    public DcMotor preShooter = null;
    public DcMotor intake = null;
    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;

    public DcMotorEx shooter = null;
    public DcMotorEx shooter1 = null;

    // Limit Switch
    public DigitalChannel arm_down_limit;

    // Variables
    public boolean last_state_b = false;
    public boolean lock = false;

    @Override
    public void runOpMode() {

        // Hardware Mapping
        lala = hardwareMap.get(DcMotor.class, "lala");

        preShooter = hardwareMap.get(DcMotor.class, "preShooter");
        intake = hardwareMap.get(DcMotor.class, "intake");
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

        shooter = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooterRight");


        // Motor Directions
        lala.setDirection(DcMotor.Direction.FORWARD);

        shooter.setDirection(DcMotor.Direction.FORWARD);
        shooter1.setDirection(DcMotor.Direction.REVERSE);

        intake.setDirection(DcMotor.Direction.REVERSE);
        preShooter.setDirection(DcMotor.Direction.REVERSE);

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Encoder Mode
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Robot Ready!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // =========================
            // Tank Drive
            // =========================
            double speedMultiplier;
            if(gamepad1.dpad_up){
                lala.setPower(1);

            }else if(gamepad1.dpad_down){
                lala.setPower(-1);
            }else{
                lala.setPower(0);
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


            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            // =========================
            // Shooter
            // =========================
            if (gamepad1.a) {
                shooter.setPower(1.0);
                shooter1.setPower(1.0);
            } else {
                shooter.setPower(0);
                shooter1.setPower(0);

                shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
            telemetry.addData("Shooter Speed", "%.2f", shooter.getVelocity());
            telemetry.addData("Shooter1 Speed", "%.2f", shooter1.getVelocity());

            telemetry.addData("Left Drive Power", "%.2f", leftDrive.getPower());
            telemetry.addData("Right Drive Power", "%.2f", rightDrive.getPower());


            telemetry.addData("gamepad1.b", gamepad1.b);
            telemetry.addData("lock", lock);
            telemetry.addData("last_state_b", last_state_b);

            telemetry.update();
        }
    }
}
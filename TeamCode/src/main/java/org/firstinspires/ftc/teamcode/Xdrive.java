package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp(name = "X Drive - Field/Robot Centric")
public class Xdrive extends LinearOpMode {

    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;

    private IMU imu;

    // false = Robot Centric
    // true  = Field Centric
    private boolean fieldCentric = false;

    private boolean lastToggle = false;

    @Override
    public void runOpMode() {

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        // 根据你的实际电机安装方向修改
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = hardwareMap.get(IMU.class, "imu");

        // 根据你的 Control Hub / Expansion Hub 实际安装方向修改
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;

        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        logoDirection,
                        usbDirection
                )
        ));

        telemetry.addLine("X Drive initialized");
        telemetry.addLine("Robot Centric");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // =========================
            // Toggle Field / Robot Centric
            // =========================

            boolean toggle = gamepad1.y;

            if (toggle && !lastToggle) {
                fieldCentric = !fieldCentric;
            }

            lastToggle = toggle;

            // =========================
            // Driver Input
            // =========================

            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;

            // Rotation
            double rx = gamepad1.right_stick_x;

            // =========================
            // Field Centric Transformation
            // =========================

            if (fieldCentric) {

                double heading =
                        imu.getRobotYawPitchRollAngles()
                                .getYaw(
                                        org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS
                                );

                // Rotate field-relative vector
                // into robot-relative coordinates
                double cos = Math.cos(-heading);
                double sin = Math.sin(-heading);

                double rotatedX = x * cos - y * sin;
                double rotatedY = x * sin + y * cos;

                x = rotatedX;
                y = rotatedY;
            }


            double frontLeftPower =
                    y + x + rx;

            double frontRightPower =
                    y - x - rx;

            double backLeftPower =
                    y - x + rx;

            double backRightPower =
                    y + x - rx;

            // =========================
            // Normalize
            // =========================

            double max = Math.max(
                    1.0,
                    Math.max(
                            Math.abs(frontLeftPower),
                            Math.max(
                                    Math.abs(frontRightPower),
                                    Math.max(
                                            Math.abs(backLeftPower),
                                            Math.abs(backRightPower)
                                    )
                            )
                    )
            );

            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;

            // =========================
            // Speed Control
            // =========================

            double speedMultiplier = 1;


            frontLeft.setPower(frontLeftPower * speedMultiplier);
            frontRight.setPower(frontRightPower * speedMultiplier);
            backLeft.setPower(backLeftPower * speedMultiplier);
            backRight.setPower(backRightPower * speedMultiplier);

            // =========================
            // Telemetry
            // =========================

            telemetry.addData(
                    "Mode",
                    fieldCentric ? "FIELD CENTRIC" : "ROBOT CENTRIC"
            );

            telemetry.addData(
                    "Heading",
                    "%.1f°",
                    Math.toDegrees(
                            imu.getRobotYawPitchRollAngles()
                                    .getYaw(
                                            org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS
                                    )
                    )
            );

            telemetry.addData(
                    "FL / FR",
                    "%.2f / %.2f",
                    frontLeftPower,
                    frontRightPower
            );

            telemetry.addData(
                    "BL / BR",
                    "%.2f / %.2f",
                    backLeftPower,
                    backRightPower
            );

            telemetry.update();
        }
    }
}
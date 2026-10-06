package org.firstinspires.ftc.teamcode.onbotjava;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@TeleOp(name = "OnBotJavaHelp TeleOp", group = "0-competition")
public class OnBotJavaHelpTeleOp extends LinearOpMode {

    private DcMotorEx leftDrive, rightDrive;
    private DcMotorEx shooterLeft, shooterRight, preShooter, ascentMotor;
    private DcMotorEx intake, retract;

    private static final double DRIVE_SPEED_MULTIPLIER = 1.0;
    private static final double SHOOTER_TARGET_VELOCITY = 1800;
    private static final double SHOOTER_P = 0.0062;
    private static final double SHOOTER_I = 0.001;
    private static final double SHOOTER_D = 0.00002;
    private static final double SHOOTER_POW = 0.8;
    private static final double TRANSFER_POW = 0.8;
    private static final double CLIMB_POW = 1.0;
    private static final double INTAKE_PWR = 1.0;
    private static final double RETRACT_PWR = 1.0;

    private final ElapsedTime runtime = new ElapsedTime();
    private final ElapsedTime shooterLoopTimer = new ElapsedTime();

    private double shooterIntegral;
    private double shooterLastError;
    private boolean shooterEnabled;
    private double shooterError;
    private double shooterOutput;
    private boolean reverseDrive;
    private boolean lastLeftStickButton;

    @Override
    public void runOpMode() {

        leftDrive = hardwareMap.get(DcMotorEx.class, "driveLeft");
        rightDrive = hardwareMap.get(DcMotorEx.class, "driveRight");

        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");

        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
        ascentMotor = hardwareMap.get(DcMotorEx.class, "ascentMotor");

        preShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        ascentMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        preShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ascentMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        preShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ascentMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        intake = hardwareMap.get(DcMotorEx.class, "intake");
        retract = hardwareMap.get(DcMotorEx.class, "laLa");

        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        retract.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        retract.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();

        runtime.reset();
        shooterLoopTimer.reset();

        while (opModeIsActive()) {

            // ==================== DRIVETRAIN ====================
            drivetrain();


            // ==================== DRIVE DIRECTION TOGGLE ====================

            boolean pressed = gamepad1.left_stick_button;

            if (pressed && !lastLeftStickButton) {
                reverseDrive = !reverseDrive;
                gamepad1.rumble(500);
            }

            lastLeftStickButton = pressed;


            // ==================== SHOOTER ====================

            boolean shootPressed = gamepad2.right_stick_button;
            boolean climbPressed = gamepad1.left_trigger > 0.4;
            boolean shooterAccelerate =
                    gamepad2.right_bumper || gamepad1.a;
            boolean transferDown =
                    gamepad2.left_trigger > 0.4;


            if (!shooterAccelerate) {

                shooterEnabled = false;
                shooterIntegral = 0;
                shooterLastError = 0;
                shooterError = 0;
                shooterOutput = 0;

                shooterLoopTimer.reset();

                shooterLeft.setPower(0);
                shooterRight.setPower(0);

            } else {

                if (!shooterEnabled) {

                    shooterIntegral = 0;
                    shooterLastError = 0;
                    shooterLoopTimer.reset();
                    shooterEnabled = true;
                }

                double currentVelocity =
                        (shooterLeft.getVelocity()
                                + shooterRight.getVelocity()) / 2.0;

                double dt =
                        Math.max(shooterLoopTimer.seconds(), 0.001);

                shooterLoopTimer.reset();

                shooterError =
                        SHOOTER_TARGET_VELOCITY - currentVelocity;

                shooterIntegral += shooterError * dt;

                double derivative =
                        (shooterError - shooterLastError) / dt;

                shooterLastError = shooterError;

                shooterOutput =
                        SHOOTER_P * shooterError
                                + SHOOTER_I * shooterIntegral
                                + SHOOTER_D * derivative;

                shooterOutput =
                        Math.max(-1.0, Math.min(1.0, shooterOutput));

                shooterLeft.setPower(shooterOutput);
                shooterRight.setPower(shooterOutput);
            }


            // ==================== TRANSFER / CLIMB ====================

            if (climbPressed) {

                preShooter.setPower(CLIMB_POW);
                ascentMotor.setPower(CLIMB_POW);

            } else if (shootPressed) {

                preShooter.setPower(TRANSFER_POW);
                ascentMotor.setPower(TRANSFER_POW);

            } else if (transferDown) {

                preShooter.setPower(-TRANSFER_POW);
                ascentMotor.setPower(-TRANSFER_POW);

            } else {

                preShooter.setPower(0);
                ascentMotor.setPower(0);
            }


            // ==================== SHOOTER RUMBLE ====================

            if (shooterLeft.getVelocity() > 1500
                    || shooterRight.getVelocity() > 1500) {

                gamepad2.rumble(3);
            }


            // ==================== INTAKE ====================

            if (gamepad1.right_bumper) {

                intake.setPower(INTAKE_PWR);

            } else if (gamepad1.left_bumper) {

                intake.setPower(-INTAKE_PWR);

            } else {

                intake.setPower(0);
            }


            // ==================== RETRACT ====================

            retract.setPower(
                    -gamepad2.left_stick_y * RETRACT_PWR
            );


            // ==================== TELEMETRY ====================

            telemetry.addData(
                    "Runtime",
                    runtime.seconds()
            );

            telemetry.addData(
                    "Drive Direction",
                    reverseDrive ? "REVERSE" : "NORMAL"
            );

            telemetry.addData(
                    "Shooter target velocity",
                    SHOOTER_TARGET_VELOCITY
            );

            telemetry.addData(
                    "Shooter output",
                    shooterOutput
            );

            telemetry.addData(
                    "Shooter error",
                    shooterError
            );

            telemetry.addData(
                    "shooterL vel",
                    shooterLeft.getVelocity()
            );

            telemetry.addData(
                    "shooterR vel",
                    shooterRight.getVelocity()
            );

            telemetry.addData(
                    "preShooter power",
                    preShooter.getPower()
            );

            telemetry.addData(
                    "ascent power",
                    ascentMotor.getPower()
            );

            telemetry.update();

            idle();
        }

        shooterLeft.setPower(0);
        shooterRight.setPower(0);
    }


    // ==================== DRIVETRAIN FUNCTION ====================

    private void drivetrain() {

        double forward = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        if (reverseDrive) {
            forward = -forward;
        }

        double leftPower = forward + turn;
        double rightPower = forward - turn;

        double maxPower = Math.max(
                Math.abs(leftPower),
                Math.abs(rightPower)
        );

        if (maxPower > 1.0) {
            leftPower /= maxPower;
            rightPower /= maxPower;
        }

        leftPower = Math.max(
                -1.0,
                Math.min(1.0, leftPower)
        );

        rightPower = Math.max(
                -1.0,
                Math.min(1.0, rightPower)
        );

        leftDrive.setPower(
                leftPower * DRIVE_SPEED_MULTIPLIER
        );

        rightDrive.setPower(
                rightPower * DRIVE_SPEED_MULTIPLIER
        );
    }
}
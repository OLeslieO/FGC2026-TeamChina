/*
Copyright 2026 FIRST Tech Challenge Team CHN

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "DriverTeleOp", group = "0-competition")
public class DriverTeleOp extends LinearOpMode {

    private DcMotorEx leftDrive;
    private DcMotorEx rightDrive;

    private DcMotorEx shooterLeft;
    private DcMotorEx shooterRight;
    private DcMotorEx preShooter;
    private DcMotorEx ascentMotor;

    private DcMotorEx intake;
    private DcMotorEx retract;

    private static final double DRIVE_SPEED_MULTIPLIER = 1.0;

    private static final double SHOOTER_POW = 1.0;
    private static final double RUMBLE_VEL = 1900;

    private static final double CLIMB_POW = 1.0;
    private static final double PRE_SHOOTER_POW = 1.0;
    private static final double INTAKE_POW = 1.0;
    private static final double RETRACT_POW = 1.0;

    private boolean shooterEnabled = false;
    private double shooterOutput = 0;
    private boolean reverseDrive = false;
    private boolean lastLeftStickButton = false;
    private boolean lastAButton = false;
    boolean reachedRumbleVel = false;

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
        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
        ascentMotor = hardwareMap.get(DcMotorEx.class, "ascentMotor");
        preShooter.setDirection(DcMotorSimple.Direction.FORWARD);
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

        waitForStart();

        while (opModeIsActive()) {

            handleDriveDirectionToggle();
            handleDriveControls();
            handleShooter();
            handleTransferClimb();
            handleIntakeAndRetract();

            idle();
        }

        stopShooter();
    }

    private void handleDriveDirectionToggle() {
        boolean pressed = gamepad1.left_stick_button;
        if (pressed && !lastLeftStickButton) {
            reverseDrive = !reverseDrive;
            gamepad1.rumble(500);
        }
        lastLeftStickButton = pressed;
    }

    private void handleDriveControls() {
        double forward = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

//        if (reverseDrive) {
//            forward = -forward;
//        }

        double leftPower = forward + turn;
        double rightPower = forward - turn;
        double maxPower = Math.max(Math.abs(leftPower), Math.abs(rightPower));

        if (maxPower > 1.0) {
            leftPower /= maxPower;
            rightPower /= maxPower;
        }

        leftDrive.setPower(Range.clip(leftPower, -1.0, 1.0) * DRIVE_SPEED_MULTIPLIER);
        rightDrive.setPower(Range.clip(rightPower, -1.0, 1.0) * DRIVE_SPEED_MULTIPLIER);
    }

    private void handleShooter() {
        boolean aPressed = gamepad1.a;

        if (gamepad2.right_bumper || aPressed) {
            shooterOutput = SHOOTER_POW;
            setShooterPower(shooterOutput);
        } else {
            shooterOutput = 0;
            stopShooter();
        }
        
        // rumble function
        boolean atRumbleVel =
                shooterLeft.getVelocity() >= RUMBLE_VEL ||
                shooterRight.getVelocity() >= RUMBLE_VEL;
        
        if (atRumbleVel && !reachedRumbleVel) {
            gamepad1.rumble(1000);
            gamepad2.rumble(1000);
        
            reachedRumbleVel = true;
        }
        
        if (!atRumbleVel) {
            reachedRumbleVel = false;
        }
    }

    private void handleTransferClimb() {
        boolean climbPressed = gamepad1.left_trigger > 0.4;
        boolean preShooterForward = gamepad2.left_bumper;
        boolean preShooterReverse = gamepad2.left_trigger > 0.4;

        if (climbPressed || preShooterForward) {
            preShooter.setPower(CLIMB_POW);
            ascentMotor.setPower(CLIMB_POW);
        } else if (preShooterReverse) {
            preShooter.setPower(-PRE_SHOOTER_POW);
            ascentMotor.setPower(-PRE_SHOOTER_POW);
        } else {
            preShooter.setPower(0);
            ascentMotor.setPower(0);
        }
    }

    private void handleIntakeAndRetract() {
        if (gamepad1.right_bumper) {
            intake.setPower(INTAKE_POW);
        } else if (gamepad1.left_bumper) {
            intake.setPower(-INTAKE_POW);
        } else {
            intake.setPower(0);
        }

        retract.setPower(-gamepad2.left_stick_y * RETRACT_POW);
    }

    private void setShooterPower(double power) {
        shooterLeft.setPower(power);
        shooterRight.setPower(power);
    }

    private void stopShooter() {
        shooterLeft.setPower(0);
        shooterRight.setPower(0);
    }
}

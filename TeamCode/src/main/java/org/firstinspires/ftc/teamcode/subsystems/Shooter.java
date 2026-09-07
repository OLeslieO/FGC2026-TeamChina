package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Hardwares;

/**
 * 双电机发射系统。
 *
 * 功能：
 * 1. 控制左右两个发射电机
 * 2. 控制 preShooter 电机
 * 3. 提供基础启动、怠速、停止命令
 * 4. 提供基础遥测信息
 */
public class Shooter {

    public static final double DEFAULT_IDLE_POWER = 0.3;
    public static final double DEFAULT_SHOOT_POWER = 1.0;
    public static final double DEFAULT_PRE_SHOOT_POWER = 1.0;
    public static final double DEFAULT_PRE_SHOOT_VELOCITY = 1500.0;

    private final DcMotorEx shooterLeft;
    private final DcMotorEx shooterRight;
    private final DcMotorEx preShooter;

    public Shooter(@NonNull Hardwares hardwares) {
        this.shooterLeft = hardwares.motors.shooterLeft;
        this.shooterRight = hardwares.motors.shooterRight;
        this.preShooter = hardwares.motors.preShooter;
        init();
    }

    private void init() {
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        preShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooterLeft.setDirection(DcMotor.Direction.FORWARD);
        shooterRight.setDirection(DcMotor.Direction.REVERSE);
        preShooter.setDirection(DcMotor.Direction.REVERSE);

        shooterLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        preShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * 兼容周期调用。当前开放环控制下无需处理。
     */
    public void run() {
        // No-op.
    }

    public void setPower(double power) {
        setPower(power, power);
    }

    public void setPower(double leftPower, double rightPower) {
        shooterLeft.setPower(clamp(leftPower));
        shooterRight.setPower(clamp(rightPower));
    }

    public InstantCommand runShooter() {
        return runShooter(DEFAULT_SHOOT_POWER);
    }

    public InstantCommand runShooter(double power) {
        return new InstantCommand(() -> setPower(power));
    }

    public InstantCommand shooterIdle() {
        return new InstantCommand(() -> setPower(DEFAULT_IDLE_POWER));
    }

    public InstantCommand stopShooter() {
        return new InstantCommand(() -> setPower(0.0));
    }

    public void setPreShooterPower(double power) {
        preShooter.setPower(clamp(power));
    }

    public InstantCommand runPreShooter() {
        return runPreShooter(DEFAULT_PRE_SHOOT_POWER);
    }

    public InstantCommand runPreShooter(double power) {
        return new InstantCommand(() -> setPreShooterPower(power));
    }

    public void setPreShooterVelocity(double velocity) {
        preShooter.setVelocity(velocity);
    }

    public InstantCommand runPreShooterVelocity() {
        return runPreShooterVelocity(DEFAULT_PRE_SHOOT_VELOCITY);
    }

    public InstantCommand runPreShooterVelocity(double velocity) {
        return new InstantCommand(() -> setPreShooterVelocity(velocity));
    }

    public InstantCommand stopPreShooter() {
        return new InstantCommand(() -> {
            setPreShooterVelocity(0.0);
            setPreShooterPower(0.0);
        });
    }

    public double[] getVelocities() {
        return new double[]{shooterLeft.getVelocity(), shooterRight.getVelocity()};
    }

    public double getPreShooterVelocity() {
        return preShooter.getVelocity();
    }

    public static class TelemetryState {
        public final double leftVelocity;
        public final double rightVelocity;
        public final double leftCurrent;
        public final double rightCurrent;
        public final double leftPower;
        public final double rightPower;
        public final double preShooterVelocity;
        public final double preShooterCurrent;
        public final double preShooterPower;

        public TelemetryState(
                double leftVelocity,
                double rightVelocity,
                double leftCurrent,
                double rightCurrent,
                double leftPower,
                double rightPower,
                double preShooterVelocity,
                double preShooterCurrent,
                double preShooterPower
        ) {
            this.leftVelocity = leftVelocity;
            this.rightVelocity = rightVelocity;
            this.leftCurrent = leftCurrent;
            this.rightCurrent = rightCurrent;
            this.leftPower = leftPower;
            this.rightPower = rightPower;
            this.preShooterVelocity = preShooterVelocity;
            this.preShooterCurrent = preShooterCurrent;
            this.preShooterPower = preShooterPower;
        }

        @NonNull
        @Override
        public String toString() {
            return String.format(
                    java.util.Locale.US,
                    "Left Velocity: %.1f\nRight Velocity: %.1f\nLeft Current: %.1f\nRight Current: %.1f\nLeft Power: %.2f\nRight Power: %.2f\nPreShooter Velocity: %.1f\nPreShooter Current: %.1f\nPreShooter Power: %.2f",
                    leftVelocity,
                    rightVelocity,
                    leftCurrent,
                    rightCurrent,
                    leftPower,
                    rightPower,
                    preShooterVelocity,
                    preShooterCurrent,
                    preShooterPower
            );
        }
    }

    public TelemetryState getTelemetryState() {
        return new TelemetryState(
                shooterLeft.getVelocity(),
                shooterRight.getVelocity(),
                shooterLeft.getCurrent(CurrentUnit.MILLIAMPS),
                shooterRight.getCurrent(CurrentUnit.MILLIAMPS),
                shooterLeft.getPower(),
                shooterRight.getPower(),
                preShooter.getVelocity(),
                preShooter.getCurrent(CurrentUnit.MILLIAMPS),
                preShooter.getPower()
        );
    }

    private static double clamp(double value) {
        return Math.max(-1.0, Math.min(1.0, value));
    }
}

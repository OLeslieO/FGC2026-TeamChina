package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardwares;

/**
 * laLa 电机机构。
 *
 * 功能：
 * 1. 控制 laLa 电机正转
 * 2. 控制 laLa 电机反转
 * 3. 控制 laLa 电机停止
 */
public class Gate {

    public static final double DEFAULT_POWER = 1.0;

    private final DcMotorEx laLaMotor;

    /**
     * 构造 laLa 机构实例。
     *
     * @param hardwares 硬件映射
     */
    public Gate(@NonNull Hardwares hardwares) {
        this.laLaMotor = hardwares.motors.laLa;
        init();
    }

    private void init() {
        laLaMotor.setDirection(DcMotor.Direction.FORWARD);
        laLaMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        laLaMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * 以指定功率设置电机输出。
     *
     * @param power 电机功率 [-1.0, 1.0]
     */
    public void setPower(double power) {
        laLaMotor.setPower(clamp(power));
    }

    /**
     * laLa 正转。
     *
     * @return 正转命令
     */
    public InstantCommand forward() {
        return new InstantCommand(() -> setPower(DEFAULT_POWER));
    }

    /**
     * laLa 反转。
     *
     * @return 反转命令
     */
    public InstantCommand reverse() {
        return new InstantCommand(() -> setPower(-DEFAULT_POWER));
    }

    /**
     * 停止 laLa 电机。
     *
     * @return 停止命令
     */
    public InstantCommand stop() {
        return new InstantCommand(() -> setPower(0.0));
    }

    /**
     * 获取当前电机功率。
     *
     * @return 当前功率
     */
    public double getPower() {
        return laLaMotor.getPower();
    }

    private static double clamp(double value) {
        return Math.max(-1.0, Math.min(1.0, value));
    }
}

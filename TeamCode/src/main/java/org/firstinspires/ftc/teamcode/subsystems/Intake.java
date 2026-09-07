package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardwares;

/**
 * 吸球系统
 * 
 * 功能：
 * 1. 控制Intake电机启动/停止
 * 2. 控制反向Intake（弹出）
 */
public class Intake {

    // ==========================================
    // 常量配置
    // ==========================================
    private static double INTAKE_PWR = 1;        // 吸球功率（向内）
    
    // ==========================================
    // 成员变量
    // ==========================================
    private DcMotorEx intakeMotor;

    /**
     * 构造吸球系统实例
     * @param hardwares 硬件映射
     */
    public Intake(@NonNull Hardwares hardwares){
        intakeMotor = hardwares.motors.intake;
        this.init();
    }

    private void init() {
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setDirection(DcMotorEx.Direction.REVERSE);
    }

    /**
     * 获取启动吸球的命令
     * @return 启动吸球的InstantCommand
     */
    public InstantCommand startIntake(){
        return new InstantCommand(
                ()->intakeMotor.setPower(INTAKE_PWR)
        );
    }

    /**
     * 获取停止吸球的命令
     * @return 停止吸球的InstantCommand
     */
    public InstantCommand stopIntake(){
        return new InstantCommand(
                ()->intakeMotor.setPower(0)
        );
    }

    /**
     * 获取启动反向吐球的命令
     * @return 启动反向吐球的InstantCommand
     */
    public InstantCommand startOutTake(){
        return new InstantCommand(
                ()->intakeMotor.setPower(-INTAKE_PWR)
        );
    }

    public void setIntakePower(double power) {
        INTAKE_PWR = power;
    }
}

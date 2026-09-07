package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

/**
 * 数据容器：从外置里程计 / 定位计算机获取的机器人运动状态。
 * 所有量以机器人自身坐标系定义：x 向左为正，y 向前为正。
 */
public class PinpointDriverData {
    
    // ==========================================
    // 成员变量
    // ==========================================
    private final GoBildaPinpointDriver pinpointDriver;
    private double headingRadians;
    private double headingDegrees;
    private double robotX;
    private double robotY;
    private double robotVx;
    private double robotVy;
    private double yawRate;
    private Pose2D robotPosition;

    /**
     * 从GoBilda Pinpoint里程计构造里程计数据实例
     * @param pinpointDriver GoBilda Pinpoint里程计驱动
     */
    public PinpointDriverData(@NonNull GoBildaPinpointDriver pinpointDriver) {
        this.pinpointDriver = pinpointDriver;
        this.headingRadians = pinpointDriver.getHeading(AngleUnit.RADIANS);
        this.headingDegrees = pinpointDriver.getHeading(AngleUnit.DEGREES);
        this.robotX = pinpointDriver.getPosX(DistanceUnit.CM);
        this.robotY = pinpointDriver.getPosY(DistanceUnit.CM);
        this.robotVx = pinpointDriver.getVelX(DistanceUnit.CM);
        this.robotVy = pinpointDriver.getVelY(DistanceUnit.CM);
        this.yawRate = pinpointDriver.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES);
        this.robotPosition = pinpointDriver.getPosition();
    }

    public PinpointDriverData() {
        this.pinpointDriver = null;
        this.headingRadians = 0;
        this.headingDegrees = 0;
        this.robotX = 0;
        this.robotY = 0;
        this.robotVx = 0;
        this.robotVy = 0;
        this.yawRate = 0;
        this.robotPosition = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.DEGREES, 0);
    }

    public void update() {
        if (pinpointDriver != null) {
            pinpointDriver.update();
            this.headingRadians = pinpointDriver.getHeading(AngleUnit.RADIANS);
            this.headingDegrees = pinpointDriver.getHeading(AngleUnit.DEGREES);
            this.robotX = pinpointDriver.getPosX(DistanceUnit.CM);
            this.robotY = pinpointDriver.getPosY(DistanceUnit.CM);
            this.robotVx = pinpointDriver.getVelX(DistanceUnit.CM);
            this.robotVy = pinpointDriver.getVelY(DistanceUnit.CM);
            this.yawRate = pinpointDriver.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES);
            this.robotPosition = pinpointDriver.getPosition();
        }
    }

    /**
     * 获取当前航向（弧度）
     * @return 当前航向（弧度）
     */
    public double getHeadingRadians() {
        return headingRadians;
    }

    /**
     * 获取当前航向（度）
     * @return 当前航向（度）
     */
    public double getHeadingDegrees() {
        return headingDegrees;
    }

    /**
     * 获取机器人X坐标 (向左为正, cm)
     * @return 机器人X坐标
     */
    public double getRobotX() {
        return robotX;
    }

    /**
     * 获取机器人Y坐标 (向前为正, cm)
     * @return 机器人Y坐标
     */
    public double getRobotY() {
        return robotY;
    }

    /**
     * 获取机器人X方向速度
     * @return 机器人X方向速度
     */
    public double getRobotVx() {
        return robotVx;
    }

    /**
     * 获取机器人Y方向速度
     * @return 机器人Y方向速度
     */
    public double getRobotVy() {
        return robotVy;
    }

    /**
     * 获取航向角速度
     * @return 航向角速度
     */
    public double getYawRate() {
        return yawRate;
    }

    /**
     * 获取机器人位置
     * @return 机器人位置
     */
    public Pose2D getRobotPosition() {
        return robotPosition;
    }
}

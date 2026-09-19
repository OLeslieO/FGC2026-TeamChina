package org.firstinspires.ftc.teamcode.subsystems;

public enum Constants {
    DRIVE_SPEED_MULTIPLIER(1.0),
    SHOOTER_SHOOT_POW(1.0),
    SHOOTER_IDLE_POW(0.3),
    SHOOTER_TARGET_VEL(1000),
    SHOOTER_PIDF_P(10.0),
    SHOOTER_PIDF_I(3.0),
    SHOOTER_PIDF_D(0.0),
    SHOOTER_PIDF_F(0.0),
    TRANSFER_POW(0.8),
    TRANSFER_VEL(1160),
    INTAKE_PWR(1.0),
    RETRACT_PWR(1.0);
    public final double value;
    Constants(double value) {
        this.value = value;
    }

}

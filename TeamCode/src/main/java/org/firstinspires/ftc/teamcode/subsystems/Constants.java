package org.firstinspires.ftc.teamcode.subsystems;

public enum Constants {
    DRIVE_SPEED_MULTIPLIER(1.0),
    SHOOTER_SHOOT_POW(1.0),
    SHOOTER_SHOOT_VEL(1800),
    SHOOTER_IDLE_POW(0.3),
    RUMBLE_TARGET_VEL(1400),
    SHOOTER_PIDF_P(35),
    SHOOTER_PIDF_I(3.0),
    SHOOTER_PIDF_D(5),
    SHOOTER_PIDF_F(16),
    TRANSFER_POW(0.8),
    TRANSFER_VEL(1500),
    INTAKE_PWR(1.0),
    RETRACT_MIN_TICKS(0),
    RETRACT_MAX_TICKS(676),
    RETRACT_PWR(1.0);

    public final double value;
    Constants(double value) {
        this.value = value;
    }

}

package org.firstinspires.ftc.teamcode.subsystems;

public enum Constants {
    DRIVE_FAST_MULTIPLIER(1.0),
    DRIVE_SLOW_MULTIPLIER(0.3),
    SHOOTER_SHOOT_POW(1.0),
    SHOOTER_IDLE_POW(0.3),
    TRANSFER_POW(0.8),
    INTAKE_PWR(1.0),
    LALA_PWR(1.0);
    public final double value;
    Constants(double value) {
        this.value = value;
    }

}

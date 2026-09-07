package org.firstinspires.ftc.teamcode.subsystems;

public enum Constants {
    SHOOTER_SHOOT_POW(1.0),
    SHOOTER_IDLE_POW(0.3),
    PRESHOOTER_SHOOT_POW(1.0),
    ASCENT_POW(1.0);
    public final double value;
    Constants(double value) {
        this.value = value;
    }

}

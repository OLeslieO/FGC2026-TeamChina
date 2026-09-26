package org.firstinspires.ftc.teamcode.shooter;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc.shooter.tuner.autotune.Gains;
import org.ftc.shooter.tuner.ftc.ShooterAutoTuneOpMode;

@TeleOp(name = "Shooter AutoTune", group = "Tuning")
public final class Tuning extends ShooterAutoTuneOpMode {
    @Override
    protected Gains testGains() {
        return new Gains(Constants.SHOOTER_KS, Constants.SHOOTER_KV, Constants.SHOOTER_KA,
                Constants.SHOOTER_KP, Constants.SHOOTER_KI, Constants.SHOOTER_KD);
    }
}

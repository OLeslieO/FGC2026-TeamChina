package org.firstinspires.ftc.teamcode.utils;

import com.seattlesolvers.solverslib.command.CommandOpMode;

public abstract class CommandOpmodeEx extends CommandOpMode {
    public abstract void onStart();
    public void functionalButtons() { };

    protected double getFunctionalButtonsUpdateIntervalSeconds() {
        return 3;
    }

    @Override
    public void runOpMode() {
        initialize();

        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();

        telemetry.clearAll();

        onStart();

        long lastFunctionalButtonsUpdateNanos = 0;

        // run the scheduler
        while (!isStopRequested() && opModeIsActive()) {
            run();
            double updateIntervalSeconds = getFunctionalButtonsUpdateIntervalSeconds();
            long nowNanos = System.nanoTime();
            if (updateIntervalSeconds <= 0 ||
                    nowNanos - lastFunctionalButtonsUpdateNanos >= updateIntervalSeconds * 1_000_000_000L) {
                functionalButtons(); // better for dashboard
                lastFunctionalButtonsUpdateNanos = nowNanos;
            }
        }
        reset();
    }
}

package org.firstinspires.ftc.teamcode.utils;

import com.seattlesolvers.solverslib.command.CommandOpMode;

public abstract class CommandOpmodeEx extends CommandOpMode {
    public abstract void onStart();
    public void functionalButtons() { };
    @Override
    public void runOpMode() {
        initialize();

        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();

        telemetry.clearAll();

        onStart();

        // run the scheduler
        while (!isStopRequested() && opModeIsActive()) {
            run();
            functionalButtons(); // better for dashboard
        }
        reset();
    }
}

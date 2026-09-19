package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;

public abstract class CommandOpmodeEx extends CommandOpMode {
    public abstract void onStart();
    public void functionalButtons() { };
    public ElapsedTime elapsedtime;



    @Override
    public void runOpMode() {
        initialize();
        elapsedtime = new ElapsedTime();
        elapsedtime.reset();


        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();

        telemetry.clearAll();

        onStart();

        while (!isStopRequested() && opModeIsActive()) {
            run();



                functionalButtons();

            }

        }
    }


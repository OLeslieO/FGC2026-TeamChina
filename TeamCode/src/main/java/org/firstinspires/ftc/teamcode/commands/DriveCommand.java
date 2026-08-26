package org.firstinspires.ftc.teamcode.commands;


import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class DriveCommand extends CommandBase {

    private final DriveSubsystem driveSubsystem;
    private final DoubleSupplier forward;
    private final DoubleSupplier turn;
    private final BooleanSupplier isSlowMode;

    public DriveCommand(DriveSubsystem subsystem,
                        DoubleSupplier forward,
                        DoubleSupplier turn,
                        BooleanSupplier isSlowMode) {
        driveSubsystem = subsystem;
        this.forward = forward;
        this.turn = turn;
        this.isSlowMode = isSlowMode;
        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        driveSubsystem.move(
                forward.getAsDouble(),
                turn.getAsDouble(),
                isSlowMode.getAsBoolean()
        );
        if (isSlowMode.getAsBoolean()){
            driveSubsystem.speedMultiplier = 0.4;
        } else {
            driveSubsystem.speedMultiplier = 1;
        }
    }
    @Override
    public void end(boolean interrupted) {
        driveSubsystem.stop();
    }

}

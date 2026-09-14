package org.firstinspires.ftc.teamcode.commands;


import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

public class DriveCommand extends CommandBase {

    private final DriveSubsystem driveSubsystem;
    private final DoubleSupplier forward;
    private final DoubleSupplier turn;
    private final DoubleSupplier speedMultiplier;

    public DriveCommand(DriveSubsystem subsystem,
                        DoubleSupplier forward,
                        DoubleSupplier turn,
                        DoubleSupplier speedMultiplier) {
        driveSubsystem = subsystem;
        this.forward = forward;
        this.turn = turn;
        this.speedMultiplier = speedMultiplier;
        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        driveSubsystem.move(
                forward.getAsDouble(),
                turn.getAsDouble(),
                speedMultiplier.getAsDouble()
        );
    }
    @Override
    public void end(boolean interrupted) {
        driveSubsystem.stop();
    }

}

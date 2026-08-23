package org.firstinspires.ftc.teamcode.commands;


import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

public class IntakeCommand extends CommandBase {

    private final IntakeSubsystem intakeSubsystem;
    private final DoubleSupplier forward;
    private final DoubleSupplier turn;
    private final DoubleSupplier speedMultiplier;

    public IntakeCommand(DriveSubsystem subsystem,
                        DoubleSupplier forward, DoubleSupplier turn, DoubleSupplier speedMultiplier) {
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

}

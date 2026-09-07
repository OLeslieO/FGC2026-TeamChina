package org.firstinspires.ftc.teamcode;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.button.Button;

import java.util.function.BooleanSupplier;

public class ButtonEx extends Button {
    private final BooleanSupplier booleanSupplier;

    public ButtonEx(BooleanSupplier supplier) {
        booleanSupplier = supplier;
    }

    @Override
    public boolean get() {
        return booleanSupplier.getAsBoolean();
    }

    public Button whenPressed(Command ... commands) {
        super.whenPressed(
                new ParallelCommandGroup(commands)
        );
        return this;
    }
}

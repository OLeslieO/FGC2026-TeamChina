package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import java.util.Objects;

public class IntakeSubsystem extends SubsystemBase {
    public enum IntakeState {
        STOPPED(0.0),
        INTAKING(Constants.INTAKE_PWR.value),
        OUTTAKING(-Constants.INTAKE_PWR.value);

        private final double power;

        IntakeState(double power) {
            this.power = power;
        }

        public double getPower() {
            return power;
        }
    }

    public enum ExtensionState {
        STOPPED(0.0),
        EXTENDING(Constants.RETRACT_PWR.value),
        RETRACTING(-Constants.RETRACT_PWR.value);

        private final double power;

        ExtensionState(double power) {
            this.power = power;
        }

        public double getPower() {
            return power;
        }
    }

    private final DcMotorEx intake;
    private final DcMotorEx retract;
    private IntakeState intakeState = IntakeState.STOPPED;
    private ExtensionState extensionState = ExtensionState.STOPPED;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        retract = hardwareMap.get(DcMotorEx.class, "laLa");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        retract.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        applyStates();
    }

    public void setIntakeState(IntakeState intakeState) {
        this.intakeState = Objects.requireNonNull(intakeState, "intakeState");
    }

    public IntakeState getIntakeState() {
        return intakeState;
    }

    public void setExtensionState(ExtensionState extensionState) {
        this.extensionState = Objects.requireNonNull(extensionState, "extensionState");
    }

    public ExtensionState getExtensionState() {
        return extensionState;
    }

    public double getIntakePower() {
        return intakeState.getPower();
    }

    public double getExtensionPower() {
        return extensionState.getPower();
    }

    @Override
    public void periodic() {
        applyStates();
    }

    private void applyStates() {
        intake.setPower(intakeState.getPower());
        retract.setPower(extensionState.getPower());
    }
}

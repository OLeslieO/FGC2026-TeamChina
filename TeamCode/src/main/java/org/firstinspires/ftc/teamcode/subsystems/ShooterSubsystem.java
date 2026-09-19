package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import java.util.Objects;

public class ShooterSubsystem extends SubsystemBase {
    public enum ShooterState {
        STOPPED(0.0),
        IDLE(Constants.SHOOTER_IDLE_POW.value),
        SHOOTING(Constants.SHOOTER_SHOOT_POW.value);

        private final double power;

        ShooterState(double power) {
            this.power = power;
        }

        public double getPower() {
            return power;
        }
    }

    public enum TransferState {
        STOPPED(0.0, false, 0.5),
        ASCENDING(Constants.TRANSFER_POW.value, false, 0.5),
        DESCENDING(-Constants.TRANSFER_POW.value, false, 0.5),
        FEEDING(Constants.TRANSFER_VEL.value, true, 1.0);

        private final double output;
        private final boolean velocityControl;
        private final double blenderPosition;

        TransferState(double output, boolean velocityControl, double blenderPosition) {
            this.output = output;
            this.velocityControl = velocityControl;
            this.blenderPosition = blenderPosition;
        }

        public double getOutput() {
            return output;
        }

        public boolean usesVelocityControl() {
            return velocityControl;
        }

        public double getBlenderPosition() {
            return blenderPosition;
        }
    }

    private final DcMotorEx shooterLeft;
    private final DcMotorEx shooterRight;
    private final DcMotorEx preShooter;
    private final DcMotorEx ascentMotor;
    private final Servo blender;
    private ShooterState shooterState = ShooterState.STOPPED;
    private TransferState transferState = TransferState.STOPPED;

    public ShooterSubsystem(HardwareMap hardwareMap) {
        shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");
        preShooter = hardwareMap.get(DcMotorEx.class, "preShooter");
        ascentMotor = hardwareMap.get(DcMotorEx.class, "ascentMotor");
        blender = hardwareMap.get(Servo.class, "blender");
        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        preShooter.setDirection(DcMotorSimple.Direction.FORWARD);
        ascentMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        preShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ascentMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        preShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ascentMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setShooterPIDF(
                Constants.SHOOTER_PIDF_P.value,
                Constants.SHOOTER_PIDF_I.value,
                Constants.SHOOTER_PIDF_D.value,
                Constants.SHOOTER_PIDF_F.value
        );
        applyStates();
    }

    public void setShooterState(ShooterState shooterState) {
        this.shooterState = Objects.requireNonNull(shooterState, "shooterState");
    }

    public ShooterState getShooterState() {
        return shooterState;
    }

    public void setTransferState(TransferState transferState) {
        this.transferState = Objects.requireNonNull(transferState, "transferState");
    }

    public TransferState getTransferState() {
        return transferState;
    }

    public double getShooterPower() {
        return shooterState.getPower();
    }

    public double getTransferOutput() {
        return transferState.getOutput();
    }

    public double getBlenderPosition() {
        return transferState.getBlenderPosition();
    }

    public double getLeftShooterVelocity() {
        return shooterLeft.getVelocity();
    }

    public double getLeftShooterPower() {
        return shooterLeft.getPower();
    }

    public double getRightShooterVelocity() {
        return shooterRight.getVelocity();
    }

    public double getRightShooterPower() {
        return shooterRight.getPower();
    }

    public double getPreShooterVelocity() {
        return preShooter.getVelocity();
    }

    public double getPreShooterPower() {
        return preShooter.getPower();
    }

    public double getAscentVelocity() {
        return ascentMotor.getVelocity();
    }

    public double getAscentPower() {
        return ascentMotor.getPower();
    }

    public void setShooterVelocity(double velocity) {
        shooterLeft.setVelocity(velocity);
        shooterRight.setVelocity(velocity);
    }

    public void setPreShooterVelocity(double velocity) {
        preShooter.setVelocity(velocity);
    }

    public void setShooterPIDF(double p, double i, double d, double f) {
        shooterLeft.setVelocityPIDFCoefficients(p, i, d, f);
        shooterRight.setVelocityPIDFCoefficients(p, i, d, f);
    }

    @Override
    public void periodic() {
        applyStates();
    }

    private void applyStates() {
        shooterLeft.setPower(shooterState.getPower());
        shooterRight.setPower(shooterState.getPower());

        if (transferState.usesVelocityControl()) {
            preShooter.setVelocity(transferState.getOutput());
            ascentMotor.setVelocity(transferState.getOutput());
        } else {
            preShooter.setPower(transferState.getOutput());
            ascentMotor.setPower(transferState.getOutput());
        }
        blender.setPosition(transferState.getBlenderPosition());
    }
}

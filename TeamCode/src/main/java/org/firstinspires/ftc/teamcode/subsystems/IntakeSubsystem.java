package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.HardwareMap;
public class IntakeSubsystem extends SubsystemBase {
    private final DcMotorEx intake, retract;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        retract = hardwareMap.get(DcMotorEx.class, "laLa");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        retract.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        retract.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        retract.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void init(){
        intake.setPower(0);
    }

    public void setIntakePower(double power) {
        intake.setPower(power);
    }

    public void setRetractPower(double power) {
        int position = retract.getCurrentPosition();

        // 向下运动，并且已经到达下限
        if (power < 0 && position <= Constants.RETRACT_MIN_TICKS.value) {
            retract.setPower(0);
            return;
        }

        // 向上运动，并且已经到达上限
        if (power > 0 && position >= Constants.RETRACT_MAX_TICKS.value) {
            retract.setPower(0);
            return;
        }

        // 没有触碰限位，正常运行
        retract.setPower(power);
    }

    public int getRetractPosition() {
        return retract.getCurrentPosition();
    }
}

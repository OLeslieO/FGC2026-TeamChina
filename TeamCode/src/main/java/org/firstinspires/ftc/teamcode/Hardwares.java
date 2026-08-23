package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.seattlesolvers.solverslib.hardware.ServoEx;
import com.seattlesolvers.solverslib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.jetbrains.annotations.Contract;

public class Hardwares {
    public Sensors sensors;
    public Motors motors;
    public Servos servos;

    public static <T> T getHardware(@NonNull HardwareMap hardwareMap, String name, Class<T> clazz) {
        return hardwareMap.get(clazz, name);
    }

    @Contract("!null, _, _, _ -> new")
    public static ServoEx getHardware(HardwareMap hardwareMap, String servoName, double minDegree, double maxDegree) {
        return new SimpleServo(hardwareMap, servoName, minDegree, maxDegree);
    }

    public static class Sensors {
        // TODO GoBilda Pinpoint Driver
        public Sensors(@NonNull HardwareMap hardwareMap) {

        }
    }

    public static class Motors {
        public DcMotorEx laLa, preShooter, intake, driveLeft, driveRight, shooterLeft, shooterRight;

        public Motors(@NonNull HardwareMap hardwareMap) {
            laLa = getHardware(hardwareMap, "laLa", DcMotorEx.class);
            preShooter = getHardware(hardwareMap, "preShooter", DcMotorEx.class);
            intake = getHardware(hardwareMap, "intake", DcMotorEx.class);

            driveLeft = getHardware(hardwareMap, "driveLeft", DcMotorEx.class);
            driveRight = getHardware(hardwareMap, "driveRight", DcMotorEx.class);

            shooterLeft = getHardware(hardwareMap, "shooterLeft", DcMotorEx.class);
            shooterRight = getHardware(hardwareMap, "shooterRight", DcMotorEx.class);
        }
    }

    public static class Servos {
        // TODO Add servos

        public Servos(@NonNull HardwareMap hardwareMap) {

        }
    }

    public Hardwares(@NonNull HardwareMap hardwareMap) {
        sensors = new Sensors(hardwareMap);
        motors = new Motors(hardwareMap);
        servos = new Servos(hardwareMap);
    }
}

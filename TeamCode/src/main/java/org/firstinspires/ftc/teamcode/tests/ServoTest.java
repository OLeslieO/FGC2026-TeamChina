package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Servo Single Test", group = "tests")
@Config
public class ServoTest extends LinearOpMode {

    private final Telemetry telemetry_M =
            new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    public static boolean read_only = false;
    public static boolean reverse0 = false;


    public static volatile double servo_pos = 0.5;


    public static String servo_name0 = "ascentServo";




    @Override
    public void runOpMode() {

        Servo servo0 = hardwareMap.get(Servo.class, servo_name0);




        if (reverse0) {
            servo0.setDirection(Servo.Direction.REVERSE);
        }




        waitForStart();

        while (opModeIsActive()) {
            if (!read_only){
                servo0.setPosition(servo_pos);

            }
            telemetry_M.addData(servo_name0, servo0.getPosition());
            telemetry_M.update();
            idle();
        }
    }
}

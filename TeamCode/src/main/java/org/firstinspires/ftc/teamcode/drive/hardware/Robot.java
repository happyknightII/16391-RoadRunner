package org.firstinspires.ftc.teamcode.drive.hardware;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorDigitalTouch;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class Robot {
    private final ElapsedTime period = new ElapsedTime();
    /* Public OpMode members. */

    public Carousel carousel;
    public Arm arm = new Arm();
    public Intake intake = new Intake();

    public MecanumDriveTrain driveTrain = new MecanumDriveTrain();

    public SensorDigitalTouch intakeSensor = new SensorDigitalTouch();

    HardwareMap hwMap;

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        intake.init(hwMap);
        arm.init(hwMap);
        driveTrain.init(hwMap);
        carousel.init(hwMap);
    }

    public void drive(double forward, double strafe, double turn) {
        Vector2d input = new Vector2d(
                -forward,
                -strafe
        );

        driveTrain.setWeightedDrivePower(
                new Pose2d(
                        input.getX(),
                        input.getY(),
                        -turn
                )
        );
    }
    public void displayTelemetry(Telemetry telemetry) {
        final List<Double> wheelLocations = driveTrain.getWheelPositions();
        telemetry.addLine("Wheel position")
                .addData("Front Left", -wheelLocations.get(0))
                .addData("Front Right", -wheelLocations.get(1))
                .addData("Back Left", -wheelLocations.get(2))
                .addData("Back Right", -wheelLocations.get(3));
        telemetry.addLine("Arm")
                .addData("Target Level", arm.getTargetLevel())
                .addData("Encoder Position", arm.getCurrentPosition());
        telemetry.update();
    }
}

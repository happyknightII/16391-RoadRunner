package org.firstinspires.ftc.teamcode.drive.opmode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.drive.hardware.Robot;

import java.util.List;

@TeleOp(name = "TeleOp", group = "Teleop")
public class TeleOP extends LinearOpMode {
    
    final int       SLOW_CAROUSEL_TIME      = 1000;
    final double    SLOW_CAROUSEL_SPEED     = 0.3;
    final int       FAST_CAROUSEL_TIME      = 7000;
    final double    FAST_CAROUSEL_SPEED     = 1.0;

    Robot robot = new Robot();   //Uses heavily modified untested hardware
    static ElapsedTime runtime = new ElapsedTime();
    boolean inverted = false;
    
    ElapsedTime carouselTimer = new ElapsedTime();

    int carouselState;


    @Override
    public void runOpMode() {

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        //robot.armLeft.setPower(1);
        //robot.armRight.setPower(1);
        //robot.carouselSpinner  .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Press Start to Begin");    //
        telemetry.update();
        robot.arm.lower();
        waitForStart();
        robot.arm.calibrate();
        if(opModeIsActive()) {
            while (opModeIsActive()) {
                opLoop();
                displayTelemetry();
                // Pace this loop so jaw action is reasonable speed.
                sleep(25);
            }
        }
    }

    public void opLoop() {
        // drive train control
        robot.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);


        if (gamepad2.left_trigger > 0) {
            robot.intake.outtake();

        } else if (gamepad2.right_trigger > 0) {
            robot.intake.intake();
        }

        if (gamepad2.a) {
            robot.arm.setLevel(Robot.ArmPosition.INVERSE_FEED);
        }else if (gamepad2.x) {
            robot.arm.setLevel(Robot.ArmPosition.INVERSE_TOP);
        }else if (gamepad2.b) {
            robot.arm.setLevel(Robot.ArmPosition.INVERSE_BOTTOM);
        }else if (gamepad2.y) {
            robot.arm.setLevel(Robot.ArmPosition.INVERSE_MIDDLE);
        }else if (gamepad2.dpad_left) {
            robot.arm.setLevel(Robot.ArmPosition.BOTTOM);
        }else if (gamepad2.dpad_up) {
            robot.arm.setLevel(Robot.ArmPosition.MIDDLE);
        }else if (gamepad2.dpad_right) {
            robot.arm.setLevel(Robot.ArmPosition.TOP);
        }else if (gamepad2.dpad_down) {
            robot.arm.setLevel(Robot.ArmPosition.FEED);
        }else if(gamepad2.left_bumper) {
            robot.arm.setLevel(Robot.ArmPosition.CAP_UP);
        }else if (gamepad2.right_bumper) {
            robot.arm.setLevel(Robot.ArmPosition.CAP_DOWN);
        }

        switch (carouselState) {
            case 0:
                robot.carousel.setPower(0);
                if (gamepad1.left_trigger > 0) {
                    robot.carousel.setPower(SLOW_CAROUSEL_SPEED);
                    carouselState = 1;
                    carouselTimer.reset();
                }
                break;
            case 1:
                if (carouselTimer.time() > SLOW_CAROUSEL_TIME) {
                    robot.carousel.setPower(FAST_CAROUSEL_SPEED);
                    carouselState = 2;
                }
                break;
            case 2:
                if (carouselTimer.time() > FAST_CAROUSEL_TIME) {
                    carouselState = 0;
                }
                break;
        }

    }

    public void displayTelemetry() {
        final List<Double> wheelLocations = robot.driveTrain.getWheelPositions();
        telemetry.addLine("Wheel position")
                .addData("Front Left", -wheelLocations.get(0))
                .addData("Front Right", -wheelLocations.get(1))
                .addData("Back Left", -wheelLocations.get(2))
                .addData("Back Right", -wheelLocations.get(3));
        telemetry.addLine("Arm")
                .addData("Target Level", robot.arm.getTargetLevel())
                .addData("Encoder Position", robot.arm.getCurrentPosition());
        telemetry.update();
    }
}
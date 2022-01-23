package org.firstinspires.ftc.teamcode.drive.opmode.competitioncode.autonomouscode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.hardware.Robot;

@Autonomous(name = "Red Auto: Warehouse - Around - Right - Short", group = "Red")
//@Disabled
public class RedAroundShort extends AutonomousTemplate {
    Robot robot = new Robot();
    private int autoState = 1;
    ElapsedTime runtime = new ElapsedTime();

    public void initialize() {
        super.initialize();
        robot.init(hardwareMap);
        robot.arm.lower();
    }

    public void starting(){
        super.starting();
        robot.arm.calibrate();
    }

    public void leftPath() {
        super.leftPath();
        robot.arm.setLevel(Robot.ArmPosition.BOTTOM);
    }

    public void rightPath() {
        super.rightPath();
        robot.arm.setLevel(Robot.ArmPosition.MIDDLE);

    }

    public void unseenPath() {
        super.unseenPath();
        robot.arm.setLevel(Robot.ArmPosition.TOP);

    }


    public void regularAutonomous() {
        super.regularAutonomous();
        Pose2d startPose = new Pose2d(6.44, -63, Math.toRadians(90));

        robot.driveTrain.setPoseEstimate(startPose);


        Trajectory startAuto = robot.driveTrain.trajectoryBuilder(startPose)
                .splineToSplineHeading(new Pose2d(-5, -45, Math.toRadians(110)), Math.toRadians(120))
                .build();

        Trajectory moveToWarehouse = robot.driveTrain.trajectoryBuilder(startAuto.end(), Math.toRadians(280))
                .splineToSplineHeading(new Pose2d(20,-66,Math.toRadians(180)), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(50,-66,Math.toRadians(180)), Math.toRadians(0))
                .build();

        Trajectory moveToShippingHub = robot.driveTrain.trajectoryBuilder(moveToWarehouse.end(),Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(20,-66,Math.toRadians(180)), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-5,-40,Math.toRadians(110)), Math.toRadians(100))
                .build();

        while (opModeIsActive()) {  //FSM
            switch(autoState) {
                case 1:
                    robot.driveTrain.followTrajectoryAsync(startAuto); //Start move
                    autoState = 2;
                    runtime.reset();
                    break;

                case 2: // at starting position to hub
                    robot.driveTrain.update();          // update values for drive motors according to trajectory
                    if (runtime.time() > 1) {           // close enough to eject block
                        robot.intake.outtake();
                    }
                    if (!robot.driveTrain.isBusy()) {   // wait for robot to arrive
                        robot.driveTrain.followTrajectoryAsync(moveToWarehouse);    // start new trajectory
                        robot.intake.stop();
                        autoState = 3;
                        runtime.reset();
                    }
                    break;

                case 3: // at hub to warehouse
                    robot.driveTrain.update();
                    if (runtime.time() > 0.2) {         // can move arm without flipping shipping hub
                        robot.arm.setLevel(Robot.ArmPosition.INVERSE_FEED);

                    }
                    if (!robot.arm.isBusy()) {          // wait for feed position to intake
                        robot.intake.intake();
                    }
                    if (!robot.driveTrain.isBusy()) {    // wait for robot to arrive
                        robot.driveTrain.followTrajectoryAsync(moveToShippingHub);
                        autoState = 4;
                        runtime.reset();
                    }
                    break;

                case 4: // at warehouse to hub
                    robot.driveTrain.update();
                    if (runtime.time() > 1) {
                        robot.arm.setLevel(Robot.ArmPosition.TOP);
                    }
                    if (!robot.driveTrain.isBusy()) {
                        robot.driveTrain.followTrajectoryAsync(moveToWarehouse);
                        autoState = 3;
                    }
                    break;
            }
        }
        while(opModeIsActive() && robot.driveTrain.isBusy()) {
            robot.driveTrain.update();
        }
        //robot.intake.outtake();
        robot.driveTrain.followTrajectoryAsync(moveToWarehouse);
        //sleep(1000);
        //robot.intake.intake();
        while(opModeIsActive() && robot.driveTrain.isBusy()) {
            robot.driveTrain.update();
        }
        robot.driveTrain.followTrajectory(moveToShippingHub);
    }
}



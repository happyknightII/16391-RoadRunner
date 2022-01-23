package org.firstinspires.ftc.teamcode.drive.opmode.competitioncode.autonomouscode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.Constants;
import org.firstinspires.ftc.teamcode.drive.hardware.Robot;

@Autonomous(name = "Red Auto: Warehouse - Around - Right - Short", group = "Red")
//@Disabled
public class RedAroundShort extends AutonomousTemplate {
    Robot robot = new Robot();
    private int autoState = 1;
    ElapsedTime elapsedTimer = new ElapsedTime();
    ElapsedTime runTimer     = new ElapsedTime();
    ElapsedTime cycleTimer   = new ElapsedTime();
    double      cycleTime   = 0;

    public void initialize() {
        robot.init(hardwareMap);
        robot.arm.lower();
    }

    public void starting() {
        runTimer.reset();
        robot.arm.calibrate();
    }

    public void leftPath() {
        robot.arm.setLevel(Constants.ArmPosition.BOTTOM);
    }

    public void rightPath() {
        robot.arm.setLevel(Constants.ArmPosition.MIDDLE);

    }

    public void unseenPath() {
        robot.arm.setLevel(Constants.ArmPosition.TOP);

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

        a: while (opModeIsActive()) {  //FSM
            switch(autoState) {
                case 1:
                    robot.driveTrain.followTrajectoryAsync(startAuto); //Start move
                    autoState = 2;
                    elapsedTimer.reset();
                    break;

                case 2: // at starting position to hub
                    robot.driveTrain.update();              // update values for drive motors according to trajectory
                    if (elapsedTimer.time() > 1) {           // close enough to eject block
                        robot.intake.outtake();
                    }
                    if (!robot.driveTrain.isBusy()) {       // wait for robot to arrive
                        robot.driveTrain.followTrajectoryAsync(moveToWarehouse);    // start new trajectory
                        robot.intake.stop();
                        autoState = 3;
                        elapsedTimer.reset();
                        cycleTimer.reset();
                    }
                    break;

                case 3: // at hub to warehouse
                    robot.driveTrain.update();
                    if (elapsedTimer.time() > 0.2) {         // can move arm without flipping shipping hub
                        robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED);

                    }
                    if (!robot.arm.isBusy()) {              // wait for feed position to intake
                        robot.intake.intake();
                    }
                    if (!robot.driveTrain.isBusy()) {       // wait for robot to arrive
                        robot.driveTrain.followTrajectoryAsync(moveToShippingHub);
                        robot.intake.stop();
                        autoState = 4;
                        elapsedTimer.reset();
                    }
                    break;

                case 4: // at warehouse to hub
                    robot.driveTrain.update();
                    if (elapsedTimer.time() > 1) {          // wait to clear barrier to raise arm
                        robot.arm.setLevel(Constants.ArmPosition.TOP);
                    }
                    if (!robot.driveTrain.isBusy()) {       // wait for robot to arrive
                            robot.driveTrain.followTrajectoryAsync(moveToWarehouse);
                            if (cycleTimer.time() > cycleTime) {                        // Store greatest cycle time
                                cycleTime = cycleTimer.time();
                            }

                            autoState = (runTimer.time() + cycleTime >= 29.5) ? 5:3;    // Check if enough time to run another cycle
                            cycleTimer.reset();
                    }
                    break;

                case 5:
                    break a;
            }
        }
    }
}



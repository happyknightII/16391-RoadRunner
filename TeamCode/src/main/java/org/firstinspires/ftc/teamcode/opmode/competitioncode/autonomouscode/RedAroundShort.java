package org.firstinspires.ftc.teamcode.opmode.competitioncode.autonomouscode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.Constants;
import org.firstinspires.ftc.teamcode.drive.hardware.Robot;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Red Auto: Warehouse - Around - Right - Short", group = "Red")
//@Disabled
public class RedAroundShort extends AutonomousTemplate {
    Robot robot = new Robot();
    private int autoState = 1;
    double  cycleNum = 4;

    public void initialize() {
        robot.init(hardwareMap);
        robot.arm.lower();
    }

    public void starting() {
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

        final Pose2d startPose = new Pose2d(6.44, -63, Math.toRadians(90));
        final Pose2d hubPose = new Pose2d(-5, -45, Math.toRadians(110));
        final Pose2d barrierPose = new Pose2d(20, -66, Math.toRadians(180));
        final Pose2d warehouseHalfPose = new Pose2d(30, -66, Math.toRadians(180));


        final double[] tangentHub = new double[]{Math.toRadians(280), Math.toRadians(0)};
        final double[] tangentBarrier = new double[]{Math.toRadians(180), Math.toRadians(100)};

        final double extraAdvance = 2;
        final double firstAdvance = 10;

        final Trajectory startAuto = robot.driveTrain.trajectoryBuilder(startPose)
                .lineToLinearHeading(hubPose)
                .build();

        final Trajectory hubToBarrier = robot.driveTrain.trajectoryBuilder(hubPose, tangentHub[0])
                .splineToSplineHeading(barrierPose, tangentHub[1])
                .build();


        double cycleCounter = 0;
        Pose2d warehouseFullPose;
        Trajectory barrierToWarehouseHalf;
        Trajectory barrierToWarehouseFull;
        TrajectorySequence warehouseFullToHub;


        robot.driveTrain.setPoseEstimate(startPose);
        robot.driveTrain.followTrajectoryAsync(startAuto);

        a: while (opModeIsActive()) {  //FSM

            robot.driveTrain.update();
            switch(autoState) {
                case 1: {   // Score block on hub
                    if (!robot.arm.isBusy() && inRange(robot.driveTrain.getPoseEstimate(), hubPose, 3, Math.toRadians(10))) {      // Wait until arm stopped and in range to release cube
                        robot.intake.outtake();
                        autoState ++;
                    } else {
                        break;
                    }
                }
                case 2: {   // Drive to Barrier
                    if (!robot.driveTrain.isBusy()) {                                                                       // Wait until at hub to start driving to barrier
                        robot.driveTrain.followTrajectoryAsync(hubToBarrier);
                        robot.arm.setLevel(Constants.ArmPosition.INVERSE_TOP);
                        robot.intake.stop();
                        autoState ++;
                    } else {
                        break;
                    }
                }
                case 3: {   // Arm position to Intake Position
                    if (inRange(robot.driveTrain.getPoseEstimate(), barrierPose, 1, Math.toRadians(10))) {  // Wait until in position to lower arm
                        robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED);
                        autoState ++;
                    } else {
                        break;
                    }
                }
                case 4: {   // Drive to Warehouse half
                    if (!robot.driveTrain.isBusy()) {
                        barrierToWarehouseHalf = robot.driveTrain.trajectoryBuilder(robot.driveTrain.getPoseEstimate()) // Wait until at warehouse half to drive full
                                .lineToLinearHeading(warehouseHalfPose)
                                .build();
                        robot.driveTrain.followTrajectoryAsync(barrierToWarehouseHalf);
                        robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED);
                        autoState ++;
                    } else {
                        break;
                    }
                }
                case 5: {   // Drive to Warehouse half
                    if (!robot.arm.isBusy()) {                                                                          // Wait until arm down to continue into warehouse
                        warehouseFullPose = new Pose2d(warehouseHalfPose.getX() + extraAdvance * cycleCounter + firstAdvance, warehouseHalfPose.getY(), warehouseHalfPose.getHeading());
                        barrierToWarehouseFull = robot.driveTrain.trajectoryBuilder(robot.driveTrain.getPoseEstimate())
                                .lineToLinearHeading(warehouseFullPose)
                                .build();
                        robot.driveTrain.followTrajectoryAsync(barrierToWarehouseFull);
                        robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED);
                        robot.intake.intake();
                        autoState ++;
                    } else {
                        break;
                    }
                }
                case 6: {   // Drive to hub
                    if (!robot.driveTrain.isBusy() || robot.intake.hasFreight()) {                                      // Wait until intake gets freight or senses freight
                        if (cycleNum == cycleCounter ++) {
                            break a;
                        } else {
                            warehouseFullToHub = robot.driveTrain.trajectorySequenceBuilder(robot.driveTrain.getPoseEstimate())
                                    .lineToLinearHeading(barrierPose).setTangent(tangentBarrier[0])
                                    .splineToSplineHeading(hubPose, tangentBarrier[1])
                                    .build();
                            robot.driveTrain.followTrajectorySequenceAsync(warehouseFullToHub);
                            robot.intake.stop();
                            robot.arm.setLevel(Constants.ArmPosition.TOP);
                            autoState = 1;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public boolean inRange(Pose2d robotPosition, Pose2d targetPosition, double thresholdDistance, double thresholdHeading) {
        double distance = Math.sqrt(Math.pow(targetPosition.getX() - robotPosition.getX(), 2) + Math.pow(targetPosition.getY() - robotPosition.getY(), 2));
        double headingError = Math.abs(targetPosition.getHeading() - robotPosition.getHeading());
        return distance < thresholdDistance && headingError < thresholdHeading;
    }
}



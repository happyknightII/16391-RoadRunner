package org.firstinspires.ftc.teamcode.drive.opmode.competitioncode.autonomouscode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.drive.Constants;
import org.firstinspires.ftc.teamcode.drive.hardware.Robot;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Red Auto: Warehouse - Around - Right - Short v2", group = "Red")
//@Disabled
public class RedAroundShortv2 extends AutonomousTemplate {
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
        runTimer.reset();
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

        Pose2d hubPose = new Pose2d(-5, -45, Math.toRadians(110));

        Pose2d barrierPose = new Pose2d(20,-66,Math.toRadians(180));

        robot.driveTrain.setPoseEstimate(startPose);


        TrajectorySequence auto = robot.driveTrain.trajectorySequenceBuilder(startPose)
                .addDisplacementMarker( 20, () -> System.out.println("robot.intake.outtake()"))    // Set Intake to eject cube for #1 approach
                .addDisplacementMarker(152, () -> System.out.println("robot.intake.outtake()"))    // Set Intake to eject cube for #2 approach
                .addDisplacementMarker(294, () -> System.out.println("robot.intake.outtake()"))
                .addDisplacementMarker(446, () -> System.out.println("robot.intake.outtake()"))
                .addDisplacementMarker(608, () -> System.out.println("robot.intake.outtake()"))

                .lineToSplineHeading(hubPose).turn(0)                                                      // Starting point -> Hub
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED))

                .splineToSplineHeading(barrierPose, Math.toRadians(0))                                      // Hub -> Warehouse #1
                .addDisplacementMarker(() -> robot.intake.intake())
                .back(30)
                .addDisplacementMarker(() -> robot.intake.stop())
                .forward(30)                                                                                // Warehouse -> Hub
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.TOP))
                .splineToSplineHeading(new Pose2d(-5,-45,Math.toRadians(110)), Math.toRadians(100)).turn(0)
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED))
                .addDisplacementMarker(() -> robot.intake.stop())


                .splineToSplineHeading(barrierPose, Math.toRadians(0))                                      // Hub -> Warehouse #2
                .addDisplacementMarker(() -> robot.intake.intake())
                .back(35)
                .addDisplacementMarker(() -> robot.intake.stop())
                .forward(35)                                                                                // Warehouse -> Hub
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.TOP))
                .splineToSplineHeading(new Pose2d(-5,-45,Math.toRadians(110)), Math.toRadians(100)).turn(0)
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED))
                .addDisplacementMarker(() -> robot.intake.stop())


                .splineToSplineHeading(barrierPose, Math.toRadians(0))                                      // Hub -> Warehouse #3
                .addDisplacementMarker(() -> robot.intake.intake())
                .back(40)
                .addDisplacementMarker(() -> robot.intake.stop())
                .forward(40)                                                                                // Warehouse -> Hub
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.TOP))
                .splineToSplineHeading(new Pose2d(-5,-45,Math.toRadians(110)), Math.toRadians(100)).turn(0)
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED))
                .addDisplacementMarker(() -> robot.intake.stop())


                .splineToSplineHeading(barrierPose, Math.toRadians(0))                                      // Hub -> Warehouse #4
                .addDisplacementMarker(() -> robot.intake.intake())
                .back(45)
                .addDisplacementMarker(() -> robot.intake.stop())
                .forward(45)                                                                                // Warehouse -> Hub
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.TOP))
                .splineToSplineHeading(new Pose2d(-5,-45,Math.toRadians(110)), Math.toRadians(100)).turn(0)
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED))

                .splineToSplineHeading(barrierPose, Math.toRadians(0))                                      // Hub -> Warehouse #5
                .addDisplacementMarker(() -> robot.intake.intake())
                .back(50)
                .addDisplacementMarker(() -> robot.intake.stop())
                .build();

        robot.driveTrain.followTrajectorySequence(auto);
    }
}



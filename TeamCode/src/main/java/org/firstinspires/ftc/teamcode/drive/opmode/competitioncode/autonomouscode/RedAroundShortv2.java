package org.firstinspires.ftc.teamcode.drive.opmode.competitioncode.autonomouscode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.drive.Constants;
import org.firstinspires.ftc.teamcode.drive.hardware.Robot;

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

        robot.driveTrain.setPoseEstimate(startPose);


        Trajectory startAuto = robot.driveTrain.trajectoryBuilder(startPose)
                .addDisplacementMarker(18, () -> robot.intake.outtake())
                .splineToSplineHeading(new Pose2d(-5, -45, Math.toRadians(110)), Math.toRadians(120))
                .addDisplacementMarker(() -> robot.intake.stop())
                .build();

        Trajectory moveToWarehouse = robot.driveTrain.trajectoryBuilder(startAuto.end(), Math.toRadians(280))
                .addDisplacementMarker(2, () -> robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED))
                .splineToSplineHeading(new Pose2d(20,-66,Math.toRadians(180)), Math.toRadians(0))
                .addDisplacementMarker(() -> robot.intake.intake())
                .splineToSplineHeading(new Pose2d(50,-66,Math.toRadians(180)), Math.toRadians(0))
                .addDisplacementMarker(() -> robot.intake.stop())
                .build();

        Trajectory moveToShippingHub = robot.driveTrain.trajectoryBuilder(moveToWarehouse.end(),Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(20,-66,Math.toRadians(180)), Math.toRadians(180))
                .addDisplacementMarker(() -> robot.arm.setLevel(Constants.ArmPosition.TOP))
                .splineToSplineHeading(new Pose2d(-5,-40,Math.toRadians(110)), Math.toRadians(100))
                .addDisplacementMarker(() -> robot.intake.outtake())
                .build();

        robot.driveTrain.followTrajectory(startAuto);
        cycleTimer.reset();
        while (opModeIsActive()) {
            robot.driveTrain.followTrajectory(moveToWarehouse);
            if (cycleTimer.time() > cycleTime) {
                cycleTime = cycleTimer.time();
            }
            cycleTimer.reset();
            if (runTimer.time() + cycleTime > 29.5) {
                break;
            }
            robot.driveTrain.followTrajectory(moveToShippingHub);
        }
    }
}



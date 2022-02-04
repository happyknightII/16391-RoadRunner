package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class MeepMeepTesting {
    static MeepMeep meepMeep = new MeepMeep(800);
    public static void main(String[] args) {

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(RedCarousel.myBot)
                .addEntity(BlueCarousel.myBot)
                .addEntity(RedWarehouse.myBot)
                .addEntity(BlueWarehouse.myBot)
                .start();


    }

    public static class RedWarehouse {
        static Pose2d startPose = new Pose2d(6.44, -63, Math.toRadians(90));

        static Pose2d hubPose = new Pose2d(-5, -45, Math.toRadians(110));

        static Pose2d barrierPose = new Pose2d(25,-64,Math.toRadians(179));
        static double endWarehouseTangent = Math.toRadians(-1);

        static double startWarehouseTangent = Math.toRadians(-80);
        static double endHubTangent = Math.toRadians(100);

        static TrajectorySequence drive = new DriveShim(DriveTrainType.MECANUM, new Constraints(56, 33, Math.toRadians(330), Math.toRadians(110), 8.87), startPose).trajectorySequenceBuilder(startPose).setTangent(Math.toRadians(280))
                .addDisplacementMarker( 20, () -> System.out.println("robot.intake.outtake()"))    // Set Intake to eject cube for #1 approach
                .addDisplacementMarker(158, () -> System.out.println("robot.intake.outtake()"))    // Set Intake to eject cube for #2 approach
                .addDisplacementMarker(302, () -> System.out.println("robot.intake.outtake()"))
                .addDisplacementMarker(452, () -> System.out.println("robot.intake.outtake()"))
                .addDisplacementMarker(608, () -> System.out.println("robot.intake.outtake()"))

                .lineToSplineHeading(hubPose).setTangent(startWarehouseTangent)                                                      // Starting point -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))

                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #1
                //.addDisplacementMarker(() -> System.out.println("robot.intake.intake()"))
                .back(30)
                //.addDisplacementMarker(() -> System.out.println("robot.intake.stop()"))
                .forward(30)                                                                                // Warehouse -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.TOP)"))
                .splineToSplineHeading(hubPose, endHubTangent).setTangent(startWarehouseTangent)
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))


                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #2
                .back(33)
                //.addDisplacementMarker(() -> System.out.println("robot.intake.stop()"))
                .forward(33)                                                                                // Warehouse -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.TOP)"))
                .splineToSplineHeading(hubPose, endHubTangent).setTangent(startWarehouseTangent)
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))


                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #3
                .back(36)
                //.addDisplacementMarker(() -> System.out.println("robot.intake.stop()"))
                .forward(36)                                                                                // Warehouse -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.TOP)"))
                .splineToSplineHeading(hubPose, endHubTangent).setTangent(startWarehouseTangent)
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))


                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #4
                .back(39)
                //.addDisplacementMarker(() -> System.out.println("robot.intake.stop()"))
                .forward(39)                                                                                // Warehouse -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.TOP)"))
                .splineToSplineHeading(hubPose, endHubTangent).setTangent(startWarehouseTangent)
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))

                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #5
                //.addDisplacementMarker(() -> robot.intake.intake())
                .back(41)
                //.addDisplacementMarker(() -> robot.intake.stop())
                .build();

        static RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setColorScheme(new ColorSchemeRedDark())
                .setDimensions(11, 15.5)
                .setConstraints(56, 60, Math.toRadians(330), Math.toRadians(110), 8.87)

                .followTrajectorySequence(drive);
    }

    public static class BlueWarehouse {
        static MeepMeep meepMeep = new MeepMeep(800);

        static Pose2d startPose = new Pose2d(6.44, 63, Math.toRadians(270));

        static Pose2d hubPose = new Pose2d(-5, 45, Math.toRadians(-110));

        static Pose2d barrierPose = new Pose2d(25, 64, Math.toRadians(181));



        static double endWarehouseTangent = Math.toRadians(1);

        static double startWarehouseTangent = Math.toRadians(80);
        static double endHubTangent = Math.toRadians(-100);

        static TrajectorySequence drive = new DriveShim(DriveTrainType.MECANUM, new Constraints(56, 33, Math.toRadians(330), Math.toRadians(110), 8.87), startPose).trajectorySequenceBuilder(startPose).setTangent(Math.toRadians(280))
                .addDisplacementMarker(20, () -> System.out.println("robot.intake.outtake()"))    // Set Intake to eject cube for #1 approach
                .addDisplacementMarker(158, () -> System.out.println("robot.intake.outtake()"))    // Set Intake to eject cube for #2 approach
                .addDisplacementMarker(302, () -> System.out.println("robot.intake.outtake()"))
                .addDisplacementMarker(452, () -> System.out.println("robot.intake.outtake()"))
                .addDisplacementMarker(608, () -> System.out.println("robot.intake.outtake()"))

                .lineToSplineHeading(hubPose).setTangent(startWarehouseTangent)                                                    // Starting point -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))

                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #1
                //.addDisplacementMarker(() -> System.out.println("robot.intake.intake()"))
                .back(30)
                //.addDisplacementMarker(() -> System.out.println("robot.intake.stop()"))
                .forward(30)                                                                                // Warehouse -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.TOP)"))
                .splineToSplineHeading(hubPose, endHubTangent).setTangent(startWarehouseTangent)
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))


                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #2
                .back(33)
                //.addDisplacementMarker(() -> System.out.println("robot.intake.stop()"))
                .forward(33)                                                                                // Warehouse -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.TOP)"))
                .splineToSplineHeading(hubPose, endHubTangent).setTangent(startWarehouseTangent)
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))


                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #3
                .back(36)
                //.addDisplacementMarker(() -> System.out.println("robot.intake.stop()"))
                .forward(36)                                                                                // Warehouse -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.TOP)"))
                .splineToSplineHeading(hubPose, endHubTangent).setTangent(startWarehouseTangent)
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))


                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #4
                .back(39)
                //.addDisplacementMarker(() -> System.out.println("robot.intake.stop()"))
                .forward(39)                                                                                // Warehouse -> Hub
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.TOP)"))
                .splineToSplineHeading(hubPose, endHubTangent).setTangent(startWarehouseTangent)
                //.addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.INVERSE_FEED)"))

                .splineToSplineHeading(barrierPose, endWarehouseTangent)                                      // Hub -> Warehouse #5
                //.addDisplacementMarker(() -> robot.intake.intake())
                .back(41)
                //.addDisplacementMarker(() -> robot.intake.stop())
                .build();

        static RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setColorScheme(new ColorSchemeBlueDark())
                .setDimensions(11, 15.5)
                .setConstraints(56, 60, Math.toRadians(330), Math.toRadians(110), 8.87)

                .followTrajectorySequence(drive);
    }

    public static class RedCarousel {
        static Pose2d startPose = new Pose2d(-25, -63, Math.toRadians(90));

        static Pose2d hubPose = new Pose2d(-20, -45, Math.toRadians(70));

        static Pose2d carouselPose = new Pose2d(-61,-55,Math.toRadians(0));

        static double startCarouselTangent = Math.toRadians(-160);
        static double endCarouselTangent = Math.toRadians(180);

        static TrajectorySequence drive = new DriveShim(DriveTrainType.MECANUM, new Constraints(30, 20, Math.toRadians(330), Math.toRadians(110), 8.87), startPose).trajectorySequenceBuilder(startPose).setTangent(Math.toRadians(280))

                .lineToSplineHeading(hubPose).setTangent(startCarouselTangent)                                                      // Starting point -> Hub
                .addDisplacementMarker(() -> System.out.println("robot.intake.outtake(-1)"))
                .waitSeconds(0.5)
                .addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.FEED)"))
                .waitSeconds(0.5)
                .splineToSplineHeading(carouselPose, endCarouselTangent)
                .addDisplacementMarker(() -> System.out.println("robot.carousel.setPower(0.3)"))
                .strafeRight(6)
                .waitSeconds(4)
                .strafeLeft(26)
                .build();

        static RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setColorScheme(new ColorSchemeRedDark())
                .setDimensions(11, 15.5)
                .setConstraints(56, 60, Math.toRadians(330), Math.toRadians(110), 8.87)

                .followTrajectorySequence(drive);
    }
    
    public static class BlueCarousel {
        static Pose2d startPose = new Pose2d(-25, 63, Math.toRadians(-90));

        static Pose2d hubPose = new Pose2d(-20, 45, Math.toRadians(-70));

        static Pose2d carouselPose = new Pose2d(-61,55,Math.toRadians(180));

        static double startCarouselTangent = Math.toRadians(160);
        static double endCarouselTangent = Math.toRadians(180);

        static TrajectorySequence drive = new DriveShim(DriveTrainType.MECANUM, new Constraints(30, 20, Math.toRadians(330), Math.toRadians(110), 8.87), startPose).trajectorySequenceBuilder(startPose).setTangent(Math.toRadians(280))

                .lineToSplineHeading(hubPose).setTangent(startCarouselTangent)                                                      // Starting point -> Hub
                .addDisplacementMarker(() -> System.out.println("robot.intake.outtake(-1)"))
                .waitSeconds(0.5)
                .addDisplacementMarker(() -> System.out.println("robot.arm.setLevel(Constants.ArmPosition.FEED)"))
                .waitSeconds(0.5)
                .splineToSplineHeading(carouselPose, endCarouselTangent)
                .addDisplacementMarker(() -> System.out.println("robot.carousel.setPower(0.3)"))
                .strafeRight(6)
                .waitSeconds(4)
                .strafeLeft(26)
                .build();

        static RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setColorScheme(new ColorSchemeBlueDark())
                .setDimensions(11, 15.5)
                .setConstraints(56, 60, Math.toRadians(330), Math.toRadians(110), 8.87)

                .followTrajectorySequence(drive);
    }
}
package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.sun.org.apache.bcel.internal.Const;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        Pose2d startPose = new Pose2d(-5, -45, Math.toRadians(110));

        TrajectorySequence drive = new DriveShim(DriveTrainType.MECANUM, new Constraints(56, 60, Math.toRadians(330), Math.toRadians(110), 8.87), startPose).trajectorySequenceBuilder(startPose).setTangent(Math.toRadians(280))
                .splineToSplineHeading(new Pose2d(20,-66,Math.toRadians(180)), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(50,-66,Math.toRadians(180)), Math.toRadians(0))
                .build();
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setDimensions(11, 15.5)
                .setConstraints(56, 60, Math.toRadians(330), Math.toRadians(110), 8.87)
                .followTrajectorySequence(drive);

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
package org.firstinspires.ftc.teamcode.drive.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Carousel {
    HardwareMap hwMap;
    DcMotor carousel;

    public Carousel () {}

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;
        carousel    = hwMap.get(DcMotor.class, "carousel");
        carousel    .setPower(0);
        carousel    .setDirection(DcMotor.Direction.FORWARD);
        carousel    .setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        carousel    .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        carousel    .setMode(DcMotor.RunMode. RUN_USING_ENCODER);
    }

    public void setPower(double power) {
        carousel.setPower(power);
    }

}

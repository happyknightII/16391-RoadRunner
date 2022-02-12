package org.firstinspires.ftc.teamcode.drive.hardware;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Intake {
    private DcMotorEx leftMotor;
    private DcMotorEx rightMotor;
    private DigitalChannel touch;
    private final double INTAKE_POWER = 1;
    private final double OUTTAKE_POWER = -.2;
    private final double CURRENT_THREASHOLD = 8;


    HardwareMap hwMap = null;


    public Intake() {}

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        leftMotor = hwMap.get(DcMotorEx.class, "feeder left");
        rightMotor = hwMap.get(DcMotorEx.class, "feeder right");
        touch = hwMap.get(DigitalChannel.class, "touch");

        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightMotor.setDirection(DcMotorEx.Direction.REVERSE);

        leftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        rightMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        leftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        leftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void stop() {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    public void intake() {
        leftMotor.setPower(INTAKE_POWER);
        rightMotor.setPower(INTAKE_POWER);
    }

    public void outtake() {
        leftMotor.setPower(OUTTAKE_POWER);
        rightMotor.setPower(OUTTAKE_POWER);
    }

    public boolean hasFreight() {

        return !touch.getState();
    }
    public boolean hasFreightCurrent() {
        return rightMotor.getCurrent(CurrentUnit.AMPS) > CURRENT_THREASHOLD
                && leftMotor.getCurrent(CurrentUnit.AMPS) > CURRENT_THREASHOLD
                && hasFreight();
    }


}

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MecanumWheels {

    //based on this image: https://i.imgur.com/R82YOwT.png

    //wheel motor power
    public double wheelFrontRightPower = 0;
    public double wheelFrontLeftPower = 0;
    public double wheelBackRightPower = 0;
    public double wheelBackLeftPower = 0;

    public MecanumWheels(){ }

    public void joystick(Gamepad gamepad1, double speed){
        double r = Math.hypot(gamepad1.right_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4;
        double rightX = -gamepad1.right_stick_y;
        wheelFrontLeftPower = (r * Math.cos(robotAngle) + rightX) / speed;
        wheelFrontRightPower = (r * Math.sin(robotAngle) - rightX) / speed;
        wheelBackLeftPower = (r * Math.sin(robotAngle) + rightX) / speed;
        wheelBackRightPower = (r * Math.cos(robotAngle) - rightX) / speed;
    }

    public void forward(double speed){
        wheelFrontRightPower = 1 / speed;
        wheelFrontLeftPower = 1 / speed;
        wheelBackRightPower = 1 / speed;
        wheelBackLeftPower = 1 / speed;
    }

    public void backwards(double speed){
        wheelFrontRightPower = -1 / speed;
        wheelFrontLeftPower = -1 / speed;
        wheelBackRightPower = -1 / speed;
        wheelBackLeftPower = -1 / speed;
    }

    public void strafeRight(double speed){
        wheelFrontRightPower = -1 / speed;
        wheelFrontLeftPower = 1 / speed;
        wheelBackRightPower = 1 / speed;
        wheelBackLeftPower = -1 / speed;
    }

    public void strafeLeft(double speed){
        wheelFrontRightPower = 1 / speed;
        wheelFrontLeftPower = -1 / speed;
        wheelBackRightPower = -1 / speed;
        wheelBackLeftPower = 1 / speed;
    }

    public void turnRight(double speed){
        wheelFrontRightPower = -1 / speed;
        wheelFrontLeftPower = 1 / speed;
        wheelBackRightPower = -1 / speed;
        wheelBackLeftPower = 1 / speed;
    }

    public void turnLeft(double speed){
        wheelFrontRightPower = 1 / speed;
        wheelFrontLeftPower = -1 / speed;
        wheelBackRightPower = 1 / speed;
        wheelBackLeftPower = -1 / speed;
    }

    public void tiltRight(double speed){
        wheelFrontLeftPower = 1 / speed;
        wheelFrontRightPower = 0;
        wheelBackRightPower = 1 / speed;
        wheelBackLeftPower = 0;
    }

    public void tiltLeft(double speed){
        wheelBackLeftPower = 1 / speed;
        wheelBackRightPower = 0;
        wheelFrontRightPower = 1 / speed;
        wheelBackLeftPower = 0;
    }

    public void stop(){
        wheelBackLeftPower = 0;
        wheelBackRightPower = 0;
        wheelFrontRightPower = 0;
        wheelFrontLeftPower = 0;
    }
}

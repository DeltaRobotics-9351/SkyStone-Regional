package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MecanumWheels {

    //based on this image: https://i.imgur.com/R82YOwT.png

    //wheel motor power
    public double wheelFrontRightPower = 0;
    public double wheelFrontLeftPower = 0;
    public double wheelBackRightPower = 0;
    public double wheelBackLeftPower = 0;

    public double speed = 0;

    public MecanumWheels(){ }

    public void joystick(Gamepad gamepad1, double speed){
        this.speed = speed;
        double r = Math.hypot(gamepad1.right_stick_x, gamepad1.left_stick_x);
        double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.right_stick_x) - Math.PI / 4;
        double rightX = -gamepad1.right_stick_y;
        wheelFrontLeftPower = (r * Math.cos(robotAngle) + rightX);
        wheelFrontRightPower = (r * Math.sin(robotAngle) - rightX);
        wheelBackLeftPower = (r * Math.sin(robotAngle) + rightX);
        wheelBackRightPower = (r * Math.cos(robotAngle) - rightX);
    }

    public void forward(double speed){
        this.speed = speed;
        wheelFrontRightPower = 1 / speed;
        wheelFrontLeftPower = 1 / speed;
        wheelBackRightPower = 1 / speed;
        wheelBackLeftPower = 1 / speed;
    }

    public void backwards(double speed){
        this.speed = speed;
        wheelFrontRightPower = -1 / speed;
        wheelFrontLeftPower = -1 / speed;
        wheelBackRightPower = -1 / speed;
        wheelBackLeftPower = -1 / speed;
    }

    public void strafeRight(double speed){
        this.speed = speed;
        wheelFrontRightPower = -1 / speed;
        wheelFrontLeftPower = 1 / speed;
        wheelBackRightPower = 1 / speed;
        wheelBackLeftPower = -1 / speed;
    }

    public void strafeLeft(double speed){
        this.speed = speed;
        wheelFrontRightPower = 1 / speed;
        wheelFrontLeftPower = -1 / speed;
        wheelBackRightPower = -1 / speed;
        wheelBackLeftPower = 1 / speed;
    }

    public void turnRight(double speed){
        this.speed = speed;
        wheelFrontRightPower = -1 / speed;
        wheelFrontLeftPower = 1 / speed;
        wheelBackRightPower = -1 / speed;
        wheelBackLeftPower = 1 / speed;
    }

    public void turnLeft(double speed){
        this.speed = speed;
        wheelFrontRightPower = 1 / speed;
        wheelFrontLeftPower = -1 / speed;
        wheelBackRightPower = 1 / speed;
        wheelBackLeftPower = -1 / speed;
    }

    public void tiltRight(double speed){
        this.speed = speed;
        wheelFrontLeftPower = 1 / speed;
        wheelFrontRightPower = 0;
        wheelBackRightPower = 1 / speed;
        wheelBackLeftPower = 0;
    }

    public void tiltLeft(double speed){
        this.speed = speed;
        wheelBackLeftPower = 1 / speed;
        wheelBackRightPower = 0;
        wheelFrontRightPower = 1 / speed;
        wheelBackLeftPower = 0;
    }

    public void stop(){
        this.speed = speed;
        wheelBackLeftPower = 0;
        wheelBackRightPower = 0;
        wheelFrontRightPower = 0;
        wheelFrontLeftPower = 0;
    }
}

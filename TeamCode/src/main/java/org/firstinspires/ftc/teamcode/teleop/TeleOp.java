package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.MecanumWheels;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="TeleOps") //se define que la clase se trata de un teleop con una annotation
public class TeleOp extends LinearOpMode { //la clase extendera a otra llamada 'LinearOpMode'

    //objeto que contiene el hardware del robot
    Hardware hdw;

    long runmillis;
    long disappearmillis;

    MecanumWheels mecanumWheels;

    @Override
    public void runOpMode(){
        hdw = new Hardware(hardwareMap); //init hardware
        hdw.createHardware();

        mecanumWheels = new MecanumWheels();
        telemetry.addData("[>]", "All set?"); //manda un mensaje a la driver station
        telemetry.update();

        waitForStart(); //espera hasta que se presione <play> en la driver station

        runmillis = System.currentTimeMillis();
        disappearmillis = runmillis + (5 * 1000); //el tiempo en el que desaparecera el mensaje "GO!!!"

        while(opModeIsActive()){

            if(System.currentTimeMillis() < disappearmillis) { //se ejecuta cuando no hayan pasado mas de 3 segundos (3000 ms) desde que se dio a <play>
                telemetry.addData("[>]", "GO!!!");
            }

            startA(); //movimientos del start A
            startB();//movimientos del start B

            telemetry.addData("wheelFrontRightPower", mecanumWheels.wheelFrontRightPower);
            telemetry.addData("wheelFrontLeftPower", mecanumWheels.wheelFrontLeftPower);
            telemetry.addData("wheelBackRightPower", mecanumWheels.wheelBackRightPower);
            telemetry.addData("wheelBackLeftPower", mecanumWheels.wheelBackLeftPower);
            telemetry.addLine().addData("servoFoundationRight", hdw.servoFoundationRight.getPosition());
            telemetry.addData("servoFoundationLeft", hdw.servoFoundationLeft.getPosition());
            telemetry.addData("servoClaw", hdw.servoClaw.getPosition());

            //set power de los motores
            hdw.wheelFrontRight.setPower(mecanumWheels.wheelFrontRightPower);
            hdw.wheelFrontLeft.setPower(mecanumWheels.wheelFrontLeftPower);
            hdw.wheelBackRight.setPower(mecanumWheels.wheelBackRightPower);
            hdw.wheelBackLeft.setPower(mecanumWheels.wheelBackLeftPower);

            telemetry.update();  //manda los mensajes telemetry a la driver station
        }

    }

    public void startA() {
        //si cualquiera de los 2 triggers es presionado (mayor que 0), el robot avanzara a la mitad
        //de velocidad. el fin de esto es para que el arrastrar la foundation en el endgame no sea
        //tan arriesgado y haya menos probabilidad de que tiremos cualquier stone
        if (gamepad1.left_trigger > 0 || gamepad1.right_trigger > 0) {
            mecanumWheels.joystick(gamepad1, 0.5);
        } else {
            mecanumWheels.joystick(gamepad1, 1);
        }

        //servos con perfil que agarraran la foundation para arrastrarla
        if(gamepad1.dpad_up){
            hdw.servoFoundationRight.setPosition(0);    
            hdw.servoFoundationLeft.setPosition(0);      
        }else if(gamepad1.dpad_down){
            hdw.servoFoundationRight.setPosition(1);
            hdw.servoFoundationLeft.setPosition(1);
        }
    }

    public void startB() {
        //servo de la garra
        if (gamepad2.a) {
            hdw.servoClaw.setPosition(1);
        } else if (gamepad2.b) {
            hdw.servoClaw.setPosition(0);
        }

        //articulacion de la garra
        if (gamepad2.left_trigger > 0.1) {
            hdw.motorArtiClaw.setPower(-0.1);
        } else if (gamepad2.right_trigger > 0.1) {
            hdw.motorArtiClaw.setPower(0.1);
        }else{
            hdw.motorArtiClaw.setPower(0);
        }

        //lift de la articulacion de la garra
        if(gamepad2.dpad_up){
            hdw.motorLift.setPower(1);
        }else if(gamepad2.dpad_down){
            hdw.motorLift.setPower(-1);
        }else{
            hdw.motorLift.setPower(0);
        }

        telemetry.addData("hola", gamepad2.left_trigger + ", " + gamepad2.right_trigger );
        telemetry.addData("motorLift", hdw.motorLift.getPower());

    }

}

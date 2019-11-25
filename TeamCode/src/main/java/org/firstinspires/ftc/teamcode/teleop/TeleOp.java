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
        hdw.initHardware(false);

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
            telemetry.addData("wheels turbo", mecanumWheels.turbo);
            telemetry.addData("servoStoneAutonomous position", hdw.servoStoneAutonomous.getPosition());

            //set power de los motores
            hdw.wheelFrontRight.setPower(mecanumWheels.wheelFrontRightPower);
            hdw.wheelFrontLeft.setPower(mecanumWheels.wheelFrontLeftPower);
            hdw.wheelBackRight.setPower(mecanumWheels.wheelBackRightPower);
            hdw.wheelBackLeft.setPower(mecanumWheels.wheelBackLeftPower);

            telemetry.update();  //manda los mensajes telemetry a la driver station
        }

    }

    public void startA() {
        //si cualquiera de los 2 triggers es presionado (mayor que 0), el robot avanzara al 30%
        //de velocidad. el fin de esto es para que el arrastrar la foundation en el endgame no sea
        //tan arriesgado y haya menos probabilidad de que tiremos cualquier stone
        if (gamepad1.left_trigger > 0.2 || gamepad1.right_trigger > 0.2) {
            mecanumWheels.joystick(gamepad1, 0.3);
        } else {
            mecanumWheels.joystick(gamepad1, 1);
        }

    }

    public void startB() {
        //intake
        if (gamepad2.a) {
            hdw.motorIntakeLeft.setPower(1);
            hdw.motorIntakeRight.setPower(1);
        } else if (gamepad2.b) {
            hdw.motorIntakeLeft.setPower(-0.5);
            hdw.motorIntakeRight.setPower(-0.5);
        }else{
            hdw.motorIntakeLeft.setPower(0);
            hdw.motorIntakeRight.setPower(0);
        }

        //servo para arrastrar las stones
        if(gamepad2.x){
            hdw.servoStoneAutonomous.setPosition(hdw.servoStoneAutonomous.getPosition() + 0.05);
        }else if(gamepad2.y){
            hdw.servoStoneAutonomous.setPosition(hdw.servoStoneAutonomous.getPosition() - 0.05);
        }

        //slider del intake
        if(gamepad2.right_trigger > 0.1) {
            hdw.motorSliders.setPower(gamepad2.right_trigger);
        }else if(gamepad2.left_trigger > 0.1){
            hdw.motorSliders.setPower(-gamepad2.left_trigger);
        }else{
            hdw.motorSliders.setPower(0);
        }

    }

}

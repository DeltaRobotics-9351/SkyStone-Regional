package org.firstinspires.ftc.teamcode.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.TelemetryMessage;
import org.firstinspires.ftc.teamcode.hardware.EncoderDriveMecanum;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipeline;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Autonomo Skystone Rojo", group="Final")
public class AutonomoSkystoneRojo extends LinearOpMode {

    private OpenCvCamera phoneCam;
    private SkystonePatternPipeline patternPipeline;
    private Hardware hdw;

    int pattern = 0;

    @Override
    public void runOpMode() {
        hdw = new Hardware(hardwareMap);
        hdw.initHardware(false);

        //creamos la vista desde el celular de la camara
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        //creamos la camara de OpenCV
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        //la inicializamos
        phoneCam.openCameraDevice();

        //creamos la pippeline
        patternPipeline = new SkystonePatternPipeline();
        //definimos la pipeline para la camara
        phoneCam.setPipeline(patternPipeline);

        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        telemetry.addData("[/!\\]", "Recuerden posicionar correctamente el robot, con los dos rectangulos que se ven en la camara apuntando justo hacia las dos ultimas stones de la quarry (las mas cercanas a el skybridge)\n\nGOOO DELTA ROBOTICS!!!");
        telemetry.update();

        //esperamos que el usuario presione <play> en la driver station
        waitForStart();

        //mientras que el pattern sea 0 (si es 0 significa que no ha detectado ningun pattern) se entrara en un bucle while
        while(patternPipeline.pattern == 0 && opModeIsActive()){
            telemetry.addData("[ERROR]", "Se ha posicionado de forma erronea el robot... Lo siento =(");
            telemetry.update();
        }

        pattern = patternPipeline.pattern;

        phoneCam.closeCameraDevice(); //apagamos la camara ya que no es necesaria a partir de este punto.

        telemetry.addData("Pattern", pattern);
        telemetry.update();

        if(pattern == 1){

            strafeLeft(0.5, 0.2);
            backwards(0.5,0.8);

            hdw.servoStoneAutonomous.setPosition(1f);
            sleep((long)2000);

            forward(0.5,0.9);
            turnRight(0.5, 0.8);
            backwards(0.5,1.5);

            hdw.servoStoneAutonomous.setPosition(0);
            sleep((long)2000);

            forward(0.5, 1.7);
            turnLeft(0.5, 0.8);
            backwards(0.5,0.8);

            hdw.servoStoneAutonomous.setPosition(1f);
            sleep((long)2000);
            forward(0.5,0.9);
            turnRight(0.5, 0.8);
            backwards(0.5,0.7);

        }else if(pattern == 2){

            backwards(0.5,0.9);

            hdw.servoStoneAutonomous.setPosition(1f);
            sleep((long)2000);

            forward(0.5,0.9);
            turnRight(0.5, 0.8);
            backwards(0.5,1.5);

            hdw.servoStoneAutonomous.setPosition(0);
            sleep((long)2000);

            forward(0.5, 1.8);
            sleep((long)1000);
            turnLeft(0.5, 0.8);
            backwards(0.5,0.8);

            hdw.servoStoneAutonomous.setPosition(1f);
            sleep((long)2000);

            forward(0.5,0.9);
            sleep((long)1000);
            turnRight(0.5, 0.8);
            sleep((long)1000);
            backwards(0.5,1.8);

            hdw.servoStoneAutonomous.setPosition(0);
            sleep((long)2000);

            forward(0.5,0.8);

        }else if(pattern == 3){

        }else{
            telemetry.addData("[ERROR]", "No se que ha pasado ni como has llegado hasta aqui. Lo siento =(");
            telemetry.update();
            while(opModeIsActive());
        }
    }

    //se define el power de todos los motores y el tiempo en el que avanzaran a este power
    public void setAllWheelPower(double frontleft, double frontright, double backleft, double backright, double time, String movementDescription){

        hdw.allWheelsForward();
        hdw.wheelFrontLeft.setPower(frontleft);
        hdw.wheelFrontRight.setPower(frontright);
        hdw.wheelBackLeft.setPower(backleft);
        hdw.wheelBackRight.setPower(backright);

        telemetry.addData("Pattern", pattern);
        telemetry.addData("movement", movementDescription);
        telemetry.addData("frontleft", -frontleft);
        telemetry.addData("frontright", frontright);
        telemetry.addData("backleft", backleft);
        telemetry.addData("backright", backright);
        telemetry.addData("time", time);
        telemetry.update();

        sleep((long) (time * 1000));

        hdw.wheelFrontLeft.setPower(0);
        hdw.wheelFrontRight.setPower(0);
        hdw.wheelBackLeft.setPower(0);
        hdw.wheelBackRight.setPower(0);

        telemetry.addData("Pattern", pattern);
        telemetry.addData("frontleft", 0);
        telemetry.addData("frontright", 0);
        telemetry.addData("backleft", 0);
        telemetry.addData("backright", 0);
        telemetry.update();
        hdw.defaultWheelsDirection();
    }

    //hacia adelante
    public void forward(double power, double time) {
        setAllWheelPower(-power, power, power, power, time, "forward");
    }

    //hacia atras
    public void backwards(double power, double time) {
        setAllWheelPower(power, -power, -power, -power, time, "backwards");
    }

    //deslizarse a la derecha
    public void strafeLeft(double power, double time) {
        setAllWheelPower(-power, -power, -power, power, time, "strafeLeft");
    }

    //deslizarse a la izquierda
    public void strafeRight(double power, double time) {
        setAllWheelPower(power, power, power, -power, time, "strafeRight");
    }

    //girar a la derecha
    public void turnRight(double power, double time) {
        setAllWheelPower(-power, -power, power, -power, time, "turnRight");
    }

    //girar a la izquierda
    public void turnLeft(double power, double time) {
        setAllWheelPower(power, power, -power, power, time, "turnLeft");
    }
}
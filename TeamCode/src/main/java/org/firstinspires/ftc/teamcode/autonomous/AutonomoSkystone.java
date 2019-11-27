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

@Autonomous(name="Autonomo Skystones", group="Test")
public class AutonomoSkystone extends LinearOpMode {

    private OpenCvCamera phoneCam;
    private SkystonePatternPipeline patternPipeline;
    private Hardware hdw;
    private EncoderDriveMecanum eDrive;

    @Override
    public void runOpMode() {
        hdw = new Hardware(hardwareMap);
        eDrive = new EncoderDriveMecanum(hdw, telemetry);

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

        //esperamos que el usuario presione <play> en la driver station
        waitForStart();

        while(patternPipeline.pattern == 0 && opModeIsActive()){
            telemetry.addData("[ERROR]", "Se ha posicionado de forma erronea el robot... Lo siento =(");
            telemetry.update();
        }

        int pattern = patternPipeline.pattern;

        phoneCam.closeCameraDevice(); //apagamos la camara ya que no es necesaria a partir de este punto.

        telemetry.addData("Pattern", pattern);
        telemetry.update();

        eDrive.extraTelemetryMessages.add(new TelemetryMessage("Pattern", pattern));

        if(pattern == 1){

        }else if(pattern == 2){

        }else if(pattern == 3){

        }else{
            telemetry.addData("[ERROR]", "No se que ha pasado ni como has llegado hasta aqui. Lo siento =(");
            telemetry.update();
            while(opModeIsActive());
        }
    }
}
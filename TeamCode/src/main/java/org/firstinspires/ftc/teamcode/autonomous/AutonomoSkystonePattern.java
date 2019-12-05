package org.firstinspires.ftc.teamcode.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Disabled //lo desabilite ya que solo lo use para probar el programa de vision
@Autonomous(name="Autonomo Skystone Pattern", group="OpenCV")
public class AutonomoSkystonePattern extends LinearOpMode {

    private OpenCvCamera phoneCam;
    private SkystonePatternPipeline patternPipeline;

    @Override
    public void runOpMode() {
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

        while (opModeIsActive()) {
            //enviamos mensajes telemetry que contienen informacion
            telemetry.addData("PATTERN", patternPipeline.pattern);
            telemetry.addData("LEFT COLOR", patternPipeline.valLeft);
            telemetry.addData("RIGHT COLOR", patternPipeline.valRight);
            telemetry.addData("FPS", String.format("%.2f", phoneCam.getFps()));
            telemetry.addData("MAX FPS", phoneCam.getCurrentPipelineMaxFps());
            telemetry.update();
        }
    }
}
package org.firstinspires.ftc.teamcode.autonomous;


import com.disnodeteam.dogecv.detectors.skystone.SkystoneDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.Locale;

@Autonomous(name="Autonomo DogeCV Test", group="DogeCV")

public class AutonomoDogeCV extends LinearOpMode {
    private OpenCvCamera phoneCam;
    private SkystoneDetector skyStoneDetector;

    @Override
    public void runOpMode() {
        //creamos la vista desde el celular de la camara
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        //creamos la camara de OpenCV
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        //la inicializamos
        phoneCam.openCameraDevice();

        //creamos el detector de skystones
        skyStoneDetector = new SkystoneDetector();
        phoneCam.setPipeline(skyStoneDetector); //definimos que la pipeline sera el skystonedetector
                                                //es decir, los frames iran hacia este.

        //empezamos a trasmitir la camara del celular trasera a la pantalla del robot controller
        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        //esperamos que el usuario presione <play> en la driver station
        waitForStart();

        while (opModeIsActive()) {
            //enviamos mensajes telemetry que contienen informacion
            telemetry.addData("Stone Position X", skyStoneDetector.getScreenPosition().x);
            telemetry.addData("Stone Position Y", skyStoneDetector.getScreenPosition().y);
            telemetry.addData("Frame Count", phoneCam.getFrameCount());
            telemetry.addData("FPS", String.format(Locale.US, "%.2f", phoneCam.getFps()));
            telemetry.addData("Total frame time ms", phoneCam.getTotalFrameTimeMs());
            telemetry.addData("Pipeline time ms", phoneCam.getPipelineTimeMs());
            telemetry.addData("Overhead time ms", phoneCam.getOverheadTimeMs());
            telemetry.addData("Theoretical max FPS", phoneCam.getCurrentPipelineMaxFps());
            telemetry.update();
        }
    }
}
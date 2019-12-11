package org.firstinspires.ftc.teamcode.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.IMUDriveMecanum;
import org.firstinspires.ftc.teamcode.hardware.TimeDriveMecanum;
import org.firstinspires.ftc.teamcode.pipeline.SkystonePatternPipelineRojo;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Autonomo Skystone Rojo", group="Final")
public class AutonomoSkystoneRojo extends LinearOpMode {

    private OpenCvCamera phoneCam;
    private SkystonePatternPipelineRojo patternPipeline;
    private Hardware hdw;
    private TimeDriveMecanum timeDrive; //en este objeto se encuentran todas las funciones para
                                        //el movimiento de las llantas mecanum con tiempo para
                                        //mantener el codigo mas organizado y facil de cambiar.
    private IMUDriveMecanum imuTurn;
    int pattern = 0;

    @Override
    public void runOpMode() {
        hdw = new Hardware(hardwareMap); //creamos el hardware
        hdw.initHardware(false); //lo inicializamos

        imuTurn = new IMUDriveMecanum(hdw, telemetry);
        timeDrive = new TimeDriveMecanum(hdw, telemetry); //el objeto necesita el hardware para definir el power
                                                          //a los motores y el telemetry para mandar mensajes.

        imuTurn.initIMU();

        telemetry.addData("[/!\\]", "Calibrando el sensor IMU, espera...");
        telemetry.update();

        imuTurn.waitForIMUCalibration();

        //obtenemos la id del monitor de la camara (la vista de la camara que se vera desde el robot controller)
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        //creamos la camara de OpenCV
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        //la inicializamos
        phoneCam.openCameraDevice();

        //creamos la pipeline
        patternPipeline = new SkystonePatternPipelineRojo();

        //definimos la pipeline para la camara
        phoneCam.setPipeline(patternPipeline);

        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        telemetry.addData("[/!\\]", "Recuerden posicionar correctamente el robot, con los dos rectangulos que se ven en la camara apuntando justo hacia las dos ultimas stones de la quarry (las mas cercanas a el skybridge)\n\nGOOO DELTA ROBOTICS!!!");
        telemetry.update();

        //esperamos que el usuario presione <play> en la driver station
        waitForStart();

        //si el pattern es 0 (si es 0 significa que no ha detectado ningun pattern) simplemente nos estacionaremos debajo del skybridge
        if(patternPipeline.pattern == 0){
            telemetry.addData("[ERROR]", "Se ha posicionado de forma erronea el robot... Me estacionare para al menos hacer algo =)");
            telemetry.update();
            timeDrive.backwards(0.6, 0.4);
            timeDrive.turnLeft(0.6, 0.8);
            sleep(1000);
            timeDrive.backwards(0.6, 1);
            while(opModeIsActive());
        }

        pattern = patternPipeline.pattern;

        phoneCam.closeCameraDevice(); //apagamos la camara ya que no es necesaria a partir de este punto.

        telemetry.addData("Pattern", pattern); //mandamos mensaje telemetry para reportar que ya se detecto un patron
        telemetry.update();

        if(pattern == 1){

            timeDrive.strafeRight(0.6, 0.5);

            sleep(500);

            timeDrive.backwards(0.6,0.9);

            sleep((long)100);
            hdw.servoStoneAutonomous.setPosition(0.6f);
            sleep((long)1000);

            timeDrive.forward(0.6,0.6);
            sleep((long)1000);
            imuTurn.rotate(-90, 0.2);
            timeDrive.backwards(0.6,1.7);

            hdw.servoStoneAutonomous.setPosition(0);
            sleep((long)1000);

            timeDrive.forward(0.6, 2.1);
            sleep((long)1000);
            imuTurn.rotate(90, 0.2);
            timeDrive.backwards(0.6,0.9);

            sleep((long)100);
            hdw.servoStoneAutonomous.setPosition(0.6f);
            sleep((long)1000);

            timeDrive.forward(0.6,0.6);
            sleep((long)1000);
            imuTurn.rotate(-90, 0.2);
            timeDrive.backwards(0.6,1.9);

            sleep((long)1000);
            hdw.servoStoneAutonomous.setPosition(0);
            sleep((long)1000);

            timeDrive.forward(0.6,0.4);

        }else if(pattern == 2){

            timeDrive.backwards(0.6,0.9);

            sleep((long)100);
            hdw.servoStoneAutonomous.setPosition(0.6f);
            sleep((long)1000);

            timeDrive.forward(0.6,0.6);
            sleep((long)1000);
            imuTurn.rotate(-90, 0.2);
            timeDrive.backwards(0.6,1.7);

            hdw.servoStoneAutonomous.setPosition(0);
            sleep((long)1000);

            timeDrive.forward(0.6, 2.2);
            sleep((long)1000);
            imuTurn.rotate(90, 0.2);
            timeDrive.backwards(0.6,0.9);

            sleep((long)100);
            hdw.servoStoneAutonomous.setPosition(0.6f);
            sleep((long)1000);

            timeDrive.forward(0.6,0.9);
            sleep((long)1000);
            imuTurn.rotate(-90, 0.2);
            timeDrive.backwards(0.6,1.9);

            sleep((long)1000);
            hdw.servoStoneAutonomous.setPosition(0);
            sleep((long)1000);

            timeDrive.forward(0.6,0.4);

        }else if(pattern == 3){

            timeDrive.strafeLeft(0.4, 0.6);

            sleep(700);

            timeDrive.backwards(0.6,1.1);

            sleep((long)100);
            hdw.servoStoneAutonomous.setPosition(0.6f);
            sleep((long)1000);

            timeDrive.forward(0.6,0.6);
            sleep((long)1000);
            imuTurn.rotate(-90, 0.2);
            timeDrive.backwards(0.6,1.4);

            hdw.servoStoneAutonomous.setPosition(0);
            sleep((long)1000);

            timeDrive.forward(0.6, 2);
            sleep((long)1000);
            imuTurn.rotate(90, 0.2);
            timeDrive.backwards(0.6,0.9);

            sleep((long)100);
            hdw.servoStoneAutonomous.setPosition(0.6f);
            sleep((long)1000);

            timeDrive.forward(0.6,1.2);
            sleep((long)1000);
            imuTurn.rotate(-90, 0.2);
            timeDrive.backwards(0.6,1.9);

            sleep((long)1000);
            hdw.servoStoneAutonomous.setPosition(0);
            sleep((long)1000);

            timeDrive.forward(0.6,0.5);

        }else{
            //en teoria este codigo nunca se deberia de ejecutar, pero por si las dudas...
            telemetry.addData("[ERROR]", "No se que ha pasado ni como has llegado hasta aqui. Lo siento =(");
            telemetry.update();
            while(opModeIsActive());
        }
    }

    //Codigo del autonomo en la posibilidad 2 (Pattern B).
    //A partir de este simplemente te deslizas para la izquierda
    //o derecha para hacer las otras 2 posibilidades (Pattern A y Pattern C)
    public void goForSkystoneRojo(){

    }

}
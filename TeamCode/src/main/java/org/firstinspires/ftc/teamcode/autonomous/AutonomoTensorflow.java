/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Stone;
import org.firstinspires.ftc.teamcode.VuforiaKey;
import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.MecanumWheels;

import com.qualcomm.robotcore.hardware.VoltageSensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "Autonomo TensorFlow", group = "Autonomos")
@Disabled
public class AutonomoTensorflow extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_STONE = "Stone";
    private static final String LABEL_SKYSTONE = "Skystone";

    //objeto que contiene el hardware del robot
    Hardware hdw;

    MecanumWheels mecanumWheels;

    //engine localizador de vuforia
    private VuforiaLocalizer vuforia;

    //engine de deteccion de objetos de tensorflow
    private TFObjectDetector tfod = null;

    @Override
    public void runOpMode() {

        hdw = new Hardware(hardwareMap); //init hardware

        mecanumWheels = new MecanumWheels();

        // tensorflow usa la camara de vuforia, asi que hacemos esto primero
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            //si el dispositivo no es compatible con tensorflow lite, se manda un mensaje a la driver station.
            telemetry.addData(">", "Ow! Este dispositivo no es compatible con TensorFlow Lite!");
        }

        //activamos la camara de deteccion de objetos antes de que empiece el opmode
        if (tfod != null) {
            tfod.activate();
        }

        //esperar a que se presione play en la driver station
        telemetry.addData(">", "Dale a play para que empiece lo bueno.");
        telemetry.update();
        waitForStart();

        if (opModeIsActive() && tfod != null) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

                        //arraylist de stones/skystones que se usara mas adelante
                        ArrayList<Stone> stones = new ArrayList();
                        boolean isSkystoneOnView = false;
                        //cantidad de skystones que se detectaran mas adelante
                        int skystones = 0;
                        if (updatedRecognitions != null && updatedRecognitions.size() > 0 ) {  //if se detectan mas de 0 recognitions
                            for (Recognition re : updatedRecognitions) {
                                int recognitionx = (int) re.getLeft(); //obtenemos el x del recognition
                                if (re.getLabel().equals(LABEL_SKYSTONE)) {
                                    isSkystoneOnView = true;
                                    skystones += 1;
                                    double dis = re.getHeight() / re.getImageHeight(); //se calcula una distancia aproximada al skystone dividiendo la altura de esta y la altura de la imagen.
                                    stones.add(new Stone(true, recognitionx, re.estimateAngleToObject(AngleUnit.DEGREES), dis)); // se guarda un objeto 'Stone' en la arraylist con algunas propiedades del recogniton
                                }else if(re.getLabel().equals(LABEL_STONE)) {
                                    double dis = re.getHeight() / re.getImageHeight(); //se calcula una distancia aproximada al stone dividiendo la altura de esta y de altura de la imagen.
                                    stones.add(new Stone(false, recognitionx, re.estimateAngleToObject(AngleUnit.DEGREES), dis)); // se guarda un objeto 'Stone' en la arraylist con algunas propiedades del recogniton
                                }
                            }

                            int index = 0;
                            for(Stone s : stones){
                                telemetry.addData("Stone", index + ": isSkystone="+s.isSkystone+" positionX="+ s.positionX + " distance=" + s.distance + " angle=" + s.angle); //se manda info de cada stone detectado a la driver station
                                index += 1;
                            }
                            telemetry.update();
                        }

                }

            }
        }

        //apagamos tensorflow para liberar recursos del sistema
        if (tfod != null) {
            tfod.shutdown();
        }
    }

    //inicializar vuforia
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VuforiaKey.key;
        parameters.cameraDirection = CameraDirection.BACK;

        //creamos una instancia de Vuforia y la guardamos en una variable
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    //Inicializar el engine de deteccion de objetos de tensorflow
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        //cargamos los modelos
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_STONE, LABEL_SKYSTONE);
    }


}

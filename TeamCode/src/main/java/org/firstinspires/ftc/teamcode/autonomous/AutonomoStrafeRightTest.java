package org.firstinspires.ftc.teamcode.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.IMUDriveMecanum;
import org.firstinspires.ftc.teamcode.hardware.TimeDriveMecanum;

@Autonomous(name="Autonomo Strafe Right Test", group="TEST")
public class AutonomoStrafeRightTest extends LinearOpMode {

    private Hardware hdw;
    private IMUDriveMecanum imuDrive; //en este objeto se encuentran todas las funciones para
                                        //el movimiento de las llantas mecanum con tiempo para
                                        //mantener el codigo mas organizado y facil de cambiar.

    @Override
    public void runOpMode() {
        hdw = new Hardware(hardwareMap); //creamos el hardware
        hdw.initHardware(false); //lo inicializamos

        imuDrive = new IMUDriveMecanum(hdw, telemetry); //el objeto necesita el hardware para definir el power
                                                          //a los motores y el telemetry para mandar mensajes.
        imuDrive.initIMU();

        telemetry.addData("[/!\\]", "Calibrando el sensor IMU, espera...");
        telemetry.update();

        imuDrive.waitForIMUCalibration();

        telemetry.addData("[/!\\]", "AUTONOMO DE PRUEBA! NO ESTA HECHO PARA SER USADO EN UNA COMPETENCIA.");
        telemetry.update();

        //esperamos que el usuario presione <play> en la driver station
        waitForStart();

        imuDrive.selfCorrectingStrafeRight(1, 3);
    }


}
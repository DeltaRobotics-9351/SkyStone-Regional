package org.firstinspires.ftc.teamcode.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.TimeDriveMecanum;

@Autonomous(name="Autonomo Foundation Rojo", group="Final")
public class AutonomoFoundationRojo extends LinearOpMode {

    private Hardware hdw;
    private TimeDriveMecanum timeDrive; //en este objeto se encuentran todas las funciones para
                                        //el movimiento de las llantas mecanum con tiempo para
                                        //mantener el codigo mas organizado y facil de cambiar.

    @Override
    public void runOpMode() {
        hdw = new Hardware(hardwareMap); //creamos el hardware
        hdw.initHardware(false); //lo inicializamos

        timeDrive = new TimeDriveMecanum(hdw, telemetry); //el objeto necesita el hardware para definir el power
                                                          //a los motores y el telemetry para mandar mensajes.

        telemetry.addData("[/!\\]", "GOOO DELTA ROBOTICS!!!");
        telemetry.update();

        //esperamos que el usuario presione <play> en la driver station
        waitForStart();

        timeDrive.forward(1, 0.2); //nos agitamos para bajar el intake
        timeDrive.backwards(1, 0.2);
        timeDrive.forward(1, 0.2);
        timeDrive.backwards(1, 0.2);

        hdw.motorSliders.setPower(1); //subimos los sliders
        sleep(800);
        hdw.motorSliders.setPower(0);

        timeDrive.forward(0.8,1.2); //avanzamos hacia la foundation

        hdw.motorSliders.setPower(-1); //bajamos los sliders
        sleep(800);
        hdw.motorSliders.setPower(0);
        sleep(500);

        timeDrive.backwards(1, 1);// vamos hacia atras para jalar la foundation
        timeDrive.strafeLeft(0.5, 4); //nos deslizamos para estacionarnos abajo del skybridge

    }


}
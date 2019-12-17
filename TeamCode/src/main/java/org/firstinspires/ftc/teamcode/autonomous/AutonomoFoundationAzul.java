package org.firstinspires.ftc.teamcode.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.Hardware;
import org.firstinspires.ftc.teamcode.hardware.TimeDriveMecanum;

@Autonomous(name="Autonomo Foundation Azul", group="Final")
public class AutonomoFoundationAzul extends LinearOpMode {

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

        timeDrive.strafeLeft(0.5, 1); //nos deslizamos

        timeDrive.forward(1, 0.2); //nos agitamos para bajar el intake
        timeDrive.backwards(1, 0.2);

        hdw.motorSliders.setPower(1); //subimos los sliders
        sleep(600);
        hdw.motorSliders.setPower(0);

        timeDrive.forward(0.8,0.9); //avanzamos hacia la foundation

        hdw.motorSliders.setPower(-1); //bajamos los sliders
        sleep(600);
        hdw.motorSliders.setPower(0);
        sleep(500);

        timeDrive.backwards(1, 1.3); // vamos hacia atras para jalar la foundation

        hdw.motorSliders.setPower(1); //subimos los sliders
        sleep(600);
        hdw.motorSliders.setPower(0);

        sleep(16000);

        timeDrive.strafeRight(0.4, 2.5); //nos deslizamos para estacionarnos abajo del skybridge

        hdw.motorSliders.setPower(-1); //bajamos los sliders
        sleep(600);
        hdw.motorSliders.setPower(0);
        sleep(500);

        timeDrive.strafeRight(0.4, 1.8); //nos deslizamos para estacionarnos abajo del skybridge

    }


}
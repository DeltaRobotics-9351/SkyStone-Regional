package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TimeDriveMecanum {

    Hardware hdw;
    Telemetry telemetry;

    public TimeDriveMecanum(Hardware hdw, Telemetry telemetry){
        this.hdw = hdw;
        this.telemetry = telemetry;
    }

    //se define el power de todos los motores y el tiempo en el que avanzaran a este power
    //la string es simplemente para mostrarla en la driver station con un mensaje telemetry.
    //(el tiempo es en segundos)

    public void setAllWheelPower(double frontleft, double frontright, double backleft, double backright, double time, String movementDescription){

        hdw.allWheelsForward(); //definimos la direccion de todos los motores hacia adelante

        hdw.wheelFrontLeft.setPower(frontleft);
        hdw.wheelFrontRight.setPower(frontright);
        hdw.wheelBackLeft.setPower(backleft);
        hdw.wheelBackRight.setPower(backright);

        //mandamos mensajes telemetry para informar sobre lo que esta pasando
        telemetry.addData("movement", movementDescription);
        telemetry.addData("frontleft", -frontleft); //este esta invertido por alguna razon.
        telemetry.addData("frontright", frontright);
        telemetry.addData("backleft", backleft);
        telemetry.addData("backright", backright);
        telemetry.addData("time", time);
        telemetry.update();

        sleep((long) (time * 1000)); //se multiplica * 1000 ya que son milisegundos, y queremos que sean segundos.

        hdw.wheelFrontLeft.setPower(0);
        hdw.wheelFrontRight.setPower(0);
        hdw.wheelBackLeft.setPower(0);
        hdw.wheelBackRight.setPower(0);

        telemetry.addData("frontleft", 0);
        telemetry.addData("frontright", 0);
        telemetry.addData("backleft", 0);
        telemetry.addData("backright", 0);
        telemetry.update();

        hdw.defaultWheelsDirection(); // volvemos a definir la direccion de los motores a como esta normalmente
    }

    //basado en esta imagen: https://i.imgur.com/R82YOwT.png
    //el power de 'frontleft' se tiene que invertir por alguna extrana razon.
    //el movementDescription es simplemente para mostrarlo en un mensaje telemetry (driver station)

    //hacia adelante
    public void forward(double power, double timeSegs) {
        setAllWheelPower(-power, power, power, power, timeSegs, "forward");
    }

    //hacia atras
    public void backwards(double power, double timeSegs) {
        setAllWheelPower(power, -power, -power, -power, timeSegs, "backwards");
    }

    //deslizarse a la izquierda
    public void strafeRight(double power, double timeSegs) {
        setAllWheelPower(-power, -power, -power, power, timeSegs, "strafeLeft");
    }

    //deslizarse a la izquierda
    public void strafeLeft(double power, double timeSegs) {
        setAllWheelPower(power, power, power, -power, timeSegs, "strafeRight");
    }

    //girar a la derecha
    public void turnRight(double power, double timeSegs) {
        setAllWheelPower(-power, -power, power, -power, timeSegs, "turnRight");
        sleep(500);
    }

    //girar a la izquierda
    public void turnLeft(double power, double timeSegs) {
        setAllWheelPower(power, power, -power, power, timeSegs, "turnLeft");
        sleep(500);
    }

    public void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

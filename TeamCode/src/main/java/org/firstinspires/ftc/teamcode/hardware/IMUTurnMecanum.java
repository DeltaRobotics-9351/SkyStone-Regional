package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

public class IMUTurnMecanum {

    public BNO055IMU imu;
    Hardware hdw;
    Orientation lastAngles = new Orientation();

    DcMotor frontleft;
    DcMotor frontright;
    DcMotor backleft;
    DcMotor backright;

    double globalAngle;

    Telemetry telemetry;

    int margenError; //esto sirve porque las llantas mecanum derrapan un poco despues de que termina el movimientow

    public IMUTurnMecanum(Hardware hdw, Telemetry telemetry, int margenError){
        this.hdw = hdw;
        this.telemetry = telemetry;
        this.margenError = margenError;
    }

    public void initIMU(){
        frontleft = hdw.wheelFrontLeft;
        frontright = hdw.wheelFrontRight;
        backleft = hdw.wheelBackLeft;
        backright = hdw.wheelBackRight;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        imu = hdw.hwMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

    }

    public void waitForIMUCalibration(){
        while (!imu.isGyroCalibrated()){ }
    }

    private double getAngle()
    {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    public void rotate(int idegrees, double power)
    {
        int degrees = idegrees;

        hdw.allWheelsForward();
        double  backleftpower, backrightpower, frontrightpower, frontleftpower;

        // reiniciamos el IMU
        resetAngle();

        if (degrees < 0) //si es menor que 0 significa que el robot girara a la derecha
        {   // girar a la derecha.
            backleftpower = power;
            backrightpower = -power;
            frontleftpower = power;
            frontrightpower = -power;
            degrees += margenError; //cambiamos los degrees sumandole el margen de error porque es un numero negativo
        }
        else if (degrees > 0) // si es mayor a 0 significa que el robot girara a la izquierda
        {   // girar a la izquierda
            backleftpower = -power;
            backrightpower = power;
            frontleftpower = -power;
            frontrightpower = power;
            degrees -= margenError; //cambiamos los degrees restandole el margen de error porque es un numero positivo
        }
        else return;

        // definimos el power de los motores
        backleft.setPower(backleftpower);
        backright.setPower(backrightpower);
        frontleft.setPower(-frontleftpower); //este se tiene que invertir por una extrana razon
        frontright.setPower(frontrightpower);

        // rotaremos hasta que se complete la vuelta
        if (degrees < 0)
        {
            while (getAngle() == 0) { //al girar a la derecha necesitamos salirnos de 0 grados primero
                telemetry.addData("imuAngle", getAngle());
                telemetry.addData("degreesDestino", idegrees);
                telemetry.addData("margenError", margenError);
                telemetry.addData("degreesTrasMargenError", degrees);
                telemetry.update();
            }

            while (getAngle() > degrees) { //entramos en un bucle hasta que los degrees no sean los esperados
                telemetry.addData("imuAngle", getAngle());
                telemetry.addData("degreesDestino", idegrees);
                telemetry.addData("margenError", margenError);
                telemetry.addData("degreesTrasMargenError", degrees);
                telemetry.update();
            }
        }
        else
            while (getAngle() < degrees) { //entramos en un bucle hasta que los degrees no sean los esperados
                telemetry.addData("imuAngle", getAngle());
                telemetry.addData("degreesDestino", idegrees);
                telemetry.addData("margenError", margenError);
                telemetry.addData("degreesTrasMargenError", degrees);
                telemetry.update();
            }

        // paramos los motores
        backleft.setPower(0);
        backright.setPower(0);
        frontleft.setPower(0);
        frontright.setPower(0);

        // esperamos a que termine de derrapar
        sleep(500);

        // reiniciamos el IMU otra vez.
        resetAngle();
        hdw.defaultWheelsDirection();
    }

    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }


    public void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}

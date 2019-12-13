package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AngleDirection;
import org.firstinspires.ftc.teamcode.OpModeStatus;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

public class IMUDriveMecanum {

    public BNO055IMU imu;
    Hardware hdw;
    Orientation lastAngles = new Orientation();

    DcMotor frontleft;
    DcMotor frontright;
    DcMotor backleft;
    DcMotor backright;

    double globalAngle;

    Telemetry telemetry;

    OpModeStatus status;

    public IMUDriveMecanum(Hardware hdw, Telemetry telemetry, OpModeStatus status){
        this.hdw = hdw;
        this.telemetry = telemetry;
        this.status = status;
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

    public void rotate(double degrees, double power)
    {

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
        }
        else if (degrees > 0) // si es mayor a 0 significa que el robot girara a la izquierda
        {   // girar a la izquierda
            backleftpower = -power;
            backrightpower = power;
            frontleftpower = -power;
            frontrightpower = power;
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
            while (getAngle() == 0 && status.opModeIsActive) { //al girar a la derecha necesitamos salirnos de 0 grados primero
                telemetry.addData("imuAngle", getAngle());
                telemetry.addData("degreesDestino", degrees);
                telemetry.update();
            }

            while (getAngle() > degrees && status.opModeIsActive) { //entramos en un bucle hasta que los degrees sean los esperados
                telemetry.addData("imuAngle", getAngle());
                telemetry.addData("degreesDestino", degrees);
                telemetry.update();
            }
        }
        else
            while (getAngle() < degrees && status.opModeIsActive) { //entramos en un bucle hasta que los degrees sean los esperados
                telemetry.addData("imuAngle", getAngle());
                telemetry.addData("degreesDestino", degrees);
                telemetry.update();
            }

        // paramos los motores
        backleft.setPower(0);
        backright.setPower(0);
        frontleft.setPower(0);
        frontright.setPower(0);

        // reiniciamos el IMU otra vez.
        resetAngle();
        hdw.defaultWheelsDirection();
    }

    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    public double calculateDeltaBetweenAngles(double angle1, double angle2){
        double deltaAngle = angle1 - angle2;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        return deltaAngle;
    }

    //como el strafing se va chueco, con el sensor imu lo podemos corregir.
    //el tiempo es en segundos.
    public void selfCorrectingStrafeRight(double power, double time){

        long finalMillis = System.currentTimeMillis() + (long)(time*1000);

        double initialAngle = getAngle();

        while(System.currentTimeMillis() < finalMillis){

            double frontleft = power, frontright = -power, backleft = -power, backright = power;

            if(getAngle() < initialAngle){
                double deltaAngle = calculateDeltaBetweenAngles(initialAngle, getAngle());

                double counteractConstant = 0.1;
                double counteractValue = deltaAngle * counteractConstant;

                frontleft = power / counteractValue;
                frontright = -power;
                backleft = -power / counteractValue;
                backright = power;

                telemetry.addData("frontleft", frontleft);
                telemetry.addData("frontright", frontright);
                telemetry.addData("backleft", backleft);
                telemetry.addData("backright", backright);
                telemetry.addData("counteractValue", counteractValue);
                telemetry.addData("deltaAngle", deltaAngle);
                telemetry.update();

            }else if(getAngle() > initialAngle){
                double deltaAngle = calculateDeltaBetweenAngles(initialAngle, getAngle());

                double counteractConstant = 0.2;
                double counteractValue = deltaAngle * counteractConstant;

                frontleft = power;
                frontright = -power / counteractValue;
                backleft = -power;
                backright = power / counteractValue;

                telemetry.addData("frontleft", frontleft);
                telemetry.addData("frontright", frontright);
                telemetry.addData("backleft", backleft);
                telemetry.addData("backright", backright);
                telemetry.addData("counteractValue", counteractValue);
                telemetry.addData("deltaAngle", deltaAngle);
                telemetry.update();

            }else{
                frontleft = power;
                frontright = -power;
                backleft = -power;
                backright = power;
                telemetry.addData("frontleft", frontleft);
                telemetry.addData("frontright", frontright);
                telemetry.addData("backleft", backleft);
                telemetry.addData("backright", backright);
                telemetry.update();
            }

            defineAllWheelPower(frontleft,frontright,backleft,backright);

        }

        defineAllWheelPower(0,0,0,0);

        telemetry.addData("frontleft", 0);
        telemetry.addData("frontright", 0);
        telemetry.addData("backleft", 0);
        telemetry.addData("backright", 0);
        telemetry.update();

    }

    private void defineAllWheelPower(double frontleft, double frontright, double backleft, double backright){
        hdw.wheelFrontLeft.setPower(frontleft);
        hdw.wheelFrontRight.setPower(frontright);
        hdw.wheelBackLeft.setPower(backleft);
        hdw.wheelBackRight.setPower(backright);
    }

    public void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

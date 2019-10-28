package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Hardware {

    //Constructor
    public Hardware(HardwareMap m){ hwMap = m; }

    //hardwaremap que se obtuvo en el constructor
    public HardwareMap hwMap;

    //llantas
    public DcMotor wheelFrontRight = null;
    public DcMotor wheelFrontLeft = null;
    public DcMotor wheelBackRight = null;
    public DcMotor wheelBackLeft = null;

    //otros motores
    public DcMotor motorIntakeLeft = null;
    public DcMotor motorIntakeRight = null;
    public DcMotor motorLift = null;
    public DcMotor motorArtiIntake = null;

    //sensores
    //public ColorSensor colorSensor = null; (ejemplo)

    public void createHardware(){

        //se obtienen todos los motores, servos y sensores del hardwaremap dado
        wheelFrontRight = hwMap.get(DcMotor.class, "fr");
        wheelFrontLeft = hwMap.get(DcMotor.class, "fl");
        wheelBackRight = hwMap.get(DcMotor.class, "br");
        wheelBackLeft = hwMap.get(DcMotor.class, "bl");
        motorIntakeLeft = hwMap.get(DcMotor.class, "inl");
        motorIntakeRight = hwMap.get(DcMotor.class, "inr");
        motorLift = hwMap.get(DcMotor.class, "lift");
        motorArtiIntake = hwMap.get(DcMotor.class, "arti");

        //La direccion por default de estos motores/servos sera FORWARD
        wheelFrontRight.setDirection(DcMotor.Direction.FORWARD);
        wheelBackRight.setDirection(DcMotor.Direction.FORWARD);
        wheelBackLeft.setDirection(DcMotor.Direction.FORWARD);
        wheelFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        motorIntakeLeft.setDirection(DcMotor.Direction.FORWARD);
        motorArtiIntake.setDirection(DcMotor.Direction.FORWARD);
        //La direccion por default de estos motores sera REVERSE
        motorIntakeRight.setDirection(DcMotor.Direction.REVERSE);
        motorLift.setDirection(DcMotor.Direction.REVERSE);

        //el power de todos los motores se define a 0
        wheelFrontRight.setPower(0);
        wheelBackRight.setPower(0);
        wheelFrontLeft.setPower(0);
        wheelBackLeft.setPower(0);
        motorIntakeLeft.setPower(0);
        motorIntakeRight.setPower(0);
        motorArtiIntake.setPower(0);
        motorLift.setPower(0);

        //estos motores frenaran si su power es 0
        //motorArtiClaw.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        //se define la posicion por default de estos servos

        //definimos los motores que correran con y sin encoders 
        wheelFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorIntakeLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorIntakeRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorArtiIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

}

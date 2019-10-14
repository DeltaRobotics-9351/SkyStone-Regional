package org.firstinspires.ftc.teamcode;

public class Stone {

    public boolean isSkystone;

    public int positionX;
    public double angle;
    public double distance;

    public Stone(boolean isSkystone, int positionX, double angle, double distance){
        this.isSkystone = isSkystone;
        this.positionX = positionX;
        this.angle = angle;
        this.distance = distance;
    }

}

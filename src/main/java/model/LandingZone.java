package model;

import java.awt.*;

public class LandingZone{
    private Rectangle zone;

    public LandingZone(int x, int y, int width, int height){
        this.zone = new Rectangle(x, y, width, height);
    }

    public boolean contains(double landerX, double landerY){
        return zone.contains(landerX, landerY);
    }

    public Rectangle getZone(){
        return zone;
    }
}
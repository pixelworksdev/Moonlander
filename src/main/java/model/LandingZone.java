package model;

import java.awt.*;

public class LandingZone{
    // -------------------------------
    // Represents a rectangular landing zone
    // -------------------------------
    private Rectangle zone; // Java AWT rectangle storing position and size

    // -------------------------------
    // Constructor: defines landing zone position and size
    // -------------------------------
    public LandingZone(int x, int y, int width, int height){
        this.zone = new Rectangle(x, y, width, height);
    }

    // -------------------------------
    // Check if a point (lander) is inside this landing zone
    // -------------------------------
    public boolean contains(double landerX, double landerY){
        return zone.contains(landerX, landerY);
    }

    // -------------------------------
    // Accessor for the rectangle
    // -------------------------------
    public Rectangle getZone(){
        return zone;
    }
}
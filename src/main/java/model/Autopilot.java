package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Autopilot{
    // -------------------------------
    // Autopilot properties
    // -------------------------------
    private boolean active = false;        // Whether autopilot is currently enabled
    private final double targetSpeed = 1.5; // Target vertical speed for safe landing
    private final double thrustStrength = 0.08; // Optional thrust strength if extended

    // -------------------------------
    // Compute vertical thrust based on lander state
    // Returns:
    // -1 : apply upward thrust
    //  1 : apply downward thrust
    //  0 : no vertical thrust
    // -------------------------------
    public int computeVertical(Moonlander lander, double groundY){
        if(!active) return 0; // If autopilot is off, do nothing

        double distance = groundY - lander.getPosition().getY(); // Distance to ground
        double velY = lander.getVelocity().getY();              // Current vertical speed

        // If falling too fast, thrust upwards
        if(velY > targetSpeed)
            return -1;
            // If moving upwards too fast, thrust downwards
        else if(velY < -targetSpeed)
            return 1;

        // If very close to the ground and descending faster than half target speed, apply upward thrust
        if(distance < 50 && velY > targetSpeed / 2)
            return -1;

        return 0; // Otherwise, no vertical adjustment
    }
}
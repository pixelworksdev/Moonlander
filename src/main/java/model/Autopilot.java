package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Autopilot{
    private boolean active = false;
    private final double targetSpeed = 1.5;
    private final double thrustStrength = 0.08;

    public int computeVertical(Moonlander lander, double groundY){
        if(!active) return 0;

        double distance = groundY - lander.getPosition().getY();
        double velY = lander.getVelocity().getY();

        if(velY > targetSpeed)
            return -1;
        else if(velY < -targetSpeed)
            return 1;

        if(distance < 50 && velY > targetSpeed/2)
            return -1;

        return 0;
    }
}
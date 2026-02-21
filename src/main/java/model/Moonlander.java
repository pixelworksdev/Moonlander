package model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import utils.*;

@Slf4j
@Getter
public class Moonlander{
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;
    private double fuel;

    private final double GRAVITY = 0.05;
    private final double THRUST = 0.1;

    private State state = State.RUNNING;

    public Moonlander(Vector2D startPosition, double fuel){
        this.position = startPosition;
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.fuel = fuel;
    }

    public void update(int horizontal, int vertical, double groundY){
        if(state != State.RUNNING) return;

        acceleration.setY(GRAVITY);

        if(fuel > 0){
            acceleration.setX(horizontal * THRUST);
            acceleration.setY(acceleration.getY() + vertical * THRUST);
            if(horizontal != 0 || vertical != 0){
                fuel -= 0.2;
                if(fuel < 0) fuel = 0;
            }
        }

        velocity.add(acceleration);
        position.add(velocity);
        acceleration.setX(0);
    }

    private void checkLanding(double groundY){
        double safeSpeed = 2.0;

        if(position.getY() >= groundY){
            position.setY(groundY);
            if(Math.abs(velocity.getY()) <= safeSpeed){
                state = State.LANDED;
                log.info("Landed successfully!");
            }else{
                state = State.CRASHED;
                log.info("Crashed! Vertical speed: " + velocity.getY());
            }
            velocity.reset();
        }
    }

    public void setState(State state){
        this.state = state;
    }
}
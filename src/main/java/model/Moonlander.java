package model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import utils.*;

@Slf4j
@Getter
public class Moonlander{
    // -------------------------------
    // Core physics properties
    // -------------------------------
    private Vector2D position;      // Current position of the lander
    private Vector2D velocity;      // Current velocity
    private Vector2D acceleration;  // Current acceleration
    private double fuel;            // Remaining fuel

    private final double GRAVITY = 0.05; // Gravity constant
    private final double THRUST = 0.1;   // Thrust power per key press

    private State state = State.RUNNING; // Current state: RUNNING, LANDED, CRASHED

    // -------------------------------
    // Constructor: initializes the lander
    // -------------------------------
    public Moonlander(Vector2D startPosition, double fuel){
        this.position = startPosition;
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.fuel = fuel;
    }

    // -------------------------------
    // Updates the lander's physics
    // -------------------------------
    public void update(int horizontal, int vertical, double groundY){
        // Only update if the game is running
        if(state != State.RUNNING) return;

        // Gravity always applies
        acceleration.setY(GRAVITY);

        // Apply thrust if fuel remains
        if(fuel > 0){
            acceleration.setX(horizontal * THRUST);            // Horizontal thrust
            acceleration.setY(acceleration.getY() + vertical * THRUST); // Vertical thrust

            if(horizontal != 0 || vertical != 0){
                fuel -= 0.2; // Reduce fuel on thrust
                if(fuel < 0) fuel = 0;
            }
        }

        // Update velocity and position
        velocity.add(acceleration);
        position.add(velocity);

        // Reset horizontal acceleration for next frame
        acceleration.setX(0);
    }

    // -------------------------------
    // Check landing conditions (unused in current code, can be called if needed)
    // -------------------------------
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
            velocity.reset(); // Stop the lander
        }
    }

    // -------------------------------
    // Set the lander state externally
    // -------------------------------
    public void setState(State state){
        this.state = state;
    }
}
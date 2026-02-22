// Main game controller implementing BaseGame
package controller;

import lombok.extern.slf4j.Slf4j;
import model.Autopilot;
import model.LandingZone;
import model.Moonlander;
import model.Terrain;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Slf4j
public class Game extends BaseGame{
    private Moonlander lander;      // Player lander object
    private Autopilot autopilot;    // Optional autopilot
    private Terrain terrain;        // Moon terrain with landing zones

    // Player input states
    private int horizontal = 0;
    private int vertical = 0;
    private boolean debug = false;

    private long startTime;
    private long elapsedTime;

    // Constructor: initialize objects and variables
    public Game(){
        lander = new Moonlander(new Vector2D(200, 50), 100);
        autopilot = new Autopilot();
        horizontal = 0;
        vertical = 0;
        elapsedTime = 0;
        log.info("Game object created");
    }

    // Initialize game: called once after view is created
    @Override
    public void initGame(JPanel view){
        super.start(view);
        terrain = new Terrain(view.getWidth(), view.getHeight()); // generate terrain
        restart(); // reset lander and timers
    }

    // Game logic: called every tick
    @Override
    public void update(){
        if(lander.getState() != State.RUNNING) return;

        // Determine vertical input: player or autopilot
        int verticalInput = autopilot.isActive() ? autopilot.computeVertical(lander, view.getHeight()) : vertical;

        // Update lander physics
        lander.update(horizontal, verticalInput, view.getHeight() - 50);

        double landerXCenter = lander.getPosition().getX() + 10;
        double landerBottomY = lander.getPosition().getY() + 10;

        int terrainY = terrain.getHeightAt((int)landerXCenter);

        // Collision detection with terrain
        if(landerBottomY >= terrainY){
            lander.getPosition().setY(terrainY - 10 + 5); // adjust to lander height

            double maxSafeVertical = 2.0;
            double maxSafeHorizontal = 1.0;

            boolean safeLanding = false;

            // Check if lander is inside any landing zone
            for(LandingZone zone : terrain.getZones()){
                Rectangle rect = zone.getZone();
                if(landerXCenter >= rect.x && landerXCenter <= rect.x + rect.width &&
                        landerBottomY >= rect.y && landerBottomY <= rect.y + rect.height + 3){
                    safeLanding = true;
                    break;
                }
            }

            // Determine landing outcome
            if(safeLanding && Math.abs(lander.getVelocity().getY()) <= maxSafeVertical &&
                    Math.abs(lander.getVelocity().getX()) <= maxSafeHorizontal){
                lander.setState(State.LANDED);
                lander.getVelocity().reset();
                log.info("Landed safely in a landing zone!");
            }else{
                lander.setState(State.CRASHED);
                lander.getVelocity().reset();
                log.info("Crashed!");
            }
        }

        // Update elapsed game time
        elapsedTime = System.currentTimeMillis() - startTime;
    }

    @Override
    public void draw(){} // unused

    // Draw everything on screen
    @Override
    public void drawInGraphics(Graphics2D g){
        int panelWidth = view.getWidth();
        int panelHeight = view.getHeight();
        int groundHeight = 50;
        int groundY = panelHeight - groundHeight;

        // Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panelWidth, panelHeight);

        // Ground rectangle
        g.setColor(Color.GRAY);
        g.fillRect(0, groundY, panelWidth, groundHeight);

        // Draw lander
        g.setColor(Color.WHITE);
        int landerX = (int) lander.getPosition().getX();
        int landerY = (int) lander.getPosition().getY();
        if (landerY > groundY) landerY = groundY;
        g.fillRect(landerX, landerY - 10, 20, 10);

        // Draw thrust flames based on input
        g.setColor(Color.ORANGE);
        int thrustLength = 10;

        if(vertical == -1) g.drawLine(landerX + 10, landerY, landerX + 10, landerY + thrustLength); // downward
        if(vertical == 1) g.drawLine(landerX + 10, landerY - 10, landerX + 10, landerY - 10 - thrustLength); // upward
        if(horizontal == -1) g.drawLine(landerX, landerY - 5, landerX - thrustLength, landerY - 5); // left
        if(horizontal == 1) g.drawLine(landerX + 20, landerY - 5, landerX + 20 + thrustLength, landerY - 5); // right

        // Draw terrain and landing zones
        terrain.draw(g);

        // Fuel bar
        int barWidth = (int) (panelWidth * 0.25);
        int barHeight = 20;
        int fuelWidth = (int) (lander.getFuel() / 100.0 * barWidth);
        g.setColor(Color.GREEN);
        g.fillRect(20, 20, fuelWidth, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(20, 20, barWidth, barHeight);

        // Fuel text
        g.setColor(Color.BLUE);
        g.drawString("Fuel: " + (int)lander.getFuel(), 25, barHeight + 15);

        // Game status text
        g.setColor(Color.YELLOW);
        String status = switch (lander.getState()){
            case LANDED -> "LANDED SUCCESSFULLY";
            case CRASHED -> "GAME OVER - CRASHED";
            default -> "";
        };
        if(!status.isEmpty()){
            int textWidth = g.getFontMetrics().stringWidth(status);
            g.drawString(status, panelWidth/2 - textWidth/2, 35);
        }

        // Timer display
        g.setColor(Color.MAGENTA);
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        String timeText = String.format("Time: %02d:%02d", minutes, seconds);
        int timeWidth = g.getFontMetrics().stringWidth(timeText);
        g.drawString(timeText, panelWidth - timeWidth - 20, 35);

        // Debug overlay
        if(debug){
            g.setColor(Color.ORANGE);
            g.drawString("DEBUG MODE", 20, 60);
            g.drawString("Pos X: " + String.format("%.2f", lander.getPosition().getX()), 20, 80);
            g.drawString("Pos Y: " + String.format("%.2f", lander.getPosition().getY()), 20, 100);
            g.drawString("Vel X: " + String.format("%.2f", lander.getVelocity().getX()), 20, 120);
            g.drawString("Vel Y: " + String.format("%.2f", lander.getVelocity().getY()), 20, 140);
            g.drawString("Acc X: " + String.format("%.2f", lander.getAcceleration().getX()), 20, 160);
            g.drawString("Acc Y: " + String.format("%.2f", lander.getAcceleration().getY()), 20, 180);
            g.drawString("Fuel: " + String.format("%.2f", lander.getFuel()), 20, 200);
            g.setColor(Color.RED);
            g.drawRect(landerX, landerY - 10, 20, 10);

            int scale = 50;
            int landerCX = landerX + 10;
            int landerCY = landerY - 5;

            // Velocity vector
            g.setColor(Color.RED);
            g.drawLine(landerCX, landerCY, landerCX + (int)(lander.getVelocity().getX()*scale),
                    landerCY + (int)(lander.getVelocity().getY()*scale));

            // Acceleration vector
            g.setColor(Color.GREEN);
            g.drawLine(landerCX, landerCY, landerCX + (int)(lander.getAcceleration().getX()*scale*5),
                    landerCY + (int)(lander.getAcceleration().getY()*scale*5));
        }

        // Autopilot status
        g.setColor(Color.MAGENTA);
        g.drawString("Autopilot: " + (autopilot.isActive() ? "ON" : "OFF"), panelWidth - timeWidth - 20, 55);
    }

    // Mouse input (unused)
    @Override public void mouseClicked(MouseEvent e){}
    @Override public void mouseDown(MouseEvent e){}
    @Override public void mouseRelease(MouseEvent e){}

    // Keyboard input: W/A/S/D + debug + autopilot + pause/restart
    @Override
    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_W -> vertical = -1;
            case KeyEvent.VK_S -> vertical = 1;
            case KeyEvent.VK_A -> horizontal = -1;
            case KeyEvent.VK_D -> horizontal = 1;
            case KeyEvent.VK_G -> {debug = !debug; log.info("Debug info shown|hidden");}
            case KeyEvent.VK_P -> {autopilot.setActive(!autopilot.isActive()); log.info("Autopilot started|stopped");}
            case KeyEvent.VK_R -> restart();
            case KeyEvent.VK_SPACE -> {
                if(lander.getState() == State.RUNNING){
                    lander.setState(State.PAUSED);
                    log.info("Game Paused");
                }else if(lander.getState() == State.PAUSED){
                    lander.setState(State.RUNNING);
                    log.info("Game Resumed");
                    startTime = System.currentTimeMillis() - elapsedTime;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_S -> vertical = 0;
            case KeyEvent.VK_A, KeyEvent.VK_D -> horizontal = 0;
        }
    }

    // Reset the game state
    private void restart(){
        lander = new Moonlander(new Vector2D(200, 50), 100);
        autopilot = new Autopilot();
        horizontal = 0;
        vertical = 0;
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
        log.info("Game restarted");
    }
}
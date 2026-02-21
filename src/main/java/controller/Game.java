package controller;

import lombok.extern.slf4j.Slf4j;
import model.Autopilot;
import model.Moonlander;
import utils.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Slf4j
public class Game extends BaseGame{
    private Moonlander lander;
    private Autopilot autopilot;

    private int horizontal = 0;
    private int vertical = 0;
    private boolean debug = false;

    private long startTime;
    private long elapsedTime;

    public Game(){
        restart();
    }

    @Override
    public void update(){
        if(lander.getState() == State.RUNNING){
            int groundY = view.getHeight() - 50;
            int verticalInput = autopilot.isActive() ? autopilot.computeVertical(lander, groundY) : vertical;
            lander.update(horizontal, verticalInput, groundY);
            elapsedTime = System.currentTimeMillis() - startTime;
        }
    }

    @Override
    public void draw(){}

    @Override
    public void drawInGraphics(Graphics2D g){
        int panelWidth = view.getWidth();
        int panelHeight = view.getHeight();
        int groundHeight = 50;
        int groundY = panelHeight - groundHeight;

        // Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panelWidth, panelHeight);

        // Ground
        g.setColor(Color.GRAY);
        g.fillRect(0, groundY, panelWidth, groundHeight);

        // Lander
        g.setColor(Color.WHITE);
        int landerX = (int) lander.getPosition().getX();
        int landerY = (int) lander.getPosition().getY();
        if (landerY > groundY) landerY = groundY;
        g.fillRect(landerX, landerY - 10, 20, 10);

        // Thrust Animation
        g.setColor(Color.ORANGE);
        int thrustLength = 10;

        if(vertical == -1){
            g.drawLine(landerX + 10, landerY, landerX + 10, landerY + thrustLength);
        }
        if(vertical == 1){
            g.drawLine(landerX + 10, landerY - 10, landerX + 10, landerY - 10 - thrustLength);
        }
        if(horizontal == -1){
            g.drawLine(landerX, landerY - 5, landerX - thrustLength, landerY - 5);
        }
        if(horizontal == 1){
            g.drawLine(landerX + 20, landerY - 5, landerX + 20 + thrustLength, landerY - 5);
        }

        //  Fuel Bar
        int barWidth = (int) (panelWidth * 0.25);
        int barHeight = 20;
        int fuelWidth = (int) (lander.getFuel() / 100.0 * barWidth);

        g.setColor(Color.GREEN);
        g.fillRect(20, 20, fuelWidth, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(20, 20, barWidth, barHeight);

        // Fuel Text
        g.setColor(Color.BLUE);
        g.drawString("Fuel: " + (int)lander.getFuel(), 25, barHeight + 15);

        // Status
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

        // Timer
        g.setColor(Color.MAGENTA);
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        String timeText = String.format("Time: %02d:%02d", minutes, seconds);
        int timeWidth = g.getFontMetrics().stringWidth(timeText);
        g.drawString(timeText, panelWidth - timeWidth - 20, 35);

        // Debug & Autopilot
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

            // Velocity Arrow
            g.setColor(Color.RED);
            g.drawLine(landerCX, landerCY, landerCX + (int)(lander.getVelocity().getX()*scale), landerCY + (int)(lander.getVelocity().getY()*scale));

            // Acceleration Arrow
            g.setColor(Color.GREEN);
            g.drawLine(landerCX, landerCY, landerCX + (int)(lander.getAcceleration().getX()*scale*5), landerCY + (int)(lander.getAcceleration().getY()*scale*5));
        }

        // Autopilot Status
        g.setColor(Color.MAGENTA);
        g.drawString("Autopilot: " + (autopilot.isActive() ? "ON" : "OFF"), panelWidth - timeWidth - 20, 55);
    }

    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mouseDown(MouseEvent e){}
    @Override
    public void mouseRelease(MouseEvent e){}

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
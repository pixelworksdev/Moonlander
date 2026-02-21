package controller;

import lombok.extern.java.Log;
import model.Autopilot;
import model.Moonlander;
import utils.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Log
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
        if(landerY > groundY) landerY = groundY;
        g.fillRect(landerX, landerY - 10, 20, 10);

        // Fuel Bar
        g.setColor(Color.GREEN);
        g.fillRect(20, 20, (int) (lander.getFuel() * 2), 20);
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, 200, 20);

        // Status
        g.setColor(Color.YELLOW);
        switch(lander.getState()) {
            case LANDED -> g.drawString("LANDED SUCCESSFULLY", panelWidth/2 - 80, panelHeight / 2);
            case CRASHED -> g.drawString("GAME OVER - CRASHED", panelWidth/2 - 80, panelHeight / 2);
            default -> {}
        }

        // Autopilot Status
        g.setColor(Color.MAGENTA);
        g.drawString("Autopilot: " + (autopilot.isActive() ? "ON" : "OFF"), 20, 320);

        g.setColor(Color.MAGENTA);
        g.drawString("Time: " + elapsedTime / 1000 + "s", panelWidth - 100, 20);

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
        }
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
            case KeyEvent.VK_G -> debug = !debug;
            case KeyEvent.VK_P -> autopilot.setActive(!autopilot.isActive());
            case KeyEvent.VK_R -> restart();
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
package controller;

import lombok.extern.java.Log;
import model.Moonlander;
import utils.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Log
public class Game extends BaseGame{

    private Moonlander lander;
    private int horizontal = 0;
    private int vertical = 0;

    public Game(){
        lander = new Moonlander(new Vector2D(200, 50), 100);
    }

    @Override
    public void update(){
        if(lander.getState() == State.RUNNING){
            int groundY = view.getHeight() - 50;
            lander.update(horizontal, vertical, groundY);
        }
    }

    @Override
    public void draw(){
        Graphics2D g = (Graphics2D) view.getGraphics();
        if(g == null) return;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, view.getWidth(), view.getHeight());

        g.setColor(Color.GRAY);
        g.fillRect(0, 500, view.getWidth(), 50);

        g.setColor(Color.WHITE);
        int landerX = (int) lander.getPosition().getX();
        int landerY = (int) lander.getPosition().getY();
        g.fillRect(landerX, landerY - 10, 20, 10);

        g.setColor(Color.GREEN);
        g.fillRect(20, 20, (int) (lander.getFuel()*2), 20);
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, 200, 20);

        g.setColor(Color.YELLOW);
        switch(lander.getState()) {
            case LANDED -> g.drawString("LANDED SUCCESSFULLY", 150, 50);
            case CRASHED -> g.drawString("GAME OVER - CRASHED", 150, 50);
            default -> {}
        }
    }

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
        g.fillRect(20, 20, (int) (lander.getFuel()*2), 20);
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, 200, 20);

        // Status Text
        g.setColor(Color.YELLOW);
        switch(lander.getState()) {
            case LANDED -> g.drawString("LANDED SUCCESSFULLY", panelWidth/2 - 80, 50);
            case CRASHED -> g.drawString("GAME OVER - CRASHED", panelWidth/2 - 80, 50);
            default -> {}
        }

        g.setColor(Color.CYAN);
        g.drawString("Fuel: " + (int)lander.getFuel(), 20, 60);
        g.drawString("VX: " + String.format("%.2f", lander.getVelocity().getX()), 20, 80);
        g.drawString("VY: " + String.format("%.2f", lander.getVelocity().getY()), 20, 100);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseDown(MouseEvent e) {}
    @Override
    public void mouseRelease(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W -> vertical = -1;
            case KeyEvent.VK_S -> vertical = 1;
            case KeyEvent.VK_A -> horizontal = -1;
            case KeyEvent.VK_D -> horizontal = 1;
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
        horizontal = 0;
        vertical = 0;
        log.info("Game restarted");
    }
}
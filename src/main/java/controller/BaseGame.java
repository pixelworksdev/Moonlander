// Base controller class for all games
package controller;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Slf4j
public abstract class BaseGame{
    protected JPanel view; // Reference to the game view
    private Timer timer;   // Swing Timer for game loop

    public BaseGame(){
        log.info("--> basegame ctor called ");
        // Timer fires every 20ms (~50 FPS), calling tick()
        timer = new Timer(20, e -> tick());
    }

    public void start(JPanel view){
        this.view = view;
        log.info("--> basegame timer started");
        timer.start();
    }

    // One tick of the game: update logic + repaint view
    private void tick(){
        update();
        view.repaint();
    }

    // Abstract methods to be implemented by subclasses
    abstract public void initGame(JPanel view);
    abstract public void update();
    abstract public void draw();
    abstract public void drawInGraphics(Graphics2D g2);

    // Mouse input handlers
    abstract public void mouseClicked(MouseEvent e);
    abstract public void mouseDown(MouseEvent e);
    abstract public void mouseRelease(MouseEvent e);

    // Keyboard input handlers
    abstract public void keyPressed(KeyEvent e);
    abstract public void keyReleased(KeyEvent e);
}
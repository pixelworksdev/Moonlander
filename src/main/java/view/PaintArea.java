package view;

import controller.BaseGame;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Log
public class PaintArea extends JPanel{
    // Reference to the game logic (model)
    private final BaseGame model;

    // Constructor: links the view to the model and sets up input listeners
    public PaintArea(BaseGame model){
        this.model = model;
        setFocusable(true); // Make panel focusable to receive key events

        // -------------------------------
        // Mouse Listener: forwards mouse input to the model
        // -------------------------------
        addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e){
                model.mouseClicked(e); // Forward click to game logic
            }
            @Override
            public void mousePressed(MouseEvent e){
                model.mouseDown(e); // Forward mouse press
            }
            @Override
            public void mouseReleased(MouseEvent e){
                model.mouseRelease(e); // Forward mouse release
            }
            @Override public void mouseEntered(MouseEvent e){} // unused
            @Override public void mouseExited(MouseEvent e){}  // unused
        });

        // -------------------------------
        // Key Listener: forwards keyboard input to the model
        // -------------------------------
        addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e){} // unused

            @Override
            public void keyPressed(KeyEvent e){
                model.keyPressed(e); // Forward key press to model
            }

            @Override
            public void keyReleased(KeyEvent e){
                // Only forward key release if model is a Game instance
                if(model instanceof controller.Game game){
                    game.keyReleased(e);
                }
            }
        });
    }

    // -------------------------------
    // Paint method: draws the game state
    // -------------------------------
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g); // clear previous frame

        Graphics2D g2 = (Graphics2D) g;

        // Enable anti-aliasing for smoother graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Let the model handle all actual drawing
        if(model != null){
            model.drawInGraphics(g2);
        }
    }
}
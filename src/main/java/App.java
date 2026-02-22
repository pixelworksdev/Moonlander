// Entry point of the application
import controller.BaseGame;
import controller.Game;
import view.PaintArea;

import javax.swing.*;

public class App{
    public static void main(String[] args){
        // Enable OpenGL for faster rendering in Java2D
        System.setProperty("sun.java2d.opengl", "true");

        // Initialize game logic (model)
        BaseGame model = new Game();

        // Initialize view and link it to the model
        PaintArea view = new PaintArea(model);
        view.setFocusable(true);

        // Setup JFrame (window) to display the game
        JFrame frame = new JFrame();
        frame.setTitle("Moonlander");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.setVisible(true);

        // Initialize game (pass view to model)
        model.initGame(view);

        // Start game loop
        model.start(view);
    }
}
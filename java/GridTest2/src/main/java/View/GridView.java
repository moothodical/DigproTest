/**
 * GridView.java
 *
 * Sets up all components on JFrame and all needed ActionListeners for Controller to use.
 */
package View;

import Controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GridView extends JFrame {
    public JButton manualReloadButton;
    private final JButton autoFetchToggleButton;
    private JButton aboutMeButton;
    private final JButton exitButton;
    private final JLabel autoFetchToggleLabel;
    private final JLabel communicatingWithServerLabel;
    private Grid grid;

    /**
     * Creates all necessary components for GUI.
     */
    public GridView() {
        getContentPane().setLayout(new BorderLayout());
        JFrame frame = this;

        manualReloadButton = new JButton("Fetch Coordinates");
        autoFetchToggleButton = new JButton("Toggle Auto Fetch Coordinates");
        aboutMeButton = new JButton("About Me!");
        exitButton = new JButton("Exit");
        aboutMeButton = new JButton("About Me!");

        aboutMeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contactInfo = "Andrei Baderca" + "\nbadercaa@protonmail.com" +
                        "\nhttps://www.linkedin.com/in/andreibaderca/"
                        + "\nhttps://github.com/moothodical"
                        + "\nA keen learner looking to get started in the software engineering space.";
                JOptionPane.showMessageDialog(frame,
                        contactInfo,
                        "About Me!",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });

        autoFetchToggleLabel = new JLabel("");
        communicatingWithServerLabel = new JLabel("");

        // Separate JPanel where grid is displayed
        grid = new Grid();
        BufferedImage worldMap = null;

        // Loads world map image and sets it on the grid

        try {
            worldMap = ImageIO.read(getClass().getClassLoader().getResource("world-map.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        grid.setImage(new ImageIcon(worldMap).getImage());

        JPanel topBarPanel = new JPanel();
        JPanel bottomBarPanel = new JPanel();
        topBarPanel.setLayout(new GridLayout(1, 3));
        topBarPanel.add(manualReloadButton);
        topBarPanel.add(autoFetchToggleButton);
        topBarPanel.add(autoFetchToggleLabel);
        topBarPanel.add(communicatingWithServerLabel);

        bottomBarPanel.add(aboutMeButton);
        bottomBarPanel.add(exitButton);

        add(topBarPanel, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(bottomBarPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * Exposes Grid JPanel so that Controller may use it.
     * @return grid
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Exposes autoFetchToggleLabel so that Controller may update it as it changes.
     * @param text Text to display
     */
    public void setAutoFetchToggleLabelText(String text) {
        autoFetchToggleLabel.setText(text);
    }

    /**
     * Exposes communicatingWithServerLabel so that Controller may update it as it fetches Coordinates.
     * @param text Text to display
     * @param color Color to display text as
     */
    public void setCommunicatingWithServerLabelText(String text, Color color) {
        communicatingWithServerLabel.setText(text);
        communicatingWithServerLabel.setForeground(color);
    }

    /**
     * Adds ActionListener to manualReloadButton so that Controller can implement functionality.
     * @param listener ActionListener that is implemented in {@link Controller#start()}
     */
    public void addFetchCoordinatesActionListener(ActionListener listener) {
        manualReloadButton.addActionListener(listener);
    }

    /**
     * Adds ActionListener to autoFetchToggleButton so that Controller can implement functionality.
     * @param listener ActionLlistener that is implemented in {@link Controller#start()}
     */
    public void addAutoFetchToggleButtonActionListener(ActionListener listener) {
        autoFetchToggleButton.addActionListener(listener);
    }

}

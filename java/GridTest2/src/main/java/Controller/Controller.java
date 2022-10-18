/**
 * Controller.java
 *
 * Controller class that manipulates Model (Coordinate.java) and Views (GridView.java, Grid.java).
 * Has functionality for fetching Coordinates through SwingWorker, manual and automatic Coordinate fetching with Timer,
 * and ActionListeners for GridView buttons.
 */
package Controller;

import Model.Coordinate;
import View.Grid;
import View.GridView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Controller {
    private final GridView view;
    private final Grid grid;
    private Coordinate coordinate;
    private SwingWorker getCoordinatesWorker;
    protected javax.swing.Timer autoReloadTimer = null;
    private boolean autoReloadEnabled;

    public Controller(GridView view, Grid grid) {
        this.view = view;
        this.grid = grid;
        this.setAutoReloadEnabled(true);
    }

    /**
     * Initializes ActionListeners and implements functionality for manualReloadButton and autoFetchToggleButtons.
     * {@link GridView#addFetchCoordinatesActionListener(ActionListener)}: fetch coordinates from server
     * and disables auto fetching functionality
     * {@link GridView#addAutoFetchToggleButtonActionListener(ActionListener)}: toggles auto fetching functionality
     */
    public void start() {

        view.addFetchCoordinatesActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.manualReloadButton.setEnabled(false);
                setAutoReloadEnabled(false);
                getCoordinatesWorker.cancel(true);
                fetchCoordinates();
            }
        });

        view.addAutoFetchToggleButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAutoReloadEnabled(!isAutoReloadEnabled());
            }
        });
    }

    /**
     * Sets autoReloadEnabled boolean and changes corresponding label in GridView
     * @param autoReloadEnabled
     */
    public void setAutoReloadEnabled(boolean autoReloadEnabled) {
        if (autoReloadEnabled) {
            // Fetch coordinates immediately after toggling on instead of only after 30s
            fetchCoordinates();
            view.setAutoFetchToggleLabelText("ON");
        } else {
            stopAutoReloadRefreshing();
            view.setAutoFetchToggleLabelText("OFF");
        }
        this.autoReloadEnabled = autoReloadEnabled;
    }

    public boolean isAutoReloadEnabled() {
        return autoReloadEnabled;
    }

    /**
     * Uses a SwingWorker to fetch coordinates as Strings from server using HTTP GET request. Parses String results into
     * their respective types, then creates Coordinate objects with scaled X, Y coordinates and name and adds them to
     * Coordinate ArrayList.
     *
     * On done(), tells Grid to get Coordinate ArrayList from Coordinate, which is used for drawing Coordinates to
     * screen.
     *
     * See {@link Grid}
     * See {@link Coordinate}
     */
    public void fetchCoordinates() {
        getCoordinatesWorker = new SwingWorker() {
            @Override
            protected Void doInBackground() throws Exception {
                view.setCommunicatingWithServerLabelText("Communicating with server...", Color.RED);
                Coordinate.clearCoordinates();
                ArrayList<String> stringCoordinates = new ArrayList<String>();

                StringBuilder result = new StringBuilder();
                URL url = new URL("https://daily.digpro.se/bios/servlet/bios.servlets.web.RecruitmentTestServlet");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.ISO_8859_1))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        // Ignore lines that start with #
                        if (line.charAt(0) == '#') {
                            continue;
                        }
                        stringCoordinates.add(line);
                    }
                    // Parses values, calculates scaled X, Y values, creates Coordinate objects and adds them to AL
                    for (int i = 0; i < stringCoordinates.size(); i++) {
                        String[] splitCoordinates = stringCoordinates.get(i).split("\\s*,\\s*");
                        int x = Integer.parseInt(splitCoordinates[0]);
                        int y = Integer.parseInt(splitCoordinates[1]);
                        String name = splitCoordinates[2];

                        // Scales X, Y coordinate location to fit 3000x3000 grid
                        int scaledX = (int) Math.round(x * grid.getX_SCALING()) + grid.getOriginX();
                        // Flip bit to get vertical coordinates in proper place
                        int scaledY = (int) Math.round(y * -1 * grid.getY_SCALING()) + grid.getOriginY(); // flip bit to get vertical coordinates in proper place

                        Coordinate coordinate = new Coordinate(new Point(scaledX, scaledY), new Point(x, y), name, 15);
                        Coordinate.coordinates.add(coordinate);
                    }
                } catch (IOException e) {
                    view.setCommunicatingWithServerLabelText("Trouble connecting with server...", Color.RED);
                } finally {
                    conn.disconnect();
                }
                return null;
            }
            @Override
            public void done() {
                grid.getCoordinates();
                view.setCommunicatingWithServerLabelText("", Color.RED);
                view.manualReloadButton.setEnabled(true);
                if (isAutoReloadEnabled()) {
                    startAutoReloadRefreshing();
                } else {
                    stopAutoReloadRefreshing();
                }
            }
        };
        getCoordinatesWorker.execute();
    }

    protected void stopAutoReloadRefreshing() {
        if (autoReloadTimer != null) {
            autoReloadTimer.stop();
            autoReloadTimer = null;
        }
    }
    protected void startAutoReloadRefreshing() {
        stopAutoReloadRefreshing();
        autoReloadTimer = new Timer(30000, e -> {
            try {
                fetchCoordinates();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        autoReloadTimer.start();
    }
}

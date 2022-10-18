/**
 * DigproSolutionsTest.java
 *
 * Main class that instantiates Model, Views, and Controller and starts application.
 */

import Controller.Controller;
import View.Grid;
import View.GridView;
import java.io.IOException;

public class DigproSolutionsTest {
    public static void main(String[] args) throws IOException {
        GridView view = new GridView();
        Grid grid = view.getGrid();
        Controller controller = new Controller(view, grid);
        controller.start();
    }
}

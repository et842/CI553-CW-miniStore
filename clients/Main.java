package clients;

import clients.backDoor.BackDoorController;
import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import clients.cashier.CashierController;
import clients.cashier.CashierModel;
import clients.cashier.CashierView;
import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import clients.packing.PackingController;
import clients.packing.PackingModel;
import clients.packing.PackingView;
import middle.LocalMiddleFactory;
import middle.MiddleFactory;
import javax.swing.*;
import java.awt.*;
import java.util.logging.*;
import java.io.IOException;

/**
 * Starts all the clients (user interface)  as a single application.
 * Good for testing the system using a single application.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 * @version year-2024
 */

class Main
{
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    static {
        try {
            FileHandler handler = new FileHandler("application.log", true); // Append logs to a file
            handler.setFormatter(new SimpleFormatter());
            Logger rootLogger = Logger.getLogger(""); // Root logger
            rootLogger.addHandler(handler);          // Attach handler to root logger
            rootLogger.setLevel(Level.INFO);         // Set log level for all loggers
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }


    public static void main(String args[]) {
        new Main().begin();
    }

    /**
     * Starts the system (Non distributed)
     */
    public void begin() {
        logger.info("System startup initiated.");
        MiddleFactory mlf = new LocalMiddleFactory();  // Direct access
        startCustomerGUI_MVC(mlf);
        startCashierGUI_MVC(mlf);
        startCashierGUI_MVC(mlf); // you can create multiple clients
        startPackingGUI_MVC(mlf);
        startBackDoorGUI_MVC(mlf);
        logger.info("System startup completed.");
    }

    /**
     * start the Customer client, -search product
     * @param mlf A factory to create objects to access the stock list
     */
    public void startCustomerGUI_MVC(MiddleFactory mlf) {
        JFrame window = new JFrame();
        window.setTitle("Customer Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.LIGHT_GRAY); // Change background color
        Dimension pos = PosOnScrn.getPos();

        CustomerModel model = new CustomerModel(mlf);
        CustomerView view = new CustomerView(window, mlf, pos.width, pos.height);
        CustomerController cont = new CustomerController(model, view);
        view.setController(cont);

        model.addObserver(view); // Add observer to the model
        logger.info("Customer Client GUI started."); // Log GUI star
        window.setVisible(true); // start Screen
    }

    /**
     * start the cashier client - customer check stock, buy product
     * @param mlf A factory to create objects to access the stock list
     */
    public void startCashierGUI_MVC(MiddleFactory mlf) {
        JFrame window = new JFrame();
        window.setTitle("Cashier Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.CYAN); // Change background color
        Dimension pos = PosOnScrn.getPos();

        CashierModel model = new CashierModel(mlf);
        CashierView view = new CashierView(window, mlf, pos.width, pos.height);
        CashierController cont = new CashierController(model, view);
        view.setController(cont);

        model.addObserver(view); // Add observer to the model
        logger.info("Cashier Client GUI started."); // Log GUI start
        window.setVisible(true); // Make window visible
        model.askForUpdate(); // Initial display
    }

    /**
     * start the Packing client - for warehouse staff to pack the bought order for customer, one order at a time
     * @param mlf A factory to create objects to access the stock list
     */
    public void startPackingGUI_MVC(MiddleFactory mlf) {
        JFrame window = new JFrame();
        window.setTitle("Packing Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.YELLOW); // Change background color
        Dimension pos = PosOnScrn.getPos();

        PackingModel model = new PackingModel(mlf);
        PackingView view = new PackingView(window, mlf, pos.width, pos.height);
        PackingController cont = new PackingController(model, view);
        view.setController(cont);

        model.addObserver(view); // Add observer to the model
        logger.info("Packing Client GUI started."); // Log GUI start
        window.setVisible(true); // Make window visible
    }

    /**
     * start the BackDoor client - store staff to check and update stock
     * @param mlf A factory to create objects to access the stock list
     */
    public void startBackDoorGUI_MVC(MiddleFactory mlf) {
        JFrame window = new JFrame();
        window.setTitle("BackDoor Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.PINK); // Change background color
        Dimension pos = PosOnScrn.getPos();

        BackDoorModel model = new BackDoorModel(mlf);
        BackDoorView view = new BackDoorView(window, mlf, pos.width, pos.height);
        BackDoorController cont = new BackDoorController(model, view);
        view.setController(cont);

        model.addObserver(view); // Add observer to the model
        logger.info("BackDoor Client GUI started."); // Log GUI start
        window.setVisible(true); // Make window visible
    }
}

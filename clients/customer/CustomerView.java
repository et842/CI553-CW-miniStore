package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;
import middle.StockException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */
public class CustomerView implements Observer {

    private static final int H = 400; // Updated height for extra button
    private static final int W = 400;

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton("Check");
    private final JButton theBtClear = new JButton("Clear");
    private final JButton theBtRemove = new JButton("Remove"); // Added Remove button

    private Picture thePicture = new Picture(80, 80);
    private CustomerController cont = null; // Controller reference

    /**
     * Construct the view
     *
     * @param rpc Window in which to construct
     * @param mf  Factory to deliver order and stock objects
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public CustomerView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        Container cp = rpc.getContentPane(); // Content Pane
        Container rootWindow = (Container) rpc; // Root Window
        cp.setLayout(null); // No layout manager
        rootWindow.setSize(W, H); // Size of Window
        rootWindow.setLocation(x, y);

        Font f = new Font("Monospaced", Font.PLAIN, 12); // Font f is

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Customer Actions");
        cp.add(pageTitle);

        theBtCheck.setBounds(16, 25 + 60 * 0, 80, 40); // Check button
        theBtCheck.addActionListener(e -> {
            System.out.println("Check button clicked."); // Debug print
            cont.doCheck(theInput.getText());
        });
        cp.add(theBtCheck);

        theBtClear.setBounds(16, 25 + 60 * 1, 80, 40); // Clear button
        theBtClear.addActionListener(e -> {
            System.out.println("Clear button clicked."); // Debug print
            cont.doClear();
        });
        cp.add(theBtClear);

        theBtRemove.setBounds(16, 25 + 60 * 2, 80, 40); // Remove button
        theBtRemove.addActionListener(e -> {
            System.out.println("Remove button clicked."); // Debug print
            cont.doRemove(theInput.getText());
        });
        cp.add(theBtRemove); // Add Remove button to the GUI

        theAction.setBounds(110, 25, 270, 20); // Message area
        theAction.setText(" ");
        cp.add(theAction);

        theInput.setBounds(110, 50, 270, 40); // Input area
        theInput.setText("");
        cp.add(theInput);

        theSP.setBounds(110, 100, 270, 200); // Scrolling pane
        theOutput.setText("");
        theOutput.setFont(f);
        cp.add(theSP);
        theSP.getViewport().add(theOutput); // Add TextArea inside the pane

        rootWindow.setVisible(true); // Make visible
        theInput.requestFocus(); // Focus is here
    }

    /**
     * Set the controller for the view.
     *
     * @param c The controller
     */
    public void setController(CustomerController c) {
        cont = c;
    }

    /**
     * Update the view
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    public void update(Observable modelC, Object arg) {
        CustomerModel model = (CustomerModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        theOutput.setText(model.getBasket().getDetails());
        theInput.requestFocus();
    }

    /**
     * Getter for the 'Remove' button
     *
     * @return the Remove button
     */
    public JButton getRemoveButton() {
        return theBtRemove;
    }

    /**
     * Getter for the product number text field
     *
     * @return the text in the input field
     */
    public String getProductNumber() {
        return theInput.getText();
    }

    /**
     * Getter for the 'Check' button
     *
     * @return the Check button
     */
    public JButton getCheckButton() {
        return theBtCheck;
    }

    /**
     * Getter for the 'Clear' button
     *
     * @return the Clear button
     */
    public JButton getClearButton() {
        return theBtClear;
    }
}

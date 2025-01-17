package clients.customer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * The Customer Controller
 */
public class CustomerController {
  private CustomerModel model = null;
  private CustomerView view = null;

  private static final Logger logger = Logger.getLogger(CustomerController.class.getName());

  /**
   * Constructor
   * @param model The model 
   * @param view  The view from which the interaction came
   */
  public CustomerController(CustomerModel model, CustomerView view) {
    this.view = view;
    this.model = model;

    // Make the view observe the model
    model.addObserver(view);

    // Add action listeners for buttons
    view.getCheckButton().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            logger.info("Customer clicked 'Check' button."); // Log the action
            doCheck(view.getProductNumber()); // Call the existing method
        }
    });

    view.getClearButton().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            logger.info("Customer clicked 'Clear' button."); // Log the action
            doClear(); // Call the existing method
        }
    });

    view.getRemoveButton().addActionListener(new ActionListener() { // New Remove button listener
        @Override
        public void actionPerformed(ActionEvent e) {
            logger.info("Customer clicked 'Remove' button."); // Log the action
            doRemove(view.getProductNumber()); // Call the new doRemove method
        }
    });
  }

  /**
   * Check interaction from view
   * @param pn The product number to be checked
   */
  public void doCheck(String pn) {
    System.out.println("doCheck in CustomerController called with Product Number: " + pn); // Debug print
    logger.info("Customer clicked 'Check' button. Product Number: " + pn); // Log button click
    model.doCheck(pn);
  }

  /**
   * Clear interaction from view
   */
  public void doClear() {
    logger.info("Customer clicked 'Clear' button."); // Log button click
    model.doClear();
  }

  /**
   * Remove interaction from view
   * @param pn The product number to be removed
   */
  public void doRemove(String pn) {
    System.out.println("doRemove in CustomerController called with Product Number: " + pn); // Debug print
    logger.info("Customer clicked 'Remove' button. Product Number: " + pn); // Log button click
    model.removeFromBasket(pn); // Call the model method to handle the removal
  }

  /**
   * Returns the model associated with this controller
   * @return the CustomerModel
   */
  public CustomerModel getModel() {
    return model;
  }
}

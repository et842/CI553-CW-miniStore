package clients.cashier;

import catalogue.BetterBasket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import java.util.Observable;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel extends Observable {
    private enum State { process, checked }

    private State theState = State.process;   // Current state
    private Product theProduct = null;        // Current product
    private BetterBasket theBasket = null;    // Bought items (BetterBasket for advanced functionality)

    private String pn = "";                   // Product being processed

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;

    /**
     * Construct the model of the Cashier
     * @param mf The factory to create the connection objects
     */
    public CashierModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter(); // Database access
            theOrder = mf.makeOrderProcessing(); // Process order
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }
        theState = State.process; // Current state
    }

    /**
     * Get the Basket of products
     * @return basket
     */
    public BetterBasket getBasket() {
        return theBasket;
    }

    /**
     * Check if the product is in Stock
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        String theAction = "";
        theState = State.process; // State process
        pn = productNum.trim(); // Product no.
        int amount = 1; // Quantity
        try {
            if (theStock.exists(pn)) { // Stock Exists?
                Product pr = theStock.getDetails(pn); // Get details
                if (pr.getQuantity() >= amount) { // In stock?
                    theAction = String.format(
                            "%s : %7.2f (%2d) ",
                            pr.getDescription(),
                            pr.getPrice(),
                            pr.getQuantity()
                    );
                    theProduct = pr; // Remember product
                    theProduct.setQuantity(amount); // Set quantity
                    theState = State.checked; // OK await BUY
                } else {
                    theAction = pr.getDescription() + " not in stock";
                }
            } else {
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCheck", e.getMessage());
            theAction = e.getMessage();
        }
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Buy the product
     */
    public void doBuy() {
        String theAction = "";
        int amount = 1; // Quantity
        try {
            if (theState != State.checked) {
                theAction = "please check its availability";
            } else {
                boolean stockBought = theStock.buyStock(
                        theProduct.getProductNum(),
                        theProduct.getQuantity()
                );
                if (stockBought) {
                    makeBasketIfReq(); // Create new Basket if required
                    theBasket.add(theProduct); // Add to bought
                    theAction = "Purchased " + theProduct.getDescription();
                } else {
                    theAction = "!!! Not in stock";
                }
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
            theAction = e.getMessage();
        }
        theState = State.process; // All Done
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Customer pays for the contents of the basket
     */
    public void doBought() {
        String theAction = "";
        try {
            if (theBasket != null && theBasket.size() >= 1) {
                theOrder.newOrder(theBasket); // Process order
                theBasket = null; // Reset basket
            }
            theAction = "Start New Order";
            theState = State.process; // All Done
        } catch (OrderException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBought", e.getMessage());
            theAction = e.getMessage();
        }
        theBasket = null;
        setChanged();
        notifyObservers(theAction); // Notify
    }

    /**
     * Remove a product from the basket
     * @param productNum The product number to be removed
     */
    public void doRemove(String productNum) {
        String theAction = "";
        if (theBasket == null || theBasket.size() == 0) {
            theAction = "Basket is empty, nothing to remove.";
        } else {
            boolean removed = theBasket.removeByProductNumber(productNum); // Custom method in BetterBasket
            if (removed) {
                theAction = "Removed product " + productNum + " from the basket.";
            } else {
                theAction = "Product " + productNum + " not found in the basket.";
            }
        }
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Ask for update of view called at start of day
     * or after system reset
     */
    public void askForUpdate() {
        setChanged();
        notifyObservers("Welcome");
    }

    /**
     * Make a Basket when required
     */
    private void makeBasketIfReq() {
        if (theBasket == null) {
            try {
                int uon = theOrder.uniqueNumber(); // Unique order num.
                theBasket = makeBasket(); // Create basket
                theBasket.setOrderNum(uon); // Add an order number
            } catch (OrderException e) {
                DEBUG.error("Comms failure\nCashierModel.makeBasket()\n%s", e.getMessage());
            }
        }
    }

    /**
     * Return an instance of a new BetterBasket
     * @return an instance of a new BetterBasket
     */
    protected BetterBasket makeBasket() {
        return new BetterBasket();
    }

}

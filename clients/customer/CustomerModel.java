package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import java.util.List;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel extends Observable {
  private Product theProduct = null;          // Current product
  private Basket theBasket  = null;          // Bought items

  private String pn = "";                    // Product being processed

  private StockReader theStock     = null;
  private OrderProcessing theOrder = null;
  private ImageIcon thePic         = null;

  /**
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public CustomerModel(MiddleFactory mf) {
    try {
      theStock = mf.makeStockReader(); // Database access
    } catch (Exception e) {
      DEBUG.error("CustomerModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage());
    }
    theBasket = makeBasket(); // Initial Basket
  }

  /**
   * return the Basket of products
   * @return the basket of products
   */
  public Basket getBasket() {
    return theBasket;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum) {
    theBasket.clear();                          // Clear s. list
    String theAction = "";
    pn  = productNum.trim();                    // Product no.
    int amount  = 1;                            //  & quantity
    try {
      if (theStock.exists(pn)) {                // Stock Exists?
        Product pr = theStock.getDetails(pn);   //  Product
        if (pr.getQuantity() >= amount) {       //  In stock?
          theAction =                           //   Display
            String.format("%s : %7.2f (%2d) ", //
              pr.getDescription(),              //    description
              pr.getPrice(),                    //    price
              pr.getQuantity());                //    quantity
          pr.setQuantity(amount);               //   Require 1
          theBasket.add(pr);                    //   Add to basket
          thePic = theStock.getImage(pn);       //    product
        } else {
          theAction = pr.getDescription() + " not in stock"; // Inform not in stock
        }
      } else {
        theAction = "Unknown product number " + pn; // Inform Unknown product number
      }
    } catch (StockException e) {
      DEBUG.error("CustomerClient.doCheck()\n%s", e.getMessage());
    }
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Clear the products from the basket
   */
  public void doClear() {
    String theAction = "";
    theBasket.clear();                        // Clear s. list
    theAction = "Enter Product Number";       // Set display
    thePic = null;                            // No picture
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Remove a product from the basket
   * @param productNum The product number to remove
   */
  public void removeFromBasket(String productNum) {
    String theAction = "";
    if (theBasket.remove(productNum)) { // Assuming Basket has a remove method
      theAction = "Removed product: " + productNum;
    } else {
      theAction = "Product not found in the basket: " + productNum;
    }
    setChanged(); notifyObservers(theAction);
  }

  /**
   * Return a picture of the product
   * @return An instance of an ImageIcon
   */
  public ImageIcon getPicture() {
    return thePic;
  }

  /**
   * ask for update of view callled at start
   */
  private void askForUpdate() {
    setChanged(); notifyObservers("START only"); // Notify
  }

  /**
   * Searches for products based on a keyword in their description.
   * @param keyword The keyword to search for.
   * @return A list of products matching the keyword.
   * @throws StockException If an error occurs during the search.
   */
  public List<Product> searchProducts(String keyword) throws StockException {
    return theStock.searchByKeyword(keyword);
  }

  /**
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket() {
    return new Basket();
  }
}

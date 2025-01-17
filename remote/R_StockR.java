package remote;

import catalogue.Product;
import dbAccess.StockR;
import middle.StockException;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Implements Read access to the stock list,
 * the stock list is held in a relational DataBase.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class R_StockR extends java.rmi.server.UnicastRemoteObject implements RemoteStockR_I {
    private static final long serialVersionUID = 1;
    private StockR aStockR = null;

    public R_StockR(String url) throws RemoteException, StockException {
        aStockR = new StockR();
    }

    /**
     * Checks if the product exits in the stock list
     * @param pNum The product number
     * @return true if exists otherwise false
     */
    public synchronized boolean exists(String pNum) throws RemoteException, StockException {
        return aStockR.exists(pNum);
    }

    /**
     * Returns details about the product in the stock list
     * @param pNum The product number
     * @return StockNumber, Description, Price, Quantity
     */
    public synchronized Product getDetails(String pNum) throws RemoteException, StockException {
        return aStockR.getDetails(pNum);
    }

    /**
     * Returns an image of the product
     * @param pNum The product number
     * @return Image
     */
    public synchronized ImageIcon getImage(String pNum) throws RemoteException, StockException {
        return aStockR.getImage(pNum);
    }

    /**
     * Searches for products based on a keyword in their description.
     * @param keyword The keyword to search for.
     * @return A list of products matching the keyword.
     * @throws RemoteException, StockException if an error occurs during the search.
     */
    @Override
    public synchronized List<Product> searchByKeyword(String keyword) throws RemoteException, StockException {
        return aStockR.searchByKeyword(keyword);
    }
}

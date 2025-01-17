package remote;

import catalogue.Product;
import middle.StockException;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Defines the RMI interface for read access to the stock object.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public interface RemoteStockR_I extends Remote {
    boolean exists(String number) throws RemoteException, StockException;
    Product getDetails(String number) throws RemoteException, StockException;
    ImageIcon getImage(String number) throws RemoteException, StockException;

    /**
     * Searches for products based on a keyword in their description.
     * @param keyword The keyword to search for.
     * @return A list of products matching the keyword.
     * @throws RemoteException, StockException if an error occurs during the search.
     */
    List<Product> searchByKeyword(String keyword) throws RemoteException, StockException;
}

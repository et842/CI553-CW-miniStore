package remote;

import catalogue.Product;
import dbAccess.StockRW;
import middle.StockException;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Implements Read/Write access to the stock list,
 * the stock list is held in a relational DataBase.
 * @author  Mike Smith University of Brighton
 * @version 2.1
 */

public class R_StockRW extends java.rmi.server.UnicastRemoteObject implements RemoteStockRW_I {
    private static final long serialVersionUID = 1;
    private StockRW aStockRW = null;

    public R_StockRW(String url) throws RemoteException, StockException {
        aStockRW = new StockRW();
    }

    public synchronized boolean exists(String pNum) throws StockException {
        return aStockRW.exists(pNum);
    }

    public synchronized Product getDetails(String pNum) throws StockException {
        return aStockRW.getDetails(pNum);
    }

    public synchronized ImageIcon getImage(String pNum) throws StockException {
        return aStockRW.getImage(pNum);
    }

    public synchronized boolean buyStock(String pNum, int amount) throws StockException {
        return aStockRW.buyStock(pNum, amount);
    }

    public synchronized void addStock(String pNum, int amount) throws StockException {
        aStockRW.addStock(pNum, amount);
    }

    public synchronized void modifyStock(Product product) throws StockException {
        aStockRW.modifyStock(product);
    }

    /**
     * Searches for products based on a keyword in their description.
     * @param keyword The keyword to search for.
     * @return A list of products matching the keyword.
     * @throws RemoteException, StockException if an error occurs during the search.
     */
    @Override
    public synchronized List<Product> searchByKeyword(String keyword) throws RemoteException, StockException {
        return aStockRW.searchByKeyword(keyword);
    }
}

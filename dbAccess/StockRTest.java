package dbAccess;

import static org.junit.jupiter.api.Assertions.*;

import catalogue.Product;
import middle.StockException;
import middle.StockReader;
import org.junit.jupiter.api.Test;

import java.util.List;

class StockRTest {

    @Test
    public void testSearchByKeyword() throws StockException {
        StockReader stockReader = new StockR(); // Ensure database setup
        List<Product> results = stockReader.searchByKeyword("toy");

        // Assert that results are not empty
        assertFalse(results.isEmpty(), "Search results should not be empty for keyword 'toy'");
    }
}

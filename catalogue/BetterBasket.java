package catalogue;

/**
 * Enhanced Basket for better management of products.
 */
public class BetterBasket extends Basket {

    /**
     * Add a product to the basket. If the product already exists in the basket,
     * increase its quantity instead of adding it again.
     *
     * @param pr The product to add to the basket.
     * @return true if the product was successfully added or updated.
     */
    @Override
    public boolean add(Product pr) {
        for (Product prInList : this) {
            if (prInList.getProductNum().equals(pr.getProductNum())) {
                int quantity = pr.getQuantity() + prInList.getQuantity();
                prInList.setQuantity(quantity);
                return true;
            }
        }
        return super.add(pr); // Call add in the base Basket
    }

    /**
     * Remove a product from the basket by its product number.
     *
     * @param productNum The product number to remove.
     * @return true if the product was removed successfully, false otherwise.
     */
    public boolean removeByProductNumber(String productNum) {
        for (Product pr : this) {
            if (pr.getProductNum().equals(productNum)) {
                super.remove(pr); // Use the remove method from ArrayList
                return true; // Indicate successful removal
            }
        }
        return false; // Product not found
    }
}

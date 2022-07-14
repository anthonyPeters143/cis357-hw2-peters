/**
 * @author Anthony Peters
 *
 * Holds Item ID, Quantities, and Totals, can add to Quantities and Totals
 */
public class SaleItemTracker implements Comparable<SaleItemTracker> {

    private Item itemIDTrack;
    private int itemQuantity;
    private double itemTotal;

    /**
     * Non-Aug. Constructor
     */
    SaleItemTracker() {
        itemIDTrack = null;
        itemQuantity = 0;
        itemTotal = 0;
    }

    /**
     * Aug. Constructor
     * Calls resetSale method to reset sale totals
     *
     * @param itemID, Item object ID
     * @param quantity, Int quantity of item
     * @param total, Double total of quantity * price
     */
    SaleItemTracker(Item itemID, int quantity, double total) {
        this.itemIDTrack = itemID;
        this.itemQuantity = quantity;
        this.itemTotal = total;
    }


    /**
     * Add to Quantity amount
     *
     * @param quantity, int amount of item
     */
    public void addItemQuantity(int quantity) {
        this.itemQuantity += quantity;
    }

    /**
     * Add to Total amount
     *
     * @param price, double total of item
     */
    public void addItemTotal(double price) {
        this.itemTotal += price;
    }

    /**
     * Returns item ID
     *
     * @return Item, item ID
     */
    public Item getItemIDTrack() {
        return this.itemIDTrack;
    }

    /**
     * Returns item quantity
     *
     * @return int, item quantity
     */
    public int getItemQuantity() {
        return this.itemQuantity;
    }

    /**
     * Returns item total
     *
     * @return double, item total
     */
    public double getItemTotal() {
        return this.itemTotal;
    }


    @Override
    public int compareTo(SaleItemTracker o) {
        return (this.itemIDTrack.getItemName().compareTo(o.itemIDTrack.getItemName()));
    }

}

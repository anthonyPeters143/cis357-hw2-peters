/**
 *
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
     *
     */
    public void addItemQuantity(int quantity) {
        this.itemQuantity += quantity;
    }

    /**
     *
     */
    public void addItemTotal(double price) {
        this.itemTotal += price;
    }

    public Item getItemIDTrack() {
        return this.itemIDTrack;
    }

    public int getItemQuantity() {
        return this.itemQuantity;
    }

    public double getItemTotal() {
        return this.itemTotal;
    }


    @Override
    public int compareTo(SaleItemTracker o) {
        return (this.itemIDTrack.getItemName().compareTo(o.itemIDTrack.getItemName()));
    }

}

public class SaleItemTracker {

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
     * Non-Aug. Constructor
     * Calls resetSale method to reset sale totals
     */
    SaleItemTracker(Item itemID, int quantity, double total) {
        this.itemIDTrack = itemID;
        this.itemQuantity = quantity;
        this.itemTotal = total;
    }


    public void addItemQuantity(int quantity) {
        this.itemQuantity = quantity;
    }

    public int getItemQuantity() {
        return this.itemQuantity;
    }

    public Item getItemIDTrack() {
        return this.itemIDTrack;
    }

}

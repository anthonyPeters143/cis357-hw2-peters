import java.text.DecimalFormat;
import java.util.ArrayList;

public class Sale {

    private double EODTotal;
    private ArrayList<SaleItemTracker> saleItemTrackerArrayList;

    /**
     * Non-Aug. Constructor
     * Calls resetSale method to reset sale totals
     */
    Sale(){
        EODTotal = 0;

        // Create sale itemList
        saleItemTrackerArrayList = new ArrayList<>();
    }

    public void addSaleItem(Item saleItem, int itemQuantity, double itemPrice) {
        // Create sale item
        saleItemTrackerArrayList.add(new SaleItemTracker(saleItem,itemQuantity,itemPrice));

        // add item price to EOD
        EODTotal += itemPrice;
    }

    public String createReceipt(DecimalFormat currencyFormat) {
        // Declare and Initialization
        String returnString = "";

        // loop to check all saleItemTracker items
        for (SaleItemTracker saleItemTracker : saleItemTrackerArrayList) {
            // Check if they have a quantity
            if (saleItemTracker.getItemQuantity() > 0){
                // check if taxable
                if (saleItemTracker.getItemIDTrack().getItemTaxable()) {
                    // Taxable

                }
                else {
                    // Non-taxable

                }
            }

        }

    }

    public double getEODTotal() {
        return EODTotal;
    }

}
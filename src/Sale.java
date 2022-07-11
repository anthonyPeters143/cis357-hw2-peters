import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

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

    /**
     * Takes saleItem ID checks if an SaleItemTracker matches if so add to quantity and price trackers,
     * if there isn't a matching SaleItemTracker then method will create one and add quantity and price trackers
     *
     * @param saleItem
     * @param itemQuantity
     * @param itemPrice
     */
    public void addSaleItem(Item saleItem, int itemQuantity, double itemPrice) {
        // Check if item already created
        // loop array list to check

        // If new
        // Create sale item
        saleItemTrackerArrayList.add(new SaleItemTracker(saleItem,itemQuantity,itemPrice));

        // If not new


        // add item price to EOD
        EODTotal += itemPrice;
    }


    /**
     *
     *
     * @param currencyFormat
     * @return
     */
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
                    saleItemTracker.

                }
                else {
                    // Non-taxable

                }
            }

        }

        return returnString;

    }

    private ArrayList<SaleItemTracker> getSortedSaleItemTrackerArrayList() {
        Collections.sort(saleItemTrackerArrayList);
        return saleItemTrackerArrayList;
    }

    public double getEODTotal() {
        return EODTotal;
    }

}
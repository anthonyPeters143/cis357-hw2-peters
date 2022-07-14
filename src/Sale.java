import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Anthony Peters
 *
 * Holds EODTotal, subtotals, subtotalTax, and saleItemTrackerArrayList. Allows adding new sale items, creating
 * receipts, sorting the saleItemTrackerArrayList, and reseting sale fields.
 */
public class Sale {

    private double EODTotal, subtotal, subtotalTax;
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
     * @param saleItem, Item Item object ID
     * @param itemQuantity, int amount of Item
     * @param itemPrice, double price of Item
     */
    public void addSaleItem(Item saleItem, int itemQuantity, double itemPrice) {
        // Add items price to EOD total
        EODTotal += itemPrice;


        // loop array list to check if itemTracker is already created
        for (SaleItemTracker saleItemTracker : saleItemTrackerArrayList) {
            if (saleItem.equals(saleItemTracker.getItemIDTrack())){
                // Item tracker already created
                // Update saleItemTracker quantity and price
                saleItemTracker.addItemQuantity(itemQuantity);
                saleItemTracker.addItemTotal(itemPrice);

                return;
            }
        }

        // Item not found, create new item tracker
        saleItemTrackerArrayList.add(new SaleItemTracker(saleItem,itemQuantity,itemPrice));
    }

    /**
     * Sorts arrayList creates String then loops through ArrayList adding to counter depending on if Items are taxable
     * then concat a formatted String including quantity, name, and total.
     *
     * @param currencyFormat, DecimalFormat formatting currency
     * @return String, String of receipt
     */
    public String createReceipt(DecimalFormat currencyFormat) {
        // Declare and Initialization
        String returnString = "";
        double taxableTotal = 0, nontaxableTotal = 0;

        // Sort into alphabetical order by name
        saleItemTrackerArrayList = getSortedSaleItemTrackerArrayList();

        // loop to check all saleItemTracker items
        for (SaleItemTracker saleItemTracker : saleItemTrackerArrayList) {
            // Check if they have a quantity
            if (saleItemTracker.getItemQuantity() > 0){
                // Check if taxable
                if (saleItemTracker.getItemIDTrack().getItemTaxable()) {
                    // Taxable
                    // Increment taxable total
                    taxableTotal += saleItemTracker.getItemTotal();

                }
                else {
                    // Non-taxable
                    // Increment nontaxable total
                    nontaxableTotal += saleItemTracker.getItemTotal();

                }

                // Add item's name, quantity, total to return list
                returnString = returnString.concat(String.format("%4s %-19s %s",saleItemTracker.getItemQuantity(),
                        saleItemTracker.getItemIDTrack().getItemName(), "$")
                        + String.format("%1$8s",currencyFormat.format(saleItemTracker.getItemTotal())+"\n"));
            }
        }
        // Compile subtotals
        subtotal = taxableTotal + nontaxableTotal;
        subtotalTax = (taxableTotal * .06) + taxableTotal + nontaxableTotal;

        // Attach subtotal output strings to bottom of receipt
        returnString = returnString.concat("\nSubtotal\t\t\t\t $" +  String.format("%1$7s",currencyFormat.format(subtotal))
                + "\nTotal with Tax (6%)\t\t $" + String.format("%1$7s",currencyFormat.format(subtotalTax)));

        return returnString;

    }

    /**
     * Sorts ArrayList then returns the sorted Array
     *
     * @return ArrayList<SaleItemTracker>, Sorted ArrayList
     */
    private ArrayList<SaleItemTracker> getSortedSaleItemTrackerArrayList() {
        Collections.sort(saleItemTrackerArrayList);
        return saleItemTrackerArrayList;
    }

    /**
     * Resets subtotals sales totals and clears ArrayList
     */
    public void resetSale() {
        // Reset subtotals
        subtotal = 0;
        subtotalTax = 0;

        // Clear arraylist
        saleItemTrackerArrayList.clear();
    }

    /**
     * Returns sale end of day total
     *
     * @return double, end of day total
     */
    public double getEODTotal() {
        return EODTotal;
    }

    /**
     * Returns sale subtotal
     *
     * @return double, subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Returns sale subtotal with tax
     *
     * @return double, subtotal with tax included
     */
    public double getSubtotalTax() {
        return subtotalTax;
    }
}
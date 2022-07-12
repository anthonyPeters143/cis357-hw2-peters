import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

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
     * @param saleItem
     * @param itemQuantity
     * @param itemPrice
     */
    public void addSaleItem(Item saleItem, int itemQuantity, double itemPrice) {
        // Add items price to EOD total
        EODTotal += itemPrice;


        // loop array list to check if itemTracker is already created
        for (SaleItemTracker saleItemTracker : saleItemTrackerArrayList) {
            if (saleItem.equals(saleItemTracker.getItemIDTrack())){
                // Item tracker already created
                // Update saleItemTracker quantity and price
                saleItemTracker.addItemTotal(itemQuantity);
                saleItemTracker.addItemTotal(itemPrice);

                return;
            }
        }

        // Item not found, create new item tracker
        saleItemTrackerArrayList.add(new SaleItemTracker(saleItem,itemQuantity,itemPrice));
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
                    taxableTotal =+ saleItemTracker.getItemTotal();

                }
                else {
                    // Non-taxable
                    // Increment nontaxable total
                    nontaxableTotal =+ saleItemTracker.getItemTotal();

                }

                // Add item's name, quantity, total to return list
                returnString = returnString.concat("\t" + saleItemTracker.getItemQuantity() +
                                "\t" + saleItemTracker.getItemIDTrack().getitemName() +
                                String.format("%1$8s",currencyFormat.format(saleItemTracker.getItemTotal())+"\n"));
            }
        }
        // Add subtotals to bottom of receipt
        subtotal = taxableTotal + nontaxableTotal;
        subtotalTax = (taxableTotal * .06) + nontaxableTotal;
        returnString = returnString.concat("Subtotal\t\t\t\t $" +  String.format("%1$7s",currencyFormat.format(subtotal)) +
                        String.format("%1$7s",currencyFormat.format(subtotalTax)));

        return returnString;

    }

    private ArrayList<SaleItemTracker> getSortedSaleItemTrackerArrayList() {
        Collections.sort(saleItemTrackerArrayList);
        return saleItemTrackerArrayList;
    }

    public void resetSale() {
        // Reset subtotals
        subtotal = 0;
        subtotalTax = 0;

        // Clear arraylist
        saleItemTrackerArrayList.clear();
    }

    public double getEODTotal() {
        return EODTotal;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getSubtotalTax() {
        return subtotalTax;
    }
}
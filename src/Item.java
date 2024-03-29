/**
 * @author Anthony Peters
 *
 * Holds Items codes, names, prices, and taxable state
 */
public class Item {

    // Global vars
    private String itemCode;
    private String itemName;
    private double price;
    private boolean itemTaxable;

    /**
     * Non-Aug. Constructor
     *
     * Sets fields to placeholder values meant to be overriden
     */
    Item(){
        itemCode = "";
        itemName = "";
        price = 0.00;
        itemTaxable = false;
    }

    /**
     * Aug. Constructor
     *
     * @param importItemCode      String, A or B + [###]
     * @param importItemName      String, title of item
     * @param importPrice         Double, price with 2 Significant figures
     */
    Item(String importItemCode, String importItemName, double importPrice){
        itemCode = importItemCode;
        itemName = importItemName;
        price = importPrice;
        itemTaxable = this.isTaxable(importItemCode);
    }

    /**
     * Returns item taxable boolean
     *
     * @param importItemCode, String
     * @return boolean, itemTaxable
     */
    private boolean isTaxable(String importItemCode) {
        // returns taxable state
        return importItemCode.charAt(0) == 'A';
    }

    /**
     * Returns item code
     *
     * @return String, itemCode
     */
    public String getItemCode() {
        return this.itemCode;
    }

    /**
     * Returns item taxable boolean
     *
     * @return boolean, itemTaxable
     */
    public boolean getItemTaxable() {
        return this.itemTaxable;
    }

    /**
     * Returns item name
     *
     * @return String, itemName
     */
    public String getItemName() {
        return this.itemName;
    }

    /**
     * Returns item price
     *
     * @return double, itemPrice
     */
    public double getItemPrice() {
        return this.price;
    }

    /**
     * Sets item code
     *
     * @param importItemCode, String itemCode
     */
    public void setItemCode(String importItemCode) {
        // Set item code
        this.itemCode = importItemCode;
    }

    /**
     * Sets item taxable flag
     *
     * @param importItemCode, String itemCode
     */
    public void setItemTaxable(String importItemCode) {
        // Check taxable status
        this.itemTaxable = this.isTaxable(importItemCode);
    }

    /**
     * Sets item name
     *
     * @param importItemName, itemName
     */
    public void setItemName(String importItemName) {
        this.itemName = importItemName;
    }

    /**
     * Sets item price
     *
     * @param importPrice, double itemPrice
     */
    public void setPrice(double importPrice) {
        this.price = importPrice;
    }


}

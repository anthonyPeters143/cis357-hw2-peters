import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Anthony Peters
 *
 * The CashRegister contatins initialize and transaction methods to create and run Sale and Item objects to track
 * multiple sales. CashReigster also can add, modifiy, and delete items from the Item data list.
 */

public class CashRegister {

    private static final String
            WELCOME_MESSAGE                     = "\nWelcome to Peter's cash register system!\n",
            FILENAME_MESSAGE                    = "\nInput file : ",
            FILE_INPUT_ERROR_MESSAGE            = "!!! Invalid input",
            BEGINNING_SALE_MESSAGE              = "\nBeginning a new sale? (Y/N) ",
            SALE_INPUT_ERROR_MESSAGE            = "!!! Invalid input\nShould be (Y/N)",
            CODE_INPUT_INCORRECT_MESSAGE        = "!!! Invalid product code\nShould be A[###] or B[###], 0000 = item list, -1 = quit\n",
            QUANTITY_INPUT_INCORRECT_MESSAGE    = "!!! Invalid quantity\nShould be [1-100]",
            BREAK_LINE                          = "--------------------",
            ENTER_CODE_MESSAGE                  = "\nEnter product code : ",
            ITEM_NAME_MESSAGE                   = "         item name : ",
            ENTER_QUANTITY_MESSAGE              = "\n\tEnter quantity : ",
            ITEM_TOTAL_MESSAGE                  = "        item total : $",
            RECEIPT_LINE                        = "\n----------------------------\n",
            RECEIPT_TOP                         = "Items list:\n",
            TENDERED_AMOUNT_RECEIPT             = "\nTendered amount\t\t\t $ ",
            TENDER_AMOUNT_WRONG                 = "\nAmount entered is invalid",
            TENDER_AMOUNT_TOO_SMALL             = "\nAmount entered is too small",
            CHANGE_AMOUNT                       = "Change\t\t\t\t\t $",
            EOD_MESSAGE                         = "\nThe total sale for the day is  $",
    
            UPDATE_PROMPT_MESSAGE               = "\nDo you want to update the items data? (A/D/M/Q): ",
            UPDATE_ERROR_MESSAGE                = "!!! Invalid input\nShould be (A/D/M/Q)",
            UPDATE_CODE_PROMPT                  = "item code: ",
            UPDATE_NAME_PROMPT                  = "item name: ",
            UPDATE_PRICE_PROMPT                 = "item price: ",
    
            UPDATE_ITEM_ALREADY_ADDED           = "!!! item already created",
            UPDATE_ITEM_NOT_FOUND               = "!!! item not found",

    
            UPDATE_CODE_ERROR_MESSAGE           = "!!! Invalid input\nShould be A[###] or B[###]\n",
            UPDATE_NAME_ERROR_MESSAGE           = "!!! Invalid input\nName shouldn't be only digits\n",

            UPDATE_NAME_OLD_MESSAGE             = "!!! Invalid input\nName already used\n",

            UPDATE_PRICE_ERROR_MESSAGE          = "!!! Invalid input\nShould be greater than 0\n",
    
            UPDATE_ADD_SUCCESSFUL               = "Item add successful!\n",
            UPDATE_DELETE_SUCCESSFUL            = "Item delete successful!\n",
            UPDATE_MODIFY_SUCCESSFUL            = "Item modify successful!\n",
    
            THANK_YOU                           = "Thanks for using POST system. Goodbye.",

            FILE_NAME_KEY                       = "item.txt";

    /**
     * ArrayList to store Item objects
     */
    private static ArrayList<Item> itemArrayList;
    private static Sale sale;

    /**
     * System in scanner
     */
    private static Scanner inputScanner;

    /**
     * Format for currency
     */
    private static final DecimalFormat currencyFormat = new DecimalFormat("#,###.00");

    /**
     * Driver for CashRegister class
     */
    public static void run() {
        // Create scanner
        inputScanner = new Scanner(System.in);

        // Declare and Initialization
        initialize(inputScanner);

        // Transaction
        transaction(inputScanner);

        // Close scanner
        inputScanner.close();
    }

    /**
     * Creates Sale and ArrayList objects, Outputs welcome message, then inputs data from item.txt file into ArrayList
     *
     * @param inputScanner Scanner, System.in scanner
     */
    private static void initialize(Scanner inputScanner) {
        // Declare and Initialization
        sale = new Sale();
        itemArrayList = new ArrayList<>();

        // Output welcome msg !!!
        System.out.print(WELCOME_MESSAGE);

        // Prompt for file name and input data from file
        fileInput(inputScanner);
    }

    /**
     * Prompts user for file name, checks input against a key if valid scans in item.txt contents into split file input
     * then convert the data into Item objects
     *
     * @param inputScanner Scanner, System.in scanner
     */
    private static void fileInput(Scanner inputScanner) {
        File fileRef;
        String fileName = null, fileLine;
        String[] splitFileImport;
        Scanner fileScanner;
        boolean fileValid = false;

        // Prompt user for file name, loop till flag is true
        // Should be item.txt
        do {
            // Setup scanner for input
            inputScanner = new Scanner(System.in);

            try {
                // Output file name prompt
                System.out.print(FILENAME_MESSAGE);

                // User input
                fileName = inputScanner.next();

                // Check if file is valid
                if (fileName.matches(FILE_NAME_KEY)){

                    // Switch flag
                    fileValid = true;
                }
                else {
                    // Error file input msg
                    System.out.print(FILE_INPUT_ERROR_MESSAGE);
                }

                // Should add more specific exception
            } catch (Exception ioException) {
                // Error file input msg
                System.out.print(FILE_INPUT_ERROR_MESSAGE);
            }

        } while (!fileValid);

        // input from file
        try {
            // Create file
            fileRef = new File(fileName);

            // Set up scanner
            fileScanner = new Scanner(fileRef);

            // Scan in input and split by new line commas
            fileLine = fileScanner.nextLine();
            splitFileImport = fileLine.split(Pattern.quote(","));

            // For loop to index and input file data into itemArray
            // Input code, name, price into Items objects in the array, advance index by 1, split index by 4
            for (int index = 0, splitIndex = 0; index < 10; index+=1, splitIndex+=3) {
                // Create by index in array and imported from split input array
                itemArrayList.add(new Item(splitFileImport[splitIndex],
                        String.valueOf(splitFileImport[splitIndex+1]),
                        Double.parseDouble(splitFileImport[splitIndex+2])));
            }

            // Close scanner
            fileScanner.close();

        } catch (Exception exception) {
            // File input failed
            exception.printStackTrace();
        }

    }

    /**
     * Driver for transaction loop
     *
     * @param inputScanner Scanner, System.in scanner
     */
    private static void transaction(Scanner inputScanner) {
        int transactionNumber;

        // Loop till checkOut method returns != 1
        do {
            // Pass scanner, run checkOut method
            transactionNumber = checkOut(inputScanner);
        } while (transactionNumber != 1);
    }

    /**
     * Prompts user for a Y or N input. If Y then returnInt=2 and findCode is call to start sale transaction. If N then
     * returnInt=1 and prints end of day total earnings then prompts for updating item data
     *
     * @param inputScanner Scanner, System.in scanner
     */
    private static int checkOut(Scanner inputScanner) {
        // Declare and Initialization
        String userInput = "";
        int returnInt;
        boolean quitFlag = false;

        // Prompt and check input for 'Y'/'y' or 'N'/'n'
        try {
            // Reset quit flag and return int
            quitFlag = false;
            returnInt = 0;

            // Loop input and check till correct
            do {
                // Output Beginning Message
                System.out.print(BEGINNING_SALE_MESSAGE);

                // User input
                userInput = inputScanner.next();

                // Test if input is 1 char long and == Y or y
                if (userInput.matches("[Yy]{1}")) {

                    // Input Correct, user = yes
                    returnInt = 2;

                    // Print text break
                    System.out.print(BREAK_LINE);

                    // New sale
                    findCode(inputScanner);

                }
                // Test if input is 1 char long and == N or n
                else if (userInput.matches("[Nn]{1}")){

                    // Input Correct, user = no
                    returnInt = 1;

                    // EOD earnings
                    System.out.print(EOD_MESSAGE +  String.format("%1$8s",currencyFormat.format(sale.getEODTotal())));
                    
                    // Prompt for update
                    updateItems(inputScanner);

                    // Output thank you message
                    System.out.print("\n" + THANK_YOU);
                }
                else {
                    // Input incorrect
                    // Print incorrect message
                    System.out.print(SALE_INPUT_ERROR_MESSAGE);
                }

            } while (returnInt == 0 || returnInt == 2);

            // Return returnInt
            return returnInt;

        } catch (Exception e) {
            // Input incorrect
            e.printStackTrace();

            return 0;
        }

    }

    /**
     * Prompts for update choices between Add, Delete, Modify, or Quit. If Add then add new item into Item ArrayList. If
     * Delete then remove Item from ArrayList. If Modify then remove old Item object and create new Item object. If Quit
     * then run list items method.
     *
     * @param inputScanner Scanner, System.in scanner
     */
    private static void updateItems(Scanner inputScanner){
        // Declare and Initialization
        String userInput;
        boolean quitFlag = false;

        // Loop till quit flag is true
        do {
            // Prompt for Add, Delete, Mod., Q Items
            System.out.print(UPDATE_PROMPT_MESSAGE);

            // User input
            userInput = inputScanner.next();

            switch (userInput) {
                case "A": {
                    // ADD
                    updateAddItem(inputScanner);
                    break;
                }
                case "D": {
                    // DELETE
                    updateDeleteItem(inputScanner);
                    break;
                }
                case "M": {
                    // MOD
                    updateModifyItem(inputScanner);
                    break;
                }
                case "Q": {
                    // QUIT
                    quitFlag = true;
                    
                    // LIST ITEMS
                    listItems();
                    
                    break;
                }
                default: {
                    // INPUT WRONG
                    System.out.print(UPDATE_ERROR_MESSAGE);
                }
            }
            
           
        } while (!quitFlag);
    }

    /**
     * Prompts user for Item code, name, and price. Creates new Item object with input fields. Outputs successful message
     *
     * @param inputScanner Scanner, System.in scanner
     */
    private static void updateAddItem(Scanner inputScanner) {
        // Declare and Initialization
        String userInput, addCode = "", addName = "";
        double addPrice = 0;
        boolean codeInputFlag = false, nameInputFlag = false, priceInputFlag = false;

        // prompt for code, name, price
        // Find code
        do {
            // Prompt for code input
            System.out.print(UPDATE_CODE_PROMPT);

            // User input
            userInput = inputScanner.next();
            
             // Check if code input is A or B + [###]
            if (userInput.matches("[AB]\\d\\d\\d")) {
                // Code input valid
                
                // Check if item already created, if so returns null
                if (findItemFromCode(userInput) == null) {
                    // Not created before
                    codeInputFlag = true;
                    addCode = userInput;
                    
                } else {
                    // Created before
                    System.out.print(UPDATE_ITEM_ALREADY_ADDED);
                    
                }
            } else {
                // Code input invalid
                System.out.print(UPDATE_CODE_ERROR_MESSAGE);
                
            }
        } while (!codeInputFlag);
        
        // Find name
        do {
            // Prompt for code input
            System.out.print(UPDATE_NAME_PROMPT);

            // Clear scanner buffer
            inputScanner.nextLine();

            // User input
            userInput = inputScanner.nextLine();
            
            // Check name if created before
            if (findItemFromName(userInput) == null) {
                // Item name not created before 
                nameInputFlag = true;
                addName = userInput;
                
            } else {
                // Item name used before
                System.out.print(UPDATE_NAME_OLD_MESSAGE);
                
            }
        } while (!nameInputFlag);
        
        // Find price
        do {
            // Prompt for code input
            System.out.print(UPDATE_PRICE_PROMPT);

            // User input
            userInput = inputScanner.next();
            
            try {
                if (Double.parseDouble(userInput) > 0) {
                    // Input valid
                    priceInputFlag = true;
                    addPrice = Double.parseDouble(userInput);
                    
                } else {
                    // Input invalid
                    System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
                    
                }
                
            } catch (Exception e) {
                // Price input invalid
                System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
            }
        } while (!priceInputFlag);
        
        // Add item to item list
        itemArrayList.add(new Item(addCode,addName,addPrice));
        
        // Output success message
        System.out.print(UPDATE_ADD_SUCCESSFUL);
    }

    /**
     * Prompts for Item code then searches for item by code, then removes it from array list
     *
     * @param inputScanner Scanner, System.in scanner
     */
    private static void updateDeleteItem(Scanner inputScanner) {
        // Declare and Initialization
        String userInput, deleteCode = "";
        boolean codeInputFlag = false;
        
        // Prompt for code
        do {
            // Prompt for code input
            System.out.print(UPDATE_CODE_PROMPT);

            // User input
            userInput = inputScanner.next();
            
             // Check if code input is A or B + [###]
            if (userInput.matches("[AB]\\d\\d\\d")) {
                // Code input valid
                // Check if item already created, if so returns null
                if (findItemFromCode(userInput) == null) {
                    // Not created before
                    System.out.print(UPDATE_ITEM_NOT_FOUND);
                    
                } else {   
                    // Created before
                    codeInputFlag = true;
                    deleteCode = userInput;

                }
            } else {
                // Code input invalid
                System.out.print(UPDATE_CODE_ERROR_MESSAGE);
                
            }
        } while (!codeInputFlag);

        // Delete item from item list
        itemArrayList.remove(findItemFromCode(deleteCode));

        // Output success message
        System.out.print(UPDATE_DELETE_SUCCESSFUL);
        
    }

    /**
     * Prompts for Item code, name, and price, searches for Item by code, then deletes original Item and creates new
     * Item. Outputs success message.
     *
     * @param inputScanner Scanner, System.in scanner
     */
    private static void updateModifyItem(Scanner inputScanner) {
        // Declare and Initialization
        String userInput, modCode = "", modName = "";
        double modPrice = 0;
        boolean codeInputFlag = false, nameInputFlag = false, priceInputFlag = false;
        
        // Prompt for code, name, price
        // Find code
        do {
            // Prompt for code input
            System.out.print(UPDATE_CODE_PROMPT);

            // User input
            userInput = inputScanner.next();
            
             // Check if code input is A or B + [###]
            if (userInput.matches("[AB]\\d\\d\\d")) {
                // Code input valid
                // Check if item already created, if so returns null
                if (findItemFromCode(userInput) == null) {
                    // Not created before
                    System.out.print(UPDATE_ITEM_NOT_FOUND);
                    
                } else {
                    // Created before
                    codeInputFlag = true;
                    modCode = userInput;
                    
                }
            } else {
                // Code input invalid
                System.out.print(UPDATE_CODE_ERROR_MESSAGE);
                
            }
        } while (!codeInputFlag);
        
        // Find name
        do {
            // Prompt for code input
            System.out.print(UPDATE_NAME_PROMPT);

            // Clear scanner buffer
            inputScanner.nextLine();

            // User input
            userInput = inputScanner.nextLine();

            // Check if name is a digit
            if (userInput.matches("\\d+")) {
                // Contains only digits
                System.out.print(UPDATE_NAME_ERROR_MESSAGE);

            } else {
                // Doesn't contain only digits
                nameInputFlag = true;
                modName = userInput;
            }

        } while (!nameInputFlag);
        
        // Find price
        do {
            // Prompt for code input
            System.out.print(UPDATE_PRICE_PROMPT);

            // User input
            userInput = inputScanner.next();
            
            try {
                if (Double.parseDouble(userInput) > 0) {
                    // Input valid
                    priceInputFlag = true;
                    modPrice = Double.parseDouble(userInput);
                    
                } else {
                    // Input invalid
                    System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
                    
                }
                
            } catch (Exception e) {
                // Price input invalid
                System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
            }
        } while (!priceInputFlag);

        // Remove old item
        itemArrayList.remove(findItemFromCode(modCode));

        // Add new item's code, name, and price
        itemArrayList.add(new Item(modCode,modName,modPrice));

        // Output success message
        System.out.print(UPDATE_MODIFY_SUCCESSFUL);
    }

    /**
     * Driver for findQuantity,runReceipt,listItems methods, prompts for Item code. If input=Item.code outputs Item name
     * then runs findQuantity. If input=-1 run runReceipt. If input=0000 print list of Items,Names,Prices
     *
     * @param inputScanner Scanner, System.in scanner
     */
    private static void findCode(Scanner inputScanner) {
        // Declare and Initialization
        Item inputItem;
        String userInput;
        boolean codeInputFlag = false, quitFlag = false;

        // Loop till code input is valid
        do {
            // Reset code flag
            codeInputFlag = false;

            // Prompt for code input
            System.out.print(ENTER_CODE_MESSAGE);

            // User input
            userInput = inputScanner.next();

            // Check if code input is A or B + [###]
            if (userInput.matches("[AB]\\d\\d\\d")) {
                // Find item
                inputItem = findItemFromCode(userInput);

                if (inputItem == null) {
                    // Item not found
                    // Output incorrect code message
                    System.out.print(CODE_INPUT_INCORRECT_MESSAGE);

                } else {
                    // Code input valid
                    // Set input flag to true
                    codeInputFlag = true;

                    // Item found
                    // Output Item name message + item name from itemArray
                    System.out.print(ITEM_NAME_MESSAGE + inputItem.getItemName());

                    // Run findQuantity
                    findQuantity(inputScanner, inputItem);
                }
            } else if (userInput.equals("-1")) {
                // Change flags for quiting and code input to true
                quitFlag = true;
                codeInputFlag = true;

                // Print receipt top
                runReceipt();

            } else if (userInput.equals("0000")) {
                // Print item list
                listItems();

            } else {
                // Output incorrect code message
                System.out.print(CODE_INPUT_INCORRECT_MESSAGE);
            }

        } while (!codeInputFlag || !quitFlag);
    }

    /**
     * Prompt for Item quantity, check if between 1-100, add sale to Sale object, then print total of selection
     *
     * @param inputScanner Scanner, System.in scanner
     * @param inputItem Item, Item to pass to Sale
     */
    private static void findQuantity(Scanner inputScanner, Item inputItem){
        // Declare and Initialization
        int itemQuantity;
        double itemPrice;
        boolean quantityInputFlag = false;

        do {
            try {
                // Prompt for item quantity
                System.out.print(ENTER_QUANTITY_MESSAGE);

                // User input
                itemQuantity = Integer.parseInt(inputScanner.next());

                // Check if quantity is [1-100]
                if (itemQuantity > 0 && itemQuantity <= 100) {
                    // Quantity input valid
                    quantityInputFlag = true;

                    // Calc price
                    itemPrice = inputItem.getItemPrice() * itemQuantity;

                    // Add Item price to Sale object
                    sale.addSaleItem(inputItem,itemQuantity,itemPrice);

                    // Output item total
                    System.out.print(ITEM_TOTAL_MESSAGE +  String.format("%1$8s",currencyFormat.format(itemPrice) + "\n"));
                }
                else {
                    // Quantity Input Incorrect
                    // Print incorrect message
                    System.out.print(QUANTITY_INPUT_INCORRECT_MESSAGE);
                }

            } catch (Exception e) {
                // Quantity input is incorrect
                System.out.print(QUANTITY_INPUT_INCORRECT_MESSAGE);
            }

        } while (!quantityInputFlag);
    }

    /**
     * Searches itemArrayList from Item with matching itemCode value, then returns Item object address. If not found
     * then will return null.
     *
     * @return Item, Item address
     * @param itemCode String, Item code to search against itemArrayList
     */
    private static Item findItemFromCode(String itemCode){
        for (Item item : itemArrayList) {
            if (Objects.equals(item.getItemCode(), itemCode)) {
                // Item found, return item object
                return item;
            }
        }
        // If not found return null
        return null;
    }

    /**
     * Searches itemArrayList from Item with matching itemName value, then returns Item object address. If not found
     * then will return null.
     *
     * @return Item, Item address
     * @param itemName String, Item code to search against itemArrayList
     */
        private static Item findItemFromName(String itemName){
        for (Item item : itemArrayList) {
            if (Objects.equals(item.getItemName(), itemName)) {
                // Item found, return item object
                return item;
            }
        }
        // If not found return null
        return null;
    }

    /**
     * Outputs a receipt string that is created by Sale.createReceipt, output subtotals, prompt for tender, then resets
     * Sale object.
     */
    // Creates receipt, prompts for tender, and output change
    private static void runReceipt() {
        // Declare and Initialization
        boolean tenderCorrectFlag = false;
        double tenderAmount = 0;

        // Print top of receipt
        System.out.print(RECEIPT_LINE + RECEIPT_TOP + sale.createReceipt(currencyFormat));

        // Loop till tendered amount is larger than total with tax
        do {
            try {
                // Prompt for tender amount
                System.out.print(TENDERED_AMOUNT_RECEIPT);

                // User input
                tenderAmount = Double.parseDouble(inputScanner.next());

                // Tender amount is correct
                if (tenderAmount >= sale.getSubtotalTax()) {
                    tenderCorrectFlag = true;

                    // Change
                    // find change by subtracting tenderAmount by Total with tax
                    System.out.print(CHANGE_AMOUNT + String.format("%1$7s",currencyFormat.format(tenderAmount-sale.getSubtotalTax())) + RECEIPT_LINE);

                    // reset sale counter in Sale object
                    sale.resetSale();
                }
                else {
                    // Tender is wrong
                    System.out.print(TENDER_AMOUNT_TOO_SMALL);
                }
            } catch (Exception e) {
                // Tender is wrong
                System.out.print(TENDER_AMOUNT_WRONG);
            }

        } while (!tenderCorrectFlag);
    }

    /**
     * Creates a String of all Items in itemArrayList, loops through itemArrayList getting code, name, and price then
     * outputs it.
     */
    // Outputs list of items included in item list
    private static void listItems() {
        // Declare and Initialization
        String returnString = "";

        // Add code, name, and price headers to top of list
        returnString = returnString.concat(String.format("%-11s %-15s %-12s\n","item code","item name","unit price"));

        // Loop through item list and concat code, name, and price of each item
        for (Item item : itemArrayList) {
            // FORMAT WITH STRING.FORMAT()
            returnString = returnString.concat(String.format("%-11s %-15s %-8s\n",item.getItemCode(),item.getItemName(),
                    currencyFormat.format(item.getItemPrice())));

        }
        // Output string
        System.out.print(returnString);
    }

}

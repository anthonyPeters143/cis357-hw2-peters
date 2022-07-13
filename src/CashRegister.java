import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CashRegister {

    private static final String INVENTORY_FILE = "INVENTORY_FILE",
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
    
            UPDATE_CODE_ERROR_MESSAGE           = "!!! Invalid input\nShould be A[###] or B[###]",
            UPDATE_NAME_ERROR_MESSAGE           = "!!! Invalid input\nName already used",
            UPDATE_PRICE_ERROR_MESSAGE          = "!!! Invalid input\nShould be greater than 0",
    
            UPDATE_ADD_SUCCESSFUL               = "Item add successful!",
            UPDATE_DELETE_SUCCESSFUL            = "Item delete successful!",
            UPDATE_MODIFY_SUCCESSFUL            = "Item modify successful!",
    
            THANK_YOU                           = "Thanks for using POST system. Goodbye.",

            FILE_NAME_KEY                       = "item.txt";

    private static ArrayList<Item> itemArrayList;
    private static Sale sale;

    private static Scanner inputScanner;

    private static final DecimalFormat currencyFormat = new DecimalFormat("#,###.00");

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
     *
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
     *
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
     *
     */
    private static void transaction(Scanner inputScanner) {
        int transactionNumber;

        // Loop till checkOut method returns != 1
        do {
            // Pass scanner, run checkOut method
            transactionNumber = checkOut(inputScanner);
        } while (transactionNumber != 1);
    }

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

    // NTF !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
            
           
        } while (!quitFlag)
    }
    
//             UPDATE_PROMPT_MESSAGE               = "\nDo you want to update the items data? (A/D/M/Q): ",
//             UPDATE_ERROR_MESSAGE                = "!!! Invalid input\nShould be (A/D/M/Q)",
//             UPDATE_CODE_PROMPT                  = "item code: ",
//             UPDATE_NAME_PROMPT                  = "item name: ",
//             UPDATE_PRICE_PROMPT                 = "item price: ",
    
//             UPDATE_ITEM_ALREADY_ADDED           = "!!! item already created",
//             UPDATE_ITEM_NOT_FOUND               = "!!! item not found",
    
//             UPDATE_CODE_ERROR_MESSAGE           = "!!! Invalid input\nShould be A[###] or B[###]",
//             UPDATE_NAME_ERROR_MESSAGE           = "!!! Invalid input",
//             UPDATE_PRICE_ERROR_MESSAGE          = "!!! Invalid input\nShould be greater than 0",
    
//             UPDATE_ADD_SUCCESSFUL               = "Item add successful!",
//             UPDATE_DELETE_SUCCESSFUL            = "Item delete successful!",
//             UPDATE_MODIFY_SUCCESSFUL            = "Item modify successful!",


    private static void updateAddItem(Scanner inputScanner) {
        // Declare and Initialization
        String userInput, addCode, addName, addPrice;
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
                System.out.print(UPDATE_CODE_ERROR_MESSAGE)
                
            }
        } while (!codeInputFlag);
        
        // Find name
        do {
            // Prompt for code input
            System.out.print(UPDATE_NAME_PROMPT);

            // User input
            userInput = inputScanner.next();
            
            // Check name if created before
            // NTF !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! SHOULD BE CHECKING FOR NAME NOT CODE, ANOTHER USE IN MOD PATH
            if (findItemFromName(userInput) == null) {
                // Item name not created before 
                nameInputFlag = true;
                addName = userInput
                
            } else {
                // Item name used before
                System.out.print(UPDATE_NAME_ERROR_MESSAGE);
                
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
                    addPrice = userInput;
                    
                } else {
                    // Input invalid
                    System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
                    
                }
                
            } catch (Execption e) {
                // Price input invalid
                System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
            }
        } while (!priceInputFlag);
        
        // Add item to item list
        
        // Output success message
        System.out.print(UPDATE_ADD_SUCCESSFUL);
    }

    private static void updateDeleteItem(Scanner inputScanner) {
        // Declare and Initialization
        String userInput, deleteCode;
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
                System.out.print(UPDATE_CODE_ERROR_MESSAGE)
                
            }
        } while (!codeInputFlag);
        
        // Delete item from item list
        
        // Output success message
        System.out.print(UPDATE_DELETE_SUCCESSFUL);
        
    }

    private static void updateModifyItem(Scanner inputScanner) {
        // Declare and Initialization
        String userInput, modCode, modName, modPrice;
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
                System.out.print(UPDATE_CODE_ERROR_MESSAGE)
                
            }
        } while (!codeInputFlag);
        
        // Find name
        do {
            // Prompt for code input
            System.out.print(UPDATE_NAME_PROMPT);

            // User input
            userInput = inputScanner.next();
            
            // Check name if created before
            // NTF !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! SHOULD BE CHECKING FOR NAME NOT CODE, ANOTHER USE IN ADD PATH
            if (findItemFromName(userInput) == null) {
                // Item name not created before 
                nameInputFlag = true;
                modName = userInput
                
            } else {
                // Item name used before
                System.out.print(UPDATE_ITEM_ALREADY_ADDED);
                
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
                    modPrice = userInput;
                    
                } else {
                    // Input invalid
                    System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
                    
                }
                
            } catch (Execption e) {
                // Price input invalid
                System.out.print(UPDATE_PRICE_ERROR_MESSAGE);
            }
        } while (!priceInputFlag);
        
        // find code, change name and price
        
        // Output success message
        System.out.print(UPDATE_MODIFY_SUCCESSFUL);
    }

    // Returns returnInt
    // Pram inputScanner
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
                    System.out.print(ITEM_NAME_MESSAGE + inputItem.getitemName());

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
                    itemPrice = inputItem.getPrice() * itemQuantity;

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

    // Returns item if code matches item in arrayList, if not returns null
    
    // ORGINALY findItem 
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
    
    // Returns item if name matches item om arrayList, if not returns null
        private static Item findItemFromName(String itemName){
        for (Item item : itemArrayList) {
            if (Objects.equals(item.getitemName(), itemName)) {
                // Item found, return item object
                return item;
            }
        }
        // If not found return null
        return null;
    }

    // Creates receipt, prompts for tender, and output change
    private static void runReceipt() {
        // Declare and Initialization
        boolean tenderCorrectFlag = false;
        double tenderAmount = 0;

        // Print top of receipt
        // NTC !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
    
    // Outputs list of items included in item list
    private static void listItems() {
        // Declare and Initialization
        String returnString = "";
        
        // Add code, name, and price headers to top of list
       
        // Loop through iitem list and concat code, name, and price of each item
        for (Item item : itemArrayList) {
            // FORMAT WITH STRING.FORMAT()
            
//             item.getItemName() == item name      CHANGE NAME IN ITEM CLASS
//             item.getItemCode() == item code
//             item.getItemPrice() == item price    CHANGE NAME IN ITEM CLASS
            }
        }
        
        // Output string
        System.out.print(returnString);
    }

}

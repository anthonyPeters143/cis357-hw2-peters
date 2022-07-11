// CHANGE NAME TO CASH REG AND CREATE DRIVE CLASS FOR MAIN


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class HW2_Peters {

    private static final String INVENTORY_FILE = "INVENTORY_FILE",
            WELCOME_MESSAGE                     = "\nWelcome to Peter's cash register system!\n",
            FILENAME_MESSAGE                    = "\nInput file : ",
            FILE_INPUT_ERROR_MESSAGE            = "!!! Invalid input",
            BEGINNING_SALE_MESSAGE              = "\nBeginning a new sale? (Y/N) ",
            SALE_INPUT_ERROR_MESSAGE            = "!!! Invalid input\nShould be (Y/N)\n",
            CODE_INPUT_INCORRECT_MESSAGE        = "!!! Invalid product code\nShould be A or B[###], -1 = quit\n",
            QUANTITY_INPUT_INCORRECT_MESSAGE    = "!!! Invalid quantity\nShould be [1-100]",
            BREAK_LINE                          = "--------------------",
            ENTER_CODE_MESSAGE                  = "\nEnter product code : ",
            ITEM_NAME_MESSAGE                   = "         item name : ",
            ENTER_QUANTITY_MESSAGE              = "\n\tEnter quantity : ",
            ITEM_TOTAL_MESSAGE                  = "        item total : $",
            RECEIPT_LINE                        = "\n----------------------------\n",
            RECEIPT_TOP                         = "Items list:\n",
            TENDERED_AMOUNT_RECEIPT             = "\nTendered amount\t\t\t $",
            TENDER_AMOUNT_WRONG                 = "\nAmount entered is invalid",
            TENDER_AMOUNT_TOO_SMALL             = "\nAmount entered is too small",
            CHANGE_AMOUNT                       = "Change\t\t\t\t\t $",
            EOD_MESSAGE                         = "\nThe total sale for the day is  $",
            THANK_YOU                           = "Thanks for using POST system. Goodbye.",

            FILE_NAME_KEY                       = "item.txt";

    private static ArrayList<Item> itemArrayList;
    private static Sale sale;

    private static Scanner inputScanner;

    private static final DecimalFormat currencyFormat = new DecimalFormat("#,###.00");


    /**
     *
     */
    public static void main(String[] args) {
        // Create scanner
        inputScanner = new Scanner(System.in);

        // Declare and Initialization
        initialize(inputScanner);

        // Transaction
        transaction(inputScanner);

        // Close scanner
        inputScanner.close();

        // Finish
        System.exit(0);
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

            // Output Beginning Message
            System.out.print(BEGINNING_SALE_MESSAGE);

            // Loop input and check till correct
            do {
                // User input
                userInput = inputScanner.nextLine();

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
                    System.out.print(EOD_MESSAGE +  String.format("%1$8s",currencyFormat.format(sale.getEODTotal())) +
                            "\n" + THANK_YOU);
                }
                else {
                    // Input incorrect
                    // Print incorrect message
                    System.out.print(SALE_INPUT_ERROR_MESSAGE);
                }

            } while (returnInt == 0);

            // Return returnInt
            return returnInt;

        } catch (Exception e) {
            // Input incorrect
            e.printStackTrace();

            return 0;
        }

    }

    // Returns returnInt
    // Pram inputScanner
    private static void findCode(Scanner inputScanner) {
        // Declare and Initialization
        Item inputItem;
        String userInput;
        boolean codeInputFlag = false, quitFlag = false;

        // Print text break
        System.out.print(BREAK_LINE);

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
                inputItem = findItem(userInput);

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


                // Print EOD

                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


            } else if (userInput.equals("0000")) {
                // Print item list

                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

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
    private static Item findItem(String itemCode){
        for (Item item : itemArrayList) {
            if (Objects.equals(item.getItemCode(), itemCode)) {
                // Item found, return item object
                return item;
            }
        }

        // If not found return null
        return null;
    }






}

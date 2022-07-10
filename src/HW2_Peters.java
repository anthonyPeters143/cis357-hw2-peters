import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class HW2_Peters {

    private static final String INVENTORY_FILE = "INVENTORY_FILE",
            WELCOME_MESSAGE                     = "\nWelcome to Peter's cash register system!\n",
            FILENAME_MESSAGE                    = "Input file:",
            FILE_INPUT_ERROR_MESSAGE            = "!!! Invalid input",
            BEGINNING_SALE_MESSAGE              = "\nBeginning a new sale? (Y/N) ",
            SALE_INPUT_ERROR_MESSAGE            = "!!! Invalid input\nShould be (Y/N)\n",
            CODE_INPUT_INCORRECT_MESSAGE        = "!!! Invalid product code\nShould be A or B[###], -1 = quit",
            QUANTITY_INPUT_INCORRECT_MESSAGE    = "!!! Invalid quantity\nShould be [1-100]",
            BREAK_LINE                          = "--------------------",
            ENTER_CODE_MESSAGE                  = "\nEnter product code: ",
            ITEM_NAME_MESSAGE                   = "         item name: ",
            ENTER_QUANTITY_MESSAGE              = "Enter quantity:     ",
            ITEM_TOTAL_MESSAGE                  = "        item total: $",
            RECEIPT_LINE                        = "\n----------------------------\n",
            RECEIPT_TOP                         ="Items list:\n",
            TENDERED_AMOUNT_RECEIPT             = "\nTendered amount\t\t\t $",
            TENDER_AMOUNT_WRONG                 = "\nAmount entered is invalid",
            TENDER_AMOUNT_TOO_SMALL             = "\nAmount entered is too small",
            CHANGE_AMOUNT                       = "Change\t\t\t\t\t $",
            EOD_MESSAGE                         = "\nThe total sale for the day is  $",
            THANK_YOU                           = "Thanks for using POST system. Goodbye.",

            FILE_NAME_KEY                       = "item.txt";

    private static ArrayList<Item> itemArrayList;
    private static Sale sale;

    private static final DecimalFormat currencyFormat = new DecimalFormat("#,###.00");


    /**
     *
     */
    public static void main(String[] args) {
        // Declare and Initialization
        initialize();

        // Transaction
        transaction();

        // Finish
        System.exit(0);
    }

    /**
     *
     */
    private static void initialize() {
        // Declare and Initialization
        sale = new Sale();

        // Output welcome msg !!!
        System.out.print(WELCOME_MESSAGE);

        // Prompt for file name and input data from file
        fileInput();

    }

    /**
     *
     */
    private static void fileInput() {
        String fileName = null, fileLine;
        String[] splitFileImport;
        Scanner fileScanner, inputScanner;
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

                    // Close scanner
                    inputScanner.close();
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
            // Set up scanner
            fileScanner = new Scanner(fileName);

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
    private static void transaction() {
        int transactionNumber;

        // Loop till checkOut method returns != 1
        do {
            // Run checkOut method
            transactionNumber = checkOut();
        } while (transactionNumber != 1);
    }

    private static int checkOut() {
        // Declare and Initialization
        Scanner inputScanner;
        int returnInt = 0;
        boolean quitFlag = false;

        // Prompt and check input for 'Y'/'y' or 'N'/'n'
        try {
            // Set up scanner
            inputScanner = new Scanner(System.in);

            // Loop input and check till correct
            do {
                // Reset quit flag
                quitFlag = false;

                // Output Beginning Message
                System.out.print(BEGINNING_SALE_MESSAGE);

                // User input
                String userInput = inputScanner.next();

                // Test if input is 1 char long and == Y or y
                if (userInput.matches("[Yy]{1}")) {

                    // Input Correct, user = yes
                    returnInt = 2;

                    // Print text break
                    System.out.print(BREAK_LINE);

                    // New sale
                    newSale(inputScanner);


                }
                // Test if input is 1 char long and == N or n
                else if (userInput.matches("[Nn]{1}")){

                    // Input Correct, user = no
                    returnInt = 1;

                    // EOD earnings
                    System.out.print(EOD_MESSAGE +  String.format("%1$8s",currencyFormat.format(sale.getEODtotal())) +
                            "\n" + THANK_YOU);
                }
                else {
                    // Input incorrect
                    // Print incorrect message
                    System.out.print(SALE_INPUT_ERROR_MESSAGE);
                }

            } while (returnInt == 0);

            // Close input scanner
            inputScanner.close();

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
    private static void newSale(Scanner inputScanner) {
        String userInput;
        boolean codeInputFlag;

        // Print text break
        System.out.print(BREAK_LINE);

        // Loop till code input is valid
        do {
            try {
                // Reset code flag
                codeInputFlag = false;

                // Prompt for code input
                System.out.print(ENTER_CODE_MESSAGE);

                // User input
                userInput = inputScanner.next();

                // Check if code input is A or B + [###]
                if (userInput.matches("[AB]\\d\\d\\d")){
                    // Code input valid

                } else if (Integer.parseInt(userInput) == -1) {
                    quitFlag = true;

                    // Print EOD


                } else {
                    // Code Input Incorrect
                    // Print incorrect message
                    System.out.print(CODE_INPUT_INCORRECT_MESSAGE);
                }

            } catch (NumberFormatException numberFormatException) {
                // Code input is incorrect
                System.out.print(CODE_INPUT_INCORRECT_MESSAGE);
            }

        } while (!codeInputFlag);
    }






}

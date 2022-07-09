import java.util.Scanner;

public class HW2_Peters {

    private static final String INVENTORY_FILE = "INVENTORY_FILE",
            WELCOME_MESSAGE                     = "\nWelcome to Peter's cash register system!\n",
            FILENAME_MESSAGE                    = "Input file:",
            FILE_INPUT_ERROR_MESSAGE            = "!!! Invalid input",
            BEGINNING_SALE_MESSAGE              = "\nBeginning a new sale? (Y/N) ",
            SALE_INPUT_ERROR_MESSAGE            = "!!! Invalid input\nShould be (Y/N)\n",
            CODE_INPUT_INCORRECT_MESSAGE        = "!!! Invalid product code\nShould be [1-10], -1 = quit",
            QUANTITY_INPUT_INCORRECT_MESSAGE    = "!!! Invalid quantity\nShould be [1-100]",
            BREAK_LINE                          = "--------------------",
            ENTER_CODE_MESSAGE                  = "Enter product code: ",
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

    // main class
    public static void main(String[] args) {
        // Declare and Initialization
        initialize();

        // Transaction


        // Finish
        System.exit(0);
    }

    private static void initialize() {
        // Declare and Initialization


        // Prompt for file name and input data from file
        fileInput();

    }

    private static void fileInput() {
        String fileName;
        boolean fileValid = false;

        // Prompt user for file name, loop till flag is true
        // Should be item.txt
        do {
            // Setup scanner for input
            Scanner inputScanner = new Scanner(System.in);

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

            // Should add more specific exception
            } catch (Exception ioException) {
                // Error msg
            }

        } while (!fileValid);

        // input from file
    }









}

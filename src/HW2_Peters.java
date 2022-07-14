// Homework 2: Cash Register Program
// Course: CIS357
// Due date: 7/13/22
// Name: Anthony Peters
// Instructor: Il-Hyung Cho
// Program description: HW2_Peters is a driver program that runs the CashRegister class with uses Item and Sale objects
// to track an Item data list, check out process including quantities, price, and name of items. The program will prompt
// for adding items the sale, adding, deleting, and modifying items of the Item data list, and as well as creating a receipt
// and prompt for tender then give change.
/*
Program features:
Change the item code type to String: Y
Provide the input in CSV format. Ask the user to enter the input file name: Y
Implement exception handling for
    File input: Y
    Checking wrong input data type: Y
    Checking invalid data value: Y
    Tendered amount less than the total amount: Y
Use ArrayList for the items data: A
Adding item data: A
Deleting item data: A
Modifying item data: A
*/


/**
 * @author Anthony Peters
 *
 * Driver class to create a ChasRegister object then run it, after it finishes running it will exit the program
 */
public class HW2_Peters {
    private static CashRegister cashRegister;

    public static void main(String[] args) {
        // Declare and Initialization
        cashRegister = new CashRegister();

        // Start cash register
        CashRegister.run();

        // Finish
        System.exit(0);
    }

}

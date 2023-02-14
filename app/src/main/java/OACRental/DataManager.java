package OACRental;
import java.util.List;
/*
* Customer
*   1. Search for a customer
*   2. Set the active customer
*   3. Get the active customer
* Items
*   1. List items/services for rent, and info about them
*   2. Maintain a cart, including deleting
*   3. Info about the cart, overall, and per item
* Records
*   1. Search for transaction records
* Settings
*   1. Database connection management
*   2. Save/load settings
* */

public class DataManager {
    /*CUSTOMER TRANSACTION INTERACTION DATA*/
    /*Global*/
    private Cart cart; //cart containing all items the current customer has selected to rent
    private Customer activeCustomer;    //the current active customer
    private Transaction activeTransaction;  //the current active transaction

    /*AddCustomer*/
    private static List<Customer> prepopulatedCustomers;   //a list of potential customers created by database query when  entering a name in the CustomerAddPage

    /*Inventory*/
    private static boolean isAdmin; //

    /*Handle access to the current customer in the UI layer*/
    public static void setActiveCustomer(){}
    public static void getActiveCustomer(){}

    /*Handle access to the current transaction in the UI layer*/
    public static void getActiveTransaction(){}
    public static void getCart(){}
    public static void setCart(){}
    public static void clearActiveTransaction(){}

    /*Handle access to the database*/
    public static List<Customer> getPrepopulatedCustomers(){
        return prepopulatedCustomers;
    }
    public static void setPrepopulatedCustomers(){
        //runs getCustomer until 2 customers are equal or it runs 10 times

    }
    public static void getCustomer(String first, String last, String ID, String phone, String email){
        //check for first & last matches in database
        //check for ID matches in database
        //check for phone number match in database
        //check for email match in database
    }
    public static void getLineItem(String itemName){}
    public static void getBundle(String bundleName){}
    public static void getTransactionRecords(){}
    public static void createCustomer(String first, String last, String ID, String phone, String email){
    }

    /*Settings management*/
    public static void getSettingsConfig(){}
    public static void setSettingsConfig(){}
    public static boolean isAdmin(){
        return isAdmin;
    }
    public static void setAdmin(){}


}

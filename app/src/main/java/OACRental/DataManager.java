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
    private static List<Product> cart; //cart containing all items the current customer has selected to rent
    private static Customer activeCustomer;    //the current active customer

    /*AddCustomer*/

    private static Database database;
    private static List<Customer> prepopulatedCustomers;   //a list of potential customers created by database query when  entering a name in the CustomerAddPage


    /*Handle access to the current customer in the UI layer*/
    public static void setActiveCustomer(Customer customer) {
        activeCustomer = customer;
    }
    public static Customer getActiveCustomer() {
        return activeCustomer;
    }

    public static List<Product> getCart() {
        return cart;
    }
    public static void setCart() {

    }
    public static void clearActiveTransaction() {
        cart.clear();
    }

    public List<Customer> searchCustomers(String first, String last, String phone, String id, String email) {
        return database.retrieveCustomers(first, last, phone, id, email);
    }

    public static void createCustomer(String first, String last, String ID, String phone, String email) {
        if(database.retrieveCustomer(first, last, ID, phone, email) == null){
            Customer newCustomer = new Customer(first, last, ID, phone, email);
            setActiveCustomer(newCustomer);
            database.addCustomer(newCustomer);
        }
    }

    public static void addProductToCart(String name) {
        // 1. determine if product exists / is in current inventory
        Product product = database.retrieveProduct(name);
        if( product == null)
        {
            throw new Error("Product not found");
        }
        else if(product.getQuantity() == 0) {
            throw new Error("Out of stock");
        }
        cart.add(product);
        // addendum: when the cart is finished (transaction complete) reduce database qty by cart contents
    }

    public static List<Product> getAllProducts() {
        //return database.getAllProducts();
        return null;
    }


    public static void getLineItem(String itemName) {

    }
    public static void getBundle(String bundleName){}
    public static void getTransactionRecords(){}

    /*Settings management*/
    public static void getSettingsConfig(){}
    public static void setSettingsConfig(){}



    private Customer CurrentCustomer;
    public void SetCustomer(Customer newCustomer){ CurrentCustomer = newCustomer;}
    public Customer GetCustomer(){ return CurrentCustomer; }
    public void NewCustomer(String firstName, String lastName, String idNum, String phone, String email){
        CurrentCustomer = new Customer(firstName, lastName, idNum, phone, email);
    }

}

package OACRental;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
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

    private static MariaDB database;
    private static List<Customer> prepopulatedCustomers;   //a list of potential customers created by database query when  entering a name in the CustomerAddPage


    /**
     * Connect to a database
     * @param url the url of the database to connect to
     * @param port the port to connect on
     * @param databaseName the name of the database on the server to connect to
     * @param username the name of the database user to connect with
     * @param password the user's password
     */
    public static void connectToDatabase(String url, int port, String databaseName, String username, String password) {
        if (database != null && database.isConnected()) {
            database.close();
        }

        // Hard-coded instantiation of a mariadb database
        // refactor into a factory design down the line if new connections required
        database = new MariaDB(url, port, databaseName, username, password);
    }


    /**
     * Set the active customer
     * @param customer the customer
     */
    public static void setActiveCustomer(Customer customer) {
        activeCustomer = customer;
    }

    /**
     * Get the active customer
     * @return the active customer, or null if not set
     */
    public static Customer getActiveCustomer() {
        return activeCustomer;
    }

    public static List<Product> getCart() {
        if (cart == null) {
            cart = new ArrayList<>();
        }

        return cart;
    }
    public static void addProductToCart(Product product) {
        if (cart == null) {
            cart = new ArrayList<>();
        }

        cart.add(product);
    }
    public static void clearActiveTransaction() {
        if (cart == null) {
            cart = new ArrayList<>();
        }

        cart.clear();
    }

    public static List<Customer> searchCustomers(String first, String last, String phone, String id, String email) {
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
        return database.getAllProducts();
    }

    public static List<TransactionRecord> getTransactionRecords(Customer customer){
        return database.retrieveTransactionRecords(customer);

    }
    public static List<TransactionRecord> getTransactionRecords(Customer customer, Date startDate, Date endDate){
        return database.retrieveTransactionRecords(customer, startDate, endDate);
    }
    public static List<TransactionRecord> getTransactionRecords(Date startDate, Date endDate){
        return database.retrieveTransactionRecords(startDate, endDate);
    }

    public static List<Product> searchInventory(String name, String size)
    {
        ArrayList<Product> products = new ArrayList<Product>();
        if(size == null)
        {
            for(Product prod : database.retrieveAllProductsWithName(name)){
                products.add(prod);
            }
            return products;
        }
        else{
            products.add(database.retrieveProduct(name, size));
            return products;
        }

    }
}

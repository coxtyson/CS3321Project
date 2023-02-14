package OACRental;

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
    private Customer CurrentCustomer;
    public void SetCustomer(Customer newCustomer){ CurrentCustomer = newCustomer;}
    public Customer GetCustomer(){ return CurrentCustomer; }
    public void NewCustomer(String firstName, String lastName, String idNum, String phone, String email){
        CurrentCustomer = new Customer(firstName, lastName, idNum, phone, email);
    }
}

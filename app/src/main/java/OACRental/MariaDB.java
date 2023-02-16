package OACRental;

import java.util.Date;
import java.util.List;

public class MariaDB implements Database {

    @Override
    public Customer retrieveCustomer(Customer customer) {
        return null;
    }

    @Override
    public Customer retrieveCustomer(String firstName, String lastName, String Phone, String ID, String email) {
        return null;
    }

    @Override
    public List<Customer> retrieveCustomers(String firstName, String lastName, String phone, String ID, String email) {
        return null;
    }


    @Override
    public TransactionRecord retrieveTransactionRecord(int transactionID) {
        return null;
    }

    @Override
    public List<TransactionRecord> retrieveTransactionRecords(Customer customer) {
        return null;
    }

    @Override
    public List<TransactionRecord> retrieveTransactionRecords(Customer customer, Date startDate, Date endDate) {
        return null;
    }

    @Override
    public List<TransactionRecord> retrieveTransactionRecords(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public Product retrieveProduct() {
        return null;
    }

    @Override
    public void addCustomer(Customer customer) {

    }

    @Override
    public void addProduct(Product product) {

    }

    @Override
    public void updateProduct(Product productOriginal, Product productUpdated) {

    }

    @Override
    public List<Product> getAllProducts(){
        return null;
    }
}

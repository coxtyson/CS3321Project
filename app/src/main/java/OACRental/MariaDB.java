package OACRental;

import java.sql.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class MariaDB implements Database {
    private Connection connection;

    public MariaDB(String url, String username, String password) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(url,username, password);

            if (!connection.isValid(5)) {
                throw new Exception("MariaDB connection failed");
            }
        }
        catch (Exception ex) {
            System.out.println("Connection failed");
            connection = null;
        }
    }

    @Override
    public Customer retrieveCustomer(Customer customer) {
        return retrieveCustomer(customer.getFirstName(), customer.getLastName(), customer.getPhone(), customer.getID(), customer.getEmail());
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
        String sql = "SELECT * FROM TABLE Transactions WHERE ID=(?)";


        /*
        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setInt(1, transactionID);
            ResultSet results = stmnt.executeQuery();

            if (results.first()) {

            }
            else {
                throw new Exception("No transaction with id " + transactionID);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
        */
    }

    @Override
    public List<TransactionRecord> retrieveTransactionRecords(Customer customer) {
        return retrieveTransactionRecords(customer, Date.from(Instant.EPOCH), Date.from(Instant.now()));
    }

    @Override
    public List<TransactionRecord> retrieveTransactionRecords(Customer customer, Date startDate, Date endDate) {
        String sql = "SELECT * FROM TABLE Transactions WHERE CustID=(?) AND Checkout BETWEEN (?) AND (?)";

        return null;
    }

    @Override
    public List<TransactionRecord> retrieveTransactionRecords(Date startDate, Date endDate) {
        String sql = "SELECT * FROM TABLE Transactions WHERE Checkout BETWEEN (?) AND (?)";
        return null;
    }

    @Override
    public Product retrieveProduct() {
        String sql = "SELECT * FROM TABLE Products";
        return null;
    }

    @Override
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO Customers (FirstName, LastName, Identification, Phone, Email) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO Products (Name, Size, Quantity, Price, IsActive, BundleOnly) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    public void updateProduct(Product productOriginal, Product productUpdated) {

    }
}

package OACRental;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MariaDB implements Database {
    private static final int timeoutSeconds = 5;
    private Connection connection;

    public MariaDB(String url, int port, String databaseName, String username, String password) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String path = "jdbc:mysql://" + url + ":" + port + "/" + databaseName;

            connection = DriverManager.getConnection(path, username, password);

            if (!connection.isValid(timeoutSeconds)) {
                throw new Exception("MariaDB connection failed");
            }
        }
        catch (Exception ex) {
            System.out.println("Mariadb connection failed with reason:");
            System.out.println(ex.getMessage());
            connection = null;
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return connection != null && connection.isValid(timeoutSeconds);
        }
        catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        }
        catch (Exception ex) {
            System.out.println("Failed to close database connection with reason: ");
            System.out.println(ex.getMessage());
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


    private void appendCustomersFromResults(List<Customer> list, ResultSet results) throws SQLException {
        while (results.next()) {
            int custid = results.getInt("ID");
            String first = results.getString("FirstName");
            String last = results.getString("LastName");
            String id = results.getString("Identification");
            String phone = results.getString("Phone");
            String email = results.getString("Email");

            Customer cm = new Customer(custid, first, last, id, phone, email);

            boolean present = false;
            for (Customer cust : list) {
                if (cm.equals(cust)) {
                    present = true;
                    break;
                }
            }

            if (!present) {
                list.add(cm);
            }
        }
    }

    /**
     * Search the database for a list of customers matching some criterion
     * @param firstName Nullable - the customer's first name
     * @param lastName Nullable - the customer's last name
     * @param phone Nullable - the customer's phone number
     * @param ID Nullable - the customer's bengal id/driver's license
     * @param email Nullable - the customer's email
     * @return a list of customer's matching all criterion
     */
    @Override
    public List<Customer> retrieveCustomers(String firstName, String lastName, String phone, String ID, String email) {
        List<Customer> custs = new ArrayList<>();

        if (firstName != null && !firstName.isEmpty()) {
            try (var stmt = connection.prepareStatement("SELECT * FROM Customers WHERE FirstName LIKE (?)")) {
                stmt.setString(1, firstName);
                stmt.execute();
                appendCustomersFromResults(custs, stmt.getResultSet());
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }

        if (lastName != null && ! lastName.isEmpty()) {
            try (var stmt = connection.prepareStatement("SELECT * FROM Customers WHERE LastName LIKE (?)")) {
                stmt.setString(1, lastName);
                stmt.execute();
                appendCustomersFromResults(custs, stmt.getResultSet());
            } catch (Exception ignored) {}
        }

        if (phone != null && !phone.isEmpty()) {
            try (var stmt = connection.prepareStatement("SELECT * FROM Customers WHERE Phone LIKE (?)")) {
                stmt.setString(1, phone);
                stmt.execute();
                appendCustomersFromResults(custs, stmt.getResultSet());
            } catch (Exception ignored) {}
        }

        if (ID != null && !ID.isEmpty()) {
            try (var stmt = connection.prepareStatement("SELECT * FROM Customers WHERE Identification LIKE (?)")) {
                stmt.setString(1, ID);
                stmt.execute();
                appendCustomersFromResults(custs, stmt.getResultSet());
            }
            catch (Exception ignored) {}
        }

        if (email != null && !email.isEmpty()) {
            try (var stmt = connection.prepareStatement("SELECT * FROM Customers WHERE Email LIKE (?)")) {
                stmt.setString(1, email);
                stmt.execute();
                appendCustomersFromResults(custs, stmt.getResultSet());
            }
            catch (Exception ignored) {}
        }

        return custs;
    }


    @Override
    public TransactionRecord retrieveTransactionRecord(int transactionID) {
        String sql = "SELECT * FROM TABLE Transactions WHERE ID=(?)";

        try (var stmtnt = connection.prepareStatement(sql)) {
            stmtnt.setInt(1, transactionID);
            ResultSet results = stmtnt.executeQuery();

            if (results.first()) {
                // TODO: need a constructor for this
                //Transaction trans = new Transaction();
                return  null;
            }
            else {
                throw new Exception("No transaction with id " + transactionID);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
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
    public Product retrieveProduct(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM Products WHERE Name=(?)";

        try (var stmtnt = connection.prepareStatement(sql)) {
            stmtnt.setString(1, name);
            ResultSet results = stmtnt.executeQuery();

            if (results.first()) {
                int prodid = results.getInt(1);
                String prodName = results.getString(2);
                String prodSize = results.getString(3);
                int prodQuant = results.getInt(4);
                double prodPrice = results.getDouble(5);
                boolean prodIsActive = results.getBoolean(6);
                boolean prodBundleOnly = results.getBoolean(7);

                return new Product(prodName, prodSize, prodQuant, new Price(prodPrice), prodBundleOnly, prodIsActive);
            }
            else {
                throw new Exception("No product with the name " + name);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    @Override
    public Product retrieveProduct(String name, String size)
    {
        /*
        if (name == null || name.isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM Products WHERE Name=(?) AND Size=(?)";

        try (var stmtnt = connection.prepareStatement(sql)) {
            stmtnt.setString(1, name);
            stmtnt.setString(2, size);
            ResultSet results = stmtnt.executeQuery();

            if (results.first()) {
                int prodid = results.getInt(1);
                String prodName = results.getString(2);
                String prodSize = results.getString(3);
                int prodQuant = results.getInt(4);
                double prodPrice = results.getDouble(5);
                boolean prodIsActive = results.getBoolean(6);
                boolean prodBundleOnly = results.getBoolean(7);

                Product prod = new Product(prodName, prodSize, prodQuant, new Price(prodPrice), prodBundleOnly);
                return prod;
            }
            else {
                throw new Exception("No product with the name " + name + "and size " + size);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
        */
        return null;
    }
    @Override
    public List<Product> retrieveAllProductsWithName(String name) {
        if (name == null) {
            return new ArrayList<>();
        }

        String sql = "SELECT * FROM Products WHERE NAME = (?)";

        try(PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setString(1, name);
            ResultSet results = stmnt.executeQuery();

            ArrayList<Product> prods = new ArrayList<>();

            while(results.next()) {
                int prodid = results.getInt(1);
                String prodName = results.getString(2);
                String prodSize = results.getString(3);
                int prodQuant = results.getInt(4);
                double prodPrice = results.getDouble(5);
                boolean prodIsActive = results.getBoolean(6);
                boolean prodBundleOnly = results.getBoolean(7);

                prods.add(new Product(prodName, prodSize, prodQuant, new Price(prodPrice), prodBundleOnly, prodIsActive));
            }

            return prods;
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
            return new ArrayList<>();
        }
    }

    @Override
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO Customers (FirstName, LastName, Identification, Phone, Email) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setString(1, customer.getFirstName());
            stmnt.setString(2, customer.getLastName());
            stmnt.setString(3, customer.getID());
            stmnt.setString(4, customer.getPhone());
            stmnt.setString(5, customer.getEmail());
            stmnt.execute();
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO Products (Name, Size, Quantity, Price, IsActive, BundleOnly) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    public void updateProduct(Product productOriginal, Product productUpdated) {

    }

    @Override
    public List<Product> bundleToComponents(String name) {
        return null;
    }

    @Override
    public List<Product> bundleToComponents(Product bundle) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM Products";

        List<Product> prods = new ArrayList<>();

        try (var stmnt = connection.prepareStatement(sql)) {
            ResultSet results = stmnt.executeQuery();

            while(results.next()) {
                int prodid = results.getInt(1);
                String prodName = results.getString(2);
                String prodSize = results.getString(3);
                int prodQuant = results.getInt(4);
                double prodPrice = results.getDouble(5);
                boolean prodIsActive = results.getBoolean(6);
                boolean prodBundleOnly = results.getBoolean(7);

                prods.add(new Product(prodName, prodSize, prodQuant, new Price(prodPrice), prodBundleOnly, prodIsActive));
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurred while retrieving mariadb products:");
            System.out.println(ex.toString());
            return prods;
        }

        return prods;
    }
}

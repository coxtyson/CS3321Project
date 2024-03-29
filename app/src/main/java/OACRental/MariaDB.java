package OACRental;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MariaDB implements Database {
    private static final int timeoutSeconds = 5;
    private Connection connection;

    public MariaDB(String url, int port, String databaseName, String username, String password) throws Exception {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String path = "jdbc:mysql://" + url + ":" + port + "/" + databaseName;

            connection = DriverManager.getConnection(path, username, password);

            if (connection == null || !connection.isValid(timeoutSeconds)) {
                throw new Exception("MariaDB connection failed for unknown reason");
            }
        }
        catch (Exception ex) {

            // If Mariadb doesn't contain an OAC database this if statement creates a OAC database
            if(ex.getMessage().contains("Unknown database")) {
                try {
                    String newPath = "jdbc:mysql://" + url + ":" + port;
                    connection = DriverManager.getConnection(newPath, username, password);
                    connection.prepareStatement("CREATE DATABASE OAC").executeUpdate();
                    connection.close();

                    // Read file contents to string
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    InputStream is = classloader.getResourceAsStream("createdb.sql");
                    String sqlFileContents = new BufferedReader(new InputStreamReader(is)).lines().parallel().collect(Collectors.joining("\n"));

                    // Break file contents into individual statements, including empty statements and comments
                    String[] fileParts = sqlFileContents.split("\\n\\n");

                    List<String> statements = new ArrayList<>();

                    // Strip out comments and empty lines
                    for (String line : fileParts) {
                        String trimmed = line.trim();

                        if (!trimmed.startsWith("--") && !trimmed.isEmpty()) {
                            statements.add(trimmed);
                        }
                    }

                    connection = DriverManager.getConnection(newPath + "/" + "OAC", username, password);

                    // Execute all the valid statements
                    for (String sql : statements) {
                        connection.prepareStatement(sql).executeUpdate();
                    }
                }

                catch (Exception newEx) {
                    throw new Exception("Failed to create OAC database. Is your SQL laid out correctly?\n\nOriginal Err:\n" + newEx.getMessage());
                }

            }
            else {
                // Rethrow so the UI can catch
                throw ex;
            }
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
        var custs = this.retrieveCustomers(firstName, lastName, Phone, ID, email, false);

        if (!custs.isEmpty()) {
            return custs.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * Search the database for a list of customers matching some criterion
     * @param firstName Nullable - the customer's first name
     * @param lastName Nullable - the customer's last name
     * @param phone Nullable - the customer's phone number
     * @param ID Nullable - the customer's bengal id/driver's license
     * @param email Nullable - the customer's email
     * @param fuzzy If false, find only customers with exact matching data, otherwise any customer that fuzzy-matches
     * @return a list of customer's matching all criterion
     */
    @Override
    public List<Customer> retrieveCustomers(String firstName, String lastName, String phone, String ID, String email, boolean fuzzy) {
        List<Customer> custs = new ArrayList<>();
        List<String> parameters = new ArrayList<>();
        StringBuilder builder = new StringBuilder("SELECT * FROM Customers WHERE ");

        if (firstName != null && !firstName.trim().isEmpty()) {
            builder.append("FirstName LIKE (?)");
            parameters.add(firstName.trim());
        }

        if (lastName != null && !lastName.trim().isEmpty()) {
            if (!parameters.isEmpty()) {
                builder.append(" AND ");
            }

            builder.append("LastName LIKE (?)");
            parameters.add(lastName.trim());
        }

        if (phone != null && !phone.trim().isEmpty()) {
            if (!parameters.isEmpty()) {
                builder.append(" AND ");
            }

            builder.append("Phone LIKE (?)");
            parameters.add(phone.trim());
        }

        if (ID != null && !ID.trim().isEmpty()) {
            if (!parameters.isEmpty()) {
                builder.append(" AND ");
            }

            builder.append("Identification LIKE (?)");
            parameters.add(ID.trim());
        }

        if (email != null && !email.trim().isEmpty()) {
            if (!parameters.isEmpty()) {
                builder.append(" AND ");
            }

            builder.append("Email LIKE (?)");
            parameters.add(email.trim());
        }

        if (parameters.isEmpty()) {
            return new ArrayList<>();
        }

        String sql = builder.toString();
        try (var stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                if (fuzzy) {
                    stmt.setString(i + 1, "%" + parameters.get(i) + "%");
                }
                else {
                    stmt.setString(i + 1, parameters.get(i));
                }
            }

            ResultSet results = stmt.executeQuery();

            while (results.next()) {
                int custid = results.getInt("ID");
                String first = results.getString("FirstName");
                String last = results.getString("LastName");
                String id = results.getString("Identification");
                String phoneres = results.getString("Phone");
                String emailres = results.getString("Email");

                custs.add(new Customer(custid, first, last, id, phoneres, emailres));
            }

            return custs;
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
            return new ArrayList<>();
        }
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

                return new Product(prodName, prodSize, prodQuant, new Price(prodPrice), prodBundleOnly, prodIsActive, prodid);
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

                Product prod = new Product(prodName, prodSize, prodQuant, new Price(prodPrice), prodBundleOnly, prodIsActive);
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
            stmnt.executeUpdate();
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void addTransaction(Transaction transaction){
        String insertTransactions = "INSERT INTO Transactions(CustID, Checkout, ExpectedReturn, Subtotal, IsActive) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement stmnt = connection.prepareStatement(insertTransactions)){
            stmnt.setString(1, transaction.getCustomer().getID());
            stmnt.setObject(2, transaction.getCheckout());
            stmnt.setObject(3, transaction.getExpectedReturn());
            stmnt.setDouble(4, transaction.getTotalPrice().getTotal());
            stmnt.setBoolean(5, true);
            stmnt.executeUpdate();
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
        
        int LastInsertID = 0;
        String getLastInsertID = "SELECT * FROM Transactions WHERE ID = LAST_INSERT_ID()";
        try(PreparedStatement stmnt = connection.prepareStatement(getLastInsertID)){
            ResultSet LastInsertIDResult = stmnt.executeQuery();
            if(LastInsertIDResult.first()){
                LastInsertID = LastInsertIDResult.getInt(1);
            }
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }

        for(int i = 0; i < transaction.getProducts().size(); i++){
            String insertLineItem = "INSERT INTO LineItem(TransactionID, Name, Size, Price) VALUES (?, ?, ?, ?)";
            try(PreparedStatement stmnt = connection.prepareStatement(insertLineItem)){
                stmnt.setInt(1, LastInsertID);
                stmnt.setString(2, transaction.getProducts().get(i).getName());
                stmnt.setString(3, transaction.getProducts().get(i).getSize());
                stmnt.setDouble(4, transaction.getProducts().get(i).getPrice().getTotal());
                stmnt.executeUpdate();
            }
            catch (Exception ex){
                System.out.println(ex.toString());
            }
        }
    }

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO Products (Name, Size, Quantity, Price, IsActive, BundleOnly) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmnt = connection.prepareStatement(sql)) {
            stmnt.setString(1, product.getName());
            stmnt.setString(2, product.getSize());
            stmnt.setInt(3, product.getQuantity());
            stmnt.setDouble(4, product.getPrice().getTotal());
            stmnt.setBoolean(5, product.isActive());
            stmnt.setBoolean(6, product.isBundleOnly());

            stmnt.executeUpdate();
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void updateProduct(Product productOriginal, Product productUpdated) {
        int id = productOriginal.getDatabaseID();

        if (id == -1) {
            throw new IllegalArgumentException("Original product not in database, make sure to use an object produced by a database call");
        }

        String sql = "UPDATE Products SET Name=(?), Size=(?), Quantity=(?), Price=(?), IsActive=(?), BundleOnly=(?) WHERE ID=(?)";

        try (var stmnt = connection.prepareStatement(sql)) {
            stmnt.setString(1, productUpdated.getName());
            stmnt.setString(2, productOriginal.getSize());
            stmnt.setInt(3, productOriginal.getQuantity());
            stmnt.setDouble(4, productOriginal.getPrice().getTotal());
            stmnt.setBoolean(5, productOriginal.isActive());
            stmnt.setBoolean(6, productOriginal.isBundleOnly());

            stmnt.setInt(7, id);

            stmnt.executeUpdate();
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
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

                prods.add(new Product(prodName, prodSize, prodQuant, new Price(prodPrice), prodBundleOnly, prodIsActive, prodid));
            }
        }
        catch (Exception ex) {
            System.out.println("Exception occurred while retrieving mariadb products:");
            System.out.println(ex.toString());
            return prods;
        }

        return prods;
    }

    @Override
    public List<Product> searchProducts(String name, String size, boolean fuzzy) {
        if ((name == null || name.isEmpty()) && (size == null || size.isEmpty())) {
            return this.getAllProducts();
        }

        // With these methods that have the boolean "fuzzy" toggle, right now it's ignored and it always fuzzy searches,
        // but to enable it, you'd just make it so if fuzzy is false, it doesn't append the % signs when building the statement

        String sql = "SELECT * FROM Products WHERE ";
        List<String> parameters = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            sql += " Name LIKE (?)";
            parameters.add(name.trim());
        }

        if (size != null && !size.isEmpty()) {
            if (!parameters.isEmpty()) {
                sql += " AND";
            }

            sql += "Size LIKE (?)";
            parameters.add(size.trim());
        }

        try (var stmnt = connection.prepareStatement(sql)) {
            for(int i = 0; i < parameters.size(); i++) {
                stmnt.setString(i  + 1, "%" + parameters.get(i) + "%");
            }

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

                prods.add(new Product(prodName, prodSize, prodQuant, new Price(prodPrice), prodBundleOnly, prodIsActive, prodid));
            }

            return prods;
        }
        catch (Exception ex) {
            return new ArrayList<>();
        }
    }
}

package OACRental;

import java.util.List;
import java.util.Date;
public interface Database {

        boolean isConnected();
        void close();

        public Customer retrieveCustomer(Customer customer);
        public Customer retrieveCustomer(String firstName, String lastName, String phone, String ID, String email);
        public List<Customer> retrieveCustomers(String firstName, String lastName, String phone, String ID, String email, boolean fuzzy);
        public TransactionRecord retrieveTransactionRecord(int transactionID);
        public List<TransactionRecord> retrieveTransactionRecords(Customer customer);
        public List<TransactionRecord> retrieveTransactionRecords(Customer customer, Date startDate, Date endDate);
        public List<TransactionRecord> retrieveTransactionRecords(Date startDate, Date endDate);

        public Product retrieveProduct(String name);
        public Product retrieveProduct(String name, String size);
        public List<Product> retrieveAllProductsWithName(String name);
        public List<Product> bundleToComponents(String name);
        public List<Product> bundleToComponents(Product product);
        public List<Product> getAllProducts();

        public void addCustomer(Customer customer);
        public void addProduct(Product product);
        public void updateProduct(Product productOriginal, Product productUpdated);
}

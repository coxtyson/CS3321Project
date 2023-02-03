package OACRental;
import java.util.List;
import java.util.Date;
public interface Database {

        public Customer retrieveCustomer(Customer customer);
        public Customer retrieveCustomer(String firstName, String lastName, String Phone, String ID, String email);
        public TransactionRecord retrieveTransactionRecord(int transactionID);
        public List<TransactionRecord> retrieveTransactionRecords(Customer customer);
        public List<TransactionRecord> retrieveTransactionRecords(Customer customer, Date startDate, Date endDate);
        public List<TransactionRecord> retrieveTransactionRecords(Date startDate, Date endDate);
        public Product retrieveProduct();
        public void addCustomer(Customer customer);
        public void addProduct(Product product);
        public void updateProduct(Product productOriginal, Product productUpdated);



}

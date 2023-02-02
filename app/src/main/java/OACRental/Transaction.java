package OACRental;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
public class Transaction {
    private Customer customer;
    private int discountPercentage;
    private List<Bundle> bundles;
    private List<LineItem> lineItems;
    private Date checkout;
    private Date expectedReturn;

    public Transaction(Customer customer, List<Bundle> bundles, List<LineItem> lineItems, Date checkout,
                       Date expectedReturn, int discountPercentage)
    {
        this.customer = customer;
        this.bundles = bundles;
        this.lineItems = lineItems;
        this.checkout = checkout;
        this.expectedReturn = expectedReturn;
        this.discountPercentage = discountPercentage;
    }

    public Transaction(Customer customer, List<Bundle> bundles, List<LineItem> lineItems, Date checkout, Date expectedReturn){
        this(customer, bundles, lineItems, checkout, expectedReturn, 0);
    }

    public Customer getCustomer(){
        return this.customer;
    }
    public int getDiscount(){
        return this.discountPercentage;
    }
    public Date getCheckout(){
        return this.checkout;
    }
    public Date getExpectedReturn(){
        return this.expectedReturn;
    }
    public List<Bundle> getBundles(){
        return this.bundles;
    }
    public List<LineItem> getLineItems(){
        return this.lineItems;
    }
}

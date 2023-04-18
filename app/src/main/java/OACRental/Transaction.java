package OACRental;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
public class Transaction {
    private Customer customer;
    private int discountPercentage;
    private List<Product> products;
    private LocalDate checkout;
    private LocalDate expectedReturn;

    public Transaction(Customer customer,List<Product> products, LocalDate checkout,
                       LocalDate expectedReturn, int discountPercentage)
    {
        this.customer = customer;
        this.products = products;
        this.checkout = checkout;
        this.expectedReturn = expectedReturn;
        this.discountPercentage = discountPercentage;
    }

    public Transaction(Customer customer, List<Product> products, LocalDate checkout, LocalDate expectedReturn){
        this(customer, products, checkout, expectedReturn, 0);
    }

    public Customer getCustomer(){
        return this.customer;
    }
    public int getDiscount(){
        return this.discountPercentage;
    }
    public LocalDate getCheckout(){
        return this.checkout;
    }
    public LocalDate getExpectedReturn(){
        return this.expectedReturn;
    }
    public List<Product> getProducts(){
        return this.products;
    }

    public Price getTotalPrice() {
        Price totalPrice = new Price(); //$00.00

        //add the prices for every item in the cart
        for(Product p : this.products) {
            totalPrice.add(p.getPrice());
        }
        if(this.discountPercentage > 0) {
            //convert percent to decimal value
            double discountDecimal = this.discountPercentage / (double)100;
            totalPrice.multiply((1-discountDecimal));
        }

        return totalPrice;
    }
}

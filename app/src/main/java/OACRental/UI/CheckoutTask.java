package OACRental.UI;

public class CheckoutTask extends TaskView {

    public CheckoutTask() {
        addPage(new CustomerAddPage(this));
        addPage(new ItemRentPage(this));
        addPage(new CheckoutPage(this));
        jumpPage(0);
    }

    @Override
    public String taskName() {
        return "Checkout";
    }
}
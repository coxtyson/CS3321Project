package OACRental;

public class Product {
    private String name;
    private String size;
    private int quantity;
    private Price price;
    private boolean isBundle;

    public Product(String name, String size, int quantity, Price price){
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }

    //create a product from an existing line item
    public Product(LineItem item, int quantity)
    {
        this(item.getName(), item.getSize(), quantity, item.getPrice());
    }

    public String getName(){
        return this.name;
    }

    public String getSize(){
        return this.size;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public Price getPrice(){
        return this.price;
    }

    public boolean isBundle() {
        return isBundle;
    }

    public void setBundle(boolean bundle) {
        isBundle = bundle;
    }
}

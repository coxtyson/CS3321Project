package OACRental;

public class Product {
    private String name;
    private String size;
    private int quantity;
    private Price price;
    private boolean isBundle;
    private boolean bundleOnly;
    private String itemsInBundle;
    private boolean isActive;

    /*Base constructor for products that are not bundles*/
    public Product(String name, String size, int quantity, Price price, boolean bundleOnly, boolean isActive){
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.bundleOnly = bundleOnly;
        this.isBundle = false;
        this.isActive = isActive;
    }

    /*Product constructor for a product that does not have a size*/
    public Product(String name, int quantity, Price price, boolean bundleOnly, boolean isActive)
    {
        this(name, null, quantity, price, bundleOnly, isActive);
    }

    /*Constructor for products that are bundles*/
    public Product(String name, String items, Price price, boolean isBundle)
    {
        this.name = name;
        this.price = price;
        this.isBundle = true;
        this.itemsInBundle = items;
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

    public boolean isBundleOnly()
    {
        return this.bundleOnly;
    }

    public void makeBundleOnly(boolean bundleOnly)
    {
        this.bundleOnly = bundleOnly;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getPrettyName() {
        if (size != null && !size.isEmpty()) {
            return name + " - " + size;
        }
        else {
            return name;
        }
    }
}

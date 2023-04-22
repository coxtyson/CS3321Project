package OACRental;

import javafx.beans.property.*;

public class Product {
    private int databaseID;
    private String name;
    private String size;
    private int quantity;
    private Price price;
    private boolean isBundle;
    private boolean bundleOnly;
    private String itemsInBundle;
    private boolean isActive;
     


    /*Base constructor for products that are not bundles*/
    public Product(String name, String size, int quantity, Price price, boolean bundleOnly, boolean isActive, int databaseID) {
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.bundleOnly = bundleOnly;
        this.isActive = isActive;

        this.isBundle = false;
        this.itemsInBundle = null;
        this.databaseID = databaseID;
    }

    public Product(String name, String size, int quantity, Price price, boolean bundleOnly, boolean isActive) {
        this(name, size, quantity, price, bundleOnly, isActive, -1);
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
        this.bundleOnly = false;
        this.itemsInBundle = items;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }

        this.name = name;
    }

    public String getSize(){
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        this.quantity = quantity;
    }

    public Price getPrice(){
        return this.price;
    }

    public void setPrice(Price price) {
        if (price == null) {
            throw new IllegalArgumentException("Price may not be null");
        }

        this.price = price;
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

    public int getDatabaseID() { return databaseID; }

    public String getItems()
    {
        if(!this.isBundle)
        {
            return this.name;
        }
        else {
            return this.itemsInBundle;
        }
    }
}

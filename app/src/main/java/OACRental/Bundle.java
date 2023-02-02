package OACRental;

import java.util.List;

public class Bundle {
    private String name;
    private Price price;
    private List<LineItem> items;

    public Bundle(String name, Price price, List<LineItem> items){
        this.name = name;
        this.price = price;
        this.items = items;
    }

    public List<LineItem> getItems() { return this.items; }
    public Price getPrice() { return this.price; }
    public String getName() { return this.name; }
}

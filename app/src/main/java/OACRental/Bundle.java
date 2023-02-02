package OACRental;

import java.util.List;

public class Bundle {
    private String name;
    private Price price;
    private List<LineItem> items;

    public Bundle(String n, Price p, List<LineItem> i){
        name = n;
        price = p;
        items = i;
    }

    public List<LineItem> getItems() { return items; }
    public Price getPrice() { return price; }
    public String getName() { return name; }
}

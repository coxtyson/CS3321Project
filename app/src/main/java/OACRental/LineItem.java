package OACRental;

public class LineItem {
    private String name;
    private String size;
    private Price price;

    public LineItem(String name, String size, Price price)
    {
        this.name = name;
        this.size = size;
        this.price = price;
    }

    public LineItem(String name, Price price)
    {
        this(name, "", price);
    }

    public String getName(){
        return this.name;
    }

    public String getSize(){
        return this.size;
    }

    public Price getPrice(){
        return this.price;
    }
}

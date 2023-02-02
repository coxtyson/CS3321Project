package OACRental;

public class Price {
    protected int dollars;
    protected int cents;

    public Price(int dollars, int cents)
    {
        this.dollars = dollars;
        this.cents = cents;
    }

    public Price(){
        this.dollars = 0;
        this.cents = 0;
    }
    public double getTotal()
    {
        int d = this.dollars;
        int c = this.cents;
        String temp = d + "." + String.format("%02d" , c);
        return Double.parseDouble(temp);
    }

    public int getDollars(){
        return this.dollars;
    }
    public int getCents(){
        return this.cents;
    }
}

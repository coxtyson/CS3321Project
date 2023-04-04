package OACRental;

import javafx.beans.binding.DoubleExpression;

public class Price {
    protected int dollars;
    protected int cents;

    public Price(double value) {
        String[] components = Double.toString(value).split("\\.");
        dollars = 0;
        cents = 0;

        if (components.length == 2) {
            dollars = Integer.parseInt(components[0]);
            String centstring = components[1];

            if (centstring.length() == 1) {
                cents = 10 * Integer.parseInt(centstring);
            }
            else if (centstring.length() == 2) {
                cents = Integer.parseInt(centstring);
            }
            else if (centstring.length() > 2) {
                String centMajor = centstring.substring(0, 3);
                cents = Integer.parseInt(centMajor);

                // ASCII value
                if (centstring.charAt(3) >= '5') {

                }
            }
        }
        else if (components.length == 1) {
            dollars = Integer.parseInt(components[0]);
        }
    }

    public Price(int dollars, int cents) {
        this.dollars = dollars;

        //ensure that if the price is created using 2 integers, the cents integer will not be greater than 99
        if(cents > 99) {
            while (cents > 99) {
                this.dollars++;
                cents -= 100;
            }
        }
        //ensure that if the price is created using a single digit int, that it is represented properly (i.e. 1 cent -> 01)
        if(cents < 10 && cents > 0)
        {
            String centStr = String.format("%02d", cents);

        }
        this.cents = cents;
    }

    public Price() {
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

    /*Method to handle adding two prices together (updates calling price to be itself + the added price)*/
    public void add(Price other) {
        this.cents += other.getCents();

        //ensure that the total cents is not 100 or more
        while (this.cents >= 100) {
            this.dollars++;
            this.cents -= 100;
        }

        this.dollars += other.getDollars();
    }

    /*Multiplies the Price by a given value*/
    public void multiply(double multiplyBy)
    {


    }

    /*Method to round cents if there are more than 2 digits worth of cents*/
    private void roundCents()
    {

    }

    public int getDollars(){
        return this.dollars;
    }
    public int getCents(){
        return this.cents;
    }

    @Override
    public String toString() {
        return "$" + Integer.toString(dollars) + "." + Integer.toString(cents);
    }
}

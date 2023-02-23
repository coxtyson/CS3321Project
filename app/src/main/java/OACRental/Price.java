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

    public int getDollars(){
        return this.dollars;
    }
    public int getCents(){
        return this.cents;
    }
}

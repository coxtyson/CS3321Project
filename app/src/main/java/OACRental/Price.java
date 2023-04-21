package OACRental;

import javafx.beans.binding.DoubleExpression;

public class Price {
    protected int dollars;
    protected int cents;

    public Price(String string) {
        String copy = string;  // Shallow copy at first

        if (copy.startsWith("$")) {
            copy = copy.substring(1); // Will deep copy without the $
        }

        String[] components = copy.split("\\.");
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
                String centMajor = centstring.substring(0, 2);
                cents = Integer.parseInt(centMajor);

                for (int i = 2; i < centstring.length(); i++) {
                    if (centstring.charAt(i) > '0') {
                        cents++;
                        break;
                    }
                }
            }
        }
        else if (components.length == 1) {
            dollars = Integer.parseInt(components[0]);
        }
    }

    public Price(double value) {
        this(Double.toString(value));
    }

    public Price(int dollars, int cents) {
        this.dollars = dollars;
        this.cents = cents;

        while (this.cents > 99) {
            this.dollars++;
            this.cents -= 100;
        }
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

    /** Method to handle adding two prices together (updates calling price to be itself + the added price) */
    public void add(Price other) {
        this.cents += other.getCents();

        //ensure that the total cents is not 100 or more
        while(this.cents >= 100) {
            this.dollars++;
            this.cents -= 100;
        }

        this.dollars += other.getDollars();
    }

    /** Multiplies the Price by a given value */
    public void multiply(double multiplyBy) {
        if(multiplyBy > 0 ) {
            double price = this.getTotal();
            double newPrice = price * multiplyBy;
            Price temp = new Price(newPrice);
            this.cents = temp.getCents();
            this.dollars = temp.getDollars();
        }
        else if (multiplyBy == 0) {
            this.dollars = 0;
            this.cents = 0;
        }
        else {
            System.out.println("Error, attempted to multiply price by a negative value");
        }
    }

    /** Method to round cents if there are more than 2 digits worth of cents*/
    private void roundCents() {

    }

    public int getDollars(){
        return this.dollars;
    }
    public int getCents(){
        return this.cents;
    }

    @Override
    public String toString() { return "$" + Integer.toString(dollars) + "." + String.format("%02d", getCents()); }
}

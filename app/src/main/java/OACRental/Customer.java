package OACRental;


import java.net.IDN;

public class Customer {
    private int databaseID;
    private String firstName;
    private String lastName;
    private String ID;
    private String phone;
    private String email;


    public Customer(int databaseID, String first, String last, String IDnum, String phone, String email) {
        this.databaseID = databaseID;
        this.firstName = first;
        this.lastName = last;
        this.ID = IDnum;
        this.phone = phone;
        this.email = email;
    }

    public Customer(String first, String last, String IDnum, String phone, String email) {
        this(-1, first, last, IDnum, phone, email);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getID() {
        return ID;
    }

    public boolean equals(Customer compareTo)
    {
        // Had to rewrite this because it was pretty wildly un-null safe
        // The database system should guarantee a first and last name,
        // but any other fields could be null in either or both instances.
        // Also intentionally does not check database id, not relevant to us

        if (compareTo == null)
            return false;

        if (!(firstName.equals(compareTo.firstName) && lastName.equals(compareTo.lastName))) {
            return false;
        }

        if (ID != null && !ID.equals(compareTo.ID)) {
            return false;
        }

        if (phone != null && !phone.equals(compareTo.phone)) {
            return false;
        }

        if (email != null && !email.equals(compareTo.email)) {
            return false;
        }

        return true;
    }
}

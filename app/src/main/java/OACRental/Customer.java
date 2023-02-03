package OACRental;


public class Customer {
    private String FirstName;
    private String LastName;
    private String ID;
    private String Phone;
    private String Email;


    public Customer(String first, String last, String IDnum, String phone, String email) {
        FirstName = first;
        LastName = last;
        ID = IDnum;
        Phone = phone;
        Email = email;
    }

    public String getFullName() {
        return FirstName + " " + LastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setFirstName(String name) {
        FirstName = name;
    }

    public void setLastName(String name) {
        LastName = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getID() {
        return ID;
    }
}

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package OACRental;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

public class AppTest {
    
    /*Test local Customer creation and basic accessor methods*/
    @Test
    public void testCustomerLocal()
    {
        Customer testCustomer = new Customer("Test", "Customer", "00000", "123-456-7890", "Test@Test.com");
        assertNotNull(testCustomer, "Customer object must be created");
        assertEquals("Test", testCustomer.getFirstName(), "Customer must have first name");
        assertEquals("Customer", testCustomer.getLastName(), "Customer must have last name");
        assertEquals("00000", testCustomer.getID(), "Customer must have ID");
        assertEquals("123-456-7890", testCustomer.getPhone(), "Customer phone number must be accessible");
        assertEquals("Test@Test.com", testCustomer.getEmail(), "Customer email address must be accessible");
    }

    /*Tests price creation and accessor methods*/
    @Test
    public void testPrice()
    {
        /*Testing base constructor using 2 ints (dollars, cents)*/
        Price testPrice = new Price(11, 11);
        assertNotNull(testPrice);
        assertEquals(testPrice.cents, 11, 0, "Price must have cents");
        assertEquals(testPrice.dollars, 11, 0, "Price must have dollars");
        assertEquals(testPrice.getCents(), 11, 0, "Cents must be accessible");
        assertEquals(testPrice.getDollars(), 11, 0, "Dollars must be accessible");
        assertEquals(testPrice.getTotal(), 11.11, 0.0, "Price must be accessible as a double");

        Price testAddZero = new Price();
        assertNotNull(testAddZero);
        assertEquals(testAddZero.getTotal(), 0.0, 0.0);

        Price testAddOne = new Price(1,1);
        assertEquals(testAddOne.getCents(), 1, 0, "1.01 should only have 1 cent");
        assertEquals(1.01, testAddOne.getTotal(), "1.01 should have total of 1.01");

        Price testPriceDouble = new Price(2.99);
        assertEquals(testPriceDouble.getTotal(), 2.99, 0, "Double price constructor is working");

        /*Testing int constructor with a cent value > 99*/
        Price testCentRollover = new Price(10,105);
        assertEquals(testCentRollover.getDollars(), 11, 0, "105 cents should be converted to $1.05");
        assertEquals(testCentRollover.getCents(), 5, 0, "105 cents should be converted to $1.05");

        /*Testing double constructor with a cent value that has more than 2 digits of decimal values*/
        Price testCentRounding1 = new Price(11.115);
        Price testCentRounding2 = new Price(11.1109);
        assertEquals(11.12, testCentRounding1.getTotal(), 0, "$11.115 should be rounded to $11.12");
        assertEquals(11.12, testCentRounding2.getTotal(), 0, "$11.1109 should be rounded to $11.12");

        /*Testing multiply function*/
    }

    /*Tests Product creation and accessor methods*/
    @Test
    public void testProduct()
    {
        //String name, String size, int quantity, Price price, boolean bundleOnly, boolean isActive
        Price price = new Price(11.11);

        Product testProduct1 = new Product("Test","Small", 10, price, false, false);
        Product testProduct2 = new Product("Test2", 5, price, true, false);
        Product testProduct3 = new Product("TestBundle", "Item1,Item2,Item3", price, true);

        assertNotNull(testProduct1, "Product 1 must be created from primary constructor with size parameter");
        assertNotNull(testProduct2, "Product 2 must be created from secondary function without size parameter");
        assertNotNull(testProduct3, "Product 3 must be created from bundle constructor using Item list parameter");

        /*Test primary single item product functions*/
        assertEquals("Test", testProduct1.getName(), "Product 1 name must be accessible");
        assertEquals("Small", testProduct1.getSize(), "Product 1 size must be accessible");
        assertEquals(10, testProduct1.getQuantity(), "Product 1 quantity must be accessible");
        assertEquals(price, testProduct1.getPrice(), "Product 1 price must be accessible");
        assertFalse(testProduct1.isBundleOnly(), "Product 1 bundle exclusivity must be accessible");
        assertFalse(testProduct1.isActive(), "Product 1 active status must be accessible");
        assertFalse(testProduct1.isBundle(), "Product 1 should not be a bundle");
        assertNull(testProduct1.getItems(), "Product 1 should have no items in bundle, since Product 1 is not a bundle");

        /*Test sizeless single item product*/
        assertNull(testProduct2.getSize(), "Product 2 was made without a size, so its size should be null");

        /*Test product that is a bundle*/
        assertTrue(testProduct3.isBundle(), "Product 3 should be a bundle");
        assertFalse(testProduct3.isBundleOnly(), "Product 3 is a bundle, so it should not be a bundle exclusive item (no infinitely nesting bundles)");
        assertEquals("Item1,Item2,Item3", testProduct3.getItems(), "Product 3 should have bundle items since it is a bundle");

    }

    /*Tests Transaction creation and accessors*/
    @Test
    public void testTransaction()
    {
        Price price = new Price(11.11);
        Customer testCustomer = new Customer("Test", "Customer", "00000", "123-456-7890", "Test@Test.com");
        Product testProduct1 = new Product("Test","Small", 10, price, false, false);
        Product testProduct2 = new Product("Test2", 5, price, true, false);
        Product testProduct3 = new Product("TestBundle", "Item1,Item2,Item3", price, true);

        ArrayList<Product> testProducts = new ArrayList<Product>();
        testProducts.add(testProduct1);
        testProducts.add(testProduct2);
        testProducts.add(testProduct3);

        Date checkoutDate = new Date();
        Date returnDate = new Date();

        Transaction testTransaction = new Transaction(testCustomer, testProducts, checkoutDate, returnDate);

        assertNotNull(testTransaction, "Transaction should be created from constructor");
        //assertEquals();

    }


}

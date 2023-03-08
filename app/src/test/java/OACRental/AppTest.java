/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package OACRental;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
        Price testPrice = new Price(11, 11);

        assertNotNull(testPrice);
        assertEquals(testPrice.cents, 11, 0, "Price must have cents");
        assertEquals(testPrice.dollars, 11, 0, "Price must have dollars");
        assertEquals(testPrice.getCents(), 11, 0, "Cents must be accessible");
        assertEquals(testPrice.getDollars(), 11, 0, "Dollars must be accessible");
        assertEquals(testPrice.getTotal(), 11.11, 0.0, "Price must be accessible as a double");
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
        assertEquals(false, testProduct1.isBundleOnly(), "Product 1 bundle exclusivity must be accessible");
        assertEquals(false, testProduct1.isActive(), "Product 1 active status must be accessible");
        assertEquals(false, testProduct1.isBundle(), "Product 1 should not be a bundle");
        assertEquals(null, testProduct1.getItems(), "Product 1 should have no items in bundle, since Product 1 is not a bundle");

        /*Test sizeless single item product*/
        assertEquals(null, testProduct2.getSize(), "Product 2 was made without a size, so its size should be null");

        /*Test product that is a bundle*/
        assertEquals(true, testProduct3.isBundle(), "Product 3 should be a bundle");
        assertEquals(false, testProduct3.isBundleOnly(), "Product 3 is a bundle, so it should not be a bundle exclusive item (no infinitely nesting bundles)");
        assertEquals("Item1,Item2,Item3", testProduct3.getItems(), "Product 3 should have bundle items since it is a bundle");

    }
}

package OACRental.UI;

import OACRental.Customer;
import OACRental.SettingsManager;
import javafx.scene.control.ListCell;

public class CustomerCell extends ListCell<Customer> {
    public CustomerCell() { }

    @Override
    protected void updateItem(Customer customer, boolean empty) {
        super.updateItem(customer, empty);

        if (empty) {
            setText("");
        }
        else if (customer == null) {
            setText("Null customer");
        }
        else {
            String base = customer.getFullName() + "\n" + customer.getID();

            if ((boolean) SettingsManager.getSettingValue("Show Customer Email")) {
                base += (customer.getEmail() == null ? "\nNo Email" : "\n" + customer.getEmail());
            }

            if ((boolean) SettingsManager.getSettingValue("Show Customer Phone")) {
                base += (customer.getPhone() == null ? "\nNo Phone" : "\n" + customer.getPhone());
            }

            setText(base);
        }
    }
}

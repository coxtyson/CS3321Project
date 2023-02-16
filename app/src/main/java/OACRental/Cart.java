package OACRental;
import java.util.List;

/*Cart used in the DataManager class to track the items to be rented by the user*/
public class Cart {
    public List<Bundle> bundles;
    public List<LineItem> lineItems;

    public void clear() {
        bundles.clear();
        lineItems.clear();
    }

    public void insert(LineItem item) {
        lineItems.add(item);
    }

    public void insert(Bundle bundle) {
        bundles.add(bundle);
    }
}

package OACRental.UI;
public class InventoryTask extends TaskView {

    public InventoryTask()
    {
        //todo add page
        addPage(new InventoryViewerPage(this));
        jumpPage(0);
    }

    @Override
    public String taskName() {
        return "Inventory";
    }
}

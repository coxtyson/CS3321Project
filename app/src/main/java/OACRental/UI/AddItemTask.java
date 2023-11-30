package OACRental.UI;

public class AddItemTask extends TaskView{
    public AddItemTask(){
        addPage(new AddItemPage(this));
        jumpPage(0);
    }
    @Override
    public String taskName(){return "Add Item";}
}

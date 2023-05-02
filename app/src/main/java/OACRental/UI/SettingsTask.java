package OACRental.UI;

public class SettingsTask extends TaskView {
    public SettingsTask() {
        addPage(new SettingsPage());
        jumpPage(0);
    }

    @Override
    public String taskName() {
        return "Settings";
    }
}

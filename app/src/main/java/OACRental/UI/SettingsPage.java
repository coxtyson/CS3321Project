package OACRental.UI;

import OACRental.Setting;
import OACRental.SettingsManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.text.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class SettingsPage extends BorderPane implements Page {


    VBox vboxSettings;
    ScrollPane scrllMain;

    Map specialActions;

    public SettingsPage() {
        setId("pageSettings");

        specialActions = new HashMap<String, Consumer<Setting>>();

        // Special actions are just functions that are called when the setting with the
        // given name is changed. So in this case, I just make it so when the "Theme"
        // combo box is changed, it updates the theme in real time
        specialActions.put("Theme", new Consumer<Setting>() {
            @Override
            public void accept(Setting setting) {
                UI.setStyle((String) setting.get());
            }
        });

        scrllMain = new ScrollPane();
        scrllMain.setId("scrllSettings");

        vboxSettings = new VBox();
        vboxSettings.setId("vboxSettings");
        vboxSettings.setSpacing(25);

        scrllMain.setContent(vboxSettings);

        setCenter(scrllMain);

        VBox vboxControls = new VBox();
        Button btnSave = new Button("Save Settings");
        btnSave.setOnAction(e -> SettingsManager.saveSettings() );

        vboxControls.getChildren().add(btnSave);

        setLeft(vboxControls);
    }


    @Override
    public void update() {
        vboxSettings.getChildren().clear();

        var settings = SettingsManager.getAllSettings();

        for (var grp : settings) {
            VBox box = new VBox();
            box.setId("vboxSettingGrp" + grp.getName());

            Label grplabel = new Label(grp.getName());
            grplabel.setFont(new Font(20));
            box.getChildren().add(grplabel);

            for (var setting : grp) {
                var name = setting.getName();
                var type = setting.getType();
                var options = setting.getOptions();
                var value = setting.get();

                VBox controlAndLabel = new VBox();
                Label lbl = new Label(name);
                Node control = null;

                if (options != null && options.length > 0) {
                    if (type == Setting.DataType.STRING) {
                        var combo = new ComboBox<String>();
                        combo.setItems(toObservableList(options));
                        combo.getSelectionModel().select((String) value);

                        combo.valueProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> observableValue, String old, String newValue) {
                                setting.set(newValue);

                                if (specialActions.containsKey(setting.getName())) {
                                    Consumer<Setting> func = (Consumer<Setting>) specialActions.get(setting.getName());
                                    func.accept(setting);
                                }
                            }
                        });

                        control = combo;
                    }
                    else if (type == Setting.DataType.INTEGER) {
                        var combo = new ComboBox<Integer>();
                        combo.setItems(toObservableList(options));
                        combo.getSelectionModel().select((Integer) value);

                        combo.valueProperty().addListener(new ChangeListener<Integer>() {
                            @Override
                            public void changed(ObservableValue<? extends Integer> observableValue, Integer old, Integer newValue) {
                                setting.set(newValue);

                                if (specialActions.containsKey(setting.getName())) {
                                    Consumer<Setting> func = (Consumer<Setting>) specialActions.get(setting.getName());
                                    func.accept(setting);
                                }
                            }
                        });

                        control = combo;
                    }
                    else {
                        throw new IllegalStateException("Only integer or string fields support limiting options");
                    }
                }
                else if (type == Setting.DataType.BOOLEAN) {
                    var chk = new CheckBox();
                    chk.setSelected((Boolean) value);

                    chk.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                            setting.set(newValue);

                            if (specialActions.containsKey(setting.getName())) {
                                Consumer<Setting> func = (Consumer<Setting>) specialActions.get(setting.getName());
                                func.accept(setting);
                            }
                        }
                    });

                    control = chk;
                }
                else if (type == Setting.DataType.STRING) {
                    var txt = new TextField();
                    txt.setText((String) value);

                    txt.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                            setting.set(newValue);

                            if (specialActions.containsKey(setting.getName())) {
                                Consumer<Setting> func = (Consumer<Setting>) specialActions.get(setting.getName());
                                func.accept(setting);
                            }
                        }
                    });

                    control = txt;
                }
                else if (type == Setting.DataType.INTEGER) {
                    var txt = new TextField();
                    int number = (Integer) value;
                    txt.setText(Integer.toString(number));

                    txt.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                            Integer value = null;

                            try {
                                value = Integer.parseInt(newValue);
                            }
                            catch (Exception ignored) {} // Don't try to update if they type something not a number

                            if (value != null) {
                                setting.set(value);

                                if (specialActions.containsKey(setting.getName())) {
                                    Consumer<Setting> func = (Consumer<Setting>) specialActions.get(setting.getName());
                                    func.accept(setting);
                                }
                            }
                        }
                    });

                    control = txt;
                }

                if (control == null) {
                    throw new IllegalStateException("The generated setting was null, meaning you probably added a new setting type and no code to generate a control for that type");
                }


                controlAndLabel.getChildren().add(lbl);
                controlAndLabel.getChildren().add(control);
                box.getChildren().add(controlAndLabel);
            }

            vboxSettings.getChildren().add(box);
        }
    }

    private static <T> ObservableList<T> toObservableList(Object[] array) {
        ObservableList<T> list = FXCollections.observableArrayList();

        for (Object obj : array) {
            list.add((T) obj);
        }

        return list;
    }
}
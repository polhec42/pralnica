package utilities;

import database.Cloth;
import database.Connect;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.util.ArrayList;

public class TableViewUtilities {

    public static class EditingCell extends TableCell<Cloth, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem().toString());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0,
                                    Boolean arg1, Boolean arg2) {
                    if (!arg2) {
                        commitEdit(textField.getText());
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    public static class ClothCheckBoxDone implements Callback<TableColumn.CellDataFeatures<Cloth, CheckBox>, ObservableValue<CheckBox>> {
        @Override
        public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Cloth, CheckBox> param) {
            Cloth c = param.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(c.getDone() == 1);
            checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
                int input = 0;
                if(new_val == true){
                    input = 1;
                }
                c.setDone(input);
                new Connect().updateCloth(c);
            });
            return new SimpleObjectProperty<>(checkBox);
        }
    }

    public static class ClothCheckBoxTaken implements Callback<TableColumn.CellDataFeatures<Cloth, CheckBox>, ObservableValue<CheckBox>> {
        @Override
        public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Cloth, CheckBox> param) {

            Cloth c = param.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(c.getTaken() == 1);

            checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {

                int input = 0;
                if(new_val == true){
                    input = 1;
                }
                c.setTaken(input);
                new Connect().updateCloth(c);
            });
            return new SimpleObjectProperty<>(checkBox);
        }
    }

    public static class ClothCheckBoxPayed implements Callback<TableColumn.CellDataFeatures<Cloth, CheckBox>, ObservableValue<CheckBox>> {

        @Override
        public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Cloth, CheckBox> param) {
            Cloth c = param.getValue();
            if(c.getPayed() != -1){
                return null;
            }
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(c.getPayed() == 1);
            checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
                int input = 0;
                if(new_val == true){
                    input = 1;
                }
                c.setPayed(input);
                new Connect().updateCloth(c);
            });
            return new SimpleObjectProperty<>(checkBox);
        }
    }

}

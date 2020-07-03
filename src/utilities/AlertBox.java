package utilities;

import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;

public class AlertBox {

    public static void error(String message){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Napaka");
        alert.setHeaderText("Napaka!");
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void success(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setGraphic(new ImageView(AlertBox.class.getResource("/pic/checkmark.png").toString()));
        alert.setTitle("Uspeh!");
        alert.setHeaderText("Uspeh!");
        alert.setContentText(message);

        alert.showAndWait();
    }
}

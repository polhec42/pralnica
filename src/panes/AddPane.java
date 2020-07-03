package panes;


import database.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utilities.AlertBox;

import java.time.LocalDate;
import java.util.ArrayList;

public class AddPane extends BorderPane {

    GridPane dataPane, clothesPane;
    HBox buttonPane;

    Label inputName;
    Label inputLabel;
    DatePicker datePicker;

    ArrayList<ComboBox> clothes;
    ArrayList<TextField> categories, colors, details;

    String volunteerName;

    public AddPane(){

        dataPane = new GridPane();
        clothesPane = new GridPane();
        buttonPane = new HBox();

        setUpDataPane();
        setUpClothesPane();
        setUpButtonPane();

        this.setTop(dataPane);
        this.setCenter(clothesPane);
        this.setBottom(buttonPane);
    }

    public void setUpDataPane(){

        dataPane.setPadding(new Insets(10, 10, 10, 10));
        dataPane.setVgap(10);
        dataPane.setHgap(10);

        Label nameSurname = new Label("Prostovoljec:");
        GridPane.setConstraints(nameSurname, 0, 0);

        inputName = new Label(volunteerName);
        GridPane.setConstraints(inputName, 1, 0);

        //Label label = new Label("Oznaka na perilu:");
        Button label = new Button("Oznaka na perilu:");
        label.setOnAction(ev -> {
            Stage newWindow = new Stage();
            newWindow.setTitle("Šifra");
            newWindow.setScene(new Scene(new CyphersPane(this)));
            newWindow.show();
        });
        GridPane.setConstraints(label, 0, 1);

        inputLabel = new Label();
        GridPane.setConstraints(inputLabel, 1, 1);

        Label date = new Label("Datum:");
        GridPane.setConstraints(date, 0, 2);
        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        GridPane.setConstraints(datePicker, 1, 2);

        dataPane.getChildren().addAll(nameSurname, inputName, label, inputLabel, date, datePicker);
    }

    public void setUpClothesPane(){

        //da pobrisem UI elemente od prej -> resi problem odstranitve polj, ki so dodani z gumbom
        this.clothesPane.getChildren().clear();
        this.dataPane.getChildren().clear();
        this.setUpDataPane();

        clothesPane.setPadding(new Insets(10, 10, 10, 10));
        clothesPane.setVgap(10);
        clothesPane.setHgap(10);

        Label clothe = new Label("Obleka");
        GridPane.setConstraints(clothe, 0,0);
        Label category = new Label("Kategorija");
        GridPane.setConstraints(category, 1, 0);
        Label color = new Label("Barva");
        GridPane.setConstraints(color, 2, 0);
        Label detail = new Label("Podrobnost");
        GridPane.setConstraints(detail, 3, 0);

        clothesPane.getChildren().addAll(clothe, category, color, detail);

        clothes = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            ComboBox cloth = new ComboBox();
            cloth.setPromptText("Izberi");
            cloth.getItems().addAll(Categories.values());
            clothes.add(cloth);
            GridPane.setConstraints(clothes.get(i), 0, i+1);
            clothesPane.getChildren().add(clothes.get(i));
        }

        categories = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            categories.add(new TextField());
            GridPane.setConstraints(categories.get(i), 1, i+1);
            clothesPane.getChildren().add(categories.get(i));
        }

        colors = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            colors.add(new TextField());
            GridPane.setConstraints(colors.get(i), 2, i+1);
            clothesPane.getChildren().add(colors.get(i));
        }

        details = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            details.add(new TextField());
            GridPane.setConstraints(details.get(i), 3, i+1);
            clothesPane.getChildren().add(details.get(i));
        }

        Button add = new Button("Novo oblačilo - plačilo");
        GridPane.setConstraints(add, 0, clothes.size() + 1);

        this.clothesPane.getChildren().add(add);
        add.setOnAction(ev ->{

            this.clothesPane.getChildren().remove(add);

            ComboBox cloth = new ComboBox();
            cloth.setPromptText("Izberi");
            cloth.getItems().addAll(Categories.values());
            clothes.add(cloth);
            int clothNumber = clothes.size() - 1;

            GridPane.setConstraints(clothes.get(clothNumber), 0, clothNumber + 1);
            categories.add(new TextField());
            GridPane.setConstraints(categories.get(clothNumber), 1, clothNumber + 1);
            colors.add(new TextField());
            GridPane.setConstraints(colors.get(clothNumber), 2, clothNumber + 1);
            details.add(new TextField());
            GridPane.setConstraints(details.get(clothNumber), 3, clothNumber + 1);
            Label price = new Label("0,20 €");
            GridPane.setConstraints(price, 4, clothNumber + 1);
            this.clothesPane.getChildren().addAll(clothes.get(clothNumber), categories.get(clothNumber), colors.get(clothNumber), details.get(clothNumber), price);

            this.clothesPane.getChildren().add(add);
            GridPane.setConstraints(add, 0, clothes.size() + 1);

        });

    }


    private void setUpButtonPane(){

        buttonPane.setPadding(new Insets(10, 10, 10, 10));

        Button ok = new Button("OK");
        ok.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));

        ok.setOnAction(e -> {
            String volunteerName = this.volunteerName;
            String cypher;
            LocalDate date;
            try {
                cypher = this.inputLabel.getText();
                date = this.datePicker.getValue();

                if(cypher == null || date == null){
                    throw new NullPointerException();
                }

                if(cypher.length() < 1){
                    AlertBox.error("Vnesi podatke o prostovoljcu, šifri in datumu!");
                    return;
                }
            }catch (NullPointerException ex){
                AlertBox.error("Vnesi podatke o prostovoljcu, šifri in datumu!");
                return;
            }
            ArrayList<Cloth> cloths = new ArrayList<>();

            for(int i = 0; i < clothes.size(); i++){

                //Če ne dodamo oblačila ga ne shranimo
                if(clothes.get(i).getSelectionModel().isEmpty()){
                    if(i == 0){
                        AlertBox.error("Praznega naročila se ne sme oddati!");
                        return;
                    }
                    break;
                }

                if(colors.get(i).getText().length() < 3 || details.get(i).getText().length() < 4){
                    AlertBox.error("Vnesi barvo in podrobnosti za vsako oblačilo!");
                    return;
                }

                cloths.add(new Cloth(clothes.get(i).getSelectionModel().getSelectedItem().toString().toLowerCase(), categories.get(i).getText(), colors.get(i).getText(), details.get(i).getText()));
            }

            int id = new Connect().createNewOrder(
                    volunteerName, cypher, date
            );

            for(int i = 0; i < cloths.size(); i++){
                Cloth c  = cloths.get(i);
                if(i > 6){
                    c.setPayed(-1);
                }
                new Connect().createNewCloth(id, c);
            }

            AlertBox.success("Naročilo je bilo uspešno dodano!");

            this.setUpDataPane();
            this.setUpClothesPane();
        });

        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        buttonPane.getChildren().add(ok);
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public Label getInputLabel() {
        return inputLabel;
    }

    public void setInputLabelText(String text) {
        this.inputLabel.setText(text);
    }
}

package panes;

import database.Connect;
import database.Order;
import database.User;
import database.Volunteer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SearchPane extends BorderPane {

    GridPane searchPart;
    StackPane showPart;

    TableView table;

    TextField input;
    ComboBox<String> category;

    public SearchPane(){

        this.setPadding(new Insets(10, 10, 10, 10));

        searchPart = new GridPane();
        showPart = new StackPane();

        configureSearchPart();
        configureShowPart();

        this.setTop(searchPart);
        this.setCenter(showPart);
    }

    private void configureShowPart() {

        this.showPart.setPadding(new Insets(10,10,10,10));

        table = new TableView();

        TableColumn<String, Order> firstColumn = new TableColumn<>("Šifra");
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("cypher"));

        TableColumn<String, Order> secondColumn = new TableColumn<>("Ime prostovoljca");
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("volunteer"));

        TableColumn<String, Order> thirdColumn = new TableColumn<>("Število kosov oblačil");
        thirdColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfClothes"));

        TableColumn<String, Order> fourthColumn = new TableColumn<>("Datum");
        fourthColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        table.getColumns().addAll(firstColumn, secondColumn, thirdColumn, fourthColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



        table.setRowFactory(e -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if(ev.getClickCount() == 2 && !row.isEmpty()){

                    Stage newWindow = new Stage();
                    newWindow.setTitle("Podrobnosti");
                    newWindow.setScene(new Scene(new DetailsPane(row.getItem())));
                    newWindow.show();
                    newWindow.setOnCloseRequest(evs -> this.updateData());
                }
            });
            return row;
        });

        this.showPart.getChildren().add(table);
    }

    public void configureSearchPart(){

        this.searchPart.setHgap(5);

        category = new ComboBox<>();
        category.getItems().addAll(
                "Šifra",
                "Prostovoljec",
                "Obleka"
        );
        category.getSelectionModel().selectFirst();
        GridPane.setConstraints(category, 0, 0);
        searchPart.getChildren().add(category);

        input = new TextField();
        input.setPromptText(category.getSelectionModel().getSelectedItem());
        GridPane.setConstraints(input, 1, 0);
        searchPart.getChildren().add(input);

        category.setOnAction(e -> input.setPromptText(category.getSelectionModel().getSelectedItem()));

        Button submit = new Button("Išči");

        submit.setOnAction(ev -> {
            String searchCategory = category.getSelectionModel().getSelectedItem();
            String inputString = input.getText();

            this.table.getItems().clear();
            this.table.getItems().addAll(new Connect().getFilteredOrders(searchCategory, inputString, 0));
        });

        GridPane.setConstraints(submit, 2, 0);
        searchPart.getChildren().add(submit);

        searchPart.setAlignment(
                Pos.BASELINE_CENTER
        );

    }

    public void updateData(){

        this.table.getItems().clear();

        if(this.input.getText().length() > 0){
            String searchCategory = category.getSelectionModel().getSelectedItem();
            String inputString = input.getText();
            this.table.getItems().addAll(new Connect().getFilteredOrders(searchCategory, inputString, 0));
        }else {
            this.table.getItems().addAll(new Connect().getOrders(0));
        }
    }
}

package panes;

import database.Connect;
import database.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class UsersPane extends HBox {


    BorderPane volunteersPane;
    BorderPane usersPane;
    TableView table, tableV;

    public UsersPane(){

        this.setPadding(new Insets(10, 10, 10, 10));

        this.volunteersPane = new BorderPane();
        configureVolunteersPane();

        this.usersPane = new BorderPane();
        configureUsersPane();

        this.getChildren().add(volunteersPane);
        this.getChildren().add(usersPane);
    }

    private void configureUsersPane(){

        this.usersPane.setPadding(new Insets(10, 10, 10, 10));

        table = new TableView();

        TableColumn<String, User> firstColumn = new TableColumn<>("Šifra");
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("cypher"));

        TableColumn<String, User> secondColumn = new TableColumn<>("Ime uporabnika");
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(firstColumn, secondColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.usersPane.setCenter(table);

        GridPane addPart = new GridPane();
        addPart.setPadding(new Insets(10, 10, 10, 10));
        addPart.setHgap(10);
        addPart.setVgap(10);

        Label nameSurname = new Label("Ime in priimek:");
        GridPane.setConstraints(nameSurname, 0, 0);

        TextField nameSurnameInput = new TextField();
        GridPane.setConstraints(nameSurnameInput, 1, 0);

        Label cypher = new Label("Šifra:");
        GridPane.setConstraints(cypher, 0, 1);

        TextField cypherInput = new TextField();
        GridPane.setConstraints(cypherInput, 1, 1);

        Button addUser = new Button("Dodaj");
        addUser.setOnAction(e -> {
            new Connect().insertUser(cypherInput.getText(), nameSurnameInput.getText());
            this.updateData();
        });
        GridPane.setConstraints(addUser, 2, 1);


        addPart.getChildren().addAll(nameSurname, nameSurnameInput, cypher, cypherInput, addUser);

        this.usersPane.setBottom(addPart);
    }

    private void configureVolunteersPane(){

        this.volunteersPane.setPadding(new Insets(10, 10, 10, 10));

        //listView = new ListView();
        tableV = new TableView();

        TableColumn<String, User> firstColumn = new TableColumn<>("Ime prostovoljca");
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableV.getColumns().addAll(firstColumn);
        tableV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.volunteersPane.setCenter(tableV);

        GridPane addPart = new GridPane();
        addPart.setPadding(new Insets(10, 10, 10, 10));
        addPart.setHgap(10);
        addPart.setVgap(10);

        Label nameSurname = new Label("Ime in priimek:");
        GridPane.setConstraints(nameSurname, 0, 0);

        TextField nameSurnameInput = new TextField();
        GridPane.setConstraints(nameSurnameInput, 1, 0);

        Button addVolunteer = new Button("Dodaj");
        addVolunteer.setOnAction(e -> {
            new Connect().insertVolunteer(nameSurnameInput.getText());
            this.updateData();
        });
        GridPane.setConstraints(addVolunteer, 2, 0);


        addPart.getChildren().addAll(nameSurname, nameSurnameInput, addVolunteer);

        this.volunteersPane.setBottom(addPart);
    }

    public void updateData(){
        this.tableV.getItems().clear();
        this.tableV.getItems().addAll(new Connect().getVolunteers());

        this.table.getItems().clear();
        this.table.getItems().addAll(new Connect().getUsers());

    }

}

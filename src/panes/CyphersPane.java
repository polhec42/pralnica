package panes;

import database.Connect;
import database.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CyphersPane extends BorderPane {

    BorderPane usersPane;
    GridPane alphabetPane;

    TableView table;

    public CyphersPane(AddPane addPane){

        usersPane = new BorderPane();
        alphabetPane = new GridPane();

        configureUsersPane(addPane);
        configureAlphabetPane();

        this.setCenter(usersPane);
        this.setRight(alphabetPane);
        //this.getChildren().addAll(usersPane, alphabetPane);
    }

    private void configureUsersPane(AddPane addPane){

        this.usersPane.setPadding(new Insets(10, 10, 10, 10));

        table = new TableView();

        TableColumn<String, User> firstColumn = new TableColumn<>("Šifra");
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("cypher"));

        TableColumn<String, User> secondColumn = new TableColumn<>("Ime uporabnika");
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(firstColumn, secondColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        table.setRowFactory(e -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if(ev.getClickCount() == 2 && !row.isEmpty()){
                    addPane.setInputLabelText(row.getItem().getCypher());
                    Stage stage = (Stage)this.getScene().getWindow();
                    stage.close();
                }
            });
            return row;
        });

        this.usersPane.setCenter(table);
        updateData(null);
    }

    private void configureAlphabetPane(){

        this.alphabetPane.setPadding(new Insets(10, 10, 10, 10));
        this.alphabetPane.setHgap(5);
        this.alphabetPane.setVgap(5);

        ArrayList<Button> buttons = new ArrayList<>();

        for(int i = 'A'; i <= 'Z'; i++){
            buttons.add(new Button(Character.toString((char)i)));
        }
        buttons.add(new Button("Č"));
        buttons.add(new Button("Š"));
        buttons.add(new Button("Ž"));
        buttons.add(new Button("*"));

        int counter = 0;

        for(Button b : buttons){
            b.setPrefSize(32, 32);
            this.alphabetPane.getChildren().add(b);
            GridPane.setConstraints(b, counter % 6, counter / 6);
            counter++;


            b.setOnAction(ev -> {
                updateData(b.getText());
            });
        }
    }
    private void updateData(String filter){

        this.table.getItems().clear();

        ArrayList<User> users = new Connect().getUsers();

        if(filter != null && !filter.equals("*")){
            ArrayList<User> filteredUsers = new ArrayList<>();
            for(int i = 0; i < users.size(); i++){
                if(users.get(i).getCypher().substring(0, 1).equals(filter)){
                    filteredUsers.add(users.get(i));
                }
            }
            this.table.getItems().addAll(filteredUsers);

        }else{
            this.table.getItems().addAll(users);
        }
    }
}

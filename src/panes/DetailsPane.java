package panes;

import database.Cloth;
import database.Connect;
import database.Order;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import utilities.TableViewUtilities;

import java.util.ArrayList;

import static javafx.scene.control.cell.CheckBoxTableCell.forTableColumn;

public class DetailsPane extends BorderPane {

    GridPane dataPane;
    StackPane clothesPane;
    HBox buttonPane;

    Order order;

    TableView table;

    public DetailsPane(Order order){

        this.order = order;

        dataPane = new GridPane();
        clothesPane = new StackPane();
        buttonPane = new HBox();

        setUpDataPane();
        setUpClothesPane();
        setUpButtonPane();

        this.setTop(dataPane);
        this.setCenter(clothesPane);
        this.setBottom(buttonPane);

        updateData();
    }

    private void setUpDataPane(){

        dataPane.setPadding(new Insets(10, 10, 10, 10));
        dataPane.setVgap(10);
        dataPane.setHgap(10);

        Label volunteer = new Label("Prostovoljec:");
        Label volunteerData = new Label(order.getVolunteer());
        Label cypher = new Label("Šifra:");
        Label cypherData = new Label(order.getCypher());
        Label date = new Label("Datum:");
        Label dateData = new Label( order.getDate());

        GridPane.setConstraints(volunteer, 0,0);
        GridPane.setConstraints(volunteerData, 1,0);

        GridPane.setConstraints(cypher, 0, 1);
        GridPane.setConstraints(cypherData, 1, 1);

        GridPane.setConstraints(date, 0, 2);
        GridPane.setConstraints(dateData, 1, 2);


        this.dataPane.getChildren().addAll(volunteer, volunteerData, cypher, cypherData, date, dateData);
    }

    private void setUpClothesPane(){

        this.clothesPane.setPadding(new Insets(10,10,10,10));

        table = new TableView();

        table.setEditable(true);
        Callback<TableColumn, TableCell> cellFactory = p -> new TableViewUtilities.EditingCell();

        ArrayList<TableColumn> columnsEdit = new ArrayList<>();
        ArrayList<TableColumn> columnsCheck = new ArrayList<>();


        TableColumn firstColumn = new TableColumn("Obleka");
        firstColumn.setCellFactory(cellFactory);
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("cloth"));
        columnsEdit.add(firstColumn);

        TableColumn seventhColumn = new TableColumn("Kategorija");
        seventhColumn.setCellFactory(cellFactory);
        seventhColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnsEdit.add(seventhColumn);

        TableColumn secondColumn = new TableColumn("Barva");
        secondColumn.setCellFactory(cellFactory);
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        columnsEdit.add(secondColumn);

        TableColumn thirdColumn = new TableColumn("Podrobnost");
        thirdColumn.setCellFactory(cellFactory);
        thirdColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));
        columnsEdit.add(thirdColumn);

        TableColumn fourthColumn = new TableColumn("Urejeno");
        fourthColumn.setCellValueFactory(new PropertyValueFactory<>("done"));
        fourthColumn.setCellValueFactory(new TableViewUtilities.ClothCheckBoxDone());
        columnsCheck.add(fourthColumn);

        TableColumn fifthColumn = new TableColumn("Oddano");
        // to poskrbi da se ne shrani da je odnešeno, če niso vsa oblačila urejena
        ArrayList<Cloth> clothes = new Connect().getClothesOfOrder(order.getId());
        boolean canBeEdited = true;
        for(Cloth cl : clothes){
            if(cl.getDone() != 1){
                canBeEdited = false;
                break;
            }
        }
        if(canBeEdited) {
            fifthColumn.setCellValueFactory(new PropertyValueFactory<>("taken"));
            fifthColumn.setCellValueFactory(new TableViewUtilities.ClothCheckBoxTaken());
        }
        //
        columnsCheck.add(fifthColumn);

        TableColumn sixthColumn = new TableColumn("Plačano");
        sixthColumn.setCellValueFactory(new PropertyValueFactory<>("payed"));
        sixthColumn.setCellValueFactory(new TableViewUtilities.ClothCheckBoxPayed());
        columnsCheck.add(sixthColumn);

        for(TableColumn co : columnsEdit){
            co.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Cloth, String>>) t -> {
                    Cloth c = t.getRowValue();
                    c.setCloth(t.getNewValue());
                    new Connect().updateCloth(c);
                });
        }


        table.getColumns().addAll(firstColumn, seventhColumn, secondColumn, thirdColumn, fourthColumn, fifthColumn, sixthColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.clothesPane.getChildren().add(table);
    }

    private void setUpButtonPane(){

        this.buttonPane.setPadding(new Insets(10, 10, 10, 10));
        this.buttonPane.setSpacing(5.0);

        Button close = new Button("OK");
        Button archived = new Button("Arhiviraj");

        archived.setOnAction(ev -> {
            ArrayList<Cloth> clothes = new Connect().getClothesOfOrder(order.getId());
            for(Cloth c : clothes){
                if(c.getTaken() != 1){
                    return;
                }
            }
            new Connect().archiveOrder(this.order.getId(), true);
            Stage stage = (Stage)archived.getScene().getWindow();
            stage.close();
        });

        close.setOnAction(ev -> {
            Stage stage = (Stage)close.getScene().getWindow();
            stage.close();
        });
        this.buttonPane.setAlignment(Pos.BASELINE_RIGHT);
        this.buttonPane.getChildren().addAll(archived, close);
    }

    private void updateData(){

        this.table.getItems().clear();
        this.table.getItems().addAll(new Connect().getClothesOfOrder(this.order.getId()));

    }
}

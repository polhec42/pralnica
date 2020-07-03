package utilities;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import panes.*;

public class Scenes {

    double width;
    double height;

    Stage window;
    BorderPane mainPane;
    AddPane addPane;
    SearchPane searchPane;
    UsersPane usersPane;
    ArchivePane archivePane;
    SignInPane loginPane;

    boolean login = false;

    public Scenes(Stage window, double width, double height){

        this.window = window;
        this.width = width;
        this.height = height;

        window.setTitle("Pralnica");

        this.mainPane = new BorderPane();
        this.mainPane.setPrefSize(width, height);

        Navigation navigation = new Navigation(mainPane.getPrefWidth()/9, height, this);

        StackPane pane = new StackPane();

        addPane = new AddPane();
        searchPane = new SearchPane();
        usersPane = new UsersPane();
        archivePane = new ArchivePane();
        loginPane = new SignInPane(this, addPane);

        mainPane.setLeft(navigation);
        mainPane.setCenter(pane);
        changeScene("login");
        window.setScene(new Scene(mainPane, width, height));
        window.show();
    }

    public void changeScene(String change){

        if(!login && !change.equals("login")){
            return;
        }

        switch (change){
            case "login":{
                mainPane.getChildren().remove(mainPane.getCenter());
                mainPane.setCenter(this.loginPane);
                break;
            }
            case "search":{
                this.searchPane.configureSearchPart();
                this.searchPane.updateData();
                mainPane.getChildren().remove(mainPane.getCenter());
                mainPane.setCenter(this.searchPane);
                break;
            }
            case "add":{
                this.addPane.setUpDataPane();
                this.addPane.setUpClothesPane();
                mainPane.getChildren().remove(mainPane.getCenter());
                mainPane.setCenter(this.addPane);
                break;
            }
            case "users":{
                this.usersPane.updateData();
                mainPane.getChildren().remove(mainPane.getCenter());
                mainPane.setCenter(this.usersPane);
                break;
            }
            case "archive":{
                this.archivePane.configureSearchPart();
                this.archivePane.updateData();
                mainPane.getChildren().remove(mainPane.getCenter());
                mainPane.setCenter(this.archivePane);
                break;
            }
        }
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}

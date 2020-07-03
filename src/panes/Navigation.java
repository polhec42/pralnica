package panes;

import utilities.Scenes;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Navigation extends VBox implements EventHandler {

    Scenes scenes;
    Button logo, search, add, archive, users;
    BorderPane mainPane;

    public Navigation(double width, double height, Scenes scenes){

        this.scenes = scenes;

        this.setPrefSize(width, height);
        this.setSpacing(20);
        this.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));

        this.logo = new Button();
        configureButton(logo, "/pic/vzd.png", width, 95);

        this.search = new Button();
        configureButton(search, "/pic/search.png", width, 50);

        this.add = new Button();
        configureButton(add, "/pic/plus-button.png", width, 50);

        this.archive = new Button();
        configureButton(archive, "/pic/inbox.png", width, 50);

        this.users = new Button();
        configureButton(users, "/pic/user.png", width, 50);

    }

    private void configureButton(Button button, String path, double width, double height){
        button.setPrefSize(width, height);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource(path).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true,  true, false)
        );
        Background background = new Background(backgroundImage);
        button.setBackground(background);
        button.setOnAction(this);
        this.getChildren().add(button);
    }

    @Override
    public void handle(Event event) {

        if(event.getSource() == search){
            this.scenes.changeScene("search");
        }else if(event.getSource() == add){
            this.scenes.changeScene("add");
        }else if(event.getSource() == archive){
            this.scenes.changeScene("archive");
        }else if(event.getSource() == users){
            this.scenes.changeScene("users");
        }
    }
}

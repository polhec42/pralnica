package panes;

import database.Connect;
import database.Volunteer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import utilities.Scenes;

import java.util.ArrayList;

public class SignInPane extends VBox {

    TextField input;
    Button submit;

    GridPane inputPane;
    BorderPane logoPane;

    public SignInPane(Scenes scene, AddPane addPane){

        logoPane = new BorderPane();
        inputPane = new GridPane();

        configureLogoPane();
        configureInputPane(scene, addPane);

        this.getChildren().addAll(logoPane, inputPane);
    }

    public void configureLogoPane(){
        this.logoPane.setPrefSize(150, 150);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/pic/vzd.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(150, 150, true, true,  true, false)
        );
        Background background = new Background(backgroundImage);
        this.logoPane.setBackground(background);

    }

    public void configureInputPane(Scenes scene, AddPane addPane){

        this.inputPane.setPadding(new Insets(10, 10, 10, 10));
        this.inputPane.setHgap(5);

        input = new TextField();
        input.setPromptText("Ime in priimek");
        GridPane.setConstraints(input, 0, 0);
        submit = new Button("Prijava");
        GridPane.setConstraints(submit, 1, 0);

        submit.setOnAction(e -> {

            String name = input.getText().toString().toUpperCase();

            ArrayList<Volunteer> volunteers = new Connect().getVolunteers();

            for(Volunteer v : volunteers){
                if(v.getName().toUpperCase().equals(name)){
                    addPane.setVolunteerName(v.getName());
                    scene.setLogin(true);
                    scene.changeScene("search");
                }
            }
        });
        this.inputPane.setAlignment(Pos.BASELINE_CENTER);
        this.inputPane.getChildren().addAll(input, submit);
    }
}

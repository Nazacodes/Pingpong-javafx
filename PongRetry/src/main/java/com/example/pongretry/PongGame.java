package com.example.pongretry;

import com.example.pongretry.controllers.GameMenuController;
import com.example.pongretry.view.GameMenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class PongGame extends Application {
    private static final double ASPECT_RATIO = 16.0 / 9.0;


    public void start(Stage primaryStage) {
        System.setProperty("prism.order", "gpu");
        GameMenuView gameMenuView = new GameMenuView();

        double Height = 1000 / ASPECT_RATIO;

        Scene menuScene = new Scene(gameMenuView, 1000, Height);
        menuScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheets/style.css")).toExternalForm());


        GameMenuController controller = new GameMenuController(gameMenuView, primaryStage, menuScene);

        controller.showMenuScene();

        primaryStage.setTitle("NAZAPONG!");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

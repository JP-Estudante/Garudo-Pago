package com.GuardouPagou.models;

import com.GuardouPagou.controllers.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        new MainController(mainView);
        
        Scene scene = new Scene(mainView.getRoot(), 950, 700);
        primaryStage.setTitle("GuardouPagou - Sistema de Notas e Faturas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
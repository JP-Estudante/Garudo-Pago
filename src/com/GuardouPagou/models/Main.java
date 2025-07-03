package com.GuardouPagou.models;

import com.GuardouPagou.views.MainView;
import com.GuardouPagou.controllers.MainController;
import com.GuardouPagou.services.AlertaService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private AlertaService alertaService;
    @Override
    public void start(Stage primaryStage) {
        // 🅰️ Carregando a fonte Poppins antes de qualquer tela
        Font poppinsRegular = Font.loadFont(
                Main.class.getClassLoader().getResourceAsStream("fonts/Poppins-Regular.ttf"), 12
        );

        Font poppinsBold = Font.loadFont(
                Main.class.getClassLoader().getResourceAsStream("fonts/Poppins-Bold.ttf"),
                12
        );
        Font poppinsMedium = Font.loadFont(
                Main.class.getClassLoader().getResourceAsStream("fonts/Poppins-Medium.ttf"),
                12
        );

        // Saída de confirmação
        System.out.println("Fonte Poppins Regular: " + (poppinsRegular != null ? "OK" : "Erro"));
        System.out.println("Fonte Poppins Bold: " + (poppinsMedium != null ? "OK" : "Erro"));
        System.out.println("Fonte Poppins Bold: " + (poppinsBold != null ? "OK" : "Erro"));

        // Cria visual e controller
        MainView mainView = new MainView();
        new MainController(mainView);

        alertaService = new AlertaService();
        alertaService.iniciar();

        Scene scene = new Scene(mainView.getRoot(), 950, 700);

        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm()
        );

        // Ícone da janela
        primaryStage.getIcons().add(
                new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream(
                                "/icons/G-Clock(100x100px).png"
                        ))
                )
        );

        primaryStage.setTitle("GuardouPagou - Sistema de Notas e Faturas");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (alertaService != null) {
            alertaService.parar();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

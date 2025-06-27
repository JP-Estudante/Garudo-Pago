package com.GuardouPagou.views;

import javafx.scene.control.TableView;

public class ViewUtils {
    public static void aplicarEstiloPadrao(TableView<?> tabela) {
        if (!tabela.getStyleClass().contains("table-padrao")) {
            tabela.getStyleClass().add("table-padrao");
        }
    }
}
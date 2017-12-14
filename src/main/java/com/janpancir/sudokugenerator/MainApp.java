/*
 * The MIT License (MIT)
 * Copyright (c) 2017 Jan Pancíř
 * www.janpancir.com
 */
package com.janpancir.sudokugenerator;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author pancijan
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Setup s = new Setup();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Sudoku generátor");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}

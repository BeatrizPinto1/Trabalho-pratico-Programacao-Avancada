package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.SistemaManager;

public class MainJFX extends Application {
    SistemaManager sistemaManager;

    @Override
    public void init() throws Exception {
        super.init();
        sistemaManager = new SistemaManager(); // here or in the constructor
    }

    @Override
    public void start(Stage stage) throws Exception {
        RootPane root = new RootPane(sistemaManager);
        Scene scene = new Scene(root,700,750);
        stage.setScene(scene);
        stage.setTitle("Management system of projects and stages");
        stage.setMinWidth(700);
        stage.setMinHeight(750);
        stage.show();
    }
}

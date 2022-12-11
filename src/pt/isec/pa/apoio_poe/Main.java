package pt.isec.pa.apoio_poe;

import javafx.application.Application;
import pt.isec.pa.apoio_poe.model.fsm.SistemContext;
import pt.isec.pa.apoio_poe.ui.gui.MainJFX;
import pt.isec.pa.apoio_poe.ui.text.SistemUI;

public class Main {
    public static void main(String[] args) {
        Application.launch(MainJFX.class,args);
    }
    /*public static void main(String[] args) {
        SistemContext fsm = new SistemContext();
        SistemUI ui = new SistemUI(fsm);
        ui.start();
    }*/
}

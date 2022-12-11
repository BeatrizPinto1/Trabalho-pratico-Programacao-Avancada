package pt.isec.pa.apoio_poe.ui.gui.resources.util;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

import java.util.Timer;
import java.util.TimerTask;

public class ToastMessage {
    private ToastMessage() {}

    public static void show(Window owner, String message) {
        final Popup popup = new Popup();
        popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece
        popup.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT);
        double x = owner.getX();
        double y = owner.getY();
        double w = owner.getWidth();
        double h = owner.getHeight();
        Label lbMessage = new Label(message); //cria a label

        //modificar a label
        lbMessage.setTextFill(Color.WHITE);
        BorderPane pane = new BorderPane(lbMessage);
        pane.setPadding(new Insets(10));
        pane.setBackground(new Background(
                new BackgroundFill(Color.color(0.5,0.5,0.5,0.75),
                        new CornerRadii(5),Insets.EMPTY)) //cantos arredondados
        );
        popup.getContent().add(pane);
        popup.show(owner,x+w/2-message.length()/2.0*lbMessage.getFont().getSize(),y+0.80*h); //mostrar popup
        Timer timer = new Timer(true);  //crio um timer
        TimerTask task = new TimerTask() { //esta tarefa vai fazer o hide
            @Override
            public void run() {
                Platform.runLater(()->{popup.hide();}); //platform.runLater para quando temos threads e queremos configurar sem ser a principal
            }
        };
        timer.schedule(task,3000); //desaparece em 3 segundos
    }
    }

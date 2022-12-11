package pt.isec.pa.apoio_poe.ui.gui;

import javafx.scene.layout.*;
import pt.isec.pa.apoio_poe.model.SistemaManager;
import pt.isec.pa.apoio_poe.model.fsm.AssignmentProposalsLOCK;
import pt.isec.pa.apoio_poe.model.fsm.ProposalsManagement;
import pt.isec.pa.apoio_poe.model.fsm.TeachersManagement;
import pt.isec.pa.apoio_poe.ui.gui.resources.CSSManager;
import pt.isec.pa.apoio_poe.ui.gui.resources.ImageManager;

public class RootPane extends BorderPane {
    SistemaManager sistemaManager;

    public RootPane(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        CSSManager.applyCSS(this,"styles.css");
        StackPane stackPane = new StackPane(
                new ConfigurationStateUI(sistemaManager),
                new ConfigurationLOCKUI(sistemaManager),
                new StudentsManagementUI(sistemaManager),
                new TeachersManagementUI(sistemaManager),
                new ProposalsManagementUI(sistemaManager),
                new ApplicationOptionsUI(sistemaManager),
                new ApplicationOptionsLOCKUI(sistemaManager),
                new AssignmentProposalsUI(sistemaManager),
                new AssignmentProposalsLOCKUI(sistemaManager),
                new AssignmentAdvisersUI(sistemaManager),
                new ConsultStateUI(sistemaManager)
        );
        stackPane.setBackground(new Background(new BackgroundImage(
                ImageManager.getImage("background6.png"),
                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)
        ));
        this.setCenter(stackPane);
    }

    private void registerHandlers() {
        sistemaManager.addPropertyChangeListener(evt -> {
            update();
        });
    }

    private void update() {

    }
}

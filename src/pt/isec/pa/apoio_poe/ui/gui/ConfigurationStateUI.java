package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import pt.isec.pa.apoio_poe.model.SistemaManager;
import pt.isec.pa.apoio_poe.model.fsm.SistemState;
import pt.isec.pa.apoio_poe.ui.gui.resources.util.ToastMessage;

import java.io.File;

public class ConfigurationStateUI extends BorderPane {
    SistemaManager sistemaManager;
    Button btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8;
    Label lbEstado,lbAlunos, lbDocentes, lbPropostas;

    public ConfigurationStateUI(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lbEstado = new Label("Configuration State");
        lbEstado.setPadding(new Insets(15));
        //lbEstado.setTextFill(Color.DARKRED);
        //setTop(lbEstado);
        setAlignment(lbEstado, Pos.TOP_CENTER);
        //VBox vBox0 = new VBox(lbEstado);
        lbEstado.setTranslateY(80);
        this.setTop(lbEstado);

        btns1 = new Button("Students Management");
        btns1.setMinWidth(250);
        btns2 = new Button("Teachers Management");
        btns2.setMinWidth(250);
        btns3 = new Button("Proposals Management");
        btns3.setMinWidth(250);
        btns4 = new Button("Save");
        btns4.setMinWidth(250);
        btns5 = new Button("Load");
        btns5.setMinWidth(250);
        btns6 = new Button("Close Phase");
        btns6.setMinWidth(250);
        btns7 = new Button("Next Phase");
        btns7.setMinWidth(250);
        btns8 = new Button("Quit");
        btns8.setMinWidth(250);
        VBox vBox = new VBox(btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        this.setCenter(vBox);

        lbAlunos = new Label("Students: ");
        lbAlunos.setId("labeldraw");
        lbDocentes = new Label("Teachers: ");
        lbDocentes.setId("labeldraw");
        lbPropostas = new Label("Proposals: ");
        lbPropostas.setId("labeldraw");

        HBox hBox = new HBox(lbAlunos, lbDocentes, lbPropostas);
        hBox.setAlignment(Pos.CENTER);
        this.setBottom(hBox);
        hBox.setSpacing(30);
        hBox.setTranslateY(-20);
    }

    private void registerHandlers() {
        sistemaManager.addPropertyChangeListener(evt -> { update(); });
        btns1.setOnAction( event -> {
            sistemaManager.change(SistemState.STUDENTS_MANAGEMENT);
        });
        btns2.setOnAction( event -> {
            sistemaManager.change(SistemState.TEACHERS_MANAGEMENT);
        });
        btns3.setOnAction( event -> {
            sistemaManager.change(SistemState.PROPOSALS_MANAGEMENT);
        });
        btns4.setOnAction(actionEvent -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setContentText("Nome: ");
            tid.setHeaderText("Que nome deseja dar ao ficheiro: ");
            tid.setTitle("Nome do ficheiro para guardar");
            tid.showAndWait();
            String nome = tid.getResult();

            sistemaManager.guarda(nome);
        });
        btns5.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Binary files (*.bin)", new String[]{"*.bin"});
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("./saves"));
            try {
                String nome = fileChooser.showOpenDialog((Window) null).getName();
                if (nome != null) {
                    sistemaManager.carrega(nome);
                } else {
                    ToastMessage.show(getScene().getWindow(), "Erro ao carregar o ficheiro");
                }
            }catch(Exception e) {
                ToastMessage.show(getScene().getWindow(), "Erro ao carregar o ficheiro");
            }

        });
        btns6.setOnAction( event -> {
            if (!sistemaManager.fecharState())
                ToastMessage.show(getScene().getWindow(),"This phase cannot be closed yet|");
            else
                sistemaManager.change(SistemState.CONFIGURATION_STATE_LOCK);
        });
        btns7.setOnAction( event -> {
            sistemaManager.up();
        });
        btns8.setOnAction( event -> {
            Platform.exit();
        });
    }

    private void update() {
        if (sistemaManager.getState() != SistemState.CONFIGURATION_STATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        lbAlunos.setText("Students: " + sistemaManager.getStudents().size());
        lbDocentes.setText("Teachers: " + sistemaManager.getTeachers().size());
        lbPropostas.setText("Proposals: " + sistemaManager.getProposals().size());
    }
}

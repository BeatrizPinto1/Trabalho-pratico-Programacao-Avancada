package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Window;
import pt.isec.pa.apoio_poe.model.SistemaManager;
import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Students;
import pt.isec.pa.apoio_poe.model.data.Teachers;
import pt.isec.pa.apoio_poe.model.fsm.SistemState;
import pt.isec.pa.apoio_poe.ui.gui.resources.util.ToastMessage;

import java.io.File;
import java.util.*;

public class ConfigurationLOCKUI extends BorderPane {
    SistemaManager sistemaManager;
    Button btns1, btns2, btns3, btns4, btns5;
    Label lbEstado, lbAlunos, lbDocentes, lbPropostas;

    public ConfigurationLOCKUI(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lbEstado = new Label("Configuration State LOCK");
        lbEstado.setPadding(new Insets(15));
        setAlignment(lbEstado, Pos.TOP_CENTER);

        lbEstado.setTranslateY(80);
        this.setTop(lbEstado);

        btns1 = new Button("Consut Students");
        btns1.setMinWidth(250);
        btns2 = new Button("Consult Teachers");
        btns2.setMinWidth(250);
        btns3 = new Button("Consult Proposals");
        btns3.setMinWidth(250);
        btns4 = new Button("Next Phase");
        btns4.setMinWidth(250);
        btns5 = new Button("Quit");
        btns5.setMinWidth(250);

        VBox vBox = new VBox(btns1, btns2, btns3, btns4, btns5);
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
            final Popup popup = new Popup();
            popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece
            //popup.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT);

            TableView tableView = new TableView();

            TableColumn<Students, Long> column1 = new TableColumn<>("Student ID");
            TableColumn<Students, String> column2 = new TableColumn<>("Name");
            TableColumn<Students, String> column3 = new TableColumn<>("Email");
            TableColumn<Students, String> column4 = new TableColumn<>("Curse");
            TableColumn<Students, String> column5 = new TableColumn<>("Branch");
            TableColumn<Students, Double> column6 = new TableColumn<>("Classification");
            TableColumn<Students, Boolean> column7 = new TableColumn<>("Posibility of internships");

            tableView.getColumns().addAll(column1, column2, column3, column4, column5, column6,column7);

            ObservableList<Students> list = FXCollections.observableArrayList(
                    sistemaManager.getStudents()
            );

            column1.setCellValueFactory(new PropertyValueFactory<>("nrEstudante"));
            column2.setCellValueFactory(new PropertyValueFactory<>("nome"));
            column3.setCellValueFactory(new PropertyValueFactory<>("email"));
            column4.setCellValueFactory(new PropertyValueFactory<>("curso"));
            column5.setCellValueFactory(new PropertyValueFactory<>("ramo"));
            column6.setCellValueFactory(new PropertyValueFactory<>("classificacao"));
            column7.setCellValueFactory(new PropertyValueFactory<>("acedeEstagios"));

            tableView.setItems(list);

            BorderPane pane = new BorderPane(tableView);
            pane.setPadding(new Insets(10));
            pane.setBackground(new Background(
                    new BackgroundFill(Color.WHITESMOKE,
                            new CornerRadii(5),Insets.EMPTY)) //cantos arredondados
            );
            popup.getContent().add(pane);
            popup.show(getScene().getWindow());
            //popup.show(getScene().getWindow(),x+w/2-tableView.getWidth()/2.0*tableView.getWidth(),y+0.80*h); //mostrar popup
            Timer timer = new Timer(true);  //crio um timer
            TimerTask task = new TimerTask() { //esta tarefa vai fazer o hide
                @Override
                public void run() {
                    Platform.runLater(()->{popup.hide();}); //platform.runLater para quando temos threads e queremos configurar sem ser a principal
                }
            };
            timer.schedule(task,30000); //desaparece em 30 segundos
        });
        btns2.setOnAction( event -> {
            final Popup popup = new Popup();
            popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

            TableView tableView = new TableView();

            TableColumn<Teachers, String> column1 = new TableColumn<>("Name");
            TableColumn<Teachers, String> column2 = new TableColumn<>("Email");

            tableView.getColumns().addAll(column1, column2);

            ObservableList<Teachers> list = FXCollections.observableArrayList(
                    sistemaManager.getTeachers()
            );

            column1.setCellValueFactory(new PropertyValueFactory<>("nome"));
            column2.setCellValueFactory(new PropertyValueFactory<>("email"));

            tableView.setItems(list);

            BorderPane pane = new BorderPane(tableView);
            pane.setPadding(new Insets(10));
            pane.setBackground(new Background(
                    new BackgroundFill(Color.WHITESMOKE,
                            new CornerRadii(5),Insets.EMPTY)) //cantos arredondados
            );
            popup.getContent().add(pane);
            popup.show(getScene().getWindow());
            //popup.show(getScene().getWindow(),x+w/2-tableView.getWidth()/2.0*tableView.getWidth(),y+0.80*h); //mostrar popup
            Timer timer = new Timer(true);  //crio um timer
            TimerTask task = new TimerTask() { //esta tarefa vai fazer o hide
                @Override
                public void run() {
                    Platform.runLater(()->{popup.hide();}); //platform.runLater para quando temos threads e queremos configurar sem ser a principal
                }
            };
            timer.schedule(task,30000); //desaparece em 30 segundos
        });
        btns3.setOnAction( event -> {
            List<String> choices = new ArrayList<>();
            choices.add("Stage");
            choices.add("Project");
            choices.add("Internship/self-proposed project");

            ChoiceDialog<String> dialog = new ChoiceDialog<>("Stage", choices);
            dialog.setTitle("Proposal Management");
            dialog.setHeaderText("Choice the type of proposal");
            dialog.setContentText("Option:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            // The Java 8 way to get the response value (with lambda expression).
            //result.ifPresent(letter -> System.out.println("Your choice: " + letter));

            //------------------------------- // ----------------------------------//

            if(result.isPresent()) { //result.get()

                final Popup popup = new Popup();
                popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

                TableView tableView = new TableView();

                switch(result.get()) {
                    case "Stage" -> {
                        TableColumn<Proposals, String> column1 = new TableColumn<>("Identification code");
                        TableColumn<Proposals, String> column2 = new TableColumn<>("Destination area");
                        TableColumn<Proposals, String> column3 = new TableColumn<>("Title");
                        TableColumn<Proposals, String> column4 = new TableColumn<>("Host identity");
                        TableColumn<Proposals, Long> column5 = new TableColumn<>("Student ID");

                        tableView.getColumns().addAll(column1, column2, column3, column4, column5);

                        ObservableList<Proposals> list = FXCollections.observableArrayList(
                                sistemaManager.getTipoPropostas(1)
                        );

                        column1.setCellValueFactory(new PropertyValueFactory<>("codIdentificacao"));
                        column2.setCellValueFactory(new PropertyValueFactory<>("areaDestino"));
                        column3.setCellValueFactory(new PropertyValueFactory<>("titulo"));
                        column4.setCellValueFactory(new PropertyValueFactory<>("identidadeAcolhimento"));
                        column5.setCellValueFactory(new PropertyValueFactory<>("nrAluno"));

                        tableView.setItems(list);
                    }
                    case "Project" -> {
                        TableColumn<Proposals, String> column1 = new TableColumn<>("Identification code");
                        TableColumn<Proposals, String> column2 = new TableColumn<>("Destination area");
                        TableColumn<Proposals, String> column3 = new TableColumn<>("Title");
                        TableColumn<Proposals, String> column4 = new TableColumn<>("Teacher's email");
                        TableColumn<Proposals, Long> column5 = new TableColumn<>("Student ID");

                        tableView.getColumns().addAll(column1, column2, column3, column4, column5);

                        ObservableList<Proposals> list = FXCollections.observableArrayList(
                                sistemaManager.getTipoPropostas(2)
                        );

                        column1.setCellValueFactory(new PropertyValueFactory<>("codIdentificacao"));
                        column2.setCellValueFactory(new PropertyValueFactory<>("areaDestino"));
                        column3.setCellValueFactory(new PropertyValueFactory<>("titulo"));
                        column4.setCellValueFactory(new PropertyValueFactory<>("emailDocente"));
                        column5.setCellValueFactory(new PropertyValueFactory<>("nrAluno"));

                        tableView.setItems(list);
                    }
                    case "Internship/self-proposed project" -> {
                        TableColumn<Proposals, String> column1 = new TableColumn<>("Identification code");
                        TableColumn<Proposals, String> column2 = new TableColumn<>("Title");
                        TableColumn<Proposals, Long> column3 = new TableColumn<>("Student ID");

                        tableView.getColumns().addAll(column1, column2, column3);

                        ObservableList<Proposals> list = FXCollections.observableArrayList(
                                sistemaManager.getTipoPropostas(3)
                        );

                        column1.setCellValueFactory(new PropertyValueFactory<>("codIdentificacao"));
                        column2.setCellValueFactory(new PropertyValueFactory<>("titulo"));
                        column3.setCellValueFactory(new PropertyValueFactory<>("nrAluno"));

                        tableView.setItems(list);
                    }
                }

                BorderPane pane = new BorderPane(tableView);
                pane.setPadding(new Insets(10));
                pane.setBackground(new Background(
                        new BackgroundFill(Color.WHITESMOKE,
                                new CornerRadii(5), Insets.EMPTY)) //cantos arredondados
                );
                popup.getContent().add(pane);
                popup.show(getScene().getWindow());
                //popup.show(getScene().getWindow(),x+w/2-tableView.getWidth()/2.0*tableView.getWidth(),y+0.80*h); //mostrar popup
                Timer timer = new Timer(true);  //crio um timer
                TimerTask task = new TimerTask() { //esta tarefa vai fazer o hide
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            popup.hide();
                        }); //platform.runLater para quando temos threads e queremos configurar sem ser a principal
                    }
                };
                timer.schedule(task, 30000); //desaparece em 30 segundos
            }
        });
        btns4.setOnAction(actionEvent -> {
            sistemaManager.up();
        });
        btns5.setOnAction(actionEvent -> {
            Platform.exit();
        });
    }

    private void update() {
        if (sistemaManager.getState() != SistemState.CONFIGURATION_STATE_LOCK) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        lbAlunos.setText("Students: " + sistemaManager.getStudents().size());
        lbDocentes.setText("Teachers: " + sistemaManager.getTeachers().size());
        lbPropostas.setText("Proposals: " + sistemaManager.getProposals().size());

    }

}

package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Window;
import pt.isec.pa.apoio_poe.model.SistemaManager;
import pt.isec.pa.apoio_poe.model.fsm.SistemState;
import pt.isec.pa.apoio_poe.ui.gui.resources.util.ToastMessage;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AssignmentProposalsUI extends BorderPane {
    SistemaManager sistemaManager;
    Button btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8, btns9, btns10, btns11, btns12, btns13, btns14, btns15, btns16, btns17, btns18;
    Label lbEstado, lbEstado2, lb1, lb2, lb3, lb4;
    VBox vBox, vBox2;

    public AssignmentProposalsUI(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lbEstado = new Label("Assignment Proposals");
        lbEstado.setPadding(new Insets(15));
        setTop(lbEstado);
        setAlignment(lbEstado, Pos.TOP_CENTER);

        btns1 = new Button("Auto assignment of SP or TP to AS");
        btns1.setMinWidth(380);
        btns2 = new Button("Auto assignment of proposals to SWA");
        btns2.setMinWidth(380);
        btns3 = new Button("Manual assignment of proposals to SWA");
        btns3.setMinWidth(380);
        btns4 = new Button("Manually remove of assignments");
        btns4.setMinWidth(380);
        btns5 = new Button("Undo");
        btns5.setMinWidth(380);
        btns6 = new Button("Redo");
        btns6.setMinWidth(380);
        btns7 = new Button("Export data");
        btns7.setMinWidth(380);
        btns8 = new Button("Students list");
        btns8.setMinWidth(380);
        btns9 = new Button("Proposals list");
        btns9.setMinWidth(380);
        btns10 = new Button("Save");
        btns10.setMinWidth(380);
        btns11 = new Button("Load");
        btns11.setMinWidth(380);
        btns12 = new Button("Close Phase");
        btns12.setMinWidth(380);
        btns13 = new Button("Next Phase");
        btns13.setMinWidth(380);
        btns14 = new Button("Previous Phase");
        btns14.setMinWidth(380);
        btns15 = new Button("Quit");
        btns15.setMinWidth(380);
        vBox = new VBox(btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8, btns9,btns10, btns11, btns12, btns13, btns14, btns15);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        this.setCenter(vBox);

        lb1 = new Label("Students: ");
        lb1.setId("labeldraw");
        lb2  = new Label("W\\ proposal: ");
        lb2.setId("labeldraw");
        lb3 = new Label("Proposals: ");
        lb3.setId("labeldraw");
        lb4  = new Label("W\\ student: ");
        lb4.setId("labeldraw");

        HBox hBox2 = new HBox(lb1, lb2, lb3, lb4);
        hBox2.setAlignment(Pos.CENTER);
        this.setBottom(hBox2);
        hBox2.setSpacing(30);
        hBox2.setTranslateY(-10);

        //======================== // ================================//

        lbEstado2 = new Label("There was a tie, resolve it manually");
        lbEstado2.setId("labeldraw");
        lbEstado2.setPadding(new Insets(15));
        lbEstado2.setTextFill(Color.DARKRED);
        //setTop(lbEstado);
        //setAlignment(lbEstado, Pos.TOP_CENTER);

        btns16 = new Button("Assign proposal to the student");
        btns16.setMinWidth(400);
        btns17 = new Button("Consult the students involved");
        btns17.setMinWidth(400);
        btns18 = new Button("Applications of the students involved");
        btns18.setMinWidth(400);

        vBox2 = new VBox(lbEstado2, btns16, btns17, btns18);
        vBox2.setAlignment(Pos.CENTER);
        vBox2.setSpacing(10);

        //this.setCenter(vBox2);
        //vBox2.setVisible(false);
    }

    private void registerHandlers() {
        sistemaManager.addPropertyChangeListener(evt -> { update(); });
        btns1.setOnAction( event -> {
            List<String> choices = new ArrayList<>();
            choices.add("Automatic assignment of self-proposals");
            choices.add("Automatic assignment of teachers proposals with associated student");

            ChoiceDialog<String> dialog = new ChoiceDialog<>("Automatic assignment of self-proposals", choices);
            dialog.setTitle("Assignment Proposals");
            dialog.setHeaderText("Auto assignment");
            dialog.setContentText("Option:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {
                switch (result.get()) {
                    case "Automatic assignment of self-proposals" -> sistemaManager.automaticSelfProprosals();
                    case "Automatic assignment of teachers proposals with associated student" -> sistemaManager.automaticteachersProprosals();
                }

            }

            // The Java 8 way to get the response value (with lambda expression).
            //result.ifPresent(letter -> System.out.println("Your choice: " + letter));
        });
        btns2.setOnAction( event -> {
            if(!sistemaManager.automaticStudentsWithoutAssignmentsProposals()) {
               // DrawStateUI(); //RETORNAR PROPOSTA

                lbEstado.setTranslateY(100);
                //this.setTop(lbEstado);

                this.setCenter(vBox2);
                /*AtomicBoolean continuar = new AtomicBoolean(false);
                do {
                    continuar.set(false);
                    Dialog<String> dialog = new Dialog<>();
                    dialog.setTitle("Auto assignment of proposals");
                    dialog.setHeaderText("There was a tie, resolve it manually!");

                    // Set the button types.
                    ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20, 150, 10, 10));

                    CheckBox checkBox1 = new CheckBox();
                    CheckBox checkBox2 = new CheckBox();
                    CheckBox checkBox3 = new CheckBox();

                    grid.add(new Label("Assign proposal to the students"), 0, 1);
                    grid.add(checkBox1, 1, 1);
                    grid.add(new Label("Consult the students involved"), 0, 2);
                    grid.add(checkBox2, 1, 2);
                    grid.add(new Label("Consult the applications of the students involved"), 0, 3);
                    grid.add(checkBox3, 1, 3);

                    // Enable/Disable login button depending on whether a username was entered.
                    Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
                    okButton.setDisable(true);

                    checkBox1.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        okButton.setDisable(!checkBox1.isSelected());
                        checkBox2.setDisable(newValue);
                        checkBox3.setDisable(newValue);
                    });
                    checkBox2.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        okButton.setDisable(!checkBox2.isSelected());
                        checkBox1.setDisable(newValue);
                        checkBox3.setDisable(newValue);
                    });
                    checkBox3.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        okButton.setDisable(!checkBox3.isSelected());
                        checkBox1.setDisable(newValue);
                        checkBox2.setDisable(newValue);
                    });

                    dialog.getDialogPane().setContent(grid);

                    // Request focus on the username field by default.
                    Platform.runLater(() -> checkBox1.requestFocus());

                    // Convert the result to a username-password-pair when the login button is clicked.
                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == okButtonType) {

                            if (checkBox1.isSelected()) {

                                String nrAluno1 = String.valueOf(sistemaManager.getStudentsDraw().get(0).getNrEstudante());
                                String nrAluno2 = String.valueOf(sistemaManager.getStudentsDraw().get(1).getNrEstudante());
                                List<String> choices = new ArrayList<>();
                                choices.add(nrAluno1);
                                choices.add(nrAluno2);

                                ChoiceDialog<String> dialog2 = new ChoiceDialog<>(nrAluno1, choices);
                                dialog2.setTitle("Automatic assignment of proposals");
                                dialog2.setHeaderText("The same proposal came to both students, which one should take it");
                                dialog2.setContentText("Option:");

                                // Traditional way to get the response value.
                                Optional<String> result = dialog2.showAndWait();

                                if (result.isPresent()) { //result.get()

                                    if (result.get().equals(nrAluno1)) {
                                        if(!sistemaManager.Draw(1))
                                            continuar.set(true);
                                    }
                                    else {
                                        if(!sistemaManager.Draw(2))
                                            continuar.set(true);
                                    }
                                }
                            }
                            if (checkBox2.isSelected()) {

                                final Popup popup = new Popup();
                                popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

                                ListView listView = new ListView();
                                listView.getItems().addAll(sistemaManager.getStudentsDraw());

                                BorderPane pane = new BorderPane(listView);
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
                                continuar.set(true);
                            }
                            if (checkBox3.isSelected()) {

                                final Popup popup = new Popup();
                                popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

                                ListView listView = new ListView();
                                listView.getItems().addAll(sistemaManager.consultApplicationsStudentsInvolved());

                                BorderPane pane = new BorderPane(listView);
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
                                continuar.set(true);
                            }
                        }

                        return null;
                    });

                    dialog.showAndWait();

                }while (continuar.get());*/

            }
        });
        btns3.setOnAction( event -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Assignment Proposals");
            dialog.setHeaderText("Manual assignment of proposals");

            // Set the button types.
            ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);


            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nrEstudante = new TextField();
            nrEstudante.setPromptText("Student ID");
            TextField codProposta = new TextField();
            codProposta.setPromptText("Student ID");

            grid.add(new Label("Student ID:"), 0, 0);
            grid.add(nrEstudante, 1, 0);
            grid.add(new Label("Proposal code:"), 0, 1);
            grid.add(codProposta, 1, 1);

            // Enable/Disable login button depending on whether a username was entered.
            Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
            okButton.setDisable(true);

            nrEstudante.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!codProposta.textProperty().getValue().isEmpty())
                    okButton.setDisable(newValue.trim().isEmpty());
            });
            codProposta.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!nrEstudante.textProperty().getValue().isEmpty())
                    okButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> nrEstudante.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {

                    Long nrAluno;

                    Scanner scanner = new Scanner(nrEstudante.textProperty().getValue());
                    if (scanner.hasNextLong())
                        nrAluno = scanner.nextLong();
                    else {
                        ToastMessage.show(getScene().getWindow(), "Insert a correct Student ID!");
                        return null;
                    }

                    if (!sistemaManager.setMapStudentProposal(nrAluno, codProposta.textProperty().getValue()).isEmpty()) {
                        ToastMessage.show(getScene().getWindow(), sistemaManager.setMapStudentProposal(nrAluno, codProposta.textProperty().getValue()));
                        return null;
                    }
                }
                return null;
            });

            dialog.showAndWait();
        });
        btns4.setOnAction( event -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Assignment Proposals");
            dialog.setHeaderText("Manually remove of assignments");

            // Set the button types.
            ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);


            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            CheckBox remove1 = new CheckBox();
            CheckBox remove2 = new CheckBox();

            grid.add(new Label("Remove one assignment"), 0, 0);
            grid.add(remove1, 1, 0);
            grid.add(new Label("Remove all assignments"), 2, 0);
            grid.add(remove2, 3, 0);

            // Enable/Disable login button depending on whether a username was entered.
            Node editButton = dialog.getDialogPane().lookupButton(okButtonType);
            editButton.setDisable(true);

            remove1.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    editButton.setDisable(!remove1.isSelected());
                remove2.setDisable(newValue);
            });
            remove2.selectedProperty().addListener((observable, oldValue, newValue) -> {
                editButton.setDisable(!remove2.isSelected());
                remove1.setDisable(newValue);
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            //Platform.runLater(() -> remove1.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {

                    if (remove1.isSelected()) {

                        TextInputDialog dialog2 = new TextInputDialog();
                        dialog2.setTitle("Manually remove of assignments");
                        dialog2.setHeaderText("Insert the student ID");
                        dialog2.setContentText("Student ID");

                        // Traditional way to get the response value.
                        Optional<String> result = dialog2.showAndWait();

                        // The Java 8 way to get the response value (with lambda expression).
                        //result.ifPresent(letter -> System.out.println("Your choice: " + letter));

                        if (result.isPresent()) {

                            Long nrAluno;

                            Scanner scanner = new Scanner(result.get());
                            if (scanner.hasNextLong())
                                nrAluno = scanner.nextLong();
                            else {
                                ToastMessage.show(getScene().getWindow(),"Insert a correct Student ID!");
                                return null;
                            }

                            if(!sistemaManager.removeOneAssignment(nrAluno).isEmpty()) {
                                ToastMessage.show(getScene().getWindow(),sistemaManager.removeOneAssignment(nrAluno));
                                return null;
                            }

                        }

                    }

                    if (remove2.isSelected())
                        sistemaManager.removeAssignments();
                }
                return null;
            });

            dialog.showAndWait();
        });
        btns5.setOnAction( event -> {
            sistemaManager.undo();
        });
        btns6.setOnAction( event -> {
            sistemaManager.redo();
        });
        btns7.setOnAction( event -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setContentText("Nome: ");
            tid.setHeaderText("Que nome deseja dar ao ficheiro: ");
            tid.setTitle("Nome do ficheiro para exportar");
            tid.showAndWait();
            String nome = tid.getResult();

            try {
                sistemaManager.exportToFile(nome, 5);
            } catch (Exception e) {
                ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
            }
        });
        btns8.setOnAction( event -> {
            List<String> choices = new ArrayList<>();
            choices.add("With self-proposal");
            choices.add("With application already registered");
            choices.add("With proposal assigned");
            choices.add("Without proposal assigned");

            ChoiceDialog<String> dialog = new ChoiceDialog<>("With self-proposal", choices);
            dialog.setTitle("Assignment Proposals");
            dialog.setHeaderText("Students lists");
            dialog.setContentText("Option:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {

                final Popup popup = new Popup();
                popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

                ListView listView = new ListView();
                switch (result.get()) {
                    case "With self-proposal" -> listView.getItems().addAll(sistemaManager.consultSelfProposedStudents());
                    case "With application already registered" -> listView.getItems().addAll(sistemaManager.CSWithAlreadyRegisteredApplication());
                    case "With proposal assigned" -> listView.getItems().addAll(sistemaManager.CSwithProposalsAssigned());
                    case "Without proposal assigned" -> listView.getItems().addAll(sistemaManager.CSwithoutProposalsAssigned());
                }

                BorderPane pane = new BorderPane(listView);
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
        btns9.setOnAction( event -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Assignment Proposals");
            dialog.setHeaderText("Proposals list");

            // Set the button types.
            ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            CheckBox checkBox1 = new CheckBox();
            CheckBox checkBox2 = new CheckBox();
            CheckBox checkBox3 = new CheckBox();
            CheckBox checkBox4 = new CheckBox();

            grid.add(new Label("Student self-proposals"), 0, 1);
            grid.add(checkBox1, 1, 1);
            grid.add(new Label("Teachers proposals"), 0, 2);
            grid.add(checkBox2, 1, 2);
            grid.add(new Label("Available proposals"), 0, 3);
            grid.add(checkBox3, 1, 3);
            grid.add(new Label("Assigned proposals"), 0, 4);
            grid.add(checkBox4, 1, 4);

            // Enable/Disable login button depending on whether a username was entered.
            Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
            okButton.setDisable(true);

            checkBox1.selectedProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(!checkBox1.isSelected());
            });
            checkBox2.selectedProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(!checkBox2.isSelected());
            });
            checkBox3.selectedProperty().addListener((observable, oldValue, newValue) -> {
                okButton.setDisable(!checkBox3.isSelected());
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> checkBox1.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {

                    final Popup popup = new Popup();
                    popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

                    ListView listView = new ListView();

                    if (checkBox1.isSelected()) {
                        listView.getItems().add(sistemaManager.studentsSelfProposals());
                    }
                    if (checkBox2.isSelected()) {
                        listView.getItems().add(sistemaManager.teachersProposals());
                    }
                    if (checkBox3.isSelected()) {
                        listView.getItems().add(sistemaManager.availableProposals());
                    }
                    if (checkBox4.isSelected()) {
                        listView.getItems().add(sistemaManager.assignedProposals());
                    }

                    BorderPane pane = new BorderPane(listView);
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

                return null;
            });

            dialog.showAndWait();
        });
        btns10.setOnAction( event -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setContentText("Nome: ");
            tid.setHeaderText("Que nome deseja dar ao ficheiro: ");
            tid.setTitle("Nome do ficheiro para guardar");
            tid.showAndWait();
            String nome = tid.getResult();

            sistemaManager.guarda(nome);
        });
        btns11.setOnAction( event -> {
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
        btns12.setOnAction( event -> {
            if (!sistemaManager.fecharState())
                ToastMessage.show(getScene().getWindow(),"This phase cannot be closed yet|");
            else
                sistemaManager.change(SistemState.ASSIGNMENT_PROPOSALS_LOCK);
        });
        btns13.setOnAction( event -> {
            sistemaManager.up();
        });
        btns14.setOnAction( event -> {
            sistemaManager.down();
        });
        btns15.setOnAction( event -> {
            Platform.exit();
        });
        btns16.setOnAction( event -> {

            String nrAluno1 = String.valueOf(sistemaManager.getStudentsDraw().get(0).getNrEstudante());
            String nrAluno2 = String.valueOf(sistemaManager.getStudentsDraw().get(1).getNrEstudante());
            List<String> choices = new ArrayList<>();
            choices.add(nrAluno1);
            choices.add(nrAluno2);

            ChoiceDialog<String> dialog2 = new ChoiceDialog<>(nrAluno1, choices);
            dialog2.setTitle("Automatic assignment of proposals");
            dialog2.setHeaderText("The same proposal came to both students, which one should take it");
            dialog2.setContentText("Option:");

            // Traditional way to get the response value.
            Optional<String> result = dialog2.showAndWait();

            if (result.isPresent()) { //result.get()

                if (result.get().equals(nrAluno1)) {
                    if(sistemaManager.Draw(1)) {
                        lbEstado.setTranslateY(10);

                        this.setCenter(vBox);
                    }
                }
                else {
                    if(sistemaManager.Draw(2)) {
                        lbEstado.setTranslateY(10);

                        this.setCenter(vBox);
                    }
                }
            }

        });
        btns17.setOnAction( event -> {

            final Popup popup = new Popup();
            popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

            ListView listView = new ListView();
            listView.getItems().addAll(sistemaManager.getStudentsDraw());

            BorderPane pane = new BorderPane(listView);
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

        });
        btns18.setOnAction( event -> {

            final Popup popup = new Popup();
            popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

            ListView listView = new ListView();
            listView.getItems().addAll(sistemaManager.consultApplicationsStudentsInvolved());

            BorderPane pane = new BorderPane(listView);
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

        });
    }

    private void update() {
        if (sistemaManager.getState() != SistemState.ASSIGNMENT_PROPOSALS) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        lb1.setText("Students: " + sistemaManager.getStudents().size());
        lb2.setText("W\\ proposal: " + sistemaManager.getMapStudentProposal().keySet().size());
        lb3.setText("Proposals: " + sistemaManager.getProposals().size());
        lb4.setText("W\\ student: " + sistemaManager.getMapStudentProposal().values().size());

    }

}

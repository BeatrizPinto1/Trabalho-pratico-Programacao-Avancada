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

public class AssignmentAdvisersUI extends BorderPane {
    SistemaManager sistemaManager;
    Button btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8, btns9, btns10, btns11;
    Label lbEstado, lb1, lb2, lb3, lb4;

    public AssignmentAdvisersUI(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lbEstado = new Label("Assignment Advisers");
        lbEstado.setPadding(new Insets(15));
        setAlignment(lbEstado, Pos.TOP_CENTER);

        lbEstado.setTranslateY(60);
        this.setTop(lbEstado);

        btns1 = new Button("Auto association of teachers");
        btns1.setMinWidth(340);
        btns2 = new Button("Manual association of teachers");
        btns2.setMinWidth(340);
        btns3 = new Button("Undo");
        btns3.setMinWidth(340);
        btns4 = new Button("Redo");
        btns4.setMinWidth(340);
        btns5 = new Button("Export data");
        btns5.setMinWidth(340);
        btns6 = new Button("Data about assignment of advisors");
        btns6.setMinWidth(340);
        btns7 = new Button("Save");
        btns7.setMinWidth(340);
        btns8 = new Button("Load");
        btns8.setMinWidth(340);
        btns9 = new Button("Close Phase");
        btns9.setMinWidth(340);
        btns10 = new Button("Previous Phase");
        btns10.setMinWidth(340);
        btns11 = new Button("Quit");
        btns11.setMinWidth(340);

        VBox vBox = new VBox(btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8, btns9,btns10, btns11);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        this.setCenter(vBox);

        lb1 = new Label("Teachers: ");
        lb1.setId("labeldraw");
        lb2  = new Label("W\\ proposal: ");
        lb2.setId("labeldraw");
        lb3 = new Label("Proposals: ");
        lb3.setId("labeldraw");
        lb4  = new Label("W\\ teacher: ");
        lb4.setId("labeldraw");

        HBox hBox2 = new HBox(lb1, lb2, lb3, lb4);
        hBox2.setAlignment(Pos.CENTER);
        this.setBottom(hBox2);
        hBox2.setSpacing(30);
        hBox2.setTranslateY(-20);
    }

    private void registerHandlers() {
        sistemaManager.addPropertyChangeListener(evt -> { update(); });
        btns1.setOnAction( event -> {
            sistemaManager.automaticAssociationOfTeachersProposingProjects();
        });
        btns2.setOnAction( event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Assignment Advisers");
            alert.setHeaderText("Manual association of teachers");
            alert.setContentText("Choose your option");

            ButtonType buttonTypeOne = new ButtonType("Insert");
            ButtonType buttonTypeTwo = new ButtonType("Consult");
            ButtonType buttonTypeThree = new ButtonType("Edit");
            ButtonType buttonTypeFour = new ButtonType("Remove");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeFour, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == buttonTypeOne){

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Applications Management");
                dialog.setHeaderText("Insert Application");

                // Set the button types.
                ButtonType insertButtonType = new ButtonType("Insert", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(insertButtonType, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField emailDocente = new TextField();
                emailDocente.setPromptText("Teacher email");
                TextField nrAluno = new TextField();
                nrAluno.setPromptText("Student ID");

                grid.add(new Label("Teacher email:"), 0, 1); //coluna 0 | linha 1
                grid.add(emailDocente, 1, 1);
                grid.add(new Label("Student Id:"), 0, 2); //coluna 0 | linha 1
                grid.add(nrAluno, 1, 2);

                // Enable/Disable login button depending on whether a username was entered.
                Node insertButton = dialog.getDialogPane().lookupButton(insertButtonType);
                insertButton.setDisable(true);

                // Do some validation (using the Java 8 lambda syntax).
                emailDocente.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(!nrAluno.textProperty().getValue().isEmpty())
                        insertButton.setDisable(newValue.trim().isEmpty());
                });
                nrAluno.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(!emailDocente.textProperty().getValue().isEmpty())
                        insertButton.setDisable(newValue.trim().isEmpty());
                });

                dialog.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(() -> emailDocente.requestFocus());

                // Convert the result to a username-password-pair when the login button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == insertButtonType) {

                        Long nrEstudante;

                        Scanner scanner = new Scanner(nrAluno.textProperty().getValue());
                        if (scanner.hasNextLong())
                            nrEstudante = scanner.nextLong();
                        else {
                            ToastMessage.show(getScene().getWindow(), "Insert a correct Student ID!");
                            return null;
                        }

                        if(!sistemaManager.setMapProposalTeacher(emailDocente.textProperty().getValue(), nrEstudante).isEmpty()) {
                            ToastMessage.show(getScene().getWindow(),sistemaManager.setMapProposalTeacher(emailDocente.textProperty().getValue(), nrEstudante));
                            return null;
                        }
                    }
                    return null;
                });
                dialog.showAndWait();

            } else if (result.get() == buttonTypeTwo) {

                final Popup popup = new Popup();
                popup.setAutoHide(true);

                ListView listView = new ListView();
                listView.getItems().addAll(sistemaManager.getProposalTeacher());

                BorderPane pane = new BorderPane(listView);
                pane.setPadding(new Insets(10));
                pane.setBackground(new Background(
                        new BackgroundFill(Color.WHITESMOKE,
                                new CornerRadii(5),Insets.EMPTY)) //cantos arredondados
                );
                popup.getContent().add(pane);
                popup.show(getScene().getWindow());
                Timer timer = new Timer(true);  //crio um timer
                TimerTask task = new TimerTask() { //esta tarefa vai fazer o hide
                    @Override
                    public void run() {
                        Platform.runLater(()->{popup.hide();}); //platform.runLater para quando temos threads e queremos configurar sem ser a principal
                    }
                };
                timer.schedule(task,30000); //desaparece em 30 segundos

            } else if (result.get() == buttonTypeThree) {

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Manual association of teachers (Edit)");
                dialog.setHeaderText("Insert the teacher email and the new student ID");

                // Set the button types.
                ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField emailDocente = new TextField();
                emailDocente.setPromptText("Teacher email");
                TextField nrAluno = new TextField();
                nrAluno.setPromptText("Student ID");

                grid.add(new Label("Teacher email:"), 0, 1); //coluna 0 | linha 1
                grid.add(emailDocente, 1, 1);
                grid.add(new Label("New student Id:"), 0, 2); //coluna 0 | linha 1
                grid.add(nrAluno, 1, 2);

                // Enable/Disable login button depending on whether a username was entered.
                Node insertButton = dialog.getDialogPane().lookupButton(editButtonType);
                insertButton.setDisable(true);

                // Do some validation (using the Java 8 lambda syntax).
                emailDocente.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(!nrAluno.textProperty().getValue().isEmpty())
                        insertButton.setDisable(newValue.trim().isEmpty());
                });
                nrAluno.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(!emailDocente.textProperty().getValue().isEmpty())
                        insertButton.setDisable(newValue.trim().isEmpty());
                });

                dialog.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(() -> emailDocente.requestFocus());

                // Convert the result to a username-password-pair when the login button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == editButtonType) {

                        Long nrEstudante;

                        Scanner scanner = new Scanner(nrAluno.textProperty().getValue());
                        if (scanner.hasNextLong())
                            nrEstudante = scanner.nextLong();
                        else {
                            ToastMessage.show(getScene().getWindow(), "Insert a correct Student ID!");
                            return null;
                        }

                        if(!sistemaManager.editTeachersAssociation(emailDocente.textProperty().getValue(), nrEstudante).isEmpty()) {
                            ToastMessage.show(getScene().getWindow(),sistemaManager.editTeachersAssociation(emailDocente.textProperty().getValue(), nrEstudante));
                            return null;
                        }
                    }
                    return null;
                });
                dialog.showAndWait();

            } else if (result.get() == buttonTypeFour) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Remove association of teacher");
                dialog.setHeaderText("Insert the teacher email");
                dialog.setContentText("Teacher email");

                // Traditional way to get the response value.
                Optional<String> result2 = dialog.showAndWait();

                if(result2.isPresent()) {
                    if(!sistemaManager.removeTeachersAssociation(result2.get()).isEmpty()) {
                        ToastMessage.show(getScene().getWindow(),sistemaManager.removeTeachersAssociation(result2.get()));
                        return;
                    }
                }
            }

        });
        btns3.setOnAction( event -> {
            sistemaManager.undo();
        });
        btns4.setOnAction( event -> {
            sistemaManager.redo();
        });
        btns5.setOnAction( event -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setContentText("Nome: ");
            tid.setHeaderText("Que nome deseja dar ao ficheiro: ");
            tid.setTitle("Nome do ficheiro para exportar");
            tid.showAndWait();
            String nome = tid.getResult();

            try {
                sistemaManager.exportToFile(nome, 6);
            } catch (Exception e) {
                ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
            }
        });
        btns6.setOnAction( event -> {

            List<String> choices = new ArrayList<>();
            choices.add("List of students with an assigned proposal and with an associated advisor");
            choices.add("List of students with an assigned proposal but without an associate supervisor");
            choices.add("Guidance by teacher");

            ChoiceDialog<String> dialog = new ChoiceDialog<>("List of students with an assigned proposal and with an associated advisor", choices);
            dialog.setTitle("Assignment Advisers");
            dialog.setHeaderText("Data about assignment of advisors");
            dialog.setContentText("Option:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {

                final Popup popup = new Popup();
                popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

                ListView listView = new ListView();
                switch (result.get()) {
                    case "List of students with an assigned proposal and with an associated advisor" -> {
                        listView.getItems().addAll(sistemaManager.studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor());
                    }
                    case "List of students with an assigned proposal but without an associate supervisor" -> {
                        listView.getItems().addAll(sistemaManager.studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor());
                    }
                    case "Guidance by teacher" -> {

                        List<String> choices2 = new ArrayList<>();
                        choices2.add("In media");
                        choices2.add("Minimum");
                        choices2.add("Maximum");
                        choices2.add("By teacher specified");

                        ChoiceDialog<String> dialog2 = new ChoiceDialog<>("In media", choices2);
                        dialog2.setTitle("Guidance by teacher");
                        dialog2.setHeaderText("Number of guidance per teacher");
                        dialog2.setContentText("Option:");

                        // Traditional way to get the response value.
                        Optional<String> result2 = dialog2.showAndWait();

                        if(result2.isPresent()) {
                            switch (result2.get()) {
                                case "In media" -> listView.getItems().addAll(sistemaManager.inMedium());
                                case "Minimum" -> listView.getItems().addAll(sistemaManager.inMin());
                                case "Maximum" -> listView.getItems().addAll(sistemaManager.inMin());
                                case "By teacher specified" -> {

                                    TextInputDialog dialog3 = new TextInputDialog();
                                    dialog3.setTitle("Guidance by specified teacher");
                                    dialog3.setHeaderText("Insert the teacher email");
                                    dialog3.setContentText("Teacher email");

                                    // Traditional way to get the response value.
                                    Optional<String> result3 = dialog3.showAndWait();

                                    // The Java 8 way to get the response value (with lambda expression).
                                    //result.ifPresent(letter -> System.out.println("Your choice: " + letter));
                                    if (result3.isPresent())
                                        listView.getItems().addAll(sistemaManager.inTeacher(result3.get()));

                                }
                            }
                        }
                    }
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
        btns7.setOnAction( event -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setContentText("Nome: ");
            tid.setHeaderText("Que nome deseja dar ao ficheiro: ");
            tid.setTitle("Nome do ficheiro para guardar");
            tid.showAndWait();
            String nome = tid.getResult();

            sistemaManager.guarda(nome);
        });
        btns8.setOnAction( event -> {
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
        btns9.setOnAction( event -> {
            sistemaManager.up();
        });
        btns10.setOnAction( event -> {
            sistemaManager.down();
        });
        btns11.setOnAction( event -> {
            Platform.exit();
        });

    }

    private void update() {
        if (sistemaManager.getState() != SistemState.ASSIGNMENT_ADVISERS) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        lb1.setText("Teachers: " + sistemaManager.getTeachers().size());
        lb2.setText("W\\ proposal: " + sistemaManager.getMapProposalTeacher().values().size());
        lb3.setText("Proposals: " + sistemaManager.getProposals().size());
        lb4.setText("W\\ teacher: " + sistemaManager.getMapProposalTeacher().keySet().size());

    }

}

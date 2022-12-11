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
import pt.isec.pa.apoio_poe.model.data.Students;
import pt.isec.pa.apoio_poe.model.data.Teachers;
import pt.isec.pa.apoio_poe.model.fsm.SistemState;
import pt.isec.pa.apoio_poe.ui.gui.resources.util.ToastMessage;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationOptionsUI extends BorderPane {
    SistemaManager sistemaManager;
    Button btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8, btns9;
    Label lbEstado, lb1, lb2, lb3, lb4;

    public ApplicationOptionsUI(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lbEstado = new Label("Application Options");
        lbEstado.setPadding(new Insets(15));
        setAlignment(lbEstado, Pos.TOP_CENTER);

        lbEstado.setTranslateY(80);
        this.setTop(lbEstado);

        btns1 = new Button("Applications Management");
        btns1.setMinWidth(250);
        btns2 = new Button("Students list");
        btns2.setMinWidth(250);
        btns3 = new Button("Proposals list");
        btns3.setMinWidth(250);
        btns4 = new Button("Save");
        btns4.setMinWidth(250);
        btns5 = new Button("Load");
        btns5.setMinWidth(250);
        btns6 = new Button("Close Phase");
        btns6.setMinWidth(250);
        btns7 = new Button("Next Phase");
        btns7.setMinWidth(250);
        btns8 = new Button("Previous Phase");
        btns8.setMinWidth(250);
        btns9 = new Button("Quit");
        btns9.setMinWidth(250);
        VBox vBox = new VBox(btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8, btns9);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        this.setCenter(vBox);

        lb1 = new Label("Students: ");
        lb1.setId("labeldraw");
        lb2  = new Label("W\\ aplication: ");
        lb2.setId("labeldraw");
        lb3 = new Label("Proposals: ");
        lb3.setId("labeldraw");
        lb4  = new Label("W\\ aplication: ");
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Applications Options");
            alert.setHeaderText("Applications Management");
            alert.setContentText("Choose your option");

            ButtonType buttonTypeOne = new ButtonType("Import");
            ButtonType buttonTypeTwo = new ButtonType("Export");
            ButtonType buttonTypeThree = new ButtonType("Insert");
            ButtonType buttonTypeFour = new ButtonType("Consult");
            ButtonType buttonTypeFive = new ButtonType("Edit");
            ButtonType buttonTypeSix = new ButtonType("Remove");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeFour, buttonTypeFive, buttonTypeSix, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", new String[]{"*.csv"});
                fileChooser.getExtensionFilters().add(extFilter);
                fileChooser.setInitialDirectory(new File("./"));

                try {
                    String nome = fileChooser.showOpenDialog((Window) null).getName();
                    if (nome != null) {
                        sistemaManager.setApplications(nome);
                    } else {
                        ToastMessage.show(getScene().getWindow(), "Erro ao carregar o ficheiro");
                    }
                } catch (Exception e) {
                    ToastMessage.show(getScene().getWindow(), "Erro ao carregar o ficheiro");
                }
            } else if (result.get() == buttonTypeTwo) {
                TextInputDialog tid = new TextInputDialog();
                tid.setContentText("Nome: ");
                tid.setHeaderText("Que nome deseja dar ao ficheiro: ");
                tid.setTitle("Nome do ficheiro para exportar");
                tid.showAndWait();
                String nome = tid.getResult();

                try {
                    sistemaManager.exportToFile(nome, 4);
                } catch (Exception e) {
                    ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
                }
            } else if (result.get() == buttonTypeThree) {
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

                TextField nrAluno = new TextField();
                nrAluno.setPromptText("Student ID");
                TextField proposal1 = new TextField();
                proposal1.setPromptText("First proposal code");
                TextField proposal2 = new TextField();
                proposal2.setPromptText("Second proposal code");
                TextField proposal3 = new TextField();
                proposal3.setPromptText("Third proposal code");
                TextField proposal4 = new TextField();
                proposal4.setPromptText("Fourth proposal code");
                TextField proposal5 = new TextField();
                proposal5.setPromptText("Fifth proposal code");
                TextField proposal6 = new TextField();
                proposal6.setPromptText("Sixth proposal code");

                grid.add(new Label("Student Id:"), 0, 1); //coluna 0 | linha 1
                grid.add(nrAluno, 1, 1);
                grid.add(new Label("First proposal code:"), 0, 2);
                grid.add(proposal1, 1, 2);
                grid.add(new Label("Second proposal code:"), 0, 3);
                grid.add(proposal2, 1, 3);
                proposal2.setDisable(true);
                grid.add(new Label("Third proposal code:"), 0, 4);
                grid.add(proposal3, 1, 4);
                proposal3.setDisable(true);
                grid.add(new Label("Fourth proposal code:"), 0, 5);
                grid.add(proposal4, 1, 5);
                proposal4.setDisable(true);
                grid.add(new Label("Fifth proposal code:"), 0, 6);
                grid.add(proposal5, 1, 6);
                proposal5.setDisable(true);
                grid.add(new Label("Sixth proposal code:"), 0, 7);
                grid.add(proposal6, 1, 7);
                proposal6.setDisable(true);

                // Enable/Disable login button depending on whether a username was entered.
                Node insertButton = dialog.getDialogPane().lookupButton(insertButtonType);
                insertButton.setDisable(true);

                // Do some validation (using the Java 8 lambda syntax).
                nrAluno.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(!proposal1.textProperty().getValue().isEmpty()) {
                        insertButton.setDisable(newValue.trim().isEmpty());
                        proposal2.setDisable(newValue.trim().isEmpty());
                        if(proposal2.isDisable()) {
                            proposal3.setDisable(newValue.trim().isEmpty());
                            proposal4.setDisable(newValue.trim().isEmpty());
                            proposal5.setDisable(newValue.trim().isEmpty());
                            proposal6.setDisable(newValue.trim().isEmpty());
                        }
                        if(!proposal2.textProperty().getValue().isEmpty())
                            proposal3.setDisable(newValue.trim().isEmpty());
                        if(!proposal3.textProperty().getValue().isEmpty())
                            proposal4.setDisable(newValue.trim().isEmpty());
                        if(!proposal4.textProperty().getValue().isEmpty())
                            proposal5.setDisable(newValue.trim().isEmpty());
                        if(!proposal5.textProperty().getValue().isEmpty())
                            proposal6.setDisable(newValue.trim().isEmpty());
                    }
                });
                proposal1.textProperty().addListener((observable, oldValue, newValue) -> {
                    if(!nrAluno.textProperty().getValue().isEmpty()) {
                        insertButton.setDisable(newValue.trim().isEmpty());
                        proposal2.setDisable(newValue.trim().isEmpty());
                        if(proposal2.isDisable()) {
                            proposal3.setDisable(newValue.trim().isEmpty());
                            proposal4.setDisable(newValue.trim().isEmpty());
                            proposal5.setDisable(newValue.trim().isEmpty());
                            proposal6.setDisable(newValue.trim().isEmpty());
                        }
                        if(!proposal2.textProperty().getValue().isEmpty())
                            proposal3.setDisable(newValue.trim().isEmpty());
                        if(!proposal3.textProperty().getValue().isEmpty())
                            proposal4.setDisable(newValue.trim().isEmpty());
                        if(!proposal4.textProperty().getValue().isEmpty())
                            proposal5.setDisable(newValue.trim().isEmpty());
                        if(!proposal5.textProperty().getValue().isEmpty())
                            proposal6.setDisable(newValue.trim().isEmpty());

                    }
                });
                proposal2.textProperty().addListener((observable, oldValue, newValue) -> {
                    proposal3.setDisable(newValue.trim().isEmpty());
                    if(proposal3.isDisable()) {
                        proposal4.setDisable(newValue.trim().isEmpty());
                        proposal5.setDisable(newValue.trim().isEmpty());
                        proposal6.setDisable(newValue.trim().isEmpty());
                    }
                    if(!proposal3.textProperty().getValue().isEmpty())
                        proposal4.setDisable(newValue.trim().isEmpty());
                    if(!proposal4.textProperty().getValue().isEmpty())
                        proposal5.setDisable(newValue.trim().isEmpty());
                    if(!proposal5.textProperty().getValue().isEmpty())
                        proposal6.setDisable(newValue.trim().isEmpty());
                });
                proposal3.textProperty().addListener((observable, oldValue, newValue) -> {
                    proposal4.setDisable(newValue.trim().isEmpty());
                    if(proposal4.isDisable()) {
                        proposal5.setDisable(newValue.trim().isEmpty());
                        proposal6.setDisable(newValue.trim().isEmpty());
                    }
                    if(!proposal4.textProperty().getValue().isEmpty())
                        proposal5.setDisable(newValue.trim().isEmpty());
                    if(!proposal5.textProperty().getValue().isEmpty())
                        proposal6.setDisable(newValue.trim().isEmpty());
                });
                proposal4.textProperty().addListener((observable, oldValue, newValue) -> {
                    proposal5.setDisable(newValue.trim().isEmpty());
                    if(proposal5.isDisable())
                        proposal6.setDisable(newValue.trim().isEmpty());
                    if(!proposal5.textProperty().getValue().isEmpty())
                        proposal6.setDisable(newValue.trim().isEmpty());
                });
                proposal5.textProperty().addListener((observable, oldValue, newValue) -> {
                    proposal6.setDisable(newValue.trim().isEmpty());
                });

                dialog.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(() -> nrAluno.requestFocus());

                // Convert the result to a username-password-pair when the login button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == insertButtonType) {

                        Long nrEstudante;
                        List<String> propostas = new ArrayList<>();

                        Scanner scanner = new Scanner(nrAluno.textProperty().getValue());
                        if (scanner.hasNextLong())
                            nrEstudante = scanner.nextLong();
                        else {
                            ToastMessage.show(getScene().getWindow(), "Insert a correct Student ID!");
                            return null;
                        }

                        propostas.add(proposal1.textProperty().getValue());

                        if (!proposal2.isDisable() && !proposal2.textProperty().getValue().isEmpty())
                            propostas.add(proposal2.textProperty().getValue());
                        if (!proposal3.isDisable() && !proposal3.textProperty().getValue().isEmpty())
                            propostas.add(proposal3.textProperty().getValue());
                        if (!proposal4.isDisable() && !proposal4.textProperty().getValue().isEmpty())
                            propostas.add(proposal4.textProperty().getValue());
                        if (!proposal5.isDisable() && !proposal5.textProperty().getValue().isEmpty())
                            propostas.add(proposal5.textProperty().getValue());
                        if (!proposal6.isDisable() && !proposal6.textProperty().getValue().isEmpty())
                            propostas.add(proposal6.textProperty().getValue());

                        if(!sistemaManager.addAplications(nrEstudante, propostas).isEmpty()) {
                            ToastMessage.show(getScene().getWindow(),sistemaManager.addAplications(nrEstudante, propostas));
                            return null;
                        }
                    }
                    return null;
                });
                dialog.showAndWait();
            } else if (result.get() == buttonTypeFour) {
                final Popup popup = new Popup();
                popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece
                //popup.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT);

                /*TableView tableView = new TableView();

                TableColumn<Map<Long, List<String>>, Long> column1 = new TableColumn<>("Student ID");
                TableColumn<Map<Long, List<String>>, List<String>> column2 = new TableColumn<>("Proposals code");

                tableView.getColumns().addAll(column1, column2);

                ObservableList<Map<Long, List<String>>> list = FXCollections.observableArrayList(
                        sistemaManager.getMapApplications()
                );

                column1.setCellValueFactory(new PropertyValueFactory<>(sistemaManager.getMapApplications().values().toString()));
                column2.setCellValueFactory(new PropertyValueFactory<>("Proposals code"));

                tableView.setItems(list);*/

                ListView listView = new ListView();
                for (Long key: sistemaManager.getMapApplications().keySet()) {
                    //System.out.println(key + " = " + sistemaManager.getMapApplications().get(key));
                    listView.getItems().add(key + " = " + sistemaManager.getMapApplications().get(key));
                }
                //listView.getItems().addAll(sistemaManager.getMapApplications());

                BorderPane pane = new BorderPane(listView);
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
            } else if (result.get() == buttonTypeFive) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Edit Proposal");
                dialog.setHeaderText("Insert the student id and a proposal identification");

                // Set the button types.
                ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);


                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField nrEstudante = new TextField();
                nrEstudante.setPromptText("Student ID");
                CheckBox add = new CheckBox();
                CheckBox remove = new CheckBox();
                TextField codProposta = new TextField();
                codProposta.setPromptText("Proposal code");

                grid.add(new Label("Student ID:"), 0, 0);
                grid.add(nrEstudante, 1, 0);
                grid.add(new Label("Add proposal"), 0, 1);
                grid.add(add, 1, 1);
                grid.add(new Label("Remove Proposal"), 2, 1);
                grid.add(remove, 3, 1);
                grid.add(new Label("Proposal code:"), 0, 3);
                grid.add(codProposta, 1, 3);

                // Enable/Disable login button depending on whether a username was entered.
                Node editButton = dialog.getDialogPane().lookupButton(editButtonType);
                editButton.setDisable(true);

                nrEstudante.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!codProposta.textProperty().getValue().isEmpty() && (add.isSelected() || remove.isSelected()))
                        editButton.setDisable(newValue.trim().isEmpty());
                });
                add.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!nrEstudante.textProperty().getValue().isEmpty() && !codProposta.textProperty().getValue().isEmpty())
                        editButton.setDisable(!add.isSelected());
                    remove.setDisable(newValue);
                });
                remove.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!nrEstudante.textProperty().getValue().isEmpty() && !codProposta.textProperty().getValue().isEmpty())
                        editButton.setDisable(!remove.isSelected());
                    add.setDisable(newValue);
                });
                codProposta.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!nrEstudante.textProperty().getValue().isEmpty() && (add.isSelected() || remove.isSelected()))
                        editButton.setDisable(newValue.trim().isEmpty());
                });

                dialog.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(() -> nrEstudante.requestFocus());

                // Convert the result to a username-password-pair when the login button is clicked.
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == editButtonType) {

                        Long nrAluno;

                        Scanner scanner = new Scanner(nrEstudante.textProperty().getValue());
                        if (scanner.hasNextLong())
                            nrAluno = scanner.nextLong();
                        else {
                            ToastMessage.show(getScene().getWindow(), "Insert a correct Student ID!");
                            return null;
                        }

                        if (add.isSelected()) {
                            if (!sistemaManager.editProposalsAdd(nrAluno, codProposta.textProperty().getValue()).isEmpty()) {
                                ToastMessage.show(getScene().getWindow(), sistemaManager.editProposalsAdd(nrAluno, codProposta.textProperty().getValue()));
                                return null;
                            }
                        }

                        if (remove.isSelected()) {
                            if (!sistemaManager.editProposalsRemove(nrAluno, codProposta.textProperty().getValue()).isEmpty()) {
                                ToastMessage.show(getScene().getWindow(), sistemaManager.editProposalsRemove(nrAluno, codProposta.textProperty().getValue()));
                                return null;
                            }
                        }
                    }
                    return null;
                });

                dialog.showAndWait();
            } else if (result.get() == buttonTypeSix) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Remove Application");
                dialog.setHeaderText("Insert the student ID");
                dialog.setContentText("Student ID");

                // Traditional way to get the response value.
                Optional<String> result2 = dialog.showAndWait();

                // The Java 8 way to get the response value (with lambda expression).
                //result.ifPresent(letter -> System.out.println("Your choice: " + letter));

                if (result2.isPresent()) {

                    Long nrAluno;

                    Scanner scanner = new Scanner(result2.get());
                    if (scanner.hasNextLong())
                        nrAluno = scanner.nextLong();
                    else {
                        ToastMessage.show(getScene().getWindow(),"Insert a correct Student ID!");
                        return;
                    }

                    if(!sistemaManager.removeApplications(nrAluno).isEmpty()) {
                        ToastMessage.show(getScene().getWindow(),sistemaManager.removeApplications(nrAluno));
                        return;
                    }

                }
            }
        });
        btns2.setOnAction( event -> {
            List<String> choices = new ArrayList<>();
            choices.add("With self-proposal");
            choices.add("With application already registered");
            choices.add("No registered application");

            ChoiceDialog<String> dialog = new ChoiceDialog<>("With self-proposal", choices);
            dialog.setTitle("Proposal Management");
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
                    case "No registered application" -> listView.getItems().addAll(sistemaManager.CSwithoutRegisteredApplication());
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
        btns3.setOnAction( event -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Application Options");
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
            grid.add(new Label("Proposals with applications"), 0, 3);
            grid.add(checkBox3, 1, 3);
            grid.add(new Label("Proposals without applications"), 0, 4);
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
                        listView.getItems().add(sistemaManager.proposalsWithApplications());
                    }
                    if (checkBox4.isSelected()) {
                        listView.getItems().add(sistemaManager.proposalsWithoutApplications());
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
                sistemaManager.change(SistemState.APPLICATION_OPTIONS_LOCK);
        });
        btns7.setOnAction( event -> {
            sistemaManager.up();
        });
        btns8.setOnAction( event -> {
            sistemaManager.down();
        });
        btns9.setOnAction( event -> {
            Platform.exit();
        });
    }

    private void update() {
        if (sistemaManager.getState() != SistemState.APPLICATION_OPTIONS) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        lb1.setText("Students: " + sistemaManager.getStudents().size());
        lb2.setText("W\\ aplication: " + sistemaManager.getMapApplications().keySet().size());
        lb3.setText("Proposals: " + sistemaManager.getProposals().size());
        lb4.setText("W\\ aplication: " + sistemaManager.getMapApplications().values().size());
    }

}

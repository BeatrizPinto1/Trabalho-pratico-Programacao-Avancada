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
import pt.isec.pa.apoio_poe.model.data.*;
import pt.isec.pa.apoio_poe.model.fsm.SistemState;
import pt.isec.pa.apoio_poe.ui.gui.resources.util.ToastMessage;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProposalsManagementUI extends BorderPane {
    SistemaManager sistemaManager;
    Button btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8;
    Label lbEstado, lbPropostas;

    public ProposalsManagementUI(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lbEstado = new Label("Proposals Management");
        lbEstado.setPadding(new Insets(15));
        setAlignment(lbEstado, Pos.TOP_CENTER);
        lbEstado.setTranslateY(80);
        this.setTop(lbEstado);

        btns1 = new Button("Import Proposals");
        btns1.setMinWidth(280);
        btns2 = new Button("Export Proposals");
        btns2.setMinWidth(280);
        btns3 = new Button("Insert Proposals");
        btns3.setMinWidth(280);
        btns4 = new Button("Consult Proposals");
        btns4.setMinWidth(280);
        btns5 = new Button("Edit Proposal");
        btns5.setMinWidth(280);
        btns6 = new Button("Remove Proposal");
        btns6.setMinWidth(280);
        btns7 = new Button("Back to Configuration State");
        btns7.setMinWidth(280);
        btns8 = new Button("Quit");
        btns8.setMinWidth(280);
        VBox vBox = new VBox(btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        this.setCenter(vBox);

        lbPropostas = new Label("Proposals: ");
        setBottom(lbPropostas);
        setAlignment(lbPropostas, Pos.BOTTOM_CENTER);
        lbPropostas.setTranslateY(-20);
        lbPropostas.setId("labeldraw");
    }

    private void registerHandlers() {
        sistemaManager.addPropertyChangeListener(evt -> { update(); });
        btns1.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", new String[]{"*.csv"});
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(new File("./"));

            try {
                String nome = fileChooser.showOpenDialog((Window) null).getName();
                if (nome != null) {
                    sistemaManager.setProposals(nome);
                } else {
                    ToastMessage.show(getScene().getWindow(), "Erro ao carregar o ficheiro");
                }
            } catch (Exception e) {
                ToastMessage.show(getScene().getWindow(), "Erro ao carregar o ficheiro");
            }

        });
        btns2.setOnAction(actionEvent -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setContentText("Nome: ");
            tid.setHeaderText("Que nome deseja dar ao ficheiro: ");
            tid.setTitle("Nome do ficheiro para exportar");
            tid.showAndWait();
            String nome = tid.getResult();

            try {
                sistemaManager.exportToFile(nome, 3);
            } catch (Exception e) {
                ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
            }
        });
        btns3.setOnAction(actionEvent -> {

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

            if (result.isPresent()) {

                Dialog<String> dialog2 = new Dialog<>();
                dialog2.setTitle("Proposals Management");
                dialog2.setHeaderText("Insert Proposal");

                // Set the button types.
                ButtonType insertButtonType = new ButtonType("Insert", ButtonBar.ButtonData.OK_DONE);
                dialog2.getDialogPane().getButtonTypes().addAll(insertButtonType, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField codigoIdentificacao = new TextField();
                codigoIdentificacao.setPromptText("Identification code");
                TextField titulo = new TextField();
                titulo.setPromptText("Title");
                TextField areaDestino = new TextField();
                areaDestino.setPromptText("Destination area");
                TextField identidadeAcolhimento = new TextField();
                identidadeAcolhimento.setPromptText("Host identity");

                TextField nrEstudante = new TextField();
                nrEstudante.setPromptText("Student ID");

                TextField emailDocente = new TextField();
                emailDocente.setPromptText("Teacher's email:");

                switch (result.get()) {
                    case "Stage" -> {
                        grid.add(new Label("Identification code:"), 0, 1); //coluna 0 | linha 1
                        grid.add(codigoIdentificacao, 1, 1);
                        grid.add(new Label("Title:"), 0, 2);
                        grid.add(titulo, 1, 2);
                        grid.add(new Label("Destination area:"), 0, 3);
                        grid.add(areaDestino, 1, 3);
                        grid.add(new Label("Host identity:"), 0, 4);
                        grid.add(identidadeAcolhimento, 1, 4);
                        grid.add(new Label("Student ID:"), 0, 5);
                        grid.add(nrEstudante, 1, 5);

                        // Enable/Disable login button depending on whether a username was entered.
                        Node insertButton = dialog2.getDialogPane().lookupButton(insertButtonType);
                        insertButton.setDisable(true);

                        // Do some validation (using the Java 8 lambda syntax).
                        codigoIdentificacao.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!titulo.textProperty().getValue().isEmpty() && !areaDestino.textProperty().getValue().isEmpty() &&
                                    !identidadeAcolhimento.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        titulo.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!codigoIdentificacao.textProperty().getValue().isEmpty() && !areaDestino.textProperty().getValue().isEmpty() &&
                                    !identidadeAcolhimento.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        areaDestino.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!codigoIdentificacao.textProperty().getValue().isEmpty() && !titulo.textProperty().getValue().isEmpty() &&
                                    !identidadeAcolhimento.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        identidadeAcolhimento.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!codigoIdentificacao.textProperty().getValue().isEmpty() && !titulo.textProperty().getValue().isEmpty() &&
                                    !areaDestino.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });

                        dialog2.setResultConverter(dialogButton -> {
                            if (dialogButton == insertButtonType) {

                                if(!areaDestino.textProperty().getValue().equalsIgnoreCase("DA") && !areaDestino.textProperty().getValue().equalsIgnoreCase("RAS") && !areaDestino.textProperty().getValue().equalsIgnoreCase("SI")) {
                                    ToastMessage.show(getScene().getWindow(),"Insert a correct destination area!");
                                    return null;
                                }

                                if(!nrEstudante.textProperty().getValue().isEmpty()) {
                                    Long nrAluno;

                                    Scanner scanner = new Scanner(nrEstudante.textProperty().getValue());
                                    if (scanner.hasNextLong())
                                        nrAluno = scanner.nextLong();
                                    else {
                                        ToastMessage.show(getScene().getWindow(),"Insert a correct Student ID!");
                                        return null;
                                    }

                                    if(sistemaManager.searchStudent(nrAluno) == null) {
                                        ToastMessage.show(getScene().getWindow(), "This student does not exist!");
                                        return null;
                                    }

                                    if (!sistemaManager.addProposals(new Stages(codigoIdentificacao.textProperty().getValue(), areaDestino.textProperty().getValue(), titulo.textProperty().getValue(), identidadeAcolhimento.textProperty().getValue(), nrAluno)))
                                        ToastMessage.show(getScene().getWindow(), "This proposal already exists!");

                                }
                                else {
                                    if (!sistemaManager.addProposals(new Stages(codigoIdentificacao.textProperty().getValue(), areaDestino.textProperty().getValue(), titulo.textProperty().getValue(), identidadeAcolhimento.textProperty().getValue())))
                                        ToastMessage.show(getScene().getWindow(), "This proposal already exists!");
                                }

                            }
                            return null;
                        });

                    }
                    case "Project" -> {
                        grid.add(new Label("Identification code:"), 0, 1); //coluna 0 | linha 1
                        grid.add(codigoIdentificacao, 1, 1);
                        grid.add(new Label("Title:"), 0, 2);
                        grid.add(titulo, 1, 2);
                        grid.add(new Label("Destination area:"), 0, 3);
                        grid.add(areaDestino, 1, 3);
                        grid.add(new Label("Teacher's email:"), 0, 4);
                        grid.add(emailDocente, 1, 4);
                        grid.add(new Label("Student ID:"), 0, 5);
                        grid.add(nrEstudante, 1, 5);

                        // Enable/Disable login button depending on whether a username was entered.
                        Node insertButton = dialog2.getDialogPane().lookupButton(insertButtonType);
                        insertButton.setDisable(true);

                        // Do some validation (using the Java 8 lambda syntax).
                        codigoIdentificacao.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!titulo.textProperty().getValue().isEmpty() && !areaDestino.textProperty().getValue().isEmpty() &&
                                    !emailDocente.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        titulo.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!codigoIdentificacao.textProperty().getValue().isEmpty() && !areaDestino.textProperty().getValue().isEmpty() &&
                                    !emailDocente.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        areaDestino.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!codigoIdentificacao.textProperty().getValue().isEmpty() && !titulo.textProperty().getValue().isEmpty() &&
                                    !emailDocente.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        emailDocente.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!codigoIdentificacao.textProperty().getValue().isEmpty() && !titulo.textProperty().getValue().isEmpty() &&
                                    !areaDestino.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });

                        dialog2.setResultConverter(dialogButton -> {
                            if (dialogButton == insertButtonType) {

                                if(!areaDestino.textProperty().getValue().equalsIgnoreCase("DA") && !areaDestino.textProperty().getValue().equalsIgnoreCase("RAS") && !areaDestino.textProperty().getValue().equalsIgnoreCase("SI")) {
                                    ToastMessage.show(getScene().getWindow(),"Insert a correct destination area!");
                                    return null;
                                }

                                String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                                Pattern pattern;
                                Matcher matcher;

                                pattern = Pattern.compile(regex);
                                matcher = pattern.matcher(emailDocente.textProperty().getValue());
                                if(!matcher.matches()) {
                                    ToastMessage.show(getScene().getWindow(),"Insert a correct teache's email!");
                                    return null;
                                }

                                if (sistemaManager.searchTeacher(emailDocente.textProperty().getValue()) == null) {
                                    ToastMessage.show(getScene().getWindow(),"This teacher does not exist!");
                                    return null;
                                }

                                if(!nrEstudante.textProperty().getValue().isEmpty()) {
                                    Long nrAluno;

                                    Scanner scanner = new Scanner(nrEstudante.textProperty().getValue());
                                    if (scanner.hasNextLong())
                                        nrAluno = scanner.nextLong();
                                    else {
                                        ToastMessage.show(getScene().getWindow(),"Insert a correct Student ID!");
                                        return null;
                                    }

                                    if(sistemaManager.searchStudent(nrAluno) == null) {
                                        ToastMessage.show(getScene().getWindow(), "This student does not exist!");
                                        return null;
                                    }

                                    if (!sistemaManager.addProposals(new Projects(codigoIdentificacao.textProperty().getValue(), areaDestino.textProperty().getValue(), titulo.textProperty().getValue(), emailDocente.textProperty().getValue(), nrAluno)))
                                        ToastMessage.show(getScene().getWindow(), "This proposal already exists!");
                                }
                                else {
                                    if (!sistemaManager.addProposals(new Projects(codigoIdentificacao.textProperty().getValue(), areaDestino.textProperty().getValue(), titulo.textProperty().getValue(), emailDocente.textProperty().getValue())))
                                        ToastMessage.show(getScene().getWindow(), "This proposal already exists!");
                                }

                            }
                            return null;
                        });
                    }
                    case "Internship/self-proposed project" -> {
                        grid.add(new Label("Identification code:"), 0, 1); //coluna 0 | linha 1
                        grid.add(codigoIdentificacao, 1, 1);
                        grid.add(new Label("Title:"), 0, 2);
                        grid.add(titulo, 1, 2);
                        grid.add(new Label("Student ID:"), 0, 3);
                        grid.add(nrEstudante, 1, 3);

                        // Enable/Disable login button depending on whether a username was entered.
                        Node insertButton = dialog2.getDialogPane().lookupButton(insertButtonType);
                        insertButton.setDisable(true);

                        // Do some validation (using the Java 8 lambda syntax).
                        codigoIdentificacao.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!titulo.textProperty().getValue().isEmpty() && !nrEstudante.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        titulo.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!codigoIdentificacao.textProperty().getValue().isEmpty() && !nrEstudante.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        nrEstudante.textProperty().addListener((observable, oldValue, newValue) -> {
                            if(!codigoIdentificacao.textProperty().getValue().isEmpty() && !titulo.textProperty().getValue().isEmpty())
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });

                        dialog2.setResultConverter(dialogButton -> {
                            if (dialogButton == insertButtonType) {

                                Long nrAluno;

                                Scanner scanner = new Scanner(nrEstudante.textProperty().getValue());
                                if (scanner.hasNextLong())
                                    nrAluno = scanner.nextLong();
                                else {
                                    ToastMessage.show(getScene().getWindow(),"Insert a correct Student ID!");
                                    return null;
                                }

                                if(sistemaManager.searchStudent(nrAluno) == null) {
                                    ToastMessage.show(getScene().getWindow(), "This student does not exist!");
                                    return null;
                                }

                                if (!sistemaManager.addProposals(new SelfProposedProject(codigoIdentificacao.textProperty().getValue(), titulo.textProperty().getValue(), nrAluno)))
                                    ToastMessage.show(getScene().getWindow(), "This proposal already exists!");

                            }
                            return null;
                        });
                    }
                }

                dialog2.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                Platform.runLater(() -> codigoIdentificacao.requestFocus());

                dialog2.showAndWait();
            }

        });
        btns4.setOnAction(actionEvent -> {
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
        btns5.setOnAction(actionEvent -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Proposals Management");
            dialog.setHeaderText("Insert the proposal identification");
            dialog.setContentText("Proposal identification");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            // The Java 8 way to get the response value (with lambda expression).
            //result.ifPresent(letter -> System.out.println("Your choice: " + letter));

            //------------------------------- // ----------------------------------//

            if (result.isPresent()) {
                Proposals proposal;
                if((proposal = sistemaManager.searchProposal(result.get())) == null) {
                    ToastMessage.show(getScene().getWindow(),"This proposal does not exist!");
                    return;
                }

                Dialog<String> dialog2 = new Dialog<>();
                dialog2.setTitle("Edit Management");
                dialog2.setHeaderText("Fill the fields you want to change");

                // Set the button types.
                ButtonType insertButtonType = new ButtonType("Insert", ButtonBar.ButtonData.OK_DONE);
                dialog2.getDialogPane().getButtonTypes().addAll(insertButtonType, ButtonType.CANCEL);

                // Create the username and password labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField titulo = new TextField();
                titulo.setPromptText("Title");
                TextField areaDestino = new TextField();
                areaDestino.setPromptText("Destination area");
                TextField identidadeAcolhimento = new TextField();
                identidadeAcolhimento.setPromptText("Host identity");

                TextField nrEstudante = new TextField();
                nrEstudante.setPromptText("Student ID");

                TextField emailDocente = new TextField();
                emailDocente.setPromptText("Teacher's email:");

                switch (sistemaManager.typeProposal(proposal)) {
                    case 1-> {
                        grid.add(new Label("New title:"), 0, 1);
                        grid.add(titulo, 1, 1);
                        grid.add(new Label("New destination area:"), 0, 2);
                        grid.add(areaDestino, 1, 2);
                        grid.add(new Label("New host identity:"), 0, 3);
                        grid.add(identidadeAcolhimento, 1, 3);
                        grid.add(new Label("New Student ID:"), 0, 4);
                        grid.add(nrEstudante, 1, 4);

                        // Enable/Disable login button depending on whether a username was entered.
                        Node insertButton = dialog2.getDialogPane().lookupButton(insertButtonType);
                        insertButton.setDisable(true);

                        // Do some validation (using the Java 8 lambda syntax).
                        titulo.textProperty().addListener((observable, oldValue, newValue) -> {
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        areaDestino.textProperty().addListener((observable, oldValue, newValue) -> {
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        identidadeAcolhimento.textProperty().addListener((observable, oldValue, newValue) -> {
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        nrEstudante.textProperty().addListener((observable, oldValue, newValue) -> {
                            insertButton.setDisable(newValue.trim().isEmpty());
                        });

                        dialog2.setResultConverter(dialogButton -> {
                            if (dialogButton == insertButtonType) {

                                if(!titulo.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 1, titulo.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 2, titulo.textProperty().getValue()));
                                        return null;
                                    }
                                }

                                if(!areaDestino.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 2, areaDestino.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 3, areaDestino.textProperty().getValue()));
                                        return null;
                                    }
                                }

                                if(!identidadeAcolhimento.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 3, identidadeAcolhimento.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 4, identidadeAcolhimento.textProperty().getValue()));
                                        return null;
                                    }
                                }

                                if(!nrEstudante.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 4, nrEstudante.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 5, nrEstudante.textProperty().getValue()));
                                        return null;
                                    }
                                }

                            }
                            return null;
                        });

                    }
                    case 2 -> {
                        grid.add(new Label("New title:"), 0, 1);
                        grid.add(titulo, 1, 1);
                        grid.add(new Label("New destination area:"), 0, 2);
                        grid.add(areaDestino, 1, 2);
                        grid.add(new Label("New teacher's email:"), 0, 3);
                        grid.add(emailDocente, 1, 3);
                        grid.add(new Label("New student ID:"), 0, 4);
                        grid.add(nrEstudante, 1, 4);

                        // Enable/Disable login button depending on whether a username was entered.
                        Node insertButton = dialog2.getDialogPane().lookupButton(insertButtonType);
                        insertButton.setDisable(true);

                        // Do some validation (using the Java 8 lambda syntax).
                        titulo.textProperty().addListener((observable, oldValue, newValue) -> {
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        areaDestino.textProperty().addListener((observable, oldValue, newValue) -> {
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        emailDocente.textProperty().addListener((observable, oldValue, newValue) -> {
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        nrEstudante.textProperty().addListener((observable, oldValue, newValue) -> {
                            insertButton.setDisable(newValue.trim().isEmpty());
                        });

                        dialog2.setResultConverter(dialogButton -> {
                            if (dialogButton == insertButtonType) {

                                if(!titulo.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 1, titulo.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 2, titulo.textProperty().getValue()));
                                        return null;
                                    }
                                }

                                if(!areaDestino.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 2, areaDestino.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 3, areaDestino.textProperty().getValue()));
                                        return null;
                                    }
                                }

                                if(!emailDocente.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 3, emailDocente.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 4, emailDocente.textProperty().getValue()));
                                        return null;
                                    }
                                }

                                if(!nrEstudante.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 4, nrEstudante.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 5, nrEstudante.textProperty().getValue()));
                                        return null;
                                    }
                                }

                            }
                            return null;
                        });
                    }
                    case 3 -> {
                        grid.add(new Label("New title:"), 0, 1);
                        grid.add(titulo, 1, 1);
                        grid.add(new Label("New student ID:"), 0, 2);
                        grid.add(nrEstudante, 1, 2);

                        // Enable/Disable login button depending on whether a username was entered.
                        Node insertButton = dialog2.getDialogPane().lookupButton(insertButtonType);
                        insertButton.setDisable(true);

                        // Do some validation (using the Java 8 lambda syntax).
                        titulo.textProperty().addListener((observable, oldValue, newValue) -> {
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });
                        nrEstudante.textProperty().addListener((observable, oldValue, newValue) -> {
                                insertButton.setDisable(newValue.trim().isEmpty());
                        });

                        dialog2.setResultConverter(dialogButton -> {
                            if (dialogButton == insertButtonType) {

                                if(!titulo.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 1, titulo.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 2, titulo.textProperty().getValue()));
                                        return null;
                                    }
                                }

                                if(!nrEstudante.textProperty().getValue().isEmpty()) {
                                    if(!sistemaManager.editaProposta(proposal.getCodIdentificacao(), 2, nrEstudante.textProperty().getValue()).isEmpty()) {
                                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaProposta(proposal.getCodIdentificacao(), 5, nrEstudante.textProperty().getValue()));
                                        return null;
                                    }
                                }

                            }
                            return null;
                        });
                    }
                }

                dialog2.getDialogPane().setContent(grid);

                // Request focus on the username field by default.
                //Platform.runLater(() -> codigoIdentificacao.requestFocus());

                dialog2.showAndWait();
            }
        });
        btns6.setOnAction( event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Remove Proposal");
            dialog.setHeaderText("Insert the identification code");
            dialog.setContentText("Identification code");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            // The Java 8 way to get the response value (with lambda expression).
            //result.ifPresent(letter -> System.out.println("Your choice: " + letter));

            if (result.isPresent()) {

                if(!sistemaManager.removeProposal(result.get()).isEmpty()) {
                    ToastMessage.show(getScene().getWindow(),"This proposal does not exist!");
                    return;
                }

            }
        });
        btns7.setOnAction( event -> {
            sistemaManager.change(SistemState.CONFIGURATION_STATE);
        });
        btns8.setOnAction( event -> {
            Platform.exit();
        });
    }

    private void update() {
        if (sistemaManager.getState() != SistemState.PROPOSALS_MANAGEMENT) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        lbPropostas.setText("Proposals: " + sistemaManager.getProposals().size());

    }

}

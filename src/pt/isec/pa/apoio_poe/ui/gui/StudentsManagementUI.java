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
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import pt.isec.pa.apoio_poe.model.SistemaManager;
import pt.isec.pa.apoio_poe.model.data.Students;
import pt.isec.pa.apoio_poe.model.fsm.SistemState;
import pt.isec.pa.apoio_poe.ui.gui.resources.util.ToastMessage;


import java.io.File;
import java.util.Optional;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentsManagementUI extends BorderPane {
    SistemaManager sistemaManager;
    Button btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8;
    Label lbEstado, lbAlunos;

    public StudentsManagementUI(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lbEstado = new Label("Students Management");
        lbEstado.setPadding(new Insets(15));
        //lbEstado.setTextFill(Color.DARKRED);
        //setTop(lbEstado);
        setAlignment(lbEstado, Pos.TOP_CENTER);
        //VBox vBox0 = new VBox(lbEstado);
        lbEstado.setTranslateY(80);
        this.setTop(lbEstado);

        btns1 = new Button("Import Students");
        btns1.setMinWidth(280);
        btns2 = new Button("Export Students");
        btns2.setMinWidth(280);
        btns3 = new Button("Insert Student");
        btns3.setMinWidth(280);
        btns4 = new Button("Consult Students");
        btns4.setMinWidth(280);
        btns5 = new Button("Edit Student");
        btns5.setMinWidth(280);
        btns6 = new Button("Remove Student");
        btns6.setMinWidth(280);
        btns7 = new Button("Back to Configuration State");
        btns7.setMinWidth(280);
        btns8 = new Button("Quit");
        btns8.setMinWidth(280);
        VBox vBox = new VBox(btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        this.setCenter(vBox);

        lbAlunos = new Label("Students: ");
        setBottom(lbAlunos);
        setAlignment(lbAlunos, Pos.BOTTOM_CENTER);
        lbAlunos.setTranslateY(-20);
        lbAlunos.setId("labeldraw");
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
                    sistemaManager.setStudents(nome);
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
                sistemaManager.exportToFile(nome, 1);
            } catch (Exception e) {
                ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
            }
        });
        btns3.setOnAction(actionEvent -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Students Management");
            dialog.setHeaderText("Insert Student");

            // Set the icon (must be included in the project).
            //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

            // Set the button types.
            ButtonType insertButtonType = new ButtonType("Insert", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(insertButtonType, ButtonType.CANCEL);


            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nrEstudante = new TextField();
            nrEstudante.setPromptText("Student ID");
            TextField name = new TextField();
            name.setPromptText("Name");
            TextField email = new TextField();
            email.setPromptText("Email");
            TextField curse = new TextField();
            curse.setPromptText("Curse");
            TextField ramo = new TextField();
            ramo.setPromptText("Branch");
            TextField classificacao = new TextField();
            classificacao.setPromptText("Classification");
            TextField acedeEstagios = new TextField();
            acedeEstagios.setPromptText("Possibility of internships");

            grid.add(new Label("Student ID:"), 0, 0);
            grid.add(nrEstudante, 1, 0);
            grid.add(new Label("Name:"), 0, 1); //coluna 0 | linha 1
            grid.add(name, 1, 1);
            grid.add(new Label("Email:"), 0, 2);
            grid.add(email, 1, 2);
            grid.add(new Label("Curse:"), 0, 3);
            grid.add(curse, 1, 3);
            grid.add(new Label("Branch:"), 0, 4);
            grid.add(ramo, 1, 4);
            grid.add(new Label("Classification:"), 0, 5);
            grid.add(classificacao, 1, 5);
            grid.add(new Label("Possibility of internships:"), 0, 6);
            grid.add(acedeEstagios, 1, 6);

            // Enable/Disable login button depending on whether a username was entered.
            Node insertButton = dialog.getDialogPane().lookupButton(insertButtonType);
            insertButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            nrEstudante.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!name.textProperty().getValue().isEmpty() && !email.textProperty().getValue().isEmpty() &&
                        !curse.textProperty().getValue().isEmpty() && !ramo.textProperty().getValue().isEmpty()
                        && !classificacao.textProperty().getValue().isEmpty() && !acedeEstagios.textProperty().getValue().isEmpty())
                    insertButton.setDisable(newValue.trim().isEmpty());
            });
            name.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty() && !email.textProperty().getValue().isEmpty() &&
                        !curse.textProperty().getValue().isEmpty() && !ramo.textProperty().getValue().isEmpty()
                        && !classificacao.textProperty().getValue().isEmpty() && !acedeEstagios.textProperty().getValue().isEmpty())
                    insertButton.setDisable(newValue.trim().isEmpty() );
            });
            email.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty() && !name.textProperty().getValue().isEmpty() &&
                        !curse.textProperty().getValue().isEmpty() && !ramo.textProperty().getValue().isEmpty()
                        && !classificacao.textProperty().getValue().isEmpty() && !acedeEstagios.textProperty().getValue().isEmpty())
                    insertButton.setDisable(newValue.trim().isEmpty());
            });
            curse.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty() && !name.textProperty().getValue().isEmpty() &&
                        !email.textProperty().getValue().isEmpty() && !ramo.textProperty().getValue().isEmpty()
                        && !classificacao.textProperty().getValue().isEmpty() && !acedeEstagios.textProperty().getValue().isEmpty())
                    insertButton.setDisable(newValue.trim().isEmpty());
            });
            ramo.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty() && !name.textProperty().getValue().isEmpty() &&
                        !email.textProperty().getValue().isEmpty() && !curse.textProperty().getValue().isEmpty()
                        && !classificacao.textProperty().getValue().isEmpty() && !acedeEstagios.textProperty().getValue().isEmpty())
                    insertButton.setDisable(newValue.trim().isEmpty());
            });
            classificacao.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty() && !name.textProperty().getValue().isEmpty() &&
                        !email.textProperty().getValue().isEmpty() && !curse.textProperty().getValue().isEmpty()
                        && !ramo.textProperty().getValue().isEmpty() && !acedeEstagios.textProperty().getValue().isEmpty())
                    insertButton.setDisable(newValue.trim().isEmpty());
            });
            acedeEstagios.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty() && !name.textProperty().getValue().isEmpty() &&
                        !email.textProperty().getValue().isEmpty() && !curse.textProperty().getValue().isEmpty()
                        && !ramo.textProperty().getValue().isEmpty() && !classificacao.textProperty().getValue().isEmpty())
                    insertButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> nrEstudante.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == insertButtonType) {
                    Long nrAluno;
                    double classific;
                    boolean acedeEstag;

                    Scanner scanner = new Scanner(nrEstudante.textProperty().getValue());
                    if (scanner.hasNextLong())
                        nrAluno = scanner.nextLong();
                    else {
                        ToastMessage.show(getScene().getWindow(),"Insert a correct Student ID!");
                        return null;
                    }

                    if(!name.textProperty().getValue().matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")) {
                        ToastMessage.show(getScene().getWindow(),"Insert a correct name!");
                        return null;
                    }

                    String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                    Pattern pattern;
                    Matcher matcher;

                    pattern = Pattern.compile(regex);
                    matcher = pattern.matcher(email.textProperty().getValue());
                    if(!matcher.matches()) {
                        ToastMessage.show(getScene().getWindow(),"Insert a correct email!");
                        return null;
                    }

                    if(!curse.textProperty().getValue().equalsIgnoreCase("LEI") && !curse.textProperty().getValue().equalsIgnoreCase("LEI-PL")) {
                        ToastMessage.show(getScene().getWindow(),"Insert a correct curse!");
                        return null;
                    }

                    if(!ramo.textProperty().getValue().equalsIgnoreCase("DA") && !ramo.textProperty().getValue().equalsIgnoreCase("RAS") && !ramo.textProperty().getValue().equalsIgnoreCase("SI")) {
                        ToastMessage.show(getScene().getWindow(),"Insert a correct branch!");
                        return null;
                    }

                    Scanner scanner2 = new Scanner(classificacao.textProperty().getValue());
                    if (scanner2.hasNextDouble())
                        classific = scanner2.nextDouble();
                    else {
                        ToastMessage.show(getScene().getWindow(),"Insert a correct classification!");
                        return null;
                    }

                    Scanner scanner3 = new Scanner(acedeEstagios.textProperty().getValue());

                    if (scanner3.hasNextBoolean())
                        acedeEstag = scanner3.nextBoolean();
                    else {
                        ToastMessage.show(getScene().getWindow(),"Insert a correct Possibility of internships!");
                        return null;
                    }

                    if (!sistemaManager.addStudent(new Students(nrAluno, name.textProperty().getValue(), email.textProperty().getValue(), curse.textProperty().getValue(), ramo.textProperty().getValue(), classific, acedeEstag)))
                        ToastMessage.show(getScene().getWindow(),"This student already exists!");

                }
                return null;
            });

            dialog.showAndWait();
        });
        btns4.setOnAction(actionEvent -> {
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
        btns5.setOnAction(actionEvent -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Edit Student");
            dialog.setHeaderText("Insert the Student ID and the fields to change");

            // Set the icon (must be included in the project).
            //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

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
            TextField name = new TextField();
            name.setPromptText("Name");
            TextField email = new TextField();
            email.setPromptText("Email");
            TextField curse = new TextField();
            curse.setPromptText("Curse");
            TextField ramo = new TextField();
            ramo.setPromptText("Branch");
            TextField classificacao = new TextField();
            classificacao.setPromptText("Classification");
            TextField acedeEstagios = new TextField();
            acedeEstagios.setPromptText("Possibility of internships");

            grid.add(new Label("Student ID:"), 0, 0);
            grid.add(nrEstudante, 1, 0);
            grid.add(new Label("New name:"), 0, 1); //coluna 0 | linha 1
            grid.add(name, 1, 1);
            grid.add(new Label("New email:"), 0, 2);
            grid.add(email, 1, 2);
            grid.add(new Label("New curse:"), 0, 3);
            grid.add(curse, 1, 3);
            grid.add(new Label("New branch:"), 0, 4);
            grid.add(ramo, 1, 4);
            grid.add(new Label("New classification:"), 0, 5);
            grid.add(classificacao, 1, 5);
            grid.add(new Label("New possibility of internships:"), 0, 6);
            grid.add(acedeEstagios, 1, 6);

            // Enable/Disable login button depending on whether a username was entered.
            Node editButton = dialog.getDialogPane().lookupButton(editButtonType);
            editButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            nrEstudante.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!name.textProperty().getValue().isEmpty() || !email.textProperty().getValue().isEmpty() ||
                        !curse.textProperty().getValue().isEmpty() || !ramo.textProperty().getValue().isEmpty()
                        || !classificacao.textProperty().getValue().isEmpty() || !acedeEstagios.textProperty().getValue().isEmpty())
                    editButton.setDisable(newValue.trim().isEmpty());
            });
            name.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty())
                    editButton.setDisable(newValue.trim().isEmpty() );
            });
            email.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty())
                    editButton.setDisable(newValue.trim().isEmpty() );
            });
            curse.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty())
                    editButton.setDisable(newValue.trim().isEmpty() );
            });
            ramo.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty())
                    editButton.setDisable(newValue.trim().isEmpty() );
            });
            classificacao.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty())
                    editButton.setDisable(newValue.trim().isEmpty() );
            });
            acedeEstagios.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!nrEstudante.textProperty().getValue().isEmpty())
                    editButton.setDisable(newValue.trim().isEmpty() );
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
                        ToastMessage.show(getScene().getWindow(),"Insert a correct Student ID!");
                        return null;
                    }

                    if(!name.textProperty().getValue().isEmpty()) {
                        if(!sistemaManager.editaEstudante(nrAluno, 1, name.textProperty().getValue()).isEmpty()) {
                            ToastMessage.show(getScene().getWindow(),sistemaManager.editaEstudante(nrAluno, 1, name.textProperty().getValue()));
                            return null;
                        }
                    }

                    if(!email.textProperty().getValue().isEmpty()) {
                        if(!sistemaManager.editaEstudante(nrAluno, 2, email.textProperty().getValue()).isEmpty()) {
                            ToastMessage.show(getScene().getWindow(),sistemaManager.editaEstudante(nrAluno, 1, name.textProperty().getValue()));
                            return null;
                        }
                    }

                    if(!curse.textProperty().getValue().isEmpty()) {
                        if(!sistemaManager.editaEstudante(nrAluno, 3, curse.textProperty().getValue()).isEmpty()) {
                            ToastMessage.show(getScene().getWindow(),sistemaManager.editaEstudante(nrAluno, 1, name.textProperty().getValue()));
                            return null;
                        }
                    }

                    if(!ramo.textProperty().getValue().isEmpty()) {
                        if(!sistemaManager.editaEstudante(nrAluno, 4, ramo.textProperty().getValue()).isEmpty()) {
                            ToastMessage.show(getScene().getWindow(),sistemaManager.editaEstudante(nrAluno, 1, name.textProperty().getValue()));
                            return null;
                        }
                    }

                    if(!classificacao.textProperty().getValue().isEmpty()) {
                        if(!sistemaManager.editaEstudante(nrAluno, 5, classificacao.textProperty().getValue()).isEmpty()) {
                            ToastMessage.show(getScene().getWindow(),sistemaManager.editaEstudante(nrAluno, 1, name.textProperty().getValue()));
                            return null;
                        }
                    }

                    if(!acedeEstagios.textProperty().getValue().isEmpty()) {
                        if(!sistemaManager.editaEstudante(nrAluno, 6, acedeEstagios.textProperty().getValue()).isEmpty()) {
                            ToastMessage.show(getScene().getWindow(),sistemaManager.editaEstudante(nrAluno, 1, name.textProperty().getValue()));
                            return null;
                        }
                    }
                }
                return null;
            });

            dialog.showAndWait();

        });
        btns6.setOnAction( event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Remove Student");
            dialog.setHeaderText("Insert the student ID");
            dialog.setContentText("Student ID");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            // The Java 8 way to get the response value (with lambda expression).
            //result.ifPresent(letter -> System.out.println("Your choice: " + letter));

            if (result.isPresent()) {

                Long nrAluno;

                Scanner scanner = new Scanner(result.get());
                if (scanner.hasNextLong())
                    nrAluno = scanner.nextLong();
                else {
                    ToastMessage.show(getScene().getWindow(),"Insert a correct Student ID!");
                    return;
                }

                if(!sistemaManager.removeStudent(nrAluno).isEmpty()) {
                    ToastMessage.show(getScene().getWindow(),"This student does not exist!");
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
        if (sistemaManager.getState() != SistemState.STUDENTS_MANAGEMENT) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        lbAlunos.setText("Students: " + sistemaManager.getStudents().size());

    }

}

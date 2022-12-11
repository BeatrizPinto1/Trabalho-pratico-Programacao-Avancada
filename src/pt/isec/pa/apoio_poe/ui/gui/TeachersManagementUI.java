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
import pt.isec.pa.apoio_poe.model.data.Teachers;
import pt.isec.pa.apoio_poe.model.fsm.SistemState;
import pt.isec.pa.apoio_poe.ui.gui.resources.util.ToastMessage;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeachersManagementUI extends BorderPane {
    SistemaManager sistemaManager;
    Button btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8;
    Label lbEstado, lbDocentes;

    public TeachersManagementUI(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lbEstado = new Label("Teachers Management");
        lbEstado.setPadding(new Insets(15));
        setAlignment(lbEstado, Pos.TOP_CENTER);
        lbEstado.setTranslateY(80);
        this.setTop(lbEstado);

        btns1 = new Button("Import Teachers");
        btns1.setMinWidth(280);
        btns2 = new Button("Export Teachers");
        btns2.setMinWidth(280);
        btns3 = new Button("Insert Teacher");
        btns3.setMinWidth(280);
        btns4 = new Button("Consult Teachers");
        btns4.setMinWidth(280);
        btns5 = new Button("Edit Teacher");
        btns5.setMinWidth(280);
        btns6 = new Button("Remove Teacher");
        btns6.setMinWidth(280);
        btns7 = new Button("Back to Configuration State");
        btns7.setMinWidth(280);
        btns8 = new Button("Quit");
        btns8.setMinWidth(280);
        VBox vBox = new VBox(btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        this.setCenter(vBox);

        lbDocentes = new Label("Teachers: ");
        setBottom(lbDocentes);
        setAlignment(lbDocentes, Pos.BOTTOM_CENTER);
        lbDocentes.setTranslateY(-20);
        lbDocentes.setId("labeldraw");
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
                    sistemaManager.setTeachers(nome);
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
                sistemaManager.exportToFile(nome, 2);
            } catch (Exception e) {
                ToastMessage.show(getScene().getWindow(),"Erro ao exportar o ficheiro");
            }
        });
        btns3.setOnAction(actionEvent -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Students Management");
            dialog.setHeaderText("Insert Student");

            // Set the button types.
            ButtonType insertButtonType = new ButtonType("Insert", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(insertButtonType, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField name = new TextField();
            name.setPromptText("Name");
            TextField email = new TextField();
            email.setPromptText("Email");

            grid.add(new Label("Name:"), 0, 1); //coluna 0 | linha 1
            grid.add(name, 1, 1);
            grid.add(new Label("Email:"), 0, 2);
            grid.add(email, 1, 2);

            // Enable/Disable login button depending on whether a username was entered.
            Node insertButton = dialog.getDialogPane().lookupButton(insertButtonType);
            insertButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            name.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!email.textProperty().getValue().isEmpty())
                    insertButton.setDisable(newValue.trim().isEmpty() );
            });
            email.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!name.textProperty().getValue().isEmpty())
                    insertButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> name.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == insertButtonType) {
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

                    if (!sistemaManager.addTeachers(new Teachers(name.textProperty().getValue(), email.textProperty().getValue())))
                        ToastMessage.show(getScene().getWindow(),"This student already exists!");

                }
                return null;
            });

            dialog.showAndWait();
        });
        btns4.setOnAction(actionEvent -> {
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
        btns5.setOnAction(actionEvent -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Edit Teacher");
            dialog.setHeaderText("Insert the Teacher email and the new name");

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

            TextField email = new TextField();
            email.setPromptText("Email");
            TextField name = new TextField();
            name.setPromptText("Name");

            grid.add(new Label("Email:"), 0, 1);
            grid.add(email, 1, 1);
            grid.add(new Label("New name:"), 0, 2); //coluna 0 | linha 1
            grid.add(name, 1, 2);


            // Enable/Disable login button depending on whether a username was entered.
            Node editButton = dialog.getDialogPane().lookupButton(editButtonType);
            editButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            name.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!email.textProperty().getValue().isEmpty())
                    editButton.setDisable(newValue.trim().isEmpty() );
            });
            email.textProperty().addListener((observable, oldValue, newValue) -> {
                if(!name.textProperty().getValue().isEmpty())
                    editButton.setDisable(newValue.trim().isEmpty() );
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> email.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == editButtonType) {

                    if(!sistemaManager.editaDocente(email.textProperty().getValue(),  name.textProperty().getValue()).isEmpty()) {
                        ToastMessage.show(getScene().getWindow(),sistemaManager.editaDocente(email.textProperty().getValue(), name.textProperty().getValue()));
                        return null;
                    }

                }
                return null;
            });

            dialog.showAndWait();

        });
        btns6.setOnAction( event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Remove Teacher");
            dialog.setHeaderText("Insert the teacher email");
            dialog.setContentText("Teacher email");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()) {
                    if(!sistemaManager.removeTeachers(result.get()).isEmpty()) {
                        ToastMessage.show(getScene().getWindow(),"This teacher does not exist!");
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
        if (sistemaManager.getState() != SistemState.TEACHERS_MANAGEMENT) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        lbDocentes.setText("Teachers: " + sistemaManager.getTeachers().size());

    }

}

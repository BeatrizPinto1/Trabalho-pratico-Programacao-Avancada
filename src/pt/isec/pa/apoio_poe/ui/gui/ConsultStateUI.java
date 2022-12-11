package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
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

public class ConsultStateUI extends BorderPane {
    SistemaManager sistemaManager;
    Button btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8, btns9, btns10, btns11;
    Label lbEstado;

    PieChart chart;
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    Label caption;

    public ConsultStateUI(SistemaManager sistemaManager) {
        this.sistemaManager = sistemaManager;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lbEstado = new Label("Consult State");
        lbEstado.setPadding(new Insets(15));
        setAlignment(lbEstado, Pos.TOP_CENTER);

        lbEstado.setTranslateY(85);
        this.setTop(lbEstado);

        btns1 = new Button("Export data");
        btns1.setMinWidth(440);
        btns2 = new Button("List of students with assigned proposals");
        btns2.setMinWidth(440);
        btns3 = new Button("List of stds w/o ass pro and w/ app options");
        btns3.setMinWidth(440);
        btns4 = new Button("Available proposals");
        btns4.setMinWidth(440);
        btns5 = new Button("Assigned proposals");
        btns5.setMinWidth(440);
        btns6 = new Button("Guidance by teacher");
        btns6.setMinWidth(440);
        btns7 = new Button("Distribution of internships/projects");
        btns7.setMinWidth(440);
        btns8 = new Button("Distribution of proposals");
        btns8.setMinWidth(440);
        btns9 = new Button("Companies with more internships");
        btns9.setMinWidth(440);
        btns10 = new Button("Teachers with more assignment");
        btns10.setMinWidth(440);
        btns11 = new Button("Quit");
        btns11.setMinWidth(440);

        VBox vBox = new VBox(btns1, btns2, btns3, btns4, btns5, btns6, btns7, btns8, btns9, btns10, btns11);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        this.setCenter(vBox);

        caption = new Label();
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");
    }

    private void registerHandlers() {
        sistemaManager.addPropertyChangeListener(evt -> { update(); });
        btns1.setOnAction( event -> {
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
        btns2.setOnAction( event -> {
            final Popup popup = new Popup();
            popup.setAutoHide(true);

            ListView listView = new ListView();
            listView.getItems().addAll(sistemaManager.CSwithProposalsAssigned());

            //listView.setPrefWidth(100);
            //listView.setPrefHeight(70);

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
        });
        btns3.setOnAction( event -> {
            final Popup popup = new Popup();
            popup.setAutoHide(true);

            ListView listView = new ListView();
            listView.getItems().addAll(sistemaManager.studentsWithoutAssignedProposalsAndWithApplicationOptions());

            //listView.setPrefWidth(100);
            //listView.setPrefHeight(70);

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
        });
        btns4.setOnAction( event -> {
            final Popup popup = new Popup();
            popup.setAutoHide(true);

            ListView listView = new ListView();
            listView.getItems().addAll(sistemaManager.availableProposals());

            //listView.setPrefWidth(100);
            //listView.setPrefHeight(70);

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
        });
        btns5.setOnAction( event -> {
            final Popup popup = new Popup();
            popup.setAutoHide(true);

            ListView listView = new ListView();
            listView.getItems().addAll(sistemaManager.assignedProposals());

            //listView.setPrefWidth(100);
            //listView.setPrefHeight(70);

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
        });
        btns6.setOnAction( event -> {

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

                final Popup popup = new Popup();
                popup.setAutoHide(true); //quando dou um toque fora da janela ela desaparece

                ListView listView = new ListView();

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
            final Popup popup = new Popup();
            popup.setAutoHide(true);

            pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("DA", sistemaManager.getRamos(1)),
                            new PieChart.Data("RAS", sistemaManager.getRamos(2)),
                            new PieChart.Data("SI", sistemaManager.getRamos(3))
                            );

            chart = new PieChart(pieChartData);
            chart.setTitle("Distribution of areas in Internships/Projects");

            pieChartData.forEach(data -> data.nameProperty().bind(
                                    Bindings.concat(data.getName(), " ", data.pieValueProperty())));

            chart.setLabelLineLength(10);
            chart.setLegendSide(Side.LEFT);

            for (final PieChart.Data data : chart.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                        e -> {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(data.getPieValue() + "%");
                        });
            }

            BorderPane pane = new BorderPane(chart);
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
        });
        btns8.setOnAction(event -> {

                    final Popup popup = new Popup();
                    popup.setAutoHide(true);

                    pieChartData =
                            FXCollections.observableArrayList(
                                    new PieChart.Data("Assigned Proposals", sistemaManager.getassignedOrNotProposals(1)),
                                    new PieChart.Data("Proposals Not Assigned", sistemaManager.getassignedOrNotProposals(2))
                            );

                    chart = new PieChart(pieChartData);
                    chart.setTitle("Distribution of Proposals");

                    double total = 0;
                    for (PieChart.Data d : chart.getData())
                        total += d.getPieValue();

                    for (PieChart.Data d : chart.getData()) {
                        String text = String.format("%.1f%%", (d.getPieValue() / total)*100);
                        d.nameProperty().bind(
                                Bindings.concat(d.getName(), " ", d.pieValueProperty(), "->" , text));

                    }
            /*pieChartData.forEach(data ->
                    data.nameProperty().bind(
                    Bindings.concat(data.getName(), " ", data.pieValueProperty())));*/


            for (final PieChart.Data data : chart.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                        e -> {
                            double total2 = 0;
                            for (PieChart.Data d : chart.getData()) {
                                total2 += d.getPieValue();
                            }
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            String text = String.format("%.1f%%", 100*data.getPieValue()/total2) ;
                            caption.setText(text);
                        }
                );
            }

            BorderPane pane = new BorderPane(chart);
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

        });
        btns9.setOnAction(event -> {

            final Popup popup = new Popup();
            popup.setAutoHide(true);

            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final BarChart<String,Number> bc =
                    new BarChart<String,Number>(xAxis,yAxis);
            bc.setTitle("Companies with more internships");
            xAxis.setLabel("Companie");
            yAxis.setLabel("Internships");

            List<String> nomes = new ArrayList<>(sistemaManager.companiesWithMoreInternships().keySet());
            List<Integer> valores = new ArrayList<>(sistemaManager.companiesWithMoreInternships().values());


            XYChart.Series series1 = new XYChart.Series();
            series1.setName("TOP5");
            for(int i = 0; i < nomes.size() && i < valores.size() && i < 5; i++) {
                series1.getData().add(new XYChart.Data(nomes.get(i), valores.get(i)));
            }

            bc.getData().addAll(series1);

            BorderPane pane = new BorderPane(bc);
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

        });
        btns10.setOnAction(event -> {

            final Popup popup = new Popup();
            popup.setAutoHide(true);

            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final BarChart<String,Number> bc =
                    new BarChart<String,Number>(xAxis,yAxis);
            bc.setTitle("Teachers with more assignments");
            xAxis.setLabel("Teacher");
            yAxis.setLabel("Assignments");

            List<String> nomes = new ArrayList<>(sistemaManager.teachersWithMoreAssignments().keySet());
            List<Integer> valores = new ArrayList<>(sistemaManager.teachersWithMoreAssignments().values());


            XYChart.Series series1 = new XYChart.Series();
            series1.setName("TOP5");
            for(int i = 0; i < nomes.size() && i < valores.size() && i < 5; i++) {
                series1.getData().add(new XYChart.Data(nomes.get(i), valores.get(i)));
            }

            bc.getData().addAll(series1);

            BorderPane pane = new BorderPane(bc);
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

        });
        btns11.setOnAction(event -> {
            Platform.exit();
        });

    }

    private void update() {
        if (sistemaManager.getState() != SistemState.CONSULT_STATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }

}

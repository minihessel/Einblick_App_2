/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *///
package Controller;

import static Model.ErrorDialog.ErrorDialog;
import View.Visualize;
import Model.Kolonne;
import Model.Table;
import View.ChartToPng;
import View.DataInsight;
import View.SideBar;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.Wizard.LinearFlow;
import org.controlsfx.validation.Validator;

/**
 *
 * @author Eskil Hesselroth
 */
public class MainFXMLController implements Initializable {

    Visualize visualize;
    ArrayList<Table> tablesList;
    SQL_manager sql_manager = new SQL_manager();
    Parent root;
    List<TableView> listOfTableViews = new ArrayList();
    boolean toggle = true;

    Map<Tab, TableView> mapOverTabAndTableView = new HashMap<>();
    Map<Tab, Table> mapOverTabAndTable = new HashMap<>();

    public MainFXMLController() {

        visualize = new Visualize();
        tablesList = new ArrayList<Table>();

    }

    public ArrayList<Table> getTablesList() {
        return this.tablesList;
    }

    TextField txtIP = new TextField();
    TextField txtPort = new TextField();
    TextField txtInstance = new TextField();
    ChoiceBox choiceBoxTables = new ChoiceBox();
    int userSelectedTableInTabPane;

    String whichHelpView;
    Dialog dlg;
    public int tabPaneCounter = 0;
    static Stage stage;
    @FXML
    private Label label;
    @FXML
    private Button btnNewSeries;
    @FXML
    private Button btnNewChart;

    @FXML
    Label visualizeLabel;
    @FXML
    private LineChart lineChart;

    @FXML
    private BarChart barChart;
    @FXML
    private Button btnNewConnection;
    @FXML
    private Button btnConnectedTables;
    @FXML
    private Button btnVisualize;

    @FXML
    private Button btnCombine;

    @FXML
    PieChart pieChart;

    @FXML
    StackedAreaChart areaChart;
    @FXML
    ScatterChart scatterChart;

    @FXML
    Button visualizeButton;

    @FXML
    Button newConnectionButton;

    @FXML
    Button combineButton;
    final Tooltip tooltip = new Tooltip();

    @FXML
    AnchorPane anchorPaneTables;

    @FXML
    AnchorPane anchorPaneVisualize;

    @FXML
    AnchorPane anchorPaneCombine;

    @FXML
    SplitPane splitPane;

    @FXML
    TabPane tabPane;

    @FXML
    Separator separator;
    @FXML
    ImageView imageView;

    @FXML
    ComboBox<Column> comboBox;
    List<Kolonne> listOfCombinedColumns = new ArrayList<Kolonne>();
    List<String> listOfColumnNames = new ArrayList<String>();

    TreeItem<String> kombinerteKolonnerRoot = new TreeItem<String>("List of combined columns");

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/helpme.fxml"));

    HelpScreenController helpScreenController;

    @FXML
    BorderPane borderPane;
    @FXML
    VBox vBoxMenu;
    @FXML
    Button btnMenu;

    @FXML
    private void btnRemoveFilters(ActionEvent event
    ) {

        if (tabPane.getSelectionModel().getSelectedItem() != null) {
            mapOverTabAndTable.get(tabPane.getSelectionModel().getSelectedItem()).removeFilters();
        }

    }

    @FXML
    public void goToColumn(ActionEvent event) {

        if (!comboBox.getSelectionModel().isEmpty()) {
            Column selectedColumn = comboBox.getSelectionModel().getSelectedItem();

            FadeTransition ft = new FadeTransition(Duration.millis(300), selectedColumn.getGraphic());
            ft.setFromValue(10.0);
            ft.setToValue(0.0);
            ft.setCycleCount(6);
            ft.setAutoReverse(true);

            ft.play();

            // 
            mapOverTabAndTableView.get(tabPane.getSelectionModel().getSelectedItem()).scrollToColumn(selectedColumn);

        }
    }

    @FXML
    private void visualizeButton(ActionEvent event) throws UnsupportedEncodingException, IOException {
        DataInsight datainsight = new DataInsight();
       datainsight.getInsight(0, 1, tabPane, mapOverTabAndTable);

    }

    @FXML
    private void btnNewChart(ActionEvent event
    ) {
        if (pieChart.visibleProperty().get()) {
            showLinearWizard("pieChart", false);
        }
        if (lineChart.visibleProperty().get()) {
            showLinearWizard("lineChart", false);
        }
        if (barChart.visibleProperty().get()) {
            showLinearWizard("barChart", false);
        }
        if (areaChart.visibleProperty().get()) {
            showLinearWizard("areaChart", false);
        }
        if (scatterChart.visibleProperty().get()) {
            showLinearWizard("scatterChart", false);
        }

    }

    @FXML
    private void btnNewSeries(ActionEvent event
    ) {
        if (pieChart.visibleProperty().get()) {
            showLinearWizard("pieChart", true);
        }
        if (lineChart.visibleProperty().get()) {
            showLinearWizard("lineChart", true);
        }
        if (barChart.visibleProperty().get()) {
            showLinearWizard("barChart", true);
        }
        if (areaChart.visibleProperty().get()) {
            showLinearWizard("areaChart", true);
        }
        if (scatterChart.visibleProperty().get()) {
            showLinearWizard("scatterChart", true);
        }

    }

    @FXML
    private void handleDataButton(ActionEvent event
    ) {
        setVisibleView("tableView");
        whichHelpView = "tableView";
    }

    @FXML
    private void combineButton(ActionEvent event
    ) throws IOException {
        openNew();

    }

    public void setVisibleView(String whichView) {
        if (whichView == "visualizeView") {

            anchorPaneVisualize.setVisible(true);
            anchorPaneTables.setVisible(false);

        }

        if (whichView == "tableView") {
            anchorPaneVisualize.setVisible(false);
            anchorPaneTables.setVisible(true);

        }

    }

    @FXML
    private void newConnectionButton(ActionEvent event) throws IOException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        setVisibleView("tableView");
        //  openDialogWithSQLConnectionInfo();
        createTabPaneWithTable("salesline");

    }

    @FXML
    private void btnHelp(ActionEvent event) throws IOException {

        loadHelpScreen();

    }

    void loadHelpScreen() throws IOException {

        helpScreenController = fxmlLoader.getController();
        helpScreenController.setValue(whichHelpView);

        stage.showAndWait();

    }

    private void showLinearWizard(String whichVisualizationType, Boolean newSeries) {
        Table table = mapOverTabAndTable.get(tabPane.getSelectionModel().getSelectedItem());

        ComboBox choiceBoxNames = new ComboBox();
        new SelectKeyComboBoxListener(choiceBoxNames);

        for (Kolonne kolonne : table.listofColumns) {
            choiceBoxNames.getItems().add(kolonne.NAVN);
        }

        ComboBox choiceBoxValues = new ComboBox();
        new SelectKeyComboBoxListener(choiceBoxValues);

        for (Kolonne kolonne : table.listofColumns) {
            choiceBoxValues.getItems().add(kolonne.NAVN);

        }

        Wizard wizard = new Wizard();

        wizard.setTitle("Connection Wizard");

        // --- page 1
        int row = 0;

        GridPane page1Grid = new GridPane();
        page1Grid.setVgap(10);
        page1Grid.setHgap(30);

        page1Grid.add(new Label("Check which field that represent the categories: "), 0, row);

        wizard.getValidationSupport().registerValidator(choiceBoxNames, Validator.createEmptyValidator("You must select what represents names"));
        page1Grid.add(choiceBoxNames, 1, row++);

        page1Grid.add(new Label("Check which field that represent the values: "), 0, row);

        wizard.getValidationSupport().registerValidator(choiceBoxValues, Validator.createEmptyValidator("You must select what represents values"));
        page1Grid.add(choiceBoxValues, 1, row);

        Wizard.WizardPane page1 = new Wizard.WizardPane();
        page1.setHeaderText("Please select your columns for visualizing");
        page1.setContent(page1Grid);

        wizard.setFlow(new LinearFlow(page1));

        // show wizard and wait for response
        wizard.showAndWait().ifPresent(result -> {
            if (result == ButtonType.FINISH) {
                int en = choiceBoxNames.getSelectionModel().getSelectedIndex();
                int to = choiceBoxValues.getSelectionModel().getSelectedIndex();
                try {

                    if (whichVisualizationType == "barChart") {

                        visualize.getBarChartData(en, to, tabPane, mapOverTabAndTable, barChart, newSeries);

                        System.out.println("true");

                    } else if (whichVisualizationType == "pieChart") {

                        visualize.getPieChartData(en, to, tabPane, mapOverTabAndTable, pieChart, label, newSeries);
                    } else if (whichVisualizationType == "lineChart") {

                        visualize.getLineChartData(en, to, tabPane, mapOverTabAndTable, lineChart, newSeries);
                    } else if (whichVisualizationType == "areaChart") {

                        visualize.getAreaChartData(en, to, tabPane, mapOverTabAndTable, areaChart, newSeries);
                    } else if (whichVisualizationType == "scatterChart") {

                        visualize.getScatterChartData(en, to, tabPane, mapOverTabAndTable, scatterChart, newSeries);
                    }

                } catch (Exception e) {
                    ErrorDialog("Invalid columns detected", "The first column has to be a text column, and the second one has to contain numbers");

                }

            }

        }
        );

    }

    @FXML
    private void btnExportChartToPNG(ActionEvent event) throws IOException {
        if (barChart.visibleProperty().get()) {
            ChartToPng chartToPNG = new ChartToPng();
            WritableImage image = barChart.snapshot(new SnapshotParameters(), null);

            chartToPNG.saveChartToPNG(image, stage);

        }
        if (pieChart.visibleProperty().get()) {
            ChartToPng chartToPNG = new ChartToPng();
            WritableImage image = pieChart.snapshot(new SnapshotParameters(), null);

            chartToPNG.saveChartToPNG(image, stage);

        }

        if (lineChart.visibleProperty().get()) {
            ChartToPng chartToPNG = new ChartToPng();
            WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);

            chartToPNG.saveChartToPNG(image, stage);

        }

    }

    @FXML
    private void barChartButton(ActionEvent event) {
        visualizeLabel.setText("Bar Chart");
        lineChart.setVisible(false);
        pieChart.setVisible(false);
        barChart.setVisible(true);
        areaChart.setVisible(false);
        scatterChart.setVisible(false);

    }

    @FXML
    private void pieChartButton(ActionEvent event) {
        visualizeLabel.setText("Pie Chart");
        lineChart.setVisible(false);
        pieChart.setVisible(true);
        barChart.setVisible(false);
        areaChart.setVisible(false);
        scatterChart.setVisible(false);

    }

    @FXML
    private void areaChartButton(ActionEvent event) {
        visualizeLabel.setText("Area Chart");
        lineChart.setVisible(false);
        pieChart.setVisible(false);
        barChart.setVisible(false);
        areaChart.setVisible(true);
        scatterChart.setVisible(false);

    }

    @FXML
    private void lineChartButton(ActionEvent event) {
        visualizeLabel.setText("Line Chart");
        lineChart.setVisible(true);
        pieChart.setVisible(false);
        barChart.setVisible(false);
        areaChart.setVisible(false);
        scatterChart.setVisible(false);

    }

    @FXML
    private void scatterChartButton(ActionEvent event) {
        visualizeLabel.setText("Scatter Chart");
        lineChart.setVisible(false);
        pieChart.setVisible(false);
        barChart.setVisible(false);
        areaChart.setVisible(false);
        scatterChart.setVisible(true);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new SelectKeyComboBoxListener(comboBox);

        try {
            root = fxmlLoader.load();
            stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOpacity(1);
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root));

        } catch (IOException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //TEMPORARY, FOR Å SLIPPE Å SKRIVE INN TILKOBLING HVER GANG
        try {
            sql_manager.getConnection("localhost", "8889", "test");

        } catch (SQLException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MainFXMLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //TEMPORARY, FOR Å SLIPPE Å SKRIVE INN TILKOBLING HVER GANG
        btnNewSeries.setVisible(true);
        btnNewChart.setVisible(true);

        SideBar sidebar = new SideBar(btnMenu, 90, vBoxMenu);

        borderPane.setLeft(sidebar);

        btnConnectedTables.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnCombine.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        btnVisualize.disableProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));
        Image noTables = new Image(
                getClass().getResourceAsStream("/Icons/no_tables.png"));
        imageView.setImage(noTables);
        imageView.visibleProperty().bind(Bindings.size(tabPane.getTabs()).isEqualTo(0));

    }

    public void createTabPaneWithTable(String whichTable) throws SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        //Hver gang brukeren kobler til en ny tabell, lager vi en ny tabpane
        //dette for å kunne organisere tabeller og vite hvilken rekkefølge de er i
        VBox vBox = new VBox();

        Table tabellen = new Table(whichTable + "@" + sql_manager.instanceName);

        //  String query = textField.getText();
        //her skjer oppkoblingen
        //laster inn dataen med en query
        tabellen.loadData("select * from " + whichTable + "  ", tabellen, tabPaneCounter);

        //legger til den nye tilkoblede tabellen i listen over tilkoblede tabeller
        tablesList.add(tabPaneCounter, tabellen);

        TableView tableViewet = new TableView();
        listOfTableViews.add(tableViewet);

        Label lbl = new Label("Number of rows : " + tabellen.numberofRows);
        AnchorPane anchorPane = new AnchorPane(lbl);

        anchorPane.setRightAnchor(lbl,
                5.0);
        //legger til tableviewet i tabben
        vBox.getChildren()
                .addAll(tableViewet, anchorPane);
        tableViewet = tabellen.fillTableView(tableViewet, tabellen);

        vBox.setId(
                "" + tabPaneCounter);

        Tab tab = new Tab(whichTable
                + "@" + sql_manager.instanceName);

        tab.setOnClosed(new EventHandler<javafx.event.Event>() {
            @Override
            public void handle(javafx.event.Event e) {
                tablesList.remove(tabellen);
                tabPane.getTabs().remove(tab);
            }
        });

        tab.setContent(vBox);

        tabPane.getTabs()
                .add(tab);
        mapOverTabAndTableView.put(tab, tableViewet);
        mapOverTabAndTable.put(tab, tabellen);
        System.out.println(tab);
        System.out.println(tabPane.getTabs().get(tabPaneCounter));
        tabPaneCounter++;

        tab.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov, Boolean old_val, Boolean new_val) {

                if (new_val.equals(Boolean.TRUE)) {

                    comboBox.getItems().clear();
                    comboBox.getItems().addAll(mapOverTabAndTableView.get(tabPane.getSelectionModel().getSelectedItem()).getColumns());
                    comboBox.setValue(null);
                    System.out.println("true");

                }

            }
        });

        tabPane.getSelectionModel()
                .select(tab);
        comboBox.getItems().addAll(mapOverTabAndTableView.get(tabPane.getSelectionModel().getSelectedItem()).getColumns());

    }

    private void openNew() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/View/CombineColumns.fxml"));
        Parent root = fxmlLoader.load();
        CombineColumnsController c = (CombineColumnsController) fxmlLoader.getController();
        Stage stage = new Stage();
        ((CombineColumnsController) fxmlLoader.getController()).setContext(tablesList, mapOverTabAndTableView, mapOverTabAndTable);
        c.myTabPane = tabPane;
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setScene(new Scene(root));
        stage.show();

    }

}

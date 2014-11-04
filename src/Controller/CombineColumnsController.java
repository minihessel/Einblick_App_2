/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Model.ErrorDialog.ErrorDialog;
import Model.Kolonne;
import Model.Table;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eskil Hesselroth
 */
public class CombineColumnsController implements Initializable {

    @FXML
    TextField textField;

    TabPane myTabPane;
    @FXML
    ListView<Kolonne> listView;
    Table combinedTable = new Table("combined table");
    final ObservableList<Kolonne> data = FXCollections.observableArrayList();
    List<Table> myList;
    Map<Tab, TableView> mapOverTabAndTableViews;
    Map<Tab, Table> mapOverTabAndTable;
    List<ComboBox> choiceBoxList = new ArrayList();

    @FXML
    VBox vBox;

    @FXML
    Button btnFinish;

    @FXML
    private void btnAdd(ActionEvent event
    ) {
        int atLeastTwoTablesSelected = 0;

        for (ComboBox cb : choiceBoxList) {
            if (!cb.getSelectionModel().isEmpty()) {
                atLeastTwoTablesSelected++;
            }
        }
        if (atLeastTwoTablesSelected > 1 && !textField.getText().isEmpty()) {
            Kolonne kol = new Kolonne(textField.getText(), combinedTable, true, combinedTable.listofColumns.size() + 1);
            kol = new Kolonne(textField.getText(), combinedTable, true, combinedTable.listofColumns.size() + 1);
            for (ComboBox cb : choiceBoxList) {
                if (!cb.getSelectionModel().isEmpty()) {
                    Kolonne addCol = myList.get(Integer.parseInt(cb.getUserData().toString())).listofColumns.get(cb.getSelectionModel().getSelectedIndex());
                    kol.listOfColumns.add(addCol);
                }
            }

            combinedTable.listofColumns.add(kol);

            listView.setItems(FXCollections.observableArrayList(combinedTable.listofColumns));
        } else if (textField.getText().isEmpty()) {
            ErrorDialog("Your new column need a new name", "Please fill in the name of your new column in the textfield");

        } else {

            ErrorDialog("Not enough columns selected", "You haven't selected enough columns. A combined column got to contain at least two columns ");

        }

    }

    @FXML
    private void btnFinish(ActionEvent event
    ) {

        if (!combinedTable.listofColumns.isEmpty()) {
            TableView tableViewCombined = new TableView();

            tableViewCombined = combinedTable.fillTableView(tableViewCombined, combinedTable);
            Tab tab = new Tab("combined table");
            tab.setOnClosed(new EventHandler<javafx.event.Event>() {
                @Override
                public void handle(javafx.event.Event e) {
                    myList.remove(combinedTable);
                    myTabPane.getTabs().remove(tab);
                }
            });
            tab.setContent(tableViewCombined);
            System.out.println(myTabPane.getTabs().size());
            myList.add(combinedTable);
            myTabPane.getTabs().add(tab);
            // myTabPane.getSelectionModel().selectLast();
            tab.setId("" + myTabPane.getTabs().size() + 1);
            mapOverTabAndTableViews.put(tab, tableViewCombined);
            mapOverTabAndTable.put(tab, combinedTable);
            Stage stage = (Stage) btnFinish.getScene().getWindow();
            // do what you have to do
            stage.close();

            //to select the last tab that has been selected
        } else {
            ErrorDialog("No columns created", "There are no columns created, can not continue. Please create atleast one combined column");
        }

    }

    @FXML
    private void btnCancel(ActionEvent event
    ) {

        Stage stage = (Stage) btnFinish.getScene().getWindow();
        // do what you have to do
        stage.close();

        //to select the last tab that has been selected
    }

    public void setContext(List<Table> tablesList, Map mapOverTabAndTableViews, Map mapOverTabAndTables) {
        this.myList = tablesList;
        this.mapOverTabAndTable = mapOverTabAndTables;
        this.mapOverTabAndTableViews = mapOverTabAndTableViews;

        for (Table tbl : myList) {
            FlowPane pane = new FlowPane();
            ImageView imageView = new ImageView(new Image(
                    getClass().getResourceAsStream("/Icons/arrow_down.png")));
            // pane.setPrefSize(20, 40);
            pane.setStyle("-fx-border-color: black;-fx-border-width:0.1px;");
            Label lbl = new Label("Table : " + tbl.NAVN);

            ComboBox cb = new ComboBox();
            new SelectKeyComboBoxListener(cb);
            cb.setUserData(tbl.tableNumber);
            choiceBoxList.add(cb);
            for (Kolonne kol : tbl.listofColumns) {
                cb.getItems().add(kol.NAVN);
            }
            pane.setOrientation(Orientation.VERTICAL);
            pane.setHgap(2);

            pane.getChildren().addAll(lbl, cb);
            pane.getChildren().add(imageView);

            vBox.getChildren().add(pane);
        }

        // initialize country dependent data here rather then in initialize()
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        final ContextMenu contextMenu = new ContextMenu();

        MenuItem delete = new MenuItem("Delete");

        contextMenu.getItems().addAll(delete);

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {

                if (t.getTarget() instanceof Text) {

                    if (t.getButton() == MouseButton.SECONDARY) {
                        delete.setOnAction(new EventHandler() {
                            public void handle(Event t) {
                                combinedTable.listofColumns.remove(listView.getSelectionModel().getSelectedItem());
                                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());

                            }

                        });
                        Node n = (Node) t.getTarget();
                        contextMenu.show(n, t.getScreenX(), t.getScreenY());
                    }

                }
            }
        });
    }

}

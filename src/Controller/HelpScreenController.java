/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Eskil Hesselroth
 */
public class HelpScreenController implements Initializable {

    @FXML
    ImageView imageView;
    @FXML
    Button btnNext;
    int counter = 1;
    List<Image> listofImages = new ArrayList();

    StringProperty whichHelpScreen = new SimpleStringProperty();

    public String getValue() {
        return whichHelpScreen.getValue();
    }

    public void setValue(String whichHelpScreen) {
        this.whichHelpScreen.set(whichHelpScreen);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        whichHelpScreen.addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {

                if (newValue != null && newValue.equals("tableView")) {
                    loadTableViewHelpScreen();

                } else if (newValue != null && newValue.equals("combineView")) {
                    Image image1 = new Image(
                            getClass().getResourceAsStream("/Icons/combineView/1.jpg"));
                    Image image2 = new Image(
                            getClass().getResourceAsStream("/Icons/combineView/2.jpg"));

                    Image image3 = new Image(
                            getClass().getResourceAsStream("/Icons/combineView/3.jpg"));

                    Image image4 = new Image(
                            getClass().getResourceAsStream("/Icons/combineView/4.jpg"));

                    listofImages.add(image1);
                    listofImages.add(image2);
                    listofImages.add(image3);
                    listofImages.add(image4);

                    imageView.setImage(listofImages.get(0));

                } else if (newValue != null && newValue.equals("visualizeView")) {
                    Image image1 = new Image(
                            getClass().getResourceAsStream("/Icons/visualizeView/1.jpg"));
                    Image image2 = new Image(
                            getClass().getResourceAsStream("/Icons/visualizeView/2.jpg"));

                    Image image3 = new Image(
                            getClass().getResourceAsStream("/Icons/visualizeView/3.jpg"));

                    listofImages.add(image1);
                    listofImages.add(image2);
                    listofImages.add(image3);

                    imageView.setImage(listofImages.get(0));
                }

            }

            private void loadTableViewHelpScreen() {
                Image image1 = new Image(
                        getClass().getResourceAsStream("/Icons/tableView/1.jpg"));
                Image image2 = new Image(
                        getClass().getResourceAsStream("/Icons/tableView/2.jpg"));

                Image image3 = new Image(
                        getClass().getResourceAsStream("/Icons/tableView/3.jpg"));

                listofImages.add(image1);
                listofImages.add(image2);
                listofImages.add(image3);
                imageView.setImage(listofImages.get(0));
            }
        });

    }

    @FXML
    private void btnNext(ActionEvent event) {

        if (counter < listofImages.size()) {

            imageView.setImage(listofImages.get(counter));
            counter++;
            if (counter == listofImages.size()) {
                Image close = new Image(
                        getClass().getResourceAsStream("/Icons/close.png"));
                btnNext.setGraphic(new ImageView(close));
                btnNext.setText(null);

            }

        } else {
            listofImages.clear();
            MainFXMLController.stage.close();
            btnNext.setGraphic(null);
            btnNext.setText("Got it!");
            counter = 0;
        }

    }

}

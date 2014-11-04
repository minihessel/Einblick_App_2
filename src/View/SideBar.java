/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * 
 * 
 * Built up on https://gist.github.com/jewelsea/1437374
  /**
     * Animates a node on and off screen to the left.
     */
   public class SideBar extends VBox {


        /**
         * @return a control button to hide and show the sidebar
         */
        /**
         * creates a sidebar containing a vertical alignment of the given nodes
         */
        public SideBar(Button btn,final double expandedWidth, Node... nodes ) {
          
           
            getStyleClass().add("sidebar");
            this.setPrefWidth(expandedWidth);
            this.setMinWidth(0);

            // create a bar to hide and show.
            setAlignment(Pos.CENTER);
            getChildren().addAll(nodes);

            // create a button to hide and show the sidebar.
             btn.getStyleClass().add("hide-left");

            // apply the animations when the button is pressed.
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // create an animation to hide sidebar.
                    final Animation hideSidebar = new Transition() {
                        {
                            setCycleDuration(Duration.millis(250));
                        }

                        protected void interpolate(double frac) {
                            final double curWidth = expandedWidth * (1.0 - frac);
                            setPrefWidth(curWidth);
                            setTranslateX(-expandedWidth + curWidth);
                        }
                    };
                    hideSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            setVisible(false);

                            btn.getStyleClass().remove("hide-left");
                            btn.getStyleClass().add("show-right");
                        }
                    });

                    // create an animation to show a sidebar.
                    final Animation showSidebar = new Transition() {
                        {
                            setCycleDuration(Duration.millis(250));
                        }

                        protected void interpolate(double frac) {
                            final double curWidth = expandedWidth * frac;
                            setPrefWidth(curWidth);
                            setTranslateX(-expandedWidth + curWidth);
                        }
                    };
                    showSidebar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {

                            btn.getStyleClass().add("hide-left");
                            btn.getStyleClass().remove("show-right");
                        }
                    });

                    if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
                        if (isVisible()) {
                            hideSidebar.play();
                        } else {
                            setVisible(true);
                            showSidebar.play();
                        }
                    }
                }
            });
        }
    }
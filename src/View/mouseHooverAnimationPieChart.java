package View;

import Controller.MainFXMLController;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * Kode bygget vidre og tilpasset en del på http://tomsondev.bestsolution.at/2012/11/21/animating-the-javafx-piechart-a-bit/
 *
 */
public abstract class mouseHooverAnimationPieChart implements Initializable {

    public static class MouseHoverAnimation implements EventHandler<MouseEvent> {

        static final Duration ANIMATION_DURATION = new Duration(500);
        static final double ANIMATION_DISTANCE = 0.15;
        private double cos;
        private double sin;
        private PieChart chart;
        private Label label;

        public MouseHoverAnimation(PieChart.Data d, PieChart chart) {
            MainFXMLController fx = new MainFXMLController();
        
            this.chart = chart;
            double start = 0;
            double angle = calcAngle(d);

            for (PieChart.Data tmp : chart.getData()) {

                if (tmp == d) {

                    break;

                }
                start += calcAngle(tmp);

            }

            cos = Math.cos(Math.toRadians(0 - start - angle / 2));
            sin = Math.sin(Math.toRadians(0 - start - angle / 2));
        }

        @Override
        public void handle(MouseEvent arg0) {
            Node n = (Node) arg0.getSource();
       
            if ("clicked".equals(n.getId())) {
                TranslateTransitionBuilder.create().toX(0).toY(0).duration(new Duration(500)).node(n).build().play();
                n.setId(null);
                n.setStyle(" ");

            } else {

                double minX = Double.MAX_VALUE;
                double maxX = Double.MAX_VALUE * -1;

                for (PieChart.Data d : chart.getData()) {
                    Node na = (Node) d.getNode();
                    TranslateTransitionBuilder.create().toX(0).toY(0).duration(new Duration(500)).node(na).build().play();
                    na.setId(" ");
                    minX = Math.min(minX, d.getNode().getBoundsInParent().getMinX());
                    maxX = Math.max(maxX, d.getNode().getBoundsInParent().getMaxX());
                    na.setBlendMode(BlendMode.SRC_OVER);
                }
                n.setId("clicked");

                double radius = maxX - minX;
                TranslateTransitionBuilder.create().toX((radius * ANIMATION_DISTANCE) * cos).toY((radius * ANIMATION_DISTANCE) * sin).duration(ANIMATION_DURATION).node(n).build().play();

            }

        }

        private static double calcAngle(PieChart.Data d) {
            double total = 0;
            for (PieChart.Data tmp : d.getChart().getData()) {
                total += tmp.getPieValue();

            }
            return 360 * (d.getPieValue() / total);
        }
    }

}

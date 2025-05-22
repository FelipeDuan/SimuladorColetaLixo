package simulador.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Representa visualmente uma zona no mapa, com retângulo colorido e label.
 */
public class ZonaView {
    private final String nome;
    private final Rectangle forma;
    private final Label rotulo;

    public ZonaView(String nome, String corHex, double x, double y, Pane mapa) {
        this.nome = nome;

        forma = new Rectangle(150, 100, Color.web(corHex));
        forma.setArcWidth(12);
        forma.setArcHeight(12);
        forma.setLayoutX(x);
        forma.setLayoutY(y);

        rotulo = new Label("Zona " + nome);
        rotulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        rotulo.setLayoutX(x + 40);
        rotulo.setLayoutY(y + 40);

        mapa.getChildren().addAll(forma, rotulo);
    }

    public double getCenterX() {
        return forma.getLayoutX() + forma.getWidth() / 2;
    }

    public double getCenterY() {
        return forma.getLayoutY() + forma.getHeight() / 2;
    }

    public String getNome() {
        return nome;
    }
}

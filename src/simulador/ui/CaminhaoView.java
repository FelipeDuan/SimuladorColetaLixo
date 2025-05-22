package simulador.ui;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class CaminhaoView {

    private final String id;
    private final Rectangle forma;
    private final Label rotulo;

    private int cargaAtual = 0;
    private int cargaMaxima = 2;
    private String status = "";

    public CaminhaoView(String id) {
        this.id = id;

        this.forma = new Rectangle(80, 30, Color.DARKGREEN);
        this.forma.setArcWidth(8);
        this.forma.setArcHeight(8);
        this.forma.setLayoutX(50);
        this.forma.setLayoutY(50);

        this.rotulo = new Label();
        this.rotulo.setFont(Font.font("Arial", 12));
        this.rotulo.setLayoutX(50);
        this.rotulo.setLayoutY(35);

        atualizarRotulo();
    }

    public void moverPara(Point2D destino, Duration tempo) {
        TranslateTransition tt = new TranslateTransition(tempo, forma);
        tt.setToX(destino.getX() - forma.getLayoutX());
        tt.setToY(destino.getY() - forma.getLayoutY());
        tt.play();

        TranslateTransition ttLabel = new TranslateTransition(tempo, rotulo);
        ttLabel.setToX(destino.getX() - rotulo.getLayoutX());
        ttLabel.setToY(destino.getY() - rotulo.getLayoutY() - 15);
        ttLabel.play();
    }

    public void simularColeta(Duration duracao) {
        mudarCor("coleta");
        setStatus("coletando...");
        PauseTransition pausa = new PauseTransition(duracao);
        pausa.setOnFinished(e -> mudarCor("idle"));
        pausa.play();
    }

    public void atualizarCarga(int atual, int maxima) {
        this.cargaAtual = atual;
        this.cargaMaxima = maxima;
        atualizarRotulo();
    }

    public void setStatus(String texto) {
        this.status = texto;
        atualizarRotulo();
    }

    private void atualizarRotulo() {
        this.rotulo.setText(id + " | " + cargaAtual + "/" + cargaMaxima + "t\n" + status);
    }

    public void mudarCor(String fase) {
        switch (fase) {
            case "coleta" -> forma.setFill(Color.ORANGE);
            case "transferencia" -> forma.setFill(Color.DEEPSKYBLUE);
            case "descarregando" -> forma.setFill(Color.GREY);
            case "finalizado" -> forma.setFill(Color.LIGHTGREEN);
            case "idle" -> forma.setFill(Color.DARKGREEN);
            default -> forma.setFill(Color.BLACK);
        }
    }

    public Rectangle getForma() {
        return forma;
    }

    public Label getRotulo() {
        return rotulo;
    }
}

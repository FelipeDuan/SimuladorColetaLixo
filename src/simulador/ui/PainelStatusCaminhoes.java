package simulador.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * Painel lateral que exibe o status atual de todos os caminhões na simulação.
 * <p>
 * Fornece informações em tempo real sobre cada caminhão, incluindo seu identificador,
 * status atual, carga e capacidade.
 */
public class PainelStatusCaminhoes {
    private final VBox container;
    private final Map<String, HBox> linhasCaminhoes = new HashMap<>();

    /**
     * Cria um novo painel de status dos caminhões.
     *
     * @param parent painel pai onde o painel de status será adicionado
     */
    public PainelStatusCaminhoes(Pane parent) {
        container = new VBox(5);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 5;");
        container.setLayoutX(10);
        container.setLayoutY(10);
        container.setPrefWidth(200);

        Label titulo = new Label("Status dos Caminhões");
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        container.getChildren().add(titulo);

        parent.getChildren().add(container);
    }

    /**
     * Adiciona ou atualiza as informações de um caminhão no painel.
     *
     * @param id         identificador do caminhão
     * @param status     descrição do status atual
     * @param cargaAtual carga atual em toneladas
     * @param capacidade capacidade máxima em toneladas
     * @param cor        cor associada ao estado atual
     */
    public void adicionarOuAtualizarCaminhao(String id, String status, int cargaAtual, int capacidade, String cor) {
        Platform.runLater(() -> {
            if (linhasCaminhoes.containsKey(id)) {
                atualizarLinhaCaminhao(id, status, cargaAtual, capacidade, cor);
            } else {
                criarLinhaCaminhao(id, status, cargaAtual, capacidade, cor);
            }
        });
    }

    /**
     * Cria uma nova linha para um caminhão no painel.
     *
     * @param id         identificador do caminhão
     * @param status     descrição do status atual
     * @param cargaAtual carga atual em toneladas
     * @param capacidade capacidade máxima em toneladas
     * @param cor        cor associada ao estado atual
     */
    private void criarLinhaCaminhao(String id, String status, int cargaAtual, int capacidade, String cor) {
        HBox linha = new HBox(5);
        linha.setAlignment(Pos.CENTER_LEFT);

        Rectangle indicador = new Rectangle(10, 10, Color.web(cor));
        Label idLabel = new Label(id);
        idLabel.setStyle("-fx-font-weight: bold;");
        Label statusLabel = new Label(status);
        statusLabel.setPrefWidth(100);
        Label cargaLabel = new Label(cargaAtual + "/" + capacidade + "T");

        linha.getChildren().addAll(indicador, idLabel, statusLabel, cargaLabel);
        container.getChildren().add(linha);
        linhasCaminhoes.put(id, linha);
    }

    /**
     * Atualiza uma linha existente de um caminhão no painel.
     *
     * @param id         identificador do caminhão
     * @param status     descrição do status atual
     * @param cargaAtual carga atual em toneladas
     * @param capacidade capacidade máxima em toneladas
     * @param cor        cor associada ao estado atual
     */
    private void atualizarLinhaCaminhao(String id, String status, int cargaAtual, int capacidade, String cor) {
        HBox linha = linhasCaminhoes.get(id);
        Rectangle indicador = (Rectangle) linha.getChildren().get(0);
        Label statusLabel = (Label) linha.getChildren().get(2);
        Label cargaLabel = (Label) linha.getChildren().get(3);

        indicador.setFill(Color.web(cor));
        statusLabel.setText(status);
        cargaLabel.setText(cargaAtual + "/" + capacidade + "T");
    }
}

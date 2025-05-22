package simulador.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Representa visualmente uma zona no mapa, com retângulo colorido, label e indicadores de lixo.
 * <p>
 * Esta classe foi aprimorada para incluir visualização dinâmica do lixo acumulado
 * através de uma barra de progresso e indicador numérico.
 */
public class ZonaView {
    private final String nome;
    private final Rectangle forma;
    private final Label rotulo;

    // Novos componentes para visualização de lixo
    private final ProgressBar barraLixo;
    private final Label valorLixo;

    // Capacidade máxima de lixo para cálculo de proporção na barra
    private int capacidadeMaxima = 100; // Valor padrão, pode ser ajustado

    /**
     * Cria uma nova representação visual de zona com indicadores de lixo.
     *
     * @param nome    nome da zona
     * @param corHex  cor em formato hexadecimal (ex: "#A5D6A7")
     * @param x       posição X no mapa
     * @param y       posição Y no mapa
     * @param mapa    painel onde a zona será adicionada
     */
    public ZonaView(String nome, String corHex, double x, double y, Pane mapa) {
        this.nome = nome;

        // Forma principal da zona
        forma = new Rectangle(150, 100, Color.web(corHex));
        forma.setArcWidth(12);
        forma.setArcHeight(12);
        forma.setLayoutX(x);
        forma.setLayoutY(y);
        forma.setId("zona_" + nome);

        // Rótulo com nome da zona
        rotulo = new Label("Zona " + nome);
        rotulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        rotulo.setLayoutX(x + 40);
        rotulo.setLayoutY(y + 30);

        // Barra de progresso para visualizar lixo
        barraLixo = new ProgressBar(0);
        barraLixo.setPrefWidth(120);
        barraLixo.setLayoutX(x + 15);
        barraLixo.setLayoutY(y + 70);
        barraLixo.setStyle("-fx-accent: #8B4513;"); // Cor marrom para lixo
        barraLixo.setId("barra_" + nome);

        // Indicador numérico de lixo
        valorLixo = new Label("0T");
        valorLixo.setLayoutX(x + 65);
        valorLixo.setLayoutY(y + 50);
        valorLixo.setId("lixo_" + nome);

        // Adiciona todos os componentes ao mapa
        mapa.getChildren().addAll(forma, rotulo, barraLixo, valorLixo);
    }

    /**
     * Atualiza a visualização do lixo acumulado na zona.
     *
     * @param quantidade       quantidade atual de lixo em toneladas
     * @param capacidadeMaxima capacidade máxima de referência para cálculo da proporção
     */
    public void atualizarLixo(int quantidade, int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
        double proporcao = Math.min(1.0, (double) quantidade / capacidadeMaxima);

        Platform.runLater(() -> {
            barraLixo.setProgress(proporcao);
            valorLixo.setText(quantidade + "T");

            // Mudar cor conforme nível de lixo
            if (proporcao > 0.8) {
                barraLixo.setStyle("-fx-accent: #FF0000;"); // Vermelho (crítico)
                valorLixo.setStyle("-fx-text-fill: #FF0000; -fx-font-weight: bold;");
            } else if (proporcao > 0.5) {
                barraLixo.setStyle("-fx-accent: #FFA500;"); // Laranja (alerta)
                valorLixo.setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
            } else {
                barraLixo.setStyle("-fx-accent: #8B4513;"); // Marrom (normal)
                valorLixo.setStyle("-fx-text-fill: #000000;");
            }
        });
    }

    /**
     * Retorna a coordenada X do centro da zona.
     *
     * @return coordenada X do centro
     */
    public double getCenterX() {
        return forma.getLayoutX() + forma.getWidth() / 2;
    }

    /**
     * Retorna a coordenada Y do centro da zona.
     *
     * @return coordenada Y do centro
     */
    public double getCenterY() {
        return forma.getLayoutY() + forma.getHeight() / 2;
    }

    /**
     * Retorna o nome da zona.
     *
     * @return nome da zona
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a forma retangular que representa a zona.
     *
     * @return retângulo da zona
     */
    public Rectangle getForma() {
        return forma;
    }

    /**
     * Retorna o rótulo da zona.
     *
     * @return label com o nome da zona
     */
    public Label getRotulo() {
        return rotulo;
    }

    /**
     * Retorna a barra de progresso que representa o lixo acumulado.
     *
     * @return barra de progresso
     */
    public ProgressBar getBarraLixo() {
        return barraLixo;
    }

    /**
     * Retorna o indicador numérico de lixo.
     *
     * @return label com a quantidade de lixo
     */
    public Label getValorLixo() {
        return valorLixo;
    }
}

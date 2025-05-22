package simulador.ui;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Representa visualmente um caminhão no mapa, com design mais realista e informações detalhadas.
 * <p>
 * Esta classe foi aprimorada para incluir um visual mais realista de caminhão,
 * melhor exibição de informações e sincronização com o tempo da simulação.
 */
public class CaminhaoView {

    private final String id;
    private final Group grupoVisual;
    private final Rectangle cabine;
    private final Rectangle caçamba;
    private final Circle rodaDianteira;
    private final Circle rodaTraseira;
    private final Polygon seta;
    private final Label rotulo;
    private final Text infoTexto;
    private final Rectangle barraProgresso;
    private final Rectangle fundoBarra;

    private int cargaAtual = 0;
    private int cargaMaxima = 2;
    private String status = "";
    private double velocidadeAnimacao = 1.0;

    /**
     * Cria uma nova representação visual de caminhão com design mais realista.
     *
     * @param id identificador do caminhão (ex: "C2", "C4", "C8")
     */
    public CaminhaoView(String id) {
        this.id = id;

        // Grupo para conter todos os elementos visuais do caminhão
        grupoVisual = new Group();
        grupoVisual.setId("caminhao_" + id);

        // Cabine do caminhão
        cabine = new Rectangle(25, 20, Color.DARKBLUE);
        cabine.setArcWidth(5);
        cabine.setArcHeight(5);
        cabine.setStroke(Color.BLACK);
        cabine.setStrokeWidth(1);

        // Caçamba do caminhão
        caçamba = new Rectangle(40, 20, Color.DARKGREEN);
        caçamba.setLayoutX(25);
        caçamba.setStroke(Color.BLACK);
        caçamba.setStrokeWidth(1);

        // Rodas
        rodaDianteira = new Circle(10, 25, 5, Color.BLACK);
        rodaTraseira = new Circle(50, 25, 5, Color.BLACK);

        // Seta direcional
        seta = new Polygon(
            0, 0,
            10, 5,
            0, 10
        );
        seta.setFill(Color.YELLOW);
        seta.setVisible(false);
        seta.setLayoutX(65);
        seta.setLayoutY(10);

        // Rótulo com ID do caminhão
        rotulo = new Label(id);
        rotulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        rotulo.setTextFill(Color.WHITE);
        rotulo.setLayoutX(5);
        rotulo.setLayoutY(2);

        // Texto de informações
        infoTexto = new Text();
        infoTexto.setFont(Font.font("Arial", 10));
        infoTexto.setFill(Color.WHITE);
        infoTexto.setLayoutX(0);
        infoTexto.setLayoutY(-10);

        // Barra de progresso para carga
        fundoBarra = new Rectangle(60, 5, Color.DARKGRAY);
        fundoBarra.setLayoutY(30);

        barraProgresso = new Rectangle(0, 5, Color.LIMEGREEN);
        barraProgresso.setLayoutY(30);

        // Adicionar todos os elementos ao grupo
        grupoVisual.getChildren().addAll(
            caçamba, cabine, rodaDianteira, rodaTraseira,
            seta, rotulo, infoTexto, fundoBarra, barraProgresso
        );

        // Configurar tooltip para informações detalhadas
        Tooltip tooltip = new Tooltip();
        Tooltip.install(grupoVisual, tooltip);

        // Atualizar tooltip quando o mouse passar sobre o caminhão
        grupoVisual.setOnMouseEntered(e -> {
            tooltip.setText(
                "ID: " + id + "\n" +
                "Carga: " + cargaAtual + "/" + cargaMaxima + "t\n" +
                "Status: " + status
            );
            tooltip.show(grupoVisual, e.getScreenX(), e.getScreenY() + 15);
        });

        grupoVisual.setOnMouseExited(e -> tooltip.hide());

        // Extrair capacidade do ID (assumindo formato "C2", "C4", "C8")
        try {
            String capacidadeStr = id.substring(1);
            cargaMaxima = Integer.parseInt(capacidadeStr);
        } catch (Exception e) {
            cargaMaxima = 2; // Valor padrão
        }

        atualizarVisual();
    }

    /**
     * Move o caminhão para uma nova posição com animação sincronizada com o tempo da simulação.
     *
     * @param destino posição de destino
     * @param tempo duração da animação
     */
    public void moverPara(Point2D destino, Duration tempo) {
        // Ajustar duração com base na velocidade da simulação
        Duration duracaoAjustada = tempo.multiply(velocidadeAnimacao);

        // Calcular direção do movimento para orientar o caminhão
        double origemX = grupoVisual.getLayoutX();
        double origemY = grupoVisual.getLayoutY();
        double destinoX = destino.getX();
        double destinoY = destino.getY();

        // Determinar se o caminhão está indo para a direita ou esquerda
        boolean indoDireita = destinoX > origemX;

        // Virar o caminhão na direção do movimento
        if (!indoDireita) {
            // Virar para a esquerda
            grupoVisual.setScaleX(-1);
            infoTexto.setScaleX(-1); // Manter texto legível
            rotulo.setScaleX(-1);    // Manter texto legível
        } else {
            // Virar para a direita (padrão)
            grupoVisual.setScaleX(1);
            infoTexto.setScaleX(1);
            rotulo.setScaleX(1);
        }

        // Mostrar seta na direção do movimento
        seta.setVisible(true);

        // Animar movimento
        TranslateTransition tt = new TranslateTransition(duracaoAjustada, grupoVisual);
        tt.setToX(destino.getX() - grupoVisual.getLayoutX());
        tt.setToY(destino.getY() - grupoVisual.getLayoutY());

        // Animar rodas durante o movimento
        RotateTransition rtRodaDianteira = new RotateTransition(duracaoAjustada, rodaDianteira);
        rtRodaDianteira.setByAngle(360 * 5); // Girar várias vezes

        RotateTransition rtRodaTraseira = new RotateTransition(duracaoAjustada, rodaTraseira);
        rtRodaTraseira.setByAngle(360 * 5); // Girar várias vezes

        // Iniciar animações
        tt.play();
        rtRodaDianteira.play();
        rtRodaTraseira.play();

        // Esconder seta quando movimento terminar
        tt.setOnFinished(e -> seta.setVisible(false));
    }

    /**
     * Simula visualmente a coleta de lixo com animação.
     *
     * @param duracao duração da animação
     */
    public void simularColeta(Duration duracao) {
        Duration duracaoAjustada = duracao.multiply(velocidadeAnimacao);

        mudarCor("coleta");
        setStatus("coletando...");

        // Efeito visual de coleta (elevação da caçamba)
        TranslateTransition ttCaçamba = new TranslateTransition(Duration.seconds(0.5), caçamba);
        ttCaçamba.setByY(-5);
        ttCaçamba.setCycleCount(2);
        ttCaçamba.setAutoReverse(true);
        ttCaçamba.play();

        // Pulsar o caminhão durante coleta
        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), grupoVisual);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.setCycleCount(4);
        scale.setAutoReverse(true);
        scale.play();

        PauseTransition pausa = new PauseTransition(duracaoAjustada);
        pausa.setOnFinished(e -> mudarCor("idle"));
        pausa.play();
    }

    /**
     * Simula visualmente a descarga de lixo com animação.
     *
     * @param duracao duração da animação
     */
    public void simularDescarga(Duration duracao) {
        Duration duracaoAjustada = duracao.multiply(velocidadeAnimacao);

        mudarCor("descarregando");
        setStatus("descarregando...");

        // Efeito visual de descarga (inclinação da caçamba)
        RotateTransition rtCaçamba = new RotateTransition(Duration.seconds(1), caçamba);
        rtCaçamba.setByAngle(30);
        rtCaçamba.setCycleCount(2);
        rtCaçamba.setAutoReverse(true);
        rtCaçamba.play();

        // Vibrar levemente o caminhão durante descarga
        TranslateTransition ttVibrar = new TranslateTransition(Duration.millis(50), grupoVisual);
        ttVibrar.setByX(2);
        ttVibrar.setCycleCount(10);
        ttVibrar.setAutoReverse(true);
        ttVibrar.play();

        PauseTransition pausa = new PauseTransition(duracaoAjustada);
        pausa.setOnFinished(e -> mudarCor("idle"));
        pausa.play();
    }

    /**
     * Atualiza a visualização da carga atual do caminhão.
     *
     * @param atual carga atual em toneladas
     * @param maxima capacidade máxima em toneladas
     */
    public void atualizarCarga(int atual, int maxima) {
        this.cargaAtual = atual;
        this.cargaMaxima = maxima;

        Platform.runLater(() -> {
            // Atualizar barra de progresso
            double proporcao = (double) atual / maxima;
            barraProgresso.setWidth(Math.max(0, proporcao * fundoBarra.getWidth()));

            // Mudar cor da barra conforme nível de carga
            if (proporcao > 0.8) {
                barraProgresso.setFill(Color.RED);
            } else if (proporcao > 0.5) {
                barraProgresso.setFill(Color.ORANGE);
            } else {
                barraProgresso.setFill(Color.LIMEGREEN);
            }

            // Atualizar visual completo
            atualizarVisual();
        });
    }

    /**
     * Define o status atual do caminhão.
     *
     * @param texto descrição do status atual
     */
    public void setStatus(String texto) {
        this.status = texto;
        atualizarVisual();
    }

    /**
     * Define a velocidade de animação relativa ao tempo da simulação.
     *
     * @param fator fator de velocidade (1.0 = normal, 0.5 = metade da velocidade, 2.0 = dobro da velocidade)
     */
    public void setVelocidadeAnimacao(double fator) {
        this.velocidadeAnimacao = fator;
    }

    /**
     * Atualiza o visual completo do caminhão com informações atuais.
     */
    private void atualizarVisual() {
        Platform.runLater(() -> {
            // Atualizar texto de informações
            infoTexto.setText(cargaAtual + "/" + cargaMaxima + "t");

            // Ajustar tamanho da caçamba conforme capacidade
            if (cargaMaxima <= 2) {
                caçamba.setWidth(40);
            } else if (cargaMaxima <= 4) {
                caçamba.setWidth(50);
            } else {
                caçamba.setWidth(60);
            }

            // Reposicionar roda traseira conforme tamanho da caçamba
            rodaTraseira.setCenterX(caçamba.getWidth() + 15);

            // Reposicionar seta conforme tamanho do caminhão
            seta.setLayoutX(caçamba.getWidth() + 25);

            // Ajustar tamanho da barra de progresso
            fundoBarra.setWidth(caçamba.getWidth() + 20);
        });
    }

    /**
     * Muda a cor do caminhão conforme seu estado atual.
     *
     * @param fase estado atual do caminhão
     */
    public void mudarCor(String fase) {
        Platform.runLater(() -> {
            switch (fase) {
                case "coleta" -> {
                    caçamba.setFill(Color.ORANGE);
                    cabine.setFill(Color.DARKBLUE);
                }
                case "transferencia" -> {
                    caçamba.setFill(Color.DEEPSKYBLUE);
                    cabine.setFill(Color.DARKBLUE);
                }
                case "descarregando" -> {
                    caçamba.setFill(Color.GREY);
                    cabine.setFill(Color.DARKBLUE);
                }
                case "finalizado" -> {
                    caçamba.setFill(Color.LIGHTGREEN);
                    cabine.setFill(Color.DARKBLUE);
                }
                case "idle" -> {
                    caçamba.setFill(Color.DARKGREEN);
                    cabine.setFill(Color.DARKBLUE);
                }
                default -> {
                    caçamba.setFill(Color.BLACK);
                    cabine.setFill(Color.DARKBLUE);
                }
            }
        });
    }

    /**
     * Retorna o grupo visual que contém todos os elementos do caminhão.
     *
     * @return grupo visual do caminhão
     */
    public Group getForma() {
        return grupoVisual;
    }

    /**
     * Retorna o rótulo do caminhão.
     *
     * @return label com o identificador
     */
    public Label getRotulo() {
        return rotulo;
    }

    /**
     * Retorna o identificador do caminhão.
     *
     * @return identificador (ex: "C2", "C4", "C8")
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o status atual do caminhão.
     *
     * @return descrição do status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retorna a capacidade máxima do caminhão.
     *
     * @return capacidade em toneladas
     */
    public int getCapacidadeMaxima() {
        return cargaMaxima;
    }

    /**
     * Retorna a carga atual do caminhão.
     *
     * @return carga atual em toneladas
     */
    public int getCargaAtual() {
        return cargaAtual;
    }
}

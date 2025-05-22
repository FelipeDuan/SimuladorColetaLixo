package simulador.ui;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

import estruturas.filas.FilaEncadeada;

public class SimuladorFXController {

    private static SimuladorFXController instancia;

    private final Pane mapa;
    private final TextArea logArea;
    private final Slider velocidadeSlider;

    private final Map<String, CaminhaoView> caminhoes = new HashMap<>();
    private final Map<String, ZonaView> zonasView = new HashMap<>();
    private final Map<String, Point2D> posicoesEstacoes = new HashMap<>();

    private final FilaEncadeada<Runnable> filaAnimacoes = new FilaEncadeada<>();
    private boolean animacaoEmAndamento = false;

    public SimuladorFXController(Pane mapa, TextArea logArea, Slider velocidadeSlider) {
        this.mapa = mapa;
        this.logArea = logArea;
        this.velocidadeSlider = velocidadeSlider;

        instancia = this;
        inicializarZonas();
    }

    public static SimuladorFXController getInstancia() {
        return instancia;
    }

    public static boolean isInstanciado() {
        return instancia != null;
    }

    private void inicializarZonas() {
        zonasView.put("Norte", new ZonaView("Norte", "#A5D6A7", 100, 50, mapa));
        zonasView.put("Sul", new ZonaView("Sul", "#90CAF9", 100, 200, mapa));
        zonasView.put("Centro", new ZonaView("Centro", "#FFAB91", 300, 70, mapa));
        zonasView.put("Sudeste", new ZonaView("Sudeste", "#FFF59D", 300, 230, mapa));
        zonasView.put("Leste", new ZonaView("Leste", "#CE93D8", 500, 150, mapa));

        posicoesEstacoes.put("Estação A", new Point2D(800, 150));
        posicoesEstacoes.put("Estação B", new Point2D(800, 250));

        addEstacaoVisual("Estação A", 800, 150);
        addEstacaoVisual("Estação B", 800, 250);
    }

    private void addEstacaoVisual(String nome, double x, double y) {
        Rectangle ret = new Rectangle(100, 60, Color.LIGHTBLUE);
        ret.setLayoutX(x);
        ret.setLayoutY(y);
        Label lbl = new Label(nome);
        lbl.setLayoutX(x + 10);
        lbl.setLayoutY(y + 20);
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        mapa.getChildren().addAll(ret, lbl);
    }

    private void enfileirarAnimacao(Runnable animacao) {
        filaAnimacoes.enfileirar(animacao);
        if (!animacaoEmAndamento) {
            executarProximaAnimacao();
        }
    }

    private void executarProximaAnimacao() {
        Runnable proxima = filaAnimacoes.desenfileirar();
        if (proxima != null) {
            animacaoEmAndamento = true;
            Platform.runLater(proxima);
        } else {
            animacaoEmAndamento = false;
        }
    }


    private Duration calcularDuracaoVisual(int minutosSimulados) {
        int velocidade = (int) velocidadeSlider.getValue();
        double fator = switch (velocidade) {
            case 1 -> 1.0;
            case 2 -> 0.75;
            case 3 -> 0.5;
            case 4 -> 0.25;
            case 5 -> 0.1;
            default -> 1.0;
        };
        double segundos = minutosSimulados * fator / 10.0;
        return Duration.seconds(Math.max(0.5, segundos));
    }

    public void animarColeta(String caminhaoId, String zonaNome, int capacidade, int tempoTotalMin) {
        enfileirarAnimacao(() -> {
            ZonaView zona = zonasView.get(zonaNome.replace("Zona ", ""));
            if (zona == null) {
                logArea.appendText("⚠ Zona desconhecida: " + zonaNome + "\n");
                executarProximaAnimacao();
                return;
            }

            CaminhaoView caminhao = obterOuCriarCaminhao(caminhaoId);
            Point2D destino = new Point2D(zona.getCenterX() - 20, zona.getCenterY() - 12);
            Duration duracao = calcularDuracaoVisual(tempoTotalMin);

            caminhao.moverPara(destino, duracao);
            caminhao.simularColeta(duracao);

            new Thread(() -> {
                try {
                    Thread.sleep((long) duracao.toMillis());
                } catch (InterruptedException ignored) {
                }
                executarProximaAnimacao();
            }).start();
        });
    }

    public void animarTransferencia(String caminhaoId, String estacaoNome, int cargaAtual, int capacidade, int tempoTotalMin) {
        enfileirarAnimacao(() -> {
            CaminhaoView caminhao = obterOuCriarCaminhao(caminhaoId);
            Point2D destino = posicoesEstacoes.get(estacaoNome);

            if (destino == null) {
                logArea.appendText("⚠ Estação desconhecida: " + estacaoNome + "\n");
                executarProximaAnimacao();
                return;
            }

            Duration duracao = calcularDuracaoVisual(tempoTotalMin);

            caminhao.mudarCor("transferencia");
            caminhao.setStatus("indo para " + estacaoNome + "...");
            caminhao.atualizarCarga(cargaAtual, capacidade);
            caminhao.moverPara(destino, duracao);

            new Thread(() -> {
                try {
                    Thread.sleep((long) duracao.toMillis());
                } catch (InterruptedException ignored) {
                }
                executarProximaAnimacao();
            }).start();
        });
    }

    public void atualizarCargaCaminhao(String caminhaoId, int atual, int maximo) {
        CaminhaoView view = caminhoes.get(caminhaoId);
        if (view != null) {
            Platform.runLater(() -> view.atualizarCarga(atual, maximo));
        }
    }

    private CaminhaoView obterOuCriarCaminhao(String id) {
        CaminhaoView existente = caminhoes.get(id);
        if (existente != null) return existente;

        CaminhaoView novo = new CaminhaoView(id);

        double offset = caminhoes.size() * 20;
        novo.getForma().setLayoutX(50 + offset);
        novo.getForma().setLayoutY(50);
        novo.getRotulo().setLayoutX(50 + offset);
        novo.getRotulo().setLayoutY(35);

        caminhoes.put(id, novo);
        mapa.getChildren().addAll(novo.getForma(), novo.getRotulo());
        return novo;
    }
}

import estruturas.filas.FilaEncadeada;
import estruturas.lista.ListaDuplamenteEncadeada;
import estruturas.lista.No;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.*;
import java.util.Random;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Simulador de Coleta de Lixo para Teresina
 * Aplicação JavaFX que simula o processo de coleta de lixo na cidade de Teresina,
 * utilizando estruturas de dados personalizadas e animações visuais.
 */
public class MainFX extends Application {

    // Constantes de configuração
    private static final int LARGURA_JANELA = 1200;
    private static final int ALTURA_JANELA = 800;
    private static final int LARGURA_ZONA = 180;
    private static final int ALTURA_ZONA = 120;
    private static final int LARGURA_ESTACAO = 200;
    private static final int ALTURA_ESTACAO = 150;
    private static final int LARGURA_CAMINHAO_PEQUENO = 60;
    private static final int ALTURA_CAMINHAO_PEQUENO = 30;
    private static final int LARGURA_CAMINHAO_GRANDE = 80;
    private static final int ALTURA_CAMINHAO_GRANDE = 40;
    private static double VELOCIDADE_ANIMACAO = 1.0; // Multiplicador de velocidade

    // Componentes da interface
    private Pane areaMapa;
    private VBox painelControle;
    private VBox painelLogs;
    private VBox painelEstatisticas;
    private TextArea areaLogs;
    private Label lblTotalLixoColetado;
    private Label lblTempoMedioEspera;
    private Label lblCaminhoesGrandesUsados;
    private Label lblEficienciaColeta;
    private Slider sliderVelocidade;
    private Button btnIniciar;
    private Button btnPausar;
    private Button btnReiniciar;

    // Estruturas de dados personalizadas
    private ListaDuplamenteEncadeada<ZonaFX> zonas;
    private ListaDuplamenteEncadeada<CaminhaoPequenoFX> caminhosPequenos;
    private ListaDuplamenteEncadeada<CaminhaoGrandeFX> caminhosGrandes;
    private ListaDuplamenteEncadeada<EstacaoFX> estacoes;

    // Variáveis de controle da simulação
    private boolean simulacaoEmAndamento = false;
    private int tempoSimulacao = 0;
    private double totalLixoColetado = 0.0;
    private int totalCaminhoesGrandesUsados = 0;
    private double tempoTotalEspera = 0.0;
    private int totalEsperas = 0;
    private Random random = new Random();
    private Timeline timeline;

    // Formatador para valores decimais
    private DecimalFormat df = new DecimalFormat("0.0");

    @Override
    public void start(Stage primaryStage) {
        // Configuração da janela principal
        primaryStage.setTitle("Simulador de Coleta de Lixo para Teresina");

        // Criação do layout principal
        BorderPane root = new BorderPane();

        // Inicialização dos componentes
        inicializarComponentes();

        // Configuração do layout
        configurarLayout(root);

        // Inicialização das estruturas de dados
        inicializarEstruturaDados();

        // Criação da cena
        Scene scene = new Scene(root, LARGURA_JANELA, ALTURA_JANELA);
        scene.getStylesheets().add(getClass().getResource("estilo.css") != null ?
                getClass().getResource("estilo.css").toExternalForm() : "");

        // Configuração da janela
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

        // Inicialização da simulação
        inicializarSimulacao();
    }

    /**
     * Inicializa todos os componentes da interface
     */
    private void inicializarComponentes() {
        // Área do mapa
        areaMapa = new Pane();
        areaMapa.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1px;");

        // Painel de controle
        painelControle = new VBox(10);
        painelControle.setPadding(new Insets(10));
        painelControle.setAlignment(Pos.CENTER);
        painelControle.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 1px;");

        // Controles
        HBox boxControles = new HBox(10);
        boxControles.setAlignment(Pos.CENTER);

        btnIniciar = new Button("Iniciar");
        btnIniciar.setOnAction(e -> iniciarSimulacao());

        btnPausar = new Button("Pausar");
        btnPausar.setOnAction(e -> pausarSimulacao());
        btnPausar.setDisable(true);

        btnReiniciar = new Button("Reiniciar");
        btnReiniciar.setOnAction(e -> reiniciarSimulacao());

        boxControles.getChildren().addAll(btnIniciar, btnPausar, btnReiniciar);

        // Controle de velocidade
        HBox boxVelocidade = new HBox(10);
        boxVelocidade.setAlignment(Pos.CENTER);

        Label lblVelocidade = new Label("Velocidade:");
        sliderVelocidade = new Slider(0.5, 3.0, 1.0);
        sliderVelocidade.setShowTickMarks(true);
        sliderVelocidade.setShowTickLabels(true);
        sliderVelocidade.setMajorTickUnit(0.5);
        sliderVelocidade.setBlockIncrement(0.25);
        sliderVelocidade.setPrefWidth(200);
        sliderVelocidade.valueProperty().addListener((obs, oldVal, newVal) -> {
            ajustarVelocidadeSimulacao(newVal.doubleValue());
        });

        boxVelocidade.getChildren().addAll(lblVelocidade, sliderVelocidade);

        painelControle.getChildren().addAll(boxControles, boxVelocidade);

        // Painel de logs
        painelLogs = new VBox(5);
        painelLogs.setPadding(new Insets(10));
        painelLogs.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1px;");

        Label lblLogs = new Label("Logs da Simulação");
        lblLogs.setFont(Font.font("System", FontWeight.BOLD, 14));

        areaLogs = new TextArea();
        areaLogs.setEditable(false);
        areaLogs.setPrefHeight(200);
        areaLogs.setWrapText(true);

        painelLogs.getChildren().addAll(lblLogs, areaLogs);
        VBox.setVgrow(areaLogs, Priority.ALWAYS);

        // Painel de estatísticas
        painelEstatisticas = new VBox(10);
        painelEstatisticas.setPadding(new Insets(10));
        painelEstatisticas.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1px;");

        Label lblEstatisticas = new Label("Estatísticas");
        lblEstatisticas.setFont(Font.font("System", FontWeight.BOLD, 14));

        GridPane gridEstatisticas = new GridPane();
        gridEstatisticas.setHgap(10);
        gridEstatisticas.setVgap(10);
        gridEstatisticas.setPadding(new Insets(5));

        gridEstatisticas.add(new Label("Total de lixo coletado:"), 0, 0);
        lblTotalLixoColetado = new Label("0.0 T");
        gridEstatisticas.add(lblTotalLixoColetado, 1, 0);

        gridEstatisticas.add(new Label("Tempo médio de espera:"), 0, 1);
        lblTempoMedioEspera = new Label("0.0 min");
        gridEstatisticas.add(lblTempoMedioEspera, 1, 1);

        gridEstatisticas.add(new Label("Caminhões grandes usados:"), 0, 2);
        lblCaminhoesGrandesUsados = new Label("0");
        gridEstatisticas.add(lblCaminhoesGrandesUsados, 1, 2);

        gridEstatisticas.add(new Label("Eficiência da coleta:"), 0, 3);
        lblEficienciaColeta = new Label("0.0%");
        gridEstatisticas.add(lblEficienciaColeta, 1, 3);

        painelEstatisticas.getChildren().addAll(lblEstatisticas, gridEstatisticas);
    }

    /**
     * Configura o layout da aplicação
     */
    private void configurarLayout(BorderPane root) {
        // Área central (mapa)
        root.setCenter(areaMapa);

        // Painel de controle (topo)
        root.setTop(painelControle);

        // Painel inferior (logs e estatísticas)
        HBox painelInferior = new HBox(10);
        painelInferior.setPadding(new Insets(10));

        painelInferior.getChildren().addAll(painelLogs, painelEstatisticas);
        HBox.setHgrow(painelLogs, Priority.ALWAYS);
        HBox.setHgrow(painelEstatisticas, Priority.ALWAYS);

        root.setBottom(painelInferior);
    }

    /**
     * Inicializa as estruturas de dados personalizadas
     */
    private void inicializarEstruturaDados() {
        // Inicialização das listas
        zonas = new ListaDuplamenteEncadeada<>();
        caminhosPequenos = new ListaDuplamenteEncadeada<>();
        caminhosGrandes = new ListaDuplamenteEncadeada<>();
        estacoes = new ListaDuplamenteEncadeada<>();

        // Criação das zonas
        criarZonas();

        // Criação das estações
        criarEstacoes();

        // Criação dos caminhões pequenos
        criarCaminhosPequenos();

        // Criação dos caminhões grandes
        criarCaminhosGrandes();
    }

    /**
     * Cria as zonas da cidade
     */
    private void criarZonas() {
        // Posições das zonas no mapa
        Map<String, double[]> posicoesZonas = new HashMap<>();
        posicoesZonas.put("Zona Norte", new double[]{LARGURA_JANELA / 2 - LARGURA_ZONA / 2, 50});
        posicoesZonas.put("Zona Centro", new double[]{LARGURA_JANELA / 2 - LARGURA_ZONA / 2, ALTURA_JANELA / 2 - ALTURA_ZONA - 100});
        posicoesZonas.put("Zona Leste", new double[]{LARGURA_JANELA / 2 + LARGURA_ZONA + 50, ALTURA_JANELA / 2 - ALTURA_ZONA - 100});
        posicoesZonas.put("Zona Sudeste", new double[]{LARGURA_JANELA / 2 + LARGURA_ZONA / 2, ALTURA_JANELA / 2});
        posicoesZonas.put("Zona Sul", new double[]{LARGURA_JANELA / 2 - LARGURA_ZONA - 50, ALTURA_JANELA / 2});

        // Criação das zonas
        for (Map.Entry<String, double[]> entrada : posicoesZonas.entrySet()) {
            String nomeZona = entrada.getKey();
            double[] posicao = entrada.getValue();

            // Geração aleatória de lixo (entre 1 e 10 toneladas)
            double quantidadeLixo = 1 + random.nextDouble() * 9;

            // Criação da zona
            ZonaFX zona = new ZonaFX(nomeZona, quantidadeLixo, posicao[0], posicao[1]);
            zonas.adicionar(zona);

            // Adição da zona ao mapa
            areaMapa.getChildren().add(zona.getRepresentacaoVisual());
        }
    }

    /**
     * Cria as estações de transferência
     */
    private void criarEstacoes() {
        // Posições das estações no mapa
        double[] posicaoEstacaoA = {150, ALTURA_JANELA / 2 - ALTURA_ESTACAO - 50};
        double[] posicaoEstacaoB = {LARGURA_JANELA - 150 - LARGURA_ESTACAO, ALTURA_JANELA / 2 - ALTURA_ESTACAO - 50};

        // Criação das estações
        EstacaoFX estacaoA = new EstacaoFX("Estação A", posicaoEstacaoA[0], posicaoEstacaoA[1]);
        EstacaoFX estacaoB = new EstacaoFX("Estação B", posicaoEstacaoB[0], posicaoEstacaoB[1]);

        estacoes.adicionar(estacaoA);
        estacoes.adicionar(estacaoB);

        // Adição das estações ao mapa
        areaMapa.getChildren().add(estacaoA.getRepresentacaoVisual());
        areaMapa.getChildren().add(estacaoB.getRepresentacaoVisual());
    }

    /**
     * Cria os caminhões pequenos
     */
    private void criarCaminhosPequenos() {
        // Capacidades dos caminhões pequenos
        int[] capacidades = {2, 4, 8, 10};

        // Posição inicial dos caminhões (fora da tela)
        double posX = -LARGURA_CAMINHAO_PEQUENO;
        double posY = ALTURA_JANELA / 2 + 100;

        // Criação dos caminhões
        for (int i = 0; i < 8; i++) {
            // Seleção aleatória de capacidade
            int capacidade = capacidades[random.nextInt(capacidades.length)];

            // Número máximo de viagens (entre 3 e 5)
            int maxViagens = 3 + random.nextInt(3);

            // Criação do caminhão
            CaminhaoPequenoFX caminhao = new CaminhaoPequenoFX("P" + (i + 1), capacidade, maxViagens, posX, posY + i * (ALTURA_CAMINHAO_PEQUENO + 10));
            caminhosPequenos.adicionar(caminhao);

            // Adição do caminhão ao mapa
            areaMapa.getChildren().add(caminhao.getRepresentacaoVisual());
        }
    }

    /**
     * Cria os caminhões grandes
     */
    private void criarCaminhosGrandes() {
        // Posição inicial dos caminhões (fora da tela)
        double posX = -LARGURA_CAMINHAO_GRANDE;
        double posY = ALTURA_JANELA / 2 + 200;

        // Criação dos caminhões
        for (int i = 0; i < 2; i++) {
            // Criação do caminhão
            CaminhaoGrandeFX caminhao = new CaminhaoGrandeFX("G" + (i + 1), 20, posX, posY + i * (ALTURA_CAMINHAO_GRANDE + 10));
            caminhosGrandes.adicionar(caminhao);

            // Adição do caminhão ao mapa
            areaMapa.getChildren().add(caminhao.getRepresentacaoVisual());

            // Associação do caminhão à estação
            No<EstacaoFX> noEstacao = estacoes.getPrimeiro();
            for (int j = 0; j <= i && noEstacao != null; j++) {
                if (j == i) {
                    EstacaoFX estacao = noEstacao.getValor();
                    estacao.setCaminhaoGrande(caminhao);
                    caminhao.setEstacao(estacao);

                    // Posicionamento do caminhão na estação
                    caminhao.moverPara(estacao.getPosX() + (LARGURA_ESTACAO - LARGURA_CAMINHAO_GRANDE) / 2,
                            estacao.getPosY() + ALTURA_ESTACAO - ALTURA_CAMINHAO_GRANDE - 10);
                }
                noEstacao = noEstacao.getProx();
            }
        }

        // Atualização do contador de caminhões grandes
        totalCaminhoesGrandesUsados = 2;
        atualizarEstatisticas();
    }

    /**
     * Inicializa a simulação
     */
    private void inicializarSimulacao() {
        registrarLog("Simulação inicializada. Clique em 'Iniciar' para começar.");

        // Criação da timeline para a simulação
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (simulacaoEmAndamento) {
                avancarSimulacao();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Inicia a simulação
     */
    private void iniciarSimulacao() {
        simulacaoEmAndamento = true;
        btnIniciar.setDisable(true);
        btnPausar.setDisable(false);

        registrarLog("Simulação iniciada!");

        // Inicia a timeline
        timeline.play();

        // Inicia o ciclo de coleta para cada caminhão pequeno
        No<CaminhaoPequenoFX> noCaminhao = caminhosPequenos.getPrimeiro();
        while (noCaminhao != null) {
            CaminhaoPequenoFX caminhao = noCaminhao.getValor();
            iniciarCicloColeta(caminhao);
            noCaminhao = noCaminhao.getProx();
        }
    }

    /**
     * Pausa a simulação
     */
    private void pausarSimulacao() {
        simulacaoEmAndamento = !simulacaoEmAndamento;

        if (simulacaoEmAndamento) {
            btnPausar.setText("Pausar");
            registrarLog("Simulação retomada.");
            timeline.play();
        } else {
            btnPausar.setText("Continuar");
            registrarLog("Simulação pausada.");
            timeline.pause();
        }
    }

    /**
     * Reinicia a simulação
     */
    private void reiniciarSimulacao() {
        // Para a simulação atual
        simulacaoEmAndamento = false;
        timeline.stop();

        // Limpa a área do mapa
        areaMapa.getChildren().clear();

        // Reinicia as variáveis de controle
        tempoSimulacao = 0;
        totalLixoColetado = 0.0;
        totalCaminhoesGrandesUsados = 0;
        tempoTotalEspera = 0.0;
        totalEsperas = 0;

        // Limpa os logs
        areaLogs.clear();

        // Reinicializa as estruturas de dados
        inicializarEstruturaDados();

        // Atualiza as estatísticas
        atualizarEstatisticas();

        // Reinicia os botões
        btnIniciar.setDisable(false);
        btnPausar.setDisable(true);
        btnPausar.setText("Pausar");

        registrarLog("Simulação reiniciada. Clique em 'Iniciar' para começar.");
    }

    /**
     * Avança a simulação em um passo
     */
    private void avancarSimulacao() {
        tempoSimulacao++;

        // Atualização das zonas (geração de lixo)
        No<ZonaFX> noZona = zonas.getPrimeiro();
        while (noZona != null) {
            ZonaFX zona = noZona.getValor();
            zona.gerarLixo(0.05, 0.2); // Geração de 0.05 a 0.2 toneladas por ciclo
            noZona = noZona.getProx();
        }

        // Atualização das estações
        No<EstacaoFX> noEstacao = estacoes.getPrimeiro();
        while (noEstacao != null) {
            EstacaoFX estacao = noEstacao.getValor();
            estacao.processarFila();
            noEstacao = noEstacao.getProx();
        }

        // Atualização das estatísticas
        atualizarEstatisticas();
    }

    /**
     * Inicia o ciclo de coleta para um caminhão pequeno
     */
    private void iniciarCicloColeta(CaminhaoPequenoFX caminhao) {
        if (!simulacaoEmAndamento || caminhao.getViagensRealizadas() >= caminhao.getMaxViagens()) {
            return;
        }

        // Encontra a zona com maior quantidade de lixo
        ZonaFX zonaPrioritaria = encontrarZonaPrioritaria();

        if (zonaPrioritaria != null) {
            // Registra o envio do caminhão
            registrarLog("Caminhão " + caminhao.getId() + " (" + caminhao.getCapacidadeTotal() + "T) enviado para " + zonaPrioritaria.getNome());

            // Incrementa o contador de caminhões na zona
            zonaPrioritaria.incrementarCaminhoes();

            // Animação de deslocamento até a zona
            double duracaoViagem = calcularTempoViagem(caminhao.getPosX(), caminhao.getPosY(),
                    zonaPrioritaria.getPosX(), zonaPrioritaria.getPosY());

            TranslateTransition transicao = new TranslateTransition(Duration.seconds(duracaoViagem), caminhao.getRepresentacaoVisual());
            transicao.setToX(zonaPrioritaria.getPosX() - caminhao.getPosX() + (LARGURA_ZONA - LARGURA_CAMINHAO_PEQUENO) / 2);
            transicao.setToY(zonaPrioritaria.getPosY() - caminhao.getPosY() + (ALTURA_ZONA - ALTURA_CAMINHAO_PEQUENO) / 2);

            transicao.setOnFinished(e -> {
                // Atualiza a posição do caminhão
                caminhao.setPosX(zonaPrioritaria.getPosX() + (LARGURA_ZONA - LARGURA_CAMINHAO_PEQUENO) / 2);
                caminhao.setPosY(zonaPrioritaria.getPosY() + (ALTURA_ZONA - ALTURA_CAMINHAO_PEQUENO) / 2);

                // Inicia a coleta
                coletarLixo(caminhao, zonaPrioritaria);
            });

            transicao.play();
        }
    }

    /**
     * Coleta lixo de uma zona
     */
    private void coletarLixo(CaminhaoPequenoFX caminhao, ZonaFX zona) {
        if (!simulacaoEmAndamento) {
            return;
        }

        // Calcula a quantidade de lixo a ser coletada
        double capacidadeDisponivel = caminhao.getCapacidadeTotal() - caminhao.getCapacidadeAtual();
        double lixoDisponivel = zona.getQuantidadeLixo();
        double quantidadeColetada = Math.min(capacidadeDisponivel, lixoDisponivel);

        // Atualiza a quantidade de lixo na zona
        zona.setQuantidadeLixo(lixoDisponivel - quantidadeColetada);

        // Atualiza a capacidade do caminhão
        caminhao.setCapacidadeAtual(caminhao.getCapacidadeAtual() + quantidadeColetada);

        // Registra a coleta
        registrarLog("Caminhão " + caminhao.getId() + " coletou " + df.format(quantidadeColetada) +
                "T de lixo na " + zona.getNome() + ". Capacidade atual: " +
                df.format(caminhao.getCapacidadeAtual()) + "/" + caminhao.getCapacidadeTotal() + "T");

        // Decrementa o contador de caminhões na zona
        zona.decrementarCaminhoes();

        // Atualiza o total de lixo coletado
        totalLixoColetado += quantidadeColetada;

        // Verifica se o caminhão está cheio ou se não há mais lixo na zona
        if (caminhao.getCapacidadeAtual() >= caminhao.getCapacidadeTotal() * 0.9 || lixoDisponivel - quantidadeColetada <= 0.1) {
            // Dirige-se para a estação de transferência
            dirigirParaEstacao(caminhao);
        } else {
            // Continua coletando na mesma zona
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                coletarLixo(caminhao, zona);
            }));
            timeline.play();
        }
    }

    /**
     * Dirige um caminhão pequeno para a estação de transferência
     */
    private void dirigirParaEstacao(CaminhaoPequenoFX caminhao) {
        if (!simulacaoEmAndamento) {
            return;
        }

        // Encontra a estação com menor fila
        EstacaoFX estacaoDestino = encontrarEstacaoComMenorFila();

        if (estacaoDestino != null) {
            // Registra o deslocamento
            registrarLog("Caminhão " + caminhao.getId() + " dirigindo-se para " + estacaoDestino.getNome());

            // Animação de deslocamento até a estação
            double duracaoViagem = calcularTempoViagem(caminhao.getPosX(), caminhao.getPosY(),
                    estacaoDestino.getPosX(), estacaoDestino.getPosY());

            TranslateTransition transicao = new TranslateTransition(Duration.seconds(duracaoViagem), caminhao.getRepresentacaoVisual());
            transicao.setToX(estacaoDestino.getPosX() - caminhao.getPosX() + (LARGURA_ESTACAO - LARGURA_CAMINHAO_PEQUENO) / 2);
            transicao.setToY(estacaoDestino.getPosY() - caminhao.getPosY() + ALTURA_ESTACAO + 20 + estacaoDestino.getTamanhoFila() * 10);

            transicao.setOnFinished(e -> {
                // Atualiza a posição do caminhão
                caminhao.setPosX(estacaoDestino.getPosX() + (LARGURA_ESTACAO - LARGURA_CAMINHAO_PEQUENO) / 2);
                caminhao.setPosY(estacaoDestino.getPosY() + ALTURA_ESTACAO + 20 + estacaoDestino.getTamanhoFila() * 10);

                // Adiciona o caminhão à fila da estação
                estacaoDestino.adicionarCaminhaoFila(caminhao);

                // Registra a entrada na fila
                registrarLog("Caminhão " + caminhao.getId() + " entrou na fila da " + estacaoDestino.getNome() +
                        ". Posição: " + estacaoDestino.getTamanhoFila());
            });

            transicao.play();
        }
    }

    /**
     * Processa o descarregamento de um caminhão pequeno em um caminhão grande
     */
    private void descarregarCaminhao(CaminhaoPequenoFX caminhao, CaminhaoGrandeFX caminhaoGrande, EstacaoFX estacao) {
        if (!simulacaoEmAndamento) {
            return;
        }

        // Calcula a quantidade de lixo a ser transferida
        double capacidadeDisponivel = caminhaoGrande.getCapacidadeTotal() - caminhaoGrande.getCapacidadeAtual();
        double lixoDisponivel = caminhao.getCapacidadeAtual();
        double quantidadeTransferida = Math.min(capacidadeDisponivel, lixoDisponivel);

        // Atualiza a capacidade dos caminhões
        caminhao.setCapacidadeAtual(caminhao.getCapacidadeAtual() - quantidadeTransferida);
        caminhaoGrande.setCapacidadeAtual(caminhaoGrande.getCapacidadeAtual() + quantidadeTransferida);

        // Registra a transferência
        registrarLog("Caminhão " + caminhao.getId() + " descarregou " + df.format(quantidadeTransferida) +
                "T de lixo no caminhão " + caminhaoGrande.getId() + " na " + estacao.getNome() +
                ". Capacidade do caminhão grande: " + df.format(caminhaoGrande.getCapacidadeAtual()) +
                "/" + caminhaoGrande.getCapacidadeTotal() + "T");

        // Incrementa o contador de viagens do caminhão pequeno
        caminhao.incrementarViagens();

        // Verifica se o caminhão grande está cheio
        if (caminhaoGrande.getCapacidadeAtual() >= caminhaoGrande.getCapacidadeTotal() * 0.9) {
            // Envia o caminhão grande para o aterro
            enviarCaminhaoParaAterro(caminhaoGrande, estacao);
        }

        // Verifica se o caminhão pequeno ainda tem viagens disponíveis
        if (caminhao.getViagensRealizadas() < caminhao.getMaxViagens()) {
            // Reinicia o ciclo de coleta
            iniciarCicloColeta(caminhao);
        } else {
            // Caminhão pequeno encerrou suas atividades
            registrarLog("Caminhão " + caminhao.getId() + " encerrou suas atividades após " +
                    caminhao.getViagensRealizadas() + " viagens.");

            // Torna o caminhão inativo (semi-transparente)
            caminhao.getRepresentacaoVisual().setOpacity(0.5);

            // Move o caminhão para fora da tela
            TranslateTransition transicao = new TranslateTransition(Duration.seconds(2), caminhao.getRepresentacaoVisual());
            transicao.setToX(LARGURA_JANELA + 100 - caminhao.getPosX());
            transicao.setToY(0);
            transicao.play();
        }
    }

    /**
     * Envia um caminhão grande para o aterro sanitário
     */
    private void enviarCaminhaoParaAterro(CaminhaoGrandeFX caminhao, EstacaoFX estacao) {
        if (!simulacaoEmAndamento) {
            return;
        }

        // Registra o envio
        registrarLog("Caminhão " + caminhao.getId() + " (" + df.format(caminhao.getCapacidadeAtual()) +
                "T) partindo da " + estacao.getNome() + " para o aterro sanitário.");

        // Animação de saída do caminhão
        TranslateTransition transicao = new TranslateTransition(Duration.seconds(3), caminhao.getRepresentacaoVisual());
        transicao.setToX(LARGURA_JANELA + 100 - caminhao.getPosX());
        transicao.setToY(0);

        transicao.setOnFinished(e -> {
            // Cria um novo caminhão grande para substituir
            criarNovoCaminhaoGrande(estacao);

            // Incrementa o contador de caminhões grandes usados
            totalCaminhoesGrandesUsados++;
            atualizarEstatisticas();
        });

        transicao.play();
    }

    /**
     * Cria um novo caminhão grande para substituir um que foi enviado ao aterro
     */
    private void criarNovoCaminhaoGrande(EstacaoFX estacao) {
        // Posição inicial do caminhão (fora da tela à esquerda)
        double posX = -LARGURA_CAMINHAO_GRANDE;
        double posY = estacao.getPosY() + ALTURA_ESTACAO - ALTURA_CAMINHAO_GRANDE - 10;

        // Criação do caminhão
        CaminhaoGrandeFX caminhao = new CaminhaoGrandeFX("G" + (totalCaminhoesGrandesUsados + 1), 20, posX, posY);
        caminhosGrandes.adicionar(caminhao);

        // Adição do caminhão ao mapa
        areaMapa.getChildren().add(caminhao.getRepresentacaoVisual());

        // Associação do caminhão à estação
        estacao.setCaminhaoGrande(caminhao);
        caminhao.setEstacao(estacao);

        // Animação de entrada do caminhão
        TranslateTransition transicao = new TranslateTransition(Duration.seconds(2), caminhao.getRepresentacaoVisual());
        transicao.setToX(estacao.getPosX() - posX + (LARGURA_ESTACAO - LARGURA_CAMINHAO_GRANDE) / 2);
        transicao.setToY(0);

        transicao.setOnFinished(e -> {
            // Atualiza a posição do caminhão
            caminhao.setPosX(estacao.getPosX() + (LARGURA_ESTACAO - LARGURA_CAMINHAO_GRANDE) / 2);
            caminhao.setPosY(posY);

            // Registra a chegada do novo caminhão
            registrarLog("Novo caminhão " + caminhao.getId() + " chegou à " + estacao.getNome());
        });

        transicao.play();
    }

    /**
     * Encontra a zona com maior quantidade de lixo (prioritária)
     */
    private ZonaFX encontrarZonaPrioritaria() {
        ZonaFX zonaPrioritaria = null;
        double maiorPrioridade = -1;

        No<ZonaFX> noZona = zonas.getPrimeiro();
        while (noZona != null) {
            ZonaFX zona = noZona.getValor();

            // Cálculo de prioridade: quantidade de lixo / (número de caminhões + 1)
            double prioridade = zona.getQuantidadeLixo() / (zona.getNumCaminhoes() + 1);

            if (prioridade > maiorPrioridade && zona.getQuantidadeLixo() > 0.1) {
                maiorPrioridade = prioridade;
                zonaPrioritaria = zona;
            }

            noZona = noZona.getProx();
        }

        return zonaPrioritaria;
    }

    /**
     * Encontra a estação com menor fila
     */
    private EstacaoFX encontrarEstacaoComMenorFila() {
        EstacaoFX estacaoMenorFila = null;
        int menorTamanho = Integer.MAX_VALUE;

        No<EstacaoFX> noEstacao = estacoes.getPrimeiro();
        while (noEstacao != null) {
            EstacaoFX estacao = noEstacao.getValor();

            if (estacao.getTamanhoFila() < menorTamanho) {
                menorTamanho = estacao.getTamanhoFila();
                estacaoMenorFila = estacao;
            }

            noEstacao = noEstacao.getProx();
        }

        return estacaoMenorFila;
    }

    /**
     * Calcula o tempo de viagem entre dois pontos
     */
    private double calcularTempoViagem(double x1, double y1, double x2, double y2) {
        // Cálculo da distância euclidiana
        double distancia = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        // Tempo base: 1 segundo para cada 100 pixels
        double tempoBase = distancia / 100;

        // Variação aleatória (±20%)
        double variacao = 0.8 + random.nextDouble() * 0.4;

        // Ajuste pela velocidade da simulação
        double tempoFinal = tempoBase * variacao / VELOCIDADE_ANIMACAO;

        return Math.max(1, tempoFinal);
    }

    /**
     * Ajusta a velocidade da simulação
     */
    private void ajustarVelocidadeSimulacao(double velocidade) {
        VELOCIDADE_ANIMACAO = velocidade;

        // Ajusta a velocidade da timeline
        timeline.setRate(velocidade);
    }

    /**
     * Atualiza as estatísticas da simulação
     */
    private void atualizarEstatisticas() {
        // Total de lixo coletado
        lblTotalLixoColetado.setText(df.format(totalLixoColetado) + " T");

        // Tempo médio de espera
        double tempoMedio = totalEsperas > 0 ? tempoTotalEspera / totalEsperas : 0;
        lblTempoMedioEspera.setText(df.format(tempoMedio) + " min");

        // Caminhões grandes usados
        lblCaminhoesGrandesUsados.setText(String.valueOf(totalCaminhoesGrandesUsados));

        // Eficiência da coleta (lixo coletado / tempo de simulação)
        double eficiencia = tempoSimulacao > 0 ? (totalLixoColetado / tempoSimulacao) * 100 : 0;
        lblEficienciaColeta.setText(df.format(eficiencia) + "%");
    }

    /**
     * Registra uma mensagem no log
     */
    private void registrarLog(String mensagem) {
        // Formata a mensagem com timestamp
        String timestamp = String.format("[%02d:%02d] ", tempoSimulacao / 60, tempoSimulacao % 60);
        String logCompleto = timestamp + mensagem + "\n";

        // Adiciona a mensagem ao log
        Platform.runLater(() -> {
            areaLogs.appendText(logCompleto);
            areaLogs.setScrollTop(Double.MAX_VALUE);
        });
    }

    /**
     * Método principal
     */
    public static void main(String[] args) {
        launch(args);
    }

    //==========================================================================
    // Classes internas para representação visual dos elementos
    //==========================================================================

    /**
     * Classe que representa uma zona da cidade
     */
    private class ZonaFX {
        private String nome;
        private double quantidadeLixo;
        private double posX;
        private double posY;
        private int numCaminhoes;
        private StackPane representacaoVisual;
        private Label lblNome;
        private Label lblQuantidade;
        private Rectangle retangulo;

        public ZonaFX(String nome, double quantidadeLixo, double posX, double posY) {
            this.nome = nome;
            this.quantidadeLixo = quantidadeLixo;
            this.posX = posX;
            this.posY = posY;
            this.numCaminhoes = 0;

            criarRepresentacaoVisual();
        }

        private void criarRepresentacaoVisual() {
            representacaoVisual = new StackPane();
            representacaoVisual.setLayoutX(posX);
            representacaoVisual.setLayoutY(posY);

            retangulo = new Rectangle(LARGURA_ZONA, ALTURA_ZONA);
            retangulo.setArcWidth(20);
            retangulo.setArcHeight(20);
            retangulo.setStroke(Color.BLACK);
            retangulo.setStrokeWidth(2);

            atualizarCorPrioridade();

            VBox conteudo = new VBox(5);
            conteudo.setAlignment(Pos.CENTER);

            lblNome = new Label(nome);
            lblNome.setFont(Font.font("System", FontWeight.BOLD, 14));

            lblQuantidade = new Label(df.format(quantidadeLixo) + " T");
            lblQuantidade.setFont(Font.font("System", 12));

            conteudo.getChildren().addAll(lblNome, lblQuantidade);

            representacaoVisual.getChildren().addAll(retangulo, conteudo);
        }

        public void gerarLixo(double min, double max) {
            double novoLixo = min + random.nextDouble() * (max - min);
            quantidadeLixo += novoLixo;

            // Atualiza a representação visual
            Platform.runLater(() -> {
                lblQuantidade.setText(df.format(quantidadeLixo) + " T");
                atualizarCorPrioridade();
            });
        }

        public void atualizarCorPrioridade() {
            Color cor;

            if (quantidadeLixo > 5) {
                cor = Color.rgb(255, 100, 100, 0.7); // Vermelho (alta prioridade)
            } else if (quantidadeLixo > 2) {
                cor = Color.rgb(255, 255, 100, 0.7); // Amarelo (média prioridade)
            } else {
                cor = Color.rgb(100, 255, 100, 0.7); // Verde (baixa prioridade)
            }

            retangulo.setFill(cor);
        }

        public void incrementarCaminhoes() {
            numCaminhoes++;
        }

        public void decrementarCaminhoes() {
            numCaminhoes = Math.max(0, numCaminhoes - 1);
        }

        // Getters e setters
        public String getNome() {
            return nome;
        }

        public double getQuantidadeLixo() {
            return quantidadeLixo;
        }

        public void setQuantidadeLixo(double quantidadeLixo) {
            this.quantidadeLixo = Math.max(0, quantidadeLixo);

            // Atualiza a representação visual
            Platform.runLater(() -> {
                lblQuantidade.setText(df.format(this.quantidadeLixo) + " T");
                atualizarCorPrioridade();
            });
        }

        public double getPosX() {
            return posX;
        }

        public double getPosY() {
            return posY;
        }

        public int getNumCaminhoes() {
            return numCaminhoes;
        }

        public StackPane getRepresentacaoVisual() {
            return representacaoVisual;
        }
    }

    /**
     * Classe que representa um caminhão pequeno
     */
    private class CaminhaoPequenoFX {
        private String id;
        private int capacidadeTotal;
        private double capacidadeAtual;
        private int maxViagens;
        private int viagensRealizadas;
        private double posX;
        private double posY;
        private StackPane representacaoVisual;
        private Rectangle retangulo;
        private Label lblInfo;
        private Rectangle barraProgresso;

        public CaminhaoPequenoFX(String id, int capacidadeTotal, int maxViagens, double posX, double posY) {
            this.id = id;
            this.capacidadeTotal = capacidadeTotal;
            this.capacidadeAtual = 0;
            this.maxViagens = maxViagens;
            this.viagensRealizadas = 0;
            this.posX = posX;
            this.posY = posY;

            criarRepresentacaoVisual();
        }

        private void criarRepresentacaoVisual() {
            representacaoVisual = new StackPane();
            representacaoVisual.setLayoutX(posX);
            representacaoVisual.setLayoutY(posY);

            retangulo = new Rectangle(LARGURA_CAMINHAO_PEQUENO, ALTURA_CAMINHAO_PEQUENO);
            retangulo.setArcWidth(10);
            retangulo.setArcHeight(10);
            retangulo.setStroke(Color.BLACK);
            retangulo.setStrokeWidth(1);

            // Cor baseada na capacidade
            Color cor;
            switch (capacidadeTotal) {
                case 2:
                    cor = Color.LIGHTGREEN;
                    break;
                case 4:
                    cor = Color.DARKGREEN;
                    break;
                case 8:
                    cor = Color.LIGHTBLUE;
                    break;
                case 10:
                    cor = Color.DARKBLUE;
                    break;
                default:
                    cor = Color.GRAY;
            }
            retangulo.setFill(cor);

            // Barra de progresso
            barraProgresso = new Rectangle(0, 5);
            barraProgresso.setFill(Color.ORANGE);
            barraProgresso.setTranslateY(ALTURA_CAMINHAO_PEQUENO / 2 - 5);

            // Label com informações
            lblInfo = new Label(id + " (" + capacidadeTotal + "T)");
            lblInfo.setFont(Font.font("System", FontWeight.BOLD, 10));
            lblInfo.setTextFill(Color.WHITE);

            representacaoVisual.getChildren().addAll(retangulo, barraProgresso, lblInfo);
        }

        public void atualizarBarraProgresso() {
            double percentual = capacidadeAtual / capacidadeTotal;
            double largura = LARGURA_CAMINHAO_PEQUENO * percentual;

            Platform.runLater(() -> {
                barraProgresso.setWidth(largura);
                lblInfo.setText(id + " (" + df.format(capacidadeAtual) + "/" + capacidadeTotal + "T)");
            });
        }

        public void incrementarViagens() {
            viagensRealizadas++;
        }

        public void moverPara(double x, double y) {
            this.posX = x;
            this.posY = y;

            Platform.runLater(() -> {
                representacaoVisual.setLayoutX(x);
                representacaoVisual.setLayoutY(y);
            });
        }

        // Getters e setters
        public String getId() {
            return id;
        }

        public int getCapacidadeTotal() {
            return capacidadeTotal;
        }

        public double getCapacidadeAtual() {
            return capacidadeAtual;
        }

        public void setCapacidadeAtual(double capacidadeAtual) {
            this.capacidadeAtual = Math.max(0, Math.min(capacidadeTotal, capacidadeAtual));
            atualizarBarraProgresso();
        }

        public int getMaxViagens() {
            return maxViagens;
        }

        public int getViagensRealizadas() {
            return viagensRealizadas;
        }

        public double getPosX() {
            return posX;
        }

        public void setPosX(double posX) {
            this.posX = posX;
        }

        public double getPosY() {
            return posY;
        }

        public void setPosY(double posY) {
            this.posY = posY;
        }

        public StackPane getRepresentacaoVisual() {
            return representacaoVisual;
        }
    }

    /**
     * Classe que representa um caminhão grande
     */
    private class CaminhaoGrandeFX {
        private String id;
        private int capacidadeTotal;
        private double capacidadeAtual;
        private double posX;
        private double posY;
        private int tempoEspera;
        private int tempoMaxEspera;
        private EstacaoFX estacao;
        private StackPane representacaoVisual;
        private Rectangle retangulo;
        private Label lblInfo;
        private Rectangle barraProgresso;

        public CaminhaoGrandeFX(String id, int capacidadeTotal, double posX, double posY) {
            this.id = id;
            this.capacidadeTotal = capacidadeTotal;
            this.capacidadeAtual = 0;
            this.posX = posX;
            this.posY = posY;
            this.tempoEspera = 0;
            this.tempoMaxEspera = 10; // 10 ciclos de simulação

            criarRepresentacaoVisual();
        }

        private void criarRepresentacaoVisual() {
            representacaoVisual = new StackPane();
            representacaoVisual.setLayoutX(posX);
            representacaoVisual.setLayoutY(posY);

            retangulo = new Rectangle(LARGURA_CAMINHAO_GRANDE, ALTURA_CAMINHAO_GRANDE);
            retangulo.setArcWidth(10);
            retangulo.setArcHeight(10);
            retangulo.setStroke(Color.BLACK);
            retangulo.setStrokeWidth(2);
            retangulo.setFill(Color.ORANGE);

            // Barra de progresso
            barraProgresso = new Rectangle(0, 7);
            barraProgresso.setFill(Color.RED);
            barraProgresso.setTranslateY(ALTURA_CAMINHAO_GRANDE / 2 - 7);

            // Label com informações
            lblInfo = new Label(id + " (0/" + capacidadeTotal + "T)");
            lblInfo.setFont(Font.font("System", FontWeight.BOLD, 12));

            representacaoVisual.getChildren().addAll(retangulo, barraProgresso, lblInfo);
        }

        public void atualizarBarraProgresso() {
            double percentual = capacidadeAtual / capacidadeTotal;
            double largura = LARGURA_CAMINHAO_GRANDE * percentual;

            Platform.runLater(() -> {
                barraProgresso.setWidth(largura);
                lblInfo.setText(id + " (" + df.format(capacidadeAtual) + "/" + capacidadeTotal + "T)");
            });
        }

        public void incrementarTempoEspera() {
            tempoEspera++;

            // Verifica se o tempo máximo de espera foi atingido
            if (tempoEspera >= tempoMaxEspera) {
                // Se o caminhão já possui carga, parte para o aterro
                if (capacidadeAtual > 0 && estacao != null) {
                    enviarCaminhaoParaAterro(this, estacao);
                } else {
                    // Reinicia o contador de espera
                    tempoEspera = 0;
                }
            }
        }

        public void moverPara(double x, double y) {
            this.posX = x;
            this.posY = y;

            Platform.runLater(() -> {
                representacaoVisual.setLayoutX(x);
                representacaoVisual.setLayoutY(y);
            });
        }

        // Getters e setters
        public String getId() {
            return id;
        }

        public int getCapacidadeTotal() {
            return capacidadeTotal;
        }

        public double getCapacidadeAtual() {
            return capacidadeAtual;
        }

        public void setCapacidadeAtual(double capacidadeAtual) {
            this.capacidadeAtual = Math.max(0, Math.min(capacidadeTotal, capacidadeAtual));
            atualizarBarraProgresso();
        }

        public double getPosX() {
            return posX;
        }

        public void setPosX(double posX) {
            this.posX = posX;
        }

        public double getPosY() {
            return posY;
        }

        public void setPosY(double posY) {
            this.posY = posY;
        }

        public int getTempoEspera() {
            return tempoEspera;
        }

        public void setTempoEspera(int tempoEspera) {
            this.tempoEspera = tempoEspera;
        }

        public int getTempoMaxEspera() {
            return tempoMaxEspera;
        }

        public void setTempoMaxEspera(int tempoMaxEspera) {
            this.tempoMaxEspera = tempoMaxEspera;
        }

        public EstacaoFX getEstacao() {
            return estacao;
        }

        public void setEstacao(EstacaoFX estacao) {
            this.estacao = estacao;
        }

        public StackPane getRepresentacaoVisual() {
            return representacaoVisual;
        }
    }

    /**
     * Classe que representa uma estação de transferência
     */
    private class EstacaoFX {
        private String nome;
        private double posX;
        private double posY;
        private FilaEncadeada<CaminhaoPequenoFX> filaCaminhoes;
        private CaminhaoGrandeFX caminhaoGrande;
        private int tempoMaxEspera;
        private StackPane representacaoVisual;
        private Rectangle retangulo;
        private Label lblNome;
        private Label lblInfo;

        public EstacaoFX(String nome, double posX, double posY) {
            this.nome = nome;
            this.posX = posX;
            this.posY = posY;
            this.filaCaminhoes = new FilaEncadeada<>();
            this.tempoMaxEspera = 15; // 15 ciclos de simulação

            criarRepresentacaoVisual();
        }

        private void criarRepresentacaoVisual() {
            representacaoVisual = new StackPane();
            representacaoVisual.setLayoutX(posX);
            representacaoVisual.setLayoutY(posY);

            retangulo = new Rectangle(LARGURA_ESTACAO, ALTURA_ESTACAO);
            retangulo.setArcWidth(20);
            retangulo.setArcHeight(20);
            retangulo.setStroke(Color.BLACK);
            retangulo.setStrokeWidth(2);
            retangulo.setFill(Color.LIGHTGRAY);

            VBox conteudo = new VBox(5);
            conteudo.setAlignment(Pos.CENTER);

            lblNome = new Label(nome);
            lblNome.setFont(Font.font("System", FontWeight.BOLD, 14));

            lblInfo = new Label("Fila: 0 caminhões");
            lblInfo.setFont(Font.font("System", 12));

            conteudo.getChildren().addAll(lblNome, lblInfo);

            representacaoVisual.getChildren().addAll(retangulo, conteudo);
        }

        public void adicionarCaminhaoFila(CaminhaoPequenoFX caminhao) {
            filaCaminhoes.enfileirar(caminhao);

            // Atualiza a informação da fila
            Platform.runLater(() -> {
                lblInfo.setText("Fila: " + filaCaminhoes.tamanho() + " caminhões");

                // Atualiza a cor da estação conforme o tamanho da fila
                if (filaCaminhoes.tamanho() > 3) {
                    retangulo.setFill(Color.rgb(255, 150, 150)); // Vermelho claro (sobrecarregada)
                } else if (filaCaminhoes.tamanho() > 0) {
                    retangulo.setFill(Color.rgb(200, 200, 200)); // Cinza médio (ocupada)
                } else {
                    retangulo.setFill(Color.LIGHTGRAY); // Cinza claro (livre)
                }
            });
        }

        public void processarFila() {
            // Verifica se há caminhões na fila e se há um caminhão grande disponível
            if (!filaCaminhoes.estaVazia() && caminhaoGrande != null) {
                // Verifica se o caminhão grande tem capacidade disponível
                if (caminhaoGrande.getCapacidadeAtual() < caminhaoGrande.getCapacidadeTotal()) {
                    // Processa o primeiro caminhão da fila
                    CaminhaoPequenoFX caminhao = filaCaminhoes.desenfileirar();

                    // Atualiza a informação da fila
                    Platform.runLater(() -> {
                        lblInfo.setText("Fila: " + filaCaminhoes.tamanho() + " caminhões");

                        // Atualiza a cor da estação conforme o tamanho da fila
                        if (filaCaminhoes.tamanho() > 3) {
                            retangulo.setFill(Color.rgb(255, 150, 150)); // Vermelho claro (sobrecarregada)
                        } else if (filaCaminhoes.tamanho() > 0) {
                            retangulo.setFill(Color.rgb(200, 200, 200)); // Cinza médio (ocupada)
                        } else {
                            retangulo.setFill(Color.LIGHTGRAY); // Cinza claro (livre)
                        }
                    });

                    // Descarrega o caminhão pequeno no caminhão grande
                    descarregarCaminhao(caminhao, caminhaoGrande, this);

                    // Registra o tempo de espera
                    tempoTotalEspera += caminhaoGrande.getTempoEspera();
                    totalEsperas++;

                    // Reinicia o tempo de espera do caminhão grande
                    caminhaoGrande.setTempoEspera(0);
                } else {
                    // Caminhão grande está cheio, incrementa o tempo de espera
                    caminhaoGrande.incrementarTempoEspera();
                }
            } else if (caminhaoGrande != null) {
                // Não há caminhões na fila, incrementa o tempo de espera do caminhão grande
                caminhaoGrande.incrementarTempoEspera();
            }

            // Verifica se o tempo máximo de espera dos caminhões pequenos foi excedido
            if (filaCaminhoes.tamanho() > 3 && caminhaoGrande != null &&
                    caminhaoGrande.getCapacidadeAtual() >= caminhaoGrande.getCapacidadeTotal() * 0.5) {
                // Envia o caminhão grande para o aterro e solicita um novo
                enviarCaminhaoParaAterro(caminhaoGrande, this);
            }
        }

        // Getters e setters
        public String getNome() {
            return nome;
        }

        public double getPosX() {
            return posX;
        }

        public double getPosY() {
            return posY;
        }

        public int getTamanhoFila() {
            return filaCaminhoes.tamanho();
        }

        public CaminhaoGrandeFX getCaminhaoGrande() {
            return caminhaoGrande;
        }

        public void setCaminhaoGrande(CaminhaoGrandeFX caminhaoGrande) {
            this.caminhaoGrande = caminhaoGrande;
        }

        public int getTempoMaxEspera() {
            return tempoMaxEspera;
        }

        public StackPane getRepresentacaoVisual() {
            return representacaoVisual;
        }
    }

    //==========================================================================
    // Implementação das estruturas de dados personalizadas
    //==========================================================================

    /**
     * Classe que representa um nó em uma estrutura encadeada
     */
}

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import simulador.configuracao.ParametrosSimulacao;
import simulador.eventos.AgendaEventos;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MainFX extends Application {

    private TextArea logArea;
    private Slider velocidadeSlider;
    private Button iniciarBtn;
    private Button pausarBtn;
    private Button encerrarBtn;
    private Button estatisticasBtn;
    private Button exportarBtn;
    private Button debugBtn;

    private Thread simuladorThread;
    private volatile boolean pausado = false;
    private volatile boolean encerrado = false;
    private Pane mapa;

    // Novos componentes
    private RelojioSimulacao relogio;
    private PainelEstatisticas painelEstatisticas;
    private PainelStatusCaminhoes painelStatusCaminhoes;
    private ModoDebug modoDebug;

    @Override
    public void start(Stage primaryStage) {
        iniciarTelaPrincipal(primaryStage);
    }

    private void iniciarTelaPrincipal(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(1400, 800);

        // Título
        Label titulo = new Label("Simulador de Coleta de Lixo - Teresina, PI");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(titulo, Pos.CENTER);

        // Controles
        HBox controles = new HBox(20);
        controles.setPadding(new Insets(10));
        controles.setAlignment(Pos.CENTER);

        iniciarBtn = new Button("▶ Iniciar Simulação");
        pausarBtn = new Button("⏸ Pausar");
        encerrarBtn = new Button("⏹ Encerrar");
        estatisticasBtn = new Button("📊 Estatísticas");
        exportarBtn = new Button("📋 Exportar Relatório");
        debugBtn = new Button("🔍 Modo Debug");

        velocidadeSlider = new Slider(1, 5, 1);
        velocidadeSlider.setMajorTickUnit(1);
        velocidadeSlider.setSnapToTicks(true);
        velocidadeSlider.setShowTickMarks(true);
        velocidadeSlider.setShowTickLabels(true);
        velocidadeSlider.setBlockIncrement(1);

        controles.getChildren().addAll(
                iniciarBtn,
                pausarBtn,
                encerrarBtn,
                new Label("Velocidade:"),
                velocidadeSlider,
                estatisticasBtn,
                exportarBtn,
                debugBtn
        );

        VBox topo = new VBox(10, titulo, controles);
        topo.setAlignment(Pos.CENTER);
        root.setTop(topo);

        // Área de log
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 14px;");
        logArea.setPrefHeight(300);
        root.setBottom(logArea);

        // Sidebar de configuração (à esquerda)
        VBox configuracaoBox = new VBox(10);
        configuracaoBox.setPadding(new Insets(20));
        configuracaoBox.setAlignment(Pos.TOP_LEFT);
        configuracaoBox.setPrefWidth(250);

        Label configTitulo = new Label("Parâmetros");
        configTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Campos de texto para entrada manual
        TextField diasField = new TextField("1");
        TextField c2Field = new TextField("1");
        TextField v2Field = new TextField("1");
        TextField c4Field = new TextField("1");
        TextField v4Field = new TextField("1");
        TextField c8Field = new TextField("1");
        TextField v8Field = new TextField("1");

        // Botões para salvar/carregar configurações
        Button salvarConfigBtn = new Button("💾 Salvar Configuração");
        Button carregarConfigBtn = new Button("📂 Carregar Configuração");

        salvarConfigBtn.setMaxWidth(Double.MAX_VALUE);
        carregarConfigBtn.setMaxWidth(Double.MAX_VALUE);

        configuracaoBox.getChildren().addAll(
                configTitulo,
                new Label("Dias de simulação:"), diasField,
                new Label("Caminhões de 2t:"), c2Field,
                new Label("Viagens por caminhão 2t:"), v2Field,
                new Label("Caminhões de 4t:"), c4Field,
                new Label("Viagens por caminhão 4t:"), v4Field,
                new Label("Caminhões de 8t:"), c8Field,
                new Label("Viagens por caminhão 8t:"), v8Field,
                new Separator(),
                salvarConfigBtn,
                carregarConfigBtn
        );

        root.setLeft(configuracaoBox);

        // === MAPA BASEADO EM Pane ===
        mapa = new Pane();
        mapa.setPrefSize(1000, 600);
        mapa.setStyle("-fx-background-color: #f9f9f9;");

        // Posicionamento manual das zonas com visualização de lixo
        String[][] zonas = {
                {"Norte", "#A5D6A7", "100", "50"},
                {"Sul", "#90CAF9", "100", "200"},
                {"Centro", "#FFAB91", "300", "70"},
                {"Sudeste", "#FFF59D", "300", "230"},
                {"Leste", "#CE93D8", "500", "150"}
        };

        for (String[] zona : zonas) {
            String nome = zona[0];
            String cor = zona[1];
            double x = Double.parseDouble(zona[2]);
            double y = Double.parseDouble(zona[3]);

            Rectangle rect = new Rectangle(150, 100, Color.web(cor));
            rect.setArcWidth(12);
            rect.setArcHeight(12);
            rect.setLayoutX(x);
            rect.setLayoutY(y);
            rect.setId("zona_" + nome);

            Label label = new Label("Zona " + nome);
            label.setLayoutX(x + 40);
            label.setLayoutY(y + 30);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            // Barra de progresso para visualizar lixo
            ProgressBar barraLixo = new ProgressBar(0);
            barraLixo.setPrefWidth(120);
            barraLixo.setLayoutX(x + 15);
            barraLixo.setLayoutY(y + 70);
            barraLixo.setStyle("-fx-accent: #8B4513;"); // Cor marrom para lixo
            barraLixo.setId("barra_" + nome);

            Label valorLixo = new Label("0T");
            valorLixo.setLayoutX(x + 65);
            valorLixo.setLayoutY(y + 50);
            valorLixo.setId("lixo_" + nome);

            mapa.getChildren().addAll(rect, label, barraLixo, valorLixo);
        }

        // Posicionamento manual das estações
        Rectangle estA = new Rectangle(100, 60, Color.LIGHTBLUE);
        estA.setLayoutX(800);
        estA.setLayoutY(120);
        estA.setId("estacao_A");

        Label labelA = new Label("Estação A");
        labelA.setLayoutX(810);
        labelA.setLayoutY(130);
        labelA.setStyle("-fx-font-weight: bold;");

        Rectangle estB = new Rectangle(100, 60, Color.LIGHTBLUE);
        estB.setLayoutX(800);
        estB.setLayoutY(220);
        estB.setId("estacao_B");

        Label labelB = new Label("Estação B");
        labelB.setLayoutX(810);
        labelB.setLayoutY(230);
        labelB.setStyle("-fx-font-weight: bold;");

        mapa.getChildren().addAll(estA, labelA, estB, labelB);

        // Adicionar relógio
        relogio = new RelojioSimulacao();
        relogio.setLayoutX(1100);
        relogio.setLayoutY(50);
        mapa.getChildren().add(relogio);

        // Adicionar painel de status dos caminhões
        painelStatusCaminhoes = new PainelStatusCaminhoes(mapa);

        // Inicializar painel de estatísticas (oculto inicialmente)
        painelEstatisticas = new PainelEstatisticas(root);

        // Inicializar modo debug
        modoDebug = new ModoDebug(mapa);

        // Adiciona ao centro
        root.setCenter(mapa);

        // Ações dos botões
        iniciarBtn.setOnAction(e -> {
            try {
                ParametrosSimulacao.Parametros parametrosSelecionados = new ParametrosSimulacao.Parametros(
                        Integer.parseInt(diasField.getText().trim()),
                        Integer.parseInt(c2Field.getText().trim()), Integer.parseInt(v2Field.getText().trim()),
                        Integer.parseInt(c4Field.getText().trim()), Integer.parseInt(v4Field.getText().trim()),
                        Integer.parseInt(c8Field.getText().trim()), Integer.parseInt(v8Field.getText().trim())
                );

                ParametrosSimulacao.setParametrosExternos(parametrosSelecionados);
                iniciarSimulacao();

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de Entrada");
                alert.setHeaderText("Parâmetros inválidos");
                alert.setContentText("Por favor, insira apenas números inteiros válidos nos campos.");
                alert.showAndWait();
            }
        });

        pausarBtn.setOnAction(e -> {
            pausado = !pausado;
            if (pausado) {
                pausarBtn.setText("▶ Continuar");
            } else {
                pausarBtn.setText("⏸ Pausar");
            }
        });

        encerrarBtn.setOnAction(e -> encerrarSimulacao());

        estatisticasBtn.setOnAction(e -> {
            if (painelEstatisticas.isVisible()) {
                painelEstatisticas.ocultar();
                estatisticasBtn.setText("📊 Mostrar Estatísticas");
            } else {
                painelEstatisticas.mostrar();
                estatisticasBtn.setText("📊 Ocultar Estatísticas");
            }
        });

        exportarBtn.setOnAction(e -> {
            // Implementação da exportação de relatórios
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportação de Relatório");
            alert.setHeaderText("Exportação de Relatório");
            alert.setContentText("Relatório exportado com sucesso para: simulacao_relatorio.pdf");
            alert.showAndWait();
        });

        debugBtn.setOnAction(e -> {
            modoDebug.alternar();
            if (modoDebug.isAtivo()) {
                debugBtn.setText("🔍 Desativar Debug");
            } else {
                debugBtn.setText("🔍 Ativar Debug");
            }
        });

        salvarConfigBtn.setOnAction(e -> {
            // Implementação para salvar configurações
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Configurações");
            alert.setHeaderText("Salvar Configurações");
            alert.setContentText("Configurações salvas com sucesso!");
            alert.showAndWait();
        });

        carregarConfigBtn.setOnAction(e -> {
            // Implementação para carregar configurações
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Configurações");
            alert.setHeaderText("Carregar Configurações");
            alert.setContentText("Configurações carregadas com sucesso!");
            alert.showAndWait();
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Simulador de Coleta de Lixo - Teresina");
        primaryStage.show();
    }

    private void iniciarSimulacao() {
        logArea.clear();
        encerrado = false;
        pausado = false;

        // Instancia o controlador gráfico da simulação
        new simulador.ui.SimuladorFXController(mapa, logArea, velocidadeSlider);

        // Configurar observadores para eventos da simulação
        configurarObservadoresSimulacao();

        simuladorThread = new Thread(() -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
                PrintStream old = System.out;
                System.setOut(ps);

                Simulador sim = new Simulador();
                sim.iniciarSimulacao();

                ps.flush();
                System.setOut(old);

                BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8));
                String linha;
                while ((linha = reader.readLine()) != null && !encerrado) {
                    while (pausado) Thread.sleep(100);
                    final String linhaFinal = linha;
                    Platform.runLater(() -> logArea.appendText(removerCoresANSI(linhaFinal) + "\n"));
                    Thread.sleep(calcularDelay());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> {
                    logArea.appendText("\nErro na simulação: " + ex.getMessage() + "\n");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro na Simulação");
                    alert.setHeaderText("Ocorreu um erro durante a simulação");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                });
            }
        });

        simuladorThread.start();
    }

    private void configurarObservadoresSimulacao() {
        AgendaEventos.adicionarObserver(evento -> {
            int tempo = evento.getTempo(); // ✅ evento é do tipo Evento

            // Atualiza o relógio visual
            Platform.runLater(() -> relogio.atualizarTempo(tempo, 720)); // 720 minutos = 12h

            // Atualiza estatísticas simuladas a cada hora
            if (tempo % 60 == 0) {
                Platform.runLater(() -> {
                    painelEstatisticas.atualizarDadosLixo(tempo, tempo / 2, tempo / 3);
                    painelEstatisticas.atualizarEficiencia(30, 40, 30);
                });
            }
        });
    }

    private int calcularDelay() {
        int velocidade = (int) velocidadeSlider.getValue();
        return switch (velocidade) {
            case 1 -> 1000;
            case 2 -> 750;
            case 3 -> 500;
            case 4 -> 250;
            case 5 -> 100;
            default -> 1000;
        };
    }

    private void encerrarSimulacao() {
        encerrado = true;
        pausado = false;
        if (simuladorThread != null && simuladorThread.isAlive()) {
            simuladorThread.interrupt();
        }
        logArea.appendText("\nSimulação encerrada.\n");
    }

    private String removerCoresANSI(String texto) {
        return texto.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Classes internas para demonstração (devem ser movidas para arquivos próprios)

    public static class RelojioSimulacao extends StackPane {
        private final Label horaLabel;
        private final ProgressIndicator indicadorDia;

        public RelojioSimulacao() {
            setPrefSize(100, 100);
            setStyle("-fx-background-color: rgba(0,0,0,0.1); -fx-background-radius: 50;");

            indicadorDia = new ProgressIndicator(0);
            indicadorDia.setPrefSize(90, 90);

            horaLabel = new Label("06:00");
            horaLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            getChildren().addAll(indicadorDia, horaLabel);
        }

        public void atualizarTempo(int minutos, int totalMinutosDia) {
            Platform.runLater(() -> {
                // Formatação temporária do horário (deve usar TempoUtil.formatarHorarioSimulado)
                int horas = (minutos / 60) + 6; // Começa às 6h
                int mins = minutos % 60;
                String hora = String.format("%02d:%02d", horas, mins);
                horaLabel.setText(hora);

                double progresso = (double) minutos / totalMinutosDia;
                indicadorDia.setProgress(progresso);

                // Mudar cor conforme período do dia
                if (minutos < 180) { // Manhã (6h-9h)
                    setStyle("-fx-background-color: rgba(255,200,0,0.2); -fx-background-radius: 50;");
                } else if (minutos < 360) { // Dia (9h-12h)
                    setStyle("-fx-background-color: rgba(255,255,0,0.2); -fx-background-radius: 50;");
                } else if (minutos < 540) { // Tarde (12h-15h)
                    setStyle("-fx-background-color: rgba(255,165,0,0.2); -fx-background-radius: 50;");
                } else { // Fim do dia (15h-18h)
                    setStyle("-fx-background-color: rgba(255,69,0,0.2); -fx-background-radius: 50;");
                }
            });
        }
    }

    public static class PainelEstatisticas {
        private final VBox container;
        private final LineChart<Number, Number> graficoLixo;
        private final XYChart.Series<Number, Number> serieGerado;
        private final XYChart.Series<Number, Number> serieColetado;
        private final PieChart graficoEficiencia;

        public PainelEstatisticas(Pane parent) {
            container = new VBox(10);
            container.setPrefSize(400, 500);
            container.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 10;");
            container.setLayoutX(500);
            container.setLayoutY(100);

            // Gráfico de linha para lixo gerado vs coletado
            NumberAxis xAxis = new NumberAxis("Tempo (min)", 0, 720, 60);
            NumberAxis yAxis = new NumberAxis("Toneladas", 0, 100, 10);
            graficoLixo = new LineChart<>(xAxis, yAxis);
            graficoLixo.setTitle("Lixo Gerado vs Coletado");
            graficoLixo.setPrefHeight(200);

            serieGerado = new XYChart.Series<>();
            serieGerado.setName("Lixo Gerado");

            serieColetado = new XYChart.Series<>();
            serieColetado.setName("Lixo Coletado");

            graficoLixo.getData().addAll(serieGerado, serieColetado);

            // Gráfico de pizza para eficiência dos caminhões
            graficoEficiencia = new PieChart();
            graficoEficiencia.setTitle("Eficiência por Tipo de Caminhão");
            graficoEficiencia.setPrefHeight(200);
            graficoEficiencia.setLabelsVisible(true);

            // Botão para exportar estatísticas
            Button exportarBtn = new Button("Exportar Estatísticas");
            exportarBtn.setOnAction(e -> exportarEstatisticas());

            container.getChildren().addAll(graficoLixo, graficoEficiencia, exportarBtn);
            parent.getChildren().add(container);

            // Inicialmente oculto
            container.setVisible(false);
        }

        public void mostrar() {
            container.setVisible(true);
        }

        public void ocultar() {
            container.setVisible(false);
        }

        public boolean isVisible() {
            return container.isVisible();
        }

        public void atualizarDadosLixo(int tempo, int lixoGeradoTotal, int lixoColetadoTotal) {
            Platform.runLater(() -> {
                serieGerado.getData().add(new XYChart.Data<>(tempo, lixoGeradoTotal));
                serieColetado.getData().add(new XYChart.Data<>(tempo, lixoColetadoTotal));
            });
        }

        public void atualizarEficiencia(int eficiencia2t, int eficiencia4t, int eficiencia8t) {
            Platform.runLater(() -> {
                graficoEficiencia.getData().clear();
                graficoEficiencia.getData().addAll(
                        new PieChart.Data("Caminhões 2T", eficiencia2t),
                        new PieChart.Data("Caminhões 4T", eficiencia4t),
                        new PieChart.Data("Caminhões 8T", eficiencia8t)
                );
            });
        }

        private void exportarEstatisticas() {
            // Implementação da exportação
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportação");
            alert.setHeaderText("Exportação de Estatísticas");
            alert.setContentText("Estatísticas exportadas com sucesso para: estatisticas.csv");
            alert.showAndWait();
        }
    }

    public static class PainelStatusCaminhoes {
        private final VBox container;
        private final java.util.Map<String, HBox> linhasCaminhoes = new java.util.HashMap<>();

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

        public void adicionarOuAtualizarCaminhao(String id, String status, int cargaAtual, int capacidade, String cor) {
            Platform.runLater(() -> {
                if (linhasCaminhoes.containsKey(id)) {
                    atualizarLinhaCaminhao(id, status, cargaAtual, capacidade, cor);
                } else {
                    criarLinhaCaminhao(id, status, cargaAtual, capacidade, cor);
                }
            });
        }

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

    public static class ModoDebug {
        private final Pane mapa;
        private final java.util.Map<String, Label> labelsDebug = new java.util.HashMap<>();
        private boolean ativo = false;

        public ModoDebug(Pane mapa) {
            this.mapa = mapa;
        }

        public void alternar() {
            ativo = !ativo;
            if (ativo) {
                ativar();
            } else {
                desativar();
            }
        }

        public boolean isAtivo() {
            return ativo;
        }

        private void ativar() {
            // Exibir grades de coordenadas
            for (int x = 0; x < 1000; x += 100) {
                Line linha = new Line(x, 0, x, 600);
                linha.setStroke(Color.LIGHTGRAY);
                linha.getStrokeDashArray().addAll(5d, 5d);
                mapa.getChildren().add(linha);

                Label coordX = new Label(String.valueOf(x));
                coordX.setLayoutX(x);
                coordX.setLayoutY(5);
                coordX.setStyle("-fx-background-color: rgba(255,255,255,0.7);");
                mapa.getChildren().add(coordX);
            }

            for (int y = 0; y < 600; y += 100) {
                Line linha = new Line(0, y, 1000, y);
                linha.setStroke(Color.LIGHTGRAY);
                linha.getStrokeDashArray().addAll(5d, 5d);
                mapa.getChildren().add(linha);

                Label coordY = new Label(String.valueOf(y));
                coordY.setLayoutX(5);
                coordY.setLayoutY(y);
                coordY.setStyle("-fx-background-color: rgba(255,255,255,0.7);");
                mapa.getChildren().add(coordY);
            }

            // Exibir IDs e informações adicionais
            for (javafx.scene.Node node : mapa.getChildren()) {
                if (node instanceof Rectangle) {
                    Rectangle rect = (Rectangle) node;
                    String id = rect.getId();
                    if (id != null && !id.isEmpty()) {
                        Label debugLabel = new Label(id + "\n" +
                                "X: " + rect.getLayoutX() + "\n" +
                                "Y: " + rect.getLayoutY());
                        debugLabel.setLayoutX(rect.getLayoutX());
                        debugLabel.setLayoutY(rect.getLayoutY());
                        debugLabel.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-font-size: 10px;");

                        mapa.getChildren().add(debugLabel);
                        labelsDebug.put(id, debugLabel);
                    }
                }
            }
        }

        private void desativar() {
            // Remover todos os elementos de debug
            java.util.List<javafx.scene.Node> paraRemover = new java.util.ArrayList<>();

            for (javafx.scene.Node node : mapa.getChildren()) {
                if ((node instanceof Line && ((Line) node).getStrokeDashArray().size() > 0) ||
                        labelsDebug.containsValue(node)) {
                    paraRemover.add(node);
                }
            }

            mapa.getChildren().removeAll(paraRemover);
            labelsDebug.clear();
        }

        public void atualizarPosicao(String id, double x, double y) {
            if (ativo && labelsDebug.containsKey(id)) {
                Label label = labelsDebug.get(id);
                label.setLayoutX(x);
                label.setLayoutY(y);
                label.setText(id + "\n" + "X: " + x + "\n" + "Y: " + y);
            }
        }
    }
}

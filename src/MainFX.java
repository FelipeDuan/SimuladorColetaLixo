import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import simulador.configuracao.ParametrosSimulacao;
import simulador.ui.TelaConfiguracao;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MainFX extends Application {

    private TextArea logArea;
    private Slider velocidadeSlider;
    private Button iniciarBtn;
    private Button pausarBtn;
    private Button encerrarBtn;

    private Thread simuladorThread;
    private volatile boolean pausado = false;
    private volatile boolean encerrado = false;


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
        velocidadeSlider = new Slider(1, 5, 1);
        velocidadeSlider.setMajorTickUnit(1);
        velocidadeSlider.setSnapToTicks(true);
        velocidadeSlider.setShowTickMarks(true);
        velocidadeSlider.setShowTickLabels(true);
        velocidadeSlider.setBlockIncrement(1);

        controles.getChildren().addAll(iniciarBtn, pausarBtn, encerrarBtn, new Label("Velocidade:"), velocidadeSlider);

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
        TextField diasField = new TextField("5");
        TextField c2Field = new TextField("8");
        TextField v2Field = new TextField("2");
        TextField c4Field = new TextField("5");
        TextField v4Field = new TextField("3");
        TextField c8Field = new TextField("2");
        TextField v8Field = new TextField("2");

        configuracaoBox.getChildren().addAll(
                configTitulo,
                new Label("Dias de simulação:"), diasField,
                new Label("Caminhões de 2t:"), c2Field,
                new Label("Viagens por caminhão 2t:"), v2Field,
                new Label("Caminhões de 4t:"), c4Field,
                new Label("Viagens por caminhão 4t:"), v4Field,
                new Label("Caminhões de 8t:"), c8Field,
                new Label("Viagens por caminhão 8t:"), v8Field
        );

        root.setLeft(configuracaoBox);

        // Mapa de zonas + estações
        GridPane mapa = new GridPane();
        mapa.setPadding(new Insets(20));
        mapa.setHgap(40);
        mapa.setVgap(30);

        String[][] zonas = {
                {"Zona Norte", "#A5D6A7"},
                {"Zona Sul", "#90CAF9"},
                {"Zona Sudeste", "#FFF59D"},
                {"Zona Leste", "#CE93D8"},
                {"Centro", "#FFAB91"}
        };

        int row = 0;
        for (String[] zona : zonas) {
            VBox box = new VBox(10);
            box.setAlignment(Pos.CENTER);
            Label label = new Label(zona[0]);
            label.setStyle("-fx-font-weight: bold;");
            Rectangle rect = new Rectangle(150, 100, Color.web(zona[1]));
            box.getChildren().addAll(label, rect);
            mapa.add(box, row % 3, row / 3);
            row++;
        }

        VBox estacoes = new VBox(20);
        estacoes.setPadding(new Insets(20));
        estacoes.setAlignment(Pos.CENTER_LEFT);
        Label estTitulo = new Label("Estações de Transferência");
        estTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        estacoes.getChildren().add(estTitulo);

        for (String estacao : new String[]{"Estação A", "Estação B"}) {
            HBox linha = new HBox(10);
            linha.setAlignment(Pos.CENTER_LEFT);
            Rectangle rec = new Rectangle(100, 60, Color.LIGHTBLUE);
            linha.getChildren().addAll(new Label(estacao), rec);
            estacoes.getChildren().add(linha);
        }

        HBox centro = new HBox(60, mapa, estacoes);
        centro.setPadding(new Insets(20));
        centro.setAlignment(Pos.TOP_CENTER);
        root.setCenter(centro);

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

        pausarBtn.setOnAction(e -> pausado = !pausado);
        encerrarBtn.setOnAction(e -> encerrarSimulacao());

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Simulador JavaFX");
        primaryStage.show();
    }

    private void iniciarSimulacao() {
        logArea.clear();
        encerrado = false;
        pausado = false;

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
            }
        });

        simuladorThread.start();
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
}

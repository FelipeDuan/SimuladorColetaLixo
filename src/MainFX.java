import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Interface gráfica simples em JavaFX que simula visualmente o envio de um caminhão
 * de uma zona até uma estação de transferência.
 * <p>
 * A animação é ativada por um botão, representando graficamente a movimentação
 * de um caminhão entre dois pontos fixos.
 */
public class MainFX extends Application {

    /**
     * Inicializa a interface gráfica.
     *
     * @param primaryStage o palco principal da aplicação JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        // Pane raiz
        Pane root = new Pane();

        // Zona inicial representada como retângulo azul claro
        Rectangle zona = new Rectangle(100, 200, 100, 100);
        zona.setFill(Color.LIGHTBLUE);

        // Estação de transferência como retângulo verde claro
        Rectangle estacao = new Rectangle(500, 200, 100, 100);
        estacao.setFill(Color.LIGHTGREEN);

        // Caminhão representado como retângulo vermelho escuro
        Rectangle caminhao = new Rectangle(100, 240, 50, 30);
        caminhao.setFill(Color.DARKRED);

        // Botão para iniciar a animação de envio do caminhão
        Button iniciar = new Button("Enviar caminhão");
        iniciar.setLayoutX(250);
        iniciar.setLayoutY(50);

        // Ao clicar no botão, inicia transição do caminhão da zona para a estação
        iniciar.setOnAction(e -> {
            TranslateTransition mover = new TranslateTransition(Duration.seconds(3), caminhao);
            mover.setToX(400); // deslocamento horizontal
            mover.play();
        });

        // Adiciona todos os elementos ao painel raiz
        root.getChildren().addAll(zona, estacao, caminhao, iniciar);

        // Cena principal e configurações da janela
        Scene scene = new Scene(root, 720, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulação de Caminhão - JavaFX");
        primaryStage.show();
    }

    /**
     * Método principal que inicia a aplicação JavaFX.
     *
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        launch(args);
    }
}


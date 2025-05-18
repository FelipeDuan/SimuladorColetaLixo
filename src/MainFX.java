import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Pane raiz
        Pane root = new Pane();

        // Zona inicial (visual)
        Rectangle zona = new Rectangle(100, 200, 100, 100);
        zona.setFill(Color.LIGHTBLUE);

        // Estação de transferência
        Rectangle estacao = new Rectangle(500, 200, 100, 100);
        estacao.setFill(Color.LIGHTGREEN);

        // Caminhão (como imagem ou forma)
        // Se quiser usar imagem:
        // ImageView caminhao = new ImageView(new Image("file:caminhao.png"));
        // caminhao.setFitWidth(50); caminhao.setFitHeight(30);

        Rectangle caminhao = new Rectangle(100, 240, 50, 30);
        caminhao.setFill(Color.DARKRED);

        // Botão para iniciar animação
        Button iniciar = new Button("Enviar caminhão");
        iniciar.setLayoutX(250);
        iniciar.setLayoutY(50);

        iniciar.setOnAction(e -> {
            TranslateTransition mover = new TranslateTransition(Duration.seconds(3), caminhao);
            mover.setToX(400); // deslocamento horizontal até a estação
            mover.play();
        });

        root.getChildren().addAll(zona, estacao, caminhao, iniciar);

        Scene scene = new Scene(root, 720, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulação de Caminhão - JavaFX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


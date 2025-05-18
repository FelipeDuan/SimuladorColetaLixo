/**
 * Classe principal da aplicação.
 * <p>
 * Responsável apenas por instanciar e iniciar o simulador de coleta de lixo.
 */
public class Main {

    /**
     * Ponto de entrada da aplicação.
     * <p>
     * Executa a simulação chamando o método {@code iniciarSimulacao()} do {@link Simulador}.
     *
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        Simulador test = new Simulador();
        test.iniciarSimulacao();
    }
}
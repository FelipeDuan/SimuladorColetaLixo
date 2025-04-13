package simulador.caminhoes;

public class CaminhaoAnimacoes {
    public static void caminhaoAndando() throws InterruptedException {
        String[] caminhaoLixo = {
                "  _____________________________________________________                       ",
                " |                                                     |                      ",
                " |                                                     |  _______             ",
                " |                                                     | | _____ \\            ",
                " |    CAMINHÃO DE LIXO ♻️                             | ||[###] \\ \\           ",
                " |   ____________   ____________   ____________        | ||-------|           ",
                " |  |          | | |          | | |          | |       | || .-    |           ",
                " |  |   LIXO   | | |   LIXO   | | |   LIXO   | |       /  ||       \\   ____    ",
                " |  |__________|_|_|__________|_|_|__________|_|______|   ||_______|  / __ \\  ",
                " |_| /  \\ |_______________| /  \\ |_| /  \\ |_______________| /  \\ |__| /  \\ |  ",
                "    | () |                 | () |   | () |                 | () |    | () |   ",
                "     \\__/                   \\__/     \\__/                   \\__/      \\__/    "
        };

        int larguraTerminal = 80;
        int estradaComprimento = 200;

        // Gerando uma estrada com buracos e irregularidades
        String estrada = "";
        for (int i = 0; i < estradaComprimento; i++) {
            // Alternar entre '-' e '.' para criar buracos ou falhas
            estrada += (Math.random() > 0.2) ? "-" : ".";
        }

        int pos = 0;

        while (true) {
            for (int i = 0; i < larguraTerminal; i++) {
                // Limpar o console (ANSI escape codes)
                System.out.print("\033[H\033[2J");
                System.out.flush();

                String espacos = " ".repeat(i);

                // Imprimir o caminhão com indentação
                for (String linha : caminhaoLixo) {
                    System.out.println(espacos + linha);
                }

                // Criar e mostrar estrada irregular em movimento
                String faixa = estrada.substring(pos, pos + larguraTerminal);
                System.out.println(faixa);

                // Avançar a posição da estrada
                pos = (pos + 1) % (estradaComprimento - larguraTerminal);

                Thread.sleep(60); // velocidade do movimento
            }
        }
    }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.Normalizer;
import java.util.Scanner;

public class Viginere {

    private static String board[][] = new String[26][26];
    private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        generateBoard();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o arquivo de entrada: ");
        String path = scanner.nextLine();
        System.out.println("Digite a chave: ");
        String key = scanner.nextLine();
        encrypt(path, key);
        System.out.println("Criptografia concluida. Arquivo: TextoCriptografado.txt");
        decriptografia.executar("TextoCriptografado.txt");
        scanner.close();
    }

    // Remove acentos (á->a, ç->c, ã->a...), converte para minúsculas e mantém apenas a-z
    public static String higienizar(String texto) {
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toLowerCase().replaceAll("[^a-z]", "");
    }

    //Função Encrypt recebe o caminho para o arquivo com o texto em claro e a chave para a substituição
    //primeiro a função chama a função higienizar para remover acentos, transformar em minusculo e limpar tudo que não for letra
    //faz a validação da chave para garantir que ela tenha pelo menos uma letra válida antes de começar a criptografia
    //em seguida abre o arquivo de entrada para leitura e o arquivo de saída para escrever o texto cifrado
    //usa a variavel keyPos para controlar a posição da chave dentro da tabela de Vigenere
    //em um loop while, varre-se o arquivo em claro linha por linha, e dentro do loop interno percorre cada letra já limpa
    //pega a posição da letra do texto claro no alfabeto e a posição da letra da chave na mesma tabela
    //depois entra na matriz gerada pela tabela de Vigenere e busca a letra criptografada correspondente
    //escreve a letra cifrada no arquivo e incrementa a posição da chave para que a próxima letra use a próxima letra da chave
    public static void encrypt(String path, String key) {
        String cleanKey = higienizar(key);
        if (cleanKey.isEmpty()) {
            System.out.println("Chave invalida: use pelo menos uma letra (a-z).");
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            BufferedWriter writer = new BufferedWriter(new FileWriter("TextoCriptografado.txt"));
            String line = reader.readLine();
            int keyPos = 0;
            while (line != null) {
                String limpa = higienizar(line);
                for (int i = 0; i < limpa.length(); i++) {
                    int lineIndex = alphabet.indexOf(Character.toUpperCase(limpa.charAt(i)));
                    char keyChar = cleanKey.charAt(keyPos % cleanKey.length());
                    int keyIndex = alphabet.indexOf(Character.toUpperCase(keyChar));
                    writer.write(board[keyIndex][lineIndex].toLowerCase());
                    keyPos++;
                }
                line = reader.readLine();
            }
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Função que gera o quadro de Vigenere, aqui ele funciona da seguinte forma, primeira linha fica o alfabeto normal, a partir da segunda a primeira letra
    // passa para a ultima colocação e assim por diante.
    // utiliza a variavel row para não mexer com a global alphabet e atribui na matriz[][] apos o loop interno de inserções e vai para o externo e modifica row
    // nessa modificação pegamos a substrig a partir da segunda posição e concatenamos com a primeira letra. Inicia o loop novamente.
    public static void generateBoard() {
        String row = alphabet;
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                board[i][j] = String.valueOf(row.charAt(j));
            }
            row = row.substring(1) + row.charAt(0);
        }
    }
}

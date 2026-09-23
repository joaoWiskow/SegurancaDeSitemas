import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
public class Viginere{
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
    //Função Encrypt recebe o caminho para o arquivo com o texto em claro e a chave para a substituição
    //faz a limpeza da chave conforme o enunciado: converte para minusculo e remove tudo diferente de letras, como pontuação, espaços e numeros
    //Inicia o escritor para o arquivo criptografado
    //usa a variavel keyPos para controlar a posição da chave dentro da tabela de Vigenere
    //em um loop while, varre-se o arquivo em claro linha por linha, dentro do loop interno ele ignora caracteres diferentes de letras
    //Pega a posição da letra atual no alfabeto e a posição correspondente da letra da chave
    //Depois usa a matriz gerada pela tabela de Vigenere para encontrar a letra criptografada e escreve no arquivo de saída
    public static void encrypt(String path, String key) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            BufferedWriter writer = new BufferedWriter(new FileWriter("TextoCriptografado.txt"));
            String cleanKey = key.toLowerCase().replaceAll("[^a-z]", "");
            String line = reader.readLine();
            int keyPos = 0;
            while(line != null){
                for(int i = 0; i < line.length(); i++){
                    char original = Character.toLowerCase(line.charAt(i));
                    if(original < 'a' || original > 'z'){
                        continue;
                    }
                    int lineIndex = alphabet.indexOf(Character.toUpperCase(original));
                    char keyChar = cleanKey.charAt(keyPos % cleanKey.length());
                    int keyIndex = alphabet.indexOf(Character.toUpperCase(keyChar));
                    String cipher = board[keyIndex][lineIndex].toLowerCase();
                    writer.write(cipher);
                    writer.flush();
                    keyPos++;
                }
                line = reader.readLine();
            }
            reader.close();
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // Função que gera o quadro de Vigenere, aqui ele funciona da seguinte forma, primeira linha fica o alfabeto normal, a partir da segunda a primeira letra
    // passa para a ultima colocação e assim por diante.
    // utiliza a variavel row para não mexer com a global alphabet e atribui na matriz[][] apos o loop interno de inserções e vai para o externo e modifica row
    // nessa modificação pegamos a substrig a partir da segunda posição e concatenamos com a primeira letra. Inicia o loop novamente.
    public static void generateBoard(){
        String row = alphabet;
        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 26; j++){
                board[i][j] = String.valueOf(row.charAt(j));
            }
            row = row.substring(1) + row.charAt(0);
        }
    }
}

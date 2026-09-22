import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
    public static void encrypt(String path, String key) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            BufferedWriter writer = new BufferedWriter(new FileWriter("TextoCriptografado.txt"));
            // chave so com letras a-z
            String cleanKey = key.toLowerCase().replaceAll("[^a-z]", "");
            String line = reader.readLine();
            int keyPos = 0;
            while(line != null){
                //Ex Chave = casa frase = ola : c(2) e o(14)
                for(int i = 0; i < line.length(); i++){
                    char original = Character.toLowerCase(line.charAt(i));
                    // mantem apenas a-z; ignora espaco, pontuacao etc.
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

    public static void generateBoard(){
        // copia local para nao alterar alphabet usado no encrypt
        String row = alphabet;
        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 26; j++){
                board[i][j] = String.valueOf(row.charAt(j));
            }
            row = row.substring(1) + row.charAt(0);
        }
    }
}

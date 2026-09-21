import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
public class Viginere{
    private static String board[][] = new String[26][26];
    private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static void main(String[] args) {
        generateBoard();
        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 26; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        // arquivoTeste.txt fica na raiz do projeto (um nivel acima de T1/)
        encrypt("TextoClaro.txt", "BBBBBBBBBBCCCCCCAAAAAVVVV");
    }
    public static void encrypt(String path, String key) {
        try {
            File file = resolveFile(path);
            File outFile = resolveOutputFile("TextoCriptografado.txt");
            System.out.println("Procurando arquivo em: " + file.getAbsolutePath());
            System.out.println("Escrevendo em: " + outFile.getAbsolutePath());

            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            String line = reader.readLine();
            int keyPos = 0;
            while(line != null){
                //Ex Chave = casa frase = ola : c(2) e o(14)
                for(int i = 0; i < line.length(); i++){
                    char original = line.charAt(i);
                    // espaco, pontuacao etc. saem iguais e nao consomem a chave
                    if(Character.isWhitespace(original) || alphabet.indexOf(Character.toUpperCase(original)) == -1){
                        writer.write(original);
                        writer.flush();
                        continue;
                    }
                    char lineChar = Character.toUpperCase(original);
                    int lineIndex = alphabet.indexOf(lineChar);
                    char keyChar = Character.toUpperCase(key.charAt(keyPos % key.length()));
                    int keyIndex = alphabet.indexOf(keyChar);
                    String cipher = board[keyIndex][lineIndex];
                    
                    writer.write(cipher);
                    writer.flush();
                    keyPos++;
                }
                // readLine() ja remove a quebra de linha; devolve uma por linha lida
                writer.newLine();
                writer.flush();
                line = reader.readLine();
            }
            reader.close();
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    // Resolve caminho relativo tanto rodando da raiz quanto de dentro de T1/
    private static File resolveFile(String path) {
        File file = new File(path);
        if(file.isFile()){
            return file;
        }
        File fromT1 = new File(".." + File.separator + path);
        if(fromT1.isFile()){
            return fromT1;
        }
        File fromRoot = new File("T1" + File.separator + path);
        if(fromRoot.isFile()){
            return fromRoot;
        }
        // fallback: T1/TextoClaro (sem .txt), nome antigo no projeto
        String baseName = new File(path).getName();
        if(baseName.endsWith(".txt")){
            String withoutExt = baseName.substring(0, baseName.length() - 4);
            File legacyT1 = new File("T1" + File.separator + withoutExt);
            if(legacyT1.isFile()){
                return legacyT1;
            }
            File legacyFromT1 = new File(withoutExt);
            if(legacyFromT1.isFile()){
                return legacyFromT1;
            }
        }
        return file;
    }
    // Garante saida na raiz do projeto (mesmo padrao do arquivo de entrada)
    private static File resolveOutputFile(String path) {
        if(new File("TextoClaro.txt").isFile() || new File("arquivoTeste.txt").isFile()){
            return new File(path);
        }
        if(new File(".." + File.separator + "TextoClaro.txt").isFile()
                || new File(".." + File.separator + "arquivoTeste.txt").isFile()){
            return new File(".." + File.separator + path);
        }
        return new File(path);
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

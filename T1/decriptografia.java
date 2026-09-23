import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class decriptografia {
    // Frequencias tipicas do portugues
    private static final double[] FREQ_PT = {
        0.1463, 0.0104, 0.0388, 0.0499, 0.1257, 0.0102, 0.0130, 0.0128,
        0.0618, 0.0040, 0.0002, 0.0278, 0.0474, 0.0505, 0.1073, 0.0252,
        0.0120, 0.0653, 0.0781, 0.0434, 0.0463, 0.0167, 0.0001, 0.0021,
        0.0001, 0.0047
    };
    // Função que calcula o índice de coincidência (IC) do texto para verificar se ele teve uma cifra monoalfabética ou polialfabética
    // aqui o texto é normalizado, removendo todos os caracteres que não sejam letras e transformando tudo em maiúsculas
    // depois conta quantas vezes cada letra aparece e calcula o valor do IC pela fórmula: somatorio(fi * (fi-1)) / (n * (n-1))
    // esse valor ajuda a identificar padrões no texto e, no caso da cifra de Vigenere, indica a presença de repetições por coluna
    //Slide 27, é a segunda forma indicada pelo prof. Avelino para a criptoanalise
    public static double calcularIC(String texto) {
        // Normaliza: maiúsculas e remove tudo que não for letra
        texto = texto.toUpperCase().replaceAll("[^A-Z]", "");
        int n = texto.length();
        if (n <= 1) {
            return 0.0; // evita divisão por zero
        }
        // Conta a frequência de cada letra (f0 a f25)
        int[] frequencias = new int[26];
        for (char c : texto.toCharArray()) {
            frequencias[c - 'A']++;
        }
        // Soma de fi * (fi - 1) para i = 0 até 25
        long somatorio = 0;
        for (int i = 0; i <= 25; i++) {
            int fi = frequencias[i];
            somatorio += (long) fi * (fi - 1);
        }
        // IC = somatorio / (n * (n - 1))
        double ic = (double) somatorio / ((double) n * (n - 1));
        return ic;
    }

    // Função que testa tamanhos de chave de 1 até 10 para descobrir qual deles produz o maior índice de coincidência médio
    // o programa separa o texto em colunas conforme o tamanho da chave e calcula o IC de cada coluna
    // depois faz a média dos ICs das colunas e compara os valores para identificar o tamanho mais provável da chave
    // esse passo é importante porque, em Vigenere, as letras da mesma posição repetem o padrão da chave e geram uma média maior de coincidência
    //Slide 29-30 para a continuação da Criptoanalise
    public static int testarTamanhosChave(String texto) {
        texto = texto.toUpperCase().replaceAll("[^A-Z]", "");
        double melhorIC = -1;
        int melhorTam = 1;
        for (int tam = 1; tam <= 10; tam++) {
            double somaIC = 0.0;
            for (int col = 0; col < tam; col++) {
                StringBuilder coluna = new StringBuilder();
                for (int i = col; i < texto.length(); i += tam) {
                    coluna.append(texto.charAt(i));
                }
                somaIC += calcularIC(coluna.toString());
            }
            double icMedio = somaIC / tam;
            if (icMedio > melhorIC) {
                melhorIC = icMedio;
                melhorTam = tam;
            }
        }
        System.out.println("Provavel tamanho de chave: " + melhorTam
                + " (maior IC medio = " + String.format("%.6f", melhorIC) + ")");
        return melhorTam;
    }

    // Função que descobre o deslocamento da letra da chave para uma coluna específica do texto cifrado
    // cada coluna representa as letras cifradas por uma mesma posição da chave, então o programa tenta todos os deslocamentos de 0 a 25
    // usa a análise de frequência do português para comparar a coluna com o padrão esperado da língua
    // quando o valor chi-quadrado for menor, significa que a coluna está mais próxima da distribuição natural do idioma
    // Slide 31
    public static int descobrirLetraChave(String coluna) {
        coluna = coluna.toUpperCase().replaceAll("[^A-Z]", "");
        int n = coluna.length();
        if (n == 0) {
            return 0;
        }
        int melhorShift = 0;
        double menorChi = Double.MAX_VALUE;
        for (int shift = 0; shift < 26; shift++) {
            int[] freq = new int[26];
            for (int i = 0; i < n; i++) {
                int dec = (coluna.charAt(i) - 'A' - shift + 26) % 26;
                freq[dec]++;
            }
            double chi = 0.0;
            for (int i = 0; i < 26; i++) {
                double esperado = FREQ_PT[i] * n;
                if (esperado > 0) {
                    double diff = freq[i] - esperado;
                    chi += (diff * diff) / esperado;
                }
            }
            if (chi < menorChi) {
                menorChi = chi;
                melhorShift = shift;
            }
        }
        return melhorShift;
    }

    // Função que lê o conteúdo de um arquivo texto e armazena em uma String
    // ela percorre linha por linha até o final do arquivo e concatena tudo em um único texto para ser processado
    public static String lerArquivo(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            line = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

    // Função que grava o texto decifrado em um arquivo de saída
    // recebe o caminho do arquivo e o conteúdo que será escrito
    public static void escreverArquivo(String path, String conteudo) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(conteudo);
        writer.close();
    }

    // Função que descobre a chave completa do ciframento Vigenere a partir do texto cifrado
    // primeiro separa o texto em colunas com base no tamanho da chave, depois analisa cada coluna para encontrar o deslocamento correspondente
    // ao final, concatena todas as letras descobertas e forma a chave completa
    public static String descobrirChave(String textoCifrado, int tamanhoChave) {
        String texto = textoCifrado.toUpperCase().replaceAll("[^A-Z]", "");
        StringBuilder[] colunas = new StringBuilder[tamanhoChave];
        for (int i = 0; i < tamanhoChave; i++) {
            colunas[i] = new StringBuilder();
        }
        for (int i = 0; i < texto.length(); i++) {
            colunas[i % tamanhoChave].append(texto.charAt(i));
        }
        StringBuilder chave = new StringBuilder();
        for (StringBuilder coluna : colunas) {
            int letra = descobrirLetraChave(coluna.toString());
            chave.append((char) ('A' + letra));
        }
        return chave.toString();
    }

    // Função que decifra o texto cifrado usando a chave descoberta
    // para cada letra do texto, calcula a diferença entre a posição da letra cifrada e a posição da letra da chave
    // depois faz a operação inversa da cifragem para recuperar a letra original
    public static String decifrar(String textoCifrado, String chave) {
        String texto = textoCifrado.toUpperCase().replaceAll("[^A-Z]", "");
        chave = chave.toUpperCase().replaceAll("[^A-Z]", "");
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            int c = texto.charAt(i) - 'A';
            int k = chave.charAt(i % chave.length()) - 'A';
            int original = (c - k + 26) % 26;
            resultado.append((char) ('A' + original));
        }
        return resultado.toString();
    }

    // Função principal da decriptografia
    // aqui o programa lê o arquivo cifrado, calcula o IC, testa os tamanhos possíveis da chave, descobre a chave e decifra o texto
    // ao final grava o resultado em um arquivo chamado TextoDecifrado.txt e mostra uma amostra do conteúdo
    public static void executar(String path) {
        try {
            System.out.println("--- Iniciando decriptografia ---");
            String texto = lerArquivo(path);
            System.out.println("IC do texto completo: " + calcularIC(texto));
            int tamanho = testarTamanhosChave(texto);
            String chave = descobrirChave(texto, tamanho);
            System.out.println("Chave descoberta: " + chave);
            String claro = decifrar(texto, chave);
            escreverArquivo("TextoDecifrado.txt", claro.toLowerCase());
            System.out.println("Texto decifrado gravado em TextoDecifrado.txt");
            System.out.println("Amostra: " + claro.substring(0, Math.min(120, claro.length())).toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Função main do programa
    // solicita ao usuário o caminho do arquivo cifrado e chama a função executar para iniciar a análise e a decifração
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o arquivo cifrado: ");
        String path = scanner.nextLine().trim();
        executar(path);
        scanner.close();
    }
}

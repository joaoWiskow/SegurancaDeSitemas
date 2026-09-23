# Cifra de Vigenère: Cifragem e Criptoanálise


## Como compilar

As duas classes precisam ser compiladas, na mesma pasta:

```bash
javac Viginere.java decriptografia.java
```

## Como executar

```bash
java Viginere
```

O programa pede duas informações:

1. **Arquivo de entrada**: o caminho do arquivo com o texto a ser criptografado (por exemplo, `TextoClaro.txt`).
2. **Chave**: a palavra usada na cifragem (por exemplo, `São Paulo`).

Depois disso, o programa:

1. Cifra o texto e grava em `TextoCriptografado.txt`.
2. Executa automaticamente a decriptografia sobre esse arquivo, mostrando o tamanho de chave provável e a chave descoberta.
3. Grava o texto decifrado em `TextoDecifrado.txt`.


## Executar só a decriptografia

Para quebrar um arquivo já cifrado:

```bash
java decriptografia
```

Informe o caminho do arquivo cifrado quando solicitado.

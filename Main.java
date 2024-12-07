import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String[] files = {
            ".txt/MobyDick-217452.txt",
            ".txt/Dracula-165307.txt",
            ".txt/DonQuixote-388208.txt"
        };

        String targetWord = readTargetWord(".txt/palavra.txt");
        if (targetWord == null) {
            System.err.println("Erro ao ler a palavra-alvo do arquivo 'palavra.txt'.");
            return;
        }

        System.out.println("Executando SerialCPU...");
        SerialCPU.processFiles(files, targetWord);

        System.out.println("Executando ParallelCPU...");
        ParallelCPU.processFiles(files, targetWord);

        System.out.println("Executando ParallelGPU...");
        ParallelGPU.processFiles(files, targetWord);

        runPythonScript("graphs.py");
    }

    private static void runPythonScript(String scriptPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
            pb.directory(new File("."));
            pb.redirectErrorStream(true);
            Process process = pb.start();

            process.getInputStream().transferTo(System.out);

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Script Python executado com sucesso.");
            } else {
                System.err.println("Erro ao executar o script Python. Código de saída: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lê a palavra-alvo a partir do arquivo "palavra.txt".
     * 
     * @param filePath Caminho do arquivo contendo a palavra-alvo.
     * @return A palavra-alvo como uma String, ou null se houver erro.
     */
    private static String readTargetWord(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();

            if (line != null && line.trim().split("\\s+").length == 1) {
                return line.trim();
            } else {
                System.err.println("Erro: O arquivo 'palavra.txt' deve conter apenas uma palavra.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao abrir o arquivo 'palavra.txt': " + e.getMessage());
        }
        return null;
    }
}
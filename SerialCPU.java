import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SerialCPU {

    public static void processFiles(String[] files, String targetWord) {
        targetWord = targetWord.toLowerCase();

        try (FileWriter csvWriter = new FileWriter(".csv/serial_word_count_results.csv")) {
            csvWriter.append("Arquivo,Amostra,Ocorrencias,Tempo de Execucao (ms)\n");

            for (String file : files) {
                for (int sample = 0; sample < 3; sample++) {
                    long startTime = System.currentTimeMillis();
                    int count = countWordOccurrences(file, targetWord);
                    long endTime = System.currentTimeMillis();

                    long elapsedTime = endTime - startTime;

                    csvWriter.append(file + "," + (sample + 1) + "," + count + "," + elapsedTime + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int countWordOccurrences(String filePath, String targetWord) {
        int wordCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.toLowerCase().split("\\W+");
                for (String word : words) {
                    if (word.equals(targetWord)) {
                        wordCount++;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + filePath);
            e.printStackTrace();
        }

        return wordCount;
    }
}
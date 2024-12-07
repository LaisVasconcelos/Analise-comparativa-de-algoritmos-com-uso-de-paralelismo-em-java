import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelCPU {

    public static void processFiles(String[] files, String targetWord) {
        targetWord = targetWord.toLowerCase();

        try (FileWriter csvWriter = new FileWriter(".csv/parallel_cpu_word_count_results.csv")) {
            csvWriter.append("Arquivo,Amostra,Ocorrencias,Tempo de Execucao (ms)\n");

            for (String file : files) {
                for (int sample = 0; sample < 3; sample++) {
                    long startTime = System.currentTimeMillis();
                    int count = countWordOccurrencesParallel(file, targetWord, Runtime.getRuntime().availableProcessors());
                    long endTime = System.currentTimeMillis();

                    long elapsedTime = endTime - startTime;

                    csvWriter.append(file + "," + (sample + 1) + "," + count + "," + elapsedTime + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int countWordOccurrencesParallel(String filePath, String targetWord, int numThreads) {
        AtomicInteger wordCount = new AtomicInteger();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            for (String line : lines) {
                executor.submit(() -> {
                    String[] words = line.toLowerCase().split("\\W+");
                    for (String word : words) {
                        if (word.equals(targetWord)) {
                            wordCount.incrementAndGet();
                        }
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao processar o arquivo: " + filePath);
            e.printStackTrace();
        }

        return wordCount.get();
    }
}
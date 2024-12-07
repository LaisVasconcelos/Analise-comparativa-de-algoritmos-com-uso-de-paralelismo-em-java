import org.jocl.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.jocl.CL.*;

public class ParallelGPU {

    /**
     * Conta as ocorrências de uma palavra-alvo em um arquivo usando GPU via OpenCL.
     *
     * @param filePath   Caminho do arquivo de texto.
     * @param targetWord Palavra-alvo a ser buscada.
     * @return Número de ocorrências da palavra-alvo no arquivo.
     */
    public static int countWordOccurrencesGPU(String filePath, String targetWord) {
        int occurrences = 0;
        targetWord = targetWord.toLowerCase();

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath))).toLowerCase();
            byte[] contentBytes = content.getBytes();
            byte[] targetBytes = targetWord.getBytes();

            int contentLength = contentBytes.length;
            int targetLength = targetBytes.length;

            int[] resultArray = new int[contentLength];
            Arrays.fill(resultArray, 0);

            CL.setExceptionsEnabled(true);
            cl_platform_id[] platforms = new cl_platform_id[1];
            clGetPlatformIDs(1, platforms, null);
            cl_platform_id platform = platforms[0];

            cl_device_id[] devices = new cl_device_id[1];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, 1, devices, null);
            cl_device_id device = devices[0];

            cl_context context = clCreateContext(null, 1, devices, null, null, null);
            cl_command_queue commandQueue = clCreateCommandQueueWithProperties(context, device, null, null);

            String kernelSource = new String(Files.readAllBytes(Paths.get("word_count_kernel.cl")));
            cl_program program = clCreateProgramWithSource(context, 1, new String[]{kernelSource}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            cl_kernel kernel = clCreateKernel(program, "countOccurrences", null);

            cl_mem contentBuffer = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_char * contentLength, Pointer.to(contentBytes), null);
            cl_mem targetBuffer = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_char * targetLength, Pointer.to(targetBytes), null);
            cl_mem resultBuffer = clCreateBuffer(context, CL_MEM_WRITE_ONLY,
                    Sizeof.cl_int * contentLength, null, null);

            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(contentBuffer));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(targetBuffer));
            clSetKernelArg(kernel, 2, Sizeof.cl_int, Pointer.to(new int[]{targetLength}));
            clSetKernelArg(kernel, 3, Sizeof.cl_mem, Pointer.to(resultBuffer));

            long globalWorkSize = contentLength;
            long[] maxWorkGroupSize = new long[1];
            clGetDeviceInfo(device, CL_DEVICE_MAX_WORK_GROUP_SIZE, Sizeof.size_t, Pointer.to(maxWorkGroupSize), null);
            long localWorkSize = Math.min(maxWorkGroupSize[0], globalWorkSize);
            globalWorkSize = (globalWorkSize / localWorkSize) * localWorkSize;

            clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                    new long[]{globalWorkSize}, new long[]{localWorkSize}, 0, null, null);

            clEnqueueReadBuffer(commandQueue, resultBuffer, CL_TRUE, 0,
                    Sizeof.cl_int * contentLength, Pointer.to(resultArray), 0, null, null);

            occurrences = Arrays.stream(resultArray).sum();

            clReleaseMemObject(contentBuffer);
            clReleaseMemObject(targetBuffer);
            clReleaseMemObject(resultBuffer);
            clReleaseKernel(kernel);
            clReleaseProgram(program);
            clReleaseCommandQueue(commandQueue);
            clReleaseContext(context);

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo ou kernel: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro durante a execução do OpenCL: ");
            e.printStackTrace();
        }

        return occurrences;
    }

    /**
     * Processa múltiplos arquivos e gera um arquivo CSV com os resultados.
     *
     * @param files      Lista de arquivos de texto a serem processados.
     * @param targetWord Palavra-alvo a ser buscada.
     */
    public static void processFiles(String[] files, String targetWord) {
        String outputCsv = ".csv/parallel_gpu_word_count_results.csv";
        int numSamples = 3;
        targetWord = targetWord.toLowerCase();

        try (FileWriter writer = new FileWriter(outputCsv)) {
            writer.append("Arquivo,Amostra,Ocorrencias,Tempo de Execucao (ms)\n");

            for (String file : files) {
                for (int sample = 1; sample <= numSamples; sample++) {
                    long startTime = System.currentTimeMillis();
                    int occurrences = countWordOccurrencesGPU(file, targetWord);
                    long endTime = System.currentTimeMillis();
                    long elapsedTime = endTime - startTime;

                    writer.append(file + "," + sample + "," + occurrences + "," + elapsedTime + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever o arquivo CSV: " + outputCsv);
            e.printStackTrace();
        }
    }
}

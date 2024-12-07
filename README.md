# Análise Comparativa de Algoritmos com Uso de Paralelismo

## Resumo

Este trabalho propõe uma análise detalhada do desempenho de diferentes algoritmos de busca em ambientes seriais e paralelos, utilizando a linguagem de programação Java. A busca por eficiência computacional é essencial em diversas aplicações, e entender como diferentes algoritmos se comportam em diferentes cenários de processamento é de suma importância. Neste estudo, serão abordados três algoritmos: **versão serial na CPU**, **versão paralela na CPU** e **versão paralela na GPU**.

Serão realizadas análises comparativas utilizando textos como conjuntos de dados de entrada para a contagem de ocorrências de uma palavra. Os resultados serão registrados em arquivos CSV, permitindo uma análise visual através de gráficos ou processamento adicional utilizando Java.

## Objetivos

1. **Implementar três métodos para a contagem de palavras**:
    - **Método SerialCPU**: Versão serial na CPU, que utiliza um loop simples para iterar sobre cada palavra do texto e contar as ocorrências.
    - **Método ParallelCPU**: Versão paralela na CPU, que utiliza um pool de threads para dividir o texto em partes e contar as palavras em paralelo.
    - **Método ParallelGPU**: Versão paralela na GPU, utilizando **OpenCL** para processar o texto em paralelo e contar as palavras de forma eficiente.

2. **Realizar análises de desempenho comparativas** entre as versões serial e paralela dos algoritmos de contagem, utilizando diferentes conjuntos de dados.
   
    - Variar o tamanho e a natureza dos conjuntos de dados de entrada.
    - Realizar uma execução Serial, paralela com CPU e paralela com GPU.
    - Realizar pelo menos 3 amostras de cada execução, para gerar dados suficientes para a análise.

3. **Investigar o comportamento dos algoritmos** sob diferentes configurações de processamento paralelo, ajustando o número de núcleos de processamento disponíveis.

4. **Gerar arquivos CSV** contendo os resultados das análises para facilitar a visualização e o processamento posterior.

5. **Gerar gráficos** para indicar as execuções com entradas de dados diferentes, utilizando ferramentas como Swing Java, Jupyter ou JSF (Prime Faces).

## Metodologia

### Implementação de Algoritmos
- **Serial**: A versão serial realiza a contagem de palavras sequencialmente, utilizando um loop simples para iterar pelas palavras do texto.
- **Paralela na CPU**: A versão paralela na CPU divide o texto em partes, utilizando um pool de threads para processar diferentes partes simultaneamente.
- **Paralela na GPU**: A versão paralela na GPU usa a biblioteca OpenCL para realizar a contagem de palavras de forma distribuída na GPU.

### Framework de Teste
Foi desenvolvido um framework de teste que executa os três métodos em diversos conjuntos de dados. O framework registra os tempos de execução e salva os resultados em arquivos CSV, que posteriormente são utilizados para gerar gráficos de análise.

### Execução em Ambientes Variados
Para testar o impacto do tamanho dos conjuntos de dados, os testes foram executados com diferentes textos de tamanhos variados. Além disso, o número de threads foi ajustado na versão paralela para observar o impacto no desempenho.

### Registro de Dados
Os resultados de tempo de execução de cada método foram armazenados em arquivos CSV para facilitar a visualização. Esses arquivos contêm informações como o número de ocorrências encontradas e o tempo de execução.

### Análise Estatística
Uma análise estatística foi realizada para identificar padrões de desempenho entre os diferentes algoritmos, considerando o impacto do tamanho do conjunto de dados e a configuração de processamento.

## Resultados e Discussão

| **Método**       | **Ocorrências Encontradas** | **Tempo de Execução** |
|------------------|-----------------------------|-----------------------|
| SerialCPU        | 66                          | 133 ms                |
| ParallelCPU      | 66                          | 82 ms                 |
| ParallelGPU      | 66                          | 2705 ms               |

- A versão **serial** teve um desempenho razoável, mas o tempo de execução foi significativamente maior em comparação com a versão paralela na CPU.
- A versão **paralela na CPU** apresentou um desempenho consideravelmente melhor, com tempos de execução reduzidos devido à utilização de múltiplos núcleos de processamento.
- A versão **paralela na GPU** teve um tempo de execução muito mais alto, devido à sobrecarga de processamento necessária para mover os dados para a GPU e aguardar sua execução.

Os gráficos mostraram que, conforme o tamanho do conjunto de dados aumentava, a versão paralela na CPU se destacava, enquanto a versão paralela na GPU apresentava um aumento de tempo de execução devido à latência na comunicação com a GPU.

## Conclusão

Este estudo demonstrou que os algoritmos paralelos em CPU proporcionam uma melhoria significativa no desempenho em comparação com a versão serial, especialmente quando lidamos com grandes volumes de dados. A implementação na GPU, embora poderosa, exigiu mais tempo devido à sobrecarga de movimentação dos dados para a GPU e comunicação entre os dispositivos.

Além disso, os testes de desempenho evidenciaram que o número de threads na versão paralela na CPU tem um impacto positivo na performance, com melhores resultados em sistemas com múltiplos núcleos de processamento.

Os arquivos CSV gerados e os gráficos de análise fornecem uma maneira eficaz de visualizar e comparar os resultados, facilitando a identificação de padrões de desempenho.

## Referências

- OpenCL Specification, Khronos Group, 2020.
- Java Concurrency in Practice, Brian Goetz, 2006.
- The Art of Multiprocessor Programming, Maurice Herlihy, Nir Shavit, 2009.

## Anexos

- **Códigos de Implementação**: O código completo está disponível no [GitHub](https://github.com/usuario/WordCounter).
  

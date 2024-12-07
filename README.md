# Relatório do Projeto: Análise de Desempenho de Algoritmos de Busca em CPU e GPU

## Autores
- Tiago Farias Rodrigues - 1914187
- Ana Laís Nunes de Vasconcelos - 2216984

## Resumo

Este trabalho explora o desempenho de algoritmos de busca seriais e paralelos em diferentes ambientes computacionais. Implementamos três abordagens: busca serial, busca paralela em CPU e busca paralela em GPU, utilizando Java e OpenCL para processamento em GPU. Os resultados foram registrados em arquivos CSV e visualizados por meio de gráficos. O projeto aborda as vantagens e limitações de cada abordagem, identificando padrões de desempenho e fornecendo insights para aplicações futuras.

---

## Introdução

A eficiência no processamento de grandes volumes de dados é essencial em diversas áreas da computação. Este trabalho visa analisar o desempenho de três abordagens distintas para busca em textos: busca serial na CPU, busca paralela utilizando multithreading na CPU e busca paralela na GPU. O objetivo é comparar os tempos de execução e a precisão dos resultados obtidos por cada abordagem.

A abordagem GPU utilizou OpenCL para distribuir o trabalho em paralelo entre os núcleos da GPU, enquanto as abordagens na CPU usaram Java, com a implementação de um pool de threads para divisão do trabalho na versão paralela.

---

## Metodologia

Para realizar este estudo, seguimos os seguintes passos:

1. **Implementação de Algoritmos:** Desenvolvemos três algoritmos de busca: SerialCPU, ParallelCPU e ParallelGPU.
2. **Framework de Teste:** Criamos uma estrutura para executar as buscas e registrar os tempos de execução e o número de ocorrências encontradas.
3. **Execução em Diferentes Ambientes:** Executamos os algoritmos em três arquivos de texto distintos para observar como o desempenho varia.
4. **Registro de Dados:** Os tempos de execução e as ocorrências foram armazenados em arquivos CSV para posterior análise.
5. **Análise Estatística:** Comparamos os tempos médios de execução de cada abordagem, identificando padrões de desempenho e limitações.

---

## Resultados e Discussão

Os resultados mostraram que a busca em GPU apresentou maior precisão e desempenho comparado às implementações de CPU. No entanto, a busca em CPU enfrentou problemas de precisão, especialmente ao lidar com palavras comuns como "the".

### Observações:

## Problemas Identificados

### Discussão sobre o Problema na Busca por CPU

Durante o desenvolvimento do projeto, foi identificado um problema significativo na forma como a contagem de palavras é realizada nos métodos **SerialCPU** e **ParallelCPU**. Esse problema está relacionado à maneira como as palavras são separadas e comparadas, resultando em uma contagem imprecisa. Em contrapartida, a abordagem **ParallelGPU** demonstrou resultados consistentes e precisos.

#### Identificação do Problema

Os métodos da CPU (tanto serial quanto paralelo) utilizam a estratégia de **separação de palavras** por meio de expressões regulares (`split("\\W+")`) e realizam a comparação com a palavra-alvo usando uma lógica de igualdade case-insensitive. No entanto, essa abordagem apresenta limitações:

1. **Separação Incompleta por Expressão Regular**:
    - A expressão regular `\\W+` separa o texto em palavras com base em caracteres não alfanuméricos. Contudo, em casos onde há caracteres especiais, como apóstrofos, hífens ou pontuação adjacente à palavra-alvo, o método pode falhar.
    - Exemplos de falhas:
        - `"whale,"` não será identificado como "whale".
        - `"whale's"` será tratado como uma palavra diferente.
2. **Fragmentação de Texto**:
    - Linhas de texto são processadas individualmente, o que pode ignorar palavras que estejam divididas por quebras de linha ou em contextos ambíguos.
3. **Eficiência e Precisão**:
    - O método **ParallelCPU**, ao dividir o texto em blocos e atribuí-los a threads, pode falhar em identificar palavras que cruzam os limites dos blocos.
    - A busca sequencial em **SerialCPU** sofre com os mesmos problemas de separação e também com a ausência de paralelismo, resultando em maior tempo de execução.

#### Contraste com a GPU

A implementação **ParallelGPU** se mostrou mais robusta devido ao uso de **OpenCL**, que processa o texto em paralelo diretamente na GPU. Nesse método:
- O texto é tratado como uma sequência contínua de caracteres, permitindo identificar palavras independentemente de pontuações ou divisões artificiais.
- A lógica de busca na GPU utiliza comparações diretas entre caracteres, garantindo maior precisão e consistência na contagem.

#### Exemplos de Inconsistência

Nos resultados coletados:
- O método **ParallelGPU** identificou corretamente **20,137 ocorrências** de "the" no arquivo `MobyDick-217452.txt`, enquanto os métodos **SerialCPU** e **ParallelCPU** apresentaram contagens significativamente menores, como **14727 ocorrências** ou outras discrepâncias.

Esses erros indicam que os métodos da CPU não conseguem lidar adequadamente com a separação e comparação de palavras, especialmente em textos com formatação complexa.

---

## Conclusão

Este trabalho evidenciou as vantagens e desvantagens de cada abordagem de busca. Embora a GPU tenha apresentado maior precisão, o custo em tempo de execução foi elevado. As abordagens em CPU, apesar de mais rápidas, requerem melhorias na lógica de separação e contagem para garantir resultados precisos.

Os insights obtidos fornecem diretrizes valiosas para a escolha da abordagem mais adequada, dependendo do contexto e das prioridades de precisão versus tempo.

---

## Bibliotecas Necessárias

Para executar este projeto, foram utilizadas as seguintes bibliotecas:

1. **Java Core:** Para a lógica principal do projeto.
2. **JOCL (Java OpenCL):** Para execução paralela em GPU.
3. **Matplotlib e Pandas (Python):** Para a geração de gráficos baseados nos arquivos CSV.
4. **JDK 21.0.1:** Ambiente de desenvolvimento para compilar e executar os programas.

---

## Referências

1. Documentação oficial do JOCL: [JOCL](http://www.jocl.org/)
2. Documentação do OpenJDK: [OpenJDK](https://openjdk.org/)
3. Referências de gráficos em Python: [Matplotlib](https://matplotlib.org/)

---

## Anexos

### Códigos Fonte
Os códigos-fonte completos estão disponíveis no repositório do projeto no GitHub:

[Repositório no GitHub](https://github.com/LaisVasconcelos/Analise-comparativa-de-algoritmos-com-uso-de-paralelismo-em-java)

### Scripts Python

O script `graphs.py` foi utilizado para gerar os gráficos baseados nos dados coletados.

### Dados de Entrada

Os textos utilizados foram:

- `MobyDick-217452.txt`
- `Dracula-165307.txt`
- `DonQuixote-388208.txt`

---

### **Passo a Passo**
1. **Prepare o Ambiente:**
   - Certifique-se de que o JDK está instalado e configurado corretamente no `JAVA_HOME`.
   - Instale o Python e as bibliotecas necessárias com:
     ```pip install matplotlib pandas```

2. **Compile os Códigos em Java:**
   - Navegue até o diretório do projeto e compile os arquivos `.java` com o seguinte comando:
     ```javac -d .class -cp ".;jocl-2.0.4.jar" SerialCPU.java ParallelCPU.java ParallelGPU.java Main.java```

3. **Insira a Palavra-Alvo:**
   - Edite o arquivo `palavra.txt` e insira a palavra que deseja buscar (somente uma palavra).

4. **Execute a Main:**
   - Use o comando abaixo para rodar o programa principal:
     ```java -cp ".class;jocl-2.0.4.jar" Main```

---

import pandas as pd
import matplotlib.pyplot as plt
import os

# Carregar os resultados dos métodos a partir dos CSVs
serial_results = pd.read_csv('.csv/serial_word_count_results.csv')
parallel_cpu_results = pd.read_csv('.csv/parallel_cpu_word_count_results.csv')
parallel_gpu_results = pd.read_csv('.csv/parallel_gpu_word_count_results.csv')

def compute_avg_results(df):
    df['Arquivo'] = df['Arquivo'].apply(lambda x: os.path.splitext(os.path.basename(x))[0])  # Apenas o nome do arquivo
    return df.groupby('Arquivo')[['Ocorrencias', 'Tempo de Execucao (ms)']].mean().reset_index()

serial_avg = compute_avg_results(serial_results)
parallel_cpu_avg = compute_avg_results(parallel_cpu_results)
parallel_gpu_avg = compute_avg_results(parallel_gpu_results)

# Criar o gráfico com todas as informações
plt.figure(figsize=(12, 8))

# Função para adicionar rótulos aos pontos
def add_labels(x, y, labels, color):
    for i, label in enumerate(labels):
        plt.text(x[i], y[i], label, fontsize=10, color=color, ha='right', va='bottom')

# Plotar linhas para Serial CPU
plt.plot(serial_avg['Ocorrencias'], serial_avg['Tempo de Execucao (ms)'], label='Serial CPU', marker='o', linestyle='-', color='blue')
add_labels(serial_avg['Ocorrencias'], serial_avg['Tempo de Execucao (ms)'], serial_avg['Arquivo'], 'blue')

# Plotar linhas para Parallel CPU
plt.plot(parallel_cpu_avg['Ocorrencias'], parallel_cpu_avg['Tempo de Execucao (ms)'], label='Parallel CPU', marker='o', linestyle='-', color='orange')
add_labels(parallel_cpu_avg['Ocorrencias'], parallel_cpu_avg['Tempo de Execucao (ms)'], parallel_cpu_avg['Arquivo'], 'orange')

# Plotar linhas para Parallel GPU
plt.plot(parallel_gpu_avg['Ocorrencias'], parallel_gpu_avg['Tempo de Execucao (ms)'], label='Parallel GPU', marker='o', linestyle='-', color='green')
add_labels(parallel_gpu_avg['Ocorrencias'], parallel_gpu_avg['Tempo de Execucao (ms)'], parallel_gpu_avg['Arquivo'], 'green')

# Adicionar informações ao gráfico
plt.title('Comparação de Métodos (Serial vs Parallel CPU vs Parallel GPU)', fontsize=16)
plt.xlabel('Número de Ocorrências', fontsize=12)
plt.ylabel('Tempo Médio de Execução (ms)', fontsize=12)
plt.xticks(fontsize=10)
plt.legend(fontsize=12)
plt.grid(True, linestyle='--', alpha=0.6)

# Mostrar o gráfico
plt.tight_layout()
plt.show()
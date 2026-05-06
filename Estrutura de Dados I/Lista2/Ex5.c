#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <windows.h>

#define MAX 100

typedef struct {
    char licensePlates[MAX][MAX];
    int start, end, queueSize;
} Queue;

void push(Queue *queue, char licensePlate[], bool showMessage) {
    if (queue->queueSize == MAX) {
        puts("\n[!] A fila esta cheia.");
        return;
    }

    strcpy(queue->licensePlates[queue->end], licensePlate);

    queue->end = (queue->end + 1) % MAX;
    (queue->queueSize)++;

    if (showMessage) {
        printf("\nCarro com a placa '%s' adicionado com sucesso.\n", licensePlate);
    }
}

void pop(Queue *queue, char *target, bool saveToTarget) {
    if (queue->queueSize == 0) {
        puts("\n[!] Sem elementos para remover.");
        return;
    }

    if (saveToTarget) {
        strcpy(target, queue->licensePlates[queue->start]);
    }

    queue->start = (queue->start + 1) % MAX;
    (queue->queueSize)--;
}

Queue startQueue() {
    Queue queue;

    queue.start = 0;
    queue.end = 0;
    queue.queueSize = 0;

    return queue;
}

void releaseCars(Queue *queue, char direction[], int amount) {
    printf("\n--- SEMAFORO ABERTO NA DIRECAO %s ---\n", direction);
    int totalReleased = 0;

    while (totalReleased < amount && queue->queueSize > 0) {
        char car[MAX];

        pop(queue, car, true);

        printf("Carro com a placa '%s' passou pelo cruzamento.\n", car);
        totalReleased++;
        Sleep(300);
    }

    if (totalReleased == 0) {
        printf("\nNenhum carro estava na fila.\n");
    } else {
        printf("\nTotal de carros liberados: %d\n", totalReleased);
    }
}

void displayQueue(Queue *queue) {
    if (queue->queueSize == 0) {
        return;
    }

    Queue tempQueue = startQueue();
    int position = 1;

    while (queue->queueSize > 0) {
        char licensePlate[MAX];

        strcpy(licensePlate, queue->licensePlates[queue->start]);

        push(&tempQueue, licensePlate, false);
        pop(queue, NULL, false);

        printf("[%02d] Placa: %-8s\n", position, licensePlate);
    }

    while (tempQueue.queueSize > 0) {
        push(queue, tempQueue.licensePlates[tempQueue.start], false);
        pop(&tempQueue, NULL, false);
    }
}

void status(Queue *NS, Queue *LO) {
    if (NS->queueSize == 0 && LO->queueSize == 0) {
        puts("\nFluxo livre: nenhuma fila possui veiculos aguardando.");
        return;
    }
    
    if (NS->queueSize > 0) {
        puts("\n------Norte|Sul------");
        displayQueue(NS);
    }

    if (LO->queueSize > 0) {
        puts("\n------Leste|Oeste------");
        displayQueue(LO);
    }
}

bool handleCommand(Queue *NS, Queue *LO, char line[]) {
    line[strcspn(line, "\n")] = 0;
    char *command = strtok(line, " ");

    if (command == NULL) {
        puts("\n[!] Sintaxe de comando invalida.");
        return 0;
    }

    if (strcasecmp(command, "chega") == 0) {
        char *queueDirection = strtok(NULL, " ");
        char *licensePlate = strtok(NULL, " ");

        if (queueDirection == NULL || licensePlate == NULL) {
            puts("\n[!] Parametros insuficientes. Uso: chega <sentido: NS|LO> <placa_carro>");
            return 0;
        }

        if (strcasecmp(queueDirection, "NS") != 0 && strcasecmp(queueDirection, "LO") != 0) {
            puts("\n[!] Entrada invalida. Certifique-se de utilizar 'NS' ou 'LO' como direcao.");
            return 0;
        }

        if (strcasecmp(queueDirection, "NS") == 0) {
            push(NS, licensePlate, true);
        } else {
            push(LO, licensePlate, true);
        }
        return 0;
    }

    if (strcasecmp(command, "abrir") == 0) {
        char *queueDirection = strtok(NULL, " ");
        char *amountString = strtok(NULL, " ");

        if (queueDirection == NULL || amountString == NULL) {
            puts("\n[!] Parametros insuficientes. Uso: abrir <sentido: NS|LO> <limite_carros>");
            return 0;
        }

        if (strcasecmp(queueDirection, "NS") != 0 && strcasecmp(queueDirection, "LO") != 0) {
            puts("\n[!] Entrada invalida. Certifique-se de utilizar 'NS' ou 'LO' como direcao.");
            return 0;
        }

        char *ptr;
        long amount = strtol(amountString, &ptr, 10);
        if (ptr == amountString || amount <= 0) {
            puts("\n[!] Entrada invalida. Certifique-se de que o numero de carros seja um inteiro positivo.");
            return 0;
        }

        if (strcasecmp(queueDirection, "NS") == 0) {
            releaseCars(NS, queueDirection, amount);
        } else {
            releaseCars(LO, queueDirection, amount);
        }
        return 0;
    }

    if (strcasecmp(command, "status") == 0) {
        status(NS, LO);
        return 0;
    }

    if (strcasecmp(command, "parar") == 0) {
        return 1;
    }

    printf("\n[!] O comando '%s' nao existe.\n", command);
    return 0;
}

void pauseAndClear() {
    printf("\nPressione ENTER para continuar...");
    fflush(stdin);
    getchar();
    system("cls");
}

int main() {
    Queue NS = startQueue();
    Queue LO = startQueue();
    bool end = false;
    char line[MAX];

    do {
        printf("=====================================================\n");
        printf("           SISTEMA DE CONTROLE DE TRAFEGO            \n");
        printf("=====================================================\n");
        printf("  COMANDO                      DESCRICAO             \n");
        printf("  -------------------------------------------------  \n");
        printf("  chega <NS|LO> <placa>      - Insere carro na fila  \n");
        printf("  abrir <NS|LO> <qtd>        - Libera N carros       \n");
        printf("  status                     - Exibe as filas atuais \n");
        printf("  parar                      - Encerra o sistema     \n");
        printf("=====================================================\n");
        printf("Digite o comando: ");
        if (fgets(line, sizeof(line), stdin) == NULL) break;

        end = handleCommand(&NS, &LO, line);
        if (!end) {
            pauseAndClear();
        }
    } while (!end);
}
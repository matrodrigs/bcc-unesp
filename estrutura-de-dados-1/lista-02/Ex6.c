#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX 100

typedef struct Node {
    struct Node *next;
    char name[MAX];
    int timeToExecution;
} Node;

typedef struct {
    Node *front;
    Node *back;
    int queueSize;
} Queue;

typedef struct {
    Queue lowPriority;
    Queue mediumPriority;
    Queue highPriority;
} Scheduler;

void push(Queue *queue, char name[], int timeToExecution) {
    Node *newNode = malloc(sizeof(Node));

    if (newNode == NULL) {
        puts("\n[!] Erro na alocacao de memoria.");
        return;
    }

    strcpy(newNode->name, name);
    newNode->timeToExecution = timeToExecution;

    if (queue->queueSize == 0) {
        queue->front = newNode;
    } else {
        queue->back->next = newNode;
    }

    queue->back = newNode;
    (queue->queueSize)++;
}

void pop(Queue *queue) {
    if (queue->queueSize == 0) {
        puts("\n[!] Sem tarefas para remover.");
        return;
    }

    if (queue->queueSize == 1) {
        queue->back = NULL;
    }

    Node *removedNode = queue->front;

    queue->front = queue->front->next;
    (queue->queueSize)--;
    free(removedNode);
}

Queue startQueue() {
    Queue newQueue;

    newQueue.front = NULL;
    newQueue.back = NULL;
    newQueue.queueSize = 0;

    return newQueue;
}

Scheduler startScheduler() {
    Scheduler newScheduler;

    newScheduler.lowPriority = startQueue();
    newScheduler.mediumPriority = startQueue();
    newScheduler.highPriority = startQueue();

    return newScheduler;
}

bool isSchedulerEmpty(Scheduler *scheduler) {
    if (scheduler->lowPriority.queueSize == 0 && scheduler->mediumPriority.queueSize == 0 && scheduler->highPriority.queueSize == 0) {
        return true;
    }

    return false;
}

void add(Scheduler *scheduler, char name[], int timeToExecution, int priority) {
    Queue *queue;

    if (priority == 1) {
        queue = &(scheduler->lowPriority);
    } else if (priority == 2) {
        queue = &(scheduler->mediumPriority);
    } else {
        queue = &(scheduler->highPriority);
    }

    push(queue, name, timeToExecution);
    printf("\nO processo '%s' foi adicionado com sucesso.", name);
}

void processTasks(Scheduler *scheduler) {
    if (isSchedulerEmpty(scheduler)) {
        puts("\n[!] Sem tarefas para processar.");
        return;
    }

    Queue *queue;

    if (scheduler->highPriority.queueSize > 0) {
        queue = &(scheduler->highPriority);
    } else if (scheduler->mediumPriority.queueSize > 0) {
        queue = &(scheduler->mediumPriority);
    } else {
        queue = &(scheduler->lowPriority);
    }

    queue->front->timeToExecution -= 2;

    if (queue->front->timeToExecution <= 0) {
        printf("\nO processo '%s' foi finalizado!\n", queue->front->name);
    } else {
        printf("\nExecutando '%s' por 2s (restam %ds)\n", queue->front->name, queue->front->timeToExecution);
        push(queue, queue->front->name, queue->front->timeToExecution);
    }

    pop(queue);
}

void printQueue(Queue *queue) {
    if (queue->queueSize == 0) {
        return;
    }

    Queue auxQueue = startQueue();

    printf("---------------------------------------\n");
    printf("TAREFA               TEMPO RESTANTE\n");
    printf("---------------------------------------\n");

    while (queue->queueSize > 0) {
        Node *currentNode = queue->front;

        printf("%-20s %d s\n", currentNode->name, currentNode->timeToExecution);

        push(&auxQueue, currentNode->name, currentNode->timeToExecution);
        pop(queue);
    }

    printf("---------------------------------------\n");

    while (auxQueue.queueSize > 0) {
        Node *currentNode = auxQueue.front;

        push(queue, currentNode->name, currentNode->timeToExecution);
        pop(&auxQueue);
    }
}

void status(Scheduler *scheduler) {
    if (isSchedulerEmpty(scheduler)) {
        puts("\n[!] Sem tarefas para mostrar.");
        return;
    }

    printf("\n=======================================\n");
    printf("         STATUS DO ESCALONADOR         \n");
    printf("=======================================\n\n");

    printf(" [!] FILA DE ALTA PRIORIDADE (Nivel 3)\n");
    printQueue(&(scheduler->highPriority));
    printf("\n");

    printf(" [-] FILA DE MEDIA PRIORIDADE (Nivel 2)\n");
    printQueue(&(scheduler->mediumPriority));
    printf("\n");

    printf(" [v] FILA DE BAIXA PRIORIDADE (Nivel 1)\n");
    printQueue(&(scheduler->lowPriority));
    
    printf("\n=======================================\n");
}

bool handleCommand(Scheduler *scheduler, char line[]) {
    line[strcspn(line, "\n")] = 0;
    char *command = strtok(line, " ");

    if (command == NULL) {
        puts("\n[!] Sintaxe de comando invalida.");
        return 0;
    }

    if (strcasecmp(command, "add") == 0) {
        char *name = strtok(NULL, " ");
        char *timeToExecutionString = strtok(NULL, " ");
        char *priorityString = strtok(NULL, " ");

        if (name == NULL || timeToExecutionString == NULL || priorityString == NULL) {
            puts("\n[!] Parametros insuficientes. Uso: add <nome> <tempo> <prio>");
            return 0;
        }

        char *ptr, *ptr2;
        long timeToExecution = strtol(timeToExecutionString, &ptr, 10);
        long priority = strtol(priorityString, &ptr2, 10);

        if (timeToExecutionString == ptr || priorityString == ptr2 || timeToExecution <= 0 || priority <= 0) {
            puts("\n[!] Entrada invalida. Certifique-se de que o tempo e a prioridade sejam inteiros positivos.");
            return 0;
        }

        if (priority > 3) {
            puts("\n[!] Prioridade invalida. Escolha 3 (Alta), 2 (Media) ou 1 (Baixa).");
            return 0;
        }

        add(scheduler, name, timeToExecution, priority);
        return 0;
    }

    if (strcasecmp(command, "processar") == 0) {
        processTasks(scheduler);
        return 0;
    }
    
    if (strcasecmp(command, "status") == 0) {
        status(scheduler);
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
    Scheduler scheduler = startScheduler();
    bool end = false;
    char line[MAX];
    
    do {
        printf("=====================================================\n");
        printf("         SISTEMA DE PROCESSAMENTO DE TAREFAS         \n");
        printf("=====================================================\n");
        printf("  COMANDO                      DESCRICAO             \n");
        printf("  -------------------------------------------------  \n");
        printf("  add <nome> <tempo> <prio>  - Adiciona uma tarefa   \n");
        printf("  processar                  - Executa por 2 seg     \n");
        printf("  status                     - Exibe as filas        \n");
        printf("  parar                      - Encerra o sistema     \n");
        printf("=====================================================\n");
        printf("  * Prioridades: 3 (Alta), 2 (Media), 1 (Baixa)      \n");
        printf("=====================================================\n");
        printf("Digite o comando: ");
        if (fgets(line, sizeof(line), stdin) == NULL) break;

        end = handleCommand(&scheduler, line);
        if (!end) {
            pauseAndClear();
        }
    } while(!end);
}
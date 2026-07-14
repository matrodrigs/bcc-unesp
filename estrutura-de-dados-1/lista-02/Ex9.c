#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <unistd.h> 

#define MAX 100

typedef struct Node {
    struct Node *next;
    char name[MAX];
    int timeToExecution;
} Node;

typedef struct {
    Node *front;
    Node *back;
    int dequeSize;
} Deque;

typedef struct {
    Deque queue;
    Deque history;
} Simulator;

Deque startDeque() {
    Deque newDeque;

    newDeque.front = NULL;
    newDeque.back = NULL;
    newDeque.dequeSize = 0;

    return newDeque;
}

Simulator startSimulator() {
    Simulator newSimulator;

    newSimulator.queue = startDeque();
    newSimulator.history = startDeque();

    return newSimulator;
}

void pushBack(Deque *deque, char name[], int timeToExecution) {
    Node *newNode = malloc(sizeof(Node));

    if (newNode == NULL) {
        puts("\n[!] Erro na alocacao de memoria.");
        return;
    }

    strcpy(newNode->name, name);
    newNode->timeToExecution = timeToExecution;
    newNode->next = NULL;

    if (deque->dequeSize == 0) {
        deque->front = newNode;
    } else {
        deque->back->next = newNode;
    }

    deque->back = newNode;
    (deque->dequeSize)++;
}

void pushFront(Deque *deque, char name[], int timeToExecution) {
    Node *newNode = malloc(sizeof(Node));

    if (newNode == NULL) {
        puts("\n[!] Erro na alocacao de memoria.");
        return;
    }

    strcpy(newNode->name, name);
    newNode->timeToExecution = timeToExecution;
    newNode->next = deque->front;

    deque->front = newNode;
    
    if (deque->dequeSize == 0) {
        deque->back = newNode;
    }
    
    (deque->dequeSize)++;
}

bool popFront(Deque *deque, char nameOut[], int *timeOut) {
    if (deque->dequeSize == 0) {
        return false;
    }

    Node *removedNode = deque->front;
    
    strcpy(nameOut, removedNode->name);
    *timeOut = removedNode->timeToExecution;

    deque->front = deque->front->next;
    
    if (deque->front == NULL) {
        deque->back = NULL;
    }
    
    (deque->dequeSize)--;
    free(removedNode);
    
    return true;
}

void runTask(Simulator *simulator) {
    if (simulator->queue.dequeSize == 0) {
        puts("\n[!] Nenhuma tarefa na fila para executar.");
        return;
    }

    char name[MAX];
    int time;
    
    popFront(&(simulator->queue), name, &time);

    printf("\nExecutando: %s\n", name);
    sleep(time);
    puts("(concluida)\n");

    pushFront(&(simulator->history), name, time);
}

void undoTask(Simulator *simulator) {
    if (simulator->history.dequeSize == 0) {
        puts("\n[!] Nenhum historico de tarefas para desfazer.");
        return;
    }

    char name[MAX];
    int time;
    
    popFront(&(simulator->history), name, &time);
    pushFront(&(simulator->queue), name, time);
    
    printf("\nTarefa \"%s\" retornou para a fila\n", name);
}

void printDeque(Deque *deque) {
    if (deque->dequeSize == 0) {
        puts("  [ Vazio ]");
        return;
    }

    Deque auxDeque = startDeque();

    printf("  ---------------------------------------\n");
    printf("  TAREFA               TEMPO (s)         \n");
    printf("  ---------------------------------------\n");

    while (deque->dequeSize > 0) {
        char name[MAX];
        int time;
        
        popFront(deque, name, &time);
        printf("  %-20s %d\n", name, time);
        pushBack(&auxDeque, name, time);
    }

    printf("  ---------------------------------------\n");

    while (auxDeque.dequeSize > 0) {
        char name[MAX];
        int time;
        
        popFront(&auxDeque, name, &time);
        pushBack(deque, name, time);
    }
}

void status(Simulator *simulator) {
    printf("\n=======================================\n");
    printf("         STATUS DO PROCESSADOR         \n");
    printf("=======================================\n\n");

    printf(" [>] TAREFAS AGUARDANDO (Fila)\n");
    printDeque(&(simulator->queue));
    printf("\n");

    printf(" [v] HISTORICO DE EXECUCAO (Pilha)\n");
    printDeque(&(simulator->history));
    
    printf("\n=======================================\n");
}

bool handleCommand(Simulator *simulator, char line[]) {
    line[strcspn(line, "\n")] = 0;
    char *command = strtok(line, " ");

    if (command == NULL) {
        puts("\n[!] Sintaxe de comando invalida.");
        return 0;
    }

    if (strcasecmp(command, "add") == 0) {
        char *name = strtok(NULL, " ");
        char *timeString = strtok(NULL, " ");

        if (name == NULL || timeString == NULL) {
            puts("\n[!] Parametros insuficientes. Uso: add <nome> <tempo>");
            return 0;
        }

        char *ptr;
        long timeToExecution = strtol(timeString, &ptr, 10);

        if (timeString == ptr || timeToExecution <= 0) {
            puts("\n[!] Entrada invalida. Certifique-se de que o tempo seja um inteiro positivo.");
            return 0;
        }

        pushBack(&(simulator->queue), name, timeToExecution);
        return 0;
    }

    if (strcasecmp(command, "run") == 0) {
        runTask(simulator);
        return 0;
    }

    if (strcasecmp(command, "undo") == 0) {
        undoTask(simulator);
        return 0;
    }
    
    if (strcasecmp(command, "status") == 0) {
        status(simulator);
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
    Simulator simulator = startSimulator();
    bool end = false;
    char line[MAX];
    
    do {
        printf("=====================================================\n");
        printf("         SIMULADOR DE TAREFAS COM HISTORICO          \n");
        printf("=====================================================\n");
        printf("  COMANDO                      DESCRICAO             \n");
        printf("  -------------------------------------------------  \n");
        printf("  add <nome> <tempo>         - Adiciona uma tarefa   \n");
        printf("  run                        - Executa proxima tarefa\n");
        printf("  undo                       - Desfaz a ultima tarefa\n");
        printf("  status                     - Exibe fila e historico\n");
        printf("  parar                      - Encerra o sistema     \n");
        printf("=====================================================\n");
        printf("Digite o comando: ");
        
        if (fgets(line, sizeof(line), stdin) == NULL) break;

        end = handleCommand(&simulator, line);
        if (!end) {
            pauseAndClear();
        }
    } while(!end);

    return 0;
}
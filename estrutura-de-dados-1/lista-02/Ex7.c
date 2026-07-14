#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX 100

typedef enum {
    NETWORK = 1,
    SOFTWARE = 2,
    HARDWARE = 3
} TicketType;

typedef struct {
    char sector[MAX];
    int extension;
} NetworkTicket;

typedef struct {
    char user[MAX];
    char program[MAX];
} SoftwareTicket;

typedef struct {
    char equipmentId[MAX];
    char defect[MAX];
} HardwareTicket;

typedef union {
    NetworkTicket network;
    SoftwareTicket software;
    HardwareTicket hardware;
} TicketData;

typedef struct Node {
    TicketType type;
    TicketData data;
    struct Node *next;
} Node;

typedef struct {
    Node *front;
    Node *back;
    int queueSize;
} Queue;

Queue startQueue() {
    Queue newQueue;

    newQueue.front = NULL;
    newQueue.back = NULL;
    newQueue.queueSize = 0;

    return newQueue;
}

void push(Queue *queue, TicketType type, TicketData data) {
    Node *newNode = malloc(sizeof(Node));

    if (newNode == NULL) {
        puts("\n[!] Erro na alocacao de memoria.");
        return;
    }

    newNode->type = type;
    newNode->data = data;
    newNode->next = NULL;

    if (queue->queueSize == 0) {
        queue->front = newNode;
    } else {
        queue->back->next = newNode;
    }

    queue->back = newNode;
    (queue->queueSize)++;
}

void attendTicket(Queue *queue) {
    if (queue->queueSize == 0) {
        puts("\n[!] A fila esta vazia. Nenhum chamado para atender.");
        return;
    }

    Node *removedNode = queue->front;

    if (removedNode->type == NETWORK) {
        printf("\nAtendendo chamado de REDE: setor=%s, ramal=%d\n", removedNode->data.network.sector, removedNode->data.network.extension);
    } 
    else if (removedNode->type == SOFTWARE) {
        printf("\nAtendendo chamado de SOFTWARE: usuario=%s, programa=%s\n", removedNode->data.software.user, removedNode->data.software.program);
    } 
    else if (removedNode->type == HARDWARE) {
        printf("\nAtendendo chamado de HARDWARE: patrimonio=%s, defeito=%s\n", removedNode->data.hardware.equipmentId, removedNode->data.hardware.defect);
    }

    queue->front = queue->front->next;
    
    if (queue->front == NULL) {
        queue->back = NULL;
    }
    
    (queue->queueSize)--;
    free(removedNode);
}

void status(Queue *queue) {
    if (queue->queueSize == 0) {
        puts("\n[!] A fila esta vazia.");
        return;
    }

    printf("\nFila atual:\n\n");

    Node *currentNode = queue->front;
    int index = 1;

    while (currentNode != NULL) {
        if (currentNode->type == NETWORK) {
            printf("%d. REDE: setor=%s, ramal=%d\n", index, currentNode->data.network.sector, currentNode->data.network.extension);
        } 
        else if (currentNode->type == SOFTWARE) {
            printf("%d. SOFTWARE: usuario=%s, programa=%s\n", index, currentNode->data.software.user, currentNode->data.software.program);
        } 
        else if (currentNode->type == HARDWARE) {
            printf("%d. HARDWARE: patrimonio=%s, defeito=%s\n", index, currentNode->data.hardware.equipmentId, currentNode->data.hardware.defect);
        }
        
        currentNode = currentNode->next;
        index++;
    }
}

void clearQueue(Queue *queue) {
    while (queue->queueSize > 0) {
        Node *removedNode = queue->front;
        queue->front = queue->front->next;
        free(removedNode);
        (queue->queueSize)--;
    }
}

bool handleCommand(Queue *queue, char line[]) {
    line[strcspn(line, "\n")] = 0;
    char *command = strtok(line, " ");

    if (command == NULL) {
        puts("\n[!] Sintaxe de comando invalida.");
        return false;
    }

    if (strcasecmp(command, "add") == 0) {
        char *ticketTypeStr = strtok(NULL, " ");
        
        if (ticketTypeStr == NULL) {
            puts("\n[!] Especifique o tipo do chamado (rede, software ou hardware).");
            return false;
        }

        TicketData newData;

        if (strcasecmp(ticketTypeStr, "rede") == 0) {
            char *sector = strtok(NULL, " ");
            char *extensionStr = strtok(NULL, " ");

            if (!sector || !extensionStr) {
                puts("\n[!] Uso: add rede <setor> <ramal>");
                return false;
            }

            char *ptr;
            long extension = strtol(extensionStr, &ptr, 10);

            if (extensionStr == ptr || extension <= 0) {
                puts("\n[!] Ramal invalido. Digite um numero positivo.");
                return false;
            }

            strcpy(newData.network.sector, sector);
            newData.network.extension = (int)extension;

            push(queue, NETWORK, newData);
            puts("\nChamado de REDE adicionado com sucesso.");

        } else if (strcasecmp(ticketTypeStr, "software") == 0) {
            char *user = strtok(NULL, " ");
            char *program = strtok(NULL, " ");

            if (!user || !program) {
                puts("\n[!] Uso: add software <usuario> <programa>");
                return false;
            }

            strcpy(newData.software.user, user);
            strcpy(newData.software.program, program);

            push(queue, SOFTWARE, newData);
            puts("\nChamado de SOFTWARE adicionado com sucesso.");

        } else if (strcasecmp(ticketTypeStr, "hardware") == 0) {
            char *equipmentId = strtok(NULL, " ");
            char *defect = strtok(NULL, " ");

            if (!equipmentId || !defect) {
                puts("\n[!] Uso: add hardware <patrimonio> <defeito>");
                return false;
            }

            strcpy(newData.hardware.equipmentId, equipmentId);
            strcpy(newData.hardware.defect, defect);

            push(queue, HARDWARE, newData);
            puts("\nChamado de HARDWARE adicionado com sucesso.");

        } else {
            printf("\n[!] Tipo de chamado '%s' desconhecido.\n", ticketTypeStr);
        }
        
        return false;
    }

    if (strcasecmp(command, "atender") == 0) {
        attendTicket(queue);
        return false;
    }
    
    if (strcasecmp(command, "status") == 0) {
        status(queue);
        return false;
    }

    if (strcasecmp(command, "parar") == 0) {
        clearQueue(queue);
        return true;
    }

    printf("\n[!] O comando '%s' nao existe.\n", command);
    return false;
}

void pauseAndClear() {
    printf("\nPressione ENTER para continuar...");
    fflush(stdin);
    getchar();
    system("cls");
}

int main() {
    Queue callQueue = startQueue();
    bool end = false;
    char line[MAX];
    
    do {
        printf("=======================================================\n");
        printf("        CENTRAL DE ATENDIMENTO - FILA DE CHAMADOS      \n");
        printf("=======================================================\n");
        printf("  COMANDO                             DESCRICAO        \n");
        printf("  ---------------------------------------------------  \n");
        printf("  add rede <setor> <ramal>          - Chamado de rede  \n");
        printf("  add software <usuario> <programa> - Chamado de sw    \n");
        printf("  add hardware <patrimonio> <def.>  - Chamado de hw    \n");
        printf("  atender                           - Atende proximo   \n");
        printf("  status                            - Lista chamados   \n");
        printf("  parar                             - Encerra sistema  \n");
        printf("=======================================================\n");
        printf("Digite o comando: ");
        
        if (fgets(line, sizeof(line), stdin) == NULL) break;

        end = handleCommand(&callQueue, line);
        if (!end) {
            pauseAndClear();
        }
    } while(!end);

    return 0;
}
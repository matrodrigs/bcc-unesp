#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX 100

typedef enum {
    BACKUP = 1,
    EMAIL = 2,
    REPORT = 3
} TaskType;

typedef struct {
    char folder[MAX];
    int size;
} BackupTask;

typedef struct {
    char recipient[MAX];
    char subject[MAX];
} EmailTask;

typedef struct {
    char name[MAX];
    int pages;
} ReportTask;

typedef struct Node {
    TaskType type;
    void *data;
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

void push(Queue *queue, TaskType type, void *data) {
    Node *newNode = malloc(sizeof(Node));

    if (newNode == NULL) {
        puts("\n[!] Erro na alocacao de memoria do no.");
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

void executeTask(Queue *queue) {
    if (queue->queueSize == 0) {
        puts("\n[!] A fila esta vazia. Nenhuma tarefa para executar.");
        return;
    }

    Node *removedNode = queue->front;

    if (removedNode->type == BACKUP) {
        BackupTask *task = (BackupTask *)removedNode->data;
        printf("\nExecutando BACKUP: pasta=%s, tamanho=%d GB\n", task->folder, task->size);
    } 
    else if (removedNode->type == EMAIL) {
        EmailTask *task = (EmailTask *)removedNode->data;
        printf("\nExecutando EMAIL: destinatario=%s, assunto=%s\n", task->recipient, task->subject);
    } 
    else if (removedNode->type == REPORT) {
        ReportTask *task = (ReportTask *)removedNode->data;
        printf("\nExecutando RELATORIO: nome=%s, paginas=%d\n", task->name, task->pages);
    }

    queue->front = queue->front->next;
    if (queue->front == NULL) {
        queue->back = NULL;
    }
    (queue->queueSize)--;

    free(removedNode->data);
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
        if (currentNode->type == BACKUP) {
            BackupTask *task = (BackupTask *)currentNode->data;
            printf("%d. BACKUP: pasta=%s, tamanho=%d GB\n", index, task->folder, task->size);
        } 
        else if (currentNode->type == EMAIL) {
            EmailTask *task = (EmailTask *)currentNode->data;
            printf("%d. EMAIL: destinatario=%s, assunto=%s\n", index, task->recipient, task->subject);
        } 
        else if (currentNode->type == REPORT) {
            ReportTask *task = (ReportTask *)currentNode->data;
            printf("%d. RELATORIO: nome=%s, paginas=%d\n", index, task->name, task->pages);
        }
        
        currentNode = currentNode->next;
        index++;
    }
}

void clearQueue(Queue *queue) {
    while (queue->queueSize > 0) {
        Node *removedNode = queue->front;
        queue->front = queue->front->next;
        
        free(removedNode->data);
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
        char *taskTypeStr = strtok(NULL, " ");
        
        if (taskTypeStr == NULL) {
            puts("\n[!] Especifique o tipo da tarefa (backup, email ou relatorio).");
            return false;
        }

        if (strcasecmp(taskTypeStr, "backup") == 0) {
            char *folder = strtok(NULL, " ");
            char *sizeStr = strtok(NULL, " ");

            if (!folder || !sizeStr) {
                puts("\n[!] Uso: add backup <pasta> <tamanho>");
                return false;
            }

            char *ptr;
            long size = strtol(sizeStr, &ptr, 10);

            if (sizeStr == ptr || size <= 0) {
                puts("\n[!] Tamanho invalido. Digite um numero positivo.");
                return false;
            }

            BackupTask *newTask = malloc(sizeof(BackupTask));
            strcpy(newTask->folder, folder);
            newTask->size = (int)size;

            push(queue, BACKUP, newTask);
            puts("\nTarefa de BACKUP adicionada com sucesso.");

        } else if (strcasecmp(taskTypeStr, "email") == 0) {
            char *recipient = strtok(NULL, " ");
            char *subject = strtok(NULL, " ");

            if (!recipient || !subject) {
                puts("\n[!] Uso: add email <destinatario> <assunto>");
                return false;
            }

            EmailTask *newTask = malloc(sizeof(EmailTask));
            strcpy(newTask->recipient, recipient);
            strcpy(newTask->subject, subject);

            push(queue, EMAIL, newTask);
            puts("\nTarefa de EMAIL adicionada com sucesso.");

        } else if (strcasecmp(taskTypeStr, "relatorio") == 0) {
            char *name = strtok(NULL, " ");
            char *pagesStr = strtok(NULL, " ");

            if (!name || !pagesStr) {
                puts("\n[!] Uso: add relatorio <nome> <paginas>");
                return false;
            }

            char *ptr;
            long pages = strtol(pagesStr, &ptr, 10);

            if (pagesStr == ptr || pages <= 0) {
                puts("\n[!] Quantidade de paginas invalida.");
                return false;
            }

            ReportTask *newTask = malloc(sizeof(ReportTask));
            strcpy(newTask->name, name);
            newTask->pages = (int)pages;

            push(queue, REPORT, newTask);
            puts("\nTarefa de RELATORIO adicionada com sucesso.");

        } else {
            printf("\n[!] Tipo de tarefa '%s' desconhecido.\n", taskTypeStr);
        }
        
        return false;
    }

    if (strcasecmp(command, "executar") == 0) {
        executeTask(queue);
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
    Queue taskQueue = startQueue();
    bool end = false;
    char line[MAX];
    
    do {
        printf("========================================================\n");
        printf("             AGENDADOR DE TAREFAS MULTIPLAS             \n");
        printf("========================================================\n");
        printf("  COMANDO                              DESCRICAO        \n");
        printf("  ----------------------------------------------------  \n");
        printf("  add backup <pasta> <tamanho>       - Envia backup     \n");
        printf("  add email <destinatario> <assunto> - Envia email      \n");
        printf("  add relatorio <nome> <paginas>     - Envia relatorio  \n");
        printf("  executar                           - Executa o topo   \n");
        printf("  status                             - Lista a fila     \n");
        printf("  parar                              - Encerra o sistema\n");
        printf("========================================================\n");
        printf("Digite o comando: ");
        
        if (fgets(line, sizeof(line), stdin) == NULL) break;

        end = handleCommand(&taskQueue, line);
        if (!end) {
            pauseAndClear();
        }
    } while(!end);

    return 0;
}
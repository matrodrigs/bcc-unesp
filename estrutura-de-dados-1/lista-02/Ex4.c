#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <windows.h>

#define maxSize 100

typedef struct Node {
    struct Node *next;
    char fileName[maxSize];
    int totalPages;
} Node;

typedef struct {
    Node *head, *tail;
    int queueSize;
} Queue;

void push(Queue *queue, char fileName[], int totalPages) {
    Node *newNode = malloc(sizeof(Node));

    if (newNode == NULL) {
        puts("\n[!] Memoria insuficiente ao alocar novo no.");
        return; 
    }

    strcpy(newNode->fileName, fileName);
    newNode->totalPages = totalPages;
    newNode->next = NULL;

    if (queue->queueSize == 0) {
        queue->head = newNode;
    } else {
        queue->tail->next = newNode;
    }

    queue->tail = newNode;
    (queue->queueSize)++;
}

void pop(Queue *queue) {
    if (queue->queueSize == 0) {
        return;
    }

    if (queue->queueSize == 1) {
        queue->tail = NULL;
    }

    Node *removedNode = queue->head;

    queue->head = queue->head->next;
    (queue->queueSize)--;
    free(removedNode);
}

bool processQueue(Queue *queue, bool removeElement, char fileName[]) {
    if (queue->queueSize == 0) {
        puts("\n[!] Sem trabalhos para remover da fila");
        return 0;
    }

    Queue auxQueue = {NULL, NULL, 0};
    Node *auxNode = queue->head;
    bool found = false;
    int posicao = 1;

    while (auxNode != NULL) {
        if (!removeElement) {
            printf("[%d] Arquivo: %-20s | Paginas: %d\n", posicao, auxNode->fileName, auxNode->totalPages);
            push(&auxQueue, auxNode->fileName, auxNode->totalPages);
            posicao++;
        } 
        else if (strcasecmp(auxNode->fileName, fileName) == 0) {
            found = true;
        } 
        else {
            push(&auxQueue, auxNode->fileName, auxNode->totalPages);
        }

        pop(queue);
        auxNode = queue->head;
    }

    auxNode = auxQueue.head;

    while (auxNode != NULL) {
        push(queue, auxQueue.head->fileName,  auxQueue.head->totalPages);
        pop(&auxQueue);
        auxNode = auxQueue.head;
    }

    return found;
}

void cancel(Queue *queue, char fileName[]) {
    if (queue->queueSize == 0) {
        puts("\n[!] Sem trabalhos para remover da fila");
        return;
    }

    bool found = processQueue(queue, true, fileName);

    if (!found) {
        printf("\n[!] O trabalho com o nome de '%s' nao foi encontrado na lista.\n", fileName);
    } else {
        printf("\nO trabalho '%s' foi removido da fila com sucesso!\n", fileName);
    }
}

void print(Queue *queue) {
    if (queue->queueSize == 0) {
        puts("\n[!] Sem trabalhos para imprimir");
        return;
    }

    int total = queue->head->totalPages, currentPage = 1, barWidth = 20;

    printf("\n[PROCESSO] Iniciando a impressao: '%s'\n", queue->head->fileName);
    while (queue->head->totalPages) {
        float progresso = (float)currentPage / total;
        int preenchido = progresso * barWidth;

        printf("\r[");
        for (int i = 0; i < barWidth; i++) {
            if (i < preenchido) printf("="); 
            else printf(" ");                
        }
        
        printf("] %3.0f%% (%d/%d)", progresso * 100, currentPage, total);
        fflush(stdout);
        
        currentPage++;
        (queue->head->totalPages)--;
        Sleep(300);
    }
    printf("\n[SUCESSO] Trabalho '%s' finalizado.\n", queue->head->fileName);
    
    pop(queue);
}

bool handleCommand(Queue *queue, char line[]) {
    line[strcspn(line, "\n")] = 0;
    char *command = strtok(line, " ");

    if (command == NULL) {
        puts("\n[!] Sintaxe de comando invalida.");
        return 0;
    }

    if (strcasecmp(command, "add") == 0) {
        char *fileName = strtok(NULL, " ");
        char *totalPagesString = strtok(NULL, " ");
        if (fileName == NULL || totalPagesString == NULL) {
            puts("\n[!] Parametros insuficientes. Uso: add <nome_arquivo> <paginas>");
            return 0;
        }

        char *ptr;
        long totalPages = strtol(totalPagesString, &ptr, 10);
        if (ptr == totalPagesString || totalPages <= 0) {
            puts("\n[!] Entrada invalida. Certifique-se de que o numero de paginas seja um inteiro positivo.");
            return 0;
        }

        push(queue, fileName, totalPages);
        printf("\nTrabalho '%s' (%d pags) adicionado com sucesso.\n", fileName, totalPages);
        return 0;
    }

    if (strcasecmp(command, "cancel") == 0) {
        char *fileName = strtok(NULL, " ");
        if (fileName == NULL) {
            puts("\n[!] Parametros insuficientes. Uso: cancel <nome_arquivo>");
            return 0;
        }

        cancel(queue, fileName);
        return 0;
    }

    if (strcasecmp(command, "print") == 0) {
        print(queue);
        return 0;
    }

    if (strcasecmp(command, "status") == 0) {
        processQueue(queue, false, "");
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
    Queue queue = {NULL, NULL, 0};
    char line[maxSize];
    bool end = false;

    do {
        printf("\n+=================================================+\n");
        printf("|              SISTEMA DE IMPRESSAO               |\n");
        printf("+=================================================+\n");
        printf("| Trabalhos na fila: %-29d|\n", queue.queueSize);
        printf("|-------------------------------------------------|\n");
        printf("| COMANDOS DISPONIVEIS:                           |\n");
        printf("|                                                 |\n");
        printf("|  add <nome> <pags>  -> Inserir na fila          |\n");
        printf("|  cancel <nome>      -> Remover da fila          |\n");
        printf("|  print              -> Imprimir proximo         |\n");
        printf("|  status             -> Ver fila atual           |\n");
        printf("|  parar              -> Encerrar sistema         |\n");
        printf("+=================================================+\n");
        printf("Digite o comando: ");
        if (fgets(line, sizeof(line), stdin) == NULL) break;

        end = handleCommand(&queue, line);
        if (!end) {
            pauseAndClear();
        }
    } while (!end);

    puts("Encerrando...");
}
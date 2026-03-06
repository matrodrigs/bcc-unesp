#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define maxMusicLen 101

typedef struct Node {
    struct Node *next;
    struct Node *previous;
    char musicName[maxMusicLen];
} Node;

typedef struct {
    struct Node *head;
    struct Node *tail;
    int nodeAmount;
} List;

void printError(const char *message) {
    printf("\n[!] %s\n", message);
}

void printMusicList(List *list) {
    Node *currentNode = list->head;

    puts("\nLista atual:");
    for (int i = 0; i < list->nodeAmount; i++) {
        printf("%d. %s\n", i + 1, currentNode->musicName);
        currentNode = currentNode->next;
    }
}

void addMusic(List *list, char *musicName) {
    Node *newNode = (Node*)malloc(sizeof(Node));

    if (newNode == NULL) {
        printError("Nao foi possivel adicionar uma musica");
        return;
    }

    strcpy(newNode->musicName, musicName);

    if (list->head == NULL) {
        newNode->next = newNode;
        newNode->previous = newNode;
        list->head = newNode;
        list->tail = newNode;
    } else {
        newNode->next = list->head;
        newNode->previous = list->tail;
        list->tail->next = newNode;
        list->head->previous = newNode;
        list->tail = newNode;
    }

    puts("Musica adicionada com sucesso!");

    list->nodeAmount++;
}

void removeCurrentMusic(List *list) {
    if (list->nodeAmount == 0) {
        printError("Sem musicas para remover");
        return;
    }

    Node *removedNode = list->head;

    if (list->nodeAmount == 1) {
        list->head = NULL;
        list->tail = NULL;
    } else {
        list->head = removedNode->next;
        list->tail->next = list->head;
        list->head->previous = list->tail;
    }

    printf("\nA musica %s foi removida!\n", removedNode->musicName);

    free(removedNode);
    list->nodeAmount--;
}

void checkCurrentMusic(List *list) {
    char currentMusicName[maxMusicLen];

    strcpy(currentMusicName, list->head->musicName);
    printf("\nA musica atual eh: %s\n", currentMusicName);
}

void nextMusic(List *list) {
    if (list->nodeAmount == 0) {
        printError("Sem musicas na lista");
        return;
    } else if (list->nodeAmount == 1) {
        printError("A lista nao tem uma proxima musica");
        return;
    }

    printf("\nPulando %s...", list->head->musicName);
    removeCurrentMusic(list);
    printf("Agora tocando: %s\n", list->head->musicName);
}

void sortMusics(List *list) {
    if (list->nodeAmount <= 2) {
        printError("Musicas insuficientes para ordenar");
        return;
    }

    Node *atual = list->head, *index;

    do {
        index = atual->next;

        while (index != list->head) {
            if (strcmp(atual->musicName, index->musicName) > 0) {
                char aux[maxMusicLen];

                strcpy(aux, atual->musicName);
                strcpy(atual->musicName, index->musicName);
                strcpy(index->musicName, aux);
            }

            index = index->next;
        }

        atual = atual->next;
    } while (atual->next != list->head);

    printMusicList(list);
}

int main() {
    List list = {NULL, NULL, 0};
    int option = 0;

    do {
        printf("\n1. Adicionar musica [%d %s na fila]\n", list.nodeAmount, list.nodeAmount == 1 ? "musica" : "musicas");
        puts("2. Verificar musica atual");
        puts("3. Proxima musica");
        puts("4. Remover musica atual");
        puts("5. Ordenar musicas em ordem alfabetica");
        puts("0. Sair");
        printf("Escolha: ");
        scanf("%d", &option);
        getchar();

        switch (option) {
            case 1:
                char musicName[maxMusicLen];

                printf("\nDigite o nome da musica (100 caracteres): ");
                fgets(musicName, sizeof(musicName), stdin);
                musicName[strcspn(musicName, "\n")] = '\0';

                addMusic(&list, musicName);
                break;
            case 2:
                checkCurrentMusic(&list);
                break;
            case 3:
                nextMusic(&list);
                break;
            case 4:
                removeCurrentMusic(&list);
                break;
            case 5:
                sortMusics(&list);
                break;
            default:
                break;
        }
    } while (option != 0);
}
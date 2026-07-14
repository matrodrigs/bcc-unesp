#include <stdio.h>
#include <stdlib.h>
#include <time.h>

typedef struct Node {
    struct Node *next;
    int value;
} Node;

void printList(Node **head) {
    if (*head == NULL) {
        puts("Lista vazia!");
        return;
    }

    Node *auxNode = *head;
    int i = 1;

    puts("\nValores na lista atual:");
    while (auxNode != NULL) {
        printf("%d. %d\n", i++, auxNode->value);
        auxNode = auxNode->next;
    }
}

void sortedInsert(Node **head, int value) { 
    Node *newNode = (Node*)malloc(sizeof(Node));

    if (newNode == NULL) {
        puts("Nao foi possivel inserir!");
        return;
    }

    newNode->value = value;
    newNode->next = NULL;

    if (*head == NULL || (*head)->value >= value) {
        newNode->next = *head;
        *head = newNode;
    }
    else {
        Node *auxNode = *head;

        while (auxNode->next != NULL && auxNode->next->value < value) {
            auxNode = auxNode->next;    
        }

        newNode->next = auxNode->next;
        auxNode->next = newNode;
    }
}

void removeSmallerValues(Node **head, int reference) {
    Node *currentNode = *head;

    printf("\nValores removidos: ");
    while (currentNode != NULL && currentNode->value < reference) {
        Node *removedNode = currentNode;
        currentNode = currentNode->next;

        printf("%d ", removedNode->value);
        free(removedNode);
    }
    putchar('\n');

    *head = currentNode;
}

int main() {
    Node *head = NULL;

    srand(time(NULL));

    for (int i = 0; i < 10; i++) {
        int randomValue = rand() % 1000 + 1;

        sortedInsert(&head, randomValue);
    }
    printList(&head);

    int referenceValue;

    printf("\nInforme o valor limite para exclusao (valores menores serao removidos): ");
    scanf("%d", &referenceValue);

    removeSmallerValues(&head, referenceValue);
    printList(&head);
}
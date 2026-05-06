#include <stdio.h>
#include <string.h>
#include <stdbool.h>

#define maxSize 100

void push(char stack[][maxSize], int *amount, char pathName[]) {
    if (*amount > maxSize) {
        return;
    }

    strcpy(stack[*amount], pathName);
    (*amount)++;
}

void pop(int *amount) {
    if (*amount == 0) {
        return;
    }

    (*amount)--;
}

char *peek(char stack[][maxSize], int size) {
    if (size == 0) {
        return '\0';
    }
    
    return stack[size - 1];
}

bool simplifyPath(char stack[][maxSize], int *stackSize, char unixPath[]) {
    char *token = strtok(unixPath, "/");
    int goBack = 0, directory = 0;

    while (token) {
        if (strcmp(token, "..") == 0) {
            pop(stackSize);
            goBack++;
        } else if (strcmp(token, ".") != 0) {
            push(stack, stackSize, token);
            directory++;
        }

        token = strtok(NULL, "/");
    }

    if (goBack > directory)
        return false;
    else
        return true;
}

void printDirectory(char stack[][maxSize], int directories) {
    printf("\nDiretorio simplificado: ");
    putchar('/');
    for (int i = 0; i < directories; i++) {
        printf("%s", stack[i]);
        if (i < directories - 2)
            putchar('/');
    }
}

void main() {
    char directory[maxSize][maxSize], unixPath[maxSize * 2];
    int directoriesAmount = 0;

    printf("Digite o diretorio UNIX: ");
    if (fgets(unixPath, sizeof(unixPath), stdin) == NULL) return;

    if (simplifyPath(directory, &directoriesAmount, unixPath)) {
        printDirectory(directory, directoriesAmount);
    } else {
        puts("Erro: A quantidade de '..' foi maior que a de diretorios!");
    }
}
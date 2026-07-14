#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int searchPassword(int passwords[100], int totalPasswords, int password) {
    for (int i = 0; i < totalPasswords; i++) {
        if (passwords[i] == password)
            return 1;
    }

    return 0;
}

void showPasswords(int passwords[100], int totalPasswords) {
    putchar('\n');
    for (int i = 0; i < totalPasswords; i++)
        printf("%d. %d\n", i + 1, passwords[i]);
    putchar('\n');
}

void sortedInsert(int passwords[100], int *totalPasswords) {
    if (*totalPasswords == 100) {
        puts("Limite maximo atingido!");
        return;
    }

    srand(time(NULL));
    int newPassword = rand() % 10000 + 1;

    while (searchPassword(passwords, *totalPasswords, newPassword)) {
        srand(time(NULL));
        newPassword = rand() % 10000 + 1;
    }

    printf("\nValor adicionado: %d\n", newPassword);

    int i = *totalPasswords - 1;

    while (i >= 0 && passwords[i] > newPassword) {
        passwords[i + 1] = passwords[i];
        i--;
    }

    passwords[i + 1] = newPassword;

    (*totalPasswords)++;
    showPasswords(passwords, *totalPasswords);
}

void removeSmallestPassword(int passwords[100], int *totalPasswords) {
    if (*totalPasswords == 0) {
        puts("Sem senhas para remover!");
        return;
    }

    int i = 0;

    printf("\nValor removido: %d\n", passwords[0]);
    while (i < (*totalPasswords - 1)) {
        passwords[i] = passwords[i + 1];
        i++;
    }

    (*totalPasswords)--;
    showPasswords(passwords, *totalPasswords);
}

int main() {
    int passwords[100];
    int totalPasswords = 0;
    int option = 0;

    do {
        puts("\n1. Adicionar senha");
        puts("2. Remover menor senha");
        puts("0. Sair");
        printf("Escolha: ");
        scanf("%d", &option);

        switch (option) {
            case 1:
                sortedInsert(passwords, &totalPasswords);
                break;
            case 2:
                removeSmallestPassword(passwords, &totalPasswords);
                break;
            default:
                break;
        }
    } while (option != 0);
}

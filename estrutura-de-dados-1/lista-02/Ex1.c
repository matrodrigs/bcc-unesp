#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define maxSize 100

void push(char stack[][maxSize], int *amount, char vehicle[]) {
    if (*amount > maxSize) {
        return;
    }

    strcpy(stack[*amount], vehicle);
    (*amount)++;
}

void pop(int *amount) {
    if (*amount == 0) {
        return;
    }

    (*amount)--;
}

void showStatus(char stack[][maxSize], int *amount) {
    if (*amount == 0) {
        puts("\n[!] Sem veiculos para mostrar.");
        return;
    }

    char tempStack[maxSize][maxSize];
    char currentVehicle[maxSize];
    int tempAmount = 0;

    puts("\n========================================");
    puts("         VEICULOS ESTACIONADOS          ");
    puts("========================================");
    while (*amount > 0) {
        strcpy(currentVehicle, stack[*amount - 1]);
        pop(amount); 

        printf("[%d] %s\n", tempAmount + 1, currentVehicle);

        push(tempStack, &tempAmount, currentVehicle);
    }
    puts("========================================\n");

    while (tempAmount > 0) {
        strcpy(currentVehicle, tempStack[tempAmount - 1]);
        push(stack, amount, currentVehicle);
        pop(&tempAmount);
    }
}

int vehicleExists(char stack[][maxSize], int *amount, char target[]) {
    char tempStack[maxSize][maxSize];
    char currentVehicle[maxSize];
    int tempAmount = 0, found = 0;

    while (*amount > 0) {
        strcpy(currentVehicle, stack[*amount - 1]);
        pop(amount); 

        if (strcasecmp(currentVehicle, target) == 0) {
            found = 1;
        }
            
        push(tempStack, &tempAmount, currentVehicle);
    }

    while (tempAmount > 0) {
        strcpy(currentVehicle, tempStack[tempAmount - 1]);
        push(stack, amount, currentVehicle);
        pop(&tempAmount);
    }

    return found;
}

void removeSpecificVehicle(char stack[][maxSize], int *amount, char target[]) {
    if (!vehicleExists(stack, amount, target)) {
        printf("\n[!] O veiculo '%s' NAO esta no estacionamento!\n", target);
        return;
    }

    char tempStack[maxSize][maxSize];
    char currentVehicle[maxSize];
    int tempAmount = 0, vehiclesMoved = 0;

    while (*amount > 0) {
        strcpy(currentVehicle, stack[*amount - 1]);
        pop(amount);

        if (strcasecmp(currentVehicle, target) == 0) {
            printf("Veiculo '%s' foi removido do estacionamento, %d carro(s) foram realocados.\n", target, vehiclesMoved);
            break;
        }
        
        vehiclesMoved++;
        push(tempStack, &tempAmount, currentVehicle);
    }

    while (tempAmount > 0) {
        strcpy(currentVehicle, tempStack[tempAmount - 1]);
        push(stack, amount, currentVehicle);
        pop(&tempAmount);
    }
}

int handleCommand(char line[], char stack[][maxSize], int *amount) {
    line[strcspn(line, "\n")] = 0;
    char *command = strtok(line, " ");

    if (command == NULL) {
        puts("\n[!] Entrada invalida");
        return 0;
    }

    if (strcasecmp(command, "parar") == 0) {
        return 1;
    }
    
    if (strcasecmp(command, "entra") == 0 || strcasecmp(command, "sai") == 0) {
        char *vehicle = strtok(NULL, " ");

        if (vehicle == NULL) {
            printf("\n[!] O comando '%s' precisa do nome do veiculo.\n", command);
            return 0;
        }

        if (strcasecmp(command, "entra") == 0) {
            if (*amount > maxSize) {
                puts("\n[!] Estacionamento lotado!");
            } 
            else if (vehicleExists(stack, amount, vehicle)) {
                printf("\n[!] O veiculo '%s' ja esta no estacionamento!\n", vehicle);
            } 
            else {
                push(stack, amount, vehicle);
                printf("Veiculo '%s' estacionado com sucesso!\n", vehicle);
            }
        } 
        else {
            removeSpecificVehicle(stack, amount, vehicle);
        }

        return 0;
    }

    if (strcasecmp(command, "status") == 0) {
        showStatus(stack, amount);
        return 0;
    }

    printf("\n[!] Comando '%s' desconhecido\n", command);
    return 0;
}

void pauseAndClear() {
    printf("\nPressione ENTER para continuar...");
    fflush(stdin);
    getchar();

    #ifdef _WIN32
        system("cls");
    #else
        system("clear");
    #endif
}

int main() {
    char stack[maxSize][maxSize], line[101];
    int amount = 0, end = 0;

    do {
        puts("====================================================");
        puts("           SISTEMA DE GESTAO DE VAGAS               ");
        puts("====================================================");
        puts(" COMANDOS DISPONIVEIS:");
        puts("  [entra nomeVeiculo]  -> Registrar entrada do veiculo");
        puts("  [sai nomeVeiculo]    -> Registrar saida do veiculo");
        puts("  [status]      -> Exibir ocupacao atual");
        puts("  [parar]        -> Encerrar o programa");
        puts("----------------------------------------------------");
        printf("Digite o comando: ");
        if (fgets(line, sizeof(line), stdin) == NULL) break;

        end = handleCommand(line, stack, &amount);
        pauseAndClear();
    } while (end != 1);

    puts("Encerrando...");
}
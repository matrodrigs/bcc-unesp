#include <stdio.h>
#include <string.h>
#include <ctype.h>

#define maxSize 100

void push(char stack[], int *amount, char character) {
    if (*amount > maxSize) {
        return;
    }

    stack[*amount] = character;
    (*amount)++;
}

void pop(int *amount) {
    if (*amount == 0) {
        return;
    }

    (*amount)--;
}

char peek(char stack[], int size) {
    if (size == 0) {
        return '\0';
    }
    
    return stack[size - 1];
}

void formatString(char string[]) {
    int j = 0;
    for (int i = 0; string[i] != '\0'; i++) {
        if (string[i] != ' ') {
            string[j++] = string[i];
        }
    }
    string[j] = '\0';
    string[strcspn(string, "\n")] = 0;
}

int priority(char c) {
    if (c == '^')
        return 3;
    else if (c == '/' || c == '*')
        return 2;
    else if (c == '+' || c == '-')
        return 1;
    else
        return -1;
}

void convertToPostFix(char equation[]) {
    char postFix[maxSize], stack[maxSize];
    int stackSize = 0, postFixSize = 0;

    for (int i = 0; equation[i] != '\0'; i++) {
        char character = equation[i];

        if (isdigit(character)) {
            push(postFix, &postFixSize, character);
        }
        else {
            if (character == '(') {
            	push(stack, &stackSize, character);
			}
			else if (character == ')') {
                while (peek(stack, stackSize) != '(') {
                    push(postFix, &postFixSize, peek(stack, stackSize));
                    pop(&stackSize);
                }
                pop(&stackSize);
            }
            else {
                while (stackSize > 0 && priority(peek(stack, stackSize)) >= priority(character)) {
                    push(postFix, &postFixSize, peek(stack, stackSize));
                    pop(&stackSize);
                }
                push(stack, &stackSize, character);
            }	
        }
    }

    while (stackSize > 0) {
        push(postFix, &postFixSize, peek(stack, stackSize));
        pop(&stackSize);
    }
    
    postFix[postFixSize] = '\0';
    strcpy(equation, postFix);
}

int postFixSolver(char equation[]) {
    int stack[maxSize]; 
    int stackSize = 0;
    
    if (equation[1] == '\0') {
        return equation[0] - '0';
    }
    
    for (int i = 0; equation[i] != '\0'; i++) {
        char character = equation[i];
        
        if (isdigit(character)) {
            stack[stackSize] = character - '0';
            stackSize++;
        }
        else {
            stackSize--;
            int value1 = stack[stackSize]; 
            
            stackSize--;
            int value2 = stack[stackSize]; 
            
            int result = 0;
                        
            if (character == '+') {
                result = value2 + value1;
            }
            else if (character == '-') {
                result = value2 - value1;
            }
            else if (character == '*') {
                result = value2 * value1;
            }
            else if (character == '/') {
                result = value2 / value1;
            }
            
            stack[stackSize] = result;
            stackSize++;
        }
    }
    
    return stack[0];
}

int main() {
    char equation[maxSize];
    int postFixSize = 0;

    printf("Digite a equacao: ");
    if (fgets(equation, sizeof(equation), stdin) == NULL) return 0;
    
    formatString(equation);
    convertToPostFix(equation);
    
    printf("Expressao pos-fixa: ");
    for (int i = 0; equation[i] != '\0'; i++) {
    	putchar(equation[i]);
    	putchar(' ');
		}
		putchar('\n');
    
    printf("Resultado: %d\n", postFixSolver(equation));
}

package core.cli;

import core.cli.question.Question;
import core.cli.question.QuestionType;
import core.cli.utils.ConsoleHelper;

import java.util.*;
import java.util.function.IntSupplier;

public class MenuRegistry {
    private List<MenuOption> options = new ArrayList<>();

    public MenuOption option(String label, IntSupplier fn) {
        MenuOption option = new MenuOption(label, fn);
        options.add(option);
        return option;
    }

    public MenuOption option(String label) {
        MenuOption option = new MenuOption(label);
        options.add(option);
        return option;
    }

    private List<MenuOption> getActiveOptions() {
        List<MenuOption> active = new ArrayList<>();
        for (MenuOption option : options) {
            if (option.getCondition().getAsBoolean()) {
                active.add(option);
            }
        }
        return active;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            List<MenuOption> activeOptions = getActiveOptions();

            displayMenu(activeOptions);

            int choice = readChoice(scanner);

            if (choice == 0) {
                System.out.println("Encerrando...");
                break;
            }

            if (isInvalidChoice(choice, activeOptions.size())) {
                System.out.println("Opção inválida");
                continue;
            }

            processOption(scanner, activeOptions.get(choice - 1));
            ConsoleHelper.pauseAndClear();
        }
    }

    private void displayMenu(List<MenuOption> activeOptions) {
        System.out.println("\n===== MENU =====");

        for (int i = 0; i < activeOptions.size(); i++) {
            MenuOption option = activeOptions.get(i);
            System.out.println(formatOptionText(i, option));
        }

        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private String formatOptionText(int index, MenuOption option) {
        String baseText = (index + 1) + " - " + option.getLabel();

        if (option.getFn() == null) {
            return baseText;
        }

        int amount = option.getFn().getAsInt();
        String registerLabel = (amount == 1) ? "cadastro" : "cadastros";

        return String.format("%s [%s %s]", baseText, amount, registerLabel);
    }

    private int readChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean isInvalidChoice(int choice, int maxOptions) {
        return choice < 0 || choice > maxOptions;
    }

    private void processOption(Scanner scanner, MenuOption option) {
        Map<String, Object> answers = collectAnswers(scanner, option);
        option.getHandler().handle(new Answers(answers));
    }

    private Map<String, Object> collectAnswers(Scanner scanner, MenuOption option) {
        Map<String, Object> answers = new HashMap<>();

        for (Question q : option.getQuestions()) {
            Object answer = askSingleQuestion(scanner, q, answers); 
            answers.put(q.getKey(), answer);
        }
        return answers;
    }

    private Object askSingleQuestion(Scanner scanner, Question q, Map<String, Object> contextAnswers) {
        do {
            System.out.print(q.getLabel() + " ");
            String input = scanner.nextLine();

            try {
                if (q.getType() == QuestionType.LIST) {
                    int qtd = Integer.parseInt(input);
                    if (qtd < q.getMinLimit() || qtd > q.getMaxLimit()) {
                        System.out.println(String.format("Quantidade inválida. Mínimo: %d, Máximo: %d", q.getMinLimit(), q.getMaxLimit()));                        continue;
                    }

                    List<Map<String, Object>> listAnswers = new ArrayList<>();
                    for (int i = 0; i < qtd; i++) {
                        System.out.println("  --- Item " + (i + 1) + " ---");
                        Map<String, Object> itemMap = new HashMap<>();
                        for (Question subQ : q.getSubQuestions()) {
                            itemMap.put(subQ.getKey(), askSingleQuestion(scanner, subQ, contextAnswers));
                        }
                        listAnswers.add(itemMap);
                    }
                    return listAnswers;
                }

                Object parsedValue = parseInputValue(q, input);

                if (q.hasValidator() && !q.isValid(parsedValue, contextAnswers)) {
                    System.out.println(q.getErrorMessage());
                    continue;
                }

                return parsedValue;

            } catch (Exception e) {
                System.out.println("Entrada inválida. Tente novamente.");
            }
        } while (true);
    }

    private Object parseInputValue(Question q, String input) {
        switch (q.getType()) {
            case NUMBER:
                return Integer.parseInt(input);
            case BOOLEAN:
                return Boolean.parseBoolean(input);
            case DECIMAL:
                return new java.math.BigDecimal(input.replace(",", ".")); 
            default:
                return input;
        }
    }
}
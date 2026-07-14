package core.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

import core.cli.question.Question;
import core.cli.question.QuestionType;
import lombok.Getter;

@Getter
public class MenuOption {
    private String label;
    private List<Question> questions = new ArrayList<>();
    private MenuHandler handler;
    private IntSupplier fn;
    private BooleanSupplier condition = () -> true;

    public MenuOption(String label) {
        this.label = label;
    }

    public MenuOption(String label, IntSupplier fn) {
        this.label = label;
        this.fn = fn;
    }

    public MenuOption ask(Object key, String label, QuestionType type) {
        questions.add(new Question(key.toString(), label, type));
        return this;
    }

    public MenuOption ask(Object key, String label, QuestionType type, Predicate<Object> validator, String errorMessage) {
        questions.add(new Question(key.toString(), label, type, validator, errorMessage));
        return this;
    }

    public MenuOption ask(Object key, String label, QuestionType type, BiPredicate<Object, Map<String, Object>> validator, String errorMessage) {
        questions.add(new Question(key.toString(), label, type, validator, errorMessage));
        return this;
    }

    public MenuOption ask(Object key, String label, int minLimit, int maxLimit, Consumer<MenuOption> subQuestionsBuilder) {
        MenuOption subMenu = new MenuOption("SubMenu Temporário");
        subQuestionsBuilder.accept(subMenu);
        this.questions.add(new Question(key.toString(), label, minLimit, maxLimit, subMenu.getQuestions()));
        return this;
    }

    public MenuOption ask(Object key, String label, QuestionType type, Function<Object, String> dynamicValidator) {
        questions.add(new Question(key.toString(), label, type, dynamicValidator));
        return this;
    }

    public MenuOption when(BooleanSupplier condition) {
        this.condition = condition;
        return this;
    }

    public MenuOption handle(MenuHandler handler) {
        this.handler = handler;
        return this;
    }
}
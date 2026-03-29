package core.cli.question;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.Getter;

@Getter
public class Question {
    private String key;
    private String label;
    private QuestionType type;
    private BiPredicate<Object, Map<String, Object>> validator;
    private String errorMessage;
    private Integer maxLimit;
    private Integer minLimit;
    private List<Question> subQuestions;
    private Function<Object, String> dynamicValidator;

    public Question(String key, String label, QuestionType type) {
        this.key = key;
        this.label = label;
        this.type = type;
        this.validator = null;
        this.errorMessage = null;
    }

    public Question(String key, String label, QuestionType type, Predicate<Object> simpleValidator, String errorMessage) {
        this.key = key;
        this.label = label;
        this.type = type;
        this.errorMessage = errorMessage;
        
        this.validator = (valor, ctx) -> simpleValidator.test(valor);
    }

    public Question(String key, String label, QuestionType type, BiPredicate<Object, Map<String, Object>> contextValidator, String errorMessage) {
        this.key = key;
        this.label = label;
        this.type = type;
        this.validator = contextValidator;
        this.errorMessage = errorMessage;
    }

    public Question(String key, String label, int minLimit, int maxLimit, List<Question> subQuestions) {
        this.key = key;
        this.label = String.format("%s (Mín: %d, Máx: %d)", label, minLimit, maxLimit);
        this.type = QuestionType.LIST;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.subQuestions = subQuestions;
    }

    public Question(String key, String label, QuestionType type, Function<Object, String> dynamicValidator) {
        this.key = key;
        this.label = label;
        this.type = type;
        this.dynamicValidator = dynamicValidator;
    }

    public boolean hasValidator() {
        return this.validator != null || this.dynamicValidator != null;
    }

    public boolean isValid(Object input, Map<String, Object> contextAnswers) {
        if (this.dynamicValidator != null) {
            String dynamicError = this.dynamicValidator.apply(input);
            
            if (dynamicError != null) {
                this.errorMessage = dynamicError;
                return false;
            }
            return true;
        }

        if (this.validator == null) return true;
        return this.validator.test(input, contextAnswers);
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
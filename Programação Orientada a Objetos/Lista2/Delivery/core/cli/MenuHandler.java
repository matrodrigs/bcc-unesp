package core.cli;

@FunctionalInterface
public interface MenuHandler {
    void handle(Answers answers);
}

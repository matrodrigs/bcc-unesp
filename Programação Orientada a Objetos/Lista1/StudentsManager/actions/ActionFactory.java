package actions;

public class ActionFactory {
    public static MenuAction create(int option) {
        switch (option) {
            case 1: return new BatchRegisterAction();
            case 2: return new SearchAction();
            case 3: return new ListAction();
            default: return null;
        }
    }
}

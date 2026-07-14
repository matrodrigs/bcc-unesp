import java.util.Arrays;
import java.util.Scanner;

public class SkillHandler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int studentAmount = scanner.nextInt();
        int[] skills = new int[studentAmount];

        for (int i = 0; i < studentAmount; i++) {
            skills[i] = scanner.nextInt();
        }

        Arrays.sort(skills);

        int totalProblems = 0;
        for (int i = 0; i < studentAmount; i += 2) {
            totalProblems += (skills[i + 1] - skills[i]);
        }

        System.out.println(totalProblems);
        scanner.close();
    }
}

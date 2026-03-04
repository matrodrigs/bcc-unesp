import java.util.Scanner;
import java.util.ArrayList;

class Vector {
    private ArrayList<Integer> values = new ArrayList<>();

    public void add(int value) {
        values.add(value);
        values.sort(null);
    }

    public void show() {
        for (int i = 0; i < values.size(); i++)
            System.out.printf("Vector[%d] = %d\n", i, values.get(i));
    }
}

public class VectorHandler {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        Vector myVector = new Vector();
        int value;

        do {
            System.out.println("Digite um valor para adicionar no vetor (-1 para parar)");
            value = reader.nextInt();

            if (value != -1)
                myVector.add(value);
        } while (value != -1);

        myVector.show();
        reader.close();
    }
}
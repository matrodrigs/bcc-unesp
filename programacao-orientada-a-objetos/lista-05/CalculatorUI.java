import javax.swing.*;
import java.awt.*;

public class CalculatorUI extends JFrame {
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    JTextField firstValue = new JTextField(10);
    JTextField secondValue = new JTextField(10);
    JLabel resultLabel = new JLabel(" = 0");
    JComboBox<String> choices = new JComboBox<>();
    JButton calculate = new JButton("Calcular");

    CalculatorUI() {
        super("Calculadora");

        setLayout(new GridLayout(2, 1));

        choices.addItem("Soma");
        choices.addItem("Subtrai");
        choices.addItem("Multiplica");
        choices.addItem("Divide");
        
        topPanel.add(firstValue);
        topPanel.add(choices);
        topPanel.add(secondValue);
        topPanel.add(resultLabel);
        
        add(topPanel);
        add(calculate);

        pack();

        setSize(400, 100);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new CalculatorUI();
    }
}
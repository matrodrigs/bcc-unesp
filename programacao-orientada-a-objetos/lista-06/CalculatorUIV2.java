import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorUIV2 extends JFrame implements ActionListener {
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    JTextField firstValue = new JTextField(10);
    JTextField secondValue = new JTextField(10);
    JLabel resultLabel = new JLabel(" = 0");
    JComboBox<String> choices = new JComboBox<>();
    JButton calculate = new JButton("Calcular");

    CalculatorUIV2() {
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

        calculate.addActionListener(this);

        add(topPanel);
        add(calculate);

        pack();

        setSize(400, 100);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setResult(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            resultLabel.setText(" = Erro");
            return;
        }

        String valorFormatado = String.format(java.util.Locale.US, "%.2f", value);

        resultLabel.setText(" = " + valorFormatado);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double value1 = Double.parseDouble(firstValue.getText().trim());
            double value2 = Double.parseDouble(secondValue.getText().trim());
            int choice = choices.getSelectedIndex();
            
            if (choice == 0) {
                setResult(value1 + value2);
            }
            else if (choice == 1) {
                setResult(value1 - value2);
            }
            else if (choice == 2) {
                setResult(value1 * value2);
            }
            else if (choice == 3) {
                setResult(value1 / value2);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: Valor(es) inválidos!");
        }
    }

    public static void main(String[] args) {
        new CalculatorUIV2();
    }
}
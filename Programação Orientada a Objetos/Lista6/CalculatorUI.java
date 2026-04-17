import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorUI extends JFrame implements ActionListener {
    JPanel centerPanel = new JPanel(new GridLayout(4, 1));
    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    JTextField firstValue = new JTextField(10);
    JTextField secondValue = new JTextField(10);
    JLabel resultLabel = new JLabel(" = 0");
    JButton sum = new JButton("Soma");
    JButton subtract = new JButton("Subtrai");
    JButton multiply = new JButton("Multiplica");
    JButton divide = new JButton("Divide");

    CalculatorUI() {
        super("Calculadora");

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        centerPanel.add(sum);
        centerPanel.add(subtract);
        centerPanel.add(multiply);
        centerPanel.add(divide);
        rightPanel.add(secondValue);
        rightPanel.add(resultLabel);

        sum.addActionListener(this);
        subtract.addActionListener(this);
        multiply.addActionListener(this);
        divide.addActionListener(this);

        add(firstValue);
        add(centerPanel);
        add(rightPanel);

        pack();

        setSize(420, 200);
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

            if (e.getSource() == sum) {
                setResult(value1 + value2);
            }
            else if (e.getSource() == subtract) {
                setResult(value1 - value2);
            }
            else if (e.getSource() == multiply) {
                setResult(value1 * value2);
            }
            else if (e.getSource() == divide) {
                setResult(value1 / value2);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: Valor(es) inválidos!");
        }
    }

    public static void main(String[] args) {
        new CalculatorUI();
    }
}
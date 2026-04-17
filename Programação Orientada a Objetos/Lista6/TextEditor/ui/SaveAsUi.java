package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveAsUi extends JDialog implements ActionListener {
    JPanel topPanel = new JPanel(new FlowLayout());
    JPanel centerPanel = new JPanel(new FlowLayout());
    JPanel bottomPanel = new JPanel(new FlowLayout());
    JLabel hint = new JLabel("Escolha o seu tipo de arquivo:");
    JComboBox<String> options = new JComboBox<>();
    JButton confirm = new JButton("Confirmar");

    private String selectedExtension = null;

    public SaveAsUi(Frame parent) {
        super(parent, "Opções de arquivo", true);

        setLayout(new GridLayout(3, 1));

        options.addItem(".txt");
        options.addItem(".docx");
        options.addItem(".pdf");

        confirm.addActionListener(this);

        topPanel.add(hint);
        centerPanel.add(options);
        bottomPanel.add(confirm);

        add(topPanel);
        add(centerPanel);
        add(bottomPanel);

        pack();

        setSize(200, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedExtension = (String) options.getSelectedItem();
        dispose();
    }

    public String getSelectedExtension() {
        return selectedExtension;
    }

    public static String askExtension(Component parent) {
        Frame frame = parent instanceof Frame ? (Frame) parent : null;
        SaveAsUi dialog = new SaveAsUi(frame);

        return dialog.getSelectedExtension();
    }
}
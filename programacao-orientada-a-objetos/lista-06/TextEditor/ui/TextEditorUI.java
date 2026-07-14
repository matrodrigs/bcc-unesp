package ui;

import utils.TextHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextEditorUI extends JFrame implements ActionListener {
    JPanel leftPanel = new JPanel(new GridLayout(10, 1));
    JButton open = new JButton("Abrir");
    JButton save = new JButton("Salvar");
    JButton saveAs = new JButton("Salvar Como");
    JButton close = new JButton("Fechar");
    JTextArea textArea = new JTextArea("Digite seu texto aqui...", 5, 20);

    public TextEditorUI() {
        super("Editor de Texto");

        leftPanel.add(open);
        leftPanel.add(save);
        leftPanel.add(saveAs);
        leftPanel.add(close);

        open.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);
        close.addActionListener(this);

        add(leftPanel, BorderLayout.WEST);
        add(textArea, BorderLayout.CENTER);

        pack();

        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == open) {
            String text = TextHandler.open();
            if (text != null) {
                textArea.setText(text);
            }
        }
        else if (e.getSource() == save) {
            TextHandler.save(textArea.getText());
        }
        else if (e.getSource() == saveAs) {
            TextHandler.saveAs(textArea.getText());
        }
        else if (e.getSource() == close) {
            System.exit(0);
        }
    }
}
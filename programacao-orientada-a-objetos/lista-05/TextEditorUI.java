import javax.swing.*;
import java.awt.*;

public class TextEditorUI extends JFrame {
    JPanel leftPanel = new JPanel(new GridLayout(10, 1));
    JButton open = new JButton("Abrir");
    JButton save = new JButton("Salvar");
    JButton saveAs = new JButton("Salvar Como");
    JButton close = new JButton("Fechar");
    JTextField title = new JTextField(20);
    JTextArea textArea = new JTextArea("Digite seu texto aqui...", 5, 20);

    public TextEditorUI() {
        super("Editor de Texto");

        leftPanel.add(open);
        leftPanel.add(save);
        leftPanel.add(saveAs);
        leftPanel.add(close);

        add(leftPanel, BorderLayout.WEST);
        add(textArea, BorderLayout.CENTER);

        pack();

        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TextEditorUI();
    }
}
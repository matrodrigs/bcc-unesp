import core.DeliverySystem;
import gui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        DeliverySystem system = new DeliverySystem();

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(system);
            frame.setVisible(true);
        });
    }
}

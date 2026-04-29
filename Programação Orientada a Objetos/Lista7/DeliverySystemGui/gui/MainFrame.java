package gui;

import core.DeliverySystem;
import gui.tabs.CadastrosTab;
import gui.tabs.PedidosTab;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CadastrosTab cadastrosTab;
    private PedidosTab pedidosTab;

    public MainFrame(DeliverySystem system) {
        setTitle("Sistema de Delivery");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane mainTabbedPane = new JTabbedPane();

        cadastrosTab = new CadastrosTab(system, this);
        pedidosTab = new PedidosTab(system, this);

        mainTabbedPane.addTab("Cadastros", cadastrosTab);
        mainTabbedPane.addTab("Pedidos", pedidosTab);

        add(mainTabbedPane, BorderLayout.CENTER);

        refreshState();
    }

    public void refreshState() {
        cadastrosTab.refresh();
        pedidosTab.refresh();
    }
}

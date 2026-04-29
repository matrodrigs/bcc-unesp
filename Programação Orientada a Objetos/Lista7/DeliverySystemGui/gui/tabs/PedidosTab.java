package gui.tabs;

import core.DeliverySystem;
import gui.MainFrame;
import models.Client;
import models.DeliveryMan;
import models.MenuItem;
import models.Order;
import models.OrderStatus;
import models.Restaurant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PedidosTab extends JPanel {
    private DeliverySystem system;
    private MainFrame mainFrame;

    private JComboBox<Client> cbClients;
    private JComboBox<Restaurant> cbRestaurants;
    private JComboBox<String> cbDishes;
    private DefaultTableModel orderItemsModel;
    private JLabel lblTotalOrder;
    private JButton btnCreateOrder;
    private JButton btnAddDish;

    private JComboBox<Order> cbPendingOrders;
    private JComboBox<DeliveryMan> cbAvailableDeliveryMen;
    private JButton btnAssign;

    private JComboBox<Order> cbUpdatableOrders;
    private JButton btnUpdateStatus;

    private JTextArea txtOrdersList;

    public PedidosTab(DeliverySystem system, MainFrame mainFrame) {
        this.system = system;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Novo Pedido", createNewOrderPanel());
        tabs.addTab("Atribuir Entregador", createAssignPanel());
        tabs.addTab("Atualizar Status", createUpdateStatusPanel());
        tabs.addTab("Lista de Pedidos", createListOrdersPanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createNewOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbClients = new JComboBox<>();
        cbRestaurants = new JComboBox<>();
        cbDishes = new JComboBox<>();
        btnAddDish = new JButton("Adicionar ao Pedido");
        btnCreateOrder = new JButton("Criar Pedido");

        cbClients.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Client) {
                    Client c = (Client) value;
                    setText(c.getId() + " - " + c.getName());
                }
                return this;
            }
        });

        cbRestaurants.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Restaurant) {
                    Restaurant r = (Restaurant) value;
                    setText(r.getId() + " - " + r.getName());
                }
                return this;
            }
        });

        cbRestaurants.addActionListener(e -> {
            Restaurant r = (Restaurant) cbRestaurants.getSelectedItem();
            cbDishes.removeAllItems();
            if (r != null) {
                for (MenuItem item : r.getMenu()) {
                    cbDishes.addItem(item.getDishName());
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        topPanel.add(cbClients, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(new JLabel("Restaurante:"), gbc);
        gbc.gridx = 1;
        topPanel.add(cbRestaurants, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(new JLabel("Prato:"), gbc);

        JPanel dishPanel = new JPanel(new BorderLayout(5, 0));
        dishPanel.add(cbDishes, BorderLayout.CENTER);
        dishPanel.add(btnAddDish, BorderLayout.EAST);
        gbc.gridx = 1;
        topPanel.add(dishPanel, gbc);

        orderItemsModel = new DefaultTableModel(new String[] { "Prato", "Preço (R$)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(orderItemsModel);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeMenuItem = new JMenuItem("Remover prato");
        popupMenu.add(removeMenuItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        removeMenuItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Deseja remover o prato selecionado do pedido?",
                        "Remover prato",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    orderItemsModel.removeRow(selectedRow);
                    updateTotal();
                }
            }
        });

        btnAddDish.addActionListener(e -> {
            Restaurant r = (Restaurant) cbRestaurants.getSelectedItem();
            String dishName = (String) cbDishes.getSelectedItem();
            if (r != null && dishName != null) {
                orderItemsModel.addRow(new Object[] { dishName, String.format("%.2f", r.getDishPrice(dishName)) });
                updateTotal();
            }
        });

        btnCreateOrder.addActionListener(e -> {
            Client c = (Client) cbClients.getSelectedItem();
            Restaurant r = (Restaurant) cbRestaurants.getSelectedItem();

            if (c == null || r == null) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente e um restaurante!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (orderItemsModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Adicione pelo menos um prato ao pedido!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<MenuItem> items = new ArrayList<>();
            for (int i = 0; i < orderItemsModel.getRowCount(); i++) {
                String dishName = (String) orderItemsModel.getValueAt(i, 0);
                items.add(new MenuItem(dishName, r.getDishPrice(dishName)));
            }

            int newOrderId = system.getOrders().size() + 1;
            Order order = new Order(newOrderId, c, r, items);
            system.createOrder(order);

            JOptionPane.showMessageDialog(this, "Pedido Nº " + newOrderId + " criado com sucesso!");
            orderItemsModel.setRowCount(0);
            updateTotal();
            mainFrame.refreshState();
        });

        lblTotalOrder = new JLabel("Total: R$ 0,00");
        lblTotalOrder.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        bottomPanel.add(lblTotalOrder);
        bottomPanel.add(btnCreateOrder);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAssignPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbPendingOrders = new JComboBox<>();
        cbAvailableDeliveryMen = new JComboBox<>();
        btnAssign = new JButton("Atribuir Entregador");

        cbPendingOrders.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Order) {
                    Order o = (Order) value;
                    setText("Pedido Nº " + o.getId() + " - " + o.getClient().getName());
                }
                return this;
            }
        });

        cbAvailableDeliveryMen.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DeliveryMan) {
                    DeliveryMan dm = (DeliveryMan) value;
                    setText(dm.getId() + " - " + dm.getName() + " (" + dm.getTransportMode() + ")");
                }
                return this;
            }
        });

        btnAssign.addActionListener(e -> {
            Order o = (Order) cbPendingOrders.getSelectedItem();
            DeliveryMan dm = (DeliveryMan) cbAvailableDeliveryMen.getSelectedItem();

            if (o != null && dm != null) {
                system.assignDeliveryManToOrder(o.getId(), dm.getId());
                JOptionPane.showMessageDialog(this,
                        "Entregador " + dm.getName() + " atribuído ao pedido Nº " + o.getId());
                mainFrame.refreshState();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Pedido (EM PREPARO):"), gbc);
        gbc.gridx = 1;
        panel.add(cbPendingOrders, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Entregador:"), gbc);
        gbc.gridx = 1;
        panel.add(cbAvailableDeliveryMen, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(btnAssign, gbc);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.add(panel);
        return wrapper;
    }

    private JPanel createUpdateStatusPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbUpdatableOrders = new JComboBox<>();
        btnUpdateStatus = new JButton("Avançar Status do Pedido");

        cbUpdatableOrders.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Order) {
                    Order o = (Order) value;
                    setText("Pedido Nº " + o.getId() + " - Status: " + o.getStatus());
                }
                return this;
            }
        });

        btnUpdateStatus.addActionListener(e -> {
            Order o = (Order) cbUpdatableOrders.getSelectedItem();
            if (o != null) {
                Order updated = system.advanceOrderStatus(o.getId());
                String msg = "Status do pedido Nº " + o.getId() + " atualizado para: " + updated.getStatus();
                if (updated.getStatus() == OrderStatus.ENTREGUE && updated.getDeliveryMan() != null) {
                    msg += "\nO entregador " + updated.getDeliveryMan().getName() + " está livre novamente.";
                }
                JOptionPane.showMessageDialog(this, msg);
                mainFrame.refreshState();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Pedido:"), gbc);
        gbc.gridx = 1;
        panel.add(cbUpdatableOrders, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(btnUpdateStatus, gbc);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.add(panel);
        return wrapper;
    }

    private JPanel createListOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        txtOrdersList = new JTextArea();
        txtOrdersList.setEditable(false);
        txtOrdersList.setFont(new Font("Monospaced", Font.PLAIN, 12));

        panel.add(new JScrollPane(txtOrdersList), BorderLayout.CENTER);
        return panel;
    }

    public void refresh() {
        cbClients.removeAllItems();
        for (Client c : system.getClients())
            cbClients.addItem(c);

        Restaurant currentRest = (Restaurant) cbRestaurants.getSelectedItem();
        cbRestaurants.removeAllItems();
        for (Restaurant r : system.getRestaurants())
            cbRestaurants.addItem(r);
        if (currentRest != null)
            cbRestaurants.setSelectedItem(currentRest);

        boolean canCreateOrder = !system.getClients().isEmpty() && !system.getRestaurants().isEmpty();
        btnCreateOrder.setEnabled(canCreateOrder);
        btnAddDish.setEnabled(canCreateOrder);
        cbClients.setEnabled(canCreateOrder);
        cbRestaurants.setEnabled(canCreateOrder);
        cbDishes.setEnabled(canCreateOrder);

        if (!canCreateOrder) {
            btnCreateOrder.setToolTipText("Cadastre ao menos um cliente e um restaurante para criar pedidos.");
            btnAddDish.setToolTipText("Cadastre ao menos um cliente e um restaurante para criar pedidos.");
        } else {
            btnCreateOrder.setToolTipText(null);
            btnAddDish.setToolTipText(null);
        }

        cbPendingOrders.removeAllItems();
        boolean hasPending = false;
        for (Order o : system.getOrders()) {
            if (o.getStatus() == OrderStatus.EM_PREPARO && o.getDeliveryMan() == null) {
                cbPendingOrders.addItem(o);
                hasPending = true;
            }
        }

        cbAvailableDeliveryMen.removeAllItems();
        boolean hasDeliveryMen = false;
        for (DeliveryMan dm : system.getDeliveryMen()) {
            if (dm.isAvailable()) {
                cbAvailableDeliveryMen.addItem(dm);
                hasDeliveryMen = true;
            }
        }

        boolean canAssign = hasPending && hasDeliveryMen;
        btnAssign.setEnabled(canAssign);
        cbPendingOrders.setEnabled(canAssign);
        cbAvailableDeliveryMen.setEnabled(canAssign);

        if (!canAssign) {
            String reason = "Para atribuir, é necessário haver pedidos EM PREPARO e entregadores disponíveis.";
            btnAssign.setToolTipText(reason);
            cbPendingOrders.setToolTipText(reason);
            cbAvailableDeliveryMen.setToolTipText(reason);
        } else {
            btnAssign.setToolTipText(null);
            cbPendingOrders.setToolTipText(null);
            cbAvailableDeliveryMen.setToolTipText(null);
        }

        cbUpdatableOrders.removeAllItems();
        boolean hasUpdatable = false;
        for (Order o : system.getOrders()) {
            if (o.getStatus() != OrderStatus.ENTREGUE &&
                    !(o.getStatus() == OrderStatus.EM_PREPARO && o.getDeliveryMan() == null)) {
                cbUpdatableOrders.addItem(o);
                hasUpdatable = true;
            }
        }

        btnUpdateStatus.setEnabled(hasUpdatable);
        cbUpdatableOrders.setEnabled(hasUpdatable);

        if (!hasUpdatable) {
            String reason = "Não há pedidos disponíveis para atualizar o status. (Pedidos EM PREPARO precisam de entregador primeiro)";
            btnUpdateStatus.setToolTipText(reason);
            cbUpdatableOrders.setToolTipText(reason);
        } else {
            btnUpdateStatus.setToolTipText(null);
            cbUpdatableOrders.setToolTipText(null);
        }

        StringBuilder sb = new StringBuilder();
        if (system.getOrders().isEmpty()) {
            sb.append("Nenhum pedido cadastrado no sistema.");
        } else {
            java.util.List<Order> orders = system.getOrders();
            for (int i = orders.size() - 1; i >= 0; i--) {
                Order o = orders.get(i);
                sb.append(o.getOrderSummary()).append("\n-----------------------------------\n");
            }
        }
        txtOrdersList.setText(sb.toString());
    }

    private void updateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < orderItemsModel.getRowCount(); i++) {
            String priceStr = (String) orderItemsModel.getValueAt(i, 1);
            try {
                BigDecimal price = new BigDecimal(priceStr.replace(",", "."));
                total = total.add(price);
            } catch (Exception e) {
            }
        }
        lblTotalOrder.setText(String.format("Total: R$ %.2f", total));
    }
}

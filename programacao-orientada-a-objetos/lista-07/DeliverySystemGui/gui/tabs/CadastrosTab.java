package gui.tabs;

import core.DeliverySystem;
import gui.MainFrame;
import models.Client;
import models.DeliveryMan;
import models.MenuItem;
import models.Restaurant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CadastrosTab extends JPanel {
    private DeliverySystem system;
    private MainFrame mainFrame;

    private DefaultListModel<Client> clientListModel;
    private JList<Client> clientList;
    private DefaultListModel<DeliveryMan> deliveryManListModel;
    private JList<DeliveryMan> deliveryManList;
    private DefaultListModel<Restaurant> restaurantListModel;
    private JList<Restaurant> restaurantList;

    public CadastrosTab(DeliverySystem system, MainFrame mainFrame) {
        this.system = system;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Cliente", createClientPanel());
        tabs.addTab("Entregador", createDeliveryManPanel());
        tabs.addTab("Restaurante", createRestaurantPanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createClientPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);

        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        clientList.setCellRenderer(new DefaultListCellRenderer() {
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

        JScrollPane scrollList = new JScrollPane(clientList);
        scrollList.setBorder(BorderFactory.createTitledBorder("Clientes"));
        splitPane.setLeftComponent(scrollList);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtName = new JTextField(20);
        JTextField txtId = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JButton btnSave = new JButton("Cadastrar Cliente");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(txtName, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(btnSave, gbc);

        clientList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && clientList.getSelectedValue() != null) {
                Client c = clientList.getSelectedValue();
                txtName.setText(c.getName());
                txtId.setText(String.valueOf(c.getId()));
                txtEmail.setText(c.getEmail());
            }
        });

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                int id = Integer.parseInt(txtId.getText().trim());
                String email = txtEmail.getText().trim();

                if (name.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (system.isIdTaken(id)) {
                    JOptionPane.showMessageDialog(this, "ID já está em uso!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                system.register(new Client(name, id, email));
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
                txtName.setText("");
                txtId.setText("");
                txtEmail.setText("");
                mainFrame.refreshState();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formWrapper.add(panel);
        splitPane.setRightComponent(formWrapper);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(splitPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createDeliveryManPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);

        deliveryManListModel = new DefaultListModel<>();
        deliveryManList = new JList<>(deliveryManListModel);
        deliveryManList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DeliveryMan) {
                    DeliveryMan d = (DeliveryMan) value;
                    setText(d.getId() + " - " + d.getName());
                }
                return this;
            }
        });

        JScrollPane scrollList = new JScrollPane(deliveryManList);
        scrollList.setBorder(BorderFactory.createTitledBorder("Entregadores"));
        splitPane.setLeftComponent(scrollList);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtName = new JTextField(20);
        JTextField txtId = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JComboBox<String> cbVehicle = new JComboBox<>(new String[] { "Moto", "Bicicleta" });
        JButton btnSave = new JButton("Cadastrar Entregador");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(txtName, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Veículo:"), gbc);
        gbc.gridx = 1;
        panel.add(cbVehicle, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(btnSave, gbc);

        deliveryManList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && deliveryManList.getSelectedValue() != null) {
                DeliveryMan d = deliveryManList.getSelectedValue();
                txtName.setText(d.getName());
                txtId.setText(String.valueOf(d.getId()));
                txtEmail.setText(d.getEmail());
                cbVehicle.setSelectedItem(d.getTransportMode());
            }
        });

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                int id = Integer.parseInt(txtId.getText().trim());
                String email = txtEmail.getText().trim();
                String vehicle = (String) cbVehicle.getSelectedItem();

                if (name.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (system.isIdTaken(id)) {
                    JOptionPane.showMessageDialog(this, "ID já está em uso!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                system.register(new DeliveryMan(name, id, email, vehicle));
                JOptionPane.showMessageDialog(this, "Entregador cadastrado com sucesso!");
                txtName.setText("");
                txtId.setText("");
                txtEmail.setText("");
                mainFrame.refreshState();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formWrapper.add(panel);
        splitPane.setRightComponent(formWrapper);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(splitPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createRestaurantPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);

        restaurantListModel = new DefaultListModel<>();
        restaurantList = new JList<>(restaurantListModel);
        restaurantList.setCellRenderer(new DefaultListCellRenderer() {
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

        JScrollPane scrollList = new JScrollPane(restaurantList);
        scrollList.setBorder(BorderFactory.createTitledBorder("Restaurantes"));
        splitPane.setLeftComponent(scrollList);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtName = new JTextField(20);
        JTextField txtId = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JTextField txtNickname = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtId, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Nome Fantasia:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNickname, gbc);

        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBorder(BorderFactory.createTitledBorder("Cardápio (Clique botão direito para remover)"));
        DefaultTableModel tableModel = new DefaultTableModel(new String[] { "Prato", "Preço (R$)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);

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
                        "Deseja realmente remover o prato selecionado?",
                        "Remover prato",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                }
            }
        });

        JPanel addMenuItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtDishName = new JTextField(15);
        JTextField txtDishPrice = new JTextField(8);
        JButton btnAddDish = new JButton("Adicionar Prato");

        addMenuItemPanel.add(new JLabel("Prato:"));
        addMenuItemPanel.add(txtDishName);
        addMenuItemPanel.add(new JLabel("Preço:"));
        addMenuItemPanel.add(txtDishPrice);
        addMenuItemPanel.add(btnAddDish);

        btnAddDish.addActionListener(e -> {
            String dishName = txtDishName.getText().trim();
            String dishPriceStr = txtDishPrice.getText().trim().replace(",", ".");
            if (dishName.isEmpty() || dishPriceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha o nome e o preço do prato!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                BigDecimal price = new BigDecimal(dishPriceStr);
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "O preço deve ser maior que zero!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                tableModel.addRow(new Object[] { dishName, String.format("%.2f", price) });
                txtDishName.setText("");
                txtDishPrice.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        menuPanel.add(addMenuItemPanel, BorderLayout.NORTH);
        menuPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnSave = new JButton("Cadastrar Restaurante");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnSave);

        restaurantList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && restaurantList.getSelectedValue() != null) {
                Restaurant r = restaurantList.getSelectedValue();
                txtName.setText(r.getName());
                txtId.setText(String.valueOf(r.getId()));
                txtEmail.setText(r.getEmail());
                txtNickname.setText(r.getNickname());

                tableModel.setRowCount(0);
                for (MenuItem item : r.getMenu()) {
                    tableModel.addRow(new Object[] { item.getDishName(), String.format("%.2f", item.getValue()) });
                }
            }
        });

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                int id = Integer.parseInt(txtId.getText().trim());
                String email = txtEmail.getText().trim();
                String nickname = txtNickname.getText().trim();

                if (name.isEmpty() || email.isEmpty() || nickname.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos do restaurante!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (system.isIdTaken(id)) {
                    JOptionPane.showMessageDialog(this, "ID já está em uso!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Adicione pelo menos um prato ao cardápio!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<MenuItem> items = new ArrayList<>();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String dishName = (String) tableModel.getValueAt(i, 0);
                    BigDecimal price = new BigDecimal(((String) tableModel.getValueAt(i, 1)).replace(",", "."));
                    items.add(new MenuItem(dishName, price));
                }

                system.register(new Restaurant(name, id, email, nickname, items));
                JOptionPane.showMessageDialog(this, "Restaurante cadastrado com sucesso!");
                txtName.setText("");
                txtId.setText("");
                txtEmail.setText("");
                txtNickname.setText("");
                tableModel.setRowCount(0);
                mainFrame.refreshState();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topWrapper.add(formPanel);

        panel.add(topWrapper, BorderLayout.NORTH);
        panel.add(menuPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        splitPane.setRightComponent(panel);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(splitPane, BorderLayout.CENTER);
        return wrapper;
    }

    public void refresh() {
        if (clientListModel != null) {
            clientListModel.clear();
            for (Client c : system.getClients()) {
                clientListModel.addElement(c);
            }
        }

        if (deliveryManListModel != null) {
            deliveryManListModel.clear();
            for (DeliveryMan d : system.getDeliveryMen()) {
                deliveryManListModel.addElement(d);
            }
        }

        if (restaurantListModel != null) {
            restaurantListModel.clear();
            for (Restaurant r : system.getRestaurants()) {
                restaurantListModel.addElement(r);
            }
        }
    }
}
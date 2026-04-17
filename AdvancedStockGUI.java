import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

//GUI Class
public class AdvancedStockGUI extends JFrame {
    private JTextField nameField, qtyField, buyField, currentField;
    private JTable table;
    private DefaultTableModel model;
    private Portfolio portfolio;

    public AdvancedStockGUI() {
        portfolio = new Portfolio();
        portfolio.createTable();

        setTitle("Stock Portfolio Manager");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel (Form)
        JPanel panel = new JPanel(new GridLayout(2, 4));

        nameField = new JTextField();
        qtyField = new JTextField();
        buyField = new JTextField();
        currentField = new JTextField();

        panel.add(new JLabel("Name"));
        panel.add(new JLabel("Quantity"));
        panel.add(new JLabel("Buy/Sell Price"));
        panel.add(new JLabel("Current Price"));

        panel.add(nameField);
        panel.add(qtyField);
        panel.add(buyField);
        panel.add(currentField);

        add(panel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] {
                "Name", "Quantity", "Buy Price", "Current Price", "Profit/Loss", "Position"
        });

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();

        JButton addBtn = new JButton("Buy");
        JButton sellBtn = new JButton("Sell");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        btnPanel.add(addBtn);
        btnPanel.add(sellBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        add(btnPanel, BorderLayout.SOUTH);
        SwingUtilities.invokeLater(() -> refreshTable());

        // Add Button
        addBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                double buy = Double.parseDouble(buyField.getText());
                double current = Double.parseDouble(currentField.getText());

                portfolio.addStock(new Stock(name, qty, buy, current, "LONG"));

                refreshTable();

                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input!");
            }
        });

        sellBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int qty = Integer.parseInt(qtyField.getText());

                portfolio.sellStock(name, qty);
                refreshTable();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Sell Input!");
            }
        });

        // Delete Button
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first!");
            } else {
                String name = table.getValueAt(selectedRow, 0).toString();
                portfolio.removeStock(name);
                refreshTable();
            }
        });

        // Refresh Button
        refreshBtn.addActionListener(e -> refreshTable());
    }

    // Refresh Table
    private void refreshTable() {
        model.setRowCount(0);

        for (Stock s : portfolio.getStocks()) {
            model.addRow(new Object[] {
                    s.getName(),
                    s.getQuantity(),
                    s.getBuyPrice(),
                    s.getCurrentPrice(),
                    s.getProfitLoss(),
                    s.getPositionType()
            });
        }
    }

    // Clear Input Fields
    private void clearFields() {
        nameField.setText("");
        qtyField.setText("");
        buyField.setText("");
        currentField.setText("");
    }

    public static void main(String[] args) {
        new AdvancedStockGUI().setVisible(true);
    }
}
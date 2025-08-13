import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class RestaurantBillingSystem6 extends JFrame implements ActionListener {
    private JTextField totalInput;
    private JButton generateBillButton, resetButton;
    private JList<String> categoryList = new JList<>(new String[] { "Non-Veg", "Veg", "Tiffins" });

    private JPanel middlePanel = new JPanel();
    private JPanel billPanel = new JPanel();

    private JPanel topPanel = new JPanel();

    private DefaultTableModel tableModel;
    private JTable receiptTable;
    private String column[] = { "Item", "Quantity", "Price", "Total" };

    private String[] tiffins = { "Idli", "Dosa", "Vada", "Upma", "Chapati", "Tea", "Ravva Dosa", "Garelu" };
    private String[] NonVeg = { "Chicken curry", "Mutton Curry", "Chicken dum biryani", "Mutton Dum biryani", "Prawns",
            "Fish" };
    private String[] Veg = { "paneer curry", "sambar", "baby corn", "Mushroom", "brinjal", "Potatot fry" };

    private double totalBill = 0.0;

    public RestaurantBillingSystem6() {

        setTitle("Restaurant Billing System");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        topPanel.setLayout(new GridLayout(1, 2));

        for (int i = 0; i < categoryList.getModel().getSize(); i++) {
            String category = categoryList.getModel().getElementAt(i);
            JButton categoryButton = new JButton(category);

            categoryButton.putClientProperty("category", getCategoryItems(category));

            categoryButton.addActionListener(e -> {
                String[] selectedCategory = (String[]) categoryButton.getClientProperty("category");

                displayItemList(selectedCategory);
            });

            topPanel.add(categoryButton);
        }

        JPanel footer = new JPanel();
        totalInput = new JTextField("Amount");
        totalInput.setEditable(false);
        generateBillButton = new JButton("Generate Bill");
        resetButton = new JButton("Reset");
        footer.add(generateBillButton);
        footer.add(totalInput);
        footer.add(resetButton);

        JPanel receiptPanel = new JPanel();

        tableModel = new DefaultTableModel(column, 0);
        receiptTable = new JTable(tableModel);
        receiptTable.setEditingRow(0);

        JScrollPane scrollPane = new JScrollPane(receiptTable);

        receiptPanel.add(scrollPane);

        JPanel centerPanel = new JPanel();
        centerPanel.add(middlePanel, BorderLayout.WEST);
        centerPanel.add(receiptPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        generateBillButton.addActionListener(this);
        resetButton.addActionListener(this);
        displayItemList(tiffins);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateBillButton) {
            generateBill();
        } else if (e.getSource() == resetButton) {
            resetFrame();
        }
    }

    private String[] getCategoryItems(String category) {
        switch (category) {
            case "Non-Veg":
                return NonVeg;
            case "Veg":
                return Veg;
            case "Tiffins":
                return tiffins;
            default:
                return new String[0];
        }
    }

    private void displayItemList(String[] items) {

        middlePanel.removeAll();
        int columns = items.length / 2 + items.length % 2;

        GridLayout gridLayout = new GridLayout(2, columns);
        JPanel itemsPanel = new JPanel(gridLayout);
        gridLayout.setHgap(10);
        gridLayout.setVgap(10);
        for (String item : items) {
            JButton itemButton = new JButton(item);
            itemButton.addActionListener(e -> askForQuantity(item));

            Dimension buttonSize = new Dimension(200, 50);
            itemButton.setPreferredSize(buttonSize);

            itemButton.setBackground(Color.lightGray);
            itemButton.setForeground(Color.BLACK);
            itemsPanel.add(itemButton);
        }

        middlePanel.add(itemsPanel);

        middlePanel.revalidate();
        middlePanel.repaint();
    }

    private void askForQuantity(String itemName) {
        try {
            String quantityString = JOptionPane.showInputDialog(this, "Enter quantity for " + itemName + ":");
            String price = JOptionPane.showInputDialog(this, "Enter price for " + itemName + ":");

            if (quantityString != null  || price!=null) {
                addItem(itemName, quantityString, price);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for quantity.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addItem(String itemName, String quantity, String price) {
        try {
            int itemQuantity = Integer.parseInt(quantity);
            double itemPrice = Double.parseDouble(price);

            double total = itemQuantity * itemPrice;

            Object[] rowData = { itemName, quantity, price, total };
            tableModel.addRow(rowData);

            totalBill += total;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for quantity and price.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateBill() {
        Component[] components = topPanel.getComponents();

        for (Component component : components) {
            component.setEnabled(false);
        }

        generateBillButton.setEnabled(false);
        String myString = Double.toString(totalBill);
        totalInput.setText(myString);
        billPanel.revalidate();
        billPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RestaurantBillingSystem6 billingSystem = new RestaurantBillingSystem6();
            billingSystem.setVisible(true);
        });
    }

    private void resetFrame() {
        tableModel.setRowCount(0);
        generateBillButton.setEnabled(true);
        totalBill = totalBill * 0.0;
        totalInput.setText("");
        Component[] components = topPanel.getComponents();
        for (Component component : components) {
            component.setEnabled(true);
        }

    }

}

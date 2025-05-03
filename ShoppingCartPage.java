import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class ShoppingCartPage extends JPanel {
    private Font customFont;
    private JLabel titleLabel, emptyLabel;
    private JScrollPane orderPane, tablePane;
    private JPanel orderPanel, titlePanel, buttonPanel;
    private JButton addButton, removeButton, placeButton;
    private JTable cartTable;
    private double currentBalance = 5000.00;
    private HomePage homePage;
    private Runnable onAddToCartRedirect;
    private OrderSummaryPage orderSummaryPage;
    private RatingsPage ratingsPage;
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color lightGray = new Color(211, 211, 211);
    private Color mediumBlue = new Color(125, 125, 200);

    public ShoppingCartPage(HomePage homePage, Runnable onAddToCartRedirect, OrderSummaryPage orderSummaryPage, RatingsPage ratingsPage, Font font) {
        setLayout(new BorderLayout());

        this.homePage = homePage;
        this.onAddToCartRedirect = onAddToCartRedirect;
        this.orderSummaryPage = orderSummaryPage;
        this.ratingsPage = ratingsPage;
        
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.ITALIC, 40);
        }
        
        this.customFont = (font != null) ? font : customFont;

        titlePanel = new JPanel();
        titlePanel.setBackground(darkBlue);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
                             BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray),
                             BorderFactory.createEmptyBorder(20, 20, 20, 0)));

        titleLabel = new JLabel(" Shopping Cart");
        titleLabel.setFont(customFont.deriveFont(40f));
        titleLabel.setForeground(white);
        titleLabel.setOpaque(false);

        orderPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2D = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, darkBlue, 0, getHeight(), lightBlue);
                graphics2D.setPaint(gradient);
                graphics2D.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        orderPanel.setOpaque(false);
        orderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        orderPane = new JScrollPane(orderPanel);
        orderPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        orderPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        orderPane.getVerticalScrollBar().setUnitIncrement(16);
        orderPane.setBorder(BorderFactory.createEmptyBorder());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        buttonPanel.setBackground(lightBlue);

        addButton = new JButton("Add to Cart");
        addButton.setBackground(mediumBlue);
        addButton.setForeground(white);
        addButton.setFont(customFont.deriveFont(15f));
        addButton.setFocusable(false);
        addButton.setContentAreaFilled(false);
        addButton.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createLineBorder(lightGray, 1),
                           BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addToCart());
        addButton.setOpaque(true);

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(darkBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                addButton.setBackground(darkBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                addButton.setBackground(darkBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(mediumBlue);
            }
        });

        removeButton = new JButton("Remove from Cart");
        removeButton.setBackground(mediumBlue);
        removeButton.setForeground(white);
        removeButton.setFont(customFont.deriveFont(15f));
        removeButton.setFocusable(false);
        removeButton.setContentAreaFilled(false);
        removeButton.setBorder(BorderFactory.createCompoundBorder(
                              BorderFactory.createLineBorder(lightGray, 1),
                              BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeButton.addActionListener(e -> removeFromCart());
        removeButton.setOpaque(true);

        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                removeButton.setBackground(darkBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                removeButton.setBackground(darkBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                removeButton.setBackground(darkBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                removeButton.setBackground(mediumBlue);
            }
        });

        placeButton = new JButton("Place Order");
        placeButton.setBackground(mediumBlue);
        placeButton.setForeground(white);
        placeButton.setFont(customFont.deriveFont(15f));
        placeButton.setFocusable(false);
        placeButton.setContentAreaFilled(false);
        placeButton.setBorder(BorderFactory.createCompoundBorder(
                               BorderFactory.createLineBorder(lightGray, 1),
                               BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        placeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        placeButton.addActionListener(e -> confirmOrder());
        placeButton.setOpaque(true);

        placeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                placeButton.setBackground(darkBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                placeButton.setBackground(darkBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                placeButton.setBackground(darkBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                placeButton.setBackground(mediumBlue);
            }
        });

        titlePanel.add(titleLabel);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(placeButton);
        add(titlePanel, BorderLayout.NORTH);
        add(orderPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshCartItems();
    }

    public void refreshCartItems() {
        orderPanel.removeAll();

        java.util.List<MainContents.CartItem> currentItems = MainContents.getCartItems();

        if (currentItems.isEmpty()) {
            emptyLabel = new JLabel("Your cart is empty.");
            emptyLabel.setFont(new Font(null, Font.BOLD, 20));
            emptyLabel.setForeground(white);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            orderPanel.add(emptyLabel);
        } else {
            String[] columnNames = {"Product", "Details", "Price", "Quantity", "Subtotal"};
            Object[][] data = new Object[currentItems.size()][5];

            for (int i = 0; i < currentItems.size(); i++) {
                MainContents.CartItem item = currentItems.get(i);
                ImageIcon itemIcon = new ImageIcon(new ImageIcon(item.iconPath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                data[i][0] = itemIcon;
                data[i][1] = String.format("<html>%s<br>Size: %s</html>", item.name, item.size);
                data[i][2] = item.price;
                data[i][3] = item.quantity;
                double price = Double.parseDouble(item.price.replace("₱", "").replace(",", ""));
                data[i][4] = "₱" + String.format("%,.2f", price * item.quantity);
            }

            cartTable = new JTable(data, columnNames) {
                public boolean isCellEditable(int row, int col) {
                    return false;
                }

                public Class<?> getColumnClass(int column) {
                    return getValueAt(0, column).getClass();
                }
            };

            cartTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            cartTable.setRowHeight(120);
            cartTable.setFont(customFont.deriveFont(15f));
            cartTable.getTableHeader().setFont(customFont.deriveFont(20f));

            DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer)
            cartTable.getTableHeader().getDefaultRenderer();
            headerRenderer.setHorizontalAlignment(JLabel.CENTER);

            cartTable.setBackground(new Color(211, 211, 211));
            cartTable.setOpaque(true);

            DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            cartTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);

            cartTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            cartTable.getColumnModel().getColumn(1).setPreferredWidth(300);
            cartTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            cartTable.getColumnModel().getColumn(3).setPreferredWidth(80); 
            cartTable.getColumnModel().getColumn(4).setPreferredWidth(100);

            tablePane = new JScrollPane(cartTable);
            tablePane.getViewport().setBackground(new Color(211, 211, 211));
            orderPanel.add(tablePane);
        }

        orderPanel.revalidate();
        orderPanel.repaint();
    }

    public void showCart() {
        refreshCartItems();
        revalidate();
        repaint();
    }

    private void addToCart() {
        if (onAddToCartRedirect != null) {
            onAddToCartRedirect.run();
        }
    }

    private void removeFromCart() {
        if(cartTable == null || cartTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "There are no items to remove.", "Cart Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int[] selectedRows = cartTable.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one item to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
        } else {
            java.util.List<MainContents.CartItem> cartItems = MainContents.getCartItems();
            java.util.List<MainContents.CartItem> toRemove = new java.util.ArrayList<>();
            StringBuilder builder = new StringBuilder();

            for (int row : selectedRows) {
                if (row >= 0 && row < cartItems.size()) {
                    MainContents.CartItem item = cartItems.get(row);
                    toRemove.add(item);
                    builder.append(item.name).append(", ");
                }
            }

            if (builder.length() > 0) {
                builder.setLength(builder.length() - 2);
            }

            cartItems.removeAll(toRemove);
            refreshCartItems();

            if (cartItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is now empty.", "Cart Empty", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, builder.toString() + " removed from cart.", "Items Removed", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void confirmOrder() {
        java.util.List<MainContents.CartItem> cartItems = MainContents.getCartItems();
        
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no items to be placed.", "Cart Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        int[] selectedRows = cartTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one item to place.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        double subtotal = 0.00;
        java.util.List<MainContents.CartItem> toRemove = new java.util.ArrayList<>();
        Object[][] selectedData = new Object[selectedRows.length][5];
        String[] columnNames = {"Product", "Details", "Price", "Quantity", "Subtotal"};
    
        for (int i = 0; i < selectedRows.length; i++) {
            int row = selectedRows[i];
            if (row >= 0 && row < cartItems.size()) {
                MainContents.CartItem item = cartItems.get(row);
                double itemPrice = Double.parseDouble(item.price.replace("₱", "").replace(",", ""));
                double itemSubtotal = itemPrice * item.quantity;
                subtotal += itemSubtotal;
    
                selectedData[i][0] = new ImageIcon(new ImageIcon(item.iconPath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                selectedData[i][1] = String.format("<html>%s<br>Size: %s</html>", item.name, item.size);
                selectedData[i][2] = item.price;
                selectedData[i][3] = item.quantity;
                selectedData[i][4] = "₱" + String.format("%,.2f", itemSubtotal);
    
                toRemove.add(item);
            }
        }
    
        double remainingBalance = currentBalance - subtotal;
        if (remainingBalance < 0) {
            JOptionPane.showMessageDialog(this, "Not enough balance!", "Insufficient Funds", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String infoMessage = String.format("""
            Current Balance: ₱%,.2f
            Item Subtotal: ₱%,.2f
            After Purchase: ₱%,.2f
    
            Do you want to place this order?
            """, currentBalance, subtotal, remainingBalance);
    
        int choice = JOptionPane.showConfirmDialog(this, infoMessage, "Order Details", JOptionPane.OK_CANCEL_OPTION);
    
        if (choice == JOptionPane.OK_OPTION) {
            currentBalance = remainingBalance;
            if (homePage != null) {
                homePage.updateBalance(remainingBalance);
            }
    
            orderSummaryPage.displayOrderSummary(selectedData, columnNames, customFont);
            ratingsPage.displayOrderSummary(selectedData, columnNames, customFont);
            cartItems.removeAll(toRemove);
            refreshCartItems();
            JOptionPane.showMessageDialog(this, "Order placed!", "Order Placed", JOptionPane.INFORMATION_MESSAGE);
            
            if (homePage != null) {
                homePage.showPanel("OrderSummaryView");
                homePage.menuItemWhite("Order Summary");
            }
        }
    }
}
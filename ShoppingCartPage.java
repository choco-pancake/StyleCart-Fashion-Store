import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class ShoppingCartPage extends JPanel {
    private Font customFont;
    private JLabel titleLabel, emptyLabel;
    private JScrollPane scrollPane;
    private JPanel orderPanel, titlePanel, buttonPanel;
    private JButton addButton, removeButton, confirmButton;
    private Runnable onAddToCartRedirect;
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color black = Color.BLACK;
    private Color borderGray = new Color(169, 169, 169);

    public ShoppingCartPage(Runnable onAddToCartRedirect) {
        setLayout(new BorderLayout());

        this.onAddToCartRedirect = onAddToCartRedirect;

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);
        } catch(FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.ITALIC, 40);
        }

        titlePanel = new JPanel();
        titlePanel.setBackground(darkBlue);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        titleLabel = new JLabel(" Shopping Cart");
        titleLabel.setFont(customFont);
        titleLabel.setForeground(white);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
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

        java.util.List<MainContents.CartItem> cartItems = MainContents.getCartItems();
        if (cartItems.isEmpty()) {
            emptyLabel = new JLabel("Your cart is empty.");
            emptyLabel.setFont(new Font(null, Font.BOLD, 20));
            emptyLabel.setForeground(white);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            orderPanel.add(emptyLabel);
        } else {
            for (MainContents.CartItem item : cartItems) {
                orderPanel.add(createCartItemPanel(item));
                orderPanel.add(Box.createVerticalStrut(10));
            }
        }

        scrollPane = new JScrollPane(orderPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButton = new JButton("Add to Cart");
        addButton.setBackground(lightBlue);
        addButton.setForeground(black);
        addButton.addActionListener(e -> addToCart());

        titlePanel.add(titleLabel);
        buttonPanel.add(addButton);
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createCartItemPanel(MainContents.CartItem item) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setOpaque(false);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        String[] columnNames = {"           Product", "Details", "Price", "Quantity", "Subtotal"};
        Object[][] data = new Object[1][5];

        ImageIcon itemIcon = new ImageIcon(item.iconPath);
        Image scaledImage = itemIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        data[0][0] = new ImageIcon(scaledImage);

        String details = String.format("<html>%s<br>Color: %s<br>Size: %s</html>", item.name, item.color, item.size);
        data[0][1] = details;

        data[0][2] = item.price;
        data[0][3] = String.valueOf(item.quantity);

        String numericPrice = item.price.replace("$", "").replace(",", "");
        double priceValue = Double.parseDouble(numericPrice);
        double subtotal = priceValue * item.quantity;
        data[0][4] = "$" + String.format("%,.2f", subtotal);

        JTable table = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };

        table.setRowHeight(120);
        table.setFont(customFont.deriveFont(15f));
        table.setForeground(black);
        table.setAlignmentX(Component.CENTER_ALIGNMENT);
        table.setFocusable(false);

        table.getTableHeader().setFont(customFont.deriveFont(20f));
        table.getTableHeader().setForeground(darkBlue);
        table.getTableHeader().setOpaque(false);
        table.setShowGrid(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);        
        table.getColumnModel().getColumn(2).setPreferredWidth(100);        
        table.getColumnModel().getColumn(3).setPreferredWidth(80);        
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  

        JScrollPane tablePane = new JScrollPane(table);
        tablePane.setBorder(BorderFactory.createEmptyBorder());
        itemPanel.add(tablePane, BorderLayout.CENTER);

        return itemPanel;
    }

    private void addToCart() {
        if(onAddToCartRedirect != null) {
            onAddToCartRedirect.run();
        }
    }
}
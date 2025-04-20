import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ShoppingCartPage extends JPanel {
    private Font customFont;
    private JLabel titleLabel, emptyLabel;
    private JScrollPane scrollPane;
    private JPanel orderPanel, titlePanel;
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color black = Color.BLACK;
    private Color borderGray = new Color(169, 169, 169);

    public ShoppingCartPage() {
        setLayout(new BorderLayout());

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

        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createCartItemPanel(MainContents.CartItem item) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout());
        itemPanel.setBackground(lightBlue);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderGray, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());
        productPanel.setOpaque(false);

        JLabel productTitle = new JLabel("Product");
        productTitle.setFont(customFont.deriveFont(20f));
        productTitle.setForeground(darkBlue);
        productTitle.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 0));

        ImageIcon itemIcon = new ImageIcon(item.iconPath);
        Image scaledImage = itemIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 0));

        JLabel detailsTitle = new JLabel("Details");
        detailsTitle.setFont(customFont.deriveFont(20f));
        detailsTitle.setForeground(darkBlue);
        detailsTitle.setBorder(BorderFactory.createEmptyBorder(0, 100, 40, 0));

        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(customFont.deriveFont(20f));
        nameLabel.setForeground(black);

        JLabel colorLabel = new JLabel("Color: " + item.color);
        colorLabel.setFont(customFont.deriveFont(15f));
        colorLabel.setForeground(black);

        JLabel sizeLabel = new JLabel("Size: " + item.size);
        sizeLabel.setFont(customFont.deriveFont(15f));
        sizeLabel.setForeground(black);
        sizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BorderLayout());
        pricePanel.setOpaque(false);

        JLabel priceTitle = new JLabel("Price");
        priceTitle.setFont(customFont.deriveFont(20f));
        priceTitle.setForeground(darkBlue);
        priceTitle.setBorder(BorderFactory.createEmptyBorder(0, 70, 80, 0));

        JLabel priceLabel = new JLabel(item.price);
        priceLabel.setFont(customFont.deriveFont(20f));
        priceLabel.setForeground(black);
        priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0));

        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BorderLayout());
        quantityPanel.setOpaque(false);

        JLabel quantityTitle = new JLabel("Qty");
        quantityTitle.setFont(customFont.deriveFont(20f));
        quantityTitle.setForeground(darkBlue);
        quantityTitle.setBorder(BorderFactory.createEmptyBorder(0, 45, 80, 0));

        JLabel quantityLabel = new JLabel(String.valueOf(item.quantity));
        quantityLabel.setFont(customFont.deriveFont(20f));
        quantityLabel.setForeground(black);
        quantityLabel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0));

        String numericPrice = item.price.replace("$", "").replace(",", "");
        double priceValue = Double.parseDouble(numericPrice);
        double subtotal = priceValue * item.quantity;

        JPanel subtotalPanel = new JPanel();
        subtotalPanel.setLayout(new BorderLayout());
        subtotalPanel.setOpaque(false);

        JLabel subtotalTitle = new JLabel("Subtotal");
        subtotalTitle.setFont(customFont.deriveFont(20f));
        subtotalTitle.setForeground(darkBlue);
        subtotalTitle.setBorder(BorderFactory.createEmptyBorder(0, 70, 80, 0));

        JLabel subtotalLabel = new JLabel("$" + String.format("%,.2f", subtotal));
        subtotalLabel.setFont(customFont.deriveFont(20f));
        subtotalLabel.setForeground(black);
        subtotalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        subtotalLabel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 20));

        productPanel.add(productTitle, BorderLayout.NORTH);
        productPanel.add(iconLabel, BorderLayout.CENTER);
        detailsPanel.add(detailsTitle, BorderLayout.NORTH);
        detailsPanel.add(detailsTitle);
        detailsPanel.add(nameLabel);
        detailsPanel.add(colorLabel);
        detailsPanel.add(sizeLabel);
        pricePanel.add(priceTitle, BorderLayout.NORTH);
        pricePanel.add(priceLabel, BorderLayout.CENTER);
        quantityPanel.add(quantityTitle, BorderLayout.NORTH);
        quantityPanel.add(quantityLabel, BorderLayout.CENTER);
        subtotalPanel.add(subtotalTitle, BorderLayout.NORTH);
        subtotalPanel.add(subtotalLabel, BorderLayout.CENTER);
        itemPanel.add(productPanel);
        itemPanel.add(detailsPanel);
        itemPanel.add(pricePanel);
        itemPanel.add(quantityPanel);
        itemPanel.add(subtotalPanel);

        return itemPanel;
    }
}
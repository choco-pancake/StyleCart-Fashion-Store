import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;

public class ViewAll extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel, sectionContainer;
    private MainContents mainContents;
    private Color lightBlue, white, black, lightGray, darkGray, darkBlue, none, peach;
    private Font itemFont, customFont, titleFont, dropdownFont;
    private JPanel topPanel, backPanel, mainPanel, itemsPanel, titleContentPanel,
                   menContainer, menPanel, womenContainer, womenPanel;
    private JButton backButton;
    private JLabel titleLabel, menLabel, womenLabel;
    private JScrollPane scrollPane;
    private JLabel[] stockLabels;
    private JSpinner[] quantitySpinners;

    private String[] items = {
            "Flower Style Valentine's Day Casual Polo Dating Shirt", "Men's Graphic Tee", "Stripped Short Sleeve Polo", "Cut and Sew T-Shirt", "Oversized T-Shirt (Embroidered Logo)",
            "Oversized T-Shirt (Shitake)", "Textured Relaxed Fit Short Sleeve Shirt", "Relaxed Fit T-Shirt with Branding Print (Off White)", "Relaxed Fit Striped Polo", "TATA Fashion New Long Sleeve Plain Varsity Jacket",
            "Plain Hoodie Jacket with Zipper", "Pokemon Men's Pullover", "Ultimate Basics Classic Bomber Jacket (Black)", "Ultimate Basics Easy Fit Long Sleeve Shirt", "Relaxed Fit Long Sleeves Striped Shirt",

            "Better Made Graphic Shirt", "Fits 'Em All Oversized Polo Shirt", "Slim Fit Polo with Contrast Tipping", "Sleeveless Top White", "Striped Knit Top",
            "Small Leather Jacket", "Woven Polo White", "Striped Long Sleeves Button Down Top with Heart Embroidery", "Summer New Style Polo Color Short Sleeved Dress", "A-Line Skirt with Pockets",
            "Striped Denim Shorts (Beige)", "Wide Leg Pants Fashion High Waist Solid Button", "Denim Carpenter Pants", "Wide Leg Jeans", "Pleated Trousers for Women"
    };


    private String[] prices = {
            "₱144", "₱349", "₱899", "₱890", "₱700", "₱990", "₱699", "₱331", "₱799", "₱323",
            "₱145", "₱448", "₱939", "₱989", "₱799",
            
            "₱321", "₱877", "₱349", "₱890", "₱749", "₱398", "₱990", "₱849", "₱900", "₱749",
            "₱799", "₱458", "₱899", "₱880", "₱963"
    };


    private String[] tags = {
        "Versatile", "High quality", "Recycled", "Trendy", "Heavyweight", "Comfortable", "Relaxing", "High density", "Best Seller", "Trendy",
        "High quality", "Outdoor", "Versatile", "Versatile", "Relaxing",
            
        "Recycled", "High quality", "Premium", "New Arrival", "Classic", "High quality", "High quality", "Relaxing", "New Arrival",  "Vintage",
        "Versatile", "High quality", "Relaxing", "High quality", "Comfortable"
    };

    private String[] icons = {
        "images/bnymen8tops.png", "images/benchmen10tops.png", "images/benchmen26tops.png", "images/leemen3tops.png", "images/leemen19tops.png",
        "images/leemen20tops.png", "images/memomen22tops.png", "images/penshoppemen17tops.png", "images/penshoppemen25tops.png", "images/bnymen22outerwear.png",
        "images/bnymen1outerwear.png", "images/benchmen29outerwear.png", "images/memomen24outerwear.png", "images/memomen26tops.png", "images/penshoppemen21tops.png",

        "images/benchwomen2tops.png", "images/leewomen11tops.png", "images/penshoppewomen22tops.png", "images/benchwomen5tops.png", "images/memowomen20tops.png",
        "images/bnywomen5outerwear.png", "images/leewomen27tops.png", "images/penshoppewomen26tops.png", "images/bnywomen14dresses.png", "images/memowomen1bottoms.png",
        "images/penshoppewomen24bottoms.png", "images/bnywomen30bottoms.png", "images/benchwomen19bottoms.png", "images/leewomen1bottoms.png", "images/memowomen11bottoms.png"
    };


    int[][] brandSizes = {
        {100, 100}, {100, 100}, {100, 100}, {60, 100}, {60, 100},
        {60, 100}, {70, 100}, {100, 100}, {100, 100}, {100, 100},
        {100, 100}, {90, 100}, {80, 100}, {80, 100}, {80, 100},
            
        {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
        {100, 100}, {60, 100}, {100, 100}, {100, 100}, {60, 100},
        {100, 100}, {60, 100}, {100, 100}, {60, 100}, {60, 100}
    };

    public ViewAll(CardLayout cardLayout, JPanel contentPanel, Color lightBlue, Color white, Color darkBlue, Color peach,
                   Color black, Color lightGray, Color darkGray, Color none, Font itemFont, Font dropdownFont, MainContents mainContents) {
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        this.mainContents = mainContents;
        this.lightBlue = lightBlue;
        this.white = white;
        this.black = black;
        this.lightGray = lightGray;
        this.darkGray = darkGray;
        this.darkBlue = darkBlue;
        this.none = none;
        this.peach = peach;
        this.itemFont = itemFont;
        this.dropdownFont = dropdownFont;

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(27f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);
            titleFont = customFont.deriveFont(35f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.ITALIC, 27);
            titleFont = customFont.deriveFont(35f);
        }

        stockLabels = new JLabel[items.length];
        quantitySpinners = new JSpinner[items.length];

        titlePanel();
        mainPanel();

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setLayout(new BorderLayout());
        setOpaque(false);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    public void updateStock(String itemName, int newStock) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(itemName)) {
                if (stockLabels[i] != null) {
                    stockLabels[i].setText("Stocks: " + newStock);
                }
                if (quantitySpinners[i] != null) {
                    SpinnerNumberModel model = (SpinnerNumberModel) quantitySpinners[i].getModel();
                    model.setMaximum(newStock);
                }
                break;
            }
        }
    }

    private void titlePanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        topPanel.setBackground(darkBlue);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray));

        backButton = createBackButton();
        backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setOpaque(false);
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        backPanel.add(backButton);

        titleLabel = new JLabel("Featured Items");
        titleLabel.setFont(titleFont.deriveFont(40f));
        titleLabel.setForeground(white);

        titleContentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleContentPanel.setOpaque(false);
        titleContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        titleContentPanel.add(titleLabel);
        topPanel.add(backPanel);
        topPanel.add(titleContentPanel);
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("←") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D graphics2d = (Graphics2D) g.create();
                graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2d.setColor(none);
                graphics2d.fillRect(0, 0, getWidth(), getHeight());

                int size = Math.min(getWidth(), getHeight()) - 4;

                if (getModel().isPressed()) {
                    graphics2d.setColor(darkGray.darker());
                } else if (getModel().isRollover()) {
                    graphics2d.setColor(darkGray);
                } else {
                    graphics2d.setColor(lightGray);
                }

                graphics2d.fill(new Ellipse2D.Double(2, 2, size, size));
                graphics2d.setColor(getModel().isPressed() || getModel().isRollover() ? white : black);
                graphics2d.setFont(getFont());

                FontMetrics metrics = graphics2d.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(getText())) / 2;
                int y = (getHeight() + metrics.getAscent() - metrics.getDescent()) / 2;
                graphics2d.drawString(getText(), x, y);
                graphics2d.dispose();
            }

            @Override
            public boolean contains(int x, int y) {
                int size = Math.min(getWidth(), getHeight());
                double radius = size / 2.0;
                double cx = getWidth() / 2.0;
                double cy = getHeight() / 2.0;
                return Point.distance(cx, cy, x, y) <= radius;
            }
        };

        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setFocusable(false);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setFont(customFont.deriveFont(30f));
        backButton.setPreferredSize(new Dimension(40, 40));
        backButton.setToolTipText("Back to main view");
        backButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "MainView");
        });

        return backButton;
    }

    private void mainPanel() {
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, darkBlue, 0, getHeight(), lightBlue);
                graphics2d.setPaint(gradient);
                graphics2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(true);

        itemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        itemsPanel.setOpaque(false);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        menContainer = new JPanel(new BorderLayout());
        menContainer.setOpaque(false);
        menLabel = new JLabel("MEN");
        menLabel.setFont(customFont.deriveFont(25f));
        menLabel.setForeground(white);
        menLabel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 0));
        menLabel.setOpaque(false);

        menPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        menPanel.setOpaque(false);
        menPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        womenContainer = new JPanel(new BorderLayout());
        womenContainer.setOpaque(false);
        womenLabel = new JLabel("WOMEN");
        womenLabel.setFont(customFont.deriveFont(25f));
        womenLabel.setForeground(white);
        womenLabel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 0));
        womenLabel.setOpaque(false);

        womenPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        womenPanel.setOpaque(false);
        womenPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        for (int i = 0; i < items.length; i++) {
            if (i < 15) {
                menPanel.add(createViewAllItem(items[i], prices[i], tags[i], icons[i], i, brandSizes[i]));
            } else {
                womenPanel.add(createViewAllItem(items[i], prices[i], tags[i], icons[i], i, brandSizes[i]));
            }
        }

        sectionContainer = new JPanel();
        sectionContainer.setLayout(new BoxLayout(sectionContainer, BoxLayout.Y_AXIS));
        sectionContainer.setOpaque(false);

        menContainer.add(menLabel, BorderLayout.NORTH);
        menContainer.add(menPanel, BorderLayout.CENTER);
        womenContainer.add(womenLabel, BorderLayout.NORTH);
        womenContainer.add(womenPanel, BorderLayout.CENTER);
        sectionContainer.add(menContainer);
        sectionContainer.add(womenContainer);
        mainPanel.add(sectionContainer, BorderLayout.CENTER);
    }

    private JPanel createViewAllItem(String item, String price, String tag, String icon, int index, int[] brandSize) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(230, 300));
        itemPanel.setBackground(lightBlue);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darkGray, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        itemPanel.setToolTipText(item);

        JPanel itemDetailsPanel = new JPanel();
        itemDetailsPanel.setLayout(new BoxLayout(itemDetailsPanel, BoxLayout.Y_AXIS));
        itemDetailsPanel.setOpaque(false);

        ImageIcon itemIcon;
        try {
            itemIcon = new ImageIcon(icon);
        } catch (Exception e) {
            System.err.println("Error loading item icon " + icon + ": " + e.getMessage());
            itemIcon = new ImageIcon();
        }
        Image scaledImage = itemIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextPane namePane = new JTextPane();
        namePane.setText(item);
        namePane.setOpaque(false);
        namePane.setForeground(black);
        namePane.setFocusable(false);
        namePane.setHighlighter(null);
        namePane.setAlignmentX(Component.CENTER_ALIGNMENT);
        namePane.setFont(itemFont);
        namePane.setPreferredSize(new Dimension(200, 90));
        namePane.setMaximumSize(new Dimension(200, 90));

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        namePane.setParagraphAttributes(center, true);

        StyledDocument doc = namePane.getStyledDocument();
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs, itemFont.getFamily());
        StyleConstants.setFontSize(attrs, itemFont.getSize());
        StyleConstants.setBold(attrs, itemFont.isBold());
        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), attrs, false);

        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BorderLayout());
        pricePanel.setOpaque(false);

        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(itemFont);
        priceLabel.setForeground(black);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        if (!tag.isEmpty()) {
            JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            tagPanel.setBackground(peach);
            tagPanel.setOpaque(true);
            tagPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

            JLabel tagLabel = new JLabel(tag);
            tagLabel.setFont(itemFont.deriveFont(13f));
            tagLabel.setForeground(black);
            tagPanel.add(tagLabel);

            Dimension labelSize = tagLabel.getPreferredSize();
            tagPanel.setPreferredSize(new Dimension(labelSize.width + 10, labelSize.height + 6));

            JPanel tagContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
            tagContainer.setOpaque(false);
            tagContainer.add(tagPanel);

            headerPanel.add(tagContainer, BorderLayout.WEST);
        } else {
            headerPanel.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.WEST);
        }

        int currentStock = mainContents.getStock(item);
        stockLabels[index] = new JLabel("Stocks: " + currentStock);
        stockLabels[index].setFont(itemFont.deriveFont(13f));
        stockLabels[index].setForeground(black);
        stockLabels[index].setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 1));

        JPanel stockContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        stockContainer.setOpaque(false);
        stockContainer.add(stockLabels[index]);

        headerPanel.add(stockContainer, BorderLayout.EAST);
        itemPanel.add(headerPanel, BorderLayout.NORTH);

        JButton addButton = new JButton("🛒");
        addButton.setForeground(black);
        addButton.setBackground(lightGray);
        addButton.setOpaque(true);
        addButton.setBorderPainted(false);
        addButton.setFont(new Font("SansSerif", Font.PLAIN, 15));
        addButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setFocusable(false);
        addButton.setPreferredSize(new Dimension(40, 30));
        addButton.setMaximumSize(new Dimension(40, 30));
        addButton.setToolTipText("Add " + item + " to cart");

        addButton.putClientProperty("itemName", item);
        addButton.putClientProperty("price", price);
        addButton.putClientProperty("iconPath", icon);

        quantitySpinners[index] = new JSpinner();
        addButton.addActionListener(e -> {
            int stock = mainContents.getStock(item);
            if (stock <= 0) {
                JOptionPane.showMessageDialog(null, "Sorry, " + item + " is out of stock!", "Out of Stock", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] sizes = {"XS", "S", "M", "L", "XL", "2XL", "3XL", "4XL"};
            JPanel detailsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            detailsPanel.setPreferredSize(new Dimension(400, 150));
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            detailsPanel.setFocusable(false);

            JLabel sizeLabel = new JLabel("Select Size:");
            sizeLabel.setFont(itemFont);
            sizeLabel.setFocusable(false);

            JComboBox<String> sizeBox = new JComboBox<>(sizes);
            sizeBox.setFont(dropdownFont);
            sizeBox.setFocusable(false);

            JLabel quantityLabel = new JLabel("Quantity:");
            quantityLabel.setFont(itemFont);
            quantityLabel.setFocusable(false);

            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, stock, 1);
            quantitySpinners[index].setModel(spinnerModel);
            quantitySpinners[index].setFont(dropdownFont);
            quantitySpinners[index].setFocusable(false);

            detailsPanel.add(sizeLabel);
            detailsPanel.add(sizeBox);
            detailsPanel.add(quantityLabel);
            detailsPanel.add(quantitySpinners[index]);

            int result = JOptionPane.showConfirmDialog(null, detailsPanel, "Product Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String selectedSize = (String) sizeBox.getSelectedItem();
                int selectedQuantity = (Integer) quantitySpinners[index].getValue();
                if (selectedQuantity > stock) {
                    JOptionPane.showMessageDialog(null, "Requested quantity exceeds available stock for " + item, "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                MainContents.CartItem newItem = new MainContents.CartItem(item, price, selectedSize, icon, selectedQuantity, brandSize[0], brandSize[1]);
                MainContents.getCartItems().add(newItem);
                JOptionPane.showMessageDialog(null, item + " added to cart! \nSize: " + selectedSize + "\nQuantity: " + selectedQuantity);
            }
        });

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(darkGray);
                addButton.setOpaque(true);
                addButton.setBorderPainted(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                addButton.setBackground(darkGray.darker());
                addButton.setOpaque(true);
                addButton.setBorderPainted(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                addButton.setBackground(darkGray);
                addButton.setOpaque(true);
                addButton.setBorderPainted(false);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(lightGray);
                addButton.setOpaque(true);
                addButton.setBorderPainted(false);
            }
        });

        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrapper.setOpaque(false);

        pricePanel.add(priceLabel, BorderLayout.WEST);
        buttonWrapper.add(addButton);
        pricePanel.add(buttonWrapper, BorderLayout.EAST);
        itemDetailsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        itemDetailsPanel.add(iconLabel);
        itemDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        itemDetailsPanel.add(namePane);
        itemDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemDetailsPanel.add(pricePanel);
        itemPanel.add(itemDetailsPanel, BorderLayout.CENTER);

        return itemPanel;
    }
}
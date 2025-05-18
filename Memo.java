import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.HashMap;

public class Memo extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Color lightBlue, white, black, lightGray, darkGray, darkBlue, none;
    private Font itemFont, customFont, titleFont, dropdownFont;
    private JPanel topPanel, backPanel, mainPanel, menPanel, womenPanel, titleContentPanel, sectionContainer, menContainer, womenContainer;
    private JButton backButton;
    private JLabel titleLabel, menLabel, womenLabel;
    private JScrollPane scrollPane;
    private HashMap<String, Integer> currentStock;
    private HashMap<String, JLabel> stockLabels;

    public Memo(CardLayout cardLayout, JPanel contentPanel, Color lightBlue, Color white, Color darkerBlue, Color black, Color lightGray, Color darkGray, 
                 Color none, Font itemFont, Font dropdownFont) {
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        this.lightBlue = lightBlue;
        this.white = white;
        this.black = black;
        this.lightGray = lightGray;
        this.darkGray = darkGray;
        this.itemFont = itemFont;
        this.dropdownFont = dropdownFont;
        this.darkBlue = new Color(75, 75, 150);
        this.none = none;

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

        // Initialize stock and stock labels
        currentStock = new HashMap<>();
        stockLabels = new HashMap<>();
        initializeStock();

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

    private void initializeStock() {
        String[] items = {
            "Basic Crew Neck T-Shirt", "Basic Crew Neck Tee", "Boxy Fit Striped Flat Knit Top (Choco Brown)", "Boxy Knit Oversized with Zip Closure", "Essential Polo for Men",
            "O.O.O Textured Polo Shirt", "O.O.O. Textured T-Shirt", "Pleated Short Sleeve Shirt (Light Blue)", "Pleated Short Sleeve Shirt (Black)", "Regular Fit Polo with Contrast Tipping",
            "Regular Fit Rib Knit Striped Polo", "Regular Fit Textured T-Shirt", "Slim Fit Polo with Textured Collar (White)", "Slim Fit Polo with Textured Collar (Green)", "T-Shirt With Contrast Piping",
            "Textured Knit T-Shirt", "Textured Open Collar Knit Polo Sleeve Shirt", "Textured Open Collar Polo (Midnight Navy)", "Textured Relaxed Fit Short Sleeve Shirt", "Textured Ringer Striped T-Shirt",
            "Ultimate Basics Comfort T-Shirt", "Ultimate Basics Premium Flat Knit Polo", "Wide Stripe Collared Pullover (Taupe)", "Zip-Up Polo Shirt with Tipping", "Color Block Collared Pullover (Black)",
            "Soft Touch Flat Knit Pullover with Contrast Tipping", "Striped Flat Knit Pullover with Collar and Zip Detail (Heather Gray)", "Ultimate Basics Classic Bomber Jacket (Black)", "Ultimate Basics Easy Fit Long Sleeve Shirt", "Wide Stripe Collared Pullover (Taupe)",

            "Collared Shirt for Women", "Extended Sleeve Textured T-Shirt", "New Ultimate Basic T-Shirt", "Ruched Front T-Shirt", "Textured Tee for Women",
            "Textured Horizontal Striped Polo", "Ultimate Basics Knit Tee", "Denim Zip Up Vest", "Linen Top for Women", "Slash Neck Top",
            "Sleeveless Top with Tipping", "Striped Knit Top", "Textured Striped Sleeveless Top", "Sleeveless Top (Cream)", "Zip Up Pullover",
            "Jumpsuit for Women", "Maxi Dress for Women", "Puff Sleeve Jumpsuit", "Textured Collared Dress", "A-Line Skirt with Pockets",
            "Denim Midi Skirt", "Overlap Skort for Women", "Skirt With Front Slit", "Suede Skort for Women", "Pleated Trousers for Women",
            "Pull Up Trousers", "Slim 4-Way Stretch Trousers", "Straight Leg Trousers with Contrast", "Trousers With Button Detail", "Trousers With Elastic Waistband"
        };
        int[] stocks = {
            100, 2400, 3600, 1800, 750,
            9200, 11000, 650, 120, 110,
            720, 27000, 19300, 22100, 8700,
            3100, 980, 5600, 450, 3900,
            8100, 1400, 13000, 2600, 5200,
            5800, 7700, 1800, 3400, 4200,
            9600, 2300, 800, 1000, 2700,
            950, 2900, 4300, 6100, 1700,
            2100, 1300, 4800, 2800, 3700,
            5, 3, 7400, 8500, 8800,
            1100, 1900, 1600, 2900, 4100,
            5100, 6200, 7100, 7900, 46000
        };

        for (int i = 0; i < items.length; i++) {
            currentStock.put(items[i], stocks[i]);
        }
    }

    public int getStock(String itemName) {
        return currentStock.getOrDefault(itemName, 0);
    }

    public void decreaseStock(String itemName, int quantity) {
        int current = currentStock.getOrDefault(itemName, 0);
        int newStock = Math.max(0, current - quantity);
        currentStock.put(itemName, newStock);
        JLabel stockLabel = stockLabels.get(itemName);
        if (stockLabel != null) {
            stockLabel.setText("Stocks: " + newStock);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
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

        titleLabel = new JLabel("Memo");
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

        String[] items = {
            "Basic Crew Neck T-Shirt", "Basic Crew Neck Tee", "Boxy Fit Striped Flat Knit Top (Choco Brown)", "Boxy Knit Oversized with Zip Closure", "Essential Polo for Men",
            "O.O.O Textured Polo Shirt", "O.O.O. Textured T-Shirt", "Pleated Short Sleeve Shirt (Light Blue)", "Pleated Short Sleeve Shirt (Black)", "Regular Fit Polo with Contrast Tipping",
            "Regular Fit Rib Knit Striped Polo", "Regular Fit Textured T-Shirt", "Slim Fit Polo with Textured Collar (White)", "Slim Fit Polo with Textured Collar (Green)", "T-Shirt With Contrast Piping",
            "Textured Knit T-Shirt", "Textured Open Collar Knit Polo Sleeve Shirt", "Textured Open Collar Polo (Midnight Navy)", "Textured Relaxed Fit Short Sleeve Shirt", "Textured Ringer Striped T-Shirt",
            "Ultimate Basics Comfort T-Shirt", "Ultimate Basics Premium Flat Knit Polo", "Wide Stripe Collared Pullover (Taupe)", "Zip-Up Polo Shirt with Tipping", "Color Block Collared Pullover (Black)",
            "Soft Touch Flat Knit Pullover with Contrast Tipping", "Striped Flat Knit Pullover with Collar and Zip Detail (Heather Gray)", "Ultimate Basics Classic Bomber Jacket (Black)", "Ultimate Basics Easy Fit Long Sleeve Shirt", "Wide Stripe Collared Pullover (Taupe)",

            "Collared Shirt for Women", "Extended Sleeve Textured T-Shirt", "New Ultimate Basic T-Shirt", "Ruched Front T-Shirt", "Textured Tee for Women",
            "Textured Horizontal Striped Polo", "Ultimate Basics Knit Tee", "Denim Zip Up Vest", "Linen Top for Women", "Slash Neck Top",
            "Sleeveless Top with Tipping", "Striped Knit Top", "Textured Striped Sleeveless Top", "Sleeveless Top (Cream)", "Zip Up Pullover",
            "Jumpsuit for Women", "Maxi Dress for Women", "Puff Sleeve Jumpsuit", "Textured Collared Dress", "A-Line Skirt with Pockets",
            "Denim Midi Skirt", "Overlap Skort for Women", "Skirt With Front Slit", "Suede Skort for Women", "Pleated Trousers for Women",
            "Pull Up Trousers", "Slim 4-Way Stretch Trousers", "Straight Leg Trousers with Contrast", "Trousers With Button Detail", "Trousers With Elastic Waistband"
        };
       
        String[] prices = {
            "₱399", "₱399", "₱719", "₱989", "₱899",
            "₱799", "₱599", "₱899", "₱899", "₱989",
            "₱849", "₱699", "₱749", "₱749", "₱399",
            "₱499", "₱989", "₱989", "₱699", "₱799",
            "₱499", "₱749", "₱659", "₱899", "₱939",
            "₱989", "₱759", "₱899", "₱989", "₱659",

            "₱899", "₱429", "₱699", "₱710", "₱699",
            "₱989", "₱439", "₱899", "₱809", "₱287",
            "₱890", "₱749", "₱699", "₱890", "₱899",
            "₱699", "₱849", "₱749", "₱659", "₱749",
            "₱679", "₱849", "₱739", "₱659", "₱963",
            "₱899", "₱699", "₱699", "₱759", "₱599"
        };
       
        String[] icons = {
           "images/memomen1tops.png", "images/memomen2tops.png", "images/memomen3tops.png", "images/memomen4tops.png", "images/memomen6tops.png",
            "images/memomen7tops.png", "images/memomen8tops.png", "images/memomen9tops.png", "images/memomen10tops.png", "images/memomen11tops.png",
            "images/memomen12tops.png", "images/memomen13tops.png", "images/memomen14tops.png", "images/memomen15tops.png", "images/memomen18tops.png",
            "images/memomen19tops.png", "images/memomen20tops.png", "images/memomen21tops.png", "images/memomen22tops.png", "images/memomen23tops.png",
            "images/memomen25tops.png", "images/memomen27tops.png", "images/memomen28tops.png", "images/memomen30tops.png", "images/memomen5outerwear.png", 
            "images/memomen16outerwear.png", "images/memomen17outerwear.png", "images/memomen24outerwear.png", "images/memomen26tops.png", "images/memomen29outerwear.png",

            "images/memowomen2tops.png", "images/memowomen5tops.png", "images/memowomen9tops.png", "images/memowomen14tops.png", "images/memowomen24tops.png",
            "images/memowomen25tops.png", "images/memowomen28tops.png", "images/memowomen4tops.png", "images/memowomen7tops.png", "images/memowomen16tops.png",
            "images/memowomen17tops.png", "images/memowomen20tops.png", "images/memowomen23tops.png", "images/memowomen30tops.png", "images/memowomen29outerwear.png",
            "images/memowomen6dresses.png", "images/memowomen8dresses.png", "images/memowomen12dresses.png", "images/memowomen22dresses.png", "images/memowomen1bottoms.png",
            "images/memowomen3bottoms.png", "images/memowomen10bottoms.png", "images/memowomen15bottoms.png", "images/memowomen21bottoms.png", "images/memowomen11bottoms.png",
            "images/memowomen13bottoms.png", "images/memowomen18bottoms.png", "images/memowomen19bottoms.png", "images/memowomen26bottoms.png", "images/memowomen27bottoms.png"
        };

        int[][] brandSize = {
            {90, 100}, {90, 100}, {100, 100}, {90, 100}, {90, 100},
            {80, 100}, {100, 100}, {90, 100}, {90, 100}, {90, 100},
            {90, 100}, {80, 100}, {90, 100}, {80, 100}, {90, 100},
            {80, 100}, {90, 100}, {90, 100}, {80, 100}, {90, 100},
            {90, 100}, {90, 100}, {90, 100}, {80, 100}, {90, 100},
            {100, 100}, {90, 100}, {90, 100}, {80, 100}, {70, 100},

            {90, 100}, {90, 100}, {120, 100}, {100, 100}, {100, 100},
            {90, 100}, {100, 100}, {100, 100}, {80, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {80, 100},
            {40, 100}, {40, 100}, {50, 100}, {50, 100}, {60, 100},
            {60, 100}, {100, 100}, {80, 100}, {80, 100}, {50, 100},
            {60, 100}, {60, 100}, {60, 100}, {60, 100}, {60, 100}
        };
 
        for (int i = 0; i < items.length; i++) {
            JPanel itemPanel = createMemoItem(items[i], prices[i], icons[i], brandSize[i][0], brandSize[i][1], currentStock.get(items[i]));
            if (i < 30) {
                menPanel.add(itemPanel);
            } else {
                womenPanel.add(itemPanel);
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

    private JPanel createMemoItem(String item, String price, String icon, int width, int height, int stock) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(230, 300));
        itemPanel.setBackground(lightBlue);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darkGray, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));  

        JPanel itemDetailsPanel = new JPanel();
        itemDetailsPanel.setLayout(new BoxLayout(itemDetailsPanel, BoxLayout.Y_AXIS));
        itemDetailsPanel.setOpaque(false);

        ImageIcon itemIcon = new ImageIcon(icon);
        Image scaledImage = itemIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
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
        priceLabel.setPreferredSize(new Dimension(100, 20));

        JLabel stockLabel = new JLabel("Stocks: " + stock);
        stockLabel.setFont(itemFont.deriveFont(13f));
        stockLabel.setForeground(black);
        stockLabel.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 1));
        stockLabels.put(item, stockLabel);

        JPanel stockContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        stockContainer.setOpaque(false);
        stockContainer.add(stockLabel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
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

        addButton.addActionListener(e -> {
            if (currentStock.get(item) <= 0) {
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

            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
                (int)1, 
                (int)1, 
                (int)currentStock.get(item), 
                (int)1
            );

            JSpinner quantitySpinner = new JSpinner(spinnerModel);
            quantitySpinner.setFont(dropdownFont);
            quantitySpinner.setFocusable(false);

            detailsPanel.add(sizeLabel);
            detailsPanel.add(sizeBox);
            detailsPanel.add(quantityLabel);
            detailsPanel.add(quantitySpinner);

            int result = JOptionPane.showConfirmDialog(null, detailsPanel, "Product Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String selectedSize = (String) sizeBox.getSelectedItem();
                int selectedQuantity = (Integer) quantitySpinner.getValue();
                if (selectedQuantity > currentStock.get(item)) {
                    JOptionPane.showMessageDialog(null, "Requested quantity exceeds available stock for " + item, "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                MainContents.CartItem newItem = new MainContents.CartItem(item, price, selectedSize, icon, selectedQuantity, width, height);
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
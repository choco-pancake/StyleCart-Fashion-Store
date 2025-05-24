import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.HashMap;

public class Bench extends JPanel {
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

    public Bench(CardLayout cardLayout, JPanel contentPanel, Color lightBlue, Color white, Color darkerBlue, Color black, Color lightGray, Color darkGray, 
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
            "Bench Everyday’s Pullover Top", "Bench x Baekhyun Logo Tee", "Men's Statement Tee", "Men's Boxy Tee", "Men's Classic Tee",
            "Men's Embriodered Tee", "Flat Knit Polo Shirt", "Men's Graphic Tee", "Heritage Oversized T-Shirt", "Heritage Polo Shirt",
            "Men's Logo Tee", "Men's Polo Shirt", "Men's Round Neck Tee", "Round Neck Tee", "Men's Short Printed Shirt",
            "Short Sleeved Polo", "Striped Polo Shirt", "Stripped Short Sleeve Polo", "Men's Printed Muscle Tee", "Men's Ribbed Racer Back T-Shirt",
            "Sleeveless Pullover Top", "Baekhyun Pullover Top", "Everyday Men’s Pullover Top", "Men's Long Sleeved Shirt", "Pokemon Men's Pullover",
            "Men's Jogging Shorts", "Men's Walking Shorts", "Men's Jogging Pants", "Men's Twill Pants", "SB19 Men's Twill Pants",
            "Better Made Graphic Shirt", "Pokemon Graphic Tee", "Cropped Plaid Resort Polo", "Oversized Graphic Tee", "Peasant Crop Top",
            "Puff Sleeve Top", "Boxy Resort Shirt", "Collared Top", "Plain Oversized Tee", "Cool Touch Crew Neck Short-sleeved T-Shirt",
            "Women's Crop Top", "Cropped Logo Tee", "Denim Boxy Cropped Shirt", "Women's Graphic T-Shirt", "Women’s K-Heart T-Shirt",
            "Oversized Polo Shirt", "Women's Plain Tee", "Women's Polo Shirt", "Women's Statement Tee", "Stripped Short Sleeve Polo",
            "Women's Woven Top", "Sleeveless Top White", "Scoop Neck Tank Top", "Women's Sleeveless Top", "Women's Hoodie Jacket",
            "Women's Shift Dress", "Denim Pants", "Everyday Baggy Pants", "Baggy Pants", "Denim Carpenter Pants"
        };
        int[] stocks = {
            1500, 1200, 800, 900, 1000,
            700, 600, 850, 950, 1100,
            1300, 750, 880, 920, 670,
            780, 890, 910, 560, 450,
            680, 720, 830, 940, 999,
            770, 860, 990, 1150, 1250,
            1400, 1100, 650, 870, 930,
            760, 820, 960, 1080, 1120,
            790, 840, 900, 970, 1030,
            880, 950, 1000, 1070, 660,
            730, 800, 850, 920, 980,
            1040, 1090, 1140, 1190, 1240
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

        titleLabel = new JLabel("Bench");
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
            "Bench Everyday’s Pullover Top", "Bench x Baekhyun Logo Tee", "Men's Statement Tee", "Men's Boxy Tee", "Men's Classic Tee",
            "Men's Embriodered Tee", "Flat Knit Polo Shirt", "Men's Graphic Tee", "Heritage Oversized T-Shirt", "Heritage Polo Shirt",
            "Men's Logo Tee", "Men's Polo Shirt", "Men's Round Neck Tee", "Round Neck Tee", "Men's Short Printed Shirt",
            "Short Sleeved Polo", "Striped Polo Shirt", "Stripped Short Sleeve Polo", "Men's Printed Muscle Tee", "Men's Ribbed Racer Back T-Shirt",
            "Sleeveless Pullover Top", "Baekhyun Pullover Top", "Everyday Men’s Pullover Top", "Men's Long Sleeved Shirt", "Pokemon Men's Pullover",
            "Men's Jogging Shorts", "Men's Walking Shorts", "Men's Jogging Pants", "Men's Twill Pants", "SB19 Men's Twill Pants",
            "Better Made Graphic Shirt", "Pokemon Graphic Tee", "Cropped Plaid Resort Polo", "Oversized Graphic Tee", "Peasant Crop Top",
            "Puff Sleeve Top", "Boxy Resort Shirt", "Collared Top", "Plain Oversized Tee", "Cool Touch Crew Neck Short-sleeved T-Shirt",
            "Women's Crop Top", "Cropped Logo Tee", "Denim Boxy Cropped Shirt", "Women's Graphic T-Shirt", "Women’s K-Heart T-Shirt",
            "Oversized Polo Shirt", "Women's Plain Tee", "Women's Polo Shirt", "Women's Statement Tee", "Stripped Short Sleeve Polo",
            "Women's Woven Top", "Sleeveless Top White", "Scoop Neck Tank Top", "Women's Sleeveless Top", "Women's Hoodie Jacket",
            "Women's Shift Dress", "Denim Pants", "Everyday Baggy Pants", "Baggy Pants", "Denim Carpenter Pants"
        };
       
        String[] prices = {
            "₱599", "₱377", "₱349", "₱349", "₱349",
            "₱377", "₱699", "₱349", "₱384", "₱447",
            "₱349", "₱499", "₱447", "₱419", "₱899",
            "₱899", "₱499", "₱899", "₱279", "₱167",
            "₱599", "₱599", "₱599", "₱499", "₱448",
            "₱499", "₱699", "₱499", "₱899", "₱899",
            "₱321", "₱349", "₱699", "₱349", "₱699",
            "₱999", "₱899", "₱419", "₱230", "₱230",
            "₱279", "₱265", "₱899", "₱349", "₱349",
            "₱321", "₱244", "₱349", "₱307", "₱699",
            "₱799", "₱160", "₱104", "₱160", "₱539",
            "₱699", "₱699", "₱799", "₱699", "₱899"
        };
       
        String[] icons = {
           "images/benchmen2tops.png", "images/benchmen3tops.png", "images/benchmen4tops.png", "images/benchmen5tops.png", "images/benchmen6tops.png",
           "images/benchmen7tops.png", "images/benchmen9tops.png", "images/benchmen10tops.png", "images/benchmen11tops.png", "images/benchmen12tops.png", 
           "images/benchmen15tops.png", "images/benchmen17tops.png", "images/benchmen20tops.png", "images/benchmen21tops.png", "images/benchmen22tops.png",
           "images/benchmen23tops.png", "images/benchmen25tops.png", "images/benchmen26tops.png", "images/benchmen18tops.png", "images/benchmen19tops.png",
           "images/benchmen24outerwear.png", "images/benchmen1tops.png", "images/benchmen8outerwear.png", "images/benchmen16outerwear.png", "images/benchmen29outerwear.png",
           "images/benchmen14activewear.png", "images/benchmen28bottoms.png", "images/benchmen13activewear.png", "images/benchmen27bottoms.png", "images/benchmen30bottoms.png",
           "images/benchwomen2tops.png", "images/benchwomen4tops.png", "images/benchwomen6tops.png", "images/benchwomen7tops.png", "images/benchwomen8tops.png",
           "images/benchwomen9tops.png", "images/benchwomen12tops.png", "images/benchwomen13tops.png", "images/benchwomen14tops.png", "images/benchwomen15tops.png",
           "images/benchwomen16tops.png", "images/benchwomen17tops.png", "images/benchwomen18tops.png", "images/benchwomen20tops.png", "images/benchwomen21tops.png",
           "images/benchwomen23tops.png", "images/benchwomen24tops.png", "images/benchwomen25tops.png", "images/benchwomen28tops.png", "images/benchwomen29tops.png",
           "images/benchwomen30tops.png", "images/benchwomen5tops.png", "images/benchwomen10tops.png", "images/benchwomen27tops.png", "images/benchwomen22outerwear.png",
           "images/benchwomen26dresses.png", "images/benchwomen1bottoms.png", "images/benchwomen3bottoms.png", "images/benchwomen11bottoms.png", "images/benchwomen19bottoms.png"
        };

        int[][] brandSize = {
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {110, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {110, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {80, 100}, {100, 100}, {100, 100},
            {100, 100}, {80, 100}, {80, 100}, {80, 100}, {80, 100}
        };
 
        for (int i = 0; i < items.length; i++) {
            JPanel itemPanel = createBenchItem(items[i], prices[i], icons[i], brandSize[i][0], brandSize[i][1], currentStock.get(items[i]));
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

    private JPanel createBenchItem(String item, String price, String icon, int width, int height, int stock) {
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
        addButton.setBackground(stock <= 0 ? darkGray : lightGray);
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

            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, stock, 1);
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
                int currentCartQuantity = 0;
                for (MainContents.CartItem cartItem : MainContents.getCartItems()) {
                    if (cartItem.getName().equals(item)) {
                        currentCartQuantity += cartItem.getQuantity();
                    }
                }
                if (currentCartQuantity + selectedQuantity > currentStock.get(item)) {
                    JOptionPane.showMessageDialog(null, "Insufficient stocks for cart addition.", "Stock Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                MainContents.CartItem newItem = new MainContents.CartItem(item, price, selectedSize, icon, selectedQuantity, width, height);
                MainContents.getCartItems().add(newItem);
                JOptionPane.showMessageDialog(null, item + " added to cart!\nSize: " + selectedSize + "\nQuantity: " + selectedQuantity);
            }
        });

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (stock > 0) {
                    addButton.setBackground(darkGray);
                    addButton.setOpaque(true);
                    addButton.setBorderPainted(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (stock > 0) {
                    addButton.setBackground(darkGray.darker());
                    addButton.setOpaque(true);
                    addButton.setBorderPainted(false);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (stock > 0) {
                    addButton.setBackground(darkGray);
                    addButton.setOpaque(true);
                    addButton.setBorderPainted(false);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(stock <= 0 ? darkGray : lightGray);
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
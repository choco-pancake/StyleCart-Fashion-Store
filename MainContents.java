import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.*;

public class MainContents extends JPanel {
    private Font titleFont, itemFont, dropdownFont;
    private JPanel mainPanel, topPanel, categoryPanel, featuredPanel, brandPanel;
    private JPanel categoryTitle, featuredTitle, brandTitle, featuredItemsPanel;
    private JLabel greetLabel, pfpLabel, categoryLabel, featuredLabel, featuredView, brandLabel;
    private ImageIcon icon;
    private Image scaledImage;
    private JScrollPane scrollPane;
    private int[] itemsStock;
    private String[] items; // Added to track item names for stock updates
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color gray = Color.GRAY;
    private Color darkerBlue = new Color(0, 0, 139);
    private Color hoverBlue = new Color(135, 206, 250);
    private Color selectBlue = new Color(100, 149, 237);
    private Color peach = new Color(255, 218, 185);
    private Color black = Color.BLACK;
    private Color lightGray = new Color(211, 211, 211);
    private Color darkGray = new Color(169, 169, 169);
    private Color none = new Color(0, 0, 0, 0);
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private static List<CartItem> cartItems = new ArrayList<>();
    private Runnable onBrandViewClick;
    private Runnable onViewCartClick;

    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static class CartItem {
        String name;
        String price;
        String size;
        String iconPath;
        int quantity;
        int iconWidth;
        int iconHeight;

        public CartItem(String name, String price, String size, String iconPath, int quantity, int iconWidth, int iconHeight) {
            this.name = name;
            this.price = price;
            this.size = size;
            this.iconPath = iconPath;
            this.quantity = quantity;
            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
        }
    }

    public void setOnBrandViewClick(Runnable onClick) {
        this.onBrandViewClick = onClick;
    }

    public void setOnViewCartClick(Runnable onClick) {
        this.onViewCartClick = onClick;
    }

    public void decreaseStock(String itemName, int quantity) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(itemName)) {
                itemsStock[i] -= quantity;
                Component[] components = featuredItemsPanel.getComponents();
                if (i < components.length) {
                    JPanel itemPanel = (JPanel) components[i];
                    JLabel stockLabel = findStockLabel(itemPanel);
                    if (stockLabel != null) {
                        stockLabel.setText("Stocks: " + itemsStock[i]);
                    }
                    JButton addButton = findAddButton(itemPanel);
                    if (addButton != null) {
                        updateSpinnerMaximum(addButton, itemsStock[i]);
                    }
                }
                break;
            }
        }
    }

    private JLabel findStockLabel(JPanel itemPanel) {
        for (Component comp : itemPanel.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JPanel) {
                        for (Component subSubComp : ((JPanel) subComp).getComponents()) {
                            if (subSubComp instanceof JLabel && ((JLabel) subSubComp).getText().startsWith("Stocks: ")) {
                                return (JLabel) subSubComp;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private JButton findAddButton(JPanel itemPanel) {
        for (Component comp : itemPanel.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JPanel) {
                        for (Component subSubComp : ((JPanel) subComp).getComponents()) {
                            if (subSubComp instanceof JButton && ((JButton) subSubComp).getText().equals("🛒")) {
                                return (JButton) subSubComp;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void updateSpinnerMaximum(JButton addButton, int maxStock) {
        addButton.addActionListener(e -> {
            String[] sizes = {"XS", "S", "M", "L", "XL", "2XL", "3XL", "4XL"};
            JPanel detailsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            detailsPanel.setPreferredSize(new Dimension(400, 200));
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

            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, maxStock, 1);
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
                String itemName = addButton.getClientProperty("itemName").toString();
                String price = addButton.getClientProperty("price").toString();
                String iconPath = addButton.getClientProperty("iconPath").toString();
                cartItems.add(new CartItem(itemName, price, selectedSize, iconPath, selectedQuantity, 100, 100));
                JOptionPane.showMessageDialog(null, itemName + " added to cart! \nSize: " + selectedSize + "\nQuantity: " + selectedQuantity);
            }
        });
    }

    public MainContents() {
        try {
            titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(titleFont);
            itemFont = titleFont.deriveFont(16f);
            dropdownFont = itemFont.deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            titleFont = new Font("Serif", Font.ITALIC, 40);
            itemFont = titleFont.deriveFont(16f);
            dropdownFont = itemFont.deriveFont(16f);
        }

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

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(scrollPane, "MainView");

        BNY bnyPanel = new BNY(cardLayout, contentPanel, lightBlue, white, darkBlue, black, lightGray, darkGray, none, itemFont, dropdownFont);
        contentPanel.add(bnyPanel, "BNYView");

        Bench benchPanel = new Bench(cardLayout, contentPanel, lightBlue, white, darkBlue, black, lightGray, darkGray, none, itemFont, dropdownFont);
        contentPanel.add(benchPanel, "BenchView");

        Lee leePanel = new Lee(cardLayout, contentPanel, lightBlue, white, darkBlue, black, lightGray, darkGray, none, itemFont, dropdownFont);
        contentPanel.add(leePanel, "LeeView");

        createGreeting();
        createCategory();
        createFeaturedItems();
        createAvailableBrands();

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);

        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }

    class HintTextField extends JTextField {
        private String hint;

        public HintTextField(String hint) {
            this.hint = hint;
            setForeground(white);
            setText(hint);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D graphics2d = (Graphics2D) g.create();
                graphics2d.setColor(gray);
                graphics2d.setFont(getFont().deriveFont(Font.ITALIC));
                Insets insets = getInsets();
                graphics2d.drawString(hint, insets.left + 5, getHeight() / 2 + graphics2d.getFontMetrics().getAscent() / 2 - 4);
                graphics2d.dispose();
            }
        }
    }

    private void createGreeting() {
        topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        greetLabel = new JLabel(" Welcome to StyleCart!");
        greetLabel.setFont(titleFont);
        greetLabel.setForeground(white);

        icon = new ImageIcon("images/pfp.png");
        scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        pfpLabel = new JLabel(new ImageIcon(scaledImage));
        pfpLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pfpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel profilePanel = new JPanel();
                profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
                profilePanel.setOpaque(false);

                ImageIcon profileIcon = new ImageIcon("images/pfp.png");
                Image fullProfileIcon = profileIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                JLabel imageLabel = new JLabel(new ImageIcon(fullProfileIcon));
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel nameLabel = new JLabel("anonymous_shopper101");
                nameLabel.setFont(titleFont.deriveFont(25f));
                nameLabel.setForeground(black);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel emailLabel = new JLabel("@anonymousshopper@gmail.com");
                emailLabel.setFont(itemFont);
                emailLabel.setForeground(gray);
                emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JPanel phonePanel = new JPanel();
                phonePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                phonePanel.setOpaque(false);

                ImageIcon phoneIcon = new ImageIcon("images/phone.png");
                Image fullPhoneIcon = phoneIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel imageLabel2 = new JLabel(new ImageIcon(fullPhoneIcon));
                JLabel phoneLabel = new JLabel("+63 9 135-7924");
                phoneLabel.setFont(itemFont.deriveFont(15f));

                JPanel homePanel = new JPanel();
                homePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                homePanel.setOpaque(false);

                ImageIcon homeIcon = new ImageIcon("images/address.png");
                Image fullHomeIcon = homeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel imageLabel3 = new JLabel(new ImageIcon(fullHomeIcon));
                JLabel homeLabel = new JLabel("Valenzuela City, Metro Manila, Philippines");
                homeLabel.setFont(itemFont.deriveFont(15f));

                profilePanel.add(imageLabel);
                profilePanel.add(Box.createVerticalStrut(8));
                profilePanel.add(nameLabel);
                profilePanel.add(Box.createVerticalStrut(8));
                profilePanel.add(emailLabel);
                profilePanel.add(Box.createVerticalStrut(8));
                phonePanel.add(imageLabel2);
                phonePanel.add(phoneLabel);
                profilePanel.add(phonePanel);
                profilePanel.add(Box.createVerticalStrut(8));
                homePanel.add(imageLabel3);
                homePanel.add(homeLabel);
                profilePanel.add(homePanel);
                profilePanel.add(Box.createVerticalStrut(8));

                JOptionPane.showMessageDialog(null, profilePanel, "Profile", JOptionPane.PLAIN_MESSAGE);
            }
        });

        topPanel.add(greetLabel, BorderLayout.WEST);
        topPanel.add(pfpLabel, BorderLayout.EAST);
        mainPanel.add(topPanel);
    }

    private void createCategory() {
        categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setOpaque(false);
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 0, 20));
        categoryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        categoryTitle = new JPanel(new BorderLayout());
        categoryTitle.setOpaque(false);

        categoryLabel = new JLabel("  CLOTHING CATEGORY");
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        categoryLabel.setForeground(white);
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 10));

        String[] category = {"Active Wear", "        Tops", "     Bottoms", "  Dresses", "  Outerwear"};
        String[] categoryIcon = {"images/activewear.png", "images/tops.png", "images/bottoms.png", "images/dresses.png", "images/outerwear.png"};

        JPanel categoryItemsPanel = new JPanel();
        categoryItemsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        categoryItemsPanel.setOpaque(false);

        for (int i = 0; i < category.length; i++) {
            categoryItemsPanel.add(createCategoryItem(category[i], categoryIcon[i]));
        }

        categoryTitle.add(categoryLabel, BorderLayout.WEST);
        categoryPanel.add(categoryTitle, BorderLayout.NORTH);
        categoryPanel.add(categoryItemsPanel, BorderLayout.CENTER);
        mainPanel.add(categoryPanel);
    }

    private JPanel createCategoryItem(String itemName, String itemIcon) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setBackground(lightBlue);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darkGray, 1),
                BorderFactory.createEmptyBorder(10, 10, 0, 10)));
        itemPanel.setPreferredSize(new Dimension(230, 130));

        ImageIcon icon = new ImageIcon(itemIcon);
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

        JLabel nameLabel = new JLabel(itemName);
        nameLabel.setFont(itemFont);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        itemPanel.add(iconLabel, BorderLayout.WEST);
        itemPanel.add(nameLabel, BorderLayout.CENTER);
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(hoverBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                itemPanel.setBackground(selectBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                itemPanel.setBackground(lightBlue);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (itemName.trim().equals("Active Wear")) {
                    JPanel activeWearPanel = new ActiveWearPanel(cardLayout, contentPanel, lightBlue, white, darkBlue, peach, black, lightGray, darkGray, none, itemFont, dropdownFont);
                    contentPanel.add(activeWearPanel, "ActiveWearView");
                    cardLayout.show(contentPanel, "ActiveWearView");
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
            }
        });

        return itemPanel;
    }

    private void createFeaturedItems() {
        featuredPanel = new JPanel(new BorderLayout());
        featuredPanel.setOpaque(false);
        featuredPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
        featuredPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        featuredTitle = new JPanel(new BorderLayout());
        featuredTitle.setOpaque(false);

        featuredLabel = new JLabel("  FEATURED ITEMS");
        featuredLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        featuredLabel.setForeground(white);
        featuredLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 10));

        featuredView = new JLabel("View All >");
        featuredView.setFont(new Font("SansSerif", Font.PLAIN, 17));
        featuredView.setForeground(white);

        featuredView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                featuredView.setForeground(darkerBlue);
                featuredView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                featuredView.setForeground(white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                featuredView.setForeground(darkerBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                featuredView.setForeground(darkerBlue);
            }
        });

        featuredItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        featuredItemsPanel.setOpaque(false);

        items = new String[]{"TATA Fashion New Long Sleeve Plain Varsity Jacket", "Wide Leg Pants Fashion High Waist Solid Button", "Flower Style Valentine's Day Casual Polo Dating Shirt", "Small Leather Jacket", "Plain Hoodie Jacket with Zipper"};
        String[] itemsPrice = {"₱40.00", "₱50.00", "₱39.00", "₱165.00", "₱135.00"};
        String[] itemsTag = {"Trendy", "High quality", "Versatile", "High quality", "High quality"};
        String[] itemsIcon = {"images/bnymen22outerwear.png", "images/bnywomen30bottoms.png", "images/bnymen8tops.png", "images/bnywomen5outerwear.png", "images/bnymen1outerwear.png"};
        itemsStock = new int[]{21364, 45600, 5760, 2, 91};

        for (int i = 0; i < items.length; i++) {
            featuredItemsPanel.add(createFeaturedItem(items[i], itemsPrice[i], itemsTag[i], itemsIcon[i], itemsStock[i], i));
        }

        featuredTitle.add(featuredLabel, BorderLayout.WEST);
        featuredTitle.add(featuredView, BorderLayout.EAST);
        featuredPanel.add(featuredTitle, BorderLayout.NORTH);
        featuredPanel.add(featuredItemsPanel, BorderLayout.CENTER);
        mainPanel.add(featuredPanel);
    }

    private JPanel createFeaturedItem(String item, String price, String tag, String icon, int stock, int index) {
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

        JLabel stockLabel = new JLabel("Stocks: " + stock);
        stockLabel.setFont(itemFont.deriveFont(13f));
        stockLabel.setForeground(black);
        stockLabel.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 1));

        JPanel stockContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        stockContainer.setOpaque(false);
        stockContainer.add(stockLabel);

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

        addButton.putClientProperty("itemName", item);
        addButton.putClientProperty("price", price);
        addButton.putClientProperty("iconPath", icon);

        addButton.addActionListener(e -> {
            String[] sizes = {"XS", "S", "M", "L", "XL", "2XL", "3XL", "4XL"};

            JPanel detailsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            detailsPanel.setPreferredSize(new Dimension(400, 200));
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

            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, itemsStock[index], 1);
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
                cartItems.add(new CartItem(item, price, selectedSize, icon, selectedQuantity, 100, 100));
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

    private void createAvailableBrands() {
        brandPanel = new JPanel(new BorderLayout());
        brandPanel.setOpaque(false);
        brandPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 10));
        brandPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        brandTitle = new JPanel(new BorderLayout());
        brandTitle.setOpaque(false);

        brandLabel = new JLabel("  AVAILABLE BRANDS");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        brandLabel.setForeground(white);
        brandLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[] brand = {"BNY", "Bench", "Lee", "Memo", "Mossimo", "Penshoppe"};
        String[] brandIcon = {"images/bny.png", "images/bench.png", "images/lee.png", "images/memo.jpg", "images/mossimo.png", "images/penshoppe.png"};
        int[][] brandSize = {{140, 70}, {150, 40}, {105, 90}, {110, 100}, {170, 80}, {160, 30}};

        JPanel brandItemsPanel = new JPanel();
        brandItemsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        brandItemsPanel.setOpaque(false);

        for (int i = 0; i < brand.length; i++) {
            brandItemsPanel.add(createBrandItem(brand[i], brandIcon[i], brandSize[i][0], brandSize[i][1]));
        }

        brandTitle.add(brandLabel, BorderLayout.WEST);
        brandPanel.add(brandTitle, BorderLayout.NORTH);
        brandPanel.add(brandItemsPanel, BorderLayout.CENTER);
        mainPanel.add(brandPanel);
    }

    private JPanel createBrandItem(String brandName, String brandIcon, int width, int height) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setBackground(lightBlue);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darkGray, 1),
                BorderFactory.createEmptyBorder(10, 10, 5, 10)));
        itemPanel.setPreferredSize(new Dimension(188, 150));

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new GridBagLayout());
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(200, 110));
        logoPanel.setMaximumSize(new Dimension(200, 110));

        ImageIcon icon = new ImageIcon(brandIcon);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        logoPanel.add(iconLabel, gbc);

        JLabel nameLabel = new JLabel(brandName);
        nameLabel.setFont(itemFont);
        nameLabel.setForeground(black);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        itemPanel.add(logoPanel, BorderLayout.CENTER);
        itemPanel.add(nameLabel, BorderLayout.SOUTH);

        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(hoverBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                itemPanel.setBackground(selectBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                itemPanel.setBackground(lightBlue);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (brandName.equals("BNY")) {
                    cardLayout.show(contentPanel, "BNYView");
                    contentPanel.revalidate();
                    contentPanel.repaint();
                } else if (brandName.equals("Bench")) {
                    cardLayout.show(contentPanel, "BenchView");
                    contentPanel.revalidate();
                    contentPanel.repaint();
                } else if(brandName.equals("Lee")) {
                    cardLayout.show(contentPanel, "LeeView");
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
            }
        });

        return itemPanel;
    }
}
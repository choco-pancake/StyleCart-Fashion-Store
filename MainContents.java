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
    private JLabel greetLabel, pfpLabel, categoryLabel, featuredLabel, brandLabel;
    private ImageIcon icon;
    private Image scaledImage;
    private JScrollPane scrollPane;
    private JLabel[] stockLabels;
    private JSpinner[] quantitySpinners;
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color gray = Color.GRAY;
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
    private BNY bnyPanel;
    private Bench benchPanel;
    private Lee leePanel;
    private Memo memoPanel;
    private Penshoppe penshoppePanel;

    private String[] items = {
        "TATA Fashion New Long Sleeve Plain Varsity Jacket", "Wide Leg Pants Fashion High Waist Solid Button", "Flower Style Valentine's Day Casual Polo Dating Shirt", "Small Leather Jacket", "Plain Hoodie Jacket with Zipper"
    };

    private int[] itemsStock = {21364, 45600, 5760, 2, 91};
    private String[] itemsPrice = {"₱323", "₱458", "₱259", "₱398", "₱145"};
    private String[] itemsTag = {"Trendy", "High quality", "Versatile", "High quality", "High quality"};

    private String[] itemsIcon = {
        "images/bnymen22outerwear.png", "images/bnywomen30bottoms.png", "images/bnymen8tops.png", "images/bnywomen5outerwear.png", "images/bnymen1outerwear.png"
    };

    private int[][] brandSizes = {
        {100, 100}, {100, 100}, {100, 100}, {100, 100}, {110, 100}
    };

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

        public String getName() { return name; }
        public String getPrice() { return price; }
        public String getSize() { return size; }
        public String getIconPath() { return iconPath; }
        public int getQuantity() { return quantity; }
        public int getIconWidth() { return iconWidth; }
        public int getIconHeight() { return iconHeight; }
    }

    public void setOnBrandViewClick(Runnable onClick) {
        this.onBrandViewClick = onClick;
    }

    public void setOnViewCartClick(Runnable onClick) {
        this.onViewCartClick = onClick;
    }

    public void decreaseStock(String itemName, int quantity) {
        boolean isFeaturedItem = false;
        int index = -1;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(itemName)) {
                isFeaturedItem = true;
                index = i;
                break;
            }
        }

        if (isFeaturedItem) {
            itemsStock[index] = Math.max(0, itemsStock[index] - quantity);
            if (stockLabels[index] != null) {
                stockLabels[index].setText("Stocks: " + itemsStock[index]);
            }
            if (quantitySpinners[index] != null) {
                SpinnerNumberModel model = (SpinnerNumberModel) quantitySpinners[index].getModel();
                model.setMaximum(itemsStock[index]);
            }
            if (bnyPanel != null && bnyPanel.getStock(itemName) > 0) {
                bnyPanel.decreaseStock(itemName, quantity);
            }
        } else {
            if (bnyPanel != null && bnyPanel.getStock(itemName) > 0) {
                bnyPanel.decreaseStock(itemName, quantity);
            } else if (benchPanel != null && benchPanel.getStock(itemName) > 0) {
                benchPanel.decreaseStock(itemName, quantity);
            } else if (leePanel != null && leePanel.getStock(itemName) > 0) {
                leePanel.decreaseStock(itemName, quantity);
            } else if (memoPanel != null && memoPanel.getStock(itemName) > 0) {
                memoPanel.decreaseStock(itemName, quantity);
            } else if (penshoppePanel != null && penshoppePanel.getStock(itemName) > 0) {
                penshoppePanel.decreaseStock(itemName, quantity);
            }
        }
    }

    public int getStock(String itemName) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(itemName)) {
                int bnyStock = (bnyPanel != null) ? bnyPanel.getStock(itemName) : itemsStock[i];
                return Math.min(itemsStock[i], bnyStock);
            }
        }

        if (bnyPanel != null && bnyPanel.getStock(itemName) > 0) {
            return bnyPanel.getStock(itemName);
        } else if (benchPanel != null && benchPanel.getStock(itemName) > 0) {
            return benchPanel.getStock(itemName);
        } else if (leePanel != null && leePanel.getStock(itemName) > 0) {
            return leePanel.getStock(itemName);
        } else if (memoPanel != null && memoPanel.getStock(itemName) > 0) {
            return memoPanel.getStock(itemName);
        } else if (penshoppePanel != null && penshoppePanel.getStock(itemName) > 0) {
            return penshoppePanel.getStock(itemName);
        }

        return 0;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public BNY getBNYInstance() {
        return bnyPanel;
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
            titleFont = new Font("SansSerif", Font.PLAIN, 40);
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
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(scrollPane, "MainView");

        bnyPanel = new BNY(cardLayout, contentPanel, lightBlue, white, darkBlue, black, lightGray, darkGray, none, itemFont, dropdownFont);
        contentPanel.add(bnyPanel, "BNYView");

        benchPanel = new Bench(cardLayout, contentPanel, lightBlue, white, darkBlue, black, lightGray, darkGray, none, itemFont, dropdownFont);
        contentPanel.add(benchPanel, "BenchView");

        leePanel = new Lee(cardLayout, contentPanel, lightBlue, white, darkBlue, black, lightGray, darkGray, none, itemFont, dropdownFont);
        contentPanel.add(leePanel, "LeeView");

        memoPanel = new Memo(cardLayout, contentPanel, lightBlue, white, darkBlue, black, lightGray, darkGray, none, itemFont, dropdownFont);
        contentPanel.add(memoPanel, "MemoView");

        penshoppePanel = new Penshoppe(cardLayout, contentPanel, lightBlue, white, darkBlue, black, lightGray, darkGray, none, itemFont, dropdownFont);
        contentPanel.add(penshoppePanel, "PenshoppeView");

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

        try {
            icon = new ImageIcon("images/pfp.png");
            scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Error loading profile image: " + e.getMessage());
            icon = new ImageIcon();
            scaledImage = null;
        }
        pfpLabel = new JLabel(new ImageIcon(scaledImage));
        pfpLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pfpLabel.setToolTipText("View Profile");

        pfpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel profilePanel = new JPanel();
                profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
                profilePanel.setOpaque(false);
                profilePanel.setPreferredSize(new Dimension(400, 300));

                ImageIcon profileIcon;
                try {
                    profileIcon = new ImageIcon("images/pfp.png");
                } catch (Exception ex) {
                    System.err.println("Error loading profile icon: " + ex.getMessage());
                    profileIcon = new ImageIcon();
                }
                Image fullProfileIcon = profileIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(fullProfileIcon));
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel nameLabel = new JLabel("SyntaxSquad_2025");
                nameLabel.setFont(titleFont.deriveFont(25f));
                nameLabel.setForeground(black);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel emailLabel = new JLabel("@syntaxsquad2025@gmail.com");
                emailLabel.setFont(itemFont);
                emailLabel.setForeground(gray);
                emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JPanel phonePanel = new JPanel();
                phonePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                phonePanel.setOpaque(false);

                ImageIcon phoneIcon;
                try {
                    phoneIcon = new ImageIcon("images/phone.png");
                } catch (Exception ex) {
                    System.err.println("Error loading phone icon: " + ex.getMessage());
                    phoneIcon = new ImageIcon();
                }
                Image fullPhoneIcon = phoneIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel imageLabel2 = new JLabel(new ImageIcon(fullPhoneIcon));
                JLabel phoneLabel = new JLabel("+63 9 135-7924");
                phoneLabel.setFont(itemFont.deriveFont(15f));

                JPanel homePanel = new JPanel();
                homePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                homePanel.setOpaque(false);

                ImageIcon homeIcon;
                try {
                    homeIcon = new ImageIcon("images/address.png");
                } catch (Exception ex) {
                    System.err.println("Error loading address icon: " + ex.getMessage());
                    homeIcon = new ImageIcon();
                }
                Image fullHomeIcon = homeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel imageLabel3 = new JLabel(new ImageIcon(fullHomeIcon));
                JLabel homeLabel = new JLabel("Valenzuela City, Metro Manila, Philippines");
                homeLabel.setFont(itemFont.deriveFont(15f));

                profilePanel.add(imageLabel);
                profilePanel.add(Box.createVerticalStrut(8));
                profilePanel.add(nameLabel);
                profilePanel.add(Box.createVerticalStrut(8));
                profilePanel.add(emailLabel);
                profilePanel.add(Box.createVerticalStrut(30));
                phonePanel.add(imageLabel2);
                phonePanel.add(phoneLabel);
                profilePanel.add(phonePanel);
                profilePanel.add(Box.createVerticalStrut(0));
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
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(5, 35, 10, 10));
        categoryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        categoryTitle = new JPanel(new BorderLayout());
        categoryTitle.setOpaque(false);

        categoryLabel = new JLabel("  CLOTHING CATEGORIES");
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        categoryLabel.setForeground(white);
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 10));

        String[] category = {"  Tops", "  Bottoms  ", "   Active", "  Dresses    ", "  Outer", "Jumpsuits"};
        String[] categoryIcon = {"images/bnywomen19tops.png", "images/penshoppewomen30bottoms.png", "images/bnymen18activewear.png", "images/bnywomen24dresses.png", "images/bnymen13outerwear.png", "images/puffsleeve.png"};

        JPanel categoryItemsPanel = new JPanel();
        categoryItemsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 7, 10));
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
        itemPanel.setOpaque(false);
        itemPanel.setPreferredSize(new Dimension(190, 100));
        itemPanel.setToolTipText(itemName.trim());

        ImageIcon icon;
        try {
            icon = new ImageIcon(itemIcon);
        } catch (Exception e) {
            System.err.println("Error loading category icon " + itemIcon + ": " + e.getMessage());
            icon = new ImageIcon();
        }
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

        JLabel nameLabel = new JLabel(itemName);
        nameLabel.setFont(itemFont);
        nameLabel.setForeground(white);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        itemPanel.add(iconLabel, BorderLayout.WEST);
        itemPanel.add(nameLabel, BorderLayout.CENTER);

        return itemPanel;
    }

    private void createFeaturedItems() {
        featuredPanel = new JPanel(new BorderLayout());
        featuredPanel.setOpaque(false);
        featuredPanel.setBorder(BorderFactory.createEmptyBorder(5, 32, 10, 20));
        featuredPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        featuredTitle = new JPanel(new BorderLayout());
        featuredTitle.setOpaque(false);

        featuredLabel = new JLabel("  FEATURED ITEMS");
        featuredLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        featuredLabel.setForeground(white);
        featuredLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 10));

        featuredItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        featuredItemsPanel.setOpaque(false);

        stockLabels = new JLabel[items.length];
        quantitySpinners = new JSpinner[items.length];

        for (int i = 0; i < items.length; i++) {
            featuredItemsPanel.add(createFeaturedItem(items[i], itemsPrice[i], itemsTag[i], itemsIcon[i], itemsStock[i], i, brandSizes[i]));
        }

        featuredTitle.add(featuredLabel, BorderLayout.WEST);
        featuredPanel.add(featuredTitle, BorderLayout.NORTH);
        featuredPanel.add(featuredItemsPanel, BorderLayout.CENTER);
        mainPanel.add(featuredPanel);
    }

    private JPanel createFeaturedItem(String item, String price, String tag, String icon, int stock, int index, int[] brandSize) {
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
        priceLabel.setPreferredSize(new Dimension(80, 20));

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

        stockLabels[index] = new JLabel("Stocks: " + stock);
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
            if (itemsStock[index] <= 0) {
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

            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, itemsStock[index], 1);
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
                if (selectedQuantity > itemsStock[index]) {
                    JOptionPane.showMessageDialog(null, "Requested quantity exceeds available stock for " + item, "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                cartItems.add(new CartItem(item, price, selectedSize, icon, selectedQuantity, brandSize[0], brandSize[1]));
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
        brandPanel.setBorder(BorderFactory.createEmptyBorder(5, 32, 10, 10));
        brandPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        brandTitle = new JPanel(new BorderLayout());
        brandTitle.setOpaque(false);

        brandLabel = new JLabel("  AVAILABLE BRANDS");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        brandLabel.setForeground(white);
        brandLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[] brand = {"BNY", "Bench", "Lee", "Memo", "Penshoppe"};
        String[] brandIcon = {"images/bny.png", "images/bench.png", "images/lee.png", "images/memo.jpg", "images/penshoppe.png"};
        int[][] brandSize = {{140, 70}, {150, 40}, {105, 90}, {110, 100}, {160, 30}};

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
        itemPanel.setPreferredSize(new Dimension(230, 150));
        itemPanel.setToolTipText(brandName);

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new GridBagLayout());
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(200, 110));
        logoPanel.setMaximumSize(new Dimension(200, 110));

        ImageIcon icon;
        try {
            icon = new ImageIcon(brandIcon);
        } catch (Exception e) {
            System.err.println("Error loading brand icon " + brandIcon + ": " + e.getMessage());
            icon = new ImageIcon();
        }
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
                } else if (brandName.equals("Lee")) {
                    cardLayout.show(contentPanel, "LeeView");
                    contentPanel.revalidate();
                    contentPanel.repaint();
                } else if (brandName.equals("Memo")) {
                    cardLayout.show(contentPanel, "MemoView");
                    contentPanel.revalidate();
                    contentPanel.repaint();
                } else if (brandName.equals("Penshoppe")) {
                    cardLayout.show(contentPanel, "PenshoppeView");
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
            }
        });

        return itemPanel;
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.*;

public class MainContents extends JPanel {
    private Font titleFont, itemFont;
    private JPanel mainPanel, topPanel, categoryPanel, featuredPanel, brandPanel;
    private JPanel categoryTitle, featuredTitle, brandTitle, featuredItemsPanel;
    private JLabel greetLabel, pfpLabel, categoryLabel, featuredLabel, featuredView, brandLabel, brandView;
    private ImageIcon icon;
    private Image scaledImage;
    private JScrollPane scrollPane;
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color gray = Color.GRAY;
    private Color borderGray = new Color(169, 169, 169);
    private Color darkerBlue = new Color(0, 0, 139);
    private Color hoverBlue = new Color(135, 206, 250);
    private Color selectBlue = new Color(100, 149, 237);
    private Color peach = new Color(255, 218, 185);
    private Color black = Color.BLACK;
    private Color lightGray = new Color(211, 211, 211);
    private Color darkGray = new Color(169, 169, 169);
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
        String color;
        String size;
        String iconPath;
        int quantity;

        public CartItem(String name, String price, String color, String size, String iconPath, int quantity) {
            this.name = name;
            this.price = price;
            this.color = color;
            this.size = size;
            this.iconPath = iconPath;
            this.quantity = quantity;
        }
    }

    public void setOnBrandViewClick(Runnable onClick) {
        this.onBrandViewClick = onClick;
    }

    public void setOnViewCartClick(Runnable onClick) {
        this.onViewCartClick = onClick;
    }

    public MainContents() {
        try {
            titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(titleFont);
            itemFont = titleFont.deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            titleFont = new Font("Serif", Font.ITALIC, 40);
            itemFont = titleFont.deriveFont(16f);
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
                JLabel phoneLabel = new JLabel("(123) 456-7890");
                phoneLabel.setFont(itemFont.deriveFont(15f));

                JPanel homePanel = new JPanel();
                homePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                homePanel.setOpaque(false);

                ImageIcon homeIcon = new ImageIcon("images/address.png");
                Image fullHomeIcon = homeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel imageLabel3 = new JLabel(new ImageIcon(fullHomeIcon));
                JLabel homeLabel = new JLabel("Boston, Massachusetts");
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
        categoryLabel.setFont(new Font(null, Font.BOLD, 20));
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
                BorderFactory.createLineBorder(borderGray, 1),
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
                if(itemName.trim().equals("Tops")) {
                    JPanel topsPanel = new TopsPanel(cardLayout, contentPanel, lightBlue, white, darkerBlue, peach, black, lightGray, darkGray, itemFont);
                    contentPanel.add(topsPanel, "TopsView");
                    cardLayout.show(contentPanel, "TopsView");
                    contentPanel.revalidate();
                    contentPanel.repaint();
                } else if (itemName.trim().equals("Active Wear")) {
                    JPanel activeWearPanel = new ActiveWearPanel(cardLayout, contentPanel, lightBlue, white, darkerBlue, peach, black, lightGray, darkGray, itemFont);
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
        featuredLabel.setFont(new Font(null, Font.BOLD, 20));
        featuredLabel.setForeground(white);
        featuredLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 10));

        featuredView = new JLabel("View All >");
        featuredView.setFont(new Font(null, Font.PLAIN, 17));
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

        String[] items = {"Own the Run Running T-Shirt", "Aeroimpact Luxe Training Light-Support Bra", "Essentials 3-Stripes Hoodie",
                "Slim-Fit Wool Pants", "Belted Wool Midi Dress"};
        String[] itemsPrice = {"$40.00", "$50.00", "$39.00", "$165.00", "$135.00"};
        String[] itemsTag = {"Top rated", "Top rated", "Top rated", "Best offer", "Best style"};
        String[] itemsIcon = {"images/featuredAdidas1.png", "images/featuredAdidas2.png", "images/featuredAdidas3.png", "images/featuredCOS1.png", "images/featuredCOS2.png"};

        for (int i = 0; i < items.length; i++) {
            featuredItemsPanel.add(createFeaturedItem(items[i], itemsPrice[i], itemsTag[i], itemsIcon[i]));
        }

        featuredTitle.add(featuredLabel, BorderLayout.WEST);
        featuredTitle.add(featuredView, BorderLayout.EAST);
        featuredPanel.add(featuredTitle, BorderLayout.NORTH);
        featuredPanel.add(featuredItemsPanel, BorderLayout.CENTER);
        mainPanel.add(featuredPanel);
    }

    private JPanel createFeaturedItem(String item, String price, String tag, String icon) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(230, 300));
        itemPanel.setBackground(lightBlue);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderGray, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        if (!tag.isEmpty()) {
            JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            tagPanel.setBackground(peach);
            tagPanel.setOpaque(true);
            tagPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

            JLabel tagLabel = new JLabel(tag);
            tagLabel.setFont(itemFont);
            tagLabel.setForeground(black);
            tagPanel.add(tagLabel);
            // Set tagPanel size based on the label's preferred size
            Dimension labelSize = tagLabel.getPreferredSize();
            tagPanel.setPreferredSize(new Dimension(labelSize.width + 10, labelSize.height + 6));

            JPanel tagContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
            tagContainer.setOpaque(false);

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

            JPanel nameWrapper = new JPanel();
            nameWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            nameWrapper.setOpaque(false);
            nameWrapper.setPreferredSize(new Dimension(200, 80));

            JPanel pricePanel = new JPanel();
            pricePanel.setLayout(new BorderLayout());
            pricePanel.setOpaque(false);

            JLabel priceLabel = new JLabel(price);
            priceLabel.setFont(itemFont);
            priceLabel.setForeground(black);
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton addButton = new JButton("🛒");
            addButton.setForeground(black);
            addButton.setBackground(lightGray);
            addButton.setOpaque(true);
            addButton.setBorderPainted(false);
            addButton.setFont(new Font(null, Font.PLAIN, 15));
            addButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addButton.setFocusable(false);
            addButton.setPreferredSize(new Dimension(40, 30));
            addButton.setMaximumSize(new Dimension(40, 30));

            addButton.addActionListener(e -> {
                String[] color = {"Red", "Green", "Blue", "Black", "White"};
                String[] size = {"XS", "S", "M", "L", "XL", "2XL", "3XL", "4XL"};

                JPanel detailsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
                detailsPanel.setPreferredSize(new Dimension(400, 200));
                detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                detailsPanel.setFocusable(false);

                JLabel colorLabel = new JLabel("Select Color:");
                colorLabel.setFont(itemFont);
                colorLabel.setFocusable(false);

                JComboBox<String> colorBox = new JComboBox<>(color);
                Font dropdownFont = itemFont.deriveFont(16f);
                colorBox.setFont(dropdownFont);
                colorBox.setFocusable(false);

                JLabel sizeLabel = new JLabel("Select size:");
                sizeLabel.setFont(itemFont);
                sizeLabel.setFocusable(false);

                JComboBox<String> sizeBox = new JComboBox<>(size);
                sizeBox.setFont(dropdownFont);
                sizeBox.setFocusable(false);

                JLabel quantityLabel = new JLabel("Quantity:");
                quantityLabel.setFont(itemFont);
                quantityLabel.setFocusable(false);

                SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 99, 1);
                JSpinner quantitySpinner = new JSpinner(spinnerModel);
                quantitySpinner.setFont(dropdownFont);
                quantitySpinner.setFocusable(false);

                detailsPanel.add(colorLabel);
                detailsPanel.add(colorBox);
                detailsPanel.add(sizeLabel);
                detailsPanel.add(sizeBox);
                detailsPanel.add(quantityLabel);
                detailsPanel.add(quantitySpinner);

                int result = JOptionPane.showConfirmDialog(null, detailsPanel, "Product Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String selectedColor = (String) colorBox.getSelectedItem();
                    String selectedSize = (String) sizeBox.getSelectedItem();
                    int selectedQuantity = (Integer) quantitySpinner.getValue();
                    cartItems.add(new CartItem(item, price, selectedColor, selectedSize, icon, selectedQuantity));
                    JOptionPane.showMessageDialog(null, item + " added to cart! \nColor: " + selectedColor + "\nSize: " + selectedSize + "\nQuantity: " + selectedQuantity);
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

            tagContainer.add(tagPanel);
            pricePanel.add(priceLabel, BorderLayout.WEST);
            buttonWrapper.add(addButton);
            pricePanel.add(buttonWrapper, BorderLayout.EAST);
            itemDetailsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            itemDetailsPanel.add(iconLabel);
            itemDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            itemDetailsPanel.add(namePane);
            itemDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            itemDetailsPanel.add(pricePanel);
            itemPanel.add(tagContainer, BorderLayout.NORTH);
            itemPanel.add(itemDetailsPanel, BorderLayout.CENTER);
        } else {
            itemPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

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
        brandLabel.setFont(new Font(null, Font.BOLD, 20));
        brandLabel.setForeground(white);
        brandLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        brandView = new JLabel("View All >");
        brandView.setFont(new Font(null, Font.PLAIN, 17));
        brandView.setForeground(white);
        brandView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        brandView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                brandView.setForeground(darkerBlue);
                brandView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                brandView.setForeground(white);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                brandView.setForeground(darkerBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                brandView.setForeground(darkerBlue);
                if (onBrandViewClick != null) {
                    onBrandViewClick.run();
                }
            }
        });

        String[] brand = {"Adidas", "COS", "Everlane", "Mango", "Aritzia", "Nike"};
        String[] brandIcon = {"images/adidas.png", "images/cos.png", "images/everlane.png", "images/mango.png", "images/aritzia.png", "images/nike.png"};
        int[][] brandSize = {{130, 90}, {150, 140}, {185, 40}, {150, 100}, {180, 90}, {140, 100}};

        JPanel brandItemsPanel = new JPanel();
        brandItemsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        brandItemsPanel.setOpaque(false);

        for (int i = 0; i < brand.length; i++) {
            brandItemsPanel.add(createBrandItem(brand[i], brandIcon[i], brandSize[i][0], brandSize[i][1]));
        }

        brandTitle.add(brandLabel, BorderLayout.WEST);
        brandTitle.add(brandView, BorderLayout.EAST);
        brandPanel.add(brandTitle, BorderLayout.NORTH);
        brandPanel.add(brandItemsPanel, BorderLayout.CENTER);
        mainPanel.add(brandPanel);
    }

    private JPanel createBrandItem(String brandName, String brandIcon, int width, int height) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setBackground(lightBlue);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(lightGray, 1),
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
                JOptionPane.showMessageDialog(null, brandName + " selected!");
            }
        });

        return itemPanel;
    }
}
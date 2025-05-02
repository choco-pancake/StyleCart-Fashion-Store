import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;

public class ActiveWearPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Color lightBlue, white, peach, black, lightGray, darkGray, darkBlue;
    private Font itemFont, customFont, titleFont;
    private JPanel topPanel, backPanel, mainPanel, menPanel, womenPanel, titleContentPanel, sectionContainer, menContainer, womenContainer;
    private JButton backButton;
    private JLabel titleLabel, menLabel, womenLabel;
    private JScrollPane scrollPane;

    public ActiveWearPanel(CardLayout cardLayout, JPanel contentPanel, Color lightBlue, Color white,
                           Color darkerBlue, Color peach, Color black, Color lightGray, Color darkGray, Font itemFont) {

        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        this.lightBlue = lightBlue;
        this.white = white;
        this.peach = peach;
        this.black = black;
        this.lightGray = lightGray;
        this.darkGray = darkGray;
        this.itemFont = itemFont;
        this.darkBlue = new Color(75, 75, 150);

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

        initTopPanel();
        initMainPanel();

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

    private void initTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        topPanel.setBackground(darkBlue);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray));
    
        backButton = createBackButton();
        backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setOpaque(false);
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        backPanel.add(backButton);
    /*
        ImageIcon icon = null;
        try {
            icon = new ImageIcon("images/activeweartop.png"); 
            Image image = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); 
            icon = new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    */
        titleLabel = new JLabel("Active Wear");
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
        JButton button = new JButton("←") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 0));
                g2.fillRect(0, 0, getWidth(), getHeight());

                int size = Math.min(getWidth(), getHeight()) - 4;
                if (getModel().isPressed()) {
                    g2.setColor(darkGray.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(darkGray);
                } else {
                    g2.setColor(lightGray);
                }

                g2.fill(new Ellipse2D.Double(2, 2, size, size));
                g2.setColor(getModel().isPressed() || getModel().isRollover() ? white : black);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
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

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusable(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(customFont.deriveFont(30f)); 
        button.setPreferredSize(new Dimension(40, 40));
        button.addActionListener(e -> cardLayout.show(contentPanel, "MainView"));
        return button;
    }

    private void initMainPanel() {
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
            "Adizero Essentials Running Shorts", "Own the Run Running T-Shirt", "Adizero Running Singlet", "Power 3-Stripes Racerback Running Bra Tank Top", "Run Pocket Medium Support Bra",
            "All Me Light Support Bra", "Aeroimpact Luxe Training Light-Support Bra", "Believe This Medium-Support Sports Bra", "Powerimpact Training Bra"
        };

        String[] prices = {
            "$30.00", "$40.00", "$70.00", "$45.00", "$22.00",
            "$45.00", "$50.00", "$24.00", "$17.00"
        };

        String[] tags = {
            "Adidas", "Adidas", "Adidas", "Adidas", "Adidas",
            "Adidas", "Adidas", "Adidas", "Adidas"
        };

        String[] icons = {
            "images/activewear1.png", "images/activewear2.png", "images/activewear3.png", "images/activewear4.png", "images/activewear5.png",
            "images/activewear6.png", "images/activewear7.png", "images/activewear8.png", "images/activewear9.png"
        };

        for(int i = 0; i < 3; i++) {
            menPanel.add(createActivewearItem(items[i], prices[i], tags[i], icons[i]));
        }

        for(int i = 3; i < items.length; i++) {
            womenPanel.add(createActivewearItem(items[i], prices[i], tags[i], icons[i]));
        }

        sectionContainer = new JPanel();
        sectionContainer.setLayout(new BorderLayout());
        sectionContainer.setOpaque(false);

        menContainer.add(menLabel, BorderLayout.NORTH);
        menContainer.add(menPanel, BorderLayout.CENTER);
        womenContainer.add(womenLabel, BorderLayout.NORTH);
        womenContainer.add(womenPanel, BorderLayout.CENTER);
        sectionContainer.add(menContainer, BorderLayout.NORTH);
        sectionContainer.add(womenContainer, BorderLayout.CENTER);        
        mainPanel.add(sectionContainer, BorderLayout.CENTER);
    }

    private JPanel createActivewearItem(String item, String price, String tag, String icon) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(230, 300));
        itemPanel.setBackground(lightBlue);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(lightGray, 1),
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
                    MainContents.getCartItems().add(new MainContents.CartItem(item, price, selectedColor, selectedSize, icon, selectedQuantity));
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
}
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;

public class Penshoppe extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Color lightBlue, white, black, lightGray, darkGray, darkBlue, none;
    private Font itemFont, customFont, titleFont, dropdownFont;
    private JPanel topPanel, backPanel, mainPanel, menPanel, womenPanel, titleContentPanel, sectionContainer, menContainer, womenContainer;
    private JButton backButton;
    private JLabel titleLabel, menLabel, womenLabel;
    private JScrollPane scrollPane;

    public Penshoppe(CardLayout cardLayout, JPanel contentPanel, Color lightBlue, Color white, Color darkerBlue, Color black, Color lightGray, Color darkGray, 
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

        titleLabel = new JLabel("Penshoppe");
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
            "23 Relaxed Fit Football Jersey", "Basic Easy Iron Short Sleeve Shirt", "Boxy Fit Plaid Short Sleeve Shirt", "Game Over Regular Fit Graphic T-Shirt", "Good Times Regular Fit Graphic T-Shirt",
            "Icon Relaxed Fit Graphic T-Shirt", "Chill Spot Central Relaxed Fit Graphic T-Shirt (Off White)", "Driven by Adrenaline Regular Fit Graphic T-Shirt", "Easy Days Regular Fit T-Shirt (Light Blue)", "Need Cash Not Advice Regular Fit T-Shirt",
            "Regular Fit Crew Neck T-Shirt", "Relaxed Fit Raglan T-Shirt with Embroidery", "Relaxed Fit T-Shirt with Branding Print", "Relaxed Fit T-Shirt with PS Embroidery", "Racing Relaxed Fit Graphic Polo",
            "Relaxed Fit Short Sleeves Resort Shirt with All-Over Embroidery", "Relaxed Fit Short Sleeves Resort Shirt with Embroidery", "Relaxed Fit Striped Polo", "Tie Dye Resort Shirt", "Easy Iron Short Sleeve Shirt",
            "Underground Relaxed Fit Graphic T-Shirt", "Relaxed Fit Long Sleeves Striped Shirt", "Relaxed Fit Pullover Sweatshirt", "Dress Code Striped Long Sleeves Shirt", "Modern Fit Nylon Sports Shorts",
            "Nylon Pull-On Cargo Shorts", "Relaxed Fit Brushed Terry Shorts", "Staple Twill Shorts for Men", "Dapper Fit Ankle Length Pull-On Trousers", "Slim Fit Staple Nylon Pants",

            "Cherry All-Over Printed Henley Regular Fit Top", "Jacquard Knit Regular Fit Polo", "If You Break It Slim Fit Graphic T-Shirt", "Regular Fit Waffle Knit Polo", "Vintage Summer Striped Relaxed Fit T-Shirt",
            "Relaxed Fit Stripes T-Shirt with Branding", "Ribbed Top", "Slim Fit Polo with Contrast Tipping", "Slim Fit Textured Top", "Unleash Your Speed Loose Fit Graphic T-Shirt",
            "Ribbed Knit Slim Fit Tank Top", "Short Sleeve Regular Fit Baby Doll Top", "Short Sleeves Top with Drawstring", "Sleeveless Stripes Peplum Top", "Relaxed Fit Stripes Pullover Sweater",
            "Striped Long Sleeves Button Down Top with Heart Embroidery", "Bermuda Denim Shorts", "Striped Denim Shorts (Striped)", "Denim Cargo Skirt", "Denim Midi Skirt",
            "Patch Pocket Denim Skirt", "Plaid Skort for Women", "Tiered Midi Skirt", "Denim Skort (Beige)", "Waffle Knit Skort",
            "Cropped Wide Leg Trousers", "Dress Code Wide Leg Pleated Trousers", "Wide Leg Cargo Pants (Black) ", "Wide Leg Cargo Denim Jeans", "Wide Leg Cargo Pants (Pink)"
        };
       
        String[] prices = {
            "₱699", "₱499", "₱899", "₱249", "₱379",
            "₱499", "₱398", "₱231", "₱252", "₱185",
            "₱218", "₱364", "₱331", "₱315", "₱899",
            "₱999", "₱899", "₱799", "₱299", "₱278",
            "₱464", "₱699", "₱799", "₱729", "₱505",
            "₱468", "₱231", "₱599", "₱398", "₱915",

            "₱599", "₱699", "₱185", "₱366", "₱398",
            "₱499", "₱399", "₱599", "₱349", "₱599",
            "₱349", "₱849", "₱899", "₱899", "₱589",
            "₱849", "₱899", "₱799", "₱654", "₱524",
            "₱371", "₱358", "₱822", "₱799", "₱699",
            "₱654", "₱645", "₱431", "₱899", "₱799"
        }; 

        String[] icons = {
           "images/penshoppemen1activewear.png", "images/penshoppemen2tops.png", "images/penshoppemen3tops.png", "images/penshoppemen4tops.png", "images/penshoppemen5tops.png",
           "images/penshoppemen6tops.png", "images/penshoppemen7tops.png", "images/penshoppemen9tops.png", "images/penshoppemen10tops.png", "images/penshoppemen12tops.png",
           "images/penshoppemen14tops.png", "images/penshoppemen16tops.png", "images/penshoppemen17tops.png", "images/penshoppemen18tops.png", "images/penshoppemen20activewear.png",
           "images/penshoppemen23tops.png", "images/penshoppemen24tops.png", "images/penshoppemen25tops.png", "images/penshoppemen26tops.png", "images/penshoppemen28tops.png",
           "images/penshoppemen29tops.png", "images/penshoppemen21tops.png", "images/penshoppemen22outerwear.png", "images/penshoppemen27tops.png", "images/penshoppemen11activewear.png",
           "images/penshoppemen13activewear.png", "images/penshoppemen15activewear.png", "images/penshoppemen30activewear.png", "images/penshoppemen8bottoms.png", "images/penshoppemen19bottoms.png",

           "images/penshoppewomen2tops.png", "images/penshoppewomen3tops.png", "images/penshoppewomen8tops.png", "images/penshoppewomen11tops.png", "images/penshoppewomen14tops.png",
           "images/penshoppewomen16tops.png", "images/penshoppewomen18tops.png", "images/penshoppewomen22tops.png", "images/penshoppewomen23tops.png", "images/penshoppewomen27tops.png",
           "images/penshoppewomen17tops.png", "images/penshoppewomen19tops.png", "images/penshoppewomen20tops.png", "images/penshoppewomen21tops.png", "images/penshoppewomen12outerwear.png",
           "images/penshoppewomen26tops.png", "images/penshoppewomen1bottoms.png", "images/penshoppewomen25bottoms.png", "images/penshoppewomen5bottoms.png", "images/penshoppewomen6bottoms.png",
           "images/penshoppewomen9bottoms.png", "images/penshoppewomen10bottoms.png", "images/penshoppewomen13bottoms.png", "images/penshoppewomen24bottoms.png", "images/penshoppewomen28bottoms.png",
           "images/penshoppewomen4bottoms.png", "images/penshoppewomen7bottoms.png", "images/penshoppewomen15bottoms.png", "images/penshoppewomen29bottoms.png", "images/penshoppewomen30bottoms.png"
        };

        int[][] brandSize = {
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {110, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {80, 100}, {60, 100},

            {100, 100}, {100, 100}, {110, 100}, {120, 100}, {100, 100},
            {100, 100}, {110, 100}, {100, 100}, {110, 100}, {100, 100},
            {80, 100}, {80, 100}, {80, 100}, {80, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {80, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {80, 100}, {80, 100}, {80, 100}, {80, 100}, {80, 100}
        };
 
        for (int i = 0; i < items.length; i++) {
            if (i < 30) {
                menPanel.add(createPenshoppeItem(items[i], prices[i], icons[i], brandSize[i][0], brandSize[i][1]));
            } else {
                womenPanel.add(createPenshoppeItem(items[i], prices[i], icons[i], brandSize[i][0], brandSize[i][1]));
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

    private JPanel createPenshoppeItem(String item, String price, String icon, int width, int height) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(230, 270));
        itemPanel.setBackground(lightBlue);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darkGray, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));  

        JPanel itemDetailsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
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

        addButton.addActionListener(e -> {
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

            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 99, 1);
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
        itemPanel.add(itemDetailsPanel, BorderLayout.NORTH);

        return itemPanel;
    }
}
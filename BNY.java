import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;

public class BNY extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Color lightBlue, white, black, lightGray, darkGray, darkBlue, none;
    private Font itemFont, customFont, titleFont, dropdownFont;
    private JPanel topPanel, backPanel, mainPanel, menPanel, womenPanel, titleContentPanel, sectionContainer, menContainer, womenContainer;
    private JButton backButton;
    private JLabel titleLabel, menLabel, womenLabel;
    private JScrollPane scrollPane;

    public BNY(CardLayout cardLayout, JPanel contentPanel, Color lightBlue, Color white, Color darkerBlue, Color black, Color lightGray, Color darkGray, 
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

        titleLabel = new JLabel("BNY");
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
            "Kaiki White Color Street Trendy Versace Shirt", "Fiaunt Fashions Oversized Retro Classic T-Shirt", "Flower Style Valentine's Day Casual Polo Dating Shirt", "High Quality Shortsleeve Casual Polo Shirt", "Korean Vintage Loose Short-Sleeved T-Shirt",
            "New WESTERN Summer Casual Pure Color Short Sleeved Shirt", "Korean Style Loose Fit Oversize Shirt Mens Silk Polo Shirt", "Plain Polo Shirt with 2 Lines", "Plain Hoodie Jacket with Zipper", "Korean New York Jacket with Zipper",
            "LSB# Super Hot CEC Men's Tide Brand Trend Lightweight Jacket", "Bomber with Zipper Jacket", "TATA Fashion New Long Sleeve Plain Varsity Jacket", "New York Printed Design Hoodie Jacket", "TATA Fashion New Casual Baseball Jacket",
            "Chinos 4-Pockets above-Knee Plain Garter Short", "Cod Men's Casual Sweat Jogger Sport Short", "Fashion Taslan Short", "HK Fashion Plain Short (with Belt)", "Street Style Short",
            "New DUKE Dri-Fit Men's Sport Short Casual Basketball Shorts", "Above-Knee Casual Garter Beach Short", "Ezra Black Regular Fit Boys Dress Pants", "Wild Cargo Pants", "Taslan Loose Casual Pants",
            "Rope Workwear with Pocket Loose Wide Leg Ankle Banded Pants", "Smart Stretch Pleat Slacks Beige", "Baggy Straight Cut Slacks", "Sweatpant Wide Leg Baggy Loose Straight Cut", "Business Formal Pants",
            
            "Casual Plain Color Block Embroidery Zipper T-Shirt", "Short Letter Pattern Slim Fit Printed T-Shirt", "Summer Square Neck Short Sleeve Slim Bottoming Knit Top", "Elegant Plain Split T-Shirt", "Sporty Plain Contrast Binding Crop T-Shirt",
            "Slubs V-Neck T-Shirt with Bunny Embro", "Sleeveless Tank Vest Solid Top", "Solid Ribbed Knit Tube Basic Crop Top", "Casual Pleated Crop Tank Top", "Flower Tube Top with Pads",
            "Hepburn Style Dress Summer Fashion Loose Slimmer", "Green Floral Strappy Dress", "Spring Autumn Sweater Dress", "Summer New Style Polo Color Short Sleeved Dress", "Heavy Industry Three-Dimensional Bow Knot Letter Diamond Dress",
            "Small Leather Jacket", "Fried Street Yellow Floral Jacket", "Western Style Jacket New French Light Luxury", "Western Style Jacket European Station Spring Autumn", "Black Jacket",
            "Fashion Trendy Plain Trouser Shorts", "New Taslan Short", "Casual Plain Button Short", "Casual Stripped Lace Up Zipper Short", "High Waisted Loose Fit Slim Stretchable Shorts Bermuda Short",
            "Mid-waist Relaxed Pants", "Highwaist Pants Denim Ripped Jeans", "Full Length High Rise Wide Leg Jeans", "Highwaist Pants with Belt", "Wide Leg Pants Fashion High Waist Solid Button"
        };
    
        String[] prices = {
            "₱259", "₱144", "₱176", "₱188", "₱199",
            "₱174", "₱170", "₱142", "₱145", "₱277",
            "₱189", "₱145", "₱323", "₱143", "₱422",
            "₱169", "₱156", "₱355", "₱199", "₱169",
            "₱294", "₱248", "₱401", "₱246", "₱169",
            "₱345", "₱350", "₱159", "₱149", "₱400",

            "₱282", "₱200", "₱129", "₱330", "₱549",
            "₱211", "₱159", "₱179", "₱177", "₱109",
            "₱930", "₱262", "₱700", "₱900", "₱824",
            "₱398", "₱443", "₱700", "₱668", "₱890",
            "₱105", "₱128", "₱283", "₱273", "₱281",
            "₱400", "₱458", "₱389", "₱500", "₱458"
        };
    
        String[] icons = {
            "images/bnymen2tops.png", "images/bnymen7tops.png", "images/bnymen8tops.png", "images/bnymen9tops.png", "images/bnymen12tops.png",
            "images/bnymen19tops.png", "images/bnymen23tops.png", "images/bnymen28tops.png", "images/bnymen1outerwear.png", "images/bnymen11outerwear.png",
            "images/bnymen13outerwear.png", "images/bnymen15outerwear.png", "images/bnymen22outerwear.png", "images/bnymen29outerwear.png", "images/bnymen30outerwear.png",
            "images/bnymen3bottoms.png", "images/bnymen4bottoms.png", "images/bnymen6activewear.png", "images/bnymen10bottoms.png", "images/bnymen17activewear.png",
            "images/bnymen18activewear.png", "images/bnymen25bottoms.png", "images/bnymen5bottoms.png", "images/bnymen14bottoms.png", "images/bnymen16activewear.png",
            "images/bnymen20bottoms.png", "images/bnymen21bottoms.png","images/bnymen24bottoms.png",  "images/bnymen26bottoms.png", "images/bnymen27bottoms.png",

            "images/bnywomen2tops.png", "images/bnywomen11tops.png", "images/bnywomen15tops.png", "images/bnywomen21tops.png", "images/bnywomen22tops.png",
	        "images/bnywomen27tops.png", "images/bnywomen1tops.png", "images/bnywomen13tops.png", "images/bnywomen19tops.png", "images/bnywomen26tops.png",
	        "images/bnywomen4dresses.png", "images/bnywomen6dresses.png", "images/bnywomen14dresses.png", "images/bnywomen24dresses.png", "images/bnywomen25dresses.png",
	        "images/bnywomen5outerwear.png","images/bnywomen9outerwear.png", "images/bnywomen16outerwear.png", "images/bnywomen17outerwear.png", "images/bnywomen28outerwear.png",
	        "images/bnywomen3bottoms.png", "images/bnywomen10activewear.png", "images/bnywomen18bottoms.png", "images/bnywomen20bottoms.png", "images/bnywomen23bottoms.png",
	        "images/bnywomen7bottoms.png", "images/bnywomen8bottoms.png", "images/bnywomen12bottoms.png", "images/bnywomen29bottoms.png", "images/bnywomen30bottoms.png"

        };

        int[][] brandSize = {
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {110, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {70, 100}, {50, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},

            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {110, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}
        };

        for (int i = 0; i < 60; i++) {
            if(i < 30) {
                menPanel.add(createBNYItem(items[i], prices[i], icons[i], brandSize[i][0], brandSize[i][1]));
            } else {
                womenPanel.add(createBNYItem(items[i], prices[i], icons[i], brandSize[i][0], brandSize[i][1]));
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

    private JPanel createBNYItem(String item, String price, String icon, int width, int height) {
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
                MainContents.CartItem newItem = new MainContents.CartItem(item, price, selectedSize, icon, selectedQuantity);
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
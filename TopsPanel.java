import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TopsPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Color lightBlue, white, darkBlue, peach, black, lightGray, darkGray, none;
    private Font itemFont, customFont, titleFont, dropdownFont;
    private JPanel topPanel, backPanel, mainPanel, menPanel, womenPanel, titleContentPanel, sectionContainer, menContainer, womenContainer;
    private JButton backButton;
    private JLabel titleLabel, menLabel, womenLabel;
    private JScrollPane scrollPane;
    private Runnable onViewCartClick;
    private HashMap<String, Integer> itemStocks; // To store item stocks
    private int lastScrollPosition = 0; // To store the last scroll position

    // Class-level arrays to be accessible in refreshStockDisplay
    private String[] items;
    private String[] prices;
    private String[] tags;
    private String[] icons;
    private int[][] brandSize;
    private int[] stocks;

    public void setOnViewCartClick(Runnable onClick) {
        this.onViewCartClick = onClick;
    }

    public TopsPanel(CardLayout cardLayout, JPanel contentPanel, Color lightBlue, Color white, Color darkBlue, Color peach, Color black, 
                          Color lightGray, Color darkGray, Color none, Font itemFont, Font dropdownFont) {
        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;
        this.lightBlue = lightBlue;
        this.white = white;
        this.darkBlue = darkBlue;
        this.peach = peach;
        this.black = black;
        this.lightGray = lightGray;
        this.darkGray = darkGray;
        this.none = none;
        this.itemFont = itemFont;
        this.dropdownFont = dropdownFont;
        this.itemStocks = new HashMap<>(); // Initialize the HashMap

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
        SwingUtilities.invokeLater(() -> {
            // Restore the last scroll position when the panel is shown
            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            if (verticalScrollBar != null) {
                verticalScrollBar.setValue(lastScrollPosition);
            }
        });
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        // Save the current scroll position when the panel is hidden
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        if (verticalScrollBar != null) {
            lastScrollPosition = verticalScrollBar.getValue();
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

        titleLabel = new JLabel("Tops");
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

        // Initialize class-level arrays
        items = new String[] {
            "Kaiki White Color Street Trendy Versace Shirt", "Fiaunt Fashions Oversized Retro Classic T-Shirt", "Flower Style Valentine's Day Casual Polo Dating Shirt", "High Quality Shortsleeve Casual Polo Shirt", "Korean Vintage Loose Short-Sleeved T-Shirt",
            "New WESTERN Summer Casual Pure Color Short Sleeved Shirt", "Korean Style Loose Fit Oversize Shirt Mens Silk Polo Shirt", "Plain Polo Shirt with 2 Lines", "Bench Everyday’s Pullover Top", "Bench x Baekhyun Logo Tee",
            "Men's Statement Tee", "Men's Boxy Tee", "Men's Classic Tee", "Men's Embroidered Tee", "Flat Knit Polo Shirt",
            "Men's Graphic Tee", "Heritage Oversized T-Shirt", "Heritage Polo Shirt", "Men's Logo Tee", "Men's Polo Shirt",
            "Men's Round Neck Tee", "Round Neck Tee", "Men's Short Printed Shirt", "Short Sleeved Polo", "Sleeveless Pullover Top",
            "Striped Polo Shirt", "Stripped Short Sleeve Polo", "Button Down Polo", "Cut and Sew T-Shirt", "Embossed Logo T-Shirt Euro Fit",
            "Embossed Logo T-Shirt (Black)", "Euro Fit Logo T-Shirt (Beige)", "Euro Fit T-Shirt", "Fits 'Em All Yarn Dyed Stripes Tee", "Graphic T-Shirt (Rust)",
            "Graphic T-Shirt (Stretchable Fabric)", "Graphic Tee for Men", "Graphic X-line T-Shirt", "Logo T-Shirt for Men", "Embossed Logo T-Shirt",
            "Oversized T-Shirt (Embroidered Logo)", "Oversized T-Shirt (Shitake)", "Polo Shirt Short Sleeve", "Polo Shirt Stripe Euro Fit", "Polo Shirt Stripes (Black and Yellow)",
            "Polo Shirt Stripes (Black and White)", "Regular Fit Raglan T-Shirt", "Small Logo T-Shirt (Gray Ridge)", "Small Logo T-Shirt (Indigo)", "Stripes with Jacquard T-Shirt",
            "Tie Dye T-Shirt", "Xline Graphic O-Neck T-Shirt", "Basic Crew Neck T-Shirt", "Basic Crew Neck Tee", "Boxy Fit Striped Flat Knit Top (Choco Brown)",
            "Boxy Knit Oversized with Zip Closure", "Essential Polo for Men", "O.O.O Textured Polo Shirt", "O.O.O. Textured T-Shirt", "Pleated Short Sleeve Shirt",
            "Regular Fit Polo with Contrast Tipping", "Regular Fit Rib Knit Striped Polo", "Regular Fit Textured T-Shirt", "Slim Fit Polo with Textured Collar (White)", "Slim Fit Polo with Textured Collar (Green)",
            "Soft Touch Flat Knit Pullover with Contrast Tipping (Light Blue)", "T-Shirt With Contrast Piping", "Textured Knit T-Shirt", "Textured Open Collar Knit Polo Sleeve Shirt", "Textured Open Collar Polo (Midnight Navy)",
            "Textured Relaxed Fit Short Sleeve Shirt", "Textured Ringer Striped T-Shirt", "Ultimate Basics Comfort T-Shirt", "Ultimate Basics Owl Polo", "Ultimate Basics Premium Flat Knit Polo",
            "Zip-Up Polo Shirt with Tipping", "Basic Easy Iron Short Sleeve Shirt", "Boxy Fit Plaid Short Sleeve Shirt", "Game Over Regular Fit Graphic T-Shirt", "Good Times Regular Fit Graphic T-Shirt",
            "Icon Relaxed Fit Graphic T-Shirt", "Chill Spot Central Relaxed Fit Graphic T-Shirt (Off White)", "Driven by Adrenaline Regular Fit Graphic T-Shirt (Black)", "Easy Days Regular Fit T-Shirt (Light Blue)", "Need Cash Not Advice Regular Fit T-Shirt (Evening Sand)",
            "Regular Fit Crew Neck T-Shirt (Banana)", "Relaxed Fit Raglan T-Shirt with Embroidery (Black)", "Relaxed Fit T-Shirt with Branding Print (Off White)", "Relaxed Fit T-Shirt with PS Embroidery (Black)", "Relaxed Fit Short Sleeves Resort Shirt with All-Over Embroidery",
            "Relaxed Fit Short Sleeves Resort Shirt with Embroidery", "Relaxed Fit Striped Polo", "Tie Dye Resort Shirt", "Easy Iron Short Sleeve Shirt (Brown)", "Underground Relaxed Fit Graphic T-Shirt (Black)",
            "Men's Printed Muscle Tee", "Men's Ribbed Racer Back T-Shirt", "Fits 'Em All French Terry Vest", "Baekhyun Pullover Top", "Ultimate Basics Easy Fit Long Sleeve Shirt",
            "Relaxed Fit Long Sleeves Striped Shirt", "Dress Code Striped Long Sleeves Shirt (Seafoam Green)",

            "Casual Plain Color Block Embroidery Zipper T-Shirt", "Short Letter Pattern Slim Fit Printed T-Shirt", "Summer Square Neck Short Sleeve Slim Bottoming Knit Top", "Elegant Plain Split T-Shirt", "Sporty Plain Contrast Binding Crop T-Shirt",
            "Slubs V-Neck T-Shirt with Bunny Embro", "Better Made Graphic Shirt", "Pokemon Graphic Tee", "Cropped Plaid Resort Polo", "Oversized Graphic Tee",
            "Peasant Crop Top", "Puff Sleeve Top", "Boxy Resort Shirt", "Collared Top", "Plain Oversized Tee",
            "Cool Touch Crew Neck Short-Sleeved T-Shirt", "Women's Crop Top", "Cropped Logo Tee", "Denim Boxy Cropped Shirt", "Women's Graphic T-Shirt",
            "Women’s K-Heart T-Shirt", "Oversized Polo Shirt", "Women's Plain Tee", "Women's Polo Shirt", "Women's Statement Tee",
            "Stripped Short Sleeve Polo", "Women's Woven Top", "Basic Tee for Women", "Contrast Combination Printed Logo Top", "Embossed Logo Tee Roundneck",
            "Embroidered Logo Tee", "Fits 'Em All Oversized Polo Shirt", "Logo Tee for Women", "Logo T-Shirt for Women", "Modified Woven Blouse",
            "Modified Woven Blouse (Twill)", "Oversized Warm Beige Xline Graphic Tee", "Rhinestone Logo Tee", "Small Logo Tee", "T-Crop Blouse Round-Neck",
            "Xline Cropped Tee", "Xline Croptop", "Collared Shirt for Women", "Extended Sleeve Textured T-Shirt", "New Ultimate Basic T-Shirt",
            "Ruched Front T-Shirt", "Textured Tee for Women", "Textured Horizontal Striped Polo", "Ultimate Basics Knit Tee", "Cherry All-Over Printed Henley Regular Fit Top",
            "Jacquard Knit Regular Fit Polo", "If You Break It Slim Fit Graphic T-Shirt", "Regular Fit Waffle Knit Polo", "Vintage Summer Striped Relaxed Fit T-Shirt", "Relaxed Fit Stripes T-Shirt with Branding",
            "Ribbed Top", "Short Sleeve Regular Fit Baby Doll Top", "Slim Fit Polo with Contrast Tipping", "Slim Fit Textured Top", "Unleash Your Speed Loose Fit Graphic T-Shirt",
            "Sleeveless Tank Vest Solid Top", "Solid Ribbed Knit Tube Basic Crop Top", "Casual Pleated Crop Tank Top", "Flower Tube Top with Pads", "Sleeveless Top White",
            "Scoop Neck Tank Top", "Women's Sleeveless Top", "Fits 'Em All French Terry Vest", "High Neck Sleeveless Tops", "Denim Zip Up Vest",
            "Linen Top for Women", "Sleeveless Top with Tipping", "Textured Striped Sleeveless Top", "Ribbed Knit Slim Fit Tank Top", "Short Sleeves Top with Drawstring",
            "Sleeveless Stripes Peplum Top", "Woven Polo White", "Striped Long Sleeves Button Down Top with Heart Embroidery"
        };

        prices = new String[] {
            "₱259", "₱144", "₱176", "₱188", "₱199", "₱174", "₱170", "₱142", "₱599", "₱377",
            "₱349", "₱349", "₱349", "₱377", "₱699", "₱349", "₱384", "₱447", "₱349", "₱499",
            "₱447", "₱419", "₱899", "₱899", "₱599", "₱499", "₱899", "₱951", "₱890", "₱890",
            "₱890", "₱890", "₱890", "₱532", "₱890", "₱790", "₱890", "₱600", "₱890", "₱890",
            "₱700", "₱990", "₱499", "₱490", "₱1000", "₱900", "₱990", "₱990", "₱990", "₱990",
            "₱790", "₱550", "₱399", "₱399", "₱719", "₱989", "₱899", "₱799", "₱599", "₱899",
            "₱989", "₱849", "₱699", "₱749", "₱749", "₱759", "₱399", "₱499", "₱989", "₱989",
            "₱699", "₱799", "₱499", "₱899", "₱749", "₱899", "₱499", "₱899", "₱249", "₱379",
            "₱499", "₱398", "₱231", "₱252", "₱185", "₱218", "₱364", "₱331", "₱315", "₱999",
            "₱899", "₱799", "₱299", "₱278", "₱464", "₱279", "₱167", "₱855", "₱599", "₱989",
            "₱699", "₱729",

            "₱282", "₱200", "₱129", "₱330", "₱549", "₱211", "₱321", "₱349", "₱699", "₱349",
            "₱699", "₱699", "₱899", "₱419", "₱230", "₱230", "₱279", "₱265", "₱899", "₱349",
            "₱349", "₱321", "₱244", "₱349", "₱307", "₱699", "₱799", "₱690", "₱790", "₱790",
            "₱690", "₱877", "₱690", "₱790", "₱890", "₱890", "₱690", "₱690", "₱690", "₱345",
            "₱350", "₱350", "₱899", "₱429", "₱699", "₱710", "₱699", "₱989", "₱439", "₱599",
            "₱699", "₱185", "₱366", "₱398", "₱499", "₱399", "₱899", "₱599", "₱349", "₱599",
            "₱159", "₱179", "₱177", "₱109", "₱160", "₱104", "₱160", "₱900", "₱590", "₱749",
            "₱809", "₱890", "₱699", "₱349", "₱899", "₱899", "₱990", "₱849"
        };

        tags = new String[] {
            "BNY", "BNY", "BNY", "BNY", "BNY","BNY", "BNY", "BNY", "Bench", "Bench",
            "Bench", "Bench", "Bench", "Bench", "Bench","Bench", "Bench", "Bench", "Bench", "Bench",
            "Bench", "Bench", "Bench", "Bench", "Bench","Bench", "Bench", "Lee", "Lee", "Lee",
            "Lee", "Lee", "Lee", "Lee", "Lee","Lee", "Lee", "Lee", "Lee", "Lee",
            "Lee", "Lee", "Lee", "Lee", "Lee","Lee", "Lee", "Lee", "Lee", "Lee",
            "Lee", "Lee", "Memo", "Memo", "Memo","Memo", "Memo", "Memo", "Memo", "Memo",
            "Memo", "Memo", "Memo", "Memo", "Memo","Memo", "Memo", "Memo", "Memo", "Memo",
            "Memo", "Memo", "Memo", "Memo", "Memo","Memo", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe",
            "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe","Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe",
            "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe","Bench", "Bench", "Lee", "Bench", "Memo",
            "Penshoppe", "Penshoppe",

            "BNY", "BNY", "BNY", "BNY", "BNY", "BNY", "BNY", "BNY", "BNY", "BNY",
            "Bench", "Bench", "Bench", "Bench", "Bench", "Bench", "Bench", "Bench", "Bench", "Bench",
            "Bench", "Bench", "Bench", "Bench", "Bench", "Bench", "Bench", "Bench", "Bench", "Bench",
            "Bench", "Bench", "Bench", "Bench", "Lee", "Lee", "Lee", "Lee", "Lee", "Lee",
            "Lee", "Lee", "Lee", "Lee", "Lee", "Lee", "Lee", "Lee", "Lee", "Lee",
            "Lee", "Lee", "Memo", "Memo", "Memo", "Memo", "Memo", "Memo", "Memo", "Memo",
            "Memo", "Memo", "Memo", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe",
            "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe", "Penshoppe"
        };

        icons = new String[] {
            "images/bnymen2tops.png", "images/bnymen7tops.png", "images/bnymen8tops.png", "images/bnymen9tops.png", "images/bnymen12tops.png",
            "images/bnymen19tops.png", "images/bnymen23tops.png", "images/bnymen28tops.png", "images/benchmen2tops.png", "images/benchmen3tops.png",
            "images/benchmen4tops.png", "images/benchmen5tops.png", "images/benchmen6tops.png", "images/benchmen7tops.png", "images/benchmen9tops.png",
            "images/benchmen10tops.png", "images/benchmen11tops.png", "images/benchmen12tops.png", "images/benchmen15tops.png", "images/benchmen17tops.png",
            "images/benchmen20tops.png", "images/benchmen21tops.png", "images/benchmen22tops.png", "images/benchmen23tops.png", "images/benchmen24tops.png",
            "images/benchmen25tops.png", "images/benchmen26tops.png", "images/leemen1tops.png", "images/leemen3tops.png", "images/leemen4tops.png",
            "images/leemen5tops.png", "images/leemen6tops.png", "images/leemen7tops.png", "images/leemen11tops.png", "images/leemen12tops.png",
            "images/leemen13tops.png", "images/leemen14tops.png", "images/leemen15tops.png", "images/leemen16tops.png", "images/leemen18tops.png",
            "images/leemen19tops.png", "images/leemen20tops.png", "images/leemen21tops.png", "images/leemen22tops.png", "images/leemen23tops.png",
            "images/leemen24tops.png", "images/leemen25tops.png", "images/leemen26tops.png", "images/leemen27tops.png", "images/leemen28tops.png",
            "images/leemen29tops.png", "images/leemen30tops.png", "images/memomen1tops.png", "images/memomen2tops.png", "images/memomen3tops.png",
            "images/memomen4tops.png", "images/memomen6tops.png", "images/memomen7tops.png", "images/memomen8tops.png", "images/memomen9tops.png",
            "images/memomen10tops.png", "images/memomen11tops.png", "images/memomen12tops.png", "images/memomen13tops.png", "images/memomen14tops.png",
            "images/memomen15tops.png", "images/memomen18tops.png", "images/memomen19tops.png", "images/memomen20tops.png", "images/memomen21tops.png",
            "images/memomen22tops.png", "images/memomen23tops.png", "images/memomen25tops.png", "images/memomen27tops.png", "images/memomen28tops.png",
            "images/memomen30tops.png", "images/penshoppemen2tops.png", "images/penshoppemen3tops.png", "images/penshoppemen4tops.png", "images/penshoppemen5tops.png",
            "images/penshoppemen6tops.png", "images/penshoppemen7tops.png", "images/penshoppemen9tops.png", "images/penshoppemen10tops.png", "images/penshoppemen12tops.png",
            "images/penshoppemen14tops.png", "images/penshoppemen16tops.png", "images/penshoppemen17tops.png", "images/penshoppemen18tops.png", "images/penshoppemen23tops.png",
            "images/penshoppemen24tops.png", "images/penshoppemen25tops.png", "images/penshoppemen26tops.png", "images/penshoppemen28tops.png", "images/penshoppemen29tops.png",
            "images/benchmen18tops.png", "images/benchmen19tops.png", "images/leemen8tops.png", "images/benchmen1tops.png", "images/memomen26tops.png",
            "images/penshoppemen21tops.png", "images/penshoppemen27tops.png",

            "images/bnywomen2tops.png", "images/bnywomen11tops.png", "images/bnywomen15tops.png", "images/bnywomen21tops.png", "images/bnywomen22tops.png",
            "images/bnywomen27tops.png", "images/benchwomen2tops.png", "images/benchwomen4tops.png", "images/benchwomen6tops.png", "images/benchwomen7tops.png",
            "images/benchwomen8tops.png", "images/benchwomen9tops.png", "images/benchwomen12tops.png", "images/benchwomen13tops.png", "images/benchwomen14tops.png",
            "images/benchwomen15tops.png", "images/benchwomen16tops.png", "images/benchwomen17tops.png", "images/benchwomen18tops.png", "images/benchwomen20tops.png",
            "images/benchwomen21tops.png", "images/benchwomen23tops.png", "images/benchwomen24tops.png", "images/benchwomen25tops.png", "images/benchwomen28tops.png",
            "images/benchwomen29tops.png", "images/benchwomen30tops.png", "images/leewomen2tops.png", "images/leewomen4tops.png", "images/leewomen7tops.png",
            "images/leewomen8tops.png", "images/leewomen11tops.png", "images/leewomen14tops.png", "images/leewomen15tops.png", "images/leewomen16tops.png",
            "images/leewomen17tops.png", "images/leewomen18tops.png", "images/leewomen19tops.png", "images/leewomen20tops.png", "images/leewomen21tops.png",
            "images/leewomen28tops.png", "images/leewomen29tops.png", "images/memowomen2tops.png", "images/memowomen5tops.png", "images/memowomen9tops.png",
            "images/memowomen14tops.png", "images/memowomen24tops.png", "images/memowomen25tops.png", "images/memowomen28tops.png", "images/penshoppewomen2tops.png",
            "images/penshoppewomen3tops.png", "images/penshoppewomen8tops.png", "images/penshoppewomen11tops.png", "images/penshoppewomen14tops.png", "images/penshoppewomen16tops.png",
            "images/penshoppewomen18tops.png", "images/penshoppewomen19tops.png", "images/penshoppewomen22tops.png", "images/penshoppewomen23tops.png", "images/penshoppewomen27tops.png",
            "images/bnywomen1tops.png", "images/bnywomen13tops.png", "images/bnywomen19tops.png", "images/bnywomen26tops.png", "images/benchwomen5tops.png",
            "images/benchwomen10tops.png", "images/benchwomen27tops.png", "images/leewomen9tops.png", "images/leewomen12tops.png", "images/memowomen4tops.png",
            "images/memowomen7tops.png", "images/memowomen17tops.png", "images/memowomen23tops.png", "images/penshoppewomen17tops.png", "images/penshoppewomen20tops.png",
            "images/penshoppewomen21tops.png", "images/leewomen27tops.png", "images/penshoppewomen26tops.png"
        };

        brandSize = new int[][] {
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {110, 100}, {110, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {110, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {90, 100}, {80, 100}, {80, 100},
            {80, 100}, {80, 100}, {100, 100}, {90, 100}, {80, 100}, {80, 100}, {80, 100}, {80, 100}, {100, 100}, {80, 100},
            {80, 100}, {80, 100}, {80, 100}, {80, 100}, {90, 100}, {80, 100}, {80, 100}, {80, 100}, {80, 100}, {80, 100},
            {80, 100}, {100, 100}, {100, 100}, {80, 100}, {80, 100}, {90, 100}, {80, 100}, {80, 100}, {80, 100}, {90, 100},
            {100, 100}, {100, 100}, {100, 100}, {80, 100}, {100, 100}, {80, 100}, {100, 100}, {100, 100}, {90, 100}, {100, 100},
            {100, 100}, {100, 100}, {80, 100}, {100, 100}, {100, 100}, {80, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {80, 100}, {100, 100}, {80, 100},
            {100, 100}, {100, 100},

            {110, 100}, {100, 100}, {110, 100}, {110, 100}, {110, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {110, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {80, 100}, {80, 100}, {80, 100},
            {100, 100}, {100, 100}, {80, 100}, {100, 100}, {80, 100}, {80, 100}, {100, 100}, {80, 100}, {90, 100}, {80, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {110, 100}, {100, 100}, {100, 100}, {80, 100}, {100, 100}, {100, 100},
            {100, 100}, {110, 100}, {110, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {110, 100}, {100, 100},
            {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100}, {100, 100},
            {80, 100}, {100, 100}, {100, 100}, {90, 100}, {90, 100}, {100, 100}, {80, 100}, {100, 100}
        };

        stocks = new int[] {
            0, 1500, 5760, 3200, 500, 7800, 12000, 300, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 10000, 2000, 500,
            700, 2500, 800, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 3000, 4500, 6000, 1500, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        // Initialize the stock map
        for (int i = 0; i < items.length; i++) {
            itemStocks.put(items[i], stocks[i]);
        }

        for (int i = 0; i < items.length; i++) {
            if (i < 102) {
                menPanel.add(createTopsItem(items[i], prices[i], tags[i], icons[i], brandSize[i][0], brandSize[i][1], itemStocks.get(items[i])));
            } else {
                womenPanel.add(createTopsItem(items[i], prices[i], tags[i], icons[i], brandSize[i][0], brandSize[i][1], itemStocks.get(items[i])));
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

    // Method to decrease stock
    public synchronized void decreaseStock(String itemName, int quantity) {
    if (itemStocks.containsKey(itemName)) {
        int currentStock = itemStocks.get(itemName);
        int newStock = Math.max(0, currentStock - quantity); // Ensure stock doesn't go negative
        itemStocks.put(itemName, newStock);
        
        // Update the stocks array for consistency
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(itemName)) {
                stocks[i] = newStock;
                break;
            }
        }
    }
}

    // Method to place the order and decrease stock
    public void placeOrder() {
        if (MainContents.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (MainContents.CartItem cartItem : MainContents.getCartItems()) {
            String itemName = cartItem.getName();
            int quantity = cartItem.getQuantity();
            int currentStock = itemStocks.getOrDefault(itemName, 0);

            // Check if there's enough stock before decreasing
            if (quantity > currentStock) {
                JOptionPane.showMessageDialog(null, "Not enough stock for " + itemName + "! Available stock: " + currentStock, "Stock Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop the order process if stock is insufficient
            }
        }

        // If all items have sufficient stock, decrease stock for each item
        for (MainContents.CartItem cartItem : MainContents.getCartItems()) {
            decreaseStock(cartItem.getName(), cartItem.getQuantity());
        }

        // Clear the cart after successfully placing the order
        MainContents.getCartItems().clear();
        refreshStockDisplay(); // Refresh the display to show updated stock
        JOptionPane.showMessageDialog(null, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to refresh the display
    public synchronized void refreshStockDisplay() {
    SwingUtilities.invokeLater(() -> {
        menPanel.removeAll();
        womenPanel.removeAll();

        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            String price = prices[i];
            String tag = tags[i];
            String icon = icons[i];
            int width = brandSize[i][0];
            int height = brandSize[i][1];
            int stock = itemStocks.getOrDefault(item, stocks[i]); // Fallback to stocks array if needed

            if (i < 102) {  // Use constant instead of magic number
                menPanel.add(createTopsItem(item, price, tag, icon, width, height, stock));
            } else {
                womenPanel.add(createTopsItem(item, price, tag, icon, width, height, stock));
            }
        }

        menPanel.revalidate();
        menPanel.repaint();
        womenPanel.revalidate();
        womenPanel.repaint();
    });
}

    private JPanel createTopsItem(String item, String price, String tag, String icon, int width, int height, int stock) {
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

        ImageIcon itemIcon = null;
        try {
            itemIcon = new ImageIcon(getClass().getResource("/" + icon));
            if (itemIcon.getIconWidth() == -1) {
                itemIcon = null;
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + icon);
            e.printStackTrace();
        }

        JLabel iconLabel;
        if (itemIcon != null) {
            Image scaledImage = itemIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            iconLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            iconLabel = new JLabel("No Image");
            iconLabel.setFont(itemFont);
            iconLabel.setForeground(black);
            iconLabel.setPreferredSize(new Dimension(width, height));
        }
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

            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, stock > 0 ? stock : 1, 1); // Limit quantity to available stock
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
                
                // Check if there's enough stock
                if (selectedQuantity > stock) {
                    JOptionPane.showMessageDialog(null, "Not enough stock available! Available stock: " + stock, "Stock Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                MainContents.CartItem newItem = new MainContents.CartItem(item, price, selectedSize, icon, selectedQuantity, width, height);
                MainContents.getCartItems().add(newItem);

                UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.PLAIN, 12));
                JOptionPane.showMessageDialog(null, item + " added to cart! \nSize: " + selectedSize + "\nQuantity: " + selectedQuantity);
                if (onViewCartClick != null) {
                    onViewCartClick.run();
                }
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

        // Create a container for tag and stock at the top
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add tag label on the top left
        if (!tag.isEmpty()) {
            JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            tagPanel.setBackground(peach);
            tagPanel.setOpaque(true);
            tagPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

            JLabel tagLabel = new JLabel(tag);
            tagLabel.setFont(itemFont);
            tagLabel.setForeground(black);
            tagPanel.add(tagLabel);

            Dimension labelSize = tagLabel.getPreferredSize();
            tagPanel.setPreferredSize(new Dimension(labelSize.width + 10, labelSize.height + 6));

            topContainer.add(tagPanel, BorderLayout.WEST);
        }

        // Add stock label on the top right
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        stockPanel.setOpaque(false);
        JLabel stockLabel = new JLabel(stock == 0 ? "Out of Stock" : "Stocks: " + stock);
        stockLabel.setFont(itemFont.deriveFont(15f));
        stockLabel.setForeground(black);
        stockPanel.add(stockLabel);
        topContainer.add(stockPanel, BorderLayout.EAST);

        pricePanel.add(priceLabel, BorderLayout.WEST);
        buttonWrapper.add(addButton);
        pricePanel.add(buttonWrapper, BorderLayout.EAST);
        itemDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemDetailsPanel.add(iconLabel);
        itemDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        itemDetailsPanel.add(namePane);
        itemDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemDetailsPanel.add(pricePanel);
        itemPanel.add(topContainer, BorderLayout.NORTH);
        itemPanel.add(itemDetailsPanel, BorderLayout.CENTER);

        return itemPanel;
    }
}
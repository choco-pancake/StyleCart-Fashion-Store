import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomePage extends JFrame {
    private BufferedImage logo;
    private Font customFont, customFont2;
    private JPanel sidePanel, logoPanel, menuItemPanel, contentPanel, moneyPanel;
    private JLabel logoLabel, logoLabel2, logoImageLabel, numberLabel, moneyLabel;
    private CardLayout cardLayout;
    private Map<String, JPanel> menuItemMap = new HashMap<>();
    private Map<String, ImageIcon[]> iconMap = new HashMap<>();
    private OrderSummaryPage orderSummaryPage;
    private ShoppingCartPage cartPage;
    private RatingsPage ratingsPage;
    private HelpPage helpPage;

    private Color white = Color.WHITE;
    private Color gray = new Color(56, 56, 56);
    private Color brown = new Color(138, 109, 70);
    private Color lightBrown = new Color(198, 169, 130);
    private Color black = Color.BLACK;

    public HomePage() {
        setTitle("Home");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        try {
            logo = ImageIO.read(new File("images/logo.jpg"));
            setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(30f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);

            customFont2 = customFont.deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.ITALIC, 40);
            customFont2 = customFont.deriveFont(20f);
        }

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(250, getHeight()));
        sidePanel.setBackground(brown);
        sidePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        ImageIcon logoIcon = getScaledIcon("images/logo.jpg", 55, 55);

        logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);

        logoLabel = new JLabel("StyleCart", SwingConstants.CENTER);
        logoLabel.setFont(customFont);
        logoLabel.setForeground(white);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoLabel2 = new JLabel("FASHION STORE", SwingConstants.CENTER);
        logoLabel2.setFont(customFont2);
        logoLabel2.setForeground(white);
        logoLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoImageLabel = new JLabel(logoIcon);
        logoImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoImageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        makeComponentClickable(logoPanel, () -> {
            menuItemWhite("Home");
            cardLayout.show(contentPanel, "MainView");
        });

        makeComponentClickable(logoLabel, () -> {
            menuItemWhite("Home");
            cardLayout.show(contentPanel, "MainView");
        });

        makeComponentClickable(logoImageLabel, () -> {
            menuItemWhite("Home");
            cardLayout.show(contentPanel, "MainView");
        });

        logoPanel.add(logoImageLabel);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createVerticalStrut(2));
        logoPanel.add(logoLabel2);
        logoPanel.add(Box.createVerticalStrut(60));
        sidePanel.add(logoPanel);

        String[][] menuItems = {
            {"Home", "images/brownhouse.png, images/whitehouse.png"},
            {"Shopping Cart", "images/browncart.png, images/whitecart.png"},
            {"Order Summary", "images/brownorder.png, images/whiteorder.png"},
            {"Ratings", "images/brownstar.png, images/whitestar.png"},
            {"Help", "images/brownquestion.png, images/whitequestion.png"}
        };

        for (String[] item : menuItems) {
            String name = item[0];
            String[] iconPaths = item[1].split(", ");
            String blackIconPath = iconPaths[0];
            String whiteIconPath = iconPaths[1];

            ImageIcon blackIcon = getScaledIcon(blackIconPath, 30, 30);
            ImageIcon whiteIcon = getScaledIcon(whiteIconPath, 30, 30);
            iconMap.put(name, new ImageIcon[]{blackIcon, whiteIcon});

            menuItemPanel = createMenuItemPanel(name);
            sidePanel.add(menuItemPanel);
            sidePanel.add(Box.createRigidArea(new Dimension(0, 50)));
        
            if(name.equals("Help")) {
                moneyPanel = new RoundedPanel(lightBrown, 30);
                moneyPanel.setLayout(new BoxLayout(moneyPanel, BoxLayout.Y_AXIS));
                moneyPanel.setBorder(new EmptyBorder(20, 10, 10, 10));
                moneyPanel.setMaximumSize(new Dimension(210, 100));
                moneyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                moneyLabel = new JLabel("Current balance:");
                moneyLabel.setFont(customFont.deriveFont(18f));
                moneyLabel.setForeground(black);
                moneyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                numberLabel = new JLabel("$5,000.00");
                numberLabel.setFont(customFont.deriveFont(25f));
                numberLabel.setForeground(black);
                numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                moneyPanel.add(moneyLabel);
                moneyPanel.add(Box.createVerticalStrut(10));
                moneyPanel.add(numberLabel);
                sidePanel.add(moneyPanel);
                sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(245, 245, 245));

        orderSummaryPage = new OrderSummaryPage(); 
        ratingsPage = new RatingsPage(); 
        helpPage = new HelpPage();

        cartPage = new ShoppingCartPage(this, () -> {
            menuItemWhite("Home");
            cardLayout.show(contentPanel, "MainView");
        }, orderSummaryPage, ratingsPage, customFont); 

        MainContents homeContent = new MainContents();
        homeContent.setOnBrandViewClick(() -> {
            boolean bnyExists = false;
            for (Component comp : contentPanel.getComponents()) {
                if (comp instanceof BNY) {
                    bnyExists = true;
                    break;
                }
            }
            if (!bnyExists) {
                BNY bnyPanel = new BNY(cardLayout, contentPanel, new Color(173, 216, 230), Color.WHITE, new Color(0, 0, 139), Color.BLACK, new Color(211, 211, 211), new Color(169, 169, 169), new Color(0, 0, 0, 0), customFont.deriveFont(16f), customFont.deriveFont(16f));
                contentPanel.add(bnyPanel, "BNY");
            }
            cardLayout.show(contentPanel, "BNY");
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        homeContent.setOnViewCartClick(() -> {
            cartPage.showCart();
            menuItemWhite("Shopping Cart");
            cardLayout.show(contentPanel, "CartView");
        });
        
        contentPanel.add(homeContent, "MainView");
        contentPanel.add(cartPage, "CartView");
        contentPanel.add(orderSummaryPage, "OrderSummaryView");
        contentPanel.add(ratingsPage, "RatingsView");
        contentPanel.add(helpPage, "HelpView");

        sidePanel.add(Box.createVerticalGlue());
        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        menuItemWhite("Home");
        cardLayout.show(contentPanel, "MainView");

        setVisible(true);
    }

    class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int cornerRadius = 15;

        public RoundedPanel(Color bgColor, int radius) {
            this.backgroundColor = bgColor;
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            Graphics2D graphics2d = (Graphics2D) g;
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setColor(backgroundColor);
            graphics2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcs.width, arcs.height);
        }
    }

    public void updateBalance(double newBalance) {
        if (numberLabel != null) {
            numberLabel.setText(String.format("$%,.2f", newBalance));
        }
    }

    private JPanel createMenuItemPanel(String text) {
        JPanel menuItemPanel = new JPanel();
        menuItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        menuItemPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 0);

        ImageIcon[] icons = iconMap.get(text);
        ImageIcon blackIcon = icons[0];

        JLabel sideLabel = new JLabel(text, blackIcon, JLabel.LEFT);
        sideLabel.setFont(customFont);
        sideLabel.setFont(new Font(null, Font.PLAIN, 17));
        sideLabel.setForeground(white);
        sideLabel.setBorder(new EmptyBorder(0, 0, 0, 0));

        menuItemPanel.add(sideLabel, gbc);
        menuItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, sideLabel.getPreferredSize().height));
        menuItemPanel.setPreferredSize(sideLabel.getPreferredSize());

        Runnable action = () -> {
            menuItemWhite(text);
            switch (text) {
                case "Home":
                    cardLayout.show(contentPanel, "MainView");
                    break;
                case "Shopping Cart":
                    cartPage.showCart();
                    menuItemWhite("Shopping Cart");
                    cardLayout.show(contentPanel, "CartView");
                    break;
                case "Order Summary":
                    cardLayout.show(contentPanel, "OrderSummaryView");
                    break;
                case "Ratings":
                    cardLayout.show(contentPanel, "RatingsView");
                    break;
                case "Help":
                    cardLayout.show(contentPanel, "HelpView");
                    break;
            }
        };

        makeComponentClickable(menuItemPanel, action);
        makeComponentClickable(sideLabel, action);

        menuItemMap.put(text, menuItemPanel);
        return menuItemPanel;
    }

    private void makeComponentClickable(JComponent component, Runnable action) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }

    private ImageIcon getScaledIcon(String path, int width, int height) {
        try {
            BufferedImage original = ImageIO.read(new File(path));
            BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = scaled.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.drawImage(original, 0, 0, width, height, null);
            graphics2D.dispose();
            return new ImageIcon(scaled);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void menuItemWhite(String selectedItem) {
        for (Map.Entry<String, JPanel> entry : menuItemMap.entrySet()) {
            JPanel entryPanel = entry.getValue();
            JLabel entryLabel = (JLabel) entryPanel.getComponent(0);

            if (entry.getKey().equals(selectedItem)) {
                entryPanel.setBackground(white);
                entryPanel.setOpaque(true);
                entryLabel.setForeground(gray);
                entryLabel.setIcon(iconMap.get(entry.getKey())[1]);
                entryPanel.setPreferredSize(entryLabel.getPreferredSize());
                entryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, entryLabel.getPreferredSize().height));
            } else {
                entryPanel.setBackground(null);
                entryPanel.setOpaque(false);
                entryLabel.setForeground(white);
                entryLabel.setIcon(iconMap.get(entry.getKey())[0]);
            }
        }

        sidePanel.revalidate();
        sidePanel.repaint();
    }

    public void showPanel(String name) {
        cardLayout.show(contentPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new HomePage();
        });
    }
}
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
    private Font customFont;
    private Font customFont2;
    private JPanel sidePanel;
    private JPanel logoPanel;
    private JLabel logoLabel;
    private JLabel logoLabel2;
    private JLabel logoImageLabel;
    private JPanel menuItemPanel;
    private JPanel contentPanel;
    private Map<String, JPanel> menuItemMap = new HashMap<>();
    private Map<String, ImageIcon[]> iconMap = new HashMap<>();

    private Color white = Color.WHITE;
    private Color gray = new Color(56, 56, 56);
    private Color brown = new Color(138, 109, 70);

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
            showHomePage();
        });

        makeComponentClickable(logoLabel, () -> {
            menuItemWhite("Home");
            showHomePage();
        });

        makeComponentClickable(logoImageLabel, () -> {
            menuItemWhite("Home");
            showHomePage();
        });

        logoPanel.add(logoImageLabel);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createVerticalStrut(2));
        logoPanel.add(logoLabel2);
        logoPanel.add(Box.createVerticalStrut(100));
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
        }

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));

        sidePanel.add(Box.createVerticalGlue());
        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        menuItemWhite("Home");
        showHomePage();

        setVisible(true);
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
                    showHomePage();
                    break;
                case "Shopping Cart":
                    showShoppingCartPage();
                    break;
                case "Order Summary":
                    showOrderSummaryPage();
                    break;
                case "Ratings":
                    showRatingsPage();
                    break;
                case "Help":
                    showHelpPage();
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

    private void showHomePage() {
        contentPanel.removeAll();
        MainContents homeContent = new MainContents();

        homeContent.setonBrandViewClick(() -> {
            showAvailableBrands();
        });

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(homeContent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showShoppingCartPage() {
        contentPanel.removeAll();
        ShoppingCartPage cartPage = new ShoppingCartPage(() -> {
            showAvailableBrands();
        });

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(cartPage, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showOrderSummaryPage() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Order Summary"));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showRatingsPage() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Ratings"));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showHelpPage() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Help and Support"));
        contentPanel.revalidate();
        contentPanel.repaint();
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

    private void menuItemWhite(String selectedItem) {
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

    private void showAvailableBrands() {
        contentPanel.removeAll();
        AvailableBrands brandsPanel = new AvailableBrands();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(brandsPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
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
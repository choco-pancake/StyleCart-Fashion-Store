import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AvailableBrands extends JPanel {
    private JPanel brandPanel, titlePanel, brandItemsPanel;
    private JLabel titleLabel;
    private JScrollPane scrollPane;
    private Font customFont;
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color black = Color.BLACK;
    private Color lightGray = new Color(211, 211, 211);
    private Color darkGray = new Color(169, 169, 169);
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JButton backButton;

    public AvailableBrands(CardLayout cardLayout, JPanel contentPanel) {
        setLayout(new BorderLayout());

        this.cardLayout = cardLayout;
        this.contentPanel = contentPanel;

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);
        } catch(FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.ITALIC, 40);
        }

        brandPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, darkBlue, 0, getHeight(), lightBlue);
                graphics2d.setPaint(gradient);
                graphics2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        brandPanel.setLayout(new BorderLayout());
        brandPanel.setOpaque(false);

        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray),
                            BorderFactory.createEmptyBorder(20, 20, 20, 0)));
        titlePanel.setBackground(darkBlue);
        titlePanel.setOpaque(true);

        backButton = new JButton("←") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(darkGray.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(darkGray);
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillOval(0, 0, getWidth(), getHeight());

                super.paintComponent(g);
                g2.dispose();
            }
        
            @Override
            public boolean contains(int x, int y) {
                double radius = getWidth() / 2;
                return Math.pow(x - radius, 2) + Math.pow(y - radius, 2) <= radius * radius;
            }
        };

        backButton.setPreferredSize(new Dimension(40, 40));
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(black);
        backButton.setFont(customFont.deriveFont(30f));
        backButton.setBackground(lightGray);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        backButton.setPreferredSize(new Dimension(35, 35));
        backButton.setOpaque(false);

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setForeground(white); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setForeground(black); 
            }
        });

        backButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "MainView");
        });

        titleLabel = new JLabel(" Available Brands");
        titleLabel.setFont(customFont);
        titleLabel.setForeground(white);
        titleLabel.setOpaque(false);

        titlePanel.add(backButton);
        titlePanel.add(titleLabel);

        scrollPane = new JScrollPane(brandPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        String[] brand = {"Adidas", "COS", "Everlane", "Mango", "Aritzia", "Nike", "Zara", "Uniqlo", "H&M", "Telfar"};
        String[] icon = {"images/adidas.png", "images/cos.png", "images/everlane.png", "images/mango.png", "images/aritzia.png",
                         "images/nike.png", "images/zara.png", "images/uniqlo.png", "images/h&m.png", "images/telfar.png"};
        String[] description = {" Shorts, singlets, shirts, tees, jerseys, pants, tights, tank tops, bras, high-rise tanks, skirts, hoodies",
                          " Shirts, tops, tees, polos, v-neck shirts, trousers, pants, jeans, dresses",
                          " Tees, shirts, tanks, blouses, pants, cropped pants, jeans, joggers, trousers, leggings, dresses, sweaters, pullovers",
                          " Blouses, shirts, tanks, polos, tops, vests, trousers, leggings, joggers, pants, shorts, slacks, dresses, hoodies",
                          " Tanks, blouses, tees, long sleeves, tops, dresses, slip-tops, zip-ups",
                          " Shirts, shorts, hoodies, polos, cropped tops, leggings, trousers, zip-tops, jackets",
                          " Shirts, blazers, collections, dresses",
                          " Shirts, pants, jeans, shorts, blouses, dresses, cardigans, parkas",
                          " Shirts, joggers, shorts, jeans, sweatshirts, hoodies, tops, skirts, dresses, jackets, dungarees",
                          " Shirts, trousers, shorts, maxi-skirts, sweatskirts, maxi dresses, hoodies, tops, tunics, jackets, blazers, coats"};
        int[][] size = {{130, 90}, {150, 140}, {165, 40}, {150, 100}, {180, 90},
                      {140, 100}, {105, 45}, {100, 100}, {100, 80}, {100, 80}};

        brandItemsPanel = new JPanel();
        brandItemsPanel.setLayout(new BoxLayout(brandItemsPanel, BoxLayout.Y_AXIS));
        brandItemsPanel.setBorder(BorderFactory.createCompoundBorder(
                                      BorderFactory.createLineBorder(lightGray, 1),
                                      BorderFactory.createEmptyBorder(20, 20, 0, 20)));
        brandItemsPanel.setOpaque(false);

        for(int i = 0; i < brand.length; i++) {
            brandItemsPanel.add(createBrandItem(brand[i], icon[i], description[i], size[i][0], size[i][1]));
            brandItemsPanel.add(Box.createVerticalStrut(20));
        }

        brandPanel.add(brandItemsPanel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBrandItem(String brandName, String brandIcon, String description, int width, int height) {
        JPanel brandItemPanel = new JPanel();
        brandItemPanel.setLayout(new BorderLayout());
        brandItemPanel.setBackground(lightBlue);
        brandItemPanel.setBorder(BorderFactory.createCompoundBorder(
                                 BorderFactory.createLineBorder(lightGray, 1),
                                 BorderFactory.createEmptyBorder(10, 10, 0, 10)));
        brandItemPanel.setPreferredSize(new Dimension(1230, 130));

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(200, 130));
        ImageIcon icon = new ImageIcon(brandIcon);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 20));
        iconLabel.setHorizontalAlignment(JLabel.LEFT);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel nameLabel = new JLabel(brandName);
        nameLabel.setFont(customFont.deriveFont(25f));
        nameLabel.setForeground(darkBlue);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(customFont.deriveFont(15f));
        descriptionLabel.setForeground(black);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descriptionLabel);
        imagePanel.add(iconLabel, BorderLayout.WEST);
        brandItemPanel.add(imagePanel, BorderLayout.WEST);
        brandItemPanel.add(textPanel, BorderLayout.CENTER);
        brandItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return brandItemPanel;
    }
}
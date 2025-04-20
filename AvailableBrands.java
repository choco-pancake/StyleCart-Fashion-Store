import javax.swing.*;
import java.awt.*;
import java.io.*;

public class AvailableBrands extends JPanel{
    private Font customFont;
    private JPanel brandPanel, titlePanel, brandItemsPanel;
    private JLabel titleLabel;
    private JScrollPane scrollPane;
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color lightGray = new Color(211, 211, 211);
    private Color white = Color.WHITE;
    private Color black = Color.black;

    public AvailableBrands() {
        setLayout(new BorderLayout());

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
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createCompoundBorder
                            (BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray),
                            (BorderFactory.createEmptyBorder(20, 20, 20, 0))));
        titlePanel.setBackground(darkBlue);
        titlePanel.setOpaque(true);

        titleLabel = new JLabel(" Available Brands");
        titleLabel.setFont(customFont);
        titleLabel.setForeground(white);
        titleLabel.setOpaque(false);

        scrollPane = new JScrollPane(brandPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        String[] brand = {"Adidas\nShorts, singlets, shirts, tees, jerseys, pants, tights, tank tops, bras, high-rise tanks, skirts, hoodies",
                          "COS\nShirts, tops, tees, polos, v-neck shirts, trousers, pants, jeans, dresses",
                          "Everlane\nTees, shirts, tanks, blouses, pants, cropped pants, jeans, joggers, trousers, leggings, dresses, sweaters, pullovers",
                          "Mango\nBlouses, shirts, tanks, polos, tops, vests, trousers, leggings, joggers, pants, shorts, slacks, dresses, hoodies",
                          "Aritzia\nTanks, blouses, tees, long sleeves, tops, dresses, slip-tops, zip-ups",
                          "Nike\nShirts, shorts, hoodies, polos, cropped tops, leggings, trousers, zip-tops, jackets",
                          "Zara\nShirts, blazers, collections, dresses",
                          "Uniqlo\nShirts, pants, jeans, shorts, blouses, dressses, cardigans, parkas",
                          "H&M\nShirts, joggers, shorts, jeans, sweatshirts, hoodies, tops, skitrs, dresses, jackets, dungarees",
                          "Telfar\nShirts, trousers, shorts, maxi-skirts, sweatskirts, maxi dresses, hoodies, tops, tunics, jackets, blazers, coats"};
        String[] icon = {"images/adidas.png", "images/cos.png", "images/everlane.png", "images/mango.png", "images/aritzia.png",
                         "images/nike.png", "images/zara.png", "images/uniqlo.png", "images/h&m.png", "images/telfar.png"};
        int[][] size = {{130, 90}, {150, 140}, {165, 40}, {150, 100}, {180, 90},
                      {140, 100}, {105, 45}, {100, 100}, {100, 80}, {100, 80}};

        brandItemsPanel = new JPanel();
        brandItemsPanel.setLayout(new BoxLayout(brandItemsPanel, BoxLayout.Y_AXIS));
        brandItemsPanel.setBorder(BorderFactory.createCompoundBorder(
                                      BorderFactory.createLineBorder(lightGray, 1),
                                      BorderFactory.createEmptyBorder(20, 20, 40, 20)));
        brandItemsPanel.setOpaque(false);

        for(int i = 0; i < brand.length; i++) {
            brandItemsPanel.add(createBrandItem(brand[i], icon[i], size[i][0], size[i][1]));
            brandItemsPanel.add(Box.createVerticalStrut(20));
        }

        titlePanel.add(titleLabel, BorderLayout.WEST);
        brandPanel.add(brandItemsPanel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBrandItem(String brandName, String brandIcon, int width, int height) {
        JPanel brandItemPanel = new JPanel();
        brandItemPanel.setLayout(new BorderLayout());
        brandItemPanel.setBackground(lightBlue);
        brandItemPanel.setBorder(BorderFactory.createCompoundBorder(
                                 BorderFactory.createLineBorder(lightGray, 1),
                                 BorderFactory.createEmptyBorder(10, 10, 0, 10)));
        brandItemPanel.setPreferredSize(new Dimension(1230, 130));

        ImageIcon icon = new ImageIcon(brandIcon);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 20));
        JLabel nameLabel = new JLabel(brandName);
        nameLabel.setFont(customFont.deriveFont(20f));
        nameLabel.setForeground(black);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandItemPanel.add(iconLabel, BorderLayout.WEST);
        brandItemPanel.add(nameLabel, BorderLayout.CENTER);
        brandItemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return brandItemPanel;
    }
}

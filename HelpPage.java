import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class HelpPage extends JPanel {
    private Font customFont;
    private JPanel helpPanel, faqPanel, questionPanel, q1Panel, q2Panel, q3Panel, q4Panel, q5Panel, q6Panel, tutPanel, guidePanel, contactPanel, teamPanel,
                   photoPanel;
    private JLabel titleLabel, faqLabel, q1question, q1answer, q2question, q2answer, q3question, q3answer, q4question, q4answer, q5question, q5answer, q6question, q6answer,
                   tutLabel, step1, step2, step3, step4, step5, step6, step6_1, step6_2, step7, step8, contactLabel;
    private JTabbedPane tabbedPane;
    private JScrollPane questionPane, tutPane;

    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color veryLightBlue = new Color(220, 240, 255);
    private Color white = Color.WHITE;
    private Color lightGray = new Color(211, 211, 211);
    private Color black = Color.black;
    private Color softBlue = new Color(190, 220, 245);
    private Color mediumBlue = new Color(175, 205, 230);

    public HelpPage() {
        setLayout(new BorderLayout());

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.ITALIC, 40);
        }

        helpPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, darkBlue, 0, getHeight(), lightBlue);
                graphics2d.setPaint(gradient);
                graphics2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        helpPanel.setOpaque(true);
        helpPanel.setLayout(new BorderLayout());

        titleLabel = new JLabel(" Help");
        titleLabel.setFont(customFont.deriveFont(40f));
        titleLabel.setForeground(white);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                             BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray), 
                             BorderFactory.createEmptyBorder(25, 25, 30, 0)));

        helpPanel.add(titleLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("FAQs", createFAQsPanel());
        tabbedPane.addTab("Tutorial", createTutorialPanel());
        tabbedPane.addTab("Contact", createContactPanel());
        tabbedPane.setFont(new Font(null, Font.BOLD, 15));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tabbedPane.setFocusable(false);
        tabbedPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        UIManager.put("TabbedPane.contentAreaColor", lightBlue);
        UIManager.put("TabbedPane.selected", lightBlue);

        helpPanel.add(tabbedPane, BorderLayout.CENTER);
        add(helpPanel, BorderLayout.CENTER);
    }

    private JPanel createFAQsPanel() {
        faqPanel = new JPanel(new BorderLayout());
        faqPanel.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createLineBorder(lightGray, 1),
                           BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        faqPanel.setOpaque(true);
        faqPanel.setBackground(veryLightBlue);

        faqLabel = new JLabel("Frequently Asked Questions");
        faqLabel.setFont(customFont.deriveFont(Font.BOLD, 30f));
        faqLabel.setForeground(darkBlue);
        faqLabel.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createMatteBorder(0, 0, 2, 0, darkBlue),
                           BorderFactory.createEmptyBorder(0, 0, 20, 0)));
        
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        questionPanel.setOpaque(false);
        questionPanel.setPreferredSize(new Dimension(0, 570)); 

        q1Panel = new JPanel();
        q1Panel.setLayout(new BoxLayout(q1Panel, BoxLayout.Y_AXIS));
        q1Panel.setOpaque(false);

        q1question = new JLabel("1. Are there only 5 clothing categories available?");
        q1question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q1answer = new JLabel("<html>Yes, there are only 5 clothing categories available in our application. However, a specific category does not merely mean there is only that specific type of clothing as shown in its respective image. Here is a list of specifications under each category:<br><br>" +
                              "<b>Active Wear</b> - running shorts, running singlets, bra tank tops, sports bras, training bras<br>" +
                              "<b>Tops</b> - t-shirts, tees, singlets, jerseys, polo shirts, tanks, tops, blouses, vests, cropped tops<br>" +
                              "<b>Bottoms</b> - shorts, pants, tights, skirts, leggings, joggers, trousers, slacks, skorts, jeans, short pants, maxi skirts, sweatskirts<br>" +
                              "<b>Dresses</b> - jersey dresses, shirt dresses, ribbed dresses, maxi dresses, denim dresses, sleeve dresses, polo dresses, cotton dresses, wrap dresses, button-front dresses, sleeveless dresses, knit dresses, dresses with pockets, mini dresses, bodycon dresses, maxi dresses, slip tops, yarn dresses<br>" +
                              "<b>Outerwear</b> - hoodies, sweaters, pullovers, hoodies, zip ups, long-sleeved polos, jackets, blazers, cardigans, parkas, sweatshirts, dungarees, tunics, coats</html>");
        q1answer.setFont(customFont.deriveFont(15f));

        q2Panel = new JPanel();
        q2Panel.setLayout(new BoxLayout(q2Panel, BoxLayout.Y_AXIS));
        q2Panel.setOpaque(false);

        q2question = new JLabel("2. Are there only 10 available brands with clothes to choose from?");
        q2question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q2answer = new JLabel("Yes, there are only 10 available brands in our store. We considered these brands carefully as they are in the list of the most affordable online shops.");
        q2answer.setFont(customFont.deriveFont(15f));

        q3Panel = new JPanel();
        q3Panel.setLayout(new BoxLayout(q3Panel, BoxLayout.Y_AXIS));
        q3Panel.setOpaque(false);

        q3question = new JLabel("3. Are the prices for different colors and/or different sizes higher than the default displayed color and size?");
        q3question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q3answer = new JLabel("No, the certain item, in any color and/or any size, remains the same, unless the quantity is higher.");
        q3answer.setFont(customFont.deriveFont(15f));

        q4Panel = new JPanel();
        q4Panel.setLayout(new BoxLayout(q4Panel, BoxLayout.Y_AXIS));
        q4Panel.setOpaque(false);

        q4question = new JLabel("4. Can I order as much items of the same product as I want to?");
        q4question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q4answer = new JLabel("Yes, as long as the quantity is equal to or less than 99, and you have enough balance to order the specific item.");
        q4answer.setFont(customFont.deriveFont(15f));

        q5Panel = new JPanel();
        q5Panel.setLayout(new BoxLayout(q5Panel, BoxLayout.Y_AXIS));
        q5Panel.setOpaque(false);

        q5question = new JLabel("5. Are the receipts printable?");
        q5question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q5answer = new JLabel("Yes, they are. The customer may present either a soft or hard copy of the receipt upon delivery of the product.");
        q5answer.setFont(customFont.deriveFont(15f));

        q6Panel = new JPanel();
        q6Panel.setLayout(new BoxLayout(q6Panel, BoxLayout.Y_AXIS));
        q6Panel.setOpaque(false);

        q6question = new JLabel("6. Can I contact the developers personally?");
        q6question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q6answer = new JLabel("<html>Yes, you can contact the developers personally. However, we will only be responding to messages related to the application's possible issues, improvements, or suggestions.</html>");
        q6answer.setFont(customFont.deriveFont(15f));
        
        questionPane = new JScrollPane(questionPanel);
        questionPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        questionPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        questionPane.getVerticalScrollBar().setUnitIncrement(16);
        questionPane.setBorder(BorderFactory.createEmptyBorder());
        questionPane.getViewport().setBackground(veryLightBlue);

        q1Panel.add(q1question);
        q1Panel.add(q1answer);
        q2Panel.add(q2question);
        q2Panel.add(q2answer);
        q3Panel.add(q3question);
        q3Panel.add(q3answer);
        q4Panel.add(q4question);
        q4Panel.add(q4answer);
        q5Panel.add(q5question);
        q5Panel.add(q5answer);
        q6Panel.add(q6question);
        q6Panel.add(q6answer);
        questionPanel.add(q1Panel);
        questionPanel.add(Box.createVerticalStrut(30));
        questionPanel.add(q2Panel);
        questionPanel.add(Box.createVerticalStrut(30));
        questionPanel.add(q3Panel);
        questionPanel.add(Box.createVerticalStrut(30));
        questionPanel.add(q4Panel);
        questionPanel.add(Box.createVerticalStrut(30));
        questionPanel.add(q5Panel);
        questionPanel.add(Box.createVerticalStrut(30));
        questionPanel.add(q6Panel);
        faqPanel.add(Box.createVerticalStrut(8));
        faqPanel.add(faqLabel, BorderLayout.NORTH);
        faqPanel.add(questionPane, BorderLayout.CENTER);

        return faqPanel;
    }

    private JPanel createTutorialPanel() {
        tutPanel = new JPanel(new BorderLayout());
        tutPanel.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createLineBorder(lightGray, 1),
                           BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        tutPanel.setOpaque(true);
        tutPanel.setBackground(veryLightBlue);
    
        tutLabel = new JLabel("StyleCart Fashion Store Tutorial");
        tutLabel.setFont(customFont.deriveFont(Font.BOLD, 30f));
        tutLabel.setForeground(darkBlue);
        tutLabel.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createMatteBorder(0, 0, 2, 0, darkBlue),
                           BorderFactory.createEmptyBorder(0, 0, 20, 0)));
    
        guidePanel = new JPanel();
        guidePanel.setLayout(new BoxLayout(guidePanel, BoxLayout.Y_AXIS));
        guidePanel.setOpaque(false);
        guidePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
        step1 = new JLabel("1. Click the arrow button on the Start Page.");
        step1.setFont(customFont.deriveFont(15f));
        step2 = new JLabel("2. Browse through clothing categories, featured items, or available brands to find clothes of your choice.");
        step2.setFont(customFont.deriveFont(15f));
        step3 = new JLabel("3. When an item has been favored, click the Cart button at its lower right.");
        step3.setFont(customFont.deriveFont(15f));
        step4 = new JLabel("4. Customize your item with colors and sizes. You may make the quantity higher.");
        step4.setFont(customFont.deriveFont(15f));
        step5 = new JLabel("5. Check you Shopping Cart and pick the item/s you would like to place as order.");
        step5.setFont(customFont.deriveFont(15f));
        step6 = new JLabel("6. Select the item/s you would like to place as order, and click 'Place Order'.");
        step6.setFont(customFont.deriveFont(15f));
        step6_1 = new JLabel("<html><i>- If you would like to add some items to your cart, click 'Add cart'.</i></html>");
        step6_1.setFont(customFont.deriveFont(Font.ITALIC, 12f));
        step6_2 = new JLabel("<html><i>- If you would like to remove items from your cart, select the item/s and click 'Remove Cart'.</i></html>");
        step6_2.setFont(customFont.deriveFont(Font.ITALIC, 12f));
        step7 = new JLabel("7. Click 'Generate Receipt' to view your proof of purchase, which will be presented upon delivery.");
        step7.setFont(customFont.deriveFont(15f));
        step8 = new JLabel("<html>8. Rate your ordered product and add a review. <i>(Optional)</i></html>");
        step8.setFont(customFont.deriveFont(15f));
        
        guidePanel.add(step1);
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step2);
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step3);
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step4);
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step5);
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step6);
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step6_1);
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step6_2);
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step7);
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step8);
    
        tutPane = new JScrollPane(guidePanel);
        tutPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tutPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tutPane.getVerticalScrollBar().setUnitIncrement(16);
        tutPane.setBorder(BorderFactory.createEmptyBorder());
        tutPane.getViewport().setBackground(veryLightBlue);
    
        tutPanel.add(tutLabel, BorderLayout.NORTH);
        tutPanel.add(tutPane, BorderLayout.CENTER);
    
        return tutPanel;
    }
    
    private JPanel createContactPanel() {
        contactPanel = new JPanel(new BorderLayout());
        contactPanel.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createLineBorder(lightGray, 1),
                           BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        contactPanel.setOpaque(true);
        contactPanel.setBackground(veryLightBlue);
    
        contactLabel = new JLabel("Our Team");
        contactLabel.setFont(customFont.deriveFont(Font.BOLD, 30f));
        contactLabel.setForeground(darkBlue);
        contactLabel.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createMatteBorder(0, 0, 2, 0, darkBlue),
                           BorderFactory.createEmptyBorder(0, 0, 20, 0)));
    
        teamPanel = new JPanel();
        teamPanel.setLayout(new BoxLayout(teamPanel, BoxLayout.Y_AXIS));
        teamPanel.setOpaque(false);
        teamPanel.setBorder(BorderFactory.createEmptyBorder(90, 0, 0, 0));

        String[] name = {"Noerly", "Rhyna", "Eunice", "Marisol", "Janila"};
        String[] photo = {"images/noerly.png", "images/rhyna.png", "images/eunice.png", "images/marisol.png", "images/janila.png"};

        photoPanel = new JPanel();
        photoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        photoPanel.setOpaque(false);

        for(int i = 0; i < name.length; i++) {
            photoPanel.add(createContactCards(name[i], photo[i]));
        }

        contactPanel.add(contactLabel, BorderLayout.NORTH);
        teamPanel.add(photoPanel);
        contactPanel.add(teamPanel, BorderLayout.CENTER);
        return contactPanel;
    }

    private JPanel createContactCards(String teamName, String teamIcon) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout(0, 20));
        productPanel.setPreferredSize(new Dimension(227, 300));
        productPanel.setBackground(softBlue);
        productPanel.setBorder(BorderFactory.createCompoundBorder(
                               BorderFactory.createLineBorder(lightBlue),
                               BorderFactory.createEmptyBorder(20, 20, 20,20)));

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new GridBagLayout());
        logoPanel.setPreferredSize(new Dimension(200, 180));
        logoPanel.setMaximumSize(new Dimension(200, 180));
        logoPanel.setOpaque(false);

        ImageIcon icon = new ImageIcon(teamIcon);
        Image scaledImage = icon.getImage().getScaledInstance(160, 180, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        logoPanel.add(iconLabel, constraints);

        JLabel nameLabel = new JLabel(teamName);
        nameLabel.setFont(customFont.deriveFont(15f));
        nameLabel.setForeground(black);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        productPanel.add(logoPanel, BorderLayout.CENTER);
        productPanel.add(nameLabel, BorderLayout.SOUTH);
        productPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        productPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                productPanel.setBackground(mediumBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                productPanel.setBackground(mediumBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                productPanel.setBackground(mediumBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                productPanel.setBackground(softBlue);
            }
        });

        return productPanel;
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class HelpPage extends JPanel {
    private Font customFont;
    private JPanel helpPanel, faqPanel, questionPanel, q1Panel, q2Panel, q3Panel, q4Panel, q5Panel, tutPanel, guidePanel, contactPanel, teamPanel,
                   photoPanel;
    private JLabel titleLabel, faqLabel, q1question, q1answer, q2question, q2answer, q3question, q3answer, q4question, q4answer, q5question, q5answer,
                   tutLabel, step1, step2, step3, step4, step5, step6, step6_1, step6_2, step7, step8, step9, contactLabel, squadLabel;
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
    private Color gray = Color.GRAY;

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
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 15));
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
        questionPanel.setPreferredSize(new Dimension(0, 500)); 

        q1Panel = new JPanel();
        q1Panel.setLayout(new BoxLayout(q1Panel, BoxLayout.Y_AXIS));
        q1Panel.setOpaque(false);

        q1question = new JLabel("1. Are there only 6 clothing categories available?");
        q1question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q1answer = new JLabel("<html>Yes, there are only 6 clothing categories available in our application. However, a specific category does not merely mean there is only that specific type of clothing as shown in its respective image. Here is a list of specifications under specific categories:<br><br>" +
                              "<b>Tops</b> - Shirts, Tops, Tees, Polos, Vests, Crop Tops, Blouses<br>" +
                              "<b>Bottoms</b> - Shorts, Pants, Slacks, Sweatpants, Trousers, Jeans, Skorts, Skirts, <br>" +
                              "<b>Active</b> - Shorts, Pants, Jerseys, Polos<br>" +
                              "<b>Outer</b> - Jackets, Pullovers, Long Sleeves, Hoodies, Sweatshirts, Sweaters</html>");
        q1answer.setFont(customFont.deriveFont(15f));

        q2Panel = new JPanel();
        q2Panel.setLayout(new BoxLayout(q2Panel, BoxLayout.Y_AXIS));
        q2Panel.setOpaque(false);

        q2question = new JLabel("2. Are the items available in 1 color only?");
        q2question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q2answer = new JLabel("<html>Most items are available in only 1 color. However, there are some items that have two colors, such as the Memo Slim Fit Polo with Textured Collar (available in white and green) and the Penshoppe Wide leg Cargo Pants (available in white and pink).</html>");
        q2answer.setFont(customFont.deriveFont(15f));

        q3Panel = new JPanel();
        q3Panel.setLayout(new BoxLayout(q3Panel, BoxLayout.Y_AXIS));
        q3Panel.setOpaque(false);

        q3question = new JLabel("3. Are there only 5 clothing brands available?");
        q3question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q3answer = new JLabel("<html>Yes, there are only 5 clothing brands available, namely, BNY, Bench, Lee, Memo and Penshoppe. We picked these brands as they fit the trendiest items ofr adolescents and adults.</html>");
        q3answer.setFont(customFont.deriveFont(15f));

        q4Panel = new JPanel();
        q4Panel.setLayout(new BoxLayout(q4Panel, BoxLayout.Y_AXIS));
        q4Panel.setOpaque(false);

        q4question = new JLabel("4. Are the receipts printable?");
        q4question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q4answer = new JLabel("Yes, they are. The customer may present either a soft or hard copy of the receipt upon delivery of the product.");
        q4answer.setFont(customFont.deriveFont(15f));

        q5Panel = new JPanel();
        q5Panel.setLayout(new BoxLayout(q5Panel, BoxLayout.Y_AXIS));
        q5Panel.setOpaque(false);

        q5question = new JLabel("5. Can I contact the developers personally?");
        q5question.setFont(customFont.deriveFont(Font.BOLD, 15f));
        
        q5answer = new JLabel("<html>Yes, you can contact the developers personally. However, we will only be responding to messages related to the application.</html>");
        q5answer.setFont(customFont.deriveFont(15f));

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
        questionPanel.add(q1Panel);
        questionPanel.add(Box.createVerticalStrut(30));
        questionPanel.add(q2Panel);
        questionPanel.add(Box.createVerticalStrut(30));
        questionPanel.add(q3Panel);
        questionPanel.add(Box.createVerticalStrut(30));
        questionPanel.add(q4Panel);
        questionPanel.add(Box.createVerticalStrut(30));
        questionPanel.add(q5Panel);
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
        step2 = new JLabel("2. Browse through available brands to find clothing of your choice. You can even add clothes to your cart from our 5 Featured Items!.");
        step2.setFont(customFont.deriveFont(15f));
        step3 = new JLabel("3. When an item has been favored, click the Cart button at its lower right.");
        step3.setFont(customFont.deriveFont(15f));
        step4 = new JLabel("4. Customize your item with colors and sizes.");
        step4.setFont(customFont.deriveFont(15f));
        step5 = new JLabel("5. Check your Shopping Cart and pick the item/s you would like to place as order.");
        step5.setFont(customFont.deriveFont(15f));
        step6 = new JLabel("6. Select the item/s you would like to place as order, and click 'Place Order'.");
        step6.setFont(customFont.deriveFont(15f));
        step6_1 = new JLabel("<html><i>- If you would like to add some/more items to your cart, click 'Add cart'.</i></html>");
        step6_1.setFont(customFont.deriveFont(Font.ITALIC, 12f));
        step6_2 = new JLabel("<html><i>- If you would like to remove items from your cart, select the item/s and click 'Remove Cart'.</i></html>");
        step6_2.setFont(customFont.deriveFont(Font.ITALIC, 12f));
        step7 = new JLabel("7. Click 'Generate Receipt' to view your proof of purchase, which will be presented upon delivery.");
        step7.setFont(customFont.deriveFont(15f));
        step8 = new JLabel("<html>8. Track your order to check when it will arrive and when it has arrived. <i>(Optional)</i></html>");
        step8.setFont(customFont.deriveFont(15f));
        step9 = new JLabel("<html>9. The ordered product and add a review. <i>(Optional)</i></html>");
        step9.setFont(customFont.deriveFont(15f));
        
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
        guidePanel.add(Box.createVerticalStrut(15));
        guidePanel.add(step9);
    
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
        teamPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        squadLabel = new JLabel("Syntax Squad");
        squadLabel.setFont(customFont.deriveFont(Font.BOLD, 25f));
        squadLabel.setForeground(darkBlue);
        squadLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        squadLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        String[] name = {"Noerly", "Rhyna", "Eunice", "Marisol", "Janila"};

        photoPanel = new JPanel();
        photoPanel.setLayout(new BoxLayout(photoPanel, BoxLayout.Y_AXIS));
        photoPanel.setOpaque(false);

        for(int i = 0; i < name.length; i++) {
            photoPanel.add(createContactCards(name[i]));
            photoPanel.add(Box.createVerticalStrut(10));
        }

        teamPanel.add(squadLabel);
        teamPanel.add(photoPanel);
        contactPanel.add(contactLabel, BorderLayout.NORTH);
        contactPanel.add(teamPanel, BorderLayout.CENTER);
        return contactPanel;
    }

    private JPanel createContactCards(String teamName) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout(0, 20));
        productPanel.setPreferredSize(new Dimension(227, 60));
        productPanel.setBackground(softBlue);
        productPanel.setBorder(BorderFactory.createCompoundBorder(
                               BorderFactory.createLineBorder(lightBlue),
                               BorderFactory.createEmptyBorder(20, 20, 20,20)));

        JLabel nameLabel = new JLabel(teamName);
        nameLabel.setFont(customFont.deriveFont(15f));
        nameLabel.setForeground(black);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        productPanel.add(nameLabel, BorderLayout.CENTER);
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
        
                ImageIcon phoneIcon = new ImageIcon("images/phone.png");
                Image fullPhoneIcon = phoneIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel imageLabel1 = new JLabel(new ImageIcon(fullPhoneIcon));
                ImageIcon igIcon = new ImageIcon("images/instagram.png");
                Image fullIgIcon = igIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel imageLabel2 = new JLabel(new ImageIcon(fullIgIcon));
                ImageIcon ghIcon = new ImageIcon("images/github.png");
                Image fullGhIcon = ghIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel imageLabel3 = new JLabel(new ImageIcon(fullGhIcon));

                if (teamName.equals("Noerly")) {
                    JPanel noerlyPanel = new JPanel();
                    noerlyPanel.setLayout(new BoxLayout(noerlyPanel, BoxLayout.Y_AXIS));
                    noerlyPanel.setOpaque(false);
    
                    JLabel noerlyName = new JLabel("Noerly Yvonne G. Idos");
                    noerlyName.setFont(customFont.deriveFont(25f));
                    noerlyName.setForeground(black);
                    noerlyName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
                    JLabel noerlyEmail = new JLabel("noerlyyvonnemaeaya@gmail.com"); 
                    noerlyEmail.setFont(customFont.deriveFont(15f));
                    noerlyEmail.setForeground(gray);
                    noerlyEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        
                    JPanel phonePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    phonePanel1.setOpaque(false);
                    JLabel phoneLabel1 = new JLabel("0976 353 9923");
                    phoneLabel1.setFont(customFont.deriveFont(15f));
                    phonePanel1.add(imageLabel1);
                    phonePanel1.add(phoneLabel1);
        
                    JPanel igPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    igPanel1.setOpaque(false);
                    JLabel igLabel1 = new JLabel("@noerly_cal");
                    igLabel1.setFont(customFont.deriveFont(15f));
                    igPanel1.add(imageLabel2);
                    igPanel1.add(igLabel1);
        
                    JPanel ghPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    ghPanel1.setOpaque(false);
                    JLabel ghLabel1 = new JLabel("@noerly_vis");
                    ghLabel1.setFont(customFont.deriveFont(15f));
                    ghPanel1.add(imageLabel3);
                    ghPanel1.add(ghLabel1);        
        
                    noerlyPanel.add(Box.createVerticalStrut(8));
                    noerlyPanel.add(noerlyName);
                    noerlyPanel.add(Box.createVerticalStrut(8));
                    noerlyPanel.add(noerlyEmail);
                    noerlyPanel.add(Box.createVerticalStrut(20));
                    noerlyPanel.add(phonePanel1);
                    noerlyPanel.add(Box.createVerticalStrut(8));
                    noerlyPanel.add(igPanel1);
                    noerlyPanel.add(Box.createVerticalStrut(8));
                    noerlyPanel.add(ghPanel1);
        
                    JOptionPane.showMessageDialog(null, noerlyPanel, "Profile", JOptionPane.PLAIN_MESSAGE);

                } else if (teamName.equals("Rhyna")) {
                    JPanel rhynaPanel = new JPanel();
                    rhynaPanel.setLayout(new BoxLayout(rhynaPanel, BoxLayout.Y_AXIS));
                    rhynaPanel.setOpaque(false);
    
                    JLabel rhynaName = new JLabel("Rhyna Jean D. Bacani");
                    rhynaName.setFont(customFont.deriveFont(25f));
                    rhynaName.setForeground(black);
                    rhynaName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
                    JLabel rhynaEmail = new JLabel("rhynajeanbacani18@gmail.com"); 
                    rhynaEmail.setFont(customFont.deriveFont(15f));
                    rhynaEmail.setForeground(gray);
                    rhynaEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        
                    JPanel phonePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    phonePanel1.setOpaque(false);
                    JLabel phoneLabel1 = new JLabel("0991 417 0068");
                    phoneLabel1.setFont(customFont.deriveFont(15f));
                    phonePanel1.add(imageLabel1);
                    phonePanel1.add(phoneLabel1);
        
                    JPanel igPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    igPanel1.setOpaque(false);
                    JLabel igLabel1 = new JLabel("@ajenixb2");
                    igLabel1.setFont(customFont.deriveFont(15f));
                    igPanel1.add(imageLabel2);
                    igPanel1.add(igLabel1);
        
                    JPanel ghPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    ghPanel1.setOpaque(false);
                    JLabel ghLabel1 = new JLabel("@hhitsgurl");
                    ghLabel1.setFont(customFont.deriveFont(15f));
                    ghPanel1.add(imageLabel3);
                    ghPanel1.add(ghLabel1);        
        
                    rhynaPanel.add(Box.createVerticalStrut(8));
                    rhynaPanel.add(rhynaName);
                    rhynaPanel.add(Box.createVerticalStrut(8));
                    rhynaPanel.add(rhynaEmail);
                    rhynaPanel.add(Box.createVerticalStrut(20));
                    rhynaPanel.add(phonePanel1);
                    rhynaPanel.add(Box.createVerticalStrut(8));
                    rhynaPanel.add(igPanel1);
                    rhynaPanel.add(Box.createVerticalStrut(8));
                    rhynaPanel.add(ghPanel1);
        
                    JOptionPane.showMessageDialog(null, rhynaPanel, "Profile", JOptionPane.PLAIN_MESSAGE);

               } else if (teamName.equals("Eunice")) {
                    JPanel eunicePanel = new JPanel();
                    eunicePanel.setLayout(new BoxLayout(eunicePanel, BoxLayout.Y_AXIS));
                    eunicePanel.setOpaque(false);
    
                    JLabel euniceName = new JLabel("Eunice O. Redonga");
                    euniceName.setFont(customFont.deriveFont(25f));
                    euniceName.setForeground(black);
                    euniceName.setAlignmentX(Component.CENTER_ALIGNMENT);
    
                    JLabel euniceEmail = new JLabel("szereese@gmail.com"); 
                    euniceEmail.setFont(customFont.deriveFont(15f));
                    euniceEmail.setForeground(gray);
                    euniceEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
    
                    JPanel phonePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    phonePanel1.setOpaque(false);
                    JLabel phoneLabel1 = new JLabel("0945 528 4879");
                    phoneLabel1.setFont(customFont.deriveFont(15f));
                    phonePanel1.add(imageLabel1);
                    phonePanel1.add(phoneLabel1);
    
                    JPanel igPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    igPanel1.setOpaque(false);
                    JLabel igLabel1 = new JLabel("@szerysz");
                    igLabel1.setFont(customFont.deriveFont(15f));
                    igPanel1.add(imageLabel2);
                    igPanel1.add(igLabel1);
    
                    JPanel ghPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    ghPanel1.setOpaque(false);
                    JLabel ghLabel1 = new JLabel("@er_shez");
                    ghLabel1.setFont(customFont.deriveFont(15f));
                    ghPanel1.add(imageLabel3);
                    ghPanel1.add(ghLabel1);        
    
                    eunicePanel.add(Box.createVerticalStrut(8));
                    eunicePanel.add(euniceName);
                    eunicePanel.add(Box.createVerticalStrut(8));
                    eunicePanel.add(euniceEmail);
                    eunicePanel.add(Box.createVerticalStrut(20));
                    eunicePanel.add(phonePanel1);
                    eunicePanel.add(Box.createVerticalStrut(8));
                    eunicePanel.add(igPanel1);
                    eunicePanel.add(Box.createVerticalStrut(8));
                    eunicePanel.add(ghPanel1);
    
                    JOptionPane.showMessageDialog(null, eunicePanel, "Profile", JOptionPane.PLAIN_MESSAGE);

                } else if (teamName.equals("Marisol")) {
                    JPanel marisolPanel = new JPanel();
                    marisolPanel.setLayout(new BoxLayout(marisolPanel, BoxLayout.Y_AXIS));
                    marisolPanel.setOpaque(false);
    
                    JLabel marisolName = new JLabel("Marisol E. Soriano");
                    marisolName.setFont(customFont.deriveFont(25f));
                    marisolName.setForeground(black);
                    marisolName.setAlignmentX(Component.CENTER_ALIGNMENT);
    
                    JLabel marisolEmail = new JLabel("marisolsoriano095@gmail.com"); 
                    marisolEmail.setFont(customFont.deriveFont(15f));
                    marisolEmail.setForeground(gray);
                    marisolEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
    
                    JPanel phonePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    phonePanel1.setOpaque(false);
                    JLabel phoneLabel1 = new JLabel("0963 515 2770");
                    phoneLabel1.setFont(customFont.deriveFont(15f));
                    phonePanel1.add(imageLabel1);
                    phonePanel1.add(phoneLabel1);
    
                    JPanel igPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    igPanel1.setOpaque(false);
                    JLabel igLabel1 = new JLabel("@meiszyy5");
                    igLabel1.setFont(customFont.deriveFont(15f));
                    igPanel1.add(imageLabel2);
                    igPanel1.add(igLabel1);
    
                    JPanel ghPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    ghPanel1.setOpaque(false);
                    JLabel ghLabel1 = new JLabel("@jile_cius");
                    ghLabel1.setFont(customFont.deriveFont(15f));
                    ghPanel1.add(imageLabel3);
                    ghPanel1.add(ghLabel1);        
    
                    marisolPanel.add(Box.createVerticalStrut(8));
                    marisolPanel.add(marisolName);
                    marisolPanel.add(Box.createVerticalStrut(8));
                    marisolPanel.add(marisolEmail);
                    marisolPanel.add(Box.createVerticalStrut(20));
                    marisolPanel.add(phonePanel1);
                    marisolPanel.add(Box.createVerticalStrut(8));
                    marisolPanel.add(igPanel1);
                    marisolPanel.add(Box.createVerticalStrut(8));
                    marisolPanel.add(ghPanel1);
    
                    JOptionPane.showMessageDialog(null, marisolPanel, "Profile", JOptionPane.PLAIN_MESSAGE);

                } else if (teamName.equals("Janila")) {
                    JPanel janilaPanel = new JPanel();
                    janilaPanel.setLayout(new BoxLayout(janilaPanel, BoxLayout.Y_AXIS));
                    janilaPanel.setOpaque(false);
    
                    JLabel janilaName = new JLabel("Janila May T. Torio");
                    janilaName.setFont(customFont.deriveFont(25f));
                    janilaName.setForeground(black);
                    janilaName.setAlignmentX(Component.CENTER_ALIGNMENT);
    
                    JLabel janilaEmail = new JLabel("janilatorio82@gmail.com"); 
                    janilaEmail.setFont(customFont.deriveFont(15f));
                    janilaEmail.setForeground(gray);
                    janilaEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
    
                    JPanel phonePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    phonePanel1.setOpaque(false);
                    JLabel phoneLabel1 = new JLabel("0966 266 8995");
                    phoneLabel1.setFont(customFont.deriveFont(15f));
                    phonePanel1.add(imageLabel1);
                    phonePanel1.add(phoneLabel1);
    
                    JPanel igPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    igPanel1.setOpaque(false);
                    JLabel igLabel1 = new JLabel("@ain't_dous");
                    igLabel1.setFont(customFont.deriveFont(15f));
                    igPanel1.add(imageLabel2);
                    igPanel1.add(igLabel1);
    
                    JPanel ghPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    ghPanel1.setOpaque(false);
                    JLabel ghLabel1 = new JLabel("@sec_retsidee");
                    ghLabel1.setFont(customFont.deriveFont(15f));
                    ghPanel1.add(imageLabel3);
                    ghPanel1.add(ghLabel1);        
    
                    janilaPanel.add(Box.createVerticalStrut(8));
                    janilaPanel.add(janilaName);
                    janilaPanel.add(Box.createVerticalStrut(8));
                    janilaPanel.add(janilaEmail);
                    janilaPanel.add(Box.createVerticalStrut(20));
                    janilaPanel.add(phonePanel1);
                    janilaPanel.add(Box.createVerticalStrut(8));
                    janilaPanel.add(igPanel1);
                    janilaPanel.add(Box.createVerticalStrut(8));
                    janilaPanel.add(ghPanel1);
    
                    JOptionPane.showMessageDialog(null, janilaPanel, "Profile", JOptionPane.PLAIN_MESSAGE);
                }
            };
        
            @Override
            public void mouseExited(MouseEvent e) {
                productPanel.setBackground(softBlue);
            }
        });

        return productPanel;
    }
}
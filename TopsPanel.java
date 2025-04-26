import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;

public class TopsPanel extends JPanel{
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private Color lightBlue, white, peach, black, lightGray, darkGray, darkBlue;
    private Font itemFont, customFont, titleFont;
    private JPanel topPanel, backPanel, mainPanel, menPanel, womenPanel, titleContentPanel, sectionContainer, menContainer, womenContainer;
    private JButton backButton;
    private JLabel titleLabel, menLabel, womenLabel;
    private JScrollPane scrollPane;

    public TopsPanel(CardLayout cardLayout, JPanel contentPanel, Color lightBlue, Color white,
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
    }

    private void initMainPanel() {
        
    }
}

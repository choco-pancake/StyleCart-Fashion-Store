import javax.swing.*;
import java.awt.*;
import java.io.*;

public class HelpPage extends JPanel {
    private Font customFont;
    private JPanel helpPanel;
    private JLabel titleLabel;

    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color lightGray = new Color(211, 211, 211);

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
                             BorderFactory.createEmptyBorder(25, 25, 25, 0)));

        helpPanel.add(titleLabel, BorderLayout.NORTH);

        add(helpPanel, BorderLayout.CENTER);
    }
}
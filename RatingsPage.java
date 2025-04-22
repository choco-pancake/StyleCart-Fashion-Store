import javax.swing.*;
import java.awt.*;

public class RatingsPage extends JPanel {
    private JPanel ratingPanel;
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);

    public RatingsPage() {
        setLayout(new BorderLayout()); // Set layout for the main RatingsPage panel

        ratingPanel = new JPanel();
        ratingPanel.setOpaque(false); // So the gradient background shows through
        ratingPanel.setLayout(new BorderLayout());

        add(ratingPanel, BorderLayout.CENTER); // Add to the main panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, darkBlue, 0, getHeight(), lightBlue);
        graphics2d.setPaint(gradient);
        graphics2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

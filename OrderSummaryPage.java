import javax.swing.*;
import java.awt.*;

public class OrderSummaryPage extends JPanel{
    private JPanel orderPanel;
    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);

    public OrderSummaryPage() {
        setLayout(new BorderLayout());

        orderPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2D = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, darkBlue, 0, getHeight(), lightBlue);
                graphics2D.setPaint(gradient);
                graphics2D.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        orderPanel.setOpaque(false);
        add(orderPanel, BorderLayout.CENTER);
    }
}

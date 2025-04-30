import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AnimatedCircle extends JFrame {
    private int circleY = 600;
    private final int circleX = 50;
    private final int circleSize = 20;
    private final int speed = 1;
    private final int targetY = 30;
    private JPanel leftPanel, rightPanel, placePanel, sortPanel, courierPanel, collectPanel, shipPanel, deliverPanel, receivePanel;
    private JLabel placeLabel, placeSublabel, sortLabel, sortSublabel, courierLabel, courierSublabel, collectLabel, collectSublabel, shipLabel, shipSublabel, deliverLabel, deliverSublabel, receiveLabel, receiveSublabel;
    private Timer timer;
    private Font customFont;
    private LocalDate currentDate;
    private DateTimeFormatter dateFormatter, timeFormatter;
    private String formattedDate, formattedTime;
    private LocalTime currentTime;
    private Color lightGray = Color.LIGHT_GRAY;

    public AnimatedCircle() {
        setTitle("Animated Circle");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.ITALIC, 40);
        }
        
        leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GREEN);
                g.fillOval(circleX, circleY, circleSize, circleSize);
            }
        };

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(700, 650));
        rightPanel.setMaximumSize(new Dimension(700, 650));
        
        placePanel = new JPanel();
        placePanel.setLayout(new BoxLayout(placePanel, BoxLayout.Y_AXIS));
        placePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        placePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        placeLabel = new JLabel("Order placed");
        placeLabel.setFont(customFont.deriveFont(20f));
        placeLabel.setForeground(lightGray);

        currentDate = LocalDate.now();
        dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        formattedDate = currentDate.format(dateFormatter);

        currentTime = LocalTime.now();
        timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        formattedTime = currentTime.format(timeFormatter);

        placeSublabel = new JLabel(formattedDate + ", " + formattedTime);
        placeSublabel.setFont(new Font(null, Font.PLAIN, 12));
        placeSublabel.setForeground(lightGray);

        sortPanel = new JPanel();
        sortPanel.setLayout(new BoxLayout(sortPanel, BoxLayout.Y_AXIS));
        sortPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        sortPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        sortLabel = new JLabel("At the sorting hub");
        sortLabel.setFont(customFont.deriveFont(20f));
        sortLabel.setForeground(lightGray);

        LocalTime timePlusOne = currentTime.plusHours(1);
        String formattedTime1 = timePlusOne.format(timeFormatter);
        sortSublabel = new JLabel(formattedDate + ", " + formattedTime1);
        sortSublabel.setFont(new Font(null, Font.PLAIN, 12));
        sortSublabel.setForeground(lightGray);

        courierPanel = new JPanel();
        courierPanel.setLayout(new BoxLayout(courierPanel, BoxLayout.Y_AXIS));
        courierPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        courierPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        courierLabel = new JLabel("On its way to the courier");
        courierLabel.setFont(customFont.deriveFont(20f));
        courierLabel.setForeground(lightGray);

        LocalTime timePlusTwo = currentTime.plusHours(2);
        String formattedTime2 = timePlusTwo.format(timeFormatter);
        courierSublabel = new JLabel(formattedDate + ", " + formattedTime2);
        courierSublabel.setFont(new Font(null, Font.PLAIN, 12));
        courierSublabel.setForeground(lightGray);

        collectPanel = new JPanel();
        collectPanel.setLayout(new BoxLayout(collectPanel, BoxLayout.Y_AXIS));
        collectPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        collectPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        collectLabel = new JLabel("Collected by the courier");
        collectLabel.setFont(customFont.deriveFont(20f));
        collectLabel.setForeground(lightGray);

        LocalTime timePlusThree = currentTime.plusHours(3);
        String formattedTime3 = timePlusThree.format(timeFormatter);
        collectSublabel = new JLabel(formattedDate + ", " + formattedTime3);
        collectSublabel.setFont(new Font(null, Font.PLAIN, 12));
        collectSublabel.setForeground(lightGray);

        shipPanel = new JPanel();
        shipPanel.setLayout(new BoxLayout(shipPanel, BoxLayout.Y_AXIS));
        shipPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        shipPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        shipLabel = new JLabel("Order shipped");
        shipLabel.setFont(customFont.deriveFont(20f));
        shipLabel.setForeground(lightGray);

        LocalTime timePlusFour = currentTime.plusHours(4);
        String formattedTime4 = timePlusFour.format(timeFormatter);
        shipSublabel = new JLabel(formattedDate + ", " + formattedTime4);
        shipSublabel.setFont(new Font(null, Font.PLAIN, 12));
        shipSublabel.setForeground(lightGray);

        deliverPanel = new JPanel();
        deliverPanel.setLayout(new BoxLayout(deliverPanel, BoxLayout.Y_AXIS));
        deliverPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        deliverPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        deliverLabel = new JLabel("Delivery will be attempted");
        deliverLabel.setFont(customFont.deriveFont(20f));
        deliverLabel.setForeground(lightGray);

        LocalTime timePlusFive = currentTime.plusHours(5);
        String formattedTime5 = timePlusFive.format(timeFormatter);
        deliverSublabel = new JLabel(formattedDate + ", " + formattedTime5);
        deliverSublabel.setFont(new Font(null, Font.PLAIN, 12));
        deliverSublabel.setForeground(lightGray);

        receivePanel = new JPanel();
        receivePanel.setLayout(new BoxLayout(receivePanel, BoxLayout.Y_AXIS));
        receivePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        receivePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        receiveLabel = new JLabel("Order received");
        receiveLabel.setFont(customFont.deriveFont(20f));
        receiveLabel.setForeground(lightGray);

        LocalTime timePlusSix = currentTime.plusHours(6);
        String formattedTime6 = timePlusSix.format(timeFormatter);
        receiveSublabel = new JLabel(formattedDate + ", " + formattedTime6);
        receiveSublabel.setFont(new Font(null, Font.PLAIN, 12));
        receiveSublabel.setForeground(lightGray);
        
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(100, 650));
        leftPanel.setMaximumSize(new Dimension(100, 650));

        placePanel.add(placeLabel);
        placePanel.add(Box.createVerticalStrut(5));
        placePanel.add(placeSublabel);

        sortPanel.add(sortLabel);
        sortPanel.add(Box.createVerticalStrut(5));
        sortPanel.add(sortSublabel);

        courierPanel.add(courierLabel);
        courierPanel.add(Box.createVerticalStrut(5));
        courierPanel.add(courierSublabel);

        collectPanel.add(collectLabel);
        collectPanel.add(Box.createVerticalStrut(5));
        collectPanel.add(collectSublabel);

        shipPanel.add(shipLabel);
        shipPanel.add(Box.createVerticalStrut(5));
        shipPanel.add(shipSublabel);

        deliverPanel.add(deliverLabel);
        deliverPanel.add(Box.createVerticalStrut(5));
        deliverPanel.add(deliverSublabel);

        receivePanel.add(receiveLabel);
        receivePanel.add(Box.createVerticalStrut(5));
        receivePanel.add(receiveSublabel);

        rightPanel.add(receivePanel);
        rightPanel.add(Box.createVerticalStrut(0));
        rightPanel.add(deliverPanel);
        rightPanel.add(Box.createVerticalStrut(0));
        rightPanel.add(shipPanel);
        rightPanel.add(Box.createVerticalStrut(0));
        rightPanel.add(collectPanel);
        rightPanel.add(Box.createVerticalStrut(0));
        rightPanel.add(courierPanel);
        rightPanel.add(Box.createVerticalStrut(0));
        rightPanel.add(sortPanel);
        rightPanel.add(Box.createVerticalStrut(0));
        rightPanel.add(placePanel);
        rightPanel.add(Box.createVerticalGlue());

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (circleY > targetY) {
                    circleY -= speed;

                    checkPanelPosition(placePanel, placeLabel, placeSublabel);
                    checkPanelPosition(sortPanel, sortLabel, sortSublabel);
                    checkPanelPosition(courierPanel, courierLabel, courierSublabel);
                    checkPanelPosition(collectPanel, collectLabel, collectSublabel);
                    checkPanelPosition(shipPanel, shipLabel, shipSublabel);
                    checkPanelPosition(deliverPanel, deliverLabel, deliverSublabel);
                    checkPanelPosition(receivePanel, receiveLabel, receiveSublabel);

                    leftPanel.repaint();
                } else {
                    ((Timer) e.getSource()).stop(); 
                }
            }
        });
        timer.start(); 
    }

    private void checkPanelPosition(JPanel panel, JLabel label, JLabel sublabel) {
        Rectangle bounds = panel.getBounds();
        Point panelLocation = panel.getLocationOnScreen();
        Point frameLocation = getLocationOnScreen();
        int panelY = panelLocation.y - frameLocation.y + bounds.height;

        if (circleY <= panelY) {
            int delay = 2000;

            new javax.swing.Timer(delay, e -> {
                label.setForeground(Color.BLACK);
                sublabel.setForeground(Color.BLACK);
                ((javax.swing.Timer) e.getSource()).stop();
            }).start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AnimatedCircle().setVisible(true);
        });
    }
}
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderSummaryPage extends JPanel {
    private Font customFont;
    private JPanel orderPanel, titlePanel, buttonPanel, trackPanel, backPanel;
    private JLabel titleLabel, emptyLabel, trackLabel;
    private DefaultTableCellRenderer headerRenderer, leftRenderer;
    private JScrollPane scrollPane;
    private JButton receiptButton, trackButton, backButton;
    private List<Object[]> allOrders;
    private LocalDateTime orderPlacedTime;

    // AnimatedCircle components
    private int circleY = 600;
    private final int circleX = 10;
    private final int circleSize = 20;
    private final int speed = 1;
    private final int targetY = 63;
    private TrackingAreaPanel trackingAreaPanel;
    private TrackingPanel placePanel, sortPanel, courierPanel, collectPanel, shipPanel, deliverPanel, receivePanel;
    private JLabel placeLabel, placeSublabel, sortLabel, sortSublabel, courierLabel, courierSublabel;
    private JLabel collectLabel, collectSublabel, shipLabel, shipSublabel, deliverLabel, deliverSublabel;
    private JLabel receiveLabel, receiveSublabel;
    private Timer timer;

    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color lightGray = new Color(211, 211, 211);
    private Color mediumBlue = new Color(125, 125, 200);
    private Color none = new Color(0, 0, 0, 0);
    private Color darkGray = new Color(180, 180, 180);
    private Color black = Color.BLACK;
    private Color darkerGray = Color.GRAY;

    // Custom panel class to draw a green circle on the left side after a delay
    private class TrackingPanel extends JPanel {
        private boolean showCircle = false;
        private boolean timerStarted = false; // Flag to prevent multiple timers

        public TrackingPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0)); // Increased left padding to make space for the circle
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        public void setShowCircle(boolean show) {
            this.showCircle = show;
            repaint();
        }

        public boolean isTimerStarted() {
            return timerStarted;
        }

        public void setTimerStarted(boolean started) {
            this.timerStarted = started;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (showCircle) {
                g.setColor(Color.GREEN);
                int circleYPos = (getHeight() - circleSize) / 2; // Center the circle vertically within the panel
                g.fillOval(10, circleYPos, circleSize, circleSize); // Draw circle on the left side
            }
        }
    }

    // Custom panel to handle both the moving circle and the tracking panels
    private class TrackingAreaPanel extends JPanel {
        public TrackingAreaPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(lightGray);
            setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
            setPreferredSize(new Dimension(800, 650));
            setMaximumSize(new Dimension(800, 650));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the moving circle on top of everything else
            g.setColor(Color.GREEN);
            g.fillOval(circleX, circleY, circleSize, circleSize);
        }
    }

    public OrderSummaryPage() {
        setLayout(new BorderLayout());

        allOrders = new ArrayList<>();

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.ITALIC, 40);
        }

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

        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        orderPanel.setOpaque(false);
        orderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(darkBlue);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray));

        titleLabel = new JLabel(" Order Summary");
        titleLabel.setFont(customFont);
        titleLabel.setForeground(white);
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(lightBlue);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        buttonPanel.setOpaque(true);

        receiptButton = new JButton("Generate Receipt");
        receiptButton.setBackground(mediumBlue);
        receiptButton.setForeground(white);
        receiptButton.setFont(customFont.deriveFont(15f));
        receiptButton.setFocusable(false);
        receiptButton.setContentAreaFilled(false);
        receiptButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(lightGray, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        receiptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        receiptButton.addActionListener(e -> generateReceipt());
        receiptButton.setOpaque(true);

        receiptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                receiptButton.setBackground(darkBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                receiptButton.setBackground(darkBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                receiptButton.setBackground(darkBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                receiptButton.setBackground(mediumBlue);
            }
        });

        trackButton = new JButton("Track Order");
        trackButton.setBackground(mediumBlue);
        trackButton.setForeground(white);
        trackButton.setFont(customFont.deriveFont(15f));
        trackButton.setFocusable(false);
        trackButton.setContentAreaFilled(false);
        trackButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(lightGray, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        trackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trackButton.addActionListener(e -> trackOrder());
        trackButton.setOpaque(true);

        trackButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                trackButton.setBackground(darkBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                trackButton.setBackground(darkBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                trackButton.setBackground(darkBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                trackButton.setBackground(mediumBlue);
            }
        });

        buttonPanel.add(receiptButton);
        buttonPanel.add(trackButton);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        add(orderPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        String[] columnNames = {"Product", "Details", "Price", "Quantity", "Subtotal"};
        displayOrderSummary(null, columnNames, customFont);
    }

    public void displayOrderSummary(Object[][] selectedData, String[] columnNames, Font tableFont) {
        if (selectedData != null && selectedData.length > 0) {
            if (allOrders.isEmpty()) {
                orderPlacedTime = LocalDateTime.now();
            }
            for (Object[] row : selectedData) {
                allOrders.add(row);
            }
        }

        orderPanel.removeAll();

        if (allOrders.isEmpty()) {
            emptyLabel = new JLabel("No orders placed yet.");
            emptyLabel.setFont(new Font(null, Font.BOLD, 20));
            emptyLabel.setForeground(white);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.add(Box.createVerticalGlue());
            orderPanel.add(emptyLabel);
            orderPanel.add(Box.createVerticalGlue());
        } else {
            Object[][] allOrdersArray = new Object[allOrders.size()][];
            for (int i = 0; i < allOrders.size(); i++) {
                allOrdersArray[i] = allOrders.get(i);
            }

            JTable summaryTable = new JTable(allOrdersArray, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                @Override
                public Class<?> getColumnClass(int column) {
                    return getValueAt(0, column).getClass();
                }
            };

            summaryTable.setRowHeight(120);
            summaryTable.setFont(tableFont.deriveFont(15f));
            summaryTable.getTableHeader().setFont(tableFont.deriveFont(20f));
            summaryTable.setRowSelectionAllowed(false);
            summaryTable.setCellSelectionEnabled(false);

            headerRenderer = (DefaultTableCellRenderer) summaryTable.getTableHeader().getDefaultRenderer();
            headerRenderer.setHorizontalAlignment(JLabel.CENTER);

            summaryTable.setBackground(lightGray);
            summaryTable.setOpaque(true);

            leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            summaryTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);

            summaryTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            summaryTable.getColumnModel().getColumn(1).setPreferredWidth(300);
            summaryTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            summaryTable.getColumnModel().getColumn(3).setPreferredWidth(80);
            summaryTable.getColumnModel().getColumn(4).setPreferredWidth(100);

            scrollPane = new JScrollPane(summaryTable);
            scrollPane.getViewport().setBackground(lightGray);

            orderPanel.add(scrollPane, BorderLayout.CENTER);
        }

        revalidate();
        repaint();
    }

    public void generateReceipt() {
        if (allOrders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no orders to generate receipt for.", "Cart Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "StyleCart_Receipt_" + timestamp + ".html";

            FileWriter writer = new FileWriter(fileName);
            double total = 0.0;

            writer.write("<html><head><style>");
            writer.write("body { font-family: Arial, sans-serif; margin: 10px; font-size: 12px; width: 300px; text-align: center; }");
            writer.write("h1 { color: #336699; }");
            writer.write("p { margin: 2px 0; }");
            writer.write("table { width: 100%; border-collapse: collapse; }");
            writer.write("th, td { border: 1px solid #ccc; padding: 5px; text-align: left; white-space: normal; }");
            writer.write("th { background-color: #f2f2f2; }");
            writer.write("hr { border: none; border-top: 1px dashed #000; margin: 5px 0; }");
            writer.write("</style></head><body>");

            writer.write("<h1>StyleCart Fashion Store Receipt</h1>");
            writer.write("<hr>");
            writer.write("<p>Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy - hh:mm a")) + "</p>");
            writer.write("<table>");
            writer.write("<tr><th>Product</th><th>Price</th><th>Quantity</th></tr>");

            for (Object[] row : allOrders) {
                String details = row[1].toString().replaceAll("<[^>]*>", " ").trim();
                String price = row[2].toString();
                String qty = row[3].toString();
                String subtotal = row[4].toString();

                total += Double.parseDouble(subtotal.replace("$", "").replace(",", ""));

                writer.write("<tr>");
                writer.write("<td>" + details + "</td>");
                writer.write("<td>" + price + "</td>");
                writer.write("<td>" + qty + "</td>");
                writer.write("</tr>");
            }

            writer.write("</table>");
            writer.write("<hr>");
            writer.write("<h3>Total Amount: $" + String.format("%,.2f", total) + "</h3>");
            writer.write("<hr>");
            writer.write("<p>Thank you for shopping at <strong>StyleCart Fashion Store!</strong></p>");
            writer.write("</body></html>");
            writer.close();

            JOptionPane.showMessageDialog(this, "You will be redirected to your receipt.", "Generate Receipt", JOptionPane.INFORMATION_MESSAGE);
            java.awt.Desktop.getDesktop().browse(new File(fileName).toURI());
            allOrders.clear();
            orderPlacedTime = null;
            displayOrderSummary(null, new String[] {"Product", "Details", "Price", "Quantity", "Subtotal"}, customFont);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating receipt!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void trackOrder() {
        if (scrollPane != null) {
            orderPanel.remove(scrollPane);
        }

        trackPanel = new JPanel();
        trackPanel.setBackground(lightGray);
        trackPanel.setLayout(new BoxLayout(trackPanel, BoxLayout.X_AXIS));

        trackingAreaPanel = new TrackingAreaPanel();

        if (orderPlacedTime == null) {
            orderPlacedTime = LocalDateTime.now();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm a");

        placePanel = createTrackingPanel("Order placed", orderPlacedTime.format(formatter));
        sortPanel = createTrackingPanel("At the sorting hub", orderPlacedTime.plusHours(1).format(formatter));
        courierPanel = createTrackingPanel("On its way to the courier", orderPlacedTime.plusHours(2).format(formatter));
        collectPanel = createTrackingPanel("Collected by the courier", orderPlacedTime.plusHours(3).format(formatter));
        shipPanel = createTrackingPanel("Order shipped", orderPlacedTime.plusHours(4).format(formatter));
        deliverPanel = createTrackingPanel("Delivery will be attempted", orderPlacedTime.plusHours(5).format(formatter));
        receivePanel = createTrackingPanel("Order received", orderPlacedTime.plusHours(6).format(formatter));

        trackingAreaPanel.add(receivePanel);
        trackingAreaPanel.add(Box.createVerticalStrut(0));
        trackingAreaPanel.add(deliverPanel);
        trackingAreaPanel.add(Box.createVerticalStrut(0));
        trackingAreaPanel.add(shipPanel);
        trackingAreaPanel.add(Box.createVerticalStrut(0));
        trackingAreaPanel.add(collectPanel);
        trackingAreaPanel.add(Box.createVerticalStrut(0));
        trackingAreaPanel.add(courierPanel);
        trackingAreaPanel.add(Box.createVerticalStrut(0));
        trackingAreaPanel.add(sortPanel);
        trackingAreaPanel.add(Box.createVerticalStrut(0));
        trackingAreaPanel.add(placePanel);
        trackingAreaPanel.add(Box.createVerticalGlue());

        backButton = createBackButton();
        backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setOpaque(false);
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        backPanel.setPreferredSize(new Dimension(200, 650)); // Increased width to accommodate the label

        trackLabel = new JLabel("Order Tracking");
        trackLabel.setFont(customFont.deriveFont(30f));
        trackLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        trackLabel.setForeground(black);

        backPanel.add(backButton);
        backPanel.add(trackLabel); // Add the label to the right of the backButton
        trackPanel.add(backPanel);
        trackPanel.add(Box.createHorizontalGlue());
        trackPanel.add(trackingAreaPanel);

        orderPanel.add(trackPanel, BorderLayout.CENTER);

        circleY = 600;
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

                    trackingAreaPanel.repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();

        orderPanel.revalidate();
        orderPanel.repaint();
    }

    private TrackingPanel createTrackingPanel(String labelText, String sublabelText) {
        TrackingPanel panel = new TrackingPanel();

        JLabel label = new JLabel(labelText);
        label.setFont(customFont.deriveFont(20f));
        label.setForeground(lightGray);

        JLabel sublabel = new JLabel(sublabelText);
        sublabel.setFont(new Font(null, Font.PLAIN, 12));
        sublabel.setForeground(lightGray);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(sublabel);

        if (labelText.equals("Order placed")) {
            placeLabel = label;
            placeSublabel = sublabel;
        } else if (labelText.equals("At the sorting hub")) {
            sortLabel = label;
            sortSublabel = sublabel;
        } else if (labelText.equals("On its way to the courier")) {
            courierLabel = label;
            courierSublabel = sublabel;
        } else if (labelText.equals("Collected by the courier")) {
            collectLabel = label;
            collectSublabel = sublabel;
        } else if (labelText.equals("Order shipped")) {
            shipLabel = label;
            shipSublabel = sublabel;
        } else if (labelText.equals("Delivery will be attempted")) {
            deliverLabel = label;
            deliverSublabel = sublabel;
        } else if (labelText.equals("Order received")) {
            receiveLabel = label;
            receiveSublabel = sublabel;
        }

        return panel;
    }

    private void checkPanelPosition(TrackingPanel panel, JLabel label, JLabel sublabel) {
        // Use the top of the panel instead of its center
        Point panelLocation = panel.getLocationOnScreen();
        Point frameLocation = SwingUtilities.getWindowAncestor(this).getLocationOnScreen();
        int panelY = panelLocation.y - frameLocation.y;

        // Only start the timer if the moving circle has passed the top of the panel
        // and the timer hasn't already been started for this panel
        if (circleY <= panelY && !panel.isTimerStarted()) {
            panel.setTimerStarted(true); // Mark the timer as started
            int delay = 4350;
            new Timer(delay, e -> {
                label.setForeground(Color.BLACK);
                sublabel.setForeground(Color.BLACK);
                panel.setShowCircle(true); // Show the green circle after the delay
                ((Timer) e.getSource()).stop();
            }).start();
        }
    }

    private JButton createBackButton() {
        backButton = new JButton("←") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D graphics2d = (Graphics2D) g.create();
                graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2d.setColor(none);
                graphics2d.fillRect(0, 0, getWidth(), getHeight());

                int size = Math.min(getWidth(), getHeight()) - 4;
                if (getModel().isPressed()) {
                    graphics2d.setColor(darkerGray.darker());
                } else if (getModel().isRollover()) {
                    graphics2d.setColor(darkerGray);
                } else {
                    graphics2d.setColor(darkGray);
                }

                graphics2d.fill(new Ellipse2D.Double(2, 2, size, size));
                graphics2d.setColor(getModel().isPressed() || getModel().isRollover() ? white : black);
                graphics2d.setFont(getFont());

                FontMetrics metrics = graphics2d.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(getText())) / 2;
                int y = (getHeight() + metrics.getAscent() - metrics.getDescent()) / 2;
                graphics2d.drawString(getText(), x, y);
                graphics2d.dispose();
            }

            @Override
            public boolean contains(int x, int y) {
                int size = Math.min(getWidth(), getHeight());
                double radius = size / 2.0;
                double cx = getWidth() / 2.0;
                double cy = getHeight() / 2.0;
                return Point.distance(cx, cy, x, y) <= radius;
            }
        };

        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setFocusable(false);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFont(customFont.deriveFont(30f));
        backButton.setPreferredSize(new Dimension(40, 40));

        backButton.addActionListener(e -> {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            orderPanel.remove(trackPanel);
            displayOrderSummary(null, new String[]{"Product", "Details", "Price", "Quantity", "Subtotal"}, customFont);
        });

        return backButton;
    }
}
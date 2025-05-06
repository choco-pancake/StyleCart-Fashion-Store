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
    private boolean receiptGenerated = false; // New flag to track receipt generation

    private final int circleSize = 20;
    private TrackingAreaPanel trackingAreaPanel;
    private TrackingPanel placePanel, sortPanel, courierPanel, collectPanel, shipPanel, deliverPanel, receivePanel;
    private JLabel placeLabel, placeSublabel, sortLabel, sortSublabel, courierLabel, courierSublabel, collectLabel, collectSublabel, shipLabel,
                   shipSublabel, deliverLabel, deliverSublabel, receiveLabel, receiveSublabel;
    private Timer timer;
    private boolean isAnimationPaused = false;
    private boolean isTrackingInitialized = false;
    private boolean isTrackingComplete = false;
    private long animationStartTime, pausedTime;
    private long[] panelTriggerTimes = new long[7]; 

    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color lightGray = new Color(211, 211, 211);
    private Color mediumBlue = new Color(125, 125, 200);
    private Color none = new Color(0, 0, 0, 0);
    private Color darkGray = new Color(180, 180, 180);
    private Color black = Color.BLACK;
    private Color darkerGray = Color.GRAY;
    private Color green = Color.GREEN;
    private Color red = Color.RED;

    private class TrackingPanel extends JPanel {
        private boolean showCircle = false;
        private boolean timerStarted = false;
        private Color circleColor = green; 

        public TrackingPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 0)); 
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        public void setShowCircle(boolean show) {
            this.showCircle = show;
            repaint();
        }

        public void setCircleColor(Color color) {
            this.circleColor = color;
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
                g.setColor(circleColor);
                int circleYPos = (getHeight() - circleSize) / 2; 
                g.fillOval(10, circleYPos, circleSize, circleSize); 
            }
        }
    }

    private class TrackingAreaPanel extends JPanel {
        public TrackingAreaPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(lightGray);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 
            setAlignmentX(Component.CENTER_ALIGNMENT); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
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
        trackButton.addActionListener(e -> {
            if (isAnimationPaused && isTrackingInitialized) {
                isAnimationPaused = false;
                long pausedDuration = System.currentTimeMillis() - pausedTime;
                animationStartTime += pausedDuration;
                timer.start();
                orderPanel.removeAll();
                orderPanel.add(trackPanel, BorderLayout.CENTER);
                orderPanel.revalidate();
                orderPanel.repaint();
            } else {
                trackOrder();
            }
        });
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

    public boolean isReceiptGenerated() {
        return !receiptGenerated && !allOrders.isEmpty();
    }

    public void displayOrderSummary(Object[][] selectedData, String[] columnNames, Font tableFont) {
        if (selectedData != null && selectedData.length > 0) {
            allOrders.clear();
            orderPlacedTime = LocalDateTime.now();
            receiptGenerated = false; 
            resetTrackingState(); 
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

    private void resetTrackingState() {
        isTrackingInitialized = false;
        isTrackingComplete = false;
        isAnimationPaused = false;
        if (timer != null) {
            timer.stop();
        }
        if (placePanel != null) {
            placePanel.setTimerStarted(false);
            placePanel.setShowCircle(false);
            placeLabel.setForeground(lightGray);
            placeSublabel.setForeground(lightGray);
        }
        if (sortPanel != null) {
            sortPanel.setTimerStarted(false);
            sortPanel.setShowCircle(false);
            sortLabel.setForeground(lightGray);
            sortSublabel.setForeground(lightGray);
        }
        if (courierPanel != null) {
            courierPanel.setTimerStarted(false);
            courierPanel.setShowCircle(false);
            courierLabel.setForeground(lightGray);
            courierSublabel.setForeground(lightGray);
        }
        if (collectPanel != null) {
            collectPanel.setTimerStarted(false);
            collectPanel.setShowCircle(false);
            collectLabel.setForeground(lightGray);
            collectSublabel.setForeground(lightGray);
        }
        if (shipPanel != null) {
            shipPanel.setTimerStarted(false);
            shipPanel.setShowCircle(false);
            shipLabel.setForeground(lightGray);
            shipSublabel.setForeground(lightGray);
        }
        if (deliverPanel != null) {
            deliverPanel.setTimerStarted(false);
            deliverPanel.setShowCircle(false);
            deliverLabel.setForeground(lightGray);
            deliverSublabel.setForeground(lightGray);
        }
        if (receivePanel != null) {
            receivePanel.setTimerStarted(false);
            receivePanel.setShowCircle(false);
            receiveLabel.setForeground(lightGray);
            receiveSublabel.setForeground(lightGray);
            receiveSublabel.setText("");
            receivePanel.setCircleColor(red);
        }
        if (trackPanel != null) {
            orderPanel.remove(trackPanel);
            trackPanel = null;
        }
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

                total += Double.parseDouble(subtotal.replace("₱", "").replace(",", ""));

                writer.write("<tr>");
                writer.write("<td>" + details + "</td>");
                writer.write("<td>" + price + "</td>");
                writer.write("<td>" + qty + "</td>");
                writer.write("</tr>");
            }

            writer.write("</table>");
            writer.write("<hr>");
            writer.write("<h3>Total Amount: ₱" + String.format("%,.2f", total) + "</h3>");
            writer.write("<hr>");
            writer.write("<p>Thank you for shopping at <strong>StyleCart Fashion Store!</strong></p>");
            writer.write("</body></html>");
            writer.close();

            receiptGenerated = true; 
            JOptionPane.showMessageDialog(orderPanel, "You will be redirected to your receipt.", "Generate Receipt", JOptionPane.INFORMATION_MESSAGE);
            java.awt.Desktop.getDesktop().browse(new File(fileName).toURI());
            orderPlacedTime = null;
            resetTrackingState();
            displayOrderSummary(null, new String[]{"Product", "Details", "Price", "Quantity", "Subtotal"}, customFont);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating receipt!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void trackOrder() {
        if (isTrackingInitialized || isTrackingComplete) {
            orderPanel.removeAll();
            orderPanel.add(trackPanel, BorderLayout.CENTER);
            orderPanel.revalidate();
            orderPanel.repaint();
            return;
        }

        if (scrollPane != null) {
            orderPanel.remove(scrollPane);
        }

        trackPanel = new JPanel();
        BorderLayout borderLayout = new BorderLayout(0, 0);
        borderLayout.setVgap(0); 
        trackPanel.setLayout(borderLayout);
        trackPanel.setBackground(lightGray);
        trackPanel.setBorder(BorderFactory.createLineBorder(darkerGray, 1));

        trackingAreaPanel = new TrackingAreaPanel();
        trackingAreaPanel.setLayout(new BoxLayout(trackingAreaPanel, BoxLayout.Y_AXIS));
        trackingAreaPanel.setOpaque(true); 
        trackingAreaPanel.setBackground(lightGray);

        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.setOpaque(false);
        centeringPanel.add(trackingAreaPanel, new GridBagConstraints());

        if (orderPlacedTime == null) {
            orderPlacedTime = LocalDateTime.now();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm a");

        placePanel = createTrackingPanel("Order placed", orderPlacedTime.format(formatter));
        sortPanel = createTrackingPanel("At the sorting hub", orderPlacedTime.plusMinutes(1443).format(formatter));
        courierPanel = createTrackingPanel("On its way to the courier", orderPlacedTime.plusMinutes(2772).format(formatter));
        collectPanel = createTrackingPanel("Collected by the courier", orderPlacedTime.plusMinutes(2887).format(formatter));
        shipPanel = createTrackingPanel("Order shipped", orderPlacedTime.plusMinutes(4336).format(formatter));
        deliverPanel = createTrackingPanel("Delivery will be attempted", orderPlacedTime.plusMinutes(4440).format(formatter));
        receivePanel = createTrackingPanel("Order received", "");
        receivePanel.setCircleColor(red); 

        placePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sortPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        courierPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        collectPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        shipPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        deliverPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        receivePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        trackingAreaPanel.add(Box.createVerticalStrut(50));

        backButton = createBackButton();
        backButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        backPanel.setOpaque(false);
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        backPanel.setPreferredSize(new Dimension(200, 50));

        trackLabel = new JLabel("Order Tracking");
        trackLabel.setFont(customFont.deriveFont(30f));
        trackLabel.setForeground(black);
        trackLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        backButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        trackLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        backPanel.add(backButton);
        backPanel.add(Box.createHorizontalStrut(20)); 
        backPanel.add(trackLabel);
        backPanel.add(Box.createHorizontalGlue());

        backPanel = new JPanel() {
            {
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                setOpaque(false);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setPreferredSize(new Dimension(200, 80));
                add(backButton);
                add(Box.createHorizontalStrut(20));
                add(trackLabel);
                add(Box.createHorizontalGlue());
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(black);
                g2d.setStroke(new BasicStroke(1));
                int startX = -getX();
                int startY = backButton.getY() + backButton.getHeight() + 3; 
                Window window = SwingUtilities.getWindowAncestor(OrderSummaryPage.this);
                int endX = window.getWidth() - getX();
                g2d.drawLine(startX, startY, endX, startY);
            }
        };

        trackPanel.add(backPanel, BorderLayout.NORTH);
        trackPanel.add(centeringPanel, BorderLayout.CENTER);

        orderPanel.add(trackPanel, BorderLayout.CENTER);

        animationStartTime = System.currentTimeMillis();

        panelTriggerTimes[0] = 0;              
        panelTriggerTimes[1] = 2000;           
        panelTriggerTimes[2] = 4000;           
        panelTriggerTimes[3] = 6000;           
        panelTriggerTimes[4] = 8000;           
        panelTriggerTimes[5] = 10000;          
        panelTriggerTimes[6] = 12000;          

        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAnimationPaused) return;

                long elapsedTime = System.currentTimeMillis() - animationStartTime;

                checkPanelPosition(placePanel, placeLabel, placeSublabel, 0);
                checkPanelPosition(sortPanel, sortLabel, sortSublabel, 1);
                checkPanelPosition(courierPanel, courierLabel, courierSublabel, 2);
                checkPanelPosition(collectPanel, collectLabel, collectSublabel, 3);
                checkPanelPosition(shipPanel, shipLabel, shipSublabel, 4);
                checkPanelPosition(deliverPanel, deliverLabel, deliverSublabel, 5);
                checkPanelPosition(receivePanel, receiveLabel, receiveSublabel, 6);

                if (elapsedTime >= panelTriggerTimes[6]) {
                    ((Timer) e.getSource()).stop();
                    isAnimationPaused = false;
                    isTrackingInitialized = false;
                    confirmReception();
                }
            }
        });
        timer.start();

        isTrackingInitialized = true;
        orderPanel.revalidate();
        orderPanel.repaint();
    }

    private void confirmReception() {
        SwingUtilities.invokeLater(() -> {
            int response = JOptionPane.showConfirmDialog(
                    orderPanel,
                    "Did you successfully receive your order?",
                    "Order Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                String receivedTime = orderPlacedTime.plusMinutes(5747).format(DateTimeFormatter.ofPattern("MMMM dd, yyyy, hh:mm a"));

                receiveSublabel.setText(receivedTime);
                receiveLabel.setForeground(black);
                receiveSublabel.setForeground(black);
                receivePanel.setCircleColor(green); 
                isTrackingComplete = true;

                JOptionPane.showMessageDialog(orderPanel,
                        "Order received!",
                        "Reception Confirmed",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                deliverPanel.setTimerStarted(false);
                deliverPanel.setShowCircle(false);
                deliverLabel.setForeground(lightGray);
                deliverSublabel.setForeground(lightGray);

                receivePanel.setTimerStarted(false);
                receivePanel.setShowCircle(false);
                receiveLabel.setForeground(lightGray);
                receiveSublabel.setForeground(lightGray);
                receiveSublabel.setText("");
                receivePanel.setCircleColor(red); 

                animationStartTime = System.currentTimeMillis();
                panelTriggerTimes[5] = 2000; 
                panelTriggerTimes[6] = 4000; 

                timer = new Timer(30, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isAnimationPaused) return;

                        long elapsedTime = System.currentTimeMillis() - animationStartTime;

                        checkPanelPosition(deliverPanel, deliverLabel, deliverSublabel, 5);
                        checkPanelPosition(receivePanel, receiveLabel, receiveSublabel, 6);

                        if (elapsedTime >= panelTriggerTimes[6]) {
                            ((Timer) e.getSource()).stop();
                            isAnimationPaused = false;
                            confirmReception(); 
                        }
                    }
                });
                timer.start();
            }
        });
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

    private void checkPanelPosition(TrackingPanel panel, JLabel label, JLabel sublabel, int panelIndex) {
        if (isAnimationPaused) return;

        long elapsedTime = System.currentTimeMillis() - animationStartTime;

        if (!panel.isTimerStarted() && elapsedTime >= panelTriggerTimes[panelIndex]) {
            panel.setTimerStarted(true);
            label.setForeground(Color.BLACK);
            sublabel.setForeground(Color.BLACK);
            panel.setShowCircle(true);
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
                int stringWidth = metrics.stringWidth(getText());
                int stringHeight = metrics.getAscent() - metrics.getDescent();
                int x = (getWidth() - stringWidth) / 2 - 1;
                int y = (getHeight() - stringHeight) / 2 + 15;
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
                isAnimationPaused = true;
                pausedTime = System.currentTimeMillis();
            }
            orderPanel.remove(trackPanel);
            displayOrderSummary(null, new String[]{"Product", "Details", "Price", "Quantity", "Subtotal"}, customFont);
        });

        return backButton;
    }
}
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
    private LocalDateTime orderPlacedTime;
    private boolean receiptGenerated = false;
    private List<MainContents.CartItem> orderedItems = new ArrayList<>();

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
    private Color green = Color.GREEN;
    private Color red = Color.RED;
    private Color darkerGray = Color.GRAY;

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

    public OrderSummaryPage(CardLayout cardLayout, JPanel contentPanel) {
        setLayout(new BorderLayout());

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("SansSerif", Font.PLAIN, 40);
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
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        receiptButton.setOpaque(true);
        receiptButton.addActionListener(e -> generateReceipt());

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
        trackButton.setOpaque(true);
        trackButton.addActionListener(e -> {
            if (isAnimationPaused && isTrackingInitialized) {
                isAnimationPaused = false;
                long pausedDuration = System.currentTimeMillis() - pausedTime;
                animationStartTime += pausedDuration;
                timer.start();
                orderPanel.removeAll();
                orderPanel.add(trackPanel);
                orderPanel.revalidate();
                orderPanel.repaint();
            } else {
                trackOrder();
            }
        });

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
        return receiptGenerated;
    }

    public List<MainContents.CartItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<MainContents.CartItem> items) {
        this.orderedItems = new ArrayList<>(items);
        this.receiptGenerated = false; // Reset receipt status for new order
        this.isTrackingComplete = false; // Reset tracking status for new order
    }

    public void displayOrderSummary(Object[][] tableData, String[] columnNames, Font tableFont) {
        orderPanel.removeAll();

        if (tableData == null || tableData.length == 0) {
            emptyLabel = new JLabel("No orders placed yet.");
            emptyLabel.setFont(new Font(null, Font.BOLD, 25));
            emptyLabel.setForeground(white);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            orderPanel.add(Box.createVerticalGlue());
            orderPanel.add(emptyLabel);
            orderPanel.add(Box.createVerticalGlue());
        } else {
            JPanel tableContainer = new JPanel(new BorderLayout());
            tableContainer.setOpaque(false);
            tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

            JTable summaryTable = new JTable(tableData, columnNames) {
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
            summaryTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            summaryTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);

            summaryTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            summaryTable.getColumnModel().getColumn(1).setPreferredWidth(300);
            summaryTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            summaryTable.getColumnModel().getColumn(3).setPreferredWidth(80);
            summaryTable.getColumnModel().getColumn(4).setPreferredWidth(100);

            scrollPane = new JScrollPane(summaryTable);
            scrollPane.getViewport().setBackground(lightGray);
            scrollPane.setColumnHeaderView(summaryTable.getTableHeader());
            scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
            scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JPanel() {
                {
                    setBackground(white);
                }
            });

            tableContainer.add(scrollPane, BorderLayout.CENTER);
            orderPanel.add(tableContainer);
        }

        orderPanel.revalidate();
        orderPanel.repaint();
    }

    public void generateReceipt() {
        if (orderedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the order to generate a receipt.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        orderPlacedTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = orderPlacedTime.format(formatter);
    
        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append("StyleCart Receipt\n");
        receiptContent.append("Order Placed: ").append(formattedDateTime).append("\n");
        receiptContent.append("----------------------------------------\n");
        receiptContent.append(String.format("%-30s %-10s %-10s %-10s\n", "Item", "Size", "Quantity", "Subtotal"));
        receiptContent.append("----------------------------------------\n");
    
        double total = 0.0;
        for (MainContents.CartItem item : orderedItems) {
            double price = Double.parseDouble(item.getPrice().replace("₱", ""));
            double subtotal = price * item.getQuantity();
            total += subtotal;
            receiptContent.append(String.format("%-30s %-10s %-10d ₱%,.2f\n",
                    item.getName().length() > 27 ? item.getName().substring(0, 27) + "..." : item.getName(),
                    item.getSize(),
                    item.getQuantity(),
                    subtotal));
        }
    
        receiptContent.append("----------------------------------------\n");
        receiptContent.append(String.format("Total: ₱%,.2f\n", total));
        receiptContent.append("----------------------------------------\n");
        receiptContent.append("Thank you for shopping with StyleCart!\n");
    
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Receipt");
        fileChooser.setSelectedFile(new File("StyleCart_Receipt.txt"));
        int userSelection = fileChooser.showSaveDialog(this);
    
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                writer.println(receiptContent.toString());
                receiptGenerated = true;
                
                int option = JOptionPane.showConfirmDialog(
                    this, 
                    "You will be redirected to your receipt.", 
                    "Receipt Generated", 
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(fileToSave);
                        } else {
                            JOptionPane.showMessageDialog(
                                this, 
                                "Cannot open receipt automatically. Please open it manually.", 
                                "Info", 
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                            this, 
                            "Error opening receipt: " + ex.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving receipt.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void trackOrder() {
        if (orderedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No orders to track.", 
                "No Orders", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (isTrackingInitialized || isTrackingComplete) {
            orderPanel.removeAll();
            orderPanel.add(trackPanel, BorderLayout.CENTER);
            orderPanel.revalidate();
            orderPanel.repaint();
            return;
        }

        orderPanel.removeAll(); // Clear all components to remove table headers

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
    
                if (canRemoveOrderItems()) {
                    orderedItems.clear();
                    displayOrderSummary(null, new String[]{"Product", "Details", "Price", "Quantity", "Subtotal"}, customFont);
                }
    
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
                receivePanel.setCircleColor(Color.RED); 

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

    public boolean canRemoveOrderItems() {
        return receiptGenerated && isTrackingComplete;
    }

    private Object[][] convertCartItemsToTableData(List<MainContents.CartItem> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
    
        Object[][] tableData = new Object[items.size()][5];
        for (int i = 0; i < items.size(); i++) {
            MainContents.CartItem item = items.get(i);
            tableData[i][0] = new ImageIcon(new ImageIcon(item.getIconPath()).getImage().getScaledInstance(item.getIconWidth(), item.getIconHeight(), Image.SCALE_SMOOTH));
            tableData[i][1] = "<html><b>" + item.getName() + "</b><br>Size: " + item.getSize() + "</html>";
            tableData[i][2] = item.getPrice();
            tableData[i][3] = item.getQuantity();
            tableData[i][4] = "₱" + String.format("%.2f", 
                Double.parseDouble(item.getPrice().replace("₱", "")) * item.getQuantity());
        }
        return tableData;
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
            
            if (canRemoveOrderItems()) {
                displayOrderSummary(null, new String[]{"Product", "Details", "Price", "Quantity", "Subtotal"}, customFont);
            } else {
                Object[][] tableData = convertCartItemsToTableData(orderedItems);
                displayOrderSummary(tableData, new String[]{"Product", "Details", "Price", "Quantity", "Subtotal"}, customFont);
            }
        });
    
        return backButton;
    }
}
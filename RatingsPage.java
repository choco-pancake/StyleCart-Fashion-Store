import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RatingsPage extends JPanel {
    private Font customFont;
    private JPanel ratingPanel, titlePanel, buttonPanel, tableContainer;
    private JLabel titleLabel, emptyLabel;
    private JTable orderTable;
    private JScrollPane tableScrollPane;
    private JButton rateButton, deliverButton;
    private List<Object[]> orderData;
    private DefaultTableCellRenderer headerRenderer, leftRenderer;
    private Set<String> ratedProducts = new HashSet<>();
    private Set<String> ratedDeliveries = new HashSet<>();

    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color lightGray = new Color(211, 211, 211);
    private Color white = Color.WHITE;
    private Color mediumBlue = new Color(125, 125, 200);
    private Color gold = new Color(220, 200, 0);
    private Color black = Color.BLACK;

    public RatingsPage() {
        setLayout(new BorderLayout());
        orderData = new ArrayList<>();

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("SansSerif", Font.PLAIN, 40);
        }

        ratingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, darkBlue, 0, getHeight(), lightBlue);
                graphics2d.setPaint(gradient);
                graphics2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        ratingPanel.setOpaque(false);

        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(darkBlue);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray));

        titleLabel = new JLabel(" Ratings");
        titleLabel.setFont(customFont);
        titleLabel.setForeground(white);
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(lightBlue);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 25, 0));

        rateButton = new JButton("Rate Product");
        rateButton.setBackground(mediumBlue);
        rateButton.setForeground(white);
        rateButton.setFont(customFont.deriveFont(15f));
        rateButton.setFocusable(false);
        rateButton.setContentAreaFilled(false);
        rateButton.setBorder(BorderFactory.createCompoundBorder(
                             BorderFactory.createLineBorder(lightGray, 1),
                             BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        rateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rateButton.addActionListener(e -> rateProduct());
        rateButton.setOpaque(true);

        rateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                rateButton.setBackground(darkBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                rateButton.setBackground(darkBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                rateButton.setBackground(darkBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rateButton.setBackground(mediumBlue);
            }
        });

        deliverButton = new JButton("Rate Delivery");
        deliverButton.setBackground(mediumBlue);
        deliverButton.setForeground(white);
        deliverButton.setFont(customFont.deriveFont(15f));
        deliverButton.setFocusable(false);
        deliverButton.setContentAreaFilled(false);
        deliverButton.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(lightGray, 1),
                                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        deliverButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deliverButton.addActionListener(e -> rateDelivery());
        deliverButton.setOpaque(true);

        deliverButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                deliverButton.setBackground(darkBlue);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                deliverButton.setBackground(darkBlue.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                deliverButton.setBackground(darkBlue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                deliverButton.setBackground(mediumBlue);
            }
        });

        titlePanel.add(titleLabel);
        buttonPanel.add(rateButton);
        buttonPanel.add(deliverButton);
        add(titlePanel, BorderLayout.NORTH);
        add(ratingPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        String[] columnNames = {"Product", "Details", "Price", "Quantity", "Subtotal"};
        displayOrderSummary(null, columnNames, customFont);
    }

    public void displayOrderSummary(Object[][] data, String[] columnNames, Font font) {
        if (data != null && data.length > 0) {
            for (Object[] row : data) {
                orderData.add(row);
            }
        }

        ratingPanel.removeAll();
        ratingPanel.setLayout(new BorderLayout());

        if (orderData.isEmpty()) {
            emptyLabel = new JLabel("No orders to rate yet.");
            emptyLabel.setFont(new Font(null, Font.BOLD, 25));
            emptyLabel.setForeground(white);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(0, 520, 0, 0));
            ratingPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            Object[][] allOrdersArray = new Object[orderData.size()][];
            for (int i = 0; i < orderData.size(); i++) {
                allOrdersArray[i] = orderData.get(i);
            }

            orderTable = new JTable(allOrdersArray, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                @Override
                public Class<?> getColumnClass(int column) {
                    return getValueAt(0, column).getClass();
                }
            };

            tableContainer = new JPanel();
            tableContainer.setLayout(new BorderLayout());
            tableContainer.setOpaque(false);
            tableContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            orderTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            orderTable.setRowHeight(120);
            orderTable.setFont(font.deriveFont(15f));
            orderTable.getTableHeader().setFont(font.deriveFont(20f));

            headerRenderer = (DefaultTableCellRenderer) orderTable.getTableHeader().getDefaultRenderer();
            headerRenderer.setHorizontalAlignment(JLabel.CENTER);

            orderTable.setBackground(lightGray);
            orderTable.setOpaque(true);

            leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            orderTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
            orderTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);

            orderTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            orderTable.getColumnModel().getColumn(1).setPreferredWidth(300);
            orderTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            orderTable.getColumnModel().getColumn(3).setPreferredWidth(80);
            orderTable.getColumnModel().getColumn(4).setPreferredWidth(100);

            tableScrollPane = new JScrollPane(orderTable);
            tableScrollPane.getViewport().setBackground(lightGray);
            tableScrollPane.setColumnHeaderView(orderTable.getTableHeader());
            tableScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
            tableScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JPanel() {
                {
                    setBackground(white);
                }
            });

            tableContainer.add(tableScrollPane, BorderLayout.CENTER);
            ratingPanel.add(tableContainer, BorderLayout.CENTER);
        }

        revalidate();
        repaint();
    }

    private boolean isFullyRated(String productName) {
        return ratedProducts.contains(productName) && ratedDeliveries.contains(productName);
    }

    private void removeFullyRatedItem(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < orderData.size()) {
            orderData.remove(rowIndex);
            String[] columnNames = {"Product", "Details", "Price", "Quantity", "Subtotal"};
            displayOrderSummary(null, columnNames, customFont);
        }
    }

    public class HintTextField extends JTextField {
        private final String hint;

        public HintTextField(String hint) {
            this.hint = hint;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (getText().isEmpty() && !hasFocus()) {
                Graphics2D graphics2d = (Graphics2D) g.create();
                graphics2d.setColor(lightGray);
                graphics2d.setFont(getFont().deriveFont(Font.ITALIC));
                graphics2d.drawString(hint, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
                graphics2d.dispose();
            }
        }
    }

    public void rateProduct() {
        if (orderData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no items to rate.",
                "No Items", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (orderTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to rate.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = orderTable.getSelectedRow();
        String productDetails = (String) orderTable.getValueAt(selectedRow, 1);
        String productName = productDetails.split("<br>")[0].replace("<html>", "").trim();

        if (ratedProducts.contains(productName)) {
            JOptionPane.showMessageDialog(this, "You have already rated this product.",
                "Product Rated", JOptionPane.INFORMATION_MESSAGE);

            if (isFullyRated(productName)) {
                removeFullyRatedItem(selectedRow);
            }
            return;
        }

        JPanel rateProductPanel = new JPanel(new BorderLayout(10, 10));

        JLabel productNameLabel = new JLabel(productName);
        productNameLabel.setFont(customFont.deriveFont(Font.BOLD, 16f));
        productNameLabel.setForeground(darkBlue);
        productNameLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray),
            BorderFactory.createEmptyBorder(0, 0, 10, 0)));

        JPanel starPanel = new JPanel();
        JButton[] stars = new JButton[5];
        final int[] rating = {0};

        for (int i = 0; i < 5; i++) {
            final int starIndex = i;
            stars[i] = new JButton("☆");
            stars[i].setFont(new Font("SansSerif", Font.PLAIN, 30));
            stars[i].setBorderPainted(false);
            stars[i].setContentAreaFilled(false);
            stars[i].setFocusPainted(false);
            stars[i].setOpaque(false);
            stars[i].setForeground(gold);

            stars[i].addActionListener(e -> {
                rating[0] = starIndex + 1;
                for (int j = 0; j < 5; j++) {
                    stars[j].setText(j <= starIndex ? "★" : "☆");
                }
            });
            starPanel.add(stars[i]);
        }

        HintTextField reviewField = new HintTextField("Write your review here...");
        reviewField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        reviewField.setPreferredSize(new Dimension(300, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel starLabel = new JLabel("Product Rating:");
        starLabel.setFont(customFont.deriveFont(15f));
        starLabel.setForeground(black);
        starLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        topPanel.add(productNameLabel, BorderLayout.NORTH);
        topPanel.add(starLabel, BorderLayout.CENTER);
        topPanel.add(starPanel, BorderLayout.SOUTH);

        JPanel reviewPanel = new JPanel(new BorderLayout());
        reviewPanel.setOpaque(false);

        JLabel reviewLabel = new JLabel("Write down your review:");
        reviewLabel.setFont(customFont.deriveFont(15f));
        reviewLabel.setForeground(black);
        reviewLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        reviewPanel.add(reviewLabel, BorderLayout.NORTH);
        reviewPanel.add(reviewField, BorderLayout.CENTER);

        rateProductPanel.add(topPanel, BorderLayout.NORTH);
        rateProductPanel.add(reviewPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, rateProductPanel,
            "Rate Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String reviewText = reviewField.getText().trim();

            if (rating[0] > 0 || !reviewText.isEmpty()) {
                JPanel confirmPanel = new JPanel();
                confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));

                JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel ratingLabel = new JLabel("Rating: ");
                ratingLabel.setFont(customFont.deriveFont(15f));
                ratingPanel.add(ratingLabel);

                for (int i = 0; i < 5; i++) {
                    JLabel star = new JLabel(i < rating[0] ? "★" : "☆");
                    star.setFont(new Font("SansSerif", Font.PLAIN, 20));
                    star.setForeground(gold);
                    ratingPanel.add(star);
                }
                confirmPanel.add(ratingPanel);

                JPanel reviewDisplayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                reviewDisplayPanel.setOpaque(false);

                JLabel reviewDisplayLabel = new JLabel("Review:");
                reviewDisplayLabel.setFont(customFont.deriveFont(15f));

                JLabel reviewResponse = new JLabel(reviewText.isEmpty() ? "None" : reviewText);
                reviewResponse.setFont(new Font("SansSerif", Font.PLAIN, 15));

                reviewDisplayPanel.add(reviewDisplayLabel);
                reviewDisplayPanel.add(reviewResponse);
                confirmPanel.add(reviewDisplayPanel);

                JOptionPane.showMessageDialog(this, confirmPanel,
                    "Product Rated", JOptionPane.INFORMATION_MESSAGE);

                ratedProducts.add(productName);

                if (isFullyRated(productName)) {
                    removeFullyRatedItem(selectedRow);
                }
            }
        }
    }

    public void rateDelivery() {
        if (orderData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no deliveries to rate.",
                "No Deliveries", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (orderTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to rate.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = orderTable.getSelectedRow();
        String productDetails = (String) orderTable.getValueAt(selectedRow, 1);
        String productName = productDetails.split("<br>")[0].replace("<html>", "").trim();

        if (ratedDeliveries.contains(productName)) {
            JOptionPane.showMessageDialog(this, "You have already rated this product.",
                "Delivery Rated", JOptionPane.INFORMATION_MESSAGE);

            if (isFullyRated(productName)) {
                removeFullyRatedItem(selectedRow);
            }
            return;
        }

        JPanel rateDeliveryPanel = new JPanel(new BorderLayout(10, 10));

        JLabel productNameLabel = new JLabel(productName);
        productNameLabel.setFont(customFont.deriveFont(Font.BOLD, 16f));
        productNameLabel.setForeground(darkBlue);
        productNameLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)));

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        questionPanel.setOpaque(false);

        JRadioButton q1 = new JRadioButton("Was the delivery faster than expected?");
        JRadioButton q2 = new JRadioButton("Is the packaging of the product in good condition?");
        JRadioButton q3 = new JRadioButton("Was the delivery driver's behavior professional?");
        JRadioButton q4 = new JRadioButton("Are you satisfied with the overall delivery process?");

        for (JRadioButton button : new JRadioButton[]{q1, q2, q3, q4}) {
            button.setFont(customFont.deriveFont(15f));
            button.setOpaque(false);
            button.setFocusable(false);
            questionPanel.add(button);
        }

        JPanel reviewPanel = new JPanel(new BorderLayout());
        reviewPanel.setOpaque(false);

        JLabel reviewLabel = new JLabel("Leave a review!");
        reviewLabel.setFont(customFont.deriveFont(15f));
        reviewLabel.setForeground(black);
        reviewLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        HintTextField reviewField = new HintTextField("Write your review here...");
        reviewField.setFont(customFont.deriveFont(15f));
        reviewField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(lightGray),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        reviewField.setOpaque(false);

        reviewPanel.add(reviewLabel, BorderLayout.NORTH);
        reviewPanel.add(reviewField, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(productNameLabel, BorderLayout.NORTH);
        topPanel.add(questionPanel, BorderLayout.CENTER);
        topPanel.add(reviewPanel, BorderLayout.SOUTH);

        rateDeliveryPanel.add(topPanel, BorderLayout.NORTH);

        int result = JOptionPane.showConfirmDialog(this, rateDeliveryPanel,
            "Rate Delivery", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            boolean anySelected = q1.isSelected() || q2.isSelected() || q3.isSelected() || q4.isSelected();
            String reviewText = reviewField.getText().trim();

            if (!anySelected && reviewText.isEmpty()) {
                return;
            }

            ImageIcon checkIcon = new ImageIcon(getClass().getResource("/images/check.png"));
            ImageIcon xIcon = new ImageIcon(getClass().getResource("/images/x.png"));
            int iconSize = 20;
            checkIcon = new ImageIcon(checkIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
            xIcon = new ImageIcon(xIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));

            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
            messagePanel.setOpaque(false);

            JLabel titleLabel = new JLabel("Delivery rated!");
            titleLabel.setFont(customFont.deriveFont(15f));
            messagePanel.add(titleLabel);
            messagePanel.add(Box.createVerticalStrut(10));

            messagePanel.add(createRow("Swift Delivery", q1.isSelected(), checkIcon, xIcon));
            messagePanel.add(createRow("Product in Good Condition", q2.isSelected(), checkIcon, xIcon));
            messagePanel.add(createRow("Professional Driver Behavior", q3.isSelected(), checkIcon, xIcon));
            messagePanel.add(createRow("Satisfactory Delivery", q4.isSelected(), checkIcon, xIcon));

            JLabel review = new JLabel("Review: ");
            review.setFont(customFont.deriveFont(15f));
            review.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            JLabel userReview = new JLabel(reviewText.isEmpty() ? "None" : reviewText);
            userReview.setFont(new Font("SansSerif", Font.PLAIN, 15));

            messagePanel.add(review);
            messagePanel.add(userReview);

            JOptionPane.showMessageDialog(this, messagePanel,
                "Delivery Review", JOptionPane.INFORMATION_MESSAGE);

            ratedDeliveries.add(productName);

            if (isFullyRated(productName)) {
                removeFullyRatedItem(selectedRow);
            }
        }
    }

    private JLabel createRow(String text, boolean selected, ImageIcon checkIcon, ImageIcon xIcon) {
        JLabel label = new JLabel(text, selected ? checkIcon : xIcon, JLabel.LEADING);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        return label;
    }
}
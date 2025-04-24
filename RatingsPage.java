import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RatingsPage extends JPanel {
    private Font customFont;
    private JPanel ratingPanel, titlePanel, buttonPanel;
    private JLabel titleLabel, emptyLabel;
    private JTable orderTable;
    private JScrollPane tableScrollPane;
    private JButton rateButton;
    private List<Object[]> orderData; 
    private DefaultTableCellRenderer headerRenderer, leftRenderer;

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
            customFont = new Font("Serif", Font.ITALIC, 40);
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

        ratingPanel.setOpaque(true);
        ratingPanel.setLayout(new BorderLayout());

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
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(lightBlue);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

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

        titlePanel.add(titleLabel);
        buttonPanel.add(rateButton);
        add(titlePanel, BorderLayout.NORTH);
        add(ratingPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        String[] columnNames = {"Product", "Details", "Price", "Quantity", "Subtotal"};

        displayOrderSummary(null, columnNames, customFont);
    }

    public void displayOrderSummary(Object[][] data, String[] columnNames, Font font) {
        if(data != null && data.length > 0) {
            for (Object[] row : data) {
                orderData.add(row);
            }
        }

        ratingPanel.removeAll();

        if(orderData.isEmpty()) {
            emptyLabel = new JLabel("No orders to rate yet.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font(null, Font.BOLD, 20));
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            emptyLabel.setForeground(white);
            emptyLabel.setOpaque(false);
            ratingPanel.add(emptyLabel, BorderLayout.NORTH);
        } else {
            Object[][] allOrdersArray = new Object[orderData.size()][];
            for(int i = 0; i < orderData.size(); i++) {
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

            orderTable.setRowHeight(120); 
            orderTable.setFont(customFont.deriveFont(15f));
            orderTable.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
            orderTable.getTableHeader().setFont(customFont.deriveFont(20f));

            headerRenderer = (DefaultTableCellRenderer)
            orderTable.getTableHeader().getDefaultRenderer();
            headerRenderer.setHorizontalAlignment(JLabel.CENTER);

            orderTable.setBackground(lightGray);
            orderTable.setOpaque(true);

            leftRenderer = new DefaultTableCellRenderer();
            leftRenderer.setHorizontalAlignment(JLabel.LEFT);
            orderTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);

            orderTable.getColumnModel().getColumn(0).setPreferredWidth(120); 
            orderTable.getColumnModel().getColumn(1).setPreferredWidth(200); 
            orderTable.getColumnModel().getColumn(2).setPreferredWidth(100); 
            orderTable.getColumnModel().getColumn(3).setPreferredWidth(100); 
            orderTable.getColumnModel().getColumn(4).setPreferredWidth(100); 

            tableScrollPane = new JScrollPane(orderTable);
            tableScrollPane.getViewport().setBackground(lightGray);
            tableScrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
            tableScrollPane.setOpaque(false);

            ratingPanel.add(tableScrollPane, BorderLayout.CENTER);
        }

        revalidate();
        repaint();
    }

    public class HintTextField extends JTextField {
        private final String hint;

        public HintTextField(String hint) {
            this.hint = hint;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(getText().isEmpty() && !hasFocus()) {
                Graphics2D graphics2d = (Graphics2D) g.create();
                graphics2d.setColor(lightGray);
                graphics2d.setFont(getFont().deriveFont(Font.ITALIC));
                graphics2d.drawString(hint, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
                graphics2d.dispose();
            }
        }
    }

    public void rateProduct() {
        if (orderTable == null || orderTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "There are no items to be rated.", "Cart Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(orderTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to rate.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        JPanel rateProductPanel = new JPanel();
        rateProductPanel.setLayout(new BorderLayout(10, 10));
    
        int selectedRow = orderTable.getSelectedRow();
        String productDetails = (String) orderTable.getValueAt(selectedRow, 1);
        String productName = productDetails.split("<br>")[0].replace("<html>", "").trim();

        JLabel productNameLabel = new JLabel(productName);
        productNameLabel.setFont(customFont.deriveFont(Font.BOLD, 16f));
        productNameLabel.setForeground(darkBlue);
        productNameLabel.setBorder(BorderFactory.createCompoundBorder(
                                   BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray),
                                   BorderFactory.createEmptyBorder(0, 0, 10, 0)));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel starLabel = new JLabel("Product Rating:");
        starLabel.setFont(customFont.deriveFont(15f));
        starLabel.setForeground(black);
        starLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JPanel starPanel = new JPanel();
        starPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray));
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
            topPanel.add(productNameLabel, BorderLayout.NORTH);
            topPanel.add(starLabel, BorderLayout.CENTER);
            topPanel.add(starPanel, BorderLayout.SOUTH);
        }

        JPanel reviewPanel = new JPanel();
        reviewPanel.setLayout(new BorderLayout());
        reviewPanel.setOpaque(false);

        JLabel reviewLabel = new JLabel("Write down your review:");
        reviewLabel.setFont(customFont.deriveFont(15f));
        reviewLabel.setForeground(black);
        reviewLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        HintTextField reviewField = new HintTextField("Write your review here...");
        reviewField.setFont(new Font(null, Font.PLAIN, 15));
        reviewField.setPreferredSize(new Dimension(300, 30));
            
        rateProductPanel.add(topPanel, BorderLayout.NORTH);
        reviewPanel.add(reviewLabel, BorderLayout.NORTH);
        reviewPanel.add(reviewField, BorderLayout.CENTER);
        rateProductPanel.add(reviewPanel, BorderLayout.CENTER);
    
        int result = JOptionPane.showConfirmDialog(this, rateProductPanel, "Rate Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            String reviewText = reviewField.getText().trim();
            
            JPanel confirmPanel = new JPanel();
            confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));

            JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel ratingLabel = new JLabel("Rating: ");
            ratingLabel.setFont(customFont.deriveFont(15f));
            ratingPanel.add(ratingLabel);

            for(int i = 0; i < 5; i++) {
                JLabel star = new JLabel(i < rating[0] ? "★" : "☆");
                star.setFont(new Font("SansSerif", Font.PLAIN, 20));
                star.setForeground(gold);
                ratingPanel.add(star);
            }
            
            confirmPanel.add(ratingPanel);

            JPanel reviewDisplayPanel = new JPanel();
            reviewDisplayPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            reviewDisplayPanel.setOpaque(false);

            JLabel reviewDisplayLabel = new JLabel("Review: " + (reviewText.isEmpty() ? "None" : reviewText));
            reviewDisplayLabel.setFont(customFont.deriveFont(15f));
            reviewDisplayPanel.add(reviewDisplayLabel);
            confirmPanel.add(reviewDisplayPanel);

            JOptionPane.showMessageDialog(this, confirmPanel, "Product Rated", JOptionPane.INFORMATION_MESSAGE);
            orderData.remove(selectedRow);
            displayOrderSummary(null, new String[]{"Product", "Details", "Price", "Quantity", "Subtotal"}, customFont);
        }
    }
}    

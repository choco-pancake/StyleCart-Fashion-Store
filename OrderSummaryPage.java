import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderSummaryPage extends JPanel {
    private Font customFont;
    private JPanel orderPanel, titlePanel, buttonPanel;
    private JLabel titleLabel, emptyLabel;
    private DefaultTableCellRenderer headerRenderer, leftRenderer;
    private JScrollPane scrollPane;
    private JButton receiptButton, trackButton;
    private List<Object[]> allOrders;

    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color lightGray = new Color(211, 211, 211);
    private Color mediumBlue = new Color(125, 125, 200);

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

            headerRenderer = (DefaultTableCellRenderer)
            summaryTable.getTableHeader().getDefaultRenderer();
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
            displayOrderSummary(null, new String[] {"Product", "Details", "Price", "Quantity", "Subtotal"}, customFont);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating receipt!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void trackOrder() {

    }
}
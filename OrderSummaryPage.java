import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderSummaryPage extends JPanel {
    private Font customFont;
    private JPanel orderPanel, titlePanel, receiptPanel;
    private JLabel titleLabel, emptyLabel;
    private DefaultTableCellRenderer headerRenderer, leftRenderer;
    private JScrollPane scrollPane;
    private JButton receiptButton;
    private List<Object[]> allOrders;

    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color white = Color.WHITE;
    private Color lightGray = new Color(211, 211, 211);
    private Color mediumBlue = new Color(124, 145, 190);

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

        receiptPanel = new JPanel();
        receiptPanel.setLayout(new FlowLayout());
        receiptPanel.setBackground(lightBlue);
        receiptPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        receiptPanel.setOpaque(true);

        receiptButton = new JButton("Generate Receipt");
        receiptButton.setBackground(mediumBlue);
        receiptButton.setForeground(white);
        receiptButton.setFont(customFont.deriveFont(15f));
        receiptButton.setFocusable(false);
        receiptButton.setContentAreaFilled(false);
        receiptButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(lightGray, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        receiptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        receiptPanel.add(receiptButton);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        add(orderPanel, BorderLayout.CENTER);
        add(receiptPanel, BorderLayout.SOUTH);
    }

    public void displayOrderSummary(Object[][] selectedData, String[] columnNames, Font tableFont) {
        if (selectedData != null && selectedData.length > 0) {
            for (Object[] row : selectedData) {
                allOrders.add(row);
            }
        }

        orderPanel.removeAll();
        orderPanel.setLayout(new BorderLayout());

        if (allOrders.isEmpty()) {
            emptyLabel = new JLabel("No items in order summary.");
            emptyLabel.setFont(new Font(null, Font.BOLD, 20));
            emptyLabel.setForeground(white);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            orderPanel.add(emptyLabel, BorderLayout.CENTER);
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

            summaryTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Product
            summaryTable.getColumnModel().getColumn(1).setPreferredWidth(300); // Details
            summaryTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Price
            summaryTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Quantity
            summaryTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Subtotal

            scrollPane = new JScrollPane(summaryTable);
            scrollPane.getViewport().setBackground(lightGray);

            orderPanel.add(scrollPane, BorderLayout.CENTER);
        }

        revalidate();
        repaint();
    }
}
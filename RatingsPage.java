import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RatingsPage extends JPanel {
    private Font customFont;
    private JPanel ratingPanel, titlePanel;
    private JLabel titleLabel;
    private JLabel emptyLabel;
    private JTable orderTable;
    private JScrollPane tableScrollPane;
    private List<Object[]> orderData; 

    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color lightGray = new Color(211, 211, 211);
    private Color white = Color.WHITE;

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

        emptyLabel = new JLabel("No orders to rate yet.", SwingConstants.CENTER);
        emptyLabel.setFont(new Font(null, Font.BOLD, 20));
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        emptyLabel.setForeground(white);
        emptyLabel.setOpaque(false);

        String[] columnNames = {"Product", "Details", "Price", "Quantity", "Subtotal"};
        orderTable = new JTable(new Object[0][5], columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return ImageIcon.class; 
                return Object.class; 
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        orderTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        orderTable.setRowHeight(120); 
        orderTable.setFont(customFont.deriveFont(15f));
        orderTable.getTableHeader().setFont(customFont.deriveFont(20f));

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer)
        orderTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        orderTable.setBackground(lightGray);
        orderTable.setOpaque(true);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        orderTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);

        orderTable.getColumnModel().getColumn(0).setPreferredWidth(120); 
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(200); 
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(100); 
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(100); 
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(100); 

        tableScrollPane = new JScrollPane(orderTable);
        tableScrollPane.getViewport().setBackground(lightGray);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tableScrollPane.setOpaque(false);

        updateDisplay();

        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(darkBlue);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, lightGray));

        titleLabel = new JLabel(" Ratings");
        titleLabel.setFont(customFont);
        titleLabel.setForeground(white);
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));

        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        add(ratingPanel, BorderLayout.CENTER);
    }

    public void displayOrderSummary(Object[][] data, String[] columnNames, Font font) {
        for (Object[] row : data) {
            orderData.add(row);
        }

        Object[][] tableData = orderData.toArray(new Object[0][0]);
        orderTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return ImageIcon.class;
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        orderTable.setRowHeight(120);
        orderTable.setFont(font.deriveFont(16f));
        orderTable.getTableHeader().setFont(font.deriveFont(18f));
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        updateDisplay();
    }

    private void updateDisplay() {
        ratingPanel.removeAll();

        if (orderData.isEmpty()) {
            ratingPanel.add(emptyLabel, BorderLayout.NORTH);
        } else {
            ratingPanel.add(tableScrollPane, BorderLayout.CENTER);
        }

        ratingPanel.revalidate();
        ratingPanel.repaint();
    }
}
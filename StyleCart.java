import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.BufferedImage;

public class StyleCart {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                new StartPage().setVisible(true);
            }
        });
    }
}

class StartPage extends JFrame {
    private JPanel logoPanel;
    private JPanel gradientPanel; 
    private JLabel titleLabel;    
    private JButton nextButton;
    private BufferedImage logo;
    private Font customFont;
    private Font customFont2;
    private boolean useCurvedText = true;

    private Color darkBlue = new Color(75, 75, 150);
    private Color lightBlue = new Color(173, 216, 230);
    private Color gold = new Color(255, 233, 0);
    private Color lightgray = new Color(105, 105, 105);
    private Color black = Color.BLACK;
    private Color brown = new Color(138, 109, 70);
    private Color white = Color.WHITE;

    public StartPage() {
        setTitle("StyleCart Fashion Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 670);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    
        try {
            logo = ImageIO.read(new File("images/logo.jpg"));
            setIconImage(logo);
        } catch(IOException e) {
            e.printStackTrace();
        }
    
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Dancing.ttf")).deriveFont(40f);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(customFont);

            customFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MontserratMedium.ttf")).deriveFont(17f);
            GraphicsEnvironment environment2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment2.registerFont(customFont2);
        } catch(FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.ITALIC, 40);
            customFont2 = new Font("Serif", Font.ITALIC, 40);
        }

        logoPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2D = (Graphics2D) g;    
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);    
                graphics2D.setColor(darkBlue); 
                graphics2D.fillRect(0, 0, getWidth(), getHeight());
                            
                Path2D path = new Path2D.Double();
                path.moveTo(0, 0);
                path.lineTo(0, 250);
                path.quadTo(getWidth() / 2.0, 350, getWidth(), 250);
                path.lineTo(getWidth(), 0);
                path.closePath();
    
                graphics2D.setColor(brown);
                graphics2D.fill(path);
    
                if (logo != null) {
                    int logoWidth = logo.getWidth();
                    int logoHeight = logo.getHeight();
    
                    int newWidth = 200;
                    int newHeight = (int) ((double) newWidth / logoWidth * logoHeight);
    
                    int x = (getWidth() - newWidth) / 2;
                    int y = 50;
                    graphics2D.drawImage(logo, x, y, newWidth, newHeight, null);
                }
    
                if (useCurvedText) {
                    String text = "Style Cart  Fashion  Store";
                    graphics2D.setFont(customFont);
                    graphics2D.setColor(gold);
    
                    int textY = 330;
    
                    QuadCurve2D curve = new QuadCurve2D.Float(
                        100, textY,                   
                        getWidth() / 2, textY + 65,   
                        getWidth() - 100, textY       
                    );
    
                    FontMetrics metrics = graphics2D.getFontMetrics();
                    float curveLength = (float) approximateLength(curve, 100);
                    float textWidth = metrics.stringWidth(text);
                    float startPos = (curveLength - textWidth) / 2; 
    
                    float currentPos = 0;
                    for (int i = 0; i < text.length(); i++) {
                        char c = text.charAt(i);
                        int charWidth = metrics.charWidth(c);
            
                        float t = (startPos + currentPos) / curveLength;
                        if (t < 0) t = 0;
                        if (t > 1) t = 1;
            
                        Point2D p = getPointOnCurve(curve, t);
                        double tangentAngle = getTangentAngle(curve, t);
            
                        AffineTransform originalTransform = graphics2D.getTransform();
            
                        graphics2D.translate(p.getX(), p.getY());
                        graphics2D.rotate(tangentAngle);            
                        graphics2D.drawString(String.valueOf(c), -charWidth/2, 0);            
                        graphics2D.setTransform(originalTransform);
            
                        currentPos += charWidth;
                    }
                }
    
                String subtitle = "Your unique style, delivered to you.";                
                graphics2D.setFont(customFont2);
                graphics2D.setColor(Color.WHITE);

                FontMetrics fm = graphics2D.getFontMetrics();
                int straightTextWidth = fm.stringWidth(subtitle);
                int x = (getWidth() - straightTextWidth) / 2;
                int y = 395; 

                graphics2D.drawString(subtitle, x, y);
                graphics2D.dispose();
            }
    
            private double approximateLength(QuadCurve2D curve, int steps) {
                double length = 0;
                Point2D prev = new Point2D.Double(curve.getX1(), curve.getY1());
                
                for (int i = 1; i <= steps; i++) {
                    double t = i / (double) steps;
                    Point2D curr = getPointOnCurve(curve, t);
                    length += prev.distance(curr);
                    prev = curr;
                }
                
                return length;
            }
            
            private Point2D getPointOnCurve(QuadCurve2D curve, double t) {
                double x = (1-t)*(1-t)*curve.getX1() + 2*(1-t)*t*curve.getCtrlX() + t*t*curve.getX2();
                double y = (1-t)*(1-t)*curve.getY1() + 2*(1-t)*t*curve.getCtrlY() + t*t*curve.getY2();
                return new Point2D.Double(x, y);
            }
            
            private double getTangentAngle(QuadCurve2D curve, double t) {
                double dx = 2*(1-t)*(curve.getCtrlX()-curve.getX1()) + 2*t*(curve.getX2()-curve.getCtrlX());
                double dy = 2*(1-t)*(curve.getCtrlY()-curve.getY1()) + 2*t*(curve.getY2()-curve.getCtrlY());
                return Math.atan2(dy, dx);
            }
        };
    
        logoPanel.setPreferredSize(new Dimension(getWidth(), 400));

        gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2D = (Graphics2D) g;

                GradientPaint gradient = new GradientPaint(0, 0, darkBlue, 0, getHeight(), lightBlue);
                graphics2D.setPaint(gradient);
                graphics2D.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        gradientPanel.setLayout(new BoxLayout(gradientPanel, BoxLayout.Y_AXIS));
        
        titleLabel = new JLabel();
        titleLabel.setFont(customFont);
     
        nextButton = new JButton("→") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D graphics2D = (Graphics2D) g;
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);                
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                graphics2D.fillRect(0, 0, getWidth(), getHeight());
                graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                
                int width = getWidth();
                int height = getHeight();
                int diameter = Math.min(width, height) - 4; 

                graphics2D.setColor(lightgray);
                graphics2D.setFont(new Font("SansSerif", Font.BOLD, 30));
                graphics2D.fill(new Ellipse2D.Double(4, 4, diameter, diameter));
                
                if(getModel().isPressed()) {
                    graphics2D.setColor(darkBlue.darker());
                } else if (getModel().isRollover()){
                    graphics2D.setColor(darkBlue);
                } else {
                    graphics2D.setColor(lightBlue);
                }
                graphics2D.fill(new Ellipse2D.Double(2, 2, diameter, diameter));
                
                if(getModel().isPressed() || getModel().isRollover()) {
                    graphics2D.setColor(white);
                } else {
                    graphics2D.setColor(black);
                }

                FontMetrics metrics = graphics2D.getFontMetrics();
                int textWidth = metrics.stringWidth(getText());
                int x = (width - textWidth) / 2;
                int y = (height + metrics.getAscent() - metrics.getDescent()) / 2;
                graphics2D.drawString(getText(), x, y);
            }
            
            @Override
            public boolean contains(int x, int y) {
                int size = Math.min(getWidth(), getHeight());
                double radius = size / 2.0;
                double centerX = getWidth() / 2.0;
                double centerY = getHeight() / 2.0;
                
                return Point.distance(centerX, centerY, x, y) <= radius;
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(60, 60); 
            }
            
            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
            
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        
        nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));       
        nextButton.setFocusable(false);      
        nextButton.setOpaque(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.setBackground(new Color(0, 0, 0, 0));      
        nextButton.setFont(new Font(null, Font.BOLD, 30));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomePage();
            }
        });
        
        add(logoPanel, BorderLayout.NORTH);
        gradientPanel.add(Box.createVerticalStrut(90));        
        gradientPanel.add(nextButton);
        gradientPanel.add(Box.createVerticalGlue());
        add(gradientPanel, BorderLayout.CENTER);
        
    }
}
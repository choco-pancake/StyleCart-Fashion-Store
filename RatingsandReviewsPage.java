import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RatingsandReviewsPage {
    private static int rating = 0;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Product Rating and Review");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Product Image
        ImageIcon icon = new ImageIcon("sample-product.jpg"); // Add your image path here
        JLabel picLabel = new JLabel(icon);
        frame.add(picLabel);

        // Product Name and Price
        JLabel nameLabel = new JLabel("Item: Wireless Earbuds");
        JLabel priceLabel = new JLabel("Price: ₱999.00");
        frame.add(nameLabel);
        frame.add(priceLabel);

        // Stars for rating
        JLabel ratingLabel = new JLabel("Rate this item:");
        frame.add(ratingLabel);

        JPanel starPanel = new JPanel();
        JButton[] stars = new JButton[5];
        for (int i = 0; i < 5; i++) {
            final int starIndex = i;
            stars[i] = new JButton("☆");
            stars[i].setFont(new Font("SansSerif", Font.PLAIN, 30));
            stars[i].setBorderPainted(false);
            stars[i].setContentAreaFilled(false);

            stars[i].addActionListener(e -> {
                rating = starIndex + 1;
                for (int j = 0; j < 5; j++) {
                    stars[j].setText(j <= starIndex ? "★" : "☆");
                }
            });

            starPanel.add(stars[i]);
        }
        frame.add(starPanel);

        // Review Text Area
        JLabel reviewLabel = new JLabel("Your review:");
        JTextArea reviewArea = new JTextArea(5, 30);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        frame.add(reviewLabel);
        frame.add(new JScrollPane(reviewArea));

        // Submit Button
        JButton submitButton = new JButton("Submit Review");
        submitButton.addActionListener(e -> {
            String review = reviewArea.getText();
            System.out.println("Rating: " + rating + " star(s)");
            System.out.println("Review: " + review);
            JOptionPane.showMessageDialog(frame, "Thank you for your feedback!");
        });
        frame.add(submitButton);

        frame.setVisible(true);
    }
}
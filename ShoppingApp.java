import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ShoppingApp {
    private static final List<Product> products = new ArrayList<>();
    private static final List<Product> cart = new ArrayList<>();
    private static JPanel cartButtonPanel; // Declare cartButtonPanel here

    static {
        // Dummy data for products
        products.add(new Product("Product 1", "Description 1", 10.99, "/product1.jpg"));
        products.add(new Product("Product 2", "Description 2", 15.99, "/product2.jpg"));
        products.add(new Product("Product 3", "Description 3", 20.99, "/product3.jpg"));
        products.add(new Product("Product 4", "Description 4", 25.99, "/product4.jpg"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Shopping App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600); // Fixed resolution
            frame.setLocationRelativeTo(null); // Center the frame

            JPanel mainPanel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Load the background image
                    try {
                        BufferedImage backgroundImage = ImageIO.read(ShoppingApp.class.getResourceAsStream("/background.jpg"));
                        // Draw the background image with low opacity
                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            // Product display panel
            JPanel productPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            for (Product product : products) {
                productPanel.add(createProductPanel(product, frame));
            }
            JScrollPane productScrollPane = new JScrollPane(productPanel);
            mainPanel.add(productScrollPane, BorderLayout.CENTER);

            // Shopping cart panel
            JPanel cartPanel = new JPanel(new BorderLayout());
            cartButtonPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // Initialize cartButtonPanel here
            JButton checkoutButton = new JButton("Checkout");
            checkoutButton.addActionListener(e -> {
                // Process checkout
                double totalPrice = 0;
                for (Product product : cart) {
                    totalPrice += product.getPrice();
                }
                JOptionPane.showMessageDialog(frame, "Total Price: $" + totalPrice);
                cart.clear();
                cartButtonPanel.removeAll();
                cartButtonPanel.revalidate();
                cartButtonPanel.repaint();
            });
            cartPanel.add(new JLabel("Shopping Cart"), BorderLayout.NORTH);
            cartPanel.add(cartButtonPanel, BorderLayout.CENTER);
            cartPanel.add(checkoutButton, BorderLayout.SOUTH);
            mainPanel.add(cartPanel, BorderLayout.EAST);

            frame.add(mainPanel);
            frame.setVisible(true);

            // Set font for the entire application
            setUIFont(new javax.swing.plaf.FontUIResource("Copperplate Gothic", Font.PLAIN, 14));
        });
    }

    private static JPanel createProductPanel(Product product, JFrame parentFrame) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel nameLabel = new JLabel(product.getName());
        panel.add(nameLabel, BorderLayout.NORTH);

        JLabel priceLabel = new JLabel("$" + product.getPrice());
        panel.add(priceLabel, BorderLayout.SOUTH);

        JButton detailsButton = new JButton();
        detailsButton.setPreferredSize(new Dimension(200, 200)); // Set preferred size for the button
        BufferedImage image = loadImage(product.getImage());
        if (image != null) {
            // Scale the image to fit the button
            Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            detailsButton.setIcon(icon);
        } else {
            detailsButton.setText("Image not available");
        }
        detailsButton.addActionListener(e -> {
            // Show product details
            JOptionPane.showMessageDialog(parentFrame, product.getDescription(), "Product Details", JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(detailsButton, BorderLayout.CENTER);

        JButton addButton = new JButton("Add to Cart");
        addButton.addActionListener(e -> {
            cart.add(product);
            JButton cartButton = new JButton(product.getName());
            cartButton.addActionListener(ev -> {
                JOptionPane.showMessageDialog(parentFrame, "Added " + product.getName() + " to cart.");
            });
            cartButtonPanel.add(cartButton);
            cartButtonPanel.revalidate();
            cartButtonPanel.repaint();
            JOptionPane.showMessageDialog(null, product.getName() + " added to cart.");
        });
        panel.add(addButton, BorderLayout.SOUTH); // Placing buttons below the image

        return panel;
    }

    private static BufferedImage loadImage(String imageName) {
        try {
            return ImageIO.read(ShoppingApp.class.getResourceAsStream(imageName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static class Product {
        private final String name;
        private final String description;
        private final double price;
        private final String image;

        public Product(String name, String description, double price, String image) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public double getPrice() {
            return price;
        }

        public String getImage() {
            return image;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Method to set the font for the entire application
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
}

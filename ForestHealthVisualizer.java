import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Main Class
public class ForestHealthVisualizer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CountrySelectionPage());
    }
}

// Database Connector Class
class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/forest_db";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "Geetha@1517"; // Replace with your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// Country Selection Page
class CountrySelectionPage extends JFrame {
    private JComboBox<String> countryComboBox;

    public CountrySelectionPage() {
        setTitle("Select Country");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set Layout
        setLayout(new BorderLayout());

        // Background Image
        JLabel background = new JLabel(new ImageIcon("forest.jpg")); // Replace with your image path
        background.setLayout(new GridBagLayout());
        add(background);

        // Components Panel with Transparent Background
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Select a Country:");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE); // Ensure visibility over background
        panel.add(label, gbc);

        gbc.gridy++;
        countryComboBox = new JComboBox<>();
        loadCountries();
        countryComboBox.setPreferredSize(new Dimension(200, 30));
        panel.add(countryComboBox, gbc);

        gbc.gridy++;
        JButton nextButton = new JButton("Next");
        nextButton.setPreferredSize(new Dimension(100, 30));
        panel.add(nextButton, gbc);

        background.add(panel);

        // Action Listener for Next Button
        nextButton.addActionListener(e -> {
            String selectedCountry = (String) countryComboBox.getSelectedItem();
            if (selectedCountry != null) {
                new ForestSelectionPage(selectedCountry);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a country.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void loadCountries() {
        String query = "SELECT country_name FROM countries";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                countryComboBox.addItem(rs.getString("country_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading countries: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Forest Selection Page
class ForestSelectionPage extends JFrame {
    private JComboBox<String> forestComboBox;
    private String selectedCountry;

    public ForestSelectionPage(String country) {
        this.selectedCountry = country;

        setTitle("Select Forest in " + country);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set Layout
        setLayout(new BorderLayout());

        // Background Image
        JLabel background = new JLabel(new ImageIcon("forest.jpg")); // Replace with your image path
        background.setLayout(new GridBagLayout());
        add(background);

        // Components Panel with Transparent Background
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Select a Forest:");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        panel.add(label, gbc);

        gbc.gridy++;
        forestComboBox = new JComboBox<>();
        loadForests();
        forestComboBox.setPreferredSize(new Dimension(200, 30));
        panel.add(forestComboBox, gbc);

        gbc.gridy++;
        JButton nextButton = new JButton("Show Health");
        nextButton.setPreferredSize(new Dimension(150, 30));
        panel.add(nextButton, gbc);

        background.add(panel);

        // Action Listener for Next Button
        nextButton.addActionListener(e -> {
            String selectedForest = (String) forestComboBox.getSelectedItem();
            if (selectedForest != null) {
                new ForestHealthPage(selectedForest);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a forest.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void loadForests() {
        String query = "SELECT f.forest_name FROM forests f JOIN countries c ON f.country_id = c.country_id WHERE c.country_name = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, selectedCountry);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                forestComboBox.addItem(rs.getString("forest_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading forests: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Forest Health Page
class ForestHealthPage extends JFrame {
    private String selectedForest;

    public ForestHealthPage(String forest) {
        this.selectedForest = forest;

        setTitle("Health Data for " + forest);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set Layout
        setLayout(new BorderLayout());

        // Background Image
        JLabel background = new JLabel(new ImageIcon("forest6.jpg")); // Replace with your image path
        background.setLayout(new GridBagLayout());
        add(background);

        // Components Panel with Transparent Background
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,15,15,15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Labels and Data
        JLabel treeDensityLabel = new JLabel("Tree Density:");
        treeDensityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        treeDensityLabel.setForeground(Color.white);
        panel.add(treeDensityLabel, gbc);

        gbc.gridx = 1;
        JLabel treeDensityValue = new JLabel();
        treeDensityValue.setFont(new Font("Arial", Font.PLAIN, 16));
        treeDensityValue.setForeground(Color.WHITE);
        panel.add(treeDensityValue, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel airQualityLabel = new JLabel("Air Quality:");
        airQualityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        airQualityLabel.setForeground(Color.WHITE);
        panel.add(airQualityLabel, gbc);

        gbc.gridx = 1;
        JLabel airQualityValue = new JLabel();
        airQualityValue.setFont(new Font("Arial", Font.PLAIN, 16));
        airQualityValue.setForeground(Color.WHITE);
        panel.add(airQualityValue, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel biodiversityLabel = new JLabel("Biodiversity:");
        biodiversityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        biodiversityLabel.setForeground(Color.WHITE);
        panel.add(biodiversityLabel, gbc);

        gbc.gridx = 1;
        JLabel biodiversityValue = new JLabel();
        biodiversityValue.setFont(new Font("Arial", Font.PLAIN, 16));
        biodiversityValue.setForeground(Color.WHITE);
        panel.add(biodiversityValue, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel avgHealthLabel = new JLabel("Average Health:");
        avgHealthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        avgHealthLabel.setForeground(Color.WHITE);
        panel.add(avgHealthLabel, gbc);

        gbc.gridx = 1;
        JLabel avgHealthValue = new JLabel();
        avgHealthValue.setFont(new Font("Arial", Font.PLAIN, 16));
        avgHealthValue.setForeground(Color.WHITE);
        panel.add(avgHealthValue, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel latitudeLabel = new JLabel("Latitude:");
        latitudeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        latitudeLabel.setForeground(Color.WHITE);
        panel.add(latitudeLabel, gbc);

        gbc.gridx = 1;
        JLabel latitudeValue = new JLabel();
        latitudeValue.setFont(new Font("Arial", Font.PLAIN, 16));
        latitudeValue.setForeground(Color.WHITE);
        panel.add(latitudeValue, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel longitudeLabel = new JLabel("Longitude:");
        longitudeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        longitudeLabel.setForeground(Color.WHITE);
        panel.add(longitudeLabel, gbc);

        gbc.gridx = 1;
        JLabel longitudeValue = new JLabel();
        longitudeValue.setFont(new Font("Arial", Font.PLAIN, 16));
        longitudeValue.setForeground(Color.WHITE);
        panel.add(longitudeValue, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton backButton = new JButton("Back to Countries");
        panel.add(backButton, gbc);

        background.add(panel);

        // Load Health Data
        loadHealthData(treeDensityValue, airQualityValue, biodiversityValue, avgHealthValue, latitudeValue, longitudeValue);

        // Action Listener for Back Button
        backButton.addActionListener(e -> {
            new CountrySelectionPage();
            dispose();
        });

        setVisible(true);
    }

    private void loadHealthData(JLabel treeDensity, JLabel airQuality, JLabel biodiversity, JLabel avgHealth, JLabel latitude, JLabel longitude) {
        String query = "SELECT fh.tree_density, fh.air_quality, fh.biodiversity, fh.avg_health, fh.latitude, fh.longitude " +
                       "FROM forest_health fh " +
                       "JOIN forests f ON fh.forest_id = f.forest_id " +
                       "WHERE f.forest_name = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, selectedForest);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                treeDensity.setText(String.valueOf(rs.getInt("tree_density")));
                airQuality.setText(String.valueOf(rs.getFloat("air_quality")));
                biodiversity.setText(String.valueOf(rs.getFloat("biodiversity")));
                avgHealth.setText(String.valueOf(rs.getFloat("avg_health")));
                latitude.setText(String.valueOf(rs.getDouble("latitude")));
                longitude.setText(String.valueOf(rs.getDouble("longitude")));
            } else {
                JOptionPane.showMessageDialog(this, "No health data found for the selected forest.", "Data Not Found", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading health data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
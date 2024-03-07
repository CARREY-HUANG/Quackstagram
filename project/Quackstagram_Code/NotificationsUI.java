import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * In this refactored version, I've separated concerns into smaller methods, parameterized where necessary, and improved readability by using meaningful variable names and comments. Additionally, I've provided placeholders for methods that require further implementation, such as getElapsedTime and methods related to button actions.
 */
public class NotificationsUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int NAV_ICON_SIZE = 20;

    public NotificationsUI() {
        setTitle("Notifications");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        JPanel headerPanel = createHeaderPanel();
        JPanel navigationPanel = createNavigationPanel();
        JPanel contentPanel = createContentPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51));
        JLabel lblRegister = new JLabel(" Notifications 🐥");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE);
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40));
        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] buttonTypes = { "home", "explore", "add", "notification", "profile" };
        for (String buttonType : buttonTypes) {
            navigationPanel.add(createIconButton("img/icons/" + buttonType + ".png", buttonType));
            navigationPanel.add(Box.createHorizontalGlue());
        }

        return navigationPanel;
    }

    private JButton createIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.addActionListener(e -> handleButtonAction(buttonType));
        return button;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        addNotificationsToPanel(contentPanel);
        return contentPanel;
    }

    private void addNotificationsToPanel(JPanel contentPanel) {
        String currentUsername = getCurrentUsername();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "notifications.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].trim().equals(currentUsername)) {
                    String notificationMessage = formatNotificationMessage(parts);
                    JPanel notificationPanel = createNotificationPanel(notificationMessage);
                    contentPanel.add(notificationPanel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentUsername() {
        String currentUsername = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                currentUsername = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentUsername;
    }

    private String formatNotificationMessage(String[] parts) {
        String userWhoLiked = parts[1].trim();
        String timestamp = parts[3].trim();
        String elapsedTime = getElapsedTime(timestamp);
        return userWhoLiked + " liked your picture - " + elapsedTime + " ago";
    }

    private JPanel createNotificationPanel(String notificationMessage) {
        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JLabel notificationLabel = new JLabel(notificationMessage);
        notificationPanel.add(notificationLabel, BorderLayout.CENTER);
        return notificationPanel;
    }

    private String getElapsedTime(String timestamp) {
        // Implementation of getElapsedTime method
        return ""; // Placeholder
    }

    private void handleButtonAction(String buttonType) {
        switch (buttonType) {
            case "home":
                openHomeUI();
                break;
            case "explore":
                exploreUI();
                break;
            case "add":
                ImageUploadUI();
                break;
            case "notification":
                notificationsUI();
                break;
            case "profile":
                openProfileUI();
                break;
            default:
                break;
        }
    }

    private void ImageUploadUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
    }

    private void openProfileUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        String loggedInUsername = "";

        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = new User(loggedInUsername);
        InstagramProfileUI profileUI = new InstagramProfileUI(user);
        profileUI.setVisible(true);
    }

    private void notificationsUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }

    private void openHomeUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
        homeUI.setVisible(true);
    }

    private void exploreUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ExploreUI explore = new ExploreUI();
        explore.setVisible(true);
    }
}

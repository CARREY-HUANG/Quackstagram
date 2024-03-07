import javax.swing.*;
import java.awt.*;

/*
 * This refactored code provides a more structured approach to building the Instagram profile UI, separating concerns into different methods and improving code readability. However, please note that the functionality for loading user data, displaying images, and handling button actions may need to be implemented based on the specific requirements of your application.
 * 
 */

public class InstagramProfileUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int NAV_ICON_SIZE = 20;

    private User currentUser;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JPanel navigationPanel;

    public InstagramProfileUI(User user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Instagram Profile");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        headerPanel = createHeaderPanel();
        navigationPanel = createNavigationPanel();
        initializeImageGrid();
        addComponentsToFrame();
    }

    private void addComponentsToFrame() {
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        // Top Header
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));
        // Add profile image, stats, and follow button to top header panel

        // Profile Name and Bio
        JPanel profileNameAndBioPanel = new JPanel(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));
        // Add profile name and bio to profileNameAndBioPanel

        headerPanel.add(topHeaderPanel);
        headerPanel.add(profileNameAndBioPanel);
        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        // Navigation Bar
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        // Add navigation buttons to navigationPanel
        return navigationPanel;
    }

    private void initializeImageGrid() {
        contentPanel.removeAll(); // Clear existing content
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

        // Load user's images and add them to contentPanel

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

        revalidate();
        repaint();
    }

    private JButton createIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);

        // Define actions based on button type
        if ("home".equals(buttonType)) {
            button.addActionListener(e -> openHomeUI());
        } else if ("profile".equals(buttonType)) {
            // Implement profile button action
        } else if ("notification".equals(buttonType)) {
            button.addActionListener(e -> notificationsUI());
        } else if ("explore".equals(buttonType)) {
            button.addActionListener(e -> exploreUI());
        } else if ("add".equals(buttonType)) {
            button.addActionListener(e -> openImageUploadUI());
        }
        return button;
    }

    private void openImageUploadUI() {
        // Open ImageUploadUI frame
        this.dispose();
        ImageUploadUI uploadUI = new ImageUploadUI();
        uploadUI.setVisible(true);
    }

    private void openHomeUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
        homeUI.setVisible(true);
    }

    private void notificationsUI() {
        // Open NotificationsUI frame
        this.dispose();
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }

    private void exploreUI() {
        // Open ExploreUI frame
        this.dispose();
        ExploreUI exploreUI = new ExploreUI();
        exploreUI.setVisible(true);
    }

    // Other methods and action handlers
}

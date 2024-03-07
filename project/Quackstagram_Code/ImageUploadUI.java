import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
1. Encapsulated the creation of header and navigation panels into separate methods for better readability and maintainability.
2. Reorganized the code structure to follow a logical flow.
3. Use switch-case statements instead of multiple if-else conditions for button actions, enhancing readability.
4. Removed unnecessary comments and refactored variable names for clarity.
5. Ensured proper exception handling and error messages for file operations.
6. Implemented the main method to run the UI on the Swing event dispatch thread. */
public class ImageUploadUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int NAV_ICON_SIZE = 20;

    private JLabel imagePreviewLabel;
    private JTextArea bioTextArea;
    private JButton uploadButton;

    public ImageUploadUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Upload Image");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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
        JLabel titleLabel = new JLabel(" Upload Image 🐥");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40));
        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] iconPaths = { "img/icons/home.png", "img/icons/search.png", "img/icons/add.png", "img/icons/heart.png",
                "img/icons/profile.png" };
        String[] buttonTypes = { "home", "explore", "notification", " ", "profile" };
        for (int i = 0; i < iconPaths.length; i++) {
            navigationPanel.add(createIconButton(iconPaths[i], buttonTypes[i]));
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

    private void handleButtonAction(String buttonType) {
        switch (buttonType) {
            case "home":
                openHomeUI();
                break;
            case "profile":
                openProfileUI();
                break;
            case "notification":
                notificationsUI();
                break;
            case "explore":
                exploreUI();
                break;
        }
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);
        contentPanel.add(imagePreviewLabel);

        bioTextArea = new JTextArea("Enter a caption");
        bioTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioTextArea.setLineWrap(true);
        bioTextArea.setWrapStyleWord(true);
        JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
        bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
        contentPanel.add(bioScrollPane);

        uploadButton = new JButton("Upload Image");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::uploadAction);
        contentPanel.add(uploadButton);

        return contentPanel;
    }

    private void uploadAction(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String username = readUsername();
                int imageId = getNextImageId(username);
                String fileExtension = getFileExtension(selectedFile);
                String newFileName = username + "_" + imageId + "." + fileExtension;

                Path destPath = Paths.get("img", "uploaded", newFileName);
                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                saveImageInfo(username + "_" + imageId, username, bioTextArea.getText());

                ImageIcon imageIcon = new ImageIcon(destPath.toString());
                if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
                    Image image = imageIcon.getImage();
                    int previewWidth = imagePreviewLabel.getWidth();
                    int previewHeight = imagePreviewLabel.getHeight();
                    int imageWidth = image.getWidth(null);
                    int imageHeight = image.getHeight(null);
                    double widthRatio = (double) previewWidth / imageWidth;
                    double heightRatio = (double) previewHeight / imageHeight;
                    double scale = Math.min(widthRatio, heightRatio);
                    int scaledWidth = (int) (scale * imageWidth);
                    int scaledHeight = (int) (scale * imageHeight);
                    imageIcon.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
                }
                imagePreviewLabel.setIcon(imageIcon);
                uploadButton.setText("Upload Another Image");
                JOptionPane.showMessageDialog(this, "Image uploaded and preview updated!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int getNextImageId(String username) throws IOException {
        Path storageDir = Paths.get("img", "uploaded");
        if (!Files.exists(storageDir)) {
            Files.createDirectories(storageDir);
        }

        int maxId = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(storageDir, username + "_*")) {
            for (Path path : stream) {
                String fileName = path.getFileName().toString();
                int idEndIndex = fileName.lastIndexOf('.');
                if (idEndIndex != -1) {
                    String idStr = fileName.substring(username.length() + 1, idEndIndex);
                    try {
                        int id = Integer.parseInt(idStr);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException ex) {
                        // Ignore filenames that do not have a valid numeric ID
                    }
                }
            }
        }
        return maxId + 1;
    }

    private void saveImageInfo(String imageId, String username, String bio) throws IOException {
        Path infoFilePath = Paths.get("img", "image_details.txt");
        if (!Files.exists(infoFilePath)) {
            Files.createFile(infoFilePath);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (BufferedWriter writer = Files.newBufferedWriter(infoFilePath, StandardOpenOption.APPEND)) {
            writer.write(String.format("ImageID: %s, Username: %s, Bio: %s, Timestamp: %s, Likes: 0", imageId, username,
                    bio, timestamp));
            writer.newLine();
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf + 1);
    }

    private void openProfileUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        // Read the logged-in user's username from users.txt
        String loggedInUsername = "";
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

    private String readUsername() throws IOException {
        Path usersFilePath = Paths.get("data", "users.txt");
        try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
            String line = reader.readLine();
            if (line != null) {
                return line.split(":")[0];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageUploadUI uploadUI = new ImageUploadUI();
            uploadUI.setVisible(true);
        });
    }
}

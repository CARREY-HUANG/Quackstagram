import java.io.*;
import java.util.*;

public class ImageLikesManager {

    private final String likesFilePath = "data/likes.txt";

    // Method to like an image
    public void likeImage(String username, String imageID) {
        try {
            Map<String, Set<String>> likesMap = readLikes();
            likesMap.computeIfAbsent(imageID, k -> new HashSet<>()).add(username);
            saveLikes(likesMap);
        } catch (IOException e) {
            System.err.println("Error while liking image: " + e.getMessage());
        }
    }

    // Method to read likes from file
    private Map<String, Set<String>> readLikes() throws IOException {
        Map<String, Set<String>> likesMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(likesFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String imageID = parts[0];
                Set<String> users = new HashSet<>(Arrays.asList(parts[1].split(",")));
                likesMap.put(imageID, users);
            }
        }
        return likesMap;
    }

    // Method to save likes to file
    private void saveLikes(Map<String, Set<String>> likesMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(likesFilePath, false))) {
            for (Map.Entry<String, Set<String>> entry : likesMap.entrySet()) {
                String line = entry.getKey() + ":" + String.join(",", entry.getValue());
                writer.write(line);
                writer.newLine();
            }
        }
    }

}

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

abstract class FileChange {
    private final String fileName;

    public FileChange(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public abstract String getChangeType();
}

class NewFile extends FileChange {
    public NewFile(String fileName) {
        super(fileName);
    }

    @Override
    public String getChangeType() {
        return "New File";
    }

    @Override
    public String toString() {
        return getFileName() + " is a new file.";
    }
}

class DeletedFile extends FileChange {
    public DeletedFile(String fileName) {
        super(fileName);
    }

    @Override
    public String getChangeType() {
        return "Deleted File";
    }

    @Override
    public String toString() {
        return getFileName() + " was deleted since the last snapshot.";
    }
}

class ModifiedFile extends FileChange {
    public ModifiedFile(String fileName) {
        super(fileName);
    }

    @Override
    public String getChangeType() {
        return "Modified File";
    }

    @Override
    public String toString() {
        return getFileName() + " was modified since the last snapshot.";
    }
}

class FileSnapshot implements Serializable {
    private final Map<String, Long> fileStates;

    public FileSnapshot() {
        this.fileStates = new HashMap<>();
    }

    public void addFile(String fileName, long lastModified) {
        fileStates.put(fileName, lastModified);
    }

    public Map<String, Long> getFileStates() {
        return fileStates;
    }
}

class SaveManager {
    private static final String SNAPSHOT_FILE = "snapshot.dat";

    public static void saveSnapshot(FileSnapshot snapshot) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SNAPSHOT_FILE))) {
            oos.writeObject(snapshot);
            System.out.println("Snapshot saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving snapshot: " + e.getMessage());
        }
    }

    public static FileSnapshot loadSnapshot() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SNAPSHOT_FILE))) {
            return (FileSnapshot) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous snapshot found. Starting fresh.");
            return new FileSnapshot();
        }
    }
}

public class DocumentChangeDetectionSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<FileChange> changes = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Enter the folder path to monitor: ");
        String folderPath = scanner.nextLine();

        File directory = new File(folderPath);
        if (!directory.isDirectory()) {
            System.out.println("Invalid folder path. Exiting.");
            return;
        }

        FileSnapshot previousSnapshot = SaveManager.loadSnapshot();
        FileSnapshot currentSnapshot = takeSnapshot(directory);

        detectChanges(previousSnapshot, currentSnapshot);

        if (changes.isEmpty()) {
            System.out.println("No changes detected since the last snapshot.");
        } else {
            changes.forEach(System.out::println);
        }

        SaveManager.saveSnapshot(currentSnapshot);
    }

    private static FileSnapshot takeSnapshot(File directory) {
        FileSnapshot snapshot = new FileSnapshot();

        try {
            Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    snapshot.addFile(file.toString(), attrs.lastModifiedTime().toMillis());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.out.println("Error reading directory: " + e.getMessage());
        }

        return snapshot;
    }

    private static void detectChanges(FileSnapshot previousSnapshot, FileSnapshot currentSnapshot) {
        Map<String, Long> previousFiles = previousSnapshot.getFileStates();
        Map<String, Long> currentFiles = currentSnapshot.getFileStates();

        // Detect new and modified files
        for (Map.Entry<String, Long> entry : currentFiles.entrySet()) {
            String fileName = entry.getKey();
            long lastModified = entry.getValue();

            if (!previousFiles.containsKey(fileName)) {
                changes.add(new NewFile(fileName));
            } else if (previousFiles.get(fileName) != lastModified) {
                changes.add(new ModifiedFile(fileName));
            }
        }

        // Detect deleted files
        for (String fileName : previousFiles.keySet()) {
            if (!currentFiles.containsKey(fileName)) {
                changes.add(new DeletedFile(fileName));
            }
        }
    }
}

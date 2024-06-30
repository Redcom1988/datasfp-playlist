import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlaylistManager {
    public static void main(String[] args) {
        Playlist playlist = new Playlist(null);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("Playlist Manager Menu");
            System.out.println("1. Load Playlist");
            System.out.println("2. Playlist Edit");
            System.out.println("3. Display Playlist");
            System.out.println("4. Save Playlist");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.println();

            switch (choice) {
                case 1:
                    playlist = loadPlaylist(scanner);
                    break;
                case 2:
                    editPlaylist(playlist, scanner);
                    break;
                case 3:
                    playlist.display();
                    break;
                case 4:
                    savePlaylist(playlist, scanner);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    private static void editPlaylist(Playlist playlist, Scanner scanner) {
        boolean editing = true;
        while (editing) {
            System.out.println();
            System.out.println("Pointer Navigation Menu");
            System.out.println("1. Move Pointer Left");
            System.out.println("2. Show Current Song");
            System.out.println("3. Move Pointer Right\n");

            System.out.println("Song Editing Menu");
            System.out.println("4. Append Song At Current Position");
            System.out.println("5. Remove Current Song");
            System.out.println("6. Sort Playlist\n");

            System.out.println("7. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.println();

            switch (choice) {
                case 1:
                    playlist.moveLeft();
                    playlist.displayCurrent();
                    break;
                case 2:
                    playlist.displayCurrent();
                case 3:
                    playlist.moveRight();
                    playlist.displayCurrent();
                    break;
                case 4:
                    System.out.println("Song Input Menu");
                    System.out.println("1. Manual Input");
                    System.out.println("2. Load Song from Songlist");
                    System.out.print("Choose an option: ");
                    int inputChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.println();

                    if (inputChoice == 1) {
                        System.out.print("Enter song name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter author name: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter duration: ");
                        int duration = scanner.nextInt();
                        playlist.appendSong(name, author, duration);
                    } else if (inputChoice == 2) {
                        selectSongFromList(playlist, scanner);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 5:
                    playlist.removeCurrentSong();
                    break;
                case 6:
                    System.out.println("Sort Playlist Menu");
                    System.out.println("1. Sort by Song Name");
                    System.out.println("2. Sort by Author Name");
                    System.out.print("Choose an option: ");
                    int sortChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.println();

                    if (sortChoice == 1) {
                        playlist.sortByName();
                    } else if (sortChoice == 2) {
                        playlist.sortByAuthor();
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 7:
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static List<String> loadList() {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("songlist.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading list file: " + e.getMessage());
        }
        return list;
    }

    private static void selectSongFromList(Playlist playlist, Scanner scanner) {
        List<String> list = loadList();
        if (list.isEmpty()) {
            System.out.println("The list is empty.");
            return;
        }

        System.out.println("Select a song from the list:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i));
        }

        System.out.print("Enter the id of the song to add: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice > 0 && choice <= list.size()) {
            String[] parts = list.get(choice - 1).split(" by | \\|\\| Duration: ");
            if (parts.length == 3) {
                playlist.appendSong(parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim()));
                System.out.println("Song added to playlist.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void savePlaylist(Playlist playlist, Scanner scanner) {
        System.out.print("Enter the name of the playlist to save: ");
        String playlistName = scanner.nextLine().trim();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(playlistName + ".ser"))) {
            out.writeObject(playlist);
            System.out.println("Playlist saved successfully as " + playlistName + ".");
        } catch (IOException e) {
            System.out.println("Error saving playlist: " + e.getMessage());
        }
    }

    private static Playlist loadPlaylist(Scanner scanner) {
        System.out.print("Enter the name of the playlist to load: ");
        String playlistName = scanner.nextLine().trim();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(playlistName + ".ser"))) {
            System.out.println("Playlist " + playlistName + " loaded successfully.");
            return (Playlist) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading playlist: " + e.getMessage());
            return new Playlist(null);
        }
    }
}

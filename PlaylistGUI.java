import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class PlaylistGUI {
    private Playlist playlist;
    private JFrame frame;
    private JTextArea playlistDisplay;

    public PlaylistGUI() {
        playlist = new Playlist();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Playlist Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Playlist");
        JMenuItem loadItem = new JMenuItem("Load Playlist");

        saveItem.addActionListener(this::savePlaylist);
        loadItem.addActionListener(this::loadPlaylist);

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);

        playlistDisplay = new JTextArea();
        playlistDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(playlistDisplay);

        JPanel controlPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JPanel panel1 = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel panel2 = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel panel3 = new JPanel(new GridLayout(2, 1, 5, 5));

        JButton appendButton = new JButton("Append Song");
        JButton removeButton = new JButton("Remove Current Song");
        JButton moveLeftButton = new JButton("Move Up");
        JButton moveRightButton = new JButton("Move Down");
        JButton sortNameButton = new JButton("Sort by Name");
        JButton sortAuthorButton = new JButton("Sort by Author");

        panel1.add(appendButton);
        panel1.add(removeButton);
        panel2.add(moveLeftButton);
        panel2.add(moveRightButton);
        panel3.add(sortNameButton);
        panel3.add(sortAuthorButton);

        controlPanel.add(panel1);
        controlPanel.add(panel2);
        controlPanel.add(panel3);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        appendButton.addActionListener(e -> appendSong());
        removeButton.addActionListener(e -> removeCurrentSong());
        sortNameButton.addActionListener(e -> sortByName());
        sortAuthorButton.addActionListener(e -> sortByAuthor());
        moveLeftButton.addActionListener(e -> moveLeft());
        moveRightButton.addActionListener(e -> moveRight());

        frame.setVisible(true);
    }

    private void appendSong() {
        Object[] options = { "Manual Input", "From Song List" };
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Choose input method:",
                "Append Song",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            manualSongInput();
        } else if (choice == 1) {
            appendSongFromList();
        }
    }

    private void manualSongInput() {
        JTextField songNameField = new JTextField();
        JTextField authorNameField = new JTextField();
        Object[] fields = {
                "Song Name:", songNameField,
                "Author Name:", authorNameField
        };
        int option = JOptionPane.showConfirmDialog(frame, fields, "Manual Song Input", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = songNameField.getText().trim();
            String author = authorNameField.getText().trim();
            if (!name.isEmpty() && !author.isEmpty()) {
                playlist.appendSong(name, author);
                displayPlaylist();
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both song and author names.");
            }
        }
    }

    private void appendSongFromList() {
        List<String> songList = loadList();
        if (songList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "The song list is empty.");
            return;
        }

        Object[] options = songList.toArray();
        String selectedSong = (String) JOptionPane.showInputDialog(
                frame,
                "Select a song:",
                "Song List Input",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedSong != null) {
            String[] parts = selectedSong.split("by");
            if (parts.length == 2) {
                playlist.appendSong(parts[0].trim(), parts[1].trim());
                displayPlaylist();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid song format.");
            }
        }
    }

    private List<String> loadList() {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("songlist.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading list file: " + e.getMessage());
        }
        return list;
    }

    private void removeCurrentSong() {
        playlist.removeCurrentSong();
        displayPlaylist();
    }

    private void sortByName() {
        playlist.sortByName();
        displayPlaylist();
    }

    private void sortByAuthor() {
        playlist.sortByAuthor();
        displayPlaylist();
    }

    private void moveLeft() {
        playlist.moveLeft();
        displayPlaylist();
    }

    private void moveRight() {
        playlist.moveRight();
        displayPlaylist();
    }

    private void displayPlaylist() {
        playlistDisplay.setText("");
        StringBuilder displayText = new StringBuilder();
        Song temp = playlist.getHead();
        Song current = playlist.getCurrent();
        while (temp != null) {
            if (temp == current) {
                displayText.append("> ");
            } else {
                displayText.append("  ");
            }
            displayText.append("Song: ").append(temp.name).append(", Author: ").append(temp.author).append("\n");
            temp = temp.next;
        }
        playlistDisplay.setText(displayText.toString());
    }

    private void savePlaylist(ActionEvent e) {
        String playlistName = JOptionPane.showInputDialog(frame, "Enter the name of the playlist to save:");
        if (playlistName != null && !playlistName.trim().isEmpty()) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(playlistName.trim() + ".ser"))) {
                out.writeObject(playlist);
                JOptionPane.showMessageDialog(frame, "Playlist saved successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving playlist: " + ex.getMessage());
            }
        }
    }

    private void loadPlaylist(ActionEvent e) {
        String playlistName = JOptionPane.showInputDialog(frame, "Enter the name of the playlist to load:");
        if (playlistName != null && !playlistName.trim().isEmpty()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(playlistName.trim() + ".ser"))) {
                playlist = (Playlist) in.readObject();
                JOptionPane.showMessageDialog(frame, "Playlist loaded successfully.");
                displayPlaylist();
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "Error loading playlist: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PlaylistGUI::new);
    }
}

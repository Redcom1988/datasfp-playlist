import java.io.Serializable;
import java.time.*;

import javax.swing.SwingUtilities;

class Song implements Serializable {
    String name;
    String author;
    int duration;
    int initialDuration;
    Song prev;
    Song next;

    public Song(String name, String author, int duration) {
        this.name = name;
        this.author = author;
        this.duration = duration;
        this.initialDuration = duration;
    }
}

public class Playlist implements Serializable {
    private Song head;
    private Song current;
    private transient PlaylistGUI gui;
    private boolean isPlaying;

    public Playlist(PlaylistGUI gui) {
        this.gui = gui;
        this.isPlaying = false;
    }

    public void appendSong(String name, String author, int duration) {
        Song newSong = new Song(name, author, duration);
        if (head == null) {
            head = newSong;
            current = newSong;
        } else {
            newSong.prev = current;
            newSong.next = current.next;
            if (current.next != null) {
                current.next.prev = newSong;
            }
            current.next = newSong;
            current = newSong;
        }
    }

    public void removeCurrentSong() {
        if (current == null)
            return;

        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next;
        }

        if (current.next != null) {
            current.next.prev = current.prev;
        }

        current = current.next != null ? current.next : current.prev;
    }

    public void sortByName() {
        if (head == null)
            return;

        boolean sorted = false;

        while (!sorted) {
            sorted = true;
            Song current = head;

            while (current.next != null) {
                if (current.name.compareToIgnoreCase(current.next.name) > 0) {
                    swap(current, current.next);
                    sorted = false;
                }
                current = current.next;
            }
        }
    }

    public void sortByAuthor() {
        if (head == null)
            return;

        boolean sorted = false;

        while (!sorted) {
            sorted = true;
            Song current = head;

            while (current.next != null) {
                if (current.author.compareToIgnoreCase(current.next.author) > 0) {
                    swap(current, current.next);
                    sorted = false;
                }
                current = current.next;
            }
        }
    }

    private void swap(Song a, Song b) {
        String tempName = a.name;
        String tempAuthor = a.author;
        a.name = b.name;
        a.author = b.author;
        b.name = tempName;
        b.author = tempAuthor;
    }

    public void playPlaylistFromCurrent() {
        if (isPlaying)
            return;

        isPlaying = true;
        new Thread(() -> {
            while (current != null && isPlaying) {
                Instant start = Instant.now();
                while (current.duration > 0 && isPlaying) {
                    Instant now = Instant.now();
                    if (Duration.between(start, now).getSeconds() >= 1) {
                        current.duration--;
                        SwingUtilities.invokeLater(() -> gui.displayPlaylist());
                        start = now; // Reset start time
                    }
                }
                if (current.duration == 0) {
                    current.duration = current.initialDuration;
                }
                if (current.next != null && isPlaying)
                    current = current.next;
            }
            isPlaying = false;
        }).start();
    }

    public void stopPlaylist() {
        isPlaying = false;
    }

    public void returnInitialDuration() {
        Song temp = head;
        while (temp != null) {
            temp.duration = temp.initialDuration;
            temp = temp.next;
        }
    }

    public void display() {
        Song temp = head;
        if (temp == null) {
            System.out.println("Playlist is empty.");
            return;
        } else {
            System.out.println("Playlist:");
        }
        while (temp != null) {
            System.out.println("Song: " + temp.name + ", Author: " + temp.author);
            temp = temp.next;
        }
    }

    public void displayCurrent() {
        if (current != null) {
            System.out.println("Current song: " + current.name + ", Author: " + current.author);
        } else {
            System.out.println("No current song.");
        }
    }

    public void moveLeft() {
        if (current != null && current.prev != null) {
            current = current.prev;
        }
    }

    public void moveRight() {
        if (current != null && current.next != null) {
            current = current.next;
        }
    }

    public Song getCurrent() {
        return current;
    }

    public Song getHead() {
        return head;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}

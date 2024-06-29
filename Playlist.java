import java.io.Serializable;

class Song implements Serializable {
    String name;
    String author;
    Song prev;
    Song next;

    public Song(String name, String author) {
        this.name = name;
        this.author = author;
    }
}

public class Playlist implements Serializable {
    private Song head;
    private Song current;

    public void appendSong(String name, String author) {
        Song newSong = new Song(name, author);
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
            System.out.println("Current song: " + current.name + ", Author: " + current.author);
        }
    }

    public void moveRight() {
        if (current != null && current.next != null) {
            current = current.next;
            System.out.println("Current song: " + current.name + ", Author: " + current.author);
        }
    }

    public Song getCurrent() {
        return current;
    }

    public Song getHead() {
        return head;
    }
}

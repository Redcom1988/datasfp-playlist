import java.util.LinkedList;
import java.util.Scanner;

public class Playlistmusic {
    Song head;
    Song current;
    Scanner key = new Scanner (System.in);

    public void tambahSong(String judul, String artis, int durasi) {
        Song newSong = new Song(judul, artis, durasi);
        if(current == null){
            head = newSong;
            current = newSong;
        } else{
            current.next = newSong;
            newSong.prev = current;
            current = newSong;
        }
    }

    public void hapusSong(){
        if(current == null)
            return;
        
        if(current.prev != null){
            current.prev.next = current.next;
        } else {
            head = current.next;
        }

        if(current.next != null){
            current.next.prev = current.prev;
        }
        current = current.next != null ? current.next : current.prev;
    }

    public void pindahkedepanSong() {
        if(current == null){
            return;
        }
        
        Song next = current.next;
        Song prev = current.prev;
        Song nextNext = next.next;

        if(prev != null){
            prev.next = next;
        } else{
            head = next;
        }
        next.prev = prev;

        next.next = current;
        current.prev = next;

        current.next = nextNext;
        if(nextNext != null){
            nextNext.prev = current;
        }
        current = next;
    }

    public void pindahkebelakangSong(){
        if(current == null){
            return;
        }

        Song prev = current.prev;
        Song next = current.next;
        Song prevPrev = prev.prev;

        if(next != null){
            next.prev = prev;
        }

        prev.next = next;

        if(prevPrev != null){
            prevPrev.next = current;
        } else{
            head = current;
        }
        current.prev = prevPrev;

        current.next = prev;
        prev.prev = current;

        current = prev;
    }

    public void tampilkanSong(){
        if(head == null){
            System.out.println("Playlist Kosong");
        }

        Song currentSong = head;
        int i = 1;
        System.out.println("Tampilan Playlist:");
        while(currentSong != null){
            System.out.println("Song " + 1 + ":" + currentSong.judul + "by" + currentSong.artis + ", Durasi:" + currentSong.durasi + "minutes");
            i++;
            currentSong = currentSong.next;
        }
    }

    public void sortJudul(){
        if(head == null)
        return;

        boolean sorted;
        do{
            sorted = true;
            Song temp = head;
            while(temp.next != null){
                if(temp.judul.compareToIgnoreCase(temp.next.judul) > 0){
                    swap(temp, temp.next);
                    sorted = false;
                } else{
                    temp = temp.next;
                }
            }
        } while(!sorted);
    }

    public void sortArtis(){
        if(head == null)
        return;

        boolean sorted;
        do{
            sorted = true;
            Song temp = head;
            while(temp.next != null){
                if(temp.artis.compareToIgnoreCase(temp.next.artis) > 0){
                    swap(temp, temp.next);
                    sorted = false;
                } else{
                    temp = temp.next;
                }
            }
        } while(!sorted);

    }

    public void swap(Song a, Song b){
        if(a == b) return;

        Song temp = a.next;
        a.next = b.next;
        b.next = temp;

        if(a.next != null){
            a.next.prev = a;
        }
        if(b.next != null){
            b.next.prev = b;
        }

        temp = a.prev;
        a.prev = b.prev;
        b.prev = temp;

        if(a.prev != null){
            a.prev.next = a;
        } else{
            head = a;
        }
        if(b.prev != null){
            b.prev.next = b;
        } else{
            head = b;
        }
    }

    public static void main(String[] args) {
        Scanner key = new Scanner (System.in);

        LinkedList <Song> list = new LinkedList<>();
        Playlistmusic playlist = new Playlistmusic();

        while(true){
        System.out.println("*****MENU*****");
        System.out.println("1. Tambah Musik");
        System.out.println("2. Hapus Musik");
        System.out.println("3. Pindahkan Musik Kedepan");
        System.out.println("4. Pindahkan Musik Kebelakang");
        System.out.println("5. Tampilkan Playlist Musik");
        System.out.println("6. Keluar\n");
        System.out.print("Masukkan Nomer: ");
        int choice = key.nextInt();
        key.nextLine();

        switch (choice) {
            case 1:

                System.out.print("Masukkan Judul: ");
                String judul = key.nextLine();
                System.out.print("Masukkan Artis: ");
                String artis = key.nextLine();
                System.out.print("Masukkan Waktu Durasi: ");
                int durasi = key.nextInt();

                list.add(new Song (judul, artis, durasi));
                    System.out.println("Musik sukses ditambahkan");

                break;
            case 2:

                System.out.print("Masukkan Judul: ");
                judul = key.nextLine();

                playlist.hapusSong();

                System.out.println("Musik dihapus");
                break;
            case 3:
                for(Song tampilan : list){
                    System.out.println("Judul: " + tampilan.judul);
                    System.out.println("Artis: " + tampilan.artis);
                    System.out.println("Durasi: + " + tampilan.durasi);
                }
                break;
            case 4:
                playlist.pindahkebelakangSong();
                break;
            case 5:
                break;
            case 6:
                System.out.println("Keluar Playlist");
                System.exit(0);
                break;
        }
    }

}
    
}

class Song{
    String judul;
    String artis;
    int durasi;
    Song prev;
    Song next;

    Song(String judul, String artis, int durasi){
        this.judul = judul;
        this.artis = artis;
        this.durasi = durasi;
    }

}


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Song {
    private String title;
    private String artist;
    private String filePath;

    public Song(String title, String artist, String filePath) {
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return title + " by " + artist;
    }
}

class Node {
    Song data;
    Node next;

    Node previous;

    public Node(Song data) {
        this.data = data;
        this.next = null;
        this.previous = null;
    }
}

public class MP3Player {
    private Node head;
    private AdvancedPlayer player;

    public MP3Player() {
        this.head = null;
        this.player = null;
    }

    public void addSong(Song song) {
        Node newNode = new Node(song);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    public void startPlaylist() {
        if (head == null) {
            System.out.println("Playlist is empty. Add songs first.");
            return;
        }

        try {
            if (player != null) {
                player.close();
            }

            FileInputStream fileInputStream = new FileInputStream(head.data.getFilePath());
            player = new AdvancedPlayer(fileInputStream);

            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    playNext();
                }
            });

            new Thread(() -> {
                try {
                    player.play(0, Integer.MAX_VALUE);
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("Now playing: " + head.data);
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void playNext() {
        if (head != null && head.next != null) {
            head = head.next;
            startPlaylist();  // Start playing the next song
        } else {
            System.out.println("End of playlist");
        }
    }
    public void pause(){
        player.stop();


    }
    public void previous(){
        if(head != null && head.next!=null){
            head = head.previous;
        }
    }

    public static void main(String[] args) {
        MP3Player player = new MP3Player();

        Song song1 = new Song("Song 1", "Artist 1", "C:\\Users\\shrey\\Desktop\\DMX_-_X_Gon_Give_It_To_Ya.mp3");
        Song song2 = new Song("Song 2", "Artist 2", "C:\\Users\\shrey\\Desktop\\The20WeekndDaft20Punk20-20Starboy20.mp3");
        /*Song song3 = new Song("Song 3", "Artist 3", "path/to/song3.mp3");*/

        player.addSong(song1);
        player.addSong(song2);
       /*player.addSong(song3);*/

        // GUI
        JFrame frame = new JFrame("MP3 Player");
        JButton previousButton = new JButton("Previous");
        JButton playButton = new JButton("Play");
        JButton nextButton = new JButton("Next");
        JButton pauseButton = new JButton("Pause");
        JButton stopButton = new JButton("stop");


        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.startPlaylist();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.playNext();
            }
        });
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.pause();
            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.previous();
            }
        });

        JPanel panel = new JPanel();
        panel.add(playButton);
        panel.add(nextButton);
panel.add(pauseButton);
panel.add(previousButton);

        frame.add(panel);
        frame.setSize(500, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}




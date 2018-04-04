import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main
{
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser = null;
    private static String currentHeader = null;
    private static final String DEFAULT_HEADER = "Rick's Irish Pub";
    private static Scanner sc = new Scanner(System.in);
    private static List<Song> songs = new ArrayList<>();


    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        setHeader();
        users.put("admin", new User("admin", "admin", true));
        users.put("user1", new User("user1", "password", false));
        users.put("user2", new User("user2", "password", false));
        login();
        loadSongs();
        printMenu();
        while (true)
        {

            System.out.println("Enter command(\"help\" for list of all commands)");
            String command = sc.nextLine();
            while (command.isEmpty())
            {
                command = sc.nextLine();
            }
            switch (command)
            {
                case "help":
                {
                    printMenu();
                    break;
                }
                case "exit":
                {
                    System.out.println("Bye");
                    System.exit(0);
                }
                case "list":
                {
                    if (songs.isEmpty())
                    {
                        System.out.println("No songs to print");
                        break;
                    }
                    System.out.println("Printing all songs:");
                    System.out.printf("%5s %20s %20s", "Num", "Title", "Artist");
                    System.out.println();
                    for (int i = 0; i < songs.size(); i++)
                    {
                        Song song = songs.get(i);
                        System.out.printf("%5s %20s %20s", i + "", song.getTitle(), song.getArtist());
                        System.out.println();
                    }
                    break;
                }
                case "listen":
                {
                    if (songs.isEmpty())
                    {
                        System.out.println("No songs to listen");
                        break;
                    }
                    listenSong();
                    break;
                }
                case "add":
                {
                    if (!currentUser.isAdmin)
                    {
                        System.out.println("You need to be admin to add songs");
                        break;
                    }
                    addSong();
                    break;
                }
                case "sort":
                {
                    sortSongs();
                    break;
                }
                case "remove":
                {
                    if (!currentUser.isAdmin)
                    {
                        System.out.println("You need to be admin to remove songs");
                        break;
                    }
                    removeSong();
                    break;
                }

                default:
                {

                    System.out.println("Wrong command " + command + ". Use list to see the list of all commands");
                }


            }
        }

    }


    private static void sortSongs()
    {
        System.out.println("Enter field to sort");
        System.out.println("1 - title");
        System.out.println("2 - artist");
        System.out.println("3 - length");
        int number = sc.nextInt();
        switch (number)
        {
            case 1:
            {
                Collections.sort(songs, new Comparator<Song>()
                {
                    public int compare(Song o1, Song o2)
                    {
                        return o1.title.compareTo(o2.title);
                    }
                });
            }
            case 2:
            {
                Collections.sort(songs, new Comparator<Song>()
                {
                    public int compare(Song o1, Song o2)
                    {
                        return o1.artist.compareTo(o2.artist);
                    }
                });
            }
            case 3:
            {
                Collections.sort(songs, new Comparator<Song>()
                {
                    public int compare(Song o1, Song o2)
                    {
                        if (o1.length == o2.length)
                        {
                            return 0;
                        }
                        return o1.length < o2.length ? -1 : 1;
                    }
                });
            }
        }

    }


    private static void printMenu()
    {
        System.out.println(currentHeader);
        System.out.println("Available commands:");
        System.out.println("list - lists all songs");
        System.out.println("sort - sort all songs");
        if (currentUser.isAdmin)
        {
            System.out.println("add - add song");
            System.out.println("remove - remove song");
        }
        System.out.println("listen  - lists all songs");
        System.out.println("exit  - closes program and exit");
    }


    private static void listenSong()
    {
        System.out.println("Enter number of song to listen");
        int number = sc.nextInt();
        if (number < 0 || number > songs.size() - 1)
        {
            System.out.println("Song number should be between " + 0 + " and " + (songs.size() - 1));
            return;
        }
        Song song = songs.get(number);
        System.out.println("Listening to song " + song.title);
        System.out.println("----------------------------");
        System.out.println(song.lyrics);
        System.out.println("----------------------------");
        System.out.println("Song ended");
    }


    private static void removeSong()
    {
        System.out.println("Enter number of song to remove");
        int number = sc.nextInt();
        if (number < 0 || number > songs.size() - 1)
        {
            System.out.println("Song number should be between " + 0 + " and " + (songs.size() - 1));
            return;
        }
        songs.remove(number);
        System.out.println("Song removed");
    }


    private static void addSong() throws IOException
    {
        Song song = new Song();
        System.out.println("Enter song title");
        song.title = sc.nextLine();
        System.out.println("Enter song lyrics");
        song.lyrics = sc.nextLine();
        System.out.println("Enter song artist");
        song.artist = sc.nextLine();
        System.out.println("Enter song length in seconds");
        song.length = sc.nextInt();
        songs.add(song);

        FileOutputStream fos = new FileOutputStream("songs.db");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(songs);
        oos.close();
        System.out.println("Song " + song.title + " has been added by number " + (songs.size() - 1));
    }


    static void login()
    {
        System.out.println("Enter username and password");

        while (currentUser == null)
        {
            String username = sc.nextLine();
            String password = sc.nextLine();
            //            System.out.println(username + " " + password);
            User user = users.get(username);
            if (user == null || !user.password.equals(password))
            {
                System.out.println("Wrong username/password. Try again");
            }
            else
            {
                currentUser = user;
            }
        }
    }


    private static void setHeader() throws IOException
    {
        Path pathToHeader = Paths.get("header.txt");
        if (Files.exists(pathToHeader))
        {
            currentHeader = new String(Files.readAllBytes(pathToHeader));
            return;
        }
        //        currentHeader = DEFAULT_HEADER + ", " + currentUser.username;
        currentHeader = DEFAULT_HEADER;
        Files.createFile(pathToHeader);
        try (PrintWriter out = new PrintWriter("header.txt"))
        {
            out.println(currentHeader);
        }
    }


    private static void loadSongs() throws IOException, ClassNotFoundException
    {
        // read object from file
        Path pathToSongs = Paths.get("songs.db");
        if (!Files.exists(pathToSongs))
        {
            return;
        }
        FileInputStream fis = new FileInputStream("songs.db");
        ObjectInputStream ois = new ObjectInputStream(fis);
        songs = (ArrayList<Song>) ois.readObject();
        ois.close();
    }
}

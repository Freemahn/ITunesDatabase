import java.io.Serializable;

/**
 * @author Pavel Gordon
 */
public class Song implements Serializable
{
    private static final long serialVersionUID = 1L;
    String title;
    String lyrics;
    String artist;
    int length;


    @Override
    public String toString()
    {
        return "Song\n" +
            "title='" + title + '\'' +
            ", lyrics='" + lyrics + '\'' +
            ", artist='" + artist + '\'' +
            ", length=" + length +
            '}';
    }





    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getLyrics()
    {
        return lyrics;
    }


    public void setLyrics(String lyrics)
    {
        this.lyrics = lyrics;
    }


    public String getArtist()
    {
        return artist;
    }


    public void setArtist(String artist)
    {
        this.artist = artist;
    }


    public int getLength()
    {
        return length;
    }


    public void setLength(int length)
    {
        this.length = length;
    }
}

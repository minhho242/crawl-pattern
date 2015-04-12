package hopamchuan;

import model.Item;

import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * Description
 *
 * @author minhho242 on 4/11/15.
 */
public class SongDTO {

    private int id;
    private String name;
    private String composer;
    private String lyric;
    private String rhythm;
    private long createdTime;
    private String singer;
    private String musicSource;
    private String sourceSongId;

    public String getMusicSource() {
        return musicSource;
    }

    public void setMusicSource(String musicSource) {
        this.musicSource = musicSource;
    }

    public String getSourceSongId() {
        return sourceSongId;
    }

    public void setSourceSongId(String sourceSongId) {
        this.sourceSongId = sourceSongId;
    }

    public SongDTO(Song song) {
        this.name = song.name.getValue();
        this.composer = song.composer.getValue();
        this.lyric = song.lyric.getValue();
        this.rhythm = song.rhythm.getValue();
        this.singer = song.singer.getValue();
        this.musicSource = song.musicSource.getValue();
        this.sourceSongId = song.sourceSongId.getValue();
        createdTime = System.currentTimeMillis() / 1000;
    }

    public SongDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getRhythm() {
        return rhythm;
    }

    public void setRhythm(String rhythm) {
        this.rhythm = rhythm;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String newLineChar = System.getProperty("line.separator");
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder resultBuidler = new StringBuilder();
        for (Field field : fields ) {
            try {
                Object temp = field.get(this);
                resultBuidler.append(field.getName()).append(": ").append(temp).append(newLineChar);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return resultBuidler.toString();
    }
}

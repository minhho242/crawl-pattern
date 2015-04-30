package hopamchuan;

import model.Item;
import util.Utils;

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
    private String type;
    private String shortLyric;
    private char firstLetter;

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
        this.name = _trim(song.name.getValue());
        this.composer = _trim(song.composer.getValue());
        this.lyric = _trim(song.lyric.getValue());
        this.rhythm = _trim(song.rhythm.getValue());
        this.singer = _trim(song.singer.getValue());
        this.musicSource = _trim(song.musicSource.getValue());
        this.sourceSongId = _trim(song.sourceSongId.getValue());
        this.type = _trim(song.type.getValue());
        this.shortLyric = _trim(song.shorLyric.getValue());
        this.firstLetter = Utils.getFirstLetter(this.name);
        createdTime = System.currentTimeMillis() / 1000;
    }

    private String _trim(String text) {
        return text != null ? text.trim() : text;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShortLyric() {
        return shortLyric;
    }

    public void setShortLyric(String shortLyric) {
        this.shortLyric = shortLyric;
    }

    public char getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(char firstLetter) {
        this.firstLetter = firstLetter;
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

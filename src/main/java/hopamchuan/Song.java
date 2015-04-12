package hopamchuan;

import model.Item;
import model.Webbot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import util.Utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description
 *
 * @author minhho242 on 4/11/15.
 */
public class Song {

    public Item name = new Item("name") {
        @Override
        public String extractValue(Document document) {
            return Utils.toTitleCase(extractByCSSQuery(document, "h1"));
        }
    };

    public Item rhythm = new Item("rhythm") {
        @Override
        public String extractValue(Document document) {
            return Utils.toTitleCase(extractByCSSQuery(document, "#selected_rhythm"));
        }
    };

    public Item composer = new Item("composer") {
        @Override
        public String extractValue(Document document) {
            return Utils.toTitleCase(extractByCSSQuery(document, ".sprite-author-icon + span"));
        }
    };

    public Item singer = new Item("singer") {
        @Override
        public String extractValue(Document document) {
            return Utils.toTitleCase(extractByCSSQuery(document, ".sprite-singer-icon + span"));
        }
    };

    public Item sourceSongId = new Item("sourceSongId") {
        @Override
        public String extractValue(Document document) {
            Elements elements = document.select("#song_id_element");
            if ( elements.size() > 0 ) {
                return elements.get(0).attr("value");
            }
            return null;
        }
    };

    public Item lyric = new Item("lyric") {
        @Override
        public String extractValue(Document document) {
            Elements elements = document.select("pre");
            if ( elements.size() > 0 ) {
                return elements.get(0).html();
            }
            return null;
        }
    };

    public Item musicSource = new Item("musicSource") {
        @Override
        public String extractValue(Document document) {
            Elements elements = document.select("#listen_button");
            if ( elements.size() > 0 ) {
                return elements.get(0).attr("source_url");
            }
            return null;
        }
    };

    public static String extractByCSSQuery(Document document, String cssQuery) {
        Elements elements = document.select(cssQuery);
        if ( elements.size() > 0 ) {
            return elements.get(0).ownText();
        }
        return null;

    }

    public static Song extract(String URL) throws IOException {
        Song song = new Song();
        Webbot webbot = new Webbot(URL);
        Field[] fields = song.getClass().getDeclaredFields();

        String htmlData = webbot.crawl(Webbot.METHOD_GET);
        Document document = Jsoup.parse(htmlData);

        for (Field field : fields) {
            try {
                Object temp = field.get(song);
                if ( temp instanceof Item) {
                    Item item = (Item)temp;
                    String value = item.extractValue(document);
                    item.setValue(value);
                }
            } catch (IllegalAccessException e) {

            }
        }

        return song;
    }

    @Override
    public String toString() {
        String newLineChar = System.getProperty("line.separator");
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder resultBuidler = new StringBuilder();
        for (Field field : fields ) {
            try {
                Object temp = field.get(this);
                if ( temp instanceof Item ) {
                    Item item = (Item) temp;
                    resultBuidler.append(item.getName()).append(": ").append(item.getValue()).append(newLineChar);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return resultBuidler.toString();
    }
}

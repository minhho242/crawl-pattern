package hopamchuan;

import model.Item;
import model.Webbot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(Song.class);

    public Item name = new Item("name") {
        @Override
        public String extractValue(Document document) {
            return Utils.toTitleCase(extractByCSSQuery(document, "h3"));
        }
    };

    public Item rhythm = new Item("rhythm") {
        @Override
        public String extractValue(Document document) {
            return extractByContainingText(document, "Điệu");
        }
    };

    public Item composer = new Item("composer") {
        @Override
        public String extractValue(Document document) {
            return extractByContainingText(document, "Sáng tác");
        }
    };

    public Item type = new Item("type") {
        @Override
        public String extractValue(Document document) {
            return extractByCSSQuery(document, ".ibar .fa-book + a");
        }
    };

    public Item singer = new Item("singer") {
        @Override
        public String extractValue(Document document) {
            return Utils.toTitleCase(extractAllByCSSQuery(document, "#fullsong p:first-child a"));
        }
    };

    public Item sourceSongId = new Item("sourceSongId") {
        @Override
        public String extractValue(Document document) {
            Elements elements = document.select("input[name=\"url\"]");
            if ( elements.size() > 0 ) {
                Pattern pattern = Pattern.compile(".*/(.*)\\.html");
                Matcher matcher = pattern.matcher(elements.get(0).attr("value"));
                if ( matcher.find() ) {
                    return matcher.group(1);
                }
            }
            return null;
        }
    };

    public Item lyric = new Item("lyric") {
        @Override
        public String extractValue(Document document) {
            Elements elements = document.select("#lyric");
            if ( elements.size() > 0 ) {
                String html = elements.get(0).html();
                Pattern pattern = Pattern.compile("(\\[[\\w]*\\])");
                Matcher matcher = pattern.matcher(html);

                StringBuffer sb = new StringBuffer();
                while ( matcher.find() ) {
                    matcher.appendReplacement(sb, "<span class='chord'>" + matcher.group(1) + "</span>");
                }
                matcher.appendTail(sb);
                return sb.toString();
            }
            return null;
        }
    };

    Item shorLyric = new Item("shortLyric") {
        @Override
        public String extractValue(Document document) {
            Elements elements = document.select("#lyric");
            if ( elements.size() > 0 ) {
                return Utils.shortString(elements.get(0).text(), 0, 100);
            }
            return null;
        }
    };

    public Item musicSource = new Item("musicSource") {
        @Override
        public String extractValue(Document document) {
            Elements elements = document.select("source");
            if ( elements.size() > 0 ) {
                return elements.get(0).attr("src");
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

    public static String extractAllByCSSQuery(Document document, String cssQuery) {
        Elements elements = document.select(cssQuery);
        StringBuffer sb = new StringBuffer();
        for (Element element : elements) {
            sb.append(element.text()).append(", ");
        }
        if ( sb.length() > 4 ) {
            sb.delete(sb.length() - 4, sb.length());
        }
        return sb.toString();
    }

    private static String extractByContainingText(Document document, String text) {
        Elements elements = document.getElementsContainingOwnText(text);
        if ( elements.size() > 0 ) {
            String ownText = elements.get(0).text();
            Pattern pattern = Pattern.compile(text + ": ([^|]*)");
            Matcher matcher = pattern.matcher(ownText);
            if ( matcher.find() ) {
                return matcher.group(1);
            }
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

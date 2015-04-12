package hopamchuan;

import model.Webbot;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HibernateUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description
 *
 * @author minhho242 on 3/24/15.
 */
public class HopAmChuanCrawlEngine implements Iterator<Song> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HopAmChuanCrawlEngine.class);
    private static final List<String> RHYTHMS = new ArrayList<>();
    private static final int DEFAULT_LIMIT = 10;

    protected String URL = "http://hopamchuan.com/ajax/get_rhythm_songs";
    protected String BASE_URL = "http://hopamchuan.com/ajax/get_rhythm_songs";
    protected String pageParam = "offset";

    private int currentOffset;
    private int currentRhytmIndex;
    private Elements itemLinks;
    private Iterator<Element> _iterator;

    static {
        String[] tempRhymthms = new String[] {
                "ballad", "blue", "slow", "bollero", "slowrock", "disco", "boston",
                "chachacha", "valse", "rhumba", "pop", "rock", "fox", "bossanova",
                "tango"
        };
        Collections.addAll(RHYTHMS, tempRhymthms);
    }

    public HopAmChuanCrawlEngine() {
        currentOffset = 0;
        currentRhytmIndex = 0;
        _nextPage();

    }

    public synchronized boolean hasNext() {
        if ( !_iterator.hasNext() ) {
            _nextPage();
        }
        return _iterator.hasNext();
    }

    /**
     * Get next song
     * @return {@link Song} object if didn't exist yet. Otherwise return null
     */
    public Song next() {

        LOGGER.info("Current offset {}", currentOffset);
        LOGGER.info("Current Rhythm {}", RHYTHMS.get(currentRhytmIndex));

        Element element = null;
        synchronized (_iterator) {
            element = _iterator.next();
        }
        String itemLink = element.attr("href");
        try {
            return Song.extract(itemLink);
        } catch (IOException e) {
            LOGGER.error("Can not extract song", e.getMessage());
            return null;
        }
    }


    private String extractSongIdFromURL(String url) {
        Pattern pattern = Pattern.compile("/song/([\\d]*)/");
        Matcher matcher = pattern.matcher(url);

        if ( matcher.find() ) {
            return matcher.group(1);
        }
        return null;
    }

    @Override
    public void remove() {
        _iterator.remove();
    }

    private boolean _nextPage() {
        try {
            Webbot webbot = new Webbot(BASE_URL);
            Map<String, String> params = new HashMap<>();
            params.put("rhythm", RHYTHMS.get(currentRhytmIndex));
            params.put("offset", String.valueOf(currentOffset));
            String htmlData = webbot.crawl(Webbot.METHOD_POST, params);

            Document document = Jsoup.parse(htmlData);
            itemLinks = document.select("a");
            if ( (itemLinks.size() == 0) && (currentRhytmIndex < RHYTHMS.size() - 1) ) {
                currentRhytmIndex++;
                currentOffset = 0;
                return _nextPage();
            }

            _iterator = itemLinks.iterator();

            currentOffset += DEFAULT_LIMIT;
            return itemLinks.size() > 0;
        } catch (IOException e) {
            return false;
        }
    }
}

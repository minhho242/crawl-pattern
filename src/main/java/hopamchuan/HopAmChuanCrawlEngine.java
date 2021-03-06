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
import util.Utils;

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
    private static final int DEFAULT_LIMIT = 10;

    protected String URL = "http://hopamviet.com/chord/";
    protected String BASE_URL = "http://hopamviet.com/chord";
    protected String pageParam = "index";

    private int currentOffset;
    private Elements itemLinks;
    private Iterator<Element> _iterator;

    public HopAmChuanCrawlEngine() {
        currentOffset = 0;
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
        Element element;
        synchronized (_iterator) {
            element = _iterator.next();
        }
        String itemLink = element.attr("href");
        try {
            return Song.extract(itemLink);
        } catch (IOException e) {
            LOGGER.error("Can not extract song {}", itemLink, e);
            return null;
        }
    }

    @Override
    public void remove() {
        _iterator.remove();
    }

    private boolean _nextPage() {
        try {
            URL = Utils.buildURL(BASE_URL, pageParam, currentOffset);
            Webbot webbot = new Webbot(URL);
            String htmlData = webbot.crawl(Webbot.METHOD_GET);

            Document document = Jsoup.parse(htmlData);
            itemLinks = document.select(".row .fa-music + a");
            _iterator = itemLinks.iterator();

            currentOffset += DEFAULT_LIMIT;
            return itemLinks.size() > 0;
        } catch (IOException e) {
            return false;
        }
    }
}

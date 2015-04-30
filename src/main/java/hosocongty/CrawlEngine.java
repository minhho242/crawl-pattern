package hosocongty;

import model.Webbot;
import model.WebbotUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 * Description
 *
 * @author minhho242 on 3/24/15.
 */
public class CrawlEngine implements Iterator<Company> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlEngine.class);

    protected String DOMAIN = "http://www.hosocongty.vn";
    protected String URL = "http://www.hosocongty.vn/search.php?ot=0&p=0";
    protected String BASE_URL = "http://www.hosocongty.vn/search.php?ot=0&p=0";
    protected String _pageParam = "curpage";

    private int _currentPage = 1;
    private Elements _itemLinks;
    Iterator<Element> _iterator;
    
    public CrawlEngine() {
        _nextPage();
        
    }
    
    public synchronized boolean hasNext() {
        if ( !_iterator.hasNext() ) {
            _nextPage();
        }
        return _iterator.hasNext();
    }

    public Company next() {
        Element element = null;
        synchronized (_iterator) {
            element = _iterator.next();
        }
        String itemLink = element.attr("href");
        itemLink = DOMAIN + itemLink.replaceFirst("\\.", "");
        
        try {
            Company company = Company.extractCompany(itemLink);
            return company;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void remove() {
        _iterator.remove();
    }

    private boolean _nextPage() {
        try {
            URL = WebbotUtils.buildParams(BASE_URL, _pageParam, _currentPage++);
            Webbot webbot = new Webbot(URL);
            String htmlData = webbot.crawl(Webbot.METHOD_GET);

            Document document = Jsoup.parse(htmlData);
            _itemLinks = document.select("h3 a");

            _iterator = _itemLinks.iterator();
            return _itemLinks.size() > 0;
        } catch (IOException e) {
            return false;
        }
    }
}

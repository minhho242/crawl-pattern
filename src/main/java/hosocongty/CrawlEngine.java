package hosocongty;

import model.Webbot;
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

    protected String URL = "http://www.hosocongty.vn";
    protected String BASE_URL = "http://www.hosocongty.vn";
    protected String pageParam = "curpage";

    private int currentPage = 1;
    private Elements itemLinks;
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
        itemLink = BASE_URL + itemLink.replaceFirst("\\.", "");
        
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
            URL = _buildURL(pageParam, currentPage++);
            Webbot webbot = new Webbot(URL);
            String htmlData = webbot.crawl(Webbot.METHOD_GET);

            Document document = Jsoup.parse(htmlData);
            itemLinks = document.select("h3 a");

            _iterator = itemLinks.iterator();
            return itemLinks.size() > 0;
        } catch (IOException e) {
            return false;
        }
    }
    
    private String _buildURL(Object... params) {
        StringBuilder sb = new StringBuilder(BASE_URL);
        if ( params.length > 0 ) {
            sb.append("?");
            for (int i = 0; i < params.length; i += 2) {
                sb.append(params[i]).append("=").append(params[i+1]).append("&");
            }
            sb.deleteCharAt(sb.length() - 1); //remove last & symbol
        }
        return sb.toString();
    }
}

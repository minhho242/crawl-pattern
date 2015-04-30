import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import hopamchuan.HopAmChuanCrawlEngine;
import hopamchuan.Song;
import hopamchuan.SongDTO;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HibernateUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Description
 *
 * @author minhho242 on 4/11/15.
 */
public class HopAmChuanMain implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(HopAmChuanMain.class);
    private static HopAmChuanCrawlEngine crawlEngine = new HopAmChuanCrawlEngine();

    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {
        _loadLog4jConfig();

        int threadNo = _getThreadNo();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadNo; i++) {
            HopAmChuanMain main = new HopAmChuanMain();
            Thread t = new Thread(main);
            t.start();

            threads.add(t);
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        HibernateUtils.getSessionFactory().close();
    }

    private static int _getThreadNo() {
        int defaultThreadNo = 5;
        try {
            return Integer.parseInt(System.getProperty("threadNo", String.valueOf(defaultThreadNo)));
        } catch (NumberFormatException e) {
            return defaultThreadNo;
        }
    }

    private void _crawl() {
        Session session = HibernateUtils.getSessionFactory().openSession();

        while (crawlEngine.hasNext()) {
            Transaction transaction = session.beginTransaction();
            Song song = crawlEngine.next();

            SongDTO songDTO = new SongDTO(song);

            LOGGER.info(songDTO.getName());
            try {
                session.save(songDTO);
                transaction.commit();
            } catch (Throwable e) {
                session.getTransaction().rollback();
                if ( e instanceof MySQLIntegrityConstraintViolationException || e instanceof ConstraintViolationException) {
                    LOGGER.error("Duplicate song Id {}", songDTO.getSourceSongId());
                    break;
                } else {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        session.close();
    }

    private static void _loadLog4jConfig() throws FileNotFoundException {
        PropertyConfigurator.configure(new FileInputStream("conf/log4j.ini"));

    }

    @Override
    public void run() {
        _crawl();
    }
}

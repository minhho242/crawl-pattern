import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import hosocongty.Company;
import hosocongty.CompanyDTO;
import hosocongty.CrawlEngine;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
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
public class HoSoCongTyMain implements Runnable {


    private static Logger LOGGER = LoggerFactory.getLogger(HoSoCongTyMain.class);
    private static CrawlEngine crawlEngine = new CrawlEngine();

    public static void mainTemp(String[] args) throws IOException, IllegalAccessException, InstantiationException {
        _loadLog4jConfig();
        int threadNo = _getThreadNo();

        List<Thread> threads = new ArrayList<Thread>();

        for (int i = 0; i < threadNo; i++) {
            HoSoCongTyMain main = new HoSoCongTyMain();
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
            try {
                session.beginTransaction();
                Company comp = crawlEngine.next();

                CompanyDTO transientCompany = new CompanyDTO(comp);

                LOGGER.info(transientCompany.getName());

                session.save(transientCompany);
                session.getTransaction().commit();
            } catch (Throwable e) {
                session.getTransaction().rollback();
                if ( e instanceof MySQLIntegrityConstraintViolationException || e instanceof ConstraintViolationException) {
                    LOGGER.error(e.getMessage());
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

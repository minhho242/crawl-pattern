package hopamchuan;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HibernateUtils;

/**
 * Description
 *
 * @author minhho242 on 4/11/15.
 */
public class SongDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SongDAO.class);

    public static SongDTO getSongBySourceSongId(String sourceSongId) {
        try {
            Session session = HibernateUtils.getSessionFactory().openSession();
            return (SongDTO) session.createQuery("FROM SongDTO as song where song.sourceSongId = :sourceSongId").setParameter("sourceSongId", sourceSongId).uniqueResult();
        } catch (NonUniqueResultException e) {
            LOGGER.error("Not unique sourceSongId {}", sourceSongId, e);
            return null;
        }
    }

    public static boolean checkExistedBySourceSongId(String sourceSongId) {
        return getSongBySourceSongId(sourceSongId) != null;
    }
}

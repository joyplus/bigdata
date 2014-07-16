package tv.joyplus.backend.huan.dao.impl;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;
import de.ailis.pherialize.Pherialize;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import tv.joyplus.backend.huan.dao.LogProcessDao;

import java.util.List;
import java.util.Map;

public class LogProcessDaoImpl extends JdbcDaoSupport implements LogProcessDao {

    private final static Log log = LogFactory.getLog(LogProcessDaoImpl.class);
    private String tubeRequest;
    private String tubeReporting;
    @Autowired
    private BeanstalkClient beanstalk;

    @Override
    public void sendToRequestLog(List<Map> list) {
        try {
            beanstalk.useTube(tubeRequest);
            for (Map p : list) {
                String serialize = Pherialize.serialize(p);
                byte[] data = Pherialize.serialize(serialize).getBytes();
                beanstalk.put(1L, 0, 120, data);
            }
        } catch (BeanstalkException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void sendToRequestLog(Map map) {
        try {
            beanstalk.useTube(tubeRequest);
            String serialize = Pherialize.serialize(map);
            byte[] data = Pherialize.serialize(serialize).getBytes();
            beanstalk.put(1L, 0, 120, data);
        } catch (BeanstalkException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void sendToReporting(List<Map> list) {
        try {
            beanstalk.useTube(tubeReporting);
            for (Map p : list) {
                String serialize = Pherialize.serialize(p);
                byte[] data = Pherialize.serialize(serialize).getBytes();
                beanstalk.put(1L, 0, 120, data);
            }
        } catch (BeanstalkException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void sendToReporting(Map map) {
        try {
            beanstalk.useTube(tubeReporting);
            String serialize = Pherialize.serialize(map);
            byte[] data = Pherialize.serialize(serialize).getBytes();
            beanstalk.put(1L, 0, 120, data);
        } catch (BeanstalkException e) {
            log.error(e.getMessage());
        }
    }


    public void setTubeRequest(String tubeRequest) {
        this.tubeRequest = tubeRequest;
    }

    public void setTubeReporting(String tubeReporting) {
        this.tubeReporting = tubeReporting;
    }
}

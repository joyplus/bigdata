package tv.joyplus.backend.appinfo.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import tv.joyplus.backend.appinfo.beans.AppLogInfo;
import tv.joyplus.backend.appinfo.dao.AppLogDao;

import java.sql.SQLException;
import java.util.List;

public class AppLogItemWriter implements ItemWriter<AppLogInfo> {
    private static final Log log = LogFactory.getLog(AppLogItemWriter.class);
    @Autowired
    private AppLogDao appLogDao;

    @Override
    public void write(List<? extends AppLogInfo> list) throws SQLException {
        appLogDao.batchSave(list);
        log.info("write done -> " + list.size());
    }
}

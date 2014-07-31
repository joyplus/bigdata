package tv.joyplus.backend.qiniu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import tv.joyplus.backend.exception.TaskException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by zino on 14-7-8.
 */
public class QiniuManager {
    private final static Log log = LogFactory.getLog(QiniuManager.class);
    private HashMap<String, QiniuConfig> configs;

    public QiniuManager() throws Exception, TaskException {
        //初始化configs
        log.debug("init qiniu properties");
        configs = new HashMap<String, QiniuConfig>();
        Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("qiniu.properties"));
        String businessIds = prop.getProperty("business.ids", "");
        String[] ids = businessIds.split(",");
        if(ids.length<=0) {
            throw new TaskException("qiniu properties error!");
        }
        for(String businessId : ids) {
            log.debug("business id->" + businessId);
            configs.put(businessId, new QiniuConfig(businessId, prop));
        }

    }

    public QiniuConfig getQiniuConfig(String businessId) {
        return configs.get(businessId);
    }

    public void download(String businessId, String ident, File file) throws Exception {
        QiniuConfig config = configs.get(businessId);
        if(config==null) {
            return ;
        }
        config.download(downloadUrl(businessId, ident), file);
    }

    public String downloadUrl(String businessId, String ident) throws Exception {
        QiniuConfig config = configs.get(businessId);
        if(config==null) {
            return null;
        }
        return config.downloadUrl(ident);
    }

    public List<QiniuItem> list(String businessId, String prifix) {
        QiniuConfig config = configs.get(businessId);
        if(config==null) {
            return null;
        }
        return config.list(prifix);
    }

    public String formatFilename(String key) {
        return key.replaceAll("/", ".");
    }
}

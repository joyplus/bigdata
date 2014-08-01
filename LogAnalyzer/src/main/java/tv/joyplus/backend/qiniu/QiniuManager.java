package tv.joyplus.backend.qiniu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import tv.joyplus.backend.exception.TaskException;
import tv.joyplus.backend.utils.FormatTool;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import tv.joyplus.backend.appinfo.Config;

/**
 * Created by zino on 14-7-8.
 */
public class QiniuManager {
    private final static Log log = LogFactory.getLog(QiniuManager.class);
    private HashMap<String, QiniuConfig> configs;

    public QiniuManager() throws Exception, TaskException {
        configs = new HashMap<String, QiniuConfig>();
        Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource(Config.LocalFile.QINIU_PROPERTIES));
        String businessIds = prop.getProperty("business.ids", "");
        if(!FormatTool.isEmpty(businessIds)) {
            String[] ids = businessIds.split(",");
            if (ids.length > 0) {// we allow no config qiniu.it will can't load filelist follow.
                for (String businessId : ids) {
                    configs.put(businessId, new QiniuConfig(businessId, prop));
                }
            }
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

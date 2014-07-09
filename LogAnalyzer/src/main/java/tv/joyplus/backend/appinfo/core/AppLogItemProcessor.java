package tv.joyplus.backend.appinfo.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import tv.joyplus.backend.appinfo.beans.AppLogInfo;
import tv.joyplus.backend.appinfo.beans.AppLogInfoV1;
import tv.joyplus.backend.exception.TaskException;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class AppLogItemProcessor implements ItemProcessor<String, AppLogInfo> {
    private static final Log log = LogFactory.getLog(AppLogItemProcessor.class);
    private Properties deviceInfo;
    private String path;
    private String businessId;

    @Override
    public AppLogInfo process(String line) throws Exception {
        return praseLogInfo(line);
    }

    private AppLogInfo praseLogInfo(String line) {
        try {
            return createAppLogInfo(line);
        } catch (TaskException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private AppLogInfo createAppLogInfo(String line) throws TaskException {
        if (line == null || line.trim().length() <= 0) {
            return null;
        }
        if (deviceInfo != null) {
            String version = deviceInfo.getProperty("version", "");
            if ("1.0".equals(version)) {
                return new AppLogInfoV1(deviceInfo, line, businessId);
            }
        }
        return null;
    }

    public void setDeviceInfo(Properties deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public void setPath(String path) {
        this.path = path;
        try {
            deviceInfo = new Properties();
            deviceInfo.load(new FileInputStream(new File(path, "/DevicesInfo")));
        } catch (Exception e){
            deviceInfo = null;
        }
    }
}

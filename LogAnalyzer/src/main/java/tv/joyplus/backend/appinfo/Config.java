package tv.joyplus.backend.appinfo;

/**
 * Created by zino on 14-7-22.
 */
public class Config {
    public static class LocalFile{
        public static String QINIU_PROPERTIES   = "qiniu.properties";
    }
    public static class DataBaseKey {
        public static String QUARTS_CRON_EXPRESSION_LOGANALYZE = "quartz.cron.expression.appLogAnalyze";
        public static String QUARTS_CRON_EXPRESSION_LOGLOAD = "quartz.cron.expression.appLogLoad";
    }

    public static class LocationKey {
        public static String AP_LOG_DIR = "app.log.dir";
        public static String AP_LOG_DOWNLOAD_DIR = "app.log.download.dir";
    }
}

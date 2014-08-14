package tv.joyplus.backend.appinfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import tv.joyplus.backend.config.ConfigManager;

public class AppLogAnalyzeLauncherWapper extends CronTriggerFactoryBean {
    private final static Log log = LogFactory.getLog(AppLogAnalyzeLauncherWapper.class);

    @Autowired
    private ConfigManager configManager;
    private String[] businessIds;

    public void setBusinessIds(String businessIds) {
        this.businessIds = businessIds.split(",");
        this.setCronExpression(this.configManager.getConfiguration(
                Config.DataBaseKey.QUARTS_CRON_EXPRESSION_LOGANALYZE).get(this.businessIds[0]));
    }
}

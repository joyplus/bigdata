package tv.joyplus.backend.appinfo.core;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;

import tv.joyplus.backend.huan.beans.LogData;

public class AppLogItemProcessor implements ItemProcessor<String, LogData> {
	private static final Log log = LogFactory.getLog(AppLogItemProcessor.class);
	private static String PATTERN_STRING = "\\[(.*?)\\].*?dnum\\=(.*?),devmodel\\=(.*?),version\\=(.*?),ip\\=(.*?),imgurl\\=(.*?),adurl\\=(.*?),sid\\=(.*?),title\\=(.*)";
	private String filename;
	private Map<String, Integer> zones;
	@Override
	public LogData process(String line) throws Exception {
		return this.praseLogInfo(line);
	}
	
	private LogData praseLogInfo(String line) {
		Pattern p = Pattern.compile(PATTERN_STRING);
		Matcher m = p.matcher(line);
		if(m.find()) {
			LogData log = new LogData();
			log.setAdDate(m.group(1));
			log.setEquipmentKey(m.group(2));
			log.setDeviceName(m.group(3));
			log.setVersion(m.group(4));
			log.setIp(m.group(5));
			log.setImgurl(m.group(6));
			log.setAdurl(m.group(7));
			log.setSid(m.group(8));
			log.setTitle(m.group(9));
			log.setZoneId(analyzeZoneId());
			return log;
		}
		return null;
	}
	private long analyzeZoneId(){
		if(filename==null) {
			return 0;
		}
		//根据filename获取zone_id
		String name = filename.substring(0, filename.length()-14);
		if(zones.containsKey(name)) {
			return zones.get(name);
		}
		return 0;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setZones(Map<String, Integer> zones) {
		this.zones = zones;
	}
	
}

package tv.joyplus.backend.huan.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.batch.item.ItemProcessor;

import tv.joyplus.backend.huan.beans.LogInfo;

public class LogItemProcessor implements ItemProcessor<String, LogInfo>{
	private static String PATTERN_STRING = "\\[(.*?)\\].*?dnum\\=(.*?),devmodel\\=(.*?),version\\=(.*?),ip\\=(.*?),imgurl\\=(.*?),adurl\\=(.*?),sid\\=(.*?),title\\=(.*)";
	private String filename;
	@Override
	public LogInfo process(String line) throws Exception {
		return this.praseLogInfo(line);
	}
	
	private LogInfo praseLogInfo(String line) {
		LogInfo log = new LogInfo();
		Pattern p = Pattern.compile(PATTERN_STRING);
		Matcher m = p.matcher(line);
		if(m.find()) {
			log.setAdDate(m.group(1));
			log.setEquitpmentKey(m.group(2));
			log.setDeviceName(m.group(3));
			log.setVersion(m.group(4));
			log.setIp(m.group(5));
			log.setImgurl(m.group(6));
			log.setAdurl(m.group(7));
			log.setSid(m.group(8));
			log.setTitle(m.group(9));
			log.setZoneId(analyzeZoneId());
		}
		return log;
	}
	private long analyzeZoneId(){
		//根据filename获取zone_id
		return 0;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}

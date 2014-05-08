package tv.joyplus.backend.huan.core;

import org.springframework.batch.item.ItemProcessor;

public class LogItemProcessor implements ItemProcessor<String, String>{

	@Override
	public String process(String line) {
		return line;
	}
}

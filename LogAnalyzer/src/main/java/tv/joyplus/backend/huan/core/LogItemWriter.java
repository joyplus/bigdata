package tv.joyplus.backend.huan.core;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import tv.joyplus.backend.huan.beans.LogInfo;

public class LogItemWriter implements ItemWriter<LogInfo> {

	@Override
	public void write(List<? extends LogInfo> list) {
		for (LogInfo str : list) {
			System.out.println(str);
		}
	}
}

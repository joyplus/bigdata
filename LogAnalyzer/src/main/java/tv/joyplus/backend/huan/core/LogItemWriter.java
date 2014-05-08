package tv.joyplus.backend.huan.core;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class LogItemWriter implements ItemWriter<String> {

	@Override
	public void write(List<? extends String> list) {
		for (String str : list) {
			System.out.println(str);
		}
	}
}

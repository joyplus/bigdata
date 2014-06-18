package tv.joyplus.backend.appinfo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath:appinfo/batch.xml");
	}
}

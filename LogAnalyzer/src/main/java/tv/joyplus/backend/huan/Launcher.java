package tv.joyplus.backend.huan;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath:huan/batch.xml");
	}
}

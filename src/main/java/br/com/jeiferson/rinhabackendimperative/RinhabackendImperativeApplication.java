package br.com.jeiferson.rinhabackendimperative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RinhabackendImperativeApplication {

	static {
		System.out.println("Logging tuning properties...");
		System.out.println("VIRTUAL_THREADS: " + System.getenv("VIRTUAL_THREADS"));
		System.out.println("POOL_SIZE: " + System.getenv("POOL_SIZE"));
		System.out.println("TOMCAT_THREAD_POOL: " + System.getenv("TOMCAT_THREAD_POOL"));
	}

	public static void main(String[] args) {
		SpringApplication.run(RinhabackendImperativeApplication.class, args);
	}

}

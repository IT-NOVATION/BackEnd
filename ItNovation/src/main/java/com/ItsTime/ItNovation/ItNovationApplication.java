package com.ItsTime.ItNovation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ItNovationApplication {

	public static void main(String[] args) {
		SpringApplication application=new SpringApplication(ItNovationApplication.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);
	}

}

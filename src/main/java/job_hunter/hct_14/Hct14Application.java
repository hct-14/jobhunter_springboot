package job_hunter.hct_14;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


//@SpringBootApplication(exclude = {
//		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
////		org.springframework.boot.actuate.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//})
@SpringBootApplication

@EnableAsync
@EnableScheduling
public class Hct14Application {
	public static void main(String[] args) {
		SpringApplication.run(Hct14Application.class, args);
	}
}


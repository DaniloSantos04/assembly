package br.com.assembly;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongock
@EnableScheduling
@EnableRabbit
public class AssemblyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssemblyApplication.class, args);
	}

}

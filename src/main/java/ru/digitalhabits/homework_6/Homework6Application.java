package ru.digitalhabits.homework_6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.*;
import org.springframework.boot.actuate.autoconfigure.metrics.startup.StartupTimeMetricsListenerAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.web.client.HttpClientMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.web.tomcat.TomcatMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;

@SpringBootApplication
(
		exclude = {
				JvmMetricsAutoConfiguration.class,
				TomcatMetricsAutoConfiguration.class,
				TaskExecutionAutoConfiguration.class,
				StartupTimeMetricsListenerAutoConfiguration.class,
				HttpClientMetricsAutoConfiguration.class,
				SystemMetricsAutoConfiguration.class,
				LogbackMetricsAutoConfiguration.class
		}
				)
public class Homework6Application {

	public static void main(String[] args) {
		SpringApplication.run(Homework6Application.class, args);
	}

}
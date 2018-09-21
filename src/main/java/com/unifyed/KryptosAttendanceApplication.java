package com.unifyed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.MappedInterceptor;

import com.unifyed.attendance.core.UnifyedTenantInterceptor;


@SpringBootApplication
public class KryptosAttendanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KryptosAttendanceApplication.class, args);
	}
	
	@Bean
	public MappedInterceptor myMappedInterceptor() {
		return new MappedInterceptor(new String[]{"/**"}, new UnifyedTenantInterceptor());
	}
}


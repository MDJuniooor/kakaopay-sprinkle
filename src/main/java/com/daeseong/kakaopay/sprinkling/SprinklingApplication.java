package com.daeseong.kakaopay.sprinkling;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SprinklingApplication {
	public static void main(String[] args) {
		SpringApplication.run(SprinklingApplication.class, args);
	}
}

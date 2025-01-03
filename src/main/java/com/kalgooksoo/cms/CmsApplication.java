package com.kalgooksoo.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * CMS 애플리케이션 메인 클래스
 */
@SpringBootApplication
public class CmsApplication extends SpringBootServletInitializer {

	/**
	 * Spring application context를 생성하고 초기화합니다.
	 * @param args 커맨드 라인 인자
	 */
	public static void main(String[] args) {
		SpringApplication.run(CmsApplication.class, args);
	}

	/**
	 * Configures the application when deployed in a servlet container.
	 * This method is called to customize the SpringApplicationBuilder
	 * during application initialization.
	 *
	 * @param application the builder for the application
	 * @return the configured SpringApplicationBuilder instance
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CmsApplication.class);
	}

}

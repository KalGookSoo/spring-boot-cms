package com.kalgooksoo.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CMS 애플리케이션 메인 클래스
 */
@SpringBootApplication
public class CmsApplication {

	/**
	 * Spring application context를 생성하고 초기화합니다.
	 * @param args 커맨드 라인 인자
	 */
	public static void main(String[] args) {
		SpringApplication.run(CmsApplication.class, args);
	}

}

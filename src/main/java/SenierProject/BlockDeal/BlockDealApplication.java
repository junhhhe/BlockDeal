package SenierProject.BlockDeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing  // JPA Auditing 활성화
public class BlockDealApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockDealApplication.class, args);
	}

	// Application 클래스나 설정 클래스의 static 초기화 블록에 추가
	public class Application {
		static {
			SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
		}
	}
}

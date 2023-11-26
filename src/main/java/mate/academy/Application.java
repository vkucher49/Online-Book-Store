package mate.academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "mate.academy")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

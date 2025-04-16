package lt.viko.eif.pvaiciulis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Entry point class for the homelessapi application.
 */
@SpringBootApplication
@EnableCaching
public class ECommerceAPI {

    /**
     * Main method to start the homelessapi application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ECommerceAPI.class, args);
    }

}
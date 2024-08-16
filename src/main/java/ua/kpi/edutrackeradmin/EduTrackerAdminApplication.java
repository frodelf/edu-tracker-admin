package ua.kpi.edutrackeradmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ua.kpi.edutrackerentity", "ua.kpi.edutrackeradmin"})
public class EduTrackerAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduTrackerAdminApplication.class, args);
    }

}

package at.spengergasse.spring_thymeleaf;

import at.spengergasse.spring_thymeleaf.entities.Device;
import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringThymeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringThymeleafApplication.class, args);
    }

    @Bean
    CommandLineRunner initDevices(DeviceRepository deviceRepository) {
        return args -> {
            if (deviceRepository.count() == 0) {

                Device d1 = new Device();
                d1.setName("MRT_01");
                d1.setType("MRT");
                d1.setLocation("Room 101");

                Device d2 = new Device();
                d2.setName("CT_01");
                d2.setType("CT");
                d2.setLocation("Room 102");

                Device d3 = new Device();
                d3.setName("XRAY_01");
                d3.setType("X-Ray");
                d3.setLocation("Room 103");

                Device d4 = new Device();
                d4.setName("CT_01");
                d4.setType("CT");
                d4.setLocation("Room 108");

                deviceRepository.save(d1);
                deviceRepository.save(d2);
                deviceRepository.save(d3);
                deviceRepository.save(d4);
            }
        };
    }
}
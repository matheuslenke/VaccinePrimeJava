package br.lenkeryan;

import br.lenkeryan.config.ManagerConsumerConfig;
import br.lenkeryan.model.ManagerInfo;
import br.lenkeryan.model.ProgramData;
import br.lenkeryan.producer.ManagerProducer;
import br.lenkeryan.utils.CustomJsonReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VaccinePrimeApp {

    public static void main(String[] args) {
        new ProgramData();
        SpringApplication.run(VaccinePrimeApp.class, args);

//        Thread thread = new Thread(new ManagerConsumerConfig());
//        thread.start();

    }
}

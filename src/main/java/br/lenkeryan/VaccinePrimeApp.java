package br.lenkeryan;

import br.lenkeryan.consumer.ManagerStreamsConsumer;
import br.lenkeryan.model.ProgramData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VaccinePrimeApp {

    public static void main(String[] args) {
        ProgramData data = new ProgramData();
        SpringApplication.run(VaccinePrimeApp.class, args);

//        Thread thread = new Thread(new ManagerStreamsConsumer());
//        thread.start();

    }
}

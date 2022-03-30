package br.lenkeryan.producer;

import br.lenkeryan.model.ManagerCoordinates;
import br.lenkeryan.model.ManagerInfo;
import br.lenkeryan.utils.Constants;
import br.lenkeryan.utils.CustomJsonReader;
import br.lenkeryan.utils.TopicManager;
import com.google.gson.Gson;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

public class ManagerProducer {
    static ManagerInfo managerInfo;
    static TopicManager topicManager;
    static CustomJsonReader jsonReader = new CustomJsonReader();
    static Logger logger = Logger.getLogger("VaccineLogger");
    static Gson json = new Gson();
    static Integer sleepingTime = 10;

    public static void main(String[] args) {

        managerInfo = jsonReader.readManagerJsonInfo("managerProducer/manager1.json");
        run();
    }


    public static void run() {
        //cria produtor com as devidas propriedades (SerDes customizado)
        KafkaProducer<String, String> producer = createProducer();
        //shutdown hook
//        Runtime.getRuntime().addShutdownHook();
        while (true) {
            if (managerInfo == null) {
                return;
            }

            ManagerCoordinates info = getManagerCoordinates();
            String data = json.toJson(info);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(Constants.MANAGERS_TOPIC, managerInfo.getId(), data);
            //enviar Localização serializada para Kafka
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("Manager: " + managerInfo.getName());
                        logger.info("Metadados recebidos \n " +
                                "Topic " + metadata.topic() + "\n " +
                                "Partition: " + metadata.partition() + "\n" +
                                "Offset: " + metadata.offset() + "\n" +
                                "Timestamp: " + metadata.timestamp());
                    } else {
                        logger.info(exception.getLocalizedMessage());
                    }
                    try {
                        Thread.sleep(sleepingTime * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            producer.flush();
        }
    }

    //iniciar produtor Kafka
    private static KafkaProducer<String, String> createProducer() {
        String BootstrapServer = "localhost:9092";
        //create properties
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<>(prop);
    }

    private static ManagerCoordinates getManagerCoordinates() {
        if ( managerInfo == null ) { return null; }
        Random random = new Random();
        double latitude = random.nextDouble(-0.001, 0.001) + managerInfo.getInitialCoordinate().getLat();
        double longitude = random.nextDouble(-0.001, 0.001) + managerInfo.getInitialCoordinate().getLon();
        return new ManagerCoordinates(latitude, longitude, managerInfo);
    }
}

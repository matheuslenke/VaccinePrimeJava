package br.lenkeryan.topology;

import br.lenkeryan.model.ManagerCoordinates;
import br.lenkeryan.model.ManagerInfo;
import br.lenkeryan.model.ProgramData;
import br.lenkeryan.utils.Constants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Properties;
import java.util.logging.Logger;

public class ManagerCoordinateTopology {
    public static String MANAGER_COORDINATES_STORE = "manager-coordinates-store";
    static Logger logger = Logger.getLogger("VaccineLogger");
//    public static final String MANAGER_COORDINATES_STORE = "manager-coordinates-store";

    public static Properties getProps() {
        Properties prop = new Properties();

        prop.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, Constants.APPLICATION_ID);
        prop.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVER);
        prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        prop.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        prop.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        return prop;
    }

    public static Topology buildTopology() {
        Serde<ManagerCoordinates> managerCoordinatesSerdes = new JsonSerde<>(ManagerCoordinates.class);
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KTable<String, ManagerCoordinates> managerLocations = streamsBuilder.table(Constants.MANAGERS_TOPIC, Consumed.with(Serdes.String(), managerCoordinatesSerdes));

        managerLocations.toStream()
                .mapValues(value -> {
                    analyseManagerInfo(value);
                    return value;
                }).peek((key, value) -> logger.info("Name: " + value.getManager().getName()));

        return streamsBuilder.build();
    }

    private static void analyseManagerInfo(ManagerCoordinates managerCoordinate) {
        Boolean managerExists = ProgramData.returnIfManagerExists(managerCoordinate.getManager().getId());
        if(!managerExists) {
            logger.info("[ManagerConsumer] Novo manager com nome ${info.manager.name} registrado no consumidor.");
            ProgramData.managers.put(managerCoordinate.getManager().getId().toString(), managerCoordinate.getManager());
        } else {
            ManagerInfo actualManager = ProgramData.managers.get(managerCoordinate.getManager().getId());
            if (actualManager != null) {
                actualManager.setInitialCoordinate(managerCoordinate.getCoordinate());
            }
        }
    }
}
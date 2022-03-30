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
    static Logger logger = Logger.getLogger("VaccineLogger");
    public static final String MANAGER_COORDINATES_STORE = "manager-coordinates-store";

    public static Properties getProps() {
        Properties prop = new Properties();

        prop.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, Constants.MANAGERS_TOPIC);
        prop.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVER);
        prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        prop.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        prop.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        return prop;
    }

    public static Topology buildTopology() {

        Properties prop = getProps();
        Serde<ManagerCoordinates> managerCoordinatesSerdes = new JsonSerde<>(ManagerCoordinates.class);
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KTable<String, ManagerCoordinates> managerLocations = streamsBuilder.table(Constants.MANAGERS_TOPIC, Consumed.with(Serdes.String(), managerCoordinatesSerdes));

        managerLocations.toStream()
                .mapValues(value -> {
                    analyseManagerInfo(value);
                    return value;
                }).peek((key, value) -> logger.info("Name: " + value.getManager().getName()))
                .groupByKey()
                .aggregate(ManagerCoordinates::new, (key, value, aggregate) -> aggregate,
                        Materialized.<String, ManagerCoordinates, KeyValueStore<Bytes, byte[]>>as(MANAGER_COORDINATES_STORE)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(managerCoordinatesSerdes)
                );

        return streamsBuilder.build();
    }

    private static void analyseManagerInfo(ManagerCoordinates managerCoordinate) {
        Boolean managerExists = ProgramData.returnIfManagerExists(managerCoordinate.getManager().getId().toString());
        if(!managerExists) {
            logger.info("[ManagerConsumer] Novo manager com nome ${info.manager.name} registrado no consumidor.");
            ProgramData.managers.put(managerCoordinate.getId().toString(), managerCoordinate.getManager());
        } else {
            ManagerInfo actualManager = ProgramData.managers.get(managerCoordinate.getManager().getId().toString());
            if (actualManager != null) {
                actualManager.setInitialCoordinate(managerCoordinate.getCoordinate());
            }
        }
    }
}


//    KStream<Long, BankBalance> bankBalancesStream = streamsBuilder.stream(BANK_TRANSACTIONS,
//                    Consumed.with(Serdes.Long(), bankTransactionSerdes))
//            .groupByKey()
//            .aggregate(BankBalance::new,
//                    (key, value, aggregate) -> aggregate.process(value),
//                    Materialized.with(Serdes.Long(), bankBalanceSerde))
//            .toStream();
//        bankBalancesStream
//                .to(BANK_BALANCES, Produced.with(Serdes.Long(), bankBalanceSerde));
//
//                bankBalancesStream
//                .mapValues((readOnlyKey, value) -> value.getLatestTransaction())
//                .filter((key, value) -> value.state == BankTransaction.BankTransactionState.REJECTED)
//                .to(REJECTED_TRANSACTIONS, Produced.with(Serdes.Long(), bankTransactionSerdes));
//
//                return streamsBuilder.build();
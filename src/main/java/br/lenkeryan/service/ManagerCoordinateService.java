package br.lenkeryan.service;

import br.lenkeryan.model.ManagerCoordinates;
import br.lenkeryan.topology.ManagerCoordinateTopology;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ManagerCoordinateService {

    private final KafkaStreams managerKafkaStreams;

    @Autowired
    public ManagerCoordinateService(KafkaStreams managerKafkaStreams) {
        this.managerKafkaStreams = managerKafkaStreams;
    }

    public ArrayList<ManagerCoordinates> getManagerCoordinates() {
        ArrayList<ManagerCoordinates> managers = new ArrayList<>();
        KeyValueIterator<String, ManagerCoordinates> iterator = getStore().all();
        iterator.forEachRemaining(stringManagerCoordinatesKeyValue -> {
             managers.add(stringManagerCoordinatesKeyValue.value);
        });
        return managers;
    }

    private ReadOnlyKeyValueStore<String, ManagerCoordinates> getStore() {
        return managerKafkaStreams.store(StoreQueryParameters.fromNameAndType(ManagerCoordinateTopology.MANAGER_COORDINATES_STORE, QueryableStoreTypes.keyValueStore()));
    }
}

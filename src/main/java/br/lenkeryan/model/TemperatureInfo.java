package br.lenkeryan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemperatureInfo {
    Double value = 0.0;
    TemperatureProducerInfo producerInfo;
    Coordinate actualCoordinate;
}

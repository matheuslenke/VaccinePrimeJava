package br.lenkeryan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemperatureProducerInfo {
    String id;
    String hospital;
    ArrayList<Vaccine> vaccines;
    Coordinate initialCoordinate;
}

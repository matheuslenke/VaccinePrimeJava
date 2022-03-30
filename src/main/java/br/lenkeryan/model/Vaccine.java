package br.lenkeryan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vaccine {
    String brand;
    String volume;
    Double maxTemperature = 0D;
    Double minTemperature = 0D;
    Double maxDuration = 0D;
    Long lastTimeOutOfBounds = 0L;

    public Boolean checkIfTemperatureIsOutOfBounds(Double temperature) {
        if(temperature < minTemperature || temperature > maxTemperature) {
            return true;
        }
        return false;
    }

    public Boolean checkIfTemperatureIsNearOutOfBounds(Double temperature) {
        Double tolerance = 1.0;
        if(temperature < minTemperature + tolerance && temperature > minTemperature) {
            return true;
        }
        if (temperature > maxTemperature - tolerance && temperature < maxTemperature) {
            return true;
        }
        return false;
    }

}

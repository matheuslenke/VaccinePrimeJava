package br.lenkeryan.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerCoordinates {
    Integer id;
    Coordinate coordinate;
    ManagerInfo manager;

    public ManagerCoordinates(double latitude, double longitude, ManagerInfo managerInfo) {
        Coordinate coord = new Coordinate(latitude, longitude);
        this.coordinate = coord;
        this.manager = managerInfo;
    }
}

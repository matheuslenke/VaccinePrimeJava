package br.lenkeryan.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerInfo {
    String id;
    String name;
    String phone;
    String email;
    Coordinate initialCoordinate;

}

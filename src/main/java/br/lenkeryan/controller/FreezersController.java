package br.lenkeryan.controller;

import br.lenkeryan.model.ManagerInfo;
import br.lenkeryan.model.ProgramData;
import br.lenkeryan.model.TemperatureProducerInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/freezers")
public class FreezersController {

    @GetMapping
    public ResponseEntity<List<TemperatureProducerInfo>> getManagersCoordinates() {
        return ResponseEntity.ok(ProgramData.knowFreezers.values().stream().toList());
    }
}

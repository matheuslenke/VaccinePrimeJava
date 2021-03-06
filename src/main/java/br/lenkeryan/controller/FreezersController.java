package br.lenkeryan.controller;

import br.lenkeryan.model.ManagerInfo;
import br.lenkeryan.model.ProgramData;
import br.lenkeryan.model.TemperatureInfo;
import br.lenkeryan.model.TemperatureProducerInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/freezers")
public class FreezersController {

    @GetMapping
    public ResponseEntity<List<TemperatureProducerInfo>> getManagersCoordinates() {
        return ResponseEntity.ok(ProgramData.knowFreezers.values().stream().toList());
    }
    @GetMapping("/temperatures")
    public ResponseEntity<List<TemperatureInfo>> getTemperatureInfoList() {
        return ResponseEntity.ok(ProgramData.temperatureReads.stream().toList());
    }
}

package br.lenkeryan.controller;

import br.lenkeryan.model.ManagerCoordinates;
import br.lenkeryan.model.ProgramData;
import br.lenkeryan.service.ManagerCoordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/manager-coordinates")
public class ManagerCoordinateController {

//    private final ManagerCoordinateService service;
//
//    @Autowired
//    public ManagerCoordinateController(ManagerCoordinateService managerCoordinateService) {
//        this.service = managerCoordinateService;
//    }

    @GetMapping
    public ResponseEntity<List<ManagerCoordinates>> getManagersCoordinates() {
        return ResponseEntity.ok(ProgramData.managerCoordinates.values().stream().toList());
    }
}

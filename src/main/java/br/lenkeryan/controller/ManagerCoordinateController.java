package br.lenkeryan.controller;

import br.lenkeryan.model.ManagerCoordinates;
import br.lenkeryan.model.ManagerInfo;
import br.lenkeryan.model.ProgramData;
import br.lenkeryan.service.ManagerCoordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/manager-coordinates")
public class ManagerCoordinateController {

    @GetMapping
    public ResponseEntity<List<ManagerInfo>> getManagersCoordinates() {
        return ResponseEntity.ok(ProgramData.managers.values().stream().toList());
    }

}

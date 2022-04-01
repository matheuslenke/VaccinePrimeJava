package br.lenkeryan.controller;

import br.lenkeryan.model.ManagerInfo;
import br.lenkeryan.model.Notification;
import br.lenkeryan.model.ProgramData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications() {
        return ResponseEntity.ok(ProgramData.notifications.stream().toList());
    }
}

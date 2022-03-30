package br.lenkeryan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Notification {
    NotificationType notificationType;
    String message;
    Boolean willNotificateAllManagers = false;
    ArrayList<ManagerInfo> managersToNotificate;

    public Notification(NotificationType type, String message, ManagerInfo nearestManager) {
        this.notificationType = type;
        this.message = message;
        managersToNotificate.add(nearestManager);
    }
}

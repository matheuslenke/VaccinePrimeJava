package br.lenkeryan.model;

import org.apache.catalina.Manager;

import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class ProgramData {
    public static ConcurrentHashMap<String, ManagerInfo> managers = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, TemperatureProducerInfo> knowFreezers = new ConcurrentHashMap<>();


    public static Boolean returnIfFreezerExists(String key) {
        if (knowFreezers.get(key) == null) {
            return false;
        }
        return true;
    }

    public static Boolean returnIfManagerExists(String key) {
        if (managers.get(key) == null) {
            return false;
        };
        return true;
    }

    public static ManagerInfo getNearestManager(Coordinate coordinate) {
        final ManagerInfo[] nearestManager = {null};
        final double[] nearestDistance = {0.0};
        managers.forEach((s, manager) -> {
            if (nearestManager[0] == null) {
                nearestManager[0] = manager;
                nearestDistance[0] = calculateDistance(coordinate, nearestManager[0].initialCoordinate);
            } else {
                var distance = calculateDistance(coordinate, nearestManager[0].initialCoordinate);

                if (distance != null) {
                    if (distance < nearestDistance[0]) {
                        nearestManager[0] = manager;
                    }
                }
            }
        });

        return nearestManager[0];
    }


    private static Double calculateDistance(Coordinate coordinate1, Coordinate coordinate2) {
        var earthRadius = 6371e3; //raio da terra em metros

        double sigma1 = coordinate1.getLat() * Math.PI / 180; // φ, λ in radians

        double sigma2 = coordinate2.getLat() * Math.PI / 180;
        double deltaSigma = (coordinate2.getLat() - coordinate1.getLat()) * Math.PI / 180;
        double deltaLambda = (coordinate2.getLon() - coordinate1.getLon()) * Math.PI / 180;

        double a = Math.sin(deltaSigma / 2) * Math.sin(deltaSigma / 2) +
                Math.cos(sigma1) * Math.cos(sigma2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return earthRadius * c;

    }
}

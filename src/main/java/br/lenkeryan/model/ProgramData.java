package br.lenkeryan.model;

import java.util.concurrent.ConcurrentHashMap;

public class ProgramData {
    public static ConcurrentHashMap<String, ManagerInfo> managers = new ConcurrentHashMap<String, ManagerInfo>();


    public static Boolean returnIfFreezerExists(String key) {
//        knownFreezersMap ?: return false
        return true;
    }

    public static Boolean returnIfManagerExists(String key) {
        if (managers.get(key) != null) {
            return false;
        };
        return true;
    }
}

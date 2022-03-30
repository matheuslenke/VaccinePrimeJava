package br.lenkeryan.model;

import java.util.concurrent.ConcurrentHashMap;

public class ProgramData {
    ConcurrentHashMap<String, ManagerInfo> managers = new ConcurrentHashMap<String, ManagerInfo>();

}

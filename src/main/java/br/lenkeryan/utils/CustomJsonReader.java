package br.lenkeryan.utils;


import br.lenkeryan.model.ManagerInfo;
import br.lenkeryan.model.TemperatureProducerInfo;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomJsonReader {
    
    public ManagerInfo readManagerJsonInfo(String filename) {
        try {
            File file = new File(filename);
            Gson gson = new Gson();
            return gson.fromJson(new FileReader(file), ManagerInfo.class);
        } catch ( Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public TemperatureProducerInfo readTemperatureProducerJsonInfo(String filename) {
        try {
            File file = new File(filename);
            Gson gson = new Gson();
            return gson.fromJson(new FileReader(file), TemperatureProducerInfo.class);
        } catch ( Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    


//    fun readManagerJsonInfo(filename: String) : ManagerInfo {
//        val fileContent = File(filename).readText()
//
//        return Json.decodeFromString(fileContent)
//    }
//
//    fun readManagerJsonList(filename: String) : ArrayList<ManagerInfo> {
//        val fileContent = File(filename).readText()
//
//        return Json.decodeFromString(fileContent)
//    }
}

package br.lenkeryan.utils;

public class Constants {
    public static Double TOLERANCE = 1.0;
    public static String BOOTSTRAP_SERVER = "localhost:9092";

    public static String MANAGERS_TOPIC = "managers-coordinates";
    public static String NOTIFICATIONS_TOPIC = "vaccine-notifications";
    public static String APPLICATION_ID = "vaccinePrime";

    public static Integer MANAGERS_PARTITIONS = 3;
    public static Integer VACCINE_PARTITIONS = 3;

}

package br.lenkeryan.utils;

import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Properties;

public class TopicManager {

    void createTopic(String topicName, Integer numPartitions) {
        Properties config = new Properties();

        config.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

//        val admin = AdminClient.create(config)
//
//        //checking if topic already exists
//        val alreadyExists = admin.listTopics().names().get().stream()
//                .anyMatch { existingTopicName: String -> existingTopicName == topicName }
//        if (alreadyExists) {
//            System.out.printf("topic already exits: %s%n", topicName)
//        } else {
//            //creating new topic
//            System.out.printf("creating topic: %s%n", topicName)
//            val newTopic = NewTopic(topicName, numPartitions, 1.toShort())
//            admin.createTopics(setOf(newTopic)).all().get()
//        }
//
//        //describing
//        println("-- describing topic --")
//        admin.describeTopics(setOf(topicName)).all().get()
//                .forEach { (topic: String, desc: TopicDescription) ->
//            println("Topic: $topic")
//            System.out.printf(
//                    "Partitions: %s, partition ids: %s%n", desc.partitions().size,
//                    desc.partitions()
//                            .stream()
//                            .map { p: TopicPartitionInfo ->
//                    Integer.toString(
//                            p.partition()
//                    )
//            }
//                        .collect(Collectors.joining(","))
//                )
//        }
    }
//
//    fun deleteTopic(topicName: String) {
//        val config = Properties()
//        config[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
//        val admin = AdminClient.create(config)
//
//        //checking if topic already exists
//        val listOfTopics = admin.listTopics().names().get().stream().toList()
//        val alreadyExists = listOfTopics.find {existingTopicName: String -> existingTopicName == topicName}
//
//        if (alreadyExists != null) {
//            admin.deleteTopics(setOf(alreadyExists))
//            println("Topic $alreadyExists Deleted!")
//        }
//    }
}

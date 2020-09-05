package it.polimi.middleware.kafka_pipeline;

import it.polimi.middleware.kafka_pipeline.common.Config;
import it.polimi.middleware.kafka_pipeline.parser.Parser;
import it.polimi.middleware.kafka_pipeline.threads.TaskManager;
import it.polimi.middleware.kafka_pipeline.topics.TopicsManager;
import java.util.Collections;

public class MainTaskManager0 {

    public static void main(String[] args) {

        // Parse global configurations
        new Parser();
        Parser.parseConfig();

        Config.printConfiguration();

        new TaskManager(0, 2).start();

    }
}
package it.polimi.middleware.kafka_pipeline.threads;

import it.polimi.middleware.kafka_pipeline.common.Config;
import it.polimi.middleware.kafka_pipeline.common.Utils;
import it.polimi.middleware.kafka_pipeline.parser.Parser;
import it.polimi.middleware.kafka_pipeline.processors.StreamProcessor;

import java.util.ArrayList;
import java.util.List;

public class JobManager {

    private List<TaskManager> taskManagers;

    public JobManager() {
        this.taskManagers = new ArrayList<>();

        // create some pipelines, according to the PARALLELISM parameter
        List<List<StreamProcessor>> pipelines = new ArrayList<>();
        for (int i = 0; i < Config.PARALLELISM; i++){
            List<StreamProcessor> p = Parser.parsePipeline(i);
            pipelines.add(p);
        }

        // create a list of processors for each task manager
        int tm_num = 2;
        List<List<StreamProcessor>> tmProcessors = new ArrayList<>();
        for (int i = 0; i < tm_num; i++) {
            tmProcessors.add(new ArrayList<>());
        }

        // round robin assignment of operators to task managers
        int tm_index = 0;
        for(int i = 0; i < pipelines.get(0).size(); i++) {
            for(int j = 0; j < pipelines.size(); j++) {

                StreamProcessor p = pipelines.get(j).get(i);

                System.out.println("JobManager : assigning to TaskManager " + tm_index + " processor " + p);

                tmProcessors.get(tm_index).add(p);

                tm_index = (tm_index + 1) % tm_num;
            }
        }

        for (int i = 0; i < tm_num; i++) {
            TaskManager tm = new TaskManager(i, tmProcessors.get(i));
            taskManagers.add(tm);
            tm.createThreads();
        }
    }

    public void start() {
        for (TaskManager tm : taskManagers)
            tm.start();
    }

}
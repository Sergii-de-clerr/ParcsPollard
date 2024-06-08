import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import parcs.*;

public class PollardHost {
    private static final int WORKERS_NUM = 4;
    private static final int NUM_OF_TASKS = 1000000;

    public static void main(String[] args) throws Exception {
        task curTask = new task();
        curTask.addJarFile("PollardTask.jar");

        int numWorkers = WORKERS_NUM;

        AMInfo info = new AMInfo(curTask, null);

        point[] points = new point[numWorkers];
        channel[] channels = new channel[numWorkers];

        for (int curWIndex = 0; curWIndex < numWorkers; curWIndex++) {
            points[curWIndex] = info.createPoint();
            channels[curWIndex] = points[curWIndex].createChannel();
            points[curWIndex].execute("PollardTask.jar");

            System.out.println("Worker " + curWIndex + " will test numbers from " + (curWIndex + 1) + " every " + numWorkers + " to " + NUM_OF_TASKS + ".");
            ArrayList<Integer> data = new ArrayList<>();
            data.add(curWIndex);
            data.add(numWorkers);
            data.add(NUM_OF_TASKS);
            channels[curWIndex].write(data);
        }

        StringBuilder finalResult = new StringBuilder();

        for (int i = 0; i < numWorkers; i++) {
            System.out.println("Waiting for result from worker " + (i + 1) + "...");
            finalResult.append((StringBuilder) channels[i].readObject() + "\n\n\n");
        }

        System.out.print(finalResult);
        System.out.println("Task completed");
        curTask.end();
    }
}

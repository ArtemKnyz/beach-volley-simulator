package org.beachvolley;

import org.beachvolley.runner.ConsoleRunner;
import org.beachvolley.service.InputService;
import org.beachvolley.service.OutputService;

public class Main {

    public static void main(String[] args) {

        ConsoleRunner runner = new ConsoleRunner(
                new InputService(),
                new OutputService()
        );

        runner.run();
    }
}
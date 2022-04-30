package ru.itmo.multithread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsServiceExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        NumberCounter numberCounter = new NumberCounter(0);

        int N = 5;
        for (int i = 0; i < N; i++) {
            executorService.execute(numberCounter::increment);
        }

        executorService.shutdown();
        System.out.println(numberCounter.getValue());
    }
}

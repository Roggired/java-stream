package ru.itmo.multithread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Let's find sum of N numbers by two ways:
 * 1. Single thread
 * 2. ForkJoinPool
 *
 * And calculate execution time for each way
 */
public class ForkJoinPoolExample {
    public static void main(String[] args) {
        int N = 1000000000;
        int[] numbers = produceNumbers(N);
        long timeBefore = System.nanoTime();
        long sum = singleThreadSum(numbers);
        long timeAfter = System.nanoTime();

        System.out.println("Single thread sum: " + sum);
        System.out.printf("Single thread execution time: %.3fms", (0.0 + timeAfter - timeBefore) / 1000000.0);
        System.out.println();

        for (int numberOfThreads = 5; numberOfThreads <= 50; numberOfThreads+=5) {
            ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
            timeBefore = System.nanoTime();
            sum = forkJoinPoolSum(numbers, forkJoinPool);
            timeAfter = System.nanoTime();

            System.out.println("ForkJoinPool(threads amount - " + numberOfThreads + ") sum: " + sum);
            System.out.printf("ForkJoinPool(threads amount - %d) execution time: %.3fms", numberOfThreads, (0.0 + timeAfter - timeBefore) / 1000000.0);
            System.out.println();
        }
    }

    private static int[] produceNumbers(int N) {
        int[] numbers = new int[N];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = (i + 1);
        }

        return numbers;
    }

    private static long singleThreadSum(int[] numbers) {
        long sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }

    private static long forkJoinPoolSum(int[] numbers, ForkJoinPool forkJoinPool) {
        return forkJoinPool.invoke(
                new SumTask(
                        0,
                        numbers.length - 1,
                        numbers
                )
        );
    }
}

class SumTask extends RecursiveTask<Long> {
    private static final int MAX_TASK_SIZE = 1000000;
    private int left;
    private int right;
    private int[] numbers;

    public SumTask(int left, int right, int[] numbers) {
        this.left = left;
        this.right = right;
        this.numbers = numbers;
    }

    @Override
    protected Long compute() {
        if (left > right) {
            return 0L;
        }

        if (left == right) {
            return (long) numbers[left];
        }

        SumTask sumTask = null;
        if (right - left > MAX_TASK_SIZE) {
            sumTask = new SumTask(
                    left + MAX_TASK_SIZE,
                    right,
                    numbers
            );
            right = left + MAX_TASK_SIZE - 1;
            getPool().submit(sumTask);
        }

        long partialSum = 0;
        for (int i = left; i <= right; i++) {
            partialSum += numbers[i];
        }

        if (sumTask != null) {
            partialSum += sumTask.join();
        }

        return partialSum;
    }
}
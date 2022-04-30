package ru.itmo.multithread;

public class SynchronizedExample {
    public static void main(String[] args) throws InterruptedException {
        NumberCounter numberCounter = new NumberCounter(0);

        int N = 5;
        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new Thread(() -> {
                numberCounter.increment();
                numberCounter.increment();
            });
            threads[i].start();
        }

        for (int i = 0; i < N; i++) {
            threads[i].join();
        }

        System.out.println(numberCounter.getValue());
    }
}

class NumberCounter {
    private int number;

    public NumberCounter(int initialNumber) {
        this.number = initialNumber;
    }

    public synchronized void increment() {
        number++;
    }

    public int getValue() {
        return number;
    }
}

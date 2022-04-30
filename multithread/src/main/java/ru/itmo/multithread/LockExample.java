package ru.itmo.multithread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockExample {
    public static void main(String[] args) throws InterruptedException {
        NumberHolder numberHolder = new NumberHolder(0);
        Lock lock = new ReentrantLock();

        int N = 5;
        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new Thread(() -> {
                lock.lock();
                numberHolder.setNumber(numberHolder.getNumber() + 2);
                lock.unlock();
            });
            threads[i].start();
        }

        for (int i = 0; i < N; i++) {
            threads[i].join();
        }

        System.out.println(numberHolder.getNumber());
    }
}

class NumberHolder {
    private int number;

    public NumberHolder(int initialNumber) {
        this.number = initialNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

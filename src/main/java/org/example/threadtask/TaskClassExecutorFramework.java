package org.example.threadtask;

public class TaskClassExecutorFramework implements Runnable{

    @Override
    public void run() {
        System.out.println("Hi Executor Service::"+Thread.currentThread());
    }
}
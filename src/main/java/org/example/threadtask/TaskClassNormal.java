package org.example.threadtask;

public class TaskClassNormal  implements Runnable{

    @Override
    public void run() {
        System.out.println("Hi::"+Thread.currentThread());
    }
}

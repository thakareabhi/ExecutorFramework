package org.example;

import org.example.exception.CustomRejectorHandler;
import org.example.threadtask.CallableTask;
import org.example.threadtask.TaskClassExecutorFramework;
import org.example.threadtask.TaskClassNormal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {


        System.out.println("Normal Class Threads Running in Parallel");

        for(int i=0;i<10;i++){
            Thread thread=new Thread(new TaskClassNormal());
            thread.start();
        }

        System.out.println("Executor Class Threads Running in Parallel");

        ExecutorService exsr= Executors.newFixedThreadPool(3);
        for(int i=0;i<15;i++){
            exsr.execute(new TaskClassExecutorFramework());
        }

        System.out.println("CachedExecutor Class Threads Running in Parallel");

        ExecutorService excsr= Executors.newCachedThreadPool();
        for(int i=0;i<100;i++){
            excsr.execute(new TaskClassExecutorFramework());
        }

        System.out.println("Scheduled Class Threads Running in Parallel");
        ScheduledExecutorService exsrsch= Executors.newScheduledThreadPool(10);
        exsrsch.schedule(new TaskClassExecutorFramework(),10, TimeUnit.SECONDS);
        exsrsch.scheduleAtFixedRate(new TaskClassExecutorFramework(),15,10,TimeUnit.SECONDS);
        exsrsch.scheduleWithFixedDelay(new TaskClassExecutorFramework(),15,10,TimeUnit.SECONDS);

        System.out.println("Single Executor Class Threads Running in Parallel");

        ExecutorService exsrsng= Executors.newFixedThreadPool(1);
        for(int i=0;i<15;i++){
            exsrsng.execute(new TaskClassExecutorFramework());
        }

        System.out.println("Exception Example");

        ExecutorService exceptionEg= new ThreadPoolExecutor(1,10,10,TimeUnit.SECONDS,new ArrayBlockingQueue<>(20));
        try{
            exceptionEg.execute(new TaskClassExecutorFramework());
        }catch (RejectedExecutionException ex){
            System.err.println(ex.getMessage());
        }

        ExecutorService exceptionEgCustom= new ThreadPoolExecutor(1,10,10,TimeUnit.SECONDS,new ArrayBlockingQueue<>(20),new CustomRejectorHandler());

        //initiate shutdown (if new task paased exception)
        exceptionEg.shutdown();

        //If isShutdown executed
        exceptionEg.isShutdown();

        //if all completed including queued
        exceptionEg.isTerminated();

        //block till all task completed or timeout occurs
        exceptionEg.awaitTermination(10,TimeUnit.SECONDS);

        //Will intiate shutdown and return all queued tasks
        List<Runnable> listPending=exceptionEg.shutdownNow();

        //Callable
        ExecutorService serCallable=Executors.newFixedThreadPool(10);
        List<Future> lFuture=new ArrayList<>();

        for(int i=0;i<100;i++){
            Future<Integer> future=serCallable.submit(new CallableTask());
            lFuture.add(future);
        }

        for(int i=0;i<100;i++){
            Future<Integer> f=lFuture.get(i);
            try{
                Integer newVar=f.get();
                System.out.println("Exception Example");
            }catch(InterruptedException | ExecutionException ex){
                ex.printStackTrace();
            }
        }

        List<Future> futList= new ArrayList<>();
        ExecutorService executorServiceCallable=Executors.newFixedThreadPool(10);

        for(int i=0;i<100;i++){
            Future<Integer> future=executorServiceCallable.submit(new CallableTask());
            futList.add(future);
        }

        futList.forEach(
                f-> {
                    try {
                        System.out.println(f.get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
        );




        //Cancel the task ()
        //future.cancel(mayInterruptifRunning:true/false);

        //If cancelled
        //future.isCancelled();

        //If Completed(successfully or even not)
        //future.isDone();
    }
}
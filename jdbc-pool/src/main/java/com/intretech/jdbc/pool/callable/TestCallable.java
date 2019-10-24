package com.intretech.jdbc.pool.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TestCallable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> callable=new Callable<String>() {
            public String call() throws Exception {
                return Thread.currentThread().getName()+"被执行";
            }
        };
        List<FutureTask<String>> futureTasks=new ArrayList<FutureTask<String>>();
        for (int i=0;i<10;i++){
            FutureTask<String> futureTask=new FutureTask<String>(callable);
            futureTasks.add(futureTask);
            Thread t=new Thread(futureTask);
            t.start();
        }
        for (FutureTask<String> e:futureTasks){
            System.out.println(e.get());
        }
    }
}

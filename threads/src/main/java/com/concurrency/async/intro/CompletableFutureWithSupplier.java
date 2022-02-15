package com.concurrency.async.intro;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class CompletableFutureWithSupplier {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return Thread.currentThread().getName();
        };

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

        // completableFuture.complete("Too long!");

        String string = completableFuture.join(); // this is blocking
        System.out.println("Result = " + string);

        // completableFuture.complete("Too long!");
        completableFuture.obtrudeValue("Too long!");

        string = completableFuture.join();
        System.out.println("Result = " + string);

        executor.shutdown();
    }
}

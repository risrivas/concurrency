package com.concurrency.async.trigger;

import com.concurrency.async.trigger.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class AsyncExample {

    public static void main(String[] args) {

        ExecutorService executor1 = Executors.newSingleThreadExecutor();
//        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        ExecutorService executor2 = Executors.newFixedThreadPool(2);

        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
            sleep(300);
            System.out.println("Function is running in " + Thread.currentThread().getName());
            Supplier<List<User>> userSupplier = () -> {
                System.out.println("Supplier is running in " + Thread.currentThread().getName());
                return ids.stream().map(User::new).collect(Collectors.toList());
            };
//            return CompletableFuture.supplyAsync(userSupplier);
            return CompletableFuture.supplyAsync(userSupplier, executor2);
        };

        Consumer<List<User>> displayer = users -> {
            System.out.println("Consumer is running in " + Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);
        //completableFuture.thenApply(fetchUsers)
//        completableFuture.thenCompose(fetchUsers)
        completableFuture.thenComposeAsync(fetchUsers, executor2)
//                .thenAccept(displayer);
                .thenAcceptAsync(displayer, executor1);

        sleep(1_000); // without sleep, the code will not print anything
        executor1.shutdown();
        executor2.shutdown();
    }

    private static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }
}

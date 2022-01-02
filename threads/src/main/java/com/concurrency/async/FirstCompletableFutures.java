package com.concurrency.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstCompletableFutures {

	public static void main(String[] args) throws InterruptedException {

		ExecutorService service = Executors.newSingleThreadExecutor();
		
		Runnable task = () -> {
			System.out.println("I am running asynchronously in the thread => " + Thread.currentThread().getName());
		};

		// use default fork-join pool
		// CompletableFuture.runAsync(task);
		// use executor service
		CompletableFuture.runAsync(task, service);

		// Thread.sleep(100);
		service.shutdown();
	}
}

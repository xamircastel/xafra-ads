package com.commons.help.job.concurrency;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public final class ConcurrencyUtils {

	private static final Logger Log = LoggerFactory.getLogger(ConcurrencyUtils.class);
	private static final ExecutorService globalService = getSimpleServiceWithScaleFactor(10);

	public static final ExecutorService getGlobalService() {
		return globalService;
	}

	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long period, TimeUnit timeUnit) {
		ScheduledExecutorService scheduledService = getScheduledService(1);
		return scheduledService.scheduleAtFixedRate(runnable, 0L, period, timeUnit);
	}

	public static ListeningExecutorService getListeningExecutorService(int poolSize) {
		return MoreExecutors.listeningDecorator(getSimpleService(poolSize));
	}

	public static ScheduledExecutorService getScheduledService(int poolSize) {
		ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(poolSize);
		addShutdownHook(scheduledService);
		return scheduledService;
	}

	public static final ExecutorService getSimpleService() {
		return getSimpleService(getThreadsCountToExecutor(1));
	}

	public static final <T> ExecutorService getSimpleServiceWithScaleFactor(int scaleFactor) {
		return getSimpleService(getThreadsCountToExecutor(scaleFactor));
	}

	public static final int getThreadsCountToExecutor(int scaleFactor) {
		int cpus = Runtime.getRuntime().availableProcessors();
		int maxThreads = cpus * scaleFactor;
		maxThreads = (maxThreads > 0) ? maxThreads : 1;
		return maxThreads;
	}

	public static final ExecutorService getSimpleService(int poolSize) {
		ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
		addShutdownHook(executorService);
		return executorService;
	}

	public static <E, R> void addShutdownHook(final Function<E, R> function, final E entryValue) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				function.apply(entryValue);
			}
		});
	}

	private static void addShutdownHook(ExecutorService executorService) {
		addShutdownHook(new Function<ExecutorService, Void>() {
			public Void apply(ExecutorService input) {
				ConcurrencyUtils.shutdown(input);
				return null;
			}
		}, executorService);
	}

	public static void shutdown(ExecutorService executorService) {
		executorService.shutdown();
		try {
			int timeToWait = 30;
			if (!executorService.awaitTermination(timeToWait, TimeUnit.SECONDS)) {
				List<Runnable> executionList = executorService.shutdownNow();
				for (Runnable runnable : executionList)
					Log.info("Trying to shutdown task: " + runnable, new Object[0]);
			}
			if (!executorService.awaitTermination(timeToWait, TimeUnit.SECONDS))
				;
		} catch (InterruptedException ex) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	public static <E, R> List<R> mapReduce(List<? extends AbstractCallable<R>> processingList) {
		return mapReduce(processingList.size(), processingList);
	}

	public static <E, R> List<R> mapReduce(int poolSize, List<? extends AbstractCallable<R>> processingList) {
		List<R> returnTypeList = Lists.newArrayList();
		ExecutorService threadPool = Executors.newFixedThreadPool(poolSize);
		ExecutorCompletionService<R> completionService = new ExecutorCompletionService<>(threadPool);
		for (Callable<R> callable : processingList)
			completionService.submit(callable);
		for (int i = 1; i <= processingList.size(); i++) {
			try {
				returnTypeList.add(completionService.take().get());
			} catch (Exception e) {
				Log.error("", e);
			}
		}
		threadPool.shutdown();
		return returnTypeList;
	}
}
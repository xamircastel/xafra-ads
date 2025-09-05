package com.commons.help.job.concurrency;

import java.util.concurrent.Callable;

public abstract class AbstractCallable<T> implements Callable<T> {

	public T call() {
		T value = null;
		try {
			value = process();
		} catch (Exception e) {
			Thread thread = Thread.currentThread();
			thread.getUncaughtExceptionHandler().uncaughtException(thread, e);
		}
		return value;
	}

	protected abstract T process();
}

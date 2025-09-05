package com.develop.job.jdbi;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseJbdiDao<B> {

	@Autowired
	private Jdbi jdbi;
	
	private Class<B> binderClazz;
	
	public BaseJbdiDao( Class<B> clazz) {
		this.binderClazz = clazz;
	}
		
	protected  B onDemand() {
		return jdbi.onDemand(binderClazz);
	}
	
	protected B handle() {
		return jdbi.open().attach(binderClazz);
	}
	
	protected  <R, X extends Exception> R handler(HandleCallback<R,X> callback) throws X {
		return jdbi.withHandle(callback);			
	}
	
	protected B binder(Handle handle) {
		return handle.attach(binderClazz);
	}
	
	protected Jdbi JDBI() {
		return jdbi;
	}
	
	protected <X extends Exception> void useHandle(HandleConsumer<X> consumer) throws X {
		jdbi.useHandle(consumer);
	}
}

package com.develop.job.tools;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

import com.google.common.base.Stopwatch;

@Component
public abstract class AbstractProcess<T> {

	protected Logger log = LoggerFactory.getLogger(getLogName());

	protected String uuid;

	protected Stopwatch stopwatch;

	private T event = null;

	public abstract void process(T event);

	protected abstract Class<?> getLogName();

	protected abstract String getLogNameFile();

	public String getUUID() {
		return this.uuid;
	}

	public void setUUid(String uuid) {
		this.uuid = uuid;
	}

	private void init() {
		if (uuid == null || "".equals(uuid) || uuid.isEmpty())
			uuid = Utils.getUUID();
		if (stopwatch == null)
			stopwatch = Stopwatch.createStarted();
	}

	private void initt() {
		uuid = Utils.getUUID();
		stopwatch = Stopwatch.createStarted();
	}

	protected void logStart(String maker, Object... arg1) {
		initt();
		log.info(getCurrentCallLine() + "[{}]|START|" + formatLogger(maker, arg1), uuid);
	}

	protected void logEnd(String maker, Object... arg1) {
		if (stopwatch != null) {
			log.info(getCurrentCallLine() + "[{}]|END|" + formatLogger(maker, arg1) + "|timeTaken {} ms", uuid,
					String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
		} else
			log.info(getCurrentCallLine() + "[{}]|END|" + formatLogger(maker, arg1), uuid);
	}

	protected void logEnd(String maker) {
		if (stopwatch != null) {
			log.info(getCurrentCallLine() + "[{}]|END|" + maker + "| timeTaken {} ms", uuid,
					String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
		} else
			log.info(getCurrentCallLine() + "[{}]|END|" + maker, uuid);
	}

	protected void logEnd() {
		if (stopwatch != null) {
			log.info(getCurrentCallLine() + "[{}]|END| timeTaken {} ms", uuid,
					String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
		} else
			log.info(getCurrentCallLine() + "[{}]|END|", uuid);
	}

	protected void logInfo(String maker, Object argums, Object arg2) {
		init();
		log.info(getCurrentCallLine() + "[{}] " + maker, uuid, argums, arg2);
	}

	protected void logInfo(Object arg1) {
		init();
		log.info(getCurrentCallLine() + "[{}] {}", uuid, arg1);
	}

	protected void logInfo(String format, Object arg1) {
		init();
		log.info(getCurrentCallLine() + "[{}] " + format, uuid, arg1);
	}

	protected void logInfo(String format, Object... arg1) {
		init();
		log.info(getCurrentCallLine() + "[{}] " + formatLogger(format, arg1), uuid);
	}

	protected void logError(Throwable t) {
		init();
		log.error(getCurrentCallLine() + "[{}] ", uuid, t);
	}

	protected void logError(String format, Object... t) {
		init();
		if (loggerThrowable(format, t) != null)
			log.error(getCurrentCallLine() + "[{}] " + formatLogger(format, t), uuid, loggerThrowable(format, t));
		else
			log.error(getCurrentCallLine() + "[{}] " + formatLogger(format, t), uuid);
	}

	protected void logError(String message) {
		init();
		log.error(getCurrentCallLine() + "[{}] {}", uuid, message);
	}

	protected void logError(String format, Throwable e) {
		init();
		log.error(getCurrentCallLine() + "[{}] {}", uuid, format, e);
	}

	protected void logDebug(String format, Object arg1) {
		init();
		log.debug(getCurrentCallLine() + "[{}] " + format, uuid, arg1);
	}

	protected void logDebug(String format, Object... arg1) {
		init();
		log.debug(getCurrentCallLine() + "[{}] " + formatLogger(format, arg1), uuid);
	}

	private String formatLogger(String messagePattern, Object... argArray) {
		FormattingTuple message = MessageFormatter.arrayFormat(messagePattern, argArray);
		return message.getMessage();
	}

	private Throwable loggerThrowable(String messagePattern, Object... argArray) {
		FormattingTuple message = MessageFormatter.arrayFormat(messagePattern, argArray);
		return message.getThrowable();
	}

	protected void getGenerateUUID() {
		this.uuid = Utils.getUUID();
	}

	public T getEvent() {
		return event;
	}

	public void setEvent(T event) {
		this.event = event;
	}

	@PreDestroy
	public void springPreDestroy() {
		LogManager.setLogName(getLogNameFile());
		log.info("destroy the inject {}, {}", getLogName(), new Date());
	}

	protected String getCurrentCallLine() {
		LogManager.setLogName(getLogNameFile());
		return getCurrentCall(3);
	}

	protected static String getCurrentCall(int deepLevel) {

		final StringBuilder stringBuilder = new StringBuilder();

		final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		if (stackTrace.length > deepLevel) {
			final StackTraceElement element = stackTrace[deepLevel];
			stringBuilder.append("(" + element.getLineNumber() + ")");
		}

		return stringBuilder.toString();
	}
}

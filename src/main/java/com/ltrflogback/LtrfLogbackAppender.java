package com.ltrflogback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class LtrfLogbackAppender<E> extends AppenderBase<E>{

	@Override
	protected void append(E eventObject) {
		LoggingEvent le = (LoggingEvent) eventObject;
		if(le.getThrowableProxy() != null)
			System.err.println(le.getThrowableProxy().getStackTraceElementProxyArray());
		System.out.println(le.getMessage());
	}
}

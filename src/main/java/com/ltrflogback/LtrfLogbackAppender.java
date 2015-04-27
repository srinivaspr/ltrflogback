package com.ltrflogback;

import com.ltrf.LogClient;
import com.ltrf.LogMessage;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;

public class LtrfLogbackAppender<E> extends AppenderBase<E>{

	private String appName;
	private String keys;
	private String additionalInfo;
	private String uri;
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
		LogClient.setURI(uri);
	}

	private static String[] keyStrings;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getKeys() {
		return keys;
	}
	
	public String[] getKeyStrings() {
		return keyStrings;
	}

	public static void setKeyStrings(String[] keyString) {
		keyStrings = keyString;
	}

	public void setKeys(String keys) {
		this.keys = keys;
		if(keys != null)
			setKeyStrings(keys.split(","));
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	@Override
	protected void append(E eventObject) {
		if(keyStrings == null || keyStrings.length == 0)
			return;
		LoggingEvent le = (LoggingEvent) eventObject;
		StringBuilder message = new StringBuilder();
		message.append(le.getMessage()+" ");	
		if(le.getThrowableProxy() != null)
		{
			StackTraceElementProxy[] elements = le.getThrowableProxy().getStackTraceElementProxyArray();
			for(StackTraceElementProxy element : elements)
			{
				message.append(element.getSTEAsString());
				message.append(System.lineSeparator());
			}
		}
		
		String key = trackable(message.toString());
		if(key != null )
		{
			trackMessage(key,message.toString());
		}
	}
	
	private void trackMessage(String key, String message) {
		try{
			LogMessage logMessage = new LogMessage();
			logMessage.setAppName(appName);
			logMessage.setKey(key);
			logMessage.setAdditionalDetails(additionalInfo);
			logMessage.setMessage(message);
			LogClient.trackMessage(logMessage);
		}catch(Exception e)
		{
			System.out.println(e);
		}		
	}

	private String trackable(String message) {
		for(String key : keyStrings)
		{
			if(message.contains(key))
			{
				return key;
			}
				
		}
		return null;
	}
}

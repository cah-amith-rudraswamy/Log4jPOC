package com.sumojsonpackage;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomLogEvent implements LogEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final LogEvent delegate;

	CustomLogEvent(LogEvent delegate) {
		this.delegate = delegate;
	}

	@Override
	@JsonIgnore
	public Map<String, String> getContextMap() {
		return delegate.getContextMap();
	}

	@JsonProperty("customMap")
	public Map<String, String> getCustomMap() {
		final Map<String, String> map = getContextMap();
		return map.isEmpty() ? null : map;
	}

	@Override
	public ThreadContext.ContextStack getContextStack() {
		return delegate.getContextStack();
	}

	@Override
	public String getLoggerFqcn() {
		return delegate.getLoggerFqcn();
	}

	@Override
	public Level getLevel() {
		return delegate.getLevel();
	}

	@Override
	public String getLoggerName() {
		return delegate.getLoggerName();
	}

	@Override
	public Marker getMarker() {
		return delegate.getMarker();
	}

	@Override
	public Message getMessage() {
		return delegate.getMessage();
	}

	@Override
	// See
	// https://help.sumologic.com/Send_Data/Sources/04Reference_Information_for_Sources/Timestamps,_Time_Zones,_Time_Ranges,_and_Date_Formats#section_7
	@JsonProperty("timestamp")
	public long getTimeMillis() {
		return delegate.getTimeMillis();
	}

	@Override
	public StackTraceElement getSource() {
		return delegate.getSource();
	}

	@Override
	public String getThreadName() {
		return delegate.getThreadName();
	}

	@Override
	public Throwable getThrown() {
		return delegate.getThrown();
	}

	@Override
	public ThrowableProxy getThrownProxy() {
		return delegate.getThrownProxy();
	}

	@Override
	public boolean isEndOfBatch() {
		return delegate.isEndOfBatch();
	}

	@Override
	public boolean isIncludeLocation() {
		return delegate.isIncludeLocation();
	}

	@Override
	public void setEndOfBatch(boolean endOfBatch) {
		delegate.setEndOfBatch(endOfBatch);
	}

	@Override
	public void setIncludeLocation(boolean locationRequired) {
		delegate.setIncludeLocation(locationRequired);
	}

	@Override
	public long getNanoTime() {
		return delegate.getNanoTime();
	}

	@Override
	public LogEvent toImmutable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReadOnlyStringMap getContextData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instant getInstant() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getThreadId() {
		// TODO Auto-generated method stub
		return delegate.getThreadId();
	}

	@Override
	public int getThreadPriority() {
		// TODO Auto-generated method stub
		return delegate.getThreadPriority();
	}

}

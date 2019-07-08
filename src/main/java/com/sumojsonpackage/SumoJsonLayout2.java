package com.sumojsonpackage;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.JsonConstants;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
import org.apache.logging.log4j.core.jackson.XmlConstants;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.layout.AbstractStringLayout.Serializer;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.core.util.StringBuilderWriter;
import org.apache.logging.log4j.util.Strings;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Plugin(name = "SumoJsonLayout2", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class SumoJsonLayout2 extends AbstractStringLayout {

	private static  String DEFAULT_FOOTER = "]";

	private static  String DEFAULT_HEADER = "[";

	protected static  String DEFAULT_EOL = "\r\n";
	protected static  String COMPACT_EOL = Strings.EMPTY;

	private static String CONTENT_TYPE = "application/json; charset=" + StandardCharsets.UTF_8.displayName();

	private ObjectWriter objectWriter;

	@PluginFactory
	public static SumoJsonLayout2 createLayout(
			@PluginAttribute(value = "locationInfo", defaultBoolean = false) boolean locationInfo) {
		SimpleFilterProvider filters = new SimpleFilterProvider();
		Set<String> except = new HashSet<>();
		if (!locationInfo) {
			except.add(JsonConstants.ELT_SOURCE);
		}
		except.add("loggerFqcn");
		except.add("endOfBatch");
		except.add(JsonConstants.ELT_NANO_TIME);
		filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
		ObjectWriter writer = new Log4jJsonObjectMapper().writer(new MinimalPrettyPrinter());
		return new SumoJsonLayout2(writer.with(filters));
	}

	SumoJsonLayout2(ObjectWriter objectWriter) {
		super(StandardCharsets.UTF_8, null, null);
		this.objectWriter = objectWriter;
	}

	/**
	 * Formats a {@link org.apache.logging.log4j.core.LogEvent}.
	 *
	 * @param event The LogEvent.
	 *
	 * @return The JSON representation of the LogEvent.
	 */
	@Override
	public String toSerializable(LogEvent event) {
		StringBuilderWriter writer = new StringBuilderWriter();
		try {
			objectWriter.writeValue(writer, wrap(event));
			writer.write('\n');
			return writer.toString();
		} catch (IOException e) {
			LOGGER.error(e);
			return Strings.EMPTY;
		}
	}

	// Overridden in tests
	LogEvent wrap(LogEvent event) {
		return new CustomLogEvent(event);
	}

	@Override
	public String getContentType() {
		return CONTENT_TYPE;
	}

	public static class Builder<B extends Builder<B>> extends AbstractStringLayout.Builder<B>
			implements org.apache.logging.log4j.core.util.Builder<SumoJsonLayout2> {

		@PluginBuilderAttribute
		private boolean propertiesAsList;

		@PluginBuilderAttribute
		private boolean objectMessageAsJsonObject;

		@PluginElement("AdditionalField")
		private KeyValuePair[] additionalFields;

		@PluginBuilderAttribute
		private boolean eventEol;

		@PluginBuilderAttribute
		private boolean compact;

		@PluginBuilderAttribute
		private boolean complete;

		@PluginBuilderAttribute
		private boolean locationInfo;

		@PluginBuilderAttribute
		private boolean properties;

		@PluginBuilderAttribute
		private boolean includeStacktrace = true;

		@PluginBuilderAttribute
		private boolean stacktraceAsString = false;

		@PluginBuilderAttribute
		private boolean includeNullDelimiter = false;

		public Builder() {
			super();
			setCharset(StandardCharsets.UTF_8);
		}

		@Override
		public SumoJsonLayout2 build() {
			 boolean encodeThreadContextAsList = isProperties() && propertiesAsList;
			 String headerPattern = toStringOrNull(getHeader());
			 String footerPattern = toStringOrNull(getFooter());
			 
			 return null;
			
			/*
			 * Configuration config, ObjectWriter objectWriter, Charset charset, boolean
			 * compact, boolean complete, boolean eventEol, Serializer headerSerializer,
			 * Serializer footerSerializer, boolean includeNullDelimiter, KeyValuePair[]
			 * additionalFields)
			 */

			/*
			 * return new SumoJsonLayout(getConfiguration(), objectWriter, getCharset(),
			 * isCompact(), isComplete(), getEventEol(), isIncludeNullDelimiter(),
			 * getAdditionalFields(), null, null, null );
			 */
			/*
			 * return new SumoJsonLayout(getConfiguration(), objectWriter, isLocationInfo(),
			 * isProperties(), encodeThreadContextAsList, isComplete(), isCompact(),
			 * getEventEol(), headerPattern, footerPattern, getCharset(),
			 * isIncludeStacktrace(), isStacktraceAsString(), isIncludeNullDelimiter(),
			 * getAdditionalFields(), getObjectMessageAsJsonObject());
			 */
		}

		public boolean isPropertiesAsList() {
			return propertiesAsList;
		}

		public B setPropertiesAsList( boolean propertiesAsList) {
			this.propertiesAsList = propertiesAsList;
			return asBuilder();
		}

		public boolean getObjectMessageAsJsonObject() {
			return objectMessageAsJsonObject;
		}

		public B setObjectMessageAsJsonObject( boolean objectMessageAsJsonObject) {
			this.objectMessageAsJsonObject = objectMessageAsJsonObject;
			return asBuilder();
		}

		public KeyValuePair[] getAdditionalFields() {
			return additionalFields;
		}

		public B setAdditionalFields(KeyValuePair[] additionalFields) {
			this.additionalFields = additionalFields;
			return asBuilder();
		}

		protected String toStringOrNull( byte[] header) {
			return header == null ? null : new String(header, Charset.defaultCharset());
		}

		public boolean getEventEol() {
			return eventEol;
		}

		public boolean isCompact() {
			return compact;
		}

		public boolean isComplete() {
			return complete;
		}

		public boolean isLocationInfo() {
			return locationInfo;
		}

		public boolean isProperties() {
			return properties;
		}

		/**
		 * If "true", includes the stacktrace of any Throwable in the generated data,
		 * defaults to "true".
		 * 
		 * @return If "true", includes the stacktrace of any Throwable in the generated
		 *         data, defaults to "true".
		 */
		public boolean isIncludeStacktrace() {
			return includeStacktrace;
		}

		public boolean isStacktraceAsString() {
			return stacktraceAsString;
		}

		public boolean isIncludeNullDelimiter() {
			return includeNullDelimiter;
		}

		public B setEventEol( boolean eventEol) {
			this.eventEol = eventEol;
			return asBuilder();
		}

		public B setCompact( boolean compact) {
			this.compact = compact;
			return asBuilder();
		}

		public B setComplete( boolean complete) {
			this.complete = complete;
			return asBuilder();
		}

		public B setLocationInfo( boolean locationInfo) {
			this.locationInfo = locationInfo;
			return asBuilder();
		}

		public B setProperties( boolean properties) {
			this.properties = properties;
			return asBuilder();
		}

		/**
		 * If "true", includes the stacktrace of any Throwable in the generated JSON,
		 * defaults to "true".
		 * 
		 * @param includeStacktrace If "true", includes the stacktrace of any Throwable
		 *                          in the generated JSON, defaults to "true".
		 * @return this builder
		 */
		public B setIncludeStacktrace( boolean includeStacktrace) {
			this.includeStacktrace = includeStacktrace;
			return asBuilder();
		}

		/**
		 * Whether to format the stacktrace as a string, and not a nested object
		 * (optional, defaults to false).
		 *
		 * @return this builder
		 */
		public B setStacktraceAsString( boolean stacktraceAsString) {
			this.stacktraceAsString = stacktraceAsString;
			return asBuilder();
		}

		/**
		 * Whether to include NULL byte as delimiter after each event (optional, default
		 * to false).
		 *
		 * @return this builder
		 */
		public B setIncludeNullDelimiter( boolean includeNullDelimiter) {
			this.includeNullDelimiter = includeNullDelimiter;
			return asBuilder();
		}
	}

	protected String eol;
	// protected  ObjectWriter objectWriter;
	protected boolean compact;
	protected boolean complete;
	protected boolean includeNullDelimiter;
	protected ResolvableKeyValuePair[] additionalFields;

	public SumoJsonLayout2( Configuration config,  ObjectWriter objectWriter,  Charset charset,
			 boolean compact,  boolean complete,  boolean eventEol,  Serializer headerSerializer,
			 Serializer footerSerializer,  boolean includeNullDelimiter,
			 KeyValuePair[] additionalFields) {
		super(config, charset, headerSerializer, footerSerializer);
		this.objectWriter = objectWriter;
		this.compact = compact;
		this.complete = complete;
		this.eol = compact && !eventEol ? COMPACT_EOL : DEFAULT_EOL;
		this.includeNullDelimiter = includeNullDelimiter;
		this.additionalFields = prepareAdditionalFields(config, additionalFields);
	}

	@Override
	public byte[] getHeader() {
		if (!this.complete) {
			return null;
		}
		 StringBuilder buf = new StringBuilder();
		 String str = serializeToString(getHeaderSerializer());
		if (str != null) {
			buf.append(str);
		}
		buf.append(this.eol);
		return getBytes(buf.toString());
	}

	/**
	 * Returns appropriate JSON footer.
	 *
	 * @return a byte array containing the footer, closing the JSON array.
	 */
	@Override
	public byte[] getFooter() {
		if (!this.complete) {
			return null;
		}
		 StringBuilder buf = new StringBuilder();
		buf.append(this.eol);
		 String str = serializeToString(getFooterSerializer());
		if (str != null) {
			buf.append(str);
		}
		buf.append(this.eol);
		return getBytes(buf.toString());
	}

	@Override
	public Map<String, String> getContentFormat() {
		 Map<String, String> result = new HashMap<>();
		result.put("version", "2.0");
		return result;
	}

	@PluginBuilderFactory
	public static <B extends Builder<B>> B newBuilder() {
		return new Builder<B>().asBuilder();
	}

	/**
	 * Creates a JSON Layout using the default settings. Useful for testing.
	 *
	 * @return A JSON Layout.
	 */
	/*
	 * public static SumoJsonLayout createDefaultLayout() { return new
	 * SumoJsonLayout(new DefaultConfiguration(), false, false, false, false, false,
	 * false, DEFAULT_HEADER, DEFAULT_FOOTER, StandardCharsets.UTF_8, true, false,
	 * false, null, false); }
	 */

	/*
	 * public void toSerializable( LogEvent event,  Writer writer) throws
	 * IOException { if (complete && eventCount > 0) { writer.append(", "); }
	 * super.toSerializable(event, writer); }
	 */

	protected static boolean valueNeedsLookup( String value) {
		return value != null && value.contains("${");
	}

	private static ResolvableKeyValuePair[] prepareAdditionalFields( Configuration config,
			 KeyValuePair[] additionalFields) {
		if (additionalFields == null || additionalFields.length == 0) {
			// No fields set
			return new ResolvableKeyValuePair[0];
		}

		// Convert to specific class which already determines whether values needs
		// lookup during serialization
		 ResolvableKeyValuePair[] resolvableFields = new ResolvableKeyValuePair[additionalFields.length];

		for (int i = 0; i < additionalFields.length; i++) {
			ResolvableKeyValuePair resolvable = resolvableFields[i] = new ResolvableKeyValuePair(additionalFields[i]);

			// Validate
			if (config == null && resolvable.valueNeedsLookup) {
				throw new IllegalArgumentException(
						"configuration needs to be set when there are additional fields with variables");
			}
		}

		return resolvableFields;
	}

	/**
	 * Formats a {@link org.apache.logging.log4j.core.LogEvent}.
	 *
	 * @param event The LogEvent.
	 * @return The XML representation of the LogEvent.
	 */
	/*
	 * @Override public String toSerializable( LogEvent event) { 
	 * StringBuilderWriter writer = new StringBuilderWriter(); try {
	 * toSerializable(event, writer); return writer.toString(); } catch (
	 * IOException e) { // Should this be an ISE or IAE? LOGGER.error(e); return
	 * Strings.EMPTY; } }
	 */

	private static LogEvent convertMutableToLog4jEvent( LogEvent event) {
		// TODO Jackson-based layouts have certain filters set up for Log4jLogEvent.
		// TODO Need to set up the same filters for MutableLogEvent but don't know
		// how...
		// This is a workaround.
		return event instanceof Log4jLogEvent ? event : Log4jLogEvent.createMemento(event);
	}

	protected Object wrapLogEvent( LogEvent event) {
		if (additionalFields.length > 0) {
			// Construct map for serialization - note that we are intentionally using
			// original LogEvent
			Map<String, String> additionalFieldsMap = resolveAdditionalFields(event);
			// This class combines LogEvent with AdditionalFields during serialization
			return new LogEventWithAdditionalFields(event, additionalFieldsMap);
		} else {
			// No additional fields, return original object
			return event;
		}
	}

	private Map<String, String> resolveAdditionalFields(LogEvent logEvent) {
		// Note: LinkedHashMap retains order
		 Map<String, String> additionalFieldsMap = new LinkedHashMap<>(additionalFields.length);
		 StrSubstitutor strSubstitutor = configuration.getStrSubstitutor();

		// Go over each field
		for (ResolvableKeyValuePair pair : additionalFields) {
			if (pair.valueNeedsLookup) {
				// Resolve value
				additionalFieldsMap.put(pair.key, strSubstitutor.replace(logEvent, pair.value));
			} else {
				// Plain text value
				additionalFieldsMap.put(pair.key, pair.value);
			}
		}

		return additionalFieldsMap;
	}

	/*
	 * public void toSerializable( LogEvent event,  Writer writer) throws
	 * JsonGenerationException, JsonMappingException, IOException {
	 * objectWriter.writeValue(writer,
	 * wrapLogEvent(convertMutableToLog4jEvent(event))); writer.write(eol); if
	 * (includeNullDelimiter) { writer.write('\0'); } markEvent(); }
	 */

	@JsonRootName(XmlConstants.ELT_EVENT)
	// @JacksonXmlRootElement(namespace = XmlConstants.XML_NAMESPACE, localName =
	// XmlConstants.ELT_EVENT)
	public static class LogEventWithAdditionalFields {

		private  Object logEvent;
		private  Map<String, String> additionalFields;

		public LogEventWithAdditionalFields(Object logEvent, Map<String, String> additionalFields) {
			this.logEvent = logEvent;
			this.additionalFields = additionalFields;
		}

		@JsonUnwrapped
		public Object getLogEvent() {
			return logEvent;
		}

		@JsonAnyGetter
		@SuppressWarnings("unused")
		public Map<String, String> getAdditionalFields() {
			return additionalFields;
		}
	}

	protected static class ResolvableKeyValuePair {

		 String key;
		 String value;
		 boolean valueNeedsLookup;

		ResolvableKeyValuePair(KeyValuePair pair) {
			this.key = pair.getKey();
			this.value = pair.getValue();
			this.valueNeedsLookup = SumoJsonLayout2.valueNeedsLookup(this.value);
		}
	}

}

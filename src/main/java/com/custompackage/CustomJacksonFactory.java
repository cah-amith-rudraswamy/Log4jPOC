package com.custompackage;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.JsonConstants;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public abstract class CustomJacksonFactory {

	static class CustomJSON extends CustomJacksonFactory {

		private final boolean encodeThreadContextAsList;
		private final boolean includeStacktrace;
		private final boolean stacktraceAsString;
		private final boolean objectMessageAsJsonObject;

		public CustomJSON(final boolean encodeThreadContextAsList, final boolean includeStacktrace,
				final boolean stacktraceAsString, final boolean objectMessageAsJsonObject) {
			this.encodeThreadContextAsList = encodeThreadContextAsList;
			this.includeStacktrace = includeStacktrace;
			this.stacktraceAsString = stacktraceAsString;
			this.objectMessageAsJsonObject = objectMessageAsJsonObject;
		}

		protected String getPropertNameForContextMap() {
			return JsonConstants.ELT_CONTEXT_MAP;
		}

		protected String getPropertNameForSource() {
			return JsonConstants.ELT_SOURCE;
		}

		protected String getPropertNameForNanoTime() {
			return JsonConstants.ELT_NANO_TIME;
		}

		protected PrettyPrinter newCompactPrinter() {
			return new MinimalPrettyPrinter();
		}

		protected ObjectMapper newObjectMapper() {
			return new Log4jJsonObjectMapper(encodeThreadContextAsList, includeStacktrace, stacktraceAsString,
					objectMessageAsJsonObject);
		}

		protected PrettyPrinter newPrettyPrinter() {
			return new DefaultPrettyPrinter();
		}

	}

	abstract protected String getPropertNameForContextMap();

	abstract protected String getPropertNameForSource();

	abstract protected String getPropertNameForNanoTime();

	abstract protected PrettyPrinter newCompactPrinter();

	abstract protected ObjectMapper newObjectMapper();

	abstract protected PrettyPrinter newPrettyPrinter();

	ObjectWriter newWriter(final boolean locationInfo, final boolean properties, final boolean compact) {
		final SimpleFilterProvider filters = new SimpleFilterProvider();
		final Set<String> except = new HashSet<>(2);
		if (!locationInfo) {
			except.add(this.getPropertNameForSource());
		}
		if (!properties) {
			except.add(this.getPropertNameForContextMap());
		}
		except.add("loggerFqcn");
	    except.add("endOfBatch");
	    except.add("instant");
	    except.add("threadId");
	    except.add("threadPriority");
	    except.add(JsonConstants.ELT_NANO_TIME);
		except.add(this.getPropertNameForNanoTime());
		filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
		final ObjectWriter writer = this.newObjectMapper()
				.writer(compact ? this.newCompactPrinter() : this.newPrettyPrinter());
		return writer.with(filters);
	}

}

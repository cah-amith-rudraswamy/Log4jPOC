package com.sumojsonpackage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.JsonConstants;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.util.StringBuilderWriter;
import org.apache.logging.log4j.util.Strings;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Plugin(name = "CustomJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class CustomJsonLayout2 extends AbstractStringLayout {
	
	
	 private static final String CONTENT_TYPE = "application/json; charset=" + StandardCharsets.UTF_8.displayName();

	  private final ObjectWriter objectWriter;

	  @PluginFactory
	  public static CustomJsonLayout2 createLayout(
	      @PluginAttribute(value = "locationInfo", defaultBoolean = false) final boolean locationInfo
	  )
	  {
	    final SimpleFilterProvider filters = new SimpleFilterProvider();
	    final Set<String> except = new HashSet<>();
	    if (!locationInfo) {
	      except.add(JsonConstants.ELT_SOURCE);
	    }
	    except.add("loggerFqcn");
	    except.add("endOfBatch");
	    except.add(JsonConstants.ELT_NANO_TIME);
	    filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
	    final ObjectWriter writer = new Log4jJsonObjectMapper().writer(new MinimalPrettyPrinter());
	    return new CustomJsonLayout2(writer.with(filters));
	  }

	  CustomJsonLayout2(ObjectWriter objectWriter)
	  {
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
	  public String toSerializable(final LogEvent event)
	  {
	    final StringBuilderWriter writer = new StringBuilderWriter();
	    try {
	      objectWriter.writeValue(writer, wrap(event));
	      writer.write('\n');
	      return writer.toString();
	    }
	    catch (final IOException e) {
	      LOGGER.error(e);
	      return Strings.EMPTY;
	    }
	  }

	  // Overridden in tests
	  LogEvent wrap(LogEvent event)
	  {
	    return new CustomLogEvent(event);
	  }

	  @Override
	  public String getContentType()
	  {
	    return CONTENT_TYPE;
	  }
}

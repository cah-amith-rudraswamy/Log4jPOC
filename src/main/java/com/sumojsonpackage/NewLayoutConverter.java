package com.sumojsonpackage;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

public class NewLayoutConverter extends LogEventPatternConverter {

	protected NewLayoutConverter( String name, String style )
    {
        super( name, style );
    }

    @Override 
    public void format( LogEvent event, StringBuilder toAppendTo )
    {

    }

}

package pro.jiefzz.demo.ejoker.extension.log4j;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

import pro.jk.ejoker.common.system.wrapper.MittenWrapper;

@SuppressWarnings("deprecation")
@Plugin(name = "FiberNamePattern", category = PatternConverter.CATEGORY)
@ConverterKeys({"xf", "fibername"})
public class FiberNamePatternConverter extends LogEventPatternConverter {
	
	private static final FiberNamePatternConverter INSTANCE =
            new FiberNamePatternConverter();

    public static FiberNamePatternConverter newInstance(
            final String[] options) {
        return INSTANCE;
    }
    
	protected FiberNamePatternConverter() {
		super("fiberName", "fiberName");
	}

	@Override
	public void format(LogEvent event, StringBuilder toAppendTo) {
		toAppendTo.append(MittenWrapper.getName(MittenWrapper.current()));
	}

}

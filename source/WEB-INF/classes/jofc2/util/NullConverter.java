package jofc2.util;

import jofc2.model.elements.NullElement;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.json.JsonWriter;
import com.thoughtworks.xstream.io.path.PathTrackingWriter;

public class NullConverter extends ConverterBase<NullElement> {

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class type) {
		return NullElement.class.isAssignableFrom(type);
	}

	@Override
	public void convert(NullElement o, PathTrackingWriter writer, MarshallingContext mc) {
		((JsonWriter) writer.underlyingWriter()).setValue(null);
	}
}

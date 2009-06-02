package jofc2.model.elements;

import jofc2.model.metadata.Converter;
import jofc2.util.NullConverter;

@Converter(NullConverter.class)
public class NullElement {

	@Override
	public String toString() {
		return null;
	}
}

/*
This file is part of JOFC2.

JOFC2 is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as
published by the Free Software Foundation, either version 3 of
the License, or (at your option) any later version.

JOFC2 is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

See <http://www.gnu.org/licenses/lgpl-3.0.txt>.
 */
package jofc2.util;

import jofc2.model.elements.ScatterChart;
import jofc2.model.elements.ScatterChart.DotStyle;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.path.PathTrackingWriter;
import org.apache.log4j.Logger;

public class ScatterChartDotStyleConverter extends ConverterBase<DotStyle> {

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class c) {
	    Logger logger = Logger.getLogger(this.getClass().getName());
	    logger.debug("XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		return ScatterChart.DotStyle.class.isAssignableFrom(c);
	}

	@Override
	public void convert(DotStyle o, PathTrackingWriter writer, MarshallingContext mc) {
	    Logger logger = Logger.getLogger(this.getClass().getName());
		logger.debug("CONVERTCONVERTCONVERTCONVERTCONVERTCONVERTCONVERTCONVERTCONVERTCONVERT");
		writeNode(writer, "type", o.getType(), true);
		writeNode(writer, "alpha", o.getAlpha(), true);
		writeNode(writer, "hollow", o.getHollow(), true);
		writeNode(writer, "background-color", o.getBackgroundColor(), true);
		writeNode(writer, "background-alpha", o.getBackgroundAlpha(), true);
		writeNode(writer, "dotSize", o.getDotSize(), true);
		writeNode(writer, "tip", o.getTip(), true);
		writeNode(writer, "halo-size", o.getHaloSize(), true);
		writeNode(writer, "rotation", o.getRotation(), true);
		writeNode(writer, "sides", o.getSides(), true);
		writeNode(writer, "width", o.getWidth(), true);
		writeNode(writer, "on-click", o.getOnClick(), true);
	}


}
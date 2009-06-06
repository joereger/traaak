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
import jofc2.model.elements.ScatterChart.Point;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.path.PathTrackingWriter;
import com.fbdblog.util.Util;
import com.fbdblog.util.Num;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;

public class ScatterChartPointConverter extends ConverterBase<Point> {

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class c) {
	    Logger logger = Logger.getLogger(this.getClass().getName());
	    logger.debug("+++===+++===+++===+++=== canConvert() called");
		return ScatterChart.Point.class.isAssignableFrom(c);
	}

	@Override
	public void convert(Point o, PathTrackingWriter writer, MarshallingContext mc) {
	    Logger logger = Logger.getLogger(this.getClass().getName());
	    logger.debug("+++===+++===+++===+++=== o.getX()="+o.getX());

	    Object xOut = o.getX();
	    if (o.getX() instanceof Number){


	        String myFormat = ".000";
            DecimalFormat df = new DecimalFormat(myFormat);
            xOut = df.format(o.getX());
            logger.debug("+++===+++===+++===+++=== String.valueOf(o.getX())="+xOut);
        }



		writeNode(writer, "x", xOut, false);
		writeNode(writer, "y", o.getY(), false);
	}
}

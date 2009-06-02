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
package jofc2.model.elements;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import jofc2.model.metadata.Alias;
import jofc2.model.metadata.Converter;
import jofc2.util.ScatterChartPointConverter;
import jofc2.util.ScatterChartDotStyleConverter;

public class ScatterChart extends Element {

	private static final String TYPE = "scatter";
	private static final long serialVersionUID = 3029567780918048503L;
	private String colour;
	@Alias("dot-size")
	private Integer dotSize;
	private Integer width;
	@Alias("dot-style")
	private DotStyle dotStyle;

	public ScatterChart() {
		super(TYPE);
	}

    public ScatterChart(Style style){
        super(style.getStyle());
    }

	public ScatterChart addPoints(Point... points) {
		getValues().addAll(Arrays.asList(points));
		return this;
	}

	public ScatterChart addPoint(Number x, Number y) {
		return addPoints(new Point(x, y));
	}

	public ScatterChart addPoints(Collection<Point> points) {
		getValues().addAll(points);
		return this;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width=width;
    }

    public Integer getDotSize() {
		return dotSize;
	}

	public void setDotSize(Integer dotSize) {
		this.dotSize = dotSize;
	}

    public DotStyle getDotStyle() {
        return dotStyle;
    }

    public void setDotStyle(DotStyle dotStyle) {
        this.dotStyle=dotStyle;
    }

    @Converter(ScatterChartPointConverter.class)
	public static class Point {

		private Number x;
		private Number y;

		public Point(Number x, Number y) {
			this.x = x;
			this.y = y;
		}

		public Number getX() {
			return x;
		}

		public void setX(Number x) {
			this.x = x;
		}

		public Number getY() {
			return y;
		}

		public void setY(Number y) {
			this.y = y;
		}
	}

	@Converter(ScatterChartDotStyleConverter.class)
	public static class DotStyle {

		private String type;
		private Number alpha;
		private Boolean hollow;
		private String backgroundColor;
		private Number backgroundAlpha;
		@Alias("dot-size")
		private String dotSize;
		private String tip;
		private Number haloSize;
		private Number rotation;
		private Integer sides;
		private String width;
		private String onClick;

		public DotStyle() {

		}

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type=type;
        }

        public Number getAlpha() {
            return alpha;
        }

        public void setAlpha(Number alpha) {
            this.alpha=alpha;
        }

        public Boolean getHollow() {
            return hollow;
        }

        public void setHollow(Boolean hollow) {
            this.hollow=hollow;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor=backgroundColor;
        }

        public Number getBackgroundAlpha() {
            return backgroundAlpha;
        }

        public void setBackgroundAlpha(Number backgroundAlpha) {
            this.backgroundAlpha=backgroundAlpha;
        }

        public String getDotSize() {
            return dotSize;
        }

        public void setDotSize(String dotSize) {
            this.dotSize=dotSize;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip=tip;
        }

        public Number getHaloSize() {
            return haloSize;
        }

        public void setHaloSize(Number haloSize) {
            this.haloSize=haloSize;
        }

        public Number getRotation() {
            return rotation;
        }

        public void setRotation(Number rotation) {
            this.rotation=rotation;
        }

        public Integer getSides() {
            return sides;
        }

        public void setSides(Integer sides) {
            this.sides=sides;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width=width;
        }

        public String getOnClick() {
            return onClick;
        }

        public void setOnClick(String onClick) {
            this.onClick=onClick;
        }
    }

    public static enum Style {
		NORMAL("scatter"), LINE("scatter_line");

		private String style;

		Style(String style) {
			this.style = style;
		}

		public String getStyle() {
			return style;
		}
	}
}

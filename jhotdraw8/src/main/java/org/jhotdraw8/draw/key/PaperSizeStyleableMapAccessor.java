/* @(#)CssSize2DStyleableMapAccessor.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.key;

import java.util.Map;
import java.util.function.Function;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.collection.Key;
import org.jhotdraw8.collection.MapAccessor;
import org.jhotdraw8.css.CssPoint2D;
import org.jhotdraw8.css.CssSize;
import org.jhotdraw8.styleable.StyleablePropertyBean;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.text.Converter;
import org.jhotdraw8.css.text.CssPaperSizeConverter;
import org.jhotdraw8.text.StyleConverterAdapter;

/**
 * CssSize2DStyleableMapAccessor.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class PaperSizeStyleableMapAccessor extends AbstractStyleableFigureMapAccessor<CssPoint2D> {

    private final static long serialVersionUID = 1L;
    private Converter<CssPoint2D> converter;

    @Nonnull
    private final CssMetaData<?, CssPoint2D> cssMetaData;
    @Nonnull
    private final MapAccessor<CssSize> xKey;
    @Nonnull
    private final MapAccessor<CssSize> yKey;

    /**
     * Creates a new instance with the specified name.
     *
     * @param name the name of the accessor
     * @param xKey the key for the x coordinate of the point
     * @param yKey the key for the y coordinate of the point
     */
    public PaperSizeStyleableMapAccessor(String name, MapAccessor<CssSize> xKey, MapAccessor<CssSize> yKey) {
        super(name, CssPoint2D.class, new MapAccessor<?>[]{xKey, yKey}, new CssPoint2D(xKey.getDefaultValue(), yKey.getDefaultValue()));

        Function<Styleable, StyleableProperty<CssPoint2D>> function = s -> {
            StyleablePropertyBean spb = (StyleablePropertyBean) s;
            return spb.getStyleableProperty(this);
        };
        boolean inherits = false;
        String property = Figure.JHOTDRAW_CSS_PREFIX + getCssName();
        final StyleConverter<String, CssPoint2D> cnvrtr
                = new StyleConverterAdapter<>(getConverter());
        CssMetaData<Styleable, CssPoint2D> md
                = new SimpleCssMetaData<>(property, function,
                        cnvrtr, getDefaultValue(), inherits);
        cssMetaData = md;

        this.xKey = xKey;
        this.yKey = yKey;
    }
    @Nonnull
    @Override
    public CssPoint2D get(@Nonnull Map<? super Key<?>, Object> a) {
      return new CssPoint2D(xKey.get(a), yKey.get(a));
    }


    @Override
    public Converter<CssPoint2D> getConverter() {
        if (converter == null) {
            converter = new CssPaperSizeConverter();
        }
        return converter;
    }
    @Nonnull
    @Override
    public CssMetaData<?, CssPoint2D> getCssMetaData() {
      return cssMetaData;
      
    }
    @Override
    public boolean isNullable() {
      return false;
    }

    @Nonnull
    @Override
    public CssPoint2D put(@Nonnull Map<? super Key<?>, Object> a, @Nonnull CssPoint2D value) {
        CssPoint2D oldValue = get(a);
        xKey.put(a, value.getX());
        yKey.put(a, value.getY());
        return oldValue;
    }

    @Nonnull
    @Override
    public CssPoint2D remove(@Nonnull Map<? super Key<?>, Object> a) {
        CssPoint2D oldValue = get(a);
        xKey.remove(a);
        yKey.remove(a);
        return oldValue;
    }

}

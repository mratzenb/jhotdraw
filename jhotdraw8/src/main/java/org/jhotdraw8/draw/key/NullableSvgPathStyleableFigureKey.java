/* @(#)NullableSvgPathStyleableFigureKey.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.key;

import java.util.function.Function;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;

import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.styleable.StyleablePropertyBean;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.text.Converter;
import org.jhotdraw8.css.text.CssSvgPathConverter;
import org.jhotdraw8.text.StyleConverterAdapter;
import org.jhotdraw8.styleable.WriteableStyleableMapAccessor;

/**
 * NullableSvgPathStyleableFigureKey.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class NullableSvgPathStyleableFigureKey extends AbstractStyleableFigureKey<String> implements WriteableStyleableMapAccessor<String> {

    private final static long serialVersionUID = 1L;

    @Nonnull
    private final CssMetaData<?, String> cssMetaData;

    private final Converter<String> converter;

    /**
     * Creates a new instance with the specified name and with null as the
     * default value.
     *
     * @param name The name of the key.
     */
    public NullableSvgPathStyleableFigureKey(String name) {
        this(name, DirtyMask.of(DirtyBits.NODE), null);
    }

    /**
     * Creates a new instance with the specified name and default value.
     *
     * @param name The name of the key.
     * @param defaultValue The default value.
     */
    public NullableSvgPathStyleableFigureKey(String name, String defaultValue) {
        this(name, DirtyMask.of(DirtyBits.NODE), defaultValue);
    }

    /**
     * Creates a new instance with the specified name, type token class, default
     * value, and allowing or disallowing null values.
     *
     * @param key The name of the name. type parameters are given. Otherwise
     * specify them in arrow brackets.
     * @param mask Dirty bit mask.
     * @param defaultValue The default value.
     */
    public NullableSvgPathStyleableFigureKey(String key, DirtyMask mask, String defaultValue) {
        super(key, String.class, mask, defaultValue);

        Function<Styleable, StyleableProperty<String>> function = s -> {
            StyleablePropertyBean spb = (StyleablePropertyBean) s;
            return spb.getStyleableProperty(this);
        };

        converter = new CssSvgPathConverter(isNullable());
        boolean inherits = false;
        String property = Figure.JHOTDRAW_CSS_PREFIX + getCssName();
        final StyleConverter<String, String> converter
                = new StyleConverterAdapter<>(getConverter());
        CssMetaData<Styleable, String> md
                = new SimpleCssMetaData<>(property, function,
                converter, defaultValue, inherits);
        cssMetaData = md;
    }

    @Nonnull
    @Override
    public CssMetaData<?, String> getCssMetaData() {
        return cssMetaData;

    }

    @Override
    public Converter<String> getConverter() {
        return converter;
    }
}

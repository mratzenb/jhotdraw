/* @(#)FigureSelectorModelTest.java
 * Copyright (c) 2016 The authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw.css;

import javafx.css.StyleOrigin;
import org.jhotdraw8.collection.ImmutableList;
import org.jhotdraw8.css.CssToken;
import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.draw.figure.FillableFigure;
import org.jhotdraw8.draw.figure.SimpleLabelFigure;
import org.jhotdraw8.css.Paintable;
import org.jhotdraw8.draw.key.NullablePaintableStyleableFigureKey;
import org.jhotdraw8.text.Converter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * FigureSelectorModelTest.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class FigureSelectorModelTest {

    public FigureSelectorModelTest() {
    }

    /**
     * Test of getProperties method, of class SimplePropertyBean.
     */
    @Test
    public void testNullValueIsNotSameAsDefaultPropertyValue() {
        System.out.println("testNullValueIsNotSameAsDefaultPropertyValue");
        SimpleLabelFigure figure = new SimpleLabelFigure();
        FigureSelectorModel instance = new FigureSelectorModel();

        final NullablePaintableStyleableFigureKey key = FillableFigure.FILL;
        final String attrName = key.getCssName();
        final String namespace=key.getCssNamespace();
        final Converter<Paintable> converter = key.getConverter();


        assertNotNull(key.getDefaultValue(), "need a key with a non-null default value for this test");

        assertEquals("none", instance.getAttributeAsString(figure,namespace, attrName), "no value has been set, must be 'none'");

        instance.setAttribute(figure, StyleOrigin.USER, namespace,attrName, ImmutableList.of(new CssToken(CssTokenType.TT_IDENT, CssTokenType.IDENT_NONE)));

        assertNull(figure.get(key), "figure.get(key) value has been explicitly set to null");

        assertEquals(instance.getAttributeAsString(figure, namespace,attrName), converter.toString(null), "model.get(figure,key) value has been explicitly set to null");

    }

}

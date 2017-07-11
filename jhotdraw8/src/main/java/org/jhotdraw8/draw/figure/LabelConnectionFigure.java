/* @(#)LabelConnectionFigure.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw.figure;

import javafx.geometry.Point2D;
import org.jhotdraw8.collection.Key;
import static org.jhotdraw8.draw.figure.AbstractLabelFigure.ORIGIN;
import static org.jhotdraw8.draw.figure.TextableFigure.TEXT;
import org.jhotdraw8.draw.render.RenderContext;

/**
 * LabelConnectionFigure.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class LabelConnectionFigure extends AbstractLabelConnectionFigure
        implements HideableFigure, FontableFigure, TextableFigure, StyleableFigure, LockableFigure, TransformableFigure, CompositableFigure {

    /**
     * The CSS type selector for a label object is {@value #TYPE_SELECTOR}.
     */
    public final static String TYPE_SELECTOR = "LabelConnection";

    public LabelConnectionFigure(Point2D position, String text) {
        this(position.getX(), position.getY(), text);
    }

    public LabelConnectionFigure(double x, double y, String text, Object... keyValues) {
        set(TEXT, text);
        set(ORIGIN, new Point2D(x, y));
        for (int i = 0; i < keyValues.length; i += 2) {
            @SuppressWarnings("unchecked") // the set() method will perform the check for us
            Key<Object> key = (Key<Object>) keyValues[i];
            set(key, keyValues[i + 1]);
        }
    }

    @Override
    protected String getText(RenderContext ctx) {
        return get(TEXT);
    }

    @Override
    public String getTypeSelector() {
        return TYPE_SELECTOR;
    }
}
/*
 * @(#)AbstractLeafFigure.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import static org.jhotdraw.draw.Figure.CHILDREN_PROPERTY;

/**
 * This base class can be used to implement figures which do not support child
 * figures.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class AbstractLeafFigure extends AbstractFigure {

    private static final ReadOnlyListProperty<Figure> children = new ReadOnlyListWrapper<>(null, CHILDREN_PROPERTY);

    @Override
    public final ReadOnlyListProperty<Figure> childrenProperty() {
        return children;
    }

    @Override
    public final boolean allowsChildren() {
        return false;
    }

}

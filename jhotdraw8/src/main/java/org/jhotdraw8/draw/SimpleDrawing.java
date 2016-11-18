/* @(#)SimpleDrawing.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.draw.figure.StyleableFigure;
import org.jhotdraw8.draw.figure.LockableFigure;
import org.jhotdraw8.draw.figure.AbstractCompositeFigure;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.jhotdraw8.draw.figure.NonTransformableFigure;
import org.jhotdraw8.text.CColor;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import org.jhotdraw8.css.StylesheetsManager;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import org.jhotdraw8.css.SimpleStylesheetsManager;
import org.jhotdraw8.draw.css.FigureSelectorModel;

/**
 * SimpleDrawing.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SimpleDrawing extends AbstractCompositeFigure 
        implements Drawing, StyleableFigure, LockableFigure, NonTransformableFigure {

    /**
     * The style manager is created lazily. 
     */
    private StylesheetsManager<Figure> styleManager = null;

    public SimpleDrawing() {
    }

    @Override
    public Node createNode(RenderContext drawingView) {
        Group g = new Group();
        Rectangle background = new Rectangle();
        background.setId("background");
        g.getProperties().put("background", background);
        return g;
    }

    @Override
    public void updateNode(RenderContext v, Node n) {
        Group g = (Group) n;
        //applyTransformableFigureProperties(n);
        applyStyleableFigureProperties(v, n);
        
        Bounds bounds = getBoundsInLocal();
        Rectangle page = (Rectangle) g.getProperties().get("background");
        page.setX(bounds.getMinX());
        page.setY(bounds.getMinY());
        page.setWidth(bounds.getWidth());
        page.setHeight(bounds.getHeight());
        CColor cclr = getStyled(BACKGROUND);
        page.setFill(cclr==null?null:cclr.getColor());

        List<Node> nodes = new ArrayList<Node>(getChildren().size());
        nodes.add(page);
        for (Figure child : getChildren()) {
            nodes.add(v.getNode(child));
        }
        ObservableList<Node> group = ((Group) n).getChildren();
        if (! group.equals(nodes)) {
            group.setAll(nodes);
        }
    }

    /**
     * The bounds of this drawing is determined by its {@code WIDTH} and
     * and {@code HEIGHT}.
     * <p>
     * The bounds of its child figures does not affect the bounds of this
     * drawing. 
     * 
     * @return bounding box (0, 0, WIDTH, HEIGHT).
     */
    @Override
    public Bounds getBoundsInLocal() {
        return new BoundingBox(0.0, 0.0, get(WIDTH), get(HEIGHT));

    }

    @Override
    public void reshape(Transform transform) {
        Bounds b = getBoundsInLocal();
        b = transform.transform(b);
        reshape(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
    }

    @Override
    public void reshape(double x, double y, double width, double height) {
        
        set(WIDTH, abs(width));
        set(HEIGHT, abs(height));
    }

    /** Returns false. */
    @Override
    public boolean isLayoutable() {
        return false;
    }

    @Override
    public StylesheetsManager<Figure> getStyleManager() {
        if (styleManager == null) {
            styleManager = createStyleManager();
            styleManager.setStylesheets(StyleOrigin.USER_AGENT, get(DOCUMENT_HOME), get(USER_AGENT_STYLESHEETS));
            styleManager.setStylesheets(StyleOrigin.AUTHOR, get(DOCUMENT_HOME), get(AUTHOR_STYLESHEETS));
            styleManager.setStylesheets(StyleOrigin.INLINE, get(INLINE_STYLESHEETS));
        }
        return styleManager;
    }
    
    protected StylesheetsManager<Figure> createStyleManager() {
        return new SimpleStylesheetsManager<>(new FigureSelectorModel());
    }

    @Override
    public void stylesheetNotify() {
        if (styleManager != null) {
            styleManager.setStylesheets(StyleOrigin.USER_AGENT, get(DOCUMENT_HOME), get(USER_AGENT_STYLESHEETS));
            styleManager.setStylesheets(StyleOrigin.AUTHOR, get(DOCUMENT_HOME), get(AUTHOR_STYLESHEETS));
            styleManager.setStylesheets(StyleOrigin.INLINE, get(INLINE_STYLESHEETS));
        }
        super.stylesheetNotify();
    }
  
}
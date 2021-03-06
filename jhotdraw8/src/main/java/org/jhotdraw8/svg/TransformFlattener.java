/* @(#)TransformFlattener.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.svg;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import org.jhotdraw8.annotation.Nonnull;

/**
 * TransformFlattener.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class TransformFlattener {

    private boolean canFlattenTranslate(Node node) {
        if (node.getRotate() != 0.0
                || node.getScaleX() != 1.0
                || node.getScaleY() != 1.0) {
            return false;
        }
        for (Transform t : node.getTransforms()) {
            if (!(t instanceof Translate)) {
                return false;
            }
        }
        return true;

    }

    @Nonnull
    private Translate flattenTranslate(Node node) {
        Translate translate = new Translate(node.getTranslateX(), node.getTranslateY());
        for (Transform t : node.getTransforms()) {
            if ((t instanceof Translate)) {
                Translate tt = (Translate) t;
                translate = (Translate) translate.createConcatenation(tt);
            } else {
                throw new IllegalArgumentException("node has non-translate transforms.");
            }
        }
        node.setTranslateX(0.0);
        node.setTranslateY(0.0);
        node.getTransforms().clear();
        return translate;
    }

    /**
     * Tries to get rid of Translation transforms on a Node, by applying it to
     * descendants of the node or by adjusting the coordinates of the node.
     *
     * @param node a node
     */
    public void flattenTranslates(Node node) {
        if (node instanceof Group) {
            flattenTranslatesInGroup((Group) node);
        } else if (node instanceof Shape) {
            flattenTranslatesInShape((Shape) node);
        }
    }

    private void flattenTranslatesInGroup(@Nonnull Group group) {
        if (!canFlattenTranslate(group)) {
            return;
        }

        Translate translate = flattenTranslate(group);

        // apply  translation to children
        if (!translate.isIdentity()) {
            for (Node child : group.getChildren()) {
                child.getTransforms().add(translate);
            }
        }
        // try to flatten the children
        for (Node child : group.getChildren()) {
            flattenTranslates(child);
        }

    }

    private void flattenTranslatesInPath(@Nonnull Path path) {
        if (!canFlattenTranslate(path)) {
            return;
        }
        Translate t = flattenTranslate(path);
        double tx = t.getX();
        double ty = t.getY();
        for (PathElement element : path.getElements()) {
            if (element instanceof ClosePath) {
                // nothing to do
            } else if (element instanceof MoveTo) {
                MoveTo e = (MoveTo) element;
                e.setX(e.getX() + tx);
                e.setY(e.getY() + ty);
            } else if (element instanceof LineTo) {
                LineTo e = (LineTo) element;
                e.setX(e.getX() + tx);
                e.setY(e.getY() + ty);
            } else if (element instanceof QuadCurveTo) {
                QuadCurveTo e = (QuadCurveTo) element;
                e.setX(e.getX() + tx);
                e.setY(e.getY() + ty);
                e.setControlX(e.getControlX() + tx);
                e.setControlY(e.getControlY() + ty);
            } else if (element instanceof CubicCurveTo) {
                CubicCurveTo e = (CubicCurveTo) element;
                e.setX(e.getX() + tx);
                e.setY(e.getY() + ty);
                e.setControlX1(e.getControlX1() + tx);
                e.setControlY1(e.getControlY1() + ty);
                e.setControlX2(e.getControlX2() + tx);
                e.setControlY2(e.getControlY2() + ty);
            } else if (element instanceof ArcTo) {
                ArcTo e = (ArcTo) element;
                e.setX(e.getX() + tx);
                e.setY(e.getY() + ty);
            } else {
                throw new UnsupportedOperationException("unknown element type: " + element);
            }
        }
    }

    private void flattenTranslatesInPolygon(@Nonnull Polygon path) {
        if (!canFlattenTranslate(path)) {
            return;
        }
        Translate t = flattenTranslate(path);
        ObservableList<Double> points = path.getPoints();
        for (int i = 0, n = points.size(); i < n; i += 2) {
            double x = points.get(i);
            double y = points.get(i + 1);
            Point2D p = t.transform(x, y);
            points.set(i, p.getX());
            points.set(i + 1, p.getY());
        }
    }
    private void flattenTranslatesInPolyline(@Nonnull Polyline path) {
        if (!canFlattenTranslate(path)) {
            return;
        }
        Translate t = flattenTranslate(path);
        ObservableList<Double> points = path.getPoints();
        for (int i = 0, n = points.size(); i < n; i += 2) {
            double x = points.get(i);
            double y = points.get(i + 1);
            Point2D p = t.transform(x, y);
            points.set(i, p.getX());
            points.set(i + 1, p.getY());
        }
    }

    private void flattenTranslatesInLine(@Nonnull Line path) {
        if (!canFlattenTranslate(path)) {
            return;
        }
        Translate t = flattenTranslate(path);
        Point2D p = t.transform(path.getStartX(), path.getStartY());
        path.setStartX(p.getX());
        path.setStartY(p.getY());
        Point2D p2 = t.transform(path.getEndX(), path.getEndY());
        path.setEndX(p2.getX());
        path.setEndY(p2.getY());
    }

    private void flattenTranslatesInRectangle(@Nonnull Rectangle path) {
        if (!canFlattenTranslate(path)) {
            return;
        }
        Translate t = flattenTranslate(path);
        Point2D p = t.transform(path.getX(), path.getY());
        path.setX(p.getX());
        path.setY(p.getY());
    }
    
    private void flattenTranslatesInShape(Shape shape) {
        if (shape instanceof Path) {
            flattenTranslatesInPath((Path) shape);
        } else if (shape instanceof Polygon) {
            flattenTranslatesInPolygon((Polygon) shape);
        } else if (shape instanceof Polyline) {
            flattenTranslatesInPolyline((Polyline) shape);
        } else if (shape instanceof Line) {
            flattenTranslatesInLine((Line) shape);
        } else if (shape instanceof Rectangle) {
            flattenTranslatesInRectangle((Rectangle) shape);
        }
        // FIXME implement more shapes
    }
}

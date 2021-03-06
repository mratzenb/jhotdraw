/* @(#)LineFigure.java
 * Copyright © by the authors and contributors ofCollection JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.figure;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Transform;
import org.jhotdraw8.annotation.Nonnull;

import org.jhotdraw8.collection.ImmutableList;
import org.jhotdraw8.collection.NonnullMapAccessor;
import org.jhotdraw8.css.CssPoint2D;
import org.jhotdraw8.css.CssRectangle2D;
import org.jhotdraw8.draw.handle.Handle;
import org.jhotdraw8.draw.handle.HandleType;
import org.jhotdraw8.draw.handle.PolyPointEditHandle;
import org.jhotdraw8.draw.handle.PolyPointMoveHandle;
import org.jhotdraw8.draw.handle.PolylineOutlineHandle;
import org.jhotdraw8.draw.key.DirtyBits;
import org.jhotdraw8.draw.key.DirtyMask;
import org.jhotdraw8.draw.key.Point2DListStyleableFigureKey;
import org.jhotdraw8.draw.render.RenderContext;
import org.jhotdraw8.geom.Shapes;

/**
 * A figure which draws a connected line segments.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SimplePolylineFigure extends AbstractLeafFigure
        implements StrokableFigure, FillableFigure, HideableFigure, StyleableFigure,
        LockableFigure, CompositableFigure, TransformableFigure, ResizableFigure,
        PathIterableFigure{

    public final static Point2DListStyleableFigureKey POINTS = new Point2DListStyleableFigureKey("points", DirtyMask.of(DirtyBits.NODE, DirtyBits.LAYOUT, DirtyBits.LAYOUT_OBSERVERS), ImmutableList.emptyList());
    /**
     * The CSS type selector for this object is {@value #TYPE_SELECTOR}.
     */
    public final static String TYPE_SELECTOR = "Polyline";

    public SimplePolylineFigure() {
        this(0, 0, 1, 1);
    }

    public SimplePolylineFigure(double startX, double startY, double endX, double endY) {
        set(POINTS, ImmutableList.of(new Point2D(startX, startY), new Point2D(endX, endY)));
        set(FILL, null);
    }

    public SimplePolylineFigure(Point2D... points) {
        set(POINTS, ImmutableList.of(points));
        set(FILL, null);
    }

    @Override
    public void createHandles(HandleType handleType, @Nonnull List<Handle> list) {
        if (handleType == HandleType.SELECT) {
            list.add(new PolylineOutlineHandle(this, POINTS,false, Handle.STYLECLASS_HANDLE_SELECT_OUTLINE));
        } else if (handleType == HandleType.MOVE) {
            list.add(new PolylineOutlineHandle(this, POINTS, false,Handle.STYLECLASS_HANDLE_MOVE_OUTLINE));
            for (int i = 0, n = getNonnull(POINTS).size(); i < n; i++) {
                list.add(new PolyPointMoveHandle(this, POINTS, i, Handle.STYLECLASS_HANDLE_MOVE));
            }
        } else if (handleType == HandleType.POINT) {
            list.add(new PolylineOutlineHandle(this, POINTS, true,Handle.STYLECLASS_HANDLE_POINT_OUTLINE));
            for (int i = 0, n = getNonnull(POINTS).size(); i < n; i++) {
                list.add(new PolyPointEditHandle(this, POINTS, i, Handle.STYLECLASS_HANDLE_POINT));
            }
        } else {
            super.createHandles(handleType, list);
        }
    }

    @Nonnull
    @Override
    public Node createNode(RenderContext drawingView) {
        return new Polyline();
    }

    @Nonnull
    @Override
    public Bounds getBoundsInLocal() {
        // XXX should be cached
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (Point2D p : getNonnull(POINTS)) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }
        return new BoundingBox(minX, minY, maxX - minX, maxY - minY);
    }
    public CssRectangle2D getCssBoundsInLocal() {
        return new CssRectangle2D(getBoundsInLocal());
    }

    @Nonnull
    @Override
    public PathIterator getPathIterator(AffineTransform tx) {
       return Shapes.pathIteratorFromPoints(getNonnull(POINTS).asList(), false, PathIterator.WIND_NON_ZERO, tx);
    }

    @Nonnull
    @Override
    public String getTypeSelector() {
        return TYPE_SELECTOR;
    }

    @Override
    public void reshapeInLocal(@Nonnull Transform transform) {
        ArrayList<Point2D> newP = getNonnull(POINTS).toArrayList();
        for (int i = 0, n = newP.size(); i < n; i++) {
            newP.set(i, transform.transform(newP.get(i)));
        }
        set(POINTS,  ImmutableList.ofCollection(newP));
    }
    @Override
    public void translateInLocal(CssPoint2D t) {
        ArrayList<Point2D> newP = getNonnull(POINTS).toArrayList();
        for (int i = 0, n = newP.size(); i < n; i++) {
            newP.set(i, newP.get(i).add(t.getConvertedValue()));
        }
        set(POINTS, ImmutableList.ofCollection(newP));
    }


    @Override
    public void updateNode(@Nonnull RenderContext ctx, @Nonnull Node node) {
        Polyline lineNode = (Polyline) node;
        applyHideableFigureProperties(ctx, node);
        applyStyleableFigureProperties(ctx, node);
        applyStrokableFigureProperties(ctx, lineNode);
        applyFillableFigureProperties(ctx, lineNode);
        applyTransformableFigureProperties(ctx, node);
        applyCompositableFigureProperties(ctx, lineNode);
        final ImmutableList<Point2D> points = getStyledNonnull(POINTS);
        List<Double> list = new ArrayList<>(points.size() * 2);
        for (Point2D p : points) {
            list.add(p.getX());
            list.add(p.getY());
        }
        lineNode.getPoints().setAll(list);
        lineNode.applyCss();
    }

    @Nonnull
    public static double[] toPointArray(@Nonnull Figure f, @Nonnull NonnullMapAccessor<ImmutableList<Point2D>> key) {
        ImmutableList<Point2D> points = f.getNonnull(key);
        double[] a = new double[points.size() * 2];
        for (int i = 0, n = points.size(), j = 0; i < n; i++, j += 2) {
            Point2D p = points.get(i);
            a[j] = p.getX();
            a[j + 1] = p.getY();
        }
        return a;
    }

}

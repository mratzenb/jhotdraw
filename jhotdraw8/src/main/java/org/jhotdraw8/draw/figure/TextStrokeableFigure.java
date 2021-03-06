/* @(#)TextableFigure.java
 * Copyright © by the authors and contributors ofCollection JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.figure;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.collection.ImmutableList;
import org.jhotdraw8.css.CssSize;
import org.jhotdraw8.css.Paintable;
import org.jhotdraw8.css.text.CssSizeConverter;
import org.jhotdraw8.draw.key.CssSizeStyleableFigureKey;
import org.jhotdraw8.draw.key.DirtyBits;
import org.jhotdraw8.draw.key.DirtyMask;
import org.jhotdraw8.draw.key.EnumStyleableFigureKey;
import org.jhotdraw8.draw.key.ListStyleableFigureKey;
import org.jhotdraw8.draw.key.NullablePaintableStyleableFigureKey;
import org.jhotdraw8.draw.key.StrokeStyleableMapAccessor;
import org.jhotdraw8.draw.render.RenderContext;
import org.jhotdraw8.io.DefaultUnitConverter;
import org.jhotdraw8.io.UnitConverter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * {@code TextStrokeableFigure} allows to change the stroke ofCollection the
 * text.
 *
 * @author Werner Randelshofer
 * @version $Id$
 * @design.pattern Figure Mixin, Traits.
 */
public interface TextStrokeableFigure extends Figure {

    /**
     * Defines the distance in user coordinates for the dashing pattern. Default
     * value: {@code 0}.
     * <p>
     * References:
     * <p>
     * <a href="http://www.w3.org/TR/SVG/painting.html#StrokeProperties">SVG
     * Stroke Properties</a>
     */
    CssSizeStyleableFigureKey TEXT_STROKE_DASH_OFFSET = new CssSizeStyleableFigureKey("text-stroke-dashoffset", DirtyMask.of(DirtyBits.NODE), CssSize.ZERO);
    /**
     * Defines the end cap style. Default value: {@code SQUARE}.
     * <p>
     * References:
     * <p>
     * <a href="http://www.w3.org/TR/SVG/painting.html#StrokeProperties">SVG
     * Stroke Properties</a>
     */
    EnumStyleableFigureKey<StrokeLineCap> TEXT_STROKE_LINE_CAP = new EnumStyleableFigureKey<>("text-stroke-linecap", StrokeLineCap.class, DirtyMask.of(DirtyBits.NODE),  StrokeLineCap.BUTT);
    /**
     * Defines the style applied where path segments meet. Default value:
     * {@code MITER}.
     * <p>
     * References:
     * <p>
     * <a href="http://www.w3.org/TR/SVG/painting.html#StrokeProperties">SVG
     * Stroke Properties</a>
     */
    EnumStyleableFigureKey<StrokeLineJoin> TEXT_STROKE_LINE_JOIN = new EnumStyleableFigureKey<>("text-stroke-linejoin", StrokeLineJoin.class, DirtyMask.of(DirtyBits.NODE),  StrokeLineJoin.MITER);
    /**
     * Defines the limit for the {@code StrokeLineJoin.MITER} style. Default
     * value: {@code 4.0}.
     * <p>
     * References:
     * <p>
     * <a href="http://www.w3.org/TR/SVG/painting.html#StrokeProperties">SVG
     * Stroke Properties</a>
     */
    CssSizeStyleableFigureKey TEXT_STROKE_MITER_LIMIT = new CssSizeStyleableFigureKey("text-stroke-miterlimit", DirtyMask.of(DirtyBits.NODE), new CssSize(10.0));
    /**
     * Defines the paint used for filling the outline of the figure. Default
     * value: {@code null}.
     * <p>
     * References:
     * <p>
     * <a href="http://www.w3.org/TR/SVG/painting.html#StrokeProperties">SVG
     * Stroke Properties</a>
     */
    NullablePaintableStyleableFigureKey TEXT_STROKE = new NullablePaintableStyleableFigureKey("text-stroke", null);
    /**
     * Defines the stroke type used for drawing outline of the figure.
     * <p>
     * Default value: {@code StrokeType.OUTSIDE}.
     */
    EnumStyleableFigureKey<StrokeType> TEXT_STROKE_TYPE = new EnumStyleableFigureKey<>("text-stroke-type", StrokeType.class, DirtyMask.of(DirtyBits.NODE), StrokeType.OUTSIDE);
    /**
     * Defines the width of the outline of the figure.
     * <p>
     * Default value: {@code 1.0}.
     * <p>
     * References:
     * <p>
     * <a href="http://www.w3.org/TR/SVG/painting.html#StrokeProperties">SVG
     * Stroke Properties</a>
     */
    CssSizeStyleableFigureKey TEXT_STROKE_WIDTH = new CssSizeStyleableFigureKey("text-stroke-width", DirtyMask.of(DirtyBits.NODE), CssSize.ONE);

    /**
     * Defines the dash array used. Default value: {@code empty array}.
     * <p>
     * References:
     * <p>
     * <a href="http://www.w3.org/TR/SVG/painting.html#StrokeProperties">SVG
     * Stroke Properties</a>
     */
    ListStyleableFigureKey<CssSize> TEXT_STROKE_DASH_ARRAY = new ListStyleableFigureKey<>("text-stroke-dasharray",
            DirtyMask.of(DirtyBits.NODE), CssSize.class, new CssSizeConverter(false), ImmutableList.emptyList());

    /**
     * Combined map accessor for all stroke style properties.
     */
    StrokeStyleableMapAccessor TEXT_STROKE_STYLE = new StrokeStyleableMapAccessor("text-stroke-style", TEXT_STROKE_WIDTH,
            TEXT_STROKE, TEXT_STROKE_TYPE, TEXT_STROKE_LINE_CAP, TEXT_STROKE_LINE_JOIN, TEXT_STROKE_MITER_LIMIT, TEXT_STROKE_DASH_OFFSET, TEXT_STROKE_DASH_ARRAY);

    /**
     * Updates a shape node.
     *
     * @param ctx the render context
     * @param shape a shape node
     */
    default void applyTextStrokeableFigureProperties(@Nullable RenderContext ctx, @Nonnull Shape shape) {
        Paint paint = Paintable.getPaint(getStyled(TEXT_STROKE));
        UnitConverter units = ctx == null ? DefaultUnitConverter.getInstance() : ctx.getNonnull(RenderContext.UNIT_CONVERTER_KEY);

        double strokeWidth = units.convert(getStyledNonnull(TEXT_STROKE_WIDTH), UnitConverter.DEFAULT);
        if (!Objects.equals(shape.getStroke(), paint)) {
            shape.setStroke(paint);
        }
        if (paint == null) {
            return;
        }
        if (shape.getStrokeWidth() != strokeWidth) {
            shape.setStrokeWidth(strokeWidth);
        }
        StrokeLineCap slp = getStyled(TEXT_STROKE_LINE_CAP);
        if (shape.getStrokeLineCap() != slp) {
            shape.setStrokeLineCap(slp);
        }
        StrokeLineJoin slj = getStyled(TEXT_STROKE_LINE_JOIN);
        if (shape.getStrokeLineJoin() != slj) {
            shape.setStrokeLineJoin(slj);
        }
        double d = units.convert(getStyledNonnull(TEXT_STROKE_MITER_LIMIT), UnitConverter.DEFAULT);
        if (shape.getStrokeMiterLimit() != d) {
            shape.setStrokeMiterLimit(d);
        }
        StrokeType st = getStyled(TEXT_STROKE_TYPE);
        if (shape.getStrokeType() != st) {
            shape.setStrokeType(st);
        }
        applyTextStrokeDashProperties(shape);

    }


    default void applyTextStrokeDashProperties(@Nonnull Shape shape) {
        double d = getStyledNonnull(TEXT_STROKE_DASH_OFFSET).getConvertedValue();
        if (shape.getStrokeDashOffset() != d) {
            shape.setStrokeDashOffset(d);
        }
        ImmutableList<CssSize> dashArray = getStyledNonnull(TEXT_STROKE_DASH_ARRAY);
        if (dashArray.isEmpty()) {
            shape.getStrokeDashArray().clear();
        } else {
            ArrayList<Double> list = new ArrayList<>(dashArray.size());
            for (CssSize sz : dashArray) {
                list.add(sz.getConvertedValue());
            }
            shape.getStrokeDashArray().setAll(list);
        }
    }

}

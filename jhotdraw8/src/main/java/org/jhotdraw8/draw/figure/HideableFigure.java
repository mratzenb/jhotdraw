/* @(#)HideableFigure.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.figure;

import javafx.scene.Node;
import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;

import org.jhotdraw8.draw.key.BooleanStyleableFigureKey;
import org.jhotdraw8.draw.key.DirtyBits;
import org.jhotdraw8.draw.key.DirtyMask;
import org.jhotdraw8.draw.render.RenderContext;

/**
 * HideableFigure.
 *
 * @design.pattern Figure Mixin, Traits.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface HideableFigure extends Figure {

    /**
     * Defines the visibility of the figure. Default value: {@code true}.
     */
    BooleanStyleableFigureKey VISIBLE = new BooleanStyleableFigureKey("visible", DirtyMask.of(DirtyBits.NODE), true);

    /**
     * Updates a figure node with all style and effect properties defined in
     * this interface.
     * <p>
     * Applies the following properties: {@code STYLE_ID}, {@code VISIBLE}.
     * <p>
     * This method is intended to be used by {@link #updateNode}.
     *
     * @param ctx the render context
     * @param node a node which was created with method {@link #createNode}.
     */
    default void applyHideableFigureProperties(@Nullable RenderContext ctx, @Nonnull Node node) {
        node.setVisible(getStyledNonnull(VISIBLE));
    }

}

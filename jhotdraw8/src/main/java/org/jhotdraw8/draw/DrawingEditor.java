/* @(#)DrawingEditor.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.collections.ObservableSet;

import org.jhotdraw8.annotation.Nullable;

import org.jhotdraw8.draw.tool.Tool;

/**
 * The DrawingEditor can be used to edit multiple {@code DrawingView}s with a
 * single {@code Tool}.
 * <p>
 * The DrawingEditor invokes {@code activate()} and {@code deactivate()} methods
 * on the {@code Tool} if it becomes the active tool or loses this status.
 *
 * @author Werner Randelshofer
 * @version $Id$
 * @design.pattern org.jhotdraw8.draw.figure.Drawing Framework, KeyAbstraction.
 * @design.pattern DrawingEditor Mediator, Mediator. The DrawingEditor allows to
 * use the same {@code Tool} with multiple {@code DrawingView}s.
 */
public interface DrawingEditor {

    // ----
    // property names
    // ----
    /**
     * The name of the drawingViews property.
     */
    public final static String DRAWING_VIEWS_PROPERTY = "drawingViews";
    /**
     * The name of the activeDrawingView property.
     */
    public final static String ACTIVE_DRAWING_VIEW_PROPERTY = "activeDrawingView";
    /**
     * The name of the activeTool property.
     */
    public final static String ACTIVE_TOOL_PROPERTY = "activeTool";
    /**
     * The name of the defaultTool property.
     */
    public final static String DEFAULT_TOOL_PROPERTY = "defaultTool";

    // ---
    // properties
    // ---

    /**
     * The drawing views associated with this editor.
     *
     * @return the property
     */
    ReadOnlySetProperty<DrawingView> drawingViewsProperty();

    /**
     * The currently active drawing view.
     *
     * @return the property
     */
    ObjectProperty<DrawingView> activeDrawingViewProperty();

    /**
     * The currently active tool.
     *
     * @return the property
     */
    ObjectProperty<Tool> activeToolProperty();

    /**
     * The default tool. When the value is not null, the default tool is made
     * the active tool whenever another tool fires {@code ToolEvent.TOOL_DONE}.
     *
     * @return the property
     */
    ObjectProperty<Tool> defaultToolProperty();

    // ---
    // convenience methods
    // ---

    /**
     * Adds a drawing view to this editor.
     *
     * @param drawingView the drawing view
     */
    default void addDrawingView(DrawingView drawingView) {
        drawingViewsProperty().add(drawingView);
    }

    /**
     * Removes a drawing view to this editor.
     *
     * @param drawingView the drawing view
     */
    default void removeDrawingView(DrawingView drawingView) {
        drawingViewsProperty().remove(drawingView);
    }

    /**
     * Gets the active drawing view.
     *
     * @return the active drawing view or empty
     */
    @Nullable
    default DrawingView getActiveDrawingView() {
        return activeDrawingViewProperty().get();
    }

    /**
     * Gets the drawing views.
     *
     * @return the active drawing view or empty
     */
    default ObservableSet<DrawingView> getDrawingViews() {
        return drawingViewsProperty().get();
    }

    /**
     * Sets the active drawing view.
     *
     * @param drawingView the drawing view or null if none is active
     */
    default void setActiveDrawingView(@Nullable DrawingView drawingView) {
        activeDrawingViewProperty().set(drawingView);
    }

    /**
     * Gets the active tool.
     *
     * @return the active tool or null
     */
    @Nullable
    default Tool getActiveTool() {
        return activeToolProperty().get();
    }

    /**
     * Sets the active tool.
     *
     * @param tool the active tool or null if none is active
     */
    default void setActiveTool(@Nullable Tool tool) {
        activeToolProperty().set(tool);
    }

    /**
     * Gets the default tool.
     *
     * @return the default tool or null
     */
    @Nullable
    default Tool getDefaultTool() {
        return defaultToolProperty().get();
    }

    /**
     * Sets the default tool.
     *
     * @param tool the default tool or null if no default tool is desired
     */
    default void setDefaultTool(@Nullable Tool tool) {
        defaultToolProperty().set(tool);
    }

}

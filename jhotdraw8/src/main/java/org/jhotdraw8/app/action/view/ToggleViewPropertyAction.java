/* @(#)ToggleViewPropertyAction.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.app.action.view;

import java.util.function.Function;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jhotdraw8.app.ActivityViewController;
import org.jhotdraw8.app.Application;
import org.jhotdraw8.app.action.AbstractViewControllerAction;
import org.jhotdraw8.util.Resources;

/**
 * ToggleViewPropertyAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class ToggleViewPropertyAction extends AbstractViewControllerAction<ActivityViewController> {

    private static final long serialVersionUID = 1L;
    @Nullable
    private BooleanProperty property;
    @Nullable
    private final Function<ActivityViewController, Node> nodeGetter;

    public ToggleViewPropertyAction(Application app, ActivityViewController view, @Nonnull BooleanProperty property, String id, Resources labels) {
        super(app, view,null);
        labels.configureAction(this, id);
        this.property = property;
        this.nodeGetter = null;
selectedProperty().bindBidirectional(  property);
    }

    public ToggleViewPropertyAction(Application app, ActivityViewController view, Function<ActivityViewController, Node> nodeGetter, String id, Resources labels) {
        super(app, view,null);
        labels.configureAction(this, id);
        this.property = null;
        this.nodeGetter = nodeGetter;
    }

    @Override
    protected void handleActionPerformed(ActionEvent event, ActivityViewController view) {
        if (property != null) {
            property.set(!property.get());
        } else {
            Node node = nodeGetter.apply(view);
            node.setVisible(!node.isVisible());
            this.setSelected(node.isVisible());
        }
    }

}

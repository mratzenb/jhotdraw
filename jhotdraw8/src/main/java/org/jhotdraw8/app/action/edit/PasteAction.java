/* @(#)PasteAction.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.app.action.edit;

import javafx.event.ActionEvent;
import javafx.scene.Node;

import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.app.Application;
import org.jhotdraw8.app.EditableComponent;
import org.jhotdraw8.app.Labels;

/**
 * Pastes the contents of the system clipboard at the caret position.
 * <p>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class PasteAction extends AbstractSelectionAction {

    private static final long serialVersionUID = 1L;

    public static final String ID = "edit.paste";

    /**
     * Creates a new instance which acts on the currently focused component.
     *
     * @param app the application
     */
    public PasteAction(Application app) {
        this(app, null);
    }

    /**
     * Creates a new instance which acts on the specified component.
     *
     * @param app the application
     * @param target The target of the action. Specify null for the currently
     * focused component.
     */
    public PasteAction(Application app, Node target) {
        super(app, target);
        Labels.getLabels().configureAction(this, ID);
    }

    @Override
    protected void handleActionPerformed(ActionEvent event, @Nonnull EditableComponent c) {
        c.paste();
    }
}

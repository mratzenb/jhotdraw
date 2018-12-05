/* @(#)ClearFileAction.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.app.action.file;

import java.util.concurrent.CompletionStage;

import javax.annotation.Nonnull;
import org.jhotdraw8.app.Application;
import org.jhotdraw8.app.DocumentOrientedActivityViewController;
import org.jhotdraw8.app.Labels;
import org.jhotdraw8.app.action.AbstractSaveUnsavedChangesAction;
import org.jhotdraw8.util.Resources;

/**
 * Clears (empties) the contents of the active view.
 * <p>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class ClearFileAction extends AbstractSaveUnsavedChangesAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "file.clear";

    /**
     * Creates a new instance.
     *
     * @param app the application
     * @param view the view
     */
    public ClearFileAction(Application app, DocumentOrientedActivityViewController view) {
        super(app, view);
        Resources labels = Labels.getLabels();
        labels.configureAction(this, "file.clear");
    }

    @Override
    public CompletionStage<Void> doIt(@Nonnull final DocumentOrientedActivityViewController view) {
        return view.clear();
    }
}

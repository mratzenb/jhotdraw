/*
 * @(#)JDoubleDrawingAttributeSlider.java  1.0  2008-05-18
 *
 * Copyright (c) 2007 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.gui;

import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.draw.*;

/**
 * A JSlider that can be used to edit a double attribute of a Drawing.
 *
 * @author Werner Randelshofer
 * @version 1.0 2008-05-18 Created.
 */
public class JDoubleDrawingAttributeSlider extends JDoubleAttributeSlider {

    /** Creates new instance. */
    public JDoubleDrawingAttributeSlider() {
        super(null, null);
    }

    public JDoubleDrawingAttributeSlider(int orientation, int min, int max, int value) {
        super(orientation, min, max, value);
    }

    public JDoubleDrawingAttributeSlider(DrawingEditor editor, AttributeKey<Double> attributeKey) {
        super(editor, attributeKey);
    }

    @Override
    protected void updateSlider() {
        if (isUpdatingSlider++ == 0) {
            if (getView() == null || attributeKey == null) {
                setValue(0);
            } else {
                Double sliderValue = null;
                boolean isFirst = true;
                Figure f = getView().getDrawing();
                sliderValue = attributeKey.get(f);
                if (sliderValue != null) {
                    setValue((int) (sliderValue * scaleFactor));
                }
            }
            repaint();
        }
        isUpdatingSlider--;
    }

    @Override
    protected void updateFigures() {
        if (isUpdatingSlider++ == 0) {
            double value = getValue() / scaleFactor;
            if (getView() != null && attributeKey != null) {
                if (attributeRestoreData.isEmpty()) {
                    Figure f = getView().getDrawing();
                    attributeRestoreData.add(f.getAttributesRestoreData());
                    attributeKey.set(f, value);
                } else {
                    Figure f = getView().getDrawing();
                    attributeKey.set(f, value);
                }
                if (!getModel().getValueIsAdjusting()) {
                    final Figure editedFigure = getView().getDrawing();
                    final LinkedList<Object> editUndoData = new LinkedList<Object>(attributeRestoreData);
                    final double editRedoValue = value;
                    UndoableEdit edit = new AbstractUndoableEdit() {

                        @Override
                        public String getPresentationName() {
                            return labels.getString("attribute."+attributeKey.getKey()+".text");
                        }

                        @Override
                        public void undo() throws CannotRedoException {
                            super.undo();
                            Iterator<Object> di = editUndoData.iterator();
                            editedFigure.willChange();
                            editedFigure.restoreAttributesTo(di.next());
                            editedFigure.changed();
                        }

                        @Override
                        public void redo() throws CannotRedoException {
                            super.redo();
                            editedFigure.willChange();
                            attributeKey.basicSet(editedFigure, editRedoValue);
                            editedFigure.changed();
                        }
                    };
                    getView().getDrawing().fireUndoableEditHappened(edit);
                }
            }
        }
        isUpdatingSlider--;
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

/* @(#)SimpleTreeModel.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */

package org.jhotdraw8.tree;

import org.jhotdraw8.tree.TreeModel;
import org.jhotdraw8.tree.TreeModelEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import org.jhotdraw8.event.Listener;

/**
 * SimpleTreeModel.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 * @param <E> the element type
 */
public class SimpleTreeModel<E> implements TreeModel<E> {

    @Override
    public E getChildAt(E parent, int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChildCount(E node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<E> getChildren(E node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CopyOnWriteArrayList<InvalidationListener> getInvalidationListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CopyOnWriteArrayList<Listener<TreeModelEvent<E>>> getTreeModelListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertChildAt(E child, E parent, int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeFromParent(E child) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObjectProperty<E> rootProperty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
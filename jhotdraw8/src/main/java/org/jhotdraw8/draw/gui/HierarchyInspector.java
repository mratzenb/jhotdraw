/* @(#)HierarchyInspector.java
 * Copyright (c) 2016 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw.gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import org.jhotdraw8.collection.Key;
import org.jhotdraw8.draw.DrawingView;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.draw.figure.StyleableFigure;
import org.jhotdraw8.draw.model.DrawingModelFigureProperty;
import org.jhotdraw8.draw.model.FigureTreePresentationModel;
import org.jhotdraw8.draw.model.SimpleDrawingModel;
import org.jhotdraw8.text.CssWordListConverter;
import org.jhotdraw8.util.Resources;

/**
 * FXML Controller class
 *
 * @author werni
 */
public class HierarchyInspector extends AbstractDrawingViewInspector {

    @FXML
    private TreeTableView<Figure> treeView;

    @FXML
    private TreeTableColumn<Figure, String> typeColumn;

    @FXML
    private TreeTableColumn<Figure, String> idColumn;
    @FXML
    private TreeTableColumn<Figure, String> classesColumn;

    private DrawingView drawingView;
    private Node node;
    private FigureTreePresentationModel model;
    private boolean isUpdatingSelection;
    private final SetChangeListener<Figure> viewSelectionHandler = change -> {
        updateSelectionInTree();
    };
    private final InvalidationListener treeSelectionHandler = change -> {
      if (model.isUpdating()) {
//        updateSelectionInTree();
      } else {
        updateSelectionInView();
      }
    };

    private CssWordListConverter wordListConverter = new CssWordListConverter();

    public HierarchyInspector() {
        this(HierarchyInspector.class.getResource("HierarchyInspector.fxml"),
                Resources.getBundle("org.jhotdraw.draw.gui.Labels"));
    }

    public HierarchyInspector(URL fxmlUrl, ResourceBundle resources) {
        init(fxmlUrl, resources);
    }

    private void init(URL fxmlUrl, ResourceBundle resources) {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setResources(resources);
        try (InputStream in = fxmlUrl.openStream()) {
            node = loader.load(in);
        } catch (IOException ex) {
            throw new InternalError(ex);
        }

        model = new FigureTreePresentationModel();
        typeColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(
                cell.getValue().getValue().getTypeSelector())
        );
        idColumn.setCellValueFactory(
                cell -> new DrawingModelFigureProperty<String>(model.getModel(),
                        cell.getValue().getValue(), StyleableFigure.STYLE_ID)
        );
        classesColumn.setCellValueFactory(
                cell -> Bindings.createStringBinding(()->wordListConverter.toString(cell.getValue().getValue().get(StyleableFigure.STYLE_CLASS)), 
                        new DrawingModelFigureProperty<ObservableList<String>>(model.getModel(),
                        cell.getValue().getValue(), StyleableFigure.STYLE_CLASS))
        );
        idColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        treeView.setRoot(model.getRoot());
        model.getRoot().setExpanded(true);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.getSelectionModel().getSelectedCells().addListener(treeSelectionHandler);
       
//        model.updatingProperty().addListener(modelUpdateHandler);
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    protected void onDrawingViewChanged(DrawingView oldValue, DrawingView newValue) {
        if (oldValue != null) {
            oldValue.getSelectedFigures().removeListener(viewSelectionHandler);
        }
        drawingView = newValue;
        if (newValue != null) {
            model.setDrawingModel(newValue.getModel());
            newValue.getSelectedFigures().addListener(viewSelectionHandler);
        } else {
            model.setDrawingModel(new SimpleDrawingModel());
        }
    }

    private void updateSelectionInTree() {
        if (!isUpdatingSelection) {
            isUpdatingSelection = true;
            TreeTableView.TreeTableViewSelectionModel<Figure> selectionModel = treeView.getSelectionModel();
            selectionModel.clearSelection();
            for (Figure f:drawingView.getSelectedFigures()) {
                selectionModel.select(model.getTreeItem(f));
            }
        isUpdatingSelection = false;
        }
    }

    private void updateSelectionInView() {
        if (!isUpdatingSelection) {
            isUpdatingSelection = true;
            TreeTableView.TreeTableViewSelectionModel<Figure> selectionModel = treeView.getSelectionModel();
            Set<Figure> newSelection = new LinkedHashSet<>();
            for (TreeItem<Figure> item:selectionModel.getSelectedItems()) {
                if (item != null) {
                newSelection.add(item.getValue());
                }
            }
            drawingView.getSelectedFigures().retainAll(newSelection);
            drawingView.getSelectedFigures().addAll(newSelection);
        isUpdatingSelection = false;
        }
    }

}
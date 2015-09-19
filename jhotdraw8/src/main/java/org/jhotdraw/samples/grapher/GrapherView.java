/* @(#)GrapherViewController.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.grapher;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import org.jhotdraw.app.AbstractView;
import org.jhotdraw.concurrent.BackgroundTask;
import org.jhotdraw.concurrent.TaskCompletionEvent;
import org.jhotdraw.draw.SimpleDrawingView;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.shape.RectangleFigure;
import org.jhotdraw.draw.SimpleDrawing;
import org.jhotdraw.draw.SimpleDrawingEditor;
import org.jhotdraw.draw.TextHolderFigure;
import org.jhotdraw.draw.constrain.GridConstrainer;
import org.jhotdraw.draw.gui.ToolsToolbar;
import org.jhotdraw.draw.gui.ZoomToolbar;
import org.jhotdraw.draw.io.DefaultFigureFactory;
import org.jhotdraw.draw.io.FigureFactory;
import org.jhotdraw.draw.io.SimpleXmlIO;
import org.jhotdraw.draw.shape.EllipseFigure;
import org.jhotdraw.draw.shape.LineFigure;
import org.jhotdraw.draw.shape.TextFigure;
import org.jhotdraw.draw.tool.CreationTool;
import org.jhotdraw.draw.tool.SelectionTool;
import org.jhotdraw.util.Resources;

/**
 *
 * @author werni
 */
public class GrapherView extends AbstractView {

    private Node node;

    @FXML
    private ToolBar toolBar;
    @FXML
    private ScrollPane scrollPane;

    private DrawingView drawingView;

    private DrawingEditor editor;

    @Override
    public void init(EventHandler<TaskCompletionEvent> callback) {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        try {
            node = loader.load(getClass().getResourceAsStream("GrapherView.fxml"));
        } catch (IOException ex) {
            throw new InternalError(ex);
        }

        drawingView = new SimpleDrawingView();
        drawingView.setConstrainer(new GridConstrainer(0, 0, 1, 1, 1));
        
        // 
        drawingView.getDrawingModel().addListener((InvalidationListener)drawingModel -> {
            modified.set(true);
        });
        
        editor = new SimpleDrawingEditor();
        editor.addDrawingView(drawingView);

        scrollPane.setContent(drawingView.getNode());

        ToolsToolbar ttbar = new ToolsToolbar();
        Resources rsrc = Resources.getBundle("org.jhotdraw.draw.Labels");
        ttbar.addTool(new SelectionTool("selectionTool", rsrc), 0, 0);
        ttbar.addTool(new CreationTool("edit.createRectangle", rsrc, () -> new RectangleFigure()), 1, 0);
        ttbar.addTool(new CreationTool("edit.createEllipse", rsrc, () -> new EllipseFigure()), 2, 0);
        ttbar.addTool(new CreationTool("edit.createLine", rsrc, () -> new LineFigure()), 1, 1);
        ttbar.addTool(new CreationTool("edit.createText", rsrc, () -> new TextFigure(0, 0, "Hello")), 2, 1);
        ttbar.setDrawingEditor(editor);
        toolBar.getItems().add(ttbar);

        ZoomToolbar ztbar = new ZoomToolbar();
        ztbar.setDrawingView(drawingView);
        toolBar.getItems().add(ztbar);
        
        callback.handle(new TaskCompletionEvent());
    }

    @Override
    public Node getNode() {
        return node;
    }

   

    @Override
    public void read(URI uri, boolean append, EventHandler<TaskCompletionEvent> callback) {
        BackgroundTask<SimpleDrawing> t = new BackgroundTask<SimpleDrawing>() {

            @Override
            protected SimpleDrawing call() throws Exception {
                try {
                    FigureFactory factory = new DefaultFigureFactory();
                    SimpleXmlIO io = new SimpleXmlIO(factory);
                    SimpleDrawing drawing = (SimpleDrawing) io.read(uri, null);
                    return drawing;
                } catch (Exception e) {
                    throw e;
                }
            }

            @Override
            protected void succeeded(SimpleDrawing value) {
                drawingView.setDrawing(value);

            }

        };
        t.addCompletionHandler(callback);
        getApplication().execute(t);
    }

    @Override
    public void write(URI uri, EventHandler<TaskCompletionEvent> callback) {
        BackgroundTask<Void> t = new BackgroundTask<Void>() {

            @Override
            protected Void call() throws Exception {
                FigureFactory factory = new DefaultFigureFactory();
                SimpleXmlIO io = new SimpleXmlIO(factory);
                io.write(uri, drawingView.getDrawing());
                return null;
            }
        };
        t.addCompletionHandler(callback);
        getApplication().execute(t);
    }

    @Override
    public void clear(EventHandler<TaskCompletionEvent> callback) {
        drawingView.setDrawing(new SimpleDrawing());
    }

    @FXML
    public void buttonPerformed(ActionEvent event) {
        Instant now = Instant.now();
        for (Figure f : drawingView.getDrawing().childrenProperty()) {
            if (f instanceof TextHolderFigure) {
                TextHolderFigure tf = (TextHolderFigure) f;
                tf.set(TextHolderFigure.TEXT, now.toString());
            }
        }
    }
}
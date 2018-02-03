/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw8.gui.fontchooser;

/**
 * FXML Controller class
 *
 * @author werni
 */
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import org.jhotdraw8.util.Resources;

public class FontChooserController {

    private final ObjectProperty<FontChooserModel> model = new SimpleObjectProperty<>();

    private final ObjectProperty<EventHandler<ActionEvent>> onAction = new SimpleObjectProperty<>();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea previewTextArea;

    @FXML
    private Label fontNameLabel;

    @FXML
    private ListView<FontCollection> collectionList;

    @FXML
    private ListView<FontFamily> familyList;

    @FXML
    private ListView<FontTypeface> typefaceList;

    public FontChooserModel getModel() {
        return model.get();
    }

    public void setModel(FontChooserModel value) {
        model.set(value);
    }

    public EventHandler<ActionEvent> getOnAction() {
        return onAction.get();
    }

    public void setOnAction(EventHandler<ActionEvent> value) {
        onAction.set(value);
    }

    @FXML
    void initialize() {
        assert previewTextArea != null : "fx:id=\"previewTextArea\" was not injected: check your FXML file 'FontChooser.fxml'.";
        assert fontNameLabel != null : "fx:id=\"fontNameLabel\" was not injected: check your FXML file 'FontChooser.fxml'.";
        assert collectionList != null : "fx:id=\"collectionList\" was not injected: check your FXML file 'FontChooser.fxml'.";
        assert familyList != null : "fx:id=\"familyList\" was not injected: check your FXML file 'FontChooser.fxml'.";
        assert typefaceList != null : "fx:id=\"typefaceList\" was not injected: check your FXML file 'FontChooser.fxml'.";

        final Resources labels = Resources.getResources("org.jhotdraw8.gui.Labels");

        collectionList.getSelectionModel().selectedItemProperty().addListener((o, oldv, newv) -> {
            familyList.setItems(newv == null ? null : newv.getFamilies());
        });
        familyList.getSelectionModel().selectedItemProperty().addListener((o, oldv, newv) -> {
            typefaceList.setItems(newv == null ? null : newv.getTypefaces());
            if (newv != null && !newv.getTypefaces().isEmpty()) {
                final ObservableList<FontTypeface> items = typefaceList.getItems();
                boolean found = false;
                for (int i = 0, n = items.size(); i < n; i++) {
                    if (items.get(i).isRegular()) {
                        typefaceList.getSelectionModel().select(i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    typefaceList.getSelectionModel().select(0);
                }
            }
        });
        typefaceList.getSelectionModel().selectedItemProperty().addListener((o, oldv, newv) -> {
            if (newv == null) {
                fontNameLabel.setText(labels.getString("FontChooser.nothingSelected"));
                previewTextArea.setFont(new Font("System Regular", 24));
            } else {
                fontNameLabel.setText(newv.getName());
                previewTextArea.setFont(new Font(newv.getName(), 24));
            }
        });

        final EventHandler<MouseEvent> onMouseHandler = evt -> {
            if (evt.getClickCount() == 2 && getOnAction() != null && getSelectedFontName() != null) {
                getOnAction().handle(new ActionEvent(evt.getSource(), evt.getTarget()));
            }
        };
        collectionList.setOnMousePressed(onMouseHandler);
        familyList.setOnMousePressed(onMouseHandler);
        typefaceList.setOnMousePressed(onMouseHandler);

        Preferences prefs = Preferences.userNodeForPackage(FontChooserController.class);
        previewTextArea.setText(prefs.get("fillerText", "Now is the time for all good men."));
        previewTextArea.textProperty().addListener((o, oldv, newv) -> {
            prefs.put("fillerText", newv);
        });

        model.addListener((o, oldv, newv) -> {
            if (newv != null) {
                collectionList.setItems(newv.getFontCollections());
            }
        });

        familyList.setCellFactory(lv -> {
            final TextFieldListCell<FontFamily> listCell = new TextFieldListCell<FontFamily>();
            listCell.setOnDragDetected(evt -> {
                Dragboard dragBoard = familyList.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                String familyNames
                        = familyList.getSelectionModel().getSelectedItems().stream().map(FontFamily::getName).collect(Collectors.joining("\n"));
                content.put(DataFormat.PLAIN_TEXT, familyNames);
                dragBoard.setDragView(listCell.snapshot(null, null));
                dragBoard.setContent(content);
                evt.consume();
            });
            return listCell;
        });

        collectionList.setCellFactory(lv -> {
            final TextFieldListCell<FontCollection> listCell = new TextFieldListCell<FontCollection>() {
                @Override
                public void updateItem(FontCollection item, boolean empty) {
                    super.updateItem(item, empty);
                    setEditable(item != null && !item.isSmartCollection());
                }

            };
            listCell.setConverter(new StringConverter<FontCollection>() {
                @Override
                public FontCollection fromString(String string) {
                    final FontCollection item = listCell.getItem();
                    item.setName(string);
                    return item;
                }

                @Override
                public String toString(FontCollection item) {
                    return (item.isSmartCollection()) ? item.getName() + "•" : item.getName();
                }

            });
            listCell.setOnDragOver(evt -> {
                if ((listCell.getItem() == null || !listCell.getItem().isSmartCollection())
                        && evt.getDragboard().hasString()) {
                    evt.acceptTransferModes(TransferMode.COPY);
                }
                evt.consume();
            });

            listCell.setOnDragDropped(evt -> {
                boolean success = false;
                if ((listCell.getItem() == null || !listCell.getItem().isSmartCollection())
                        && evt.getDragboard().hasString()) {
                    String droppedString = evt.getDragboard().getString();
                    addFamiliesToCollection(listCell.getItem(),
                            droppedString.split("\n")
                    );
                    success = true;
                }
                evt.setDropCompleted(success);
                evt.consume();
            });
            return listCell;
        });

        familyList.setOnKeyReleased(evt -> {
            if (evt.getCode() == KeyCode.DELETE) {
                FontCollection fontCollection = collectionList.getSelectionModel().getSelectedItem();
                if (!fontCollection.isSmartCollection()) {
                    fontCollection.getFamilies().remove(familyList.getSelectionModel().getSelectedItem());
                }
                evt.consume();
            }
        });
        collectionList.setOnKeyReleased(evt -> {
            if (evt.getCode() == KeyCode.DELETE) {
                FontCollection fontCollection = collectionList.getSelectionModel().getSelectedItem();
                if (!fontCollection.isSmartCollection()) {
                    collectionList.getItems().remove(collectionList.getSelectionModel().getSelectedIndex());
                }
                evt.consume();
            }
        });

    }

    private void addFamiliesToCollection(FontCollection collection, String[] familyNames) {
        final FontChooserModel model = getModel();
        FontCollection allFonts = model.getAllFonts();
        if (collection == null) {
            final Resources labels = Resources.getResources("org.jhotdraw8.gui.Labels");
            collection = new FontCollection(labels.getString("FontCollection.unnamed"), Collections.emptyList());
            model.getFontCollections().add(collection);
        }
        if (collection.isSmartCollection()) {
            return;
        }
        final ObservableList<FontFamily> existing = collection.getFamilies();
        final ArrayList<FontFamily> collected = DefaultFontChooserModelFactory.collectFamiliesNamed(allFonts.getFamilies(), familyNames);
        for (FontFamily family : collected) {
            if (!existing.contains(family)) {
                existing.add(family);
            }
        }
        existing.sort(Comparator.comparing(FontFamily::getName));
    }

    public ObjectProperty<FontChooserModel> modelProperty() {
        return model;
    }

    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }

    public void selectFontName(String fontName) {
        final ObservableList<FontCollection> collections = collectionList.getItems();
        for (int i = 0, n = collections.size(); i < n; i++) {
            final FontCollection fontCollection = collections.get(i);
            final ObservableList<FontFamily> families = fontCollection.getFamilies();
            for (int j = 0, m = families.size(); j < m; j++) {
                final FontFamily fontFamily = families.get(j);
                final ObservableList<FontTypeface> typefaces = fontFamily.getTypefaces();
                for (int k = 0, p = typefaces.size(); k < p; k++) {
                    final FontTypeface fontTypeface = typefaces.get(k);
                    if (fontTypeface.getName().equals(fontName)) {
                        collectionList.getSelectionModel().select(i);
                        familyList.getSelectionModel().select(j);
                        typefaceList.getSelectionModel().select(k);
                        break;
                    }
                }
            }
        }
    }

    public String getSelectedFontName() {
        FontTypeface typeface = typefaceList == null ? null : typefaceList.getSelectionModel().getSelectedItem();
        return typeface == null ? null : typeface.getName();
    }
}

/* @(#)Resources.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.util;

import java.io.Serializable;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Menu;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import org.jhotdraw8.app.action.Action;
import java.util.ResourceBundle;

/**
 * This is a convenience wrapper for accessing resources stored in a
 * ResourceBundle.
 * <p>
 * A resources object may reference a parent resources object using the resource
 * key "parent".
 * <p>
 * <b>Placeholders</b><br>
 * On top of the functionality provided by ResourceBundle, a property value can
 * include text from another property, by specifying the desired property name
 * and format type between <code>"${"</code> and <code>"}"</code>.
 * <p>
 * For example, if there is a {@code "imagedir"} property with the value
 * {@code "/org/jhotdraw8/undo/images"}, then this could be used in an attribute
 * like this: <code>${imagedir}/editUndo.png</code>. This is resolved at
 * run-time as {@code /org/jhotdraw8/undo/images/editUndo.png}.
 * <p>
 * Property names in placeholders can contain modifiers. Modifiers are written
 * between @code "[$"} and {@code "]"}. Each modifier has a fallback chain.
 * <p>
 * For example, if the property name modifier {@code "os"} has the value "win",
 * and its fallback chain is {@code "mac","default"}, then the property name
 * <code>${preferences.text.[$os]}</code> is first evaluted to {@code
 * preferences.text.win}, and - if no property with this name exists - it is
 * evaluated to {@code preferences.text.mac}, and then to
 * {@code preferences.text.default}.
 * <p>
 * The property name modifier "os" is defined by default. It can assume the
 * values "win", "mac" and "other". Its fallback chain is "default".
 * <p>
 * The format type can be optinally specified after a comma. The following
 * format types are supported:
 * <ul>
 * <li>{@code string} This is the default format.</li>
 * <li>{@code accelerator} This format replaces all occurences of the keywords
 * shift, control, ctrl, meta, alt, altGraph by getProperties which start with
 * {@code accelerator.}. For example, shift is replaced by
 * {@code accelerator.shift}.
 * </li>
 * </ul>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class Resources extends ResourceBundle implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final HashSet<String> acceleratorKeys = new HashSet<String>(
            Arrays.asList(new String[]{
        "shift", "control", "ctrl", "meta", "alt", "altGraph"
    }));
    /**
     * The wrapped resource bundle.
     */
    private transient ResourceBundle resource;
    /**
     * The locale.
     */
    private final Locale locale;
    /**
     * The base class
     */
    private Class<?> baseClass = getClass();
    /**
     * The base name of the resource bundle.
     */
    private final String baseName;
    /**
     * The global verbose property.
     */
    private static boolean isVerbose = false;
    /**
     * The global map of property name modifiers. The key of this map is the
     * name of the property name modifier, the value of this map is a fallback
     * chain.
     */
    private static HashMap<String, String[]> propertyNameModifiers = new HashMap<String, String[]>();

    /**
     * The parent resource object.
     */
    private final Resources parent;

    /**
     * List of decoders. The first decoder which can decode a resource value is
     * will be used to convert the resource value to an object.
     */
    private static List<ResourceDecoder> decoders = new ArrayList<>();

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        String os;
        if (osName.startsWith("mac os x")) {
            os = "mac";
        } else if (osName.startsWith("windows")) {
            os = "win";
        } else {
            os = "other";
        }
        propertyNameModifiers.put("os", new String[]{os, "default"});
    }

    /**
     * Creates a new ResouceBundleUtil which wraps the provided resource bundle.
     *
     * @param baseName the base name
     * @param locale the locale
     */
    public Resources(String baseName, Locale locale) {
        this.locale = locale;
        this.baseName = baseName;
        this.resource = ResourceBundle.getBundle(baseName, locale);

        Resources potentialParent = null;
        try {
            String parentBaseName = this.resource.getString("parent");
            if (parentBaseName != null && !Objects.equals(baseName, parentBaseName)) {
                potentialParent = new Resources(parentBaseName, locale);
            }
        } catch (MissingResourceException e) {

        }
        this.parent = potentialParent;
    }

    /**
     * Returns the wrapped resource bundle.
     *
     * @return The wrapped resource bundle.
     */
    public ResourceBundle getWrappedBundle() {
        return resource;
    }

    /**
     * Get a String from the ResourceBundle.
     * <br>Convenience method to save casting.
     *
     * @param key The key of the property.
     * @return The value of the property. Returns the key if the property is
     * missing. / public String getString(String key) { try { String value =
     * getStringRecursive(key); // System.out.println("Resources "+baseName+"
     * get("+key+"):"+value); return value; } catch (MissingResourceException e)
     * { // System.out.println("Resources "+baseName+"
     * get("+key+"):***MISSING***"); if (isVerbose) {
     * System.err.println("Warning Resources[" + baseName + "] \"" + key + "\"
     * not found."); //e.printStackTrace(); } return key; } }
     */
    /**
     * Recursive part of the getString method.
     *
     * @param key
     * @throws java.util.MissingResourceException
     */
    private String getStringRecursive(String key) throws MissingResourceException {
        try {
            String value;
            try {
                value = resource.getString(key);
            } catch (MissingResourceException e) {
                if (parent != null) {
                    value = parent.getStringRecursive(key);
                } else {
                    value = null;
                }

                if (value == null) {
                    if (isVerbose) {
                        System.err.println("Warning Resources[" + baseName + "] \"" + key + "\" not found.");
                        //e.printStackTrace();
                    }
                    return null;
                }
            }

            // Substitute placeholders in the value
            for (int p1 = value.indexOf("${"); p1 != -1; p1 = value.indexOf("${")) {
                int p2 = value.indexOf('}', p1 + 2);
                if (p2 == -1) {
                    break;
                }

                String placeholderKey = value.substring(p1 + 2, p2);
                String placeholderFormat;
                int p3 = placeholderKey.indexOf(',');
                if (p3 != -1) {
                    placeholderFormat = placeholderKey.substring(p3 + 1);
                    placeholderKey = placeholderKey.substring(0, p3);
                } else {
                    placeholderFormat = "string";
                }
                ArrayList<String> fallbackKeys = new ArrayList<String>();
                generateFallbackKeys(placeholderKey, fallbackKeys);

                String placeholderValue = null;
                for (String fk : fallbackKeys) {
                    try {
                        placeholderValue = getStringRecursive(fk);
                        break;
                    } catch (MissingResourceException e) {
                    }
                }
                if (placeholderValue == null) {
                    throw new MissingResourceException("Placeholder value for fallback keys \"" + fallbackKeys + "\" in key \"" + key + "\" not found in " + baseName, baseName, key);
                }

                // Do post-processing depending on placeholder format 
                if ("accelerator".equals(placeholderFormat)) {
                    // Localize the keywords shift, control, ctrl, meta, alt, altGraph
                    StringBuilder b = new StringBuilder();
                    for (String s : placeholderValue.split(" ")) {
                        if (acceleratorKeys.contains(s)) {
                            b.append(getString("accelerator." + s));
                        } else {
                            b.append(s);
                        }
                    }
                    placeholderValue = b.toString();
                }

                // Insert placeholder value into value
                value = value.substring(0, p1) + placeholderValue + value.substring(p2 + 1);
            }

            return value;
        } catch (MissingResourceException e) {
            if (isVerbose) {
                System.err.println("Warning Resources[" + baseName + "] \"" + key + "\" not found.");
                //e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Generates fallback keys by processing all property name modifiers in the
     * key.
     */
    private void generateFallbackKeys(String key, ArrayList<String> fallbackKeys) {
        int p1 = key.indexOf("[$");
        if (p1 == -1) {
            fallbackKeys.add(key);
        } else {
            int p2 = key.indexOf(']', p1 + 2);
            if (p2 == -1) {
                return;
            }
            String modifierKey = key.substring(p1 + 2, p2);
            String[] modifierValues = propertyNameModifiers.get(modifierKey);
            if (modifierValues == null) {
                modifierValues = new String[]{"default"};
            }
            for (String mv : modifierValues) {
                generateFallbackKeys(key.substring(0, p1) + mv + key.substring(p2 + 1), fallbackKeys);
            }
        }
    }

    /**
     * Returns a formatted string using javax.text.MessageFormat.
     *
     * @param key the key
     * @param arguments the arguments
     * @return formatted String
     */
    public String getFormatted(String key, Object... arguments) {
        return MessageFormat.format(getString(key), arguments);
    }

    /**
     * Returns a formatted string using java.util.Formatter().
     *
     * @param key the key
     * @param arguments the arguments
     * @return formatted String
     */
    public String format(String key, Object... arguments) {
        //return String.format(resource.getLocale(), getString(key), arguments);
        return new Formatter(resource.getLocale()).format(getString(key), arguments).toString();
    }

    /**
     * Get an Integer from the ResourceBundle.
     * <br>Convenience method to save casting.
     *
     * @param key The key of the property.
     * @return The value of the property. Returns -1 if the property is missing.
     */
    public Integer getInteger(String key) {
        try {
            return Integer.valueOf(getStringRecursive(key));
        } catch (MissingResourceException e) {
            if (isVerbose) {
                System.err.println("Warning Resources[" + baseName + "] \"" + key + "\" not found.");
                //e.printStackTrace();
            }
            return -1;
        }
    }

    /**
     * Get a small image icon from the ResourceBundle for use on a
     * {@code JMenuItem}.
     * <br>Convenience method .
     *
     * @param key The key of the property. This method appends ".smallIcon" to
     * the key.
     * @param baseClass the base class used to retrieve the image resource
     * @return The value of the property. Returns null if the property is
     * missing.
     */
    public Node getSmallIconProperty(String key, Class<?> baseClass) {
        return getIconProperty(key, ".smallIcon", baseClass);
    }

    /**
     * Get a large image icon from the ResourceBundle for use on a
     * {@code JButton}.
     * <br>Convenience method .
     *
     * @param key The key of the property. This method appends ".largeIcon" to
     * the key.
     * @param baseClass the base class used to retrieve the image resource
     * @return The value of the property. Returns null if the property is
     * missing.
     */
    public Node getLargeIconProperty(String key, Class<?> baseClass) {
        return getIconProperty(key, ".largeIcon", baseClass);
    }

    private Node getIconProperty(String key, String suffix, Class<?> baseClass) {
        try {
            String rsrcName = getStringRecursive(key + suffix);
            if ("".equals(rsrcName) || rsrcName == null) {
                return null;
            }

            for (ResourceDecoder d : decoders) {
                if (d.canDecodeValue(key, rsrcName, Node.class)) {
                    return d.decode(key, rsrcName, Node.class, baseClass);
                }
            }

            URL url = baseClass.getResource(rsrcName);
            if (isVerbose && url == null) {
                System.err.println("Warning Resources[" + baseName + "].getIconProperty \"" + key + suffix + "\" resource:" + rsrcName + " not found.");
            }
            return (url == null) ? null : new ImageView(url.toString());
        } catch (MissingResourceException e) {
            if (isVerbose) {
                System.err.println("Warning Resources[" + baseName + "].getIconProperty \"" + key + suffix + "\" not found.");
                //e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Get a Mnemonic from the ResourceBundle.
     * <br>Convenience method.
     *
     * @param key The key of the property.
     * @return The first char of the value of the property. Returns '\0' if the
     * property is missing.
     */
    public char getMnemonic(String key) {
        String s = getStringRecursive(key);
        return (s == null || s.length() == 0) ? '\0' : s.charAt(0);
    }

    /**
     * Gets a char for a JavaBeans "mnemonic" property from the ResourceBundle.
     * <br>Convenience method.
     *
     * @param key The key of the property. This method appends ".mnemonic" to
     * the key.
     * @return The first char of the value of the property. Returns '\0' if the
     * property is missing.
     */
    public KeyCombination getMnemonicProperty(String key) {
        String s;
        try {
            s = getStringRecursive(key + ".mnemonic");
        } catch (MissingResourceException e) {
            if (isVerbose) {
                System.err.println("Warning Resources[" + baseName + "] \"" + key + ".mnemonic\" not found.");
                //e.printStackTrace();
            }
            s = null;
        }
        return (s == null || s.length() == 0) ? null : KeyCombination.valueOf(s);
    }

    /**
     * Get a String for a JavaBeans "toolTipText" property from the
     * ResourceBundle.
     * <br>Convenience method.
     *
     * @param key The key of the property. This method appends ".toolTipText" to
     * the key.
     * @return The ToolTip. Returns null if no tooltip is defined.
     */
    public String getToolTipTextProperty(String key) {
        try {
            String value = getStringRecursive(key + ".toolTipText");
            return value;
        } catch (MissingResourceException e) {
            if (isVerbose) {
                System.err.println("Warning Resources[" + baseName + "] \"" + key + ".toolTipText\" not found.");
                //e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Get a String for a JavaBeans "text" property from the ResourceBundle.
     * <br>Convenience method.
     *
     * @param key The key of the property. This method appends ".text" to the
     * key.
     * @return The ToolTip. Returns null if no tooltip is defined.
     */
    public String getTextProperty(String key) {
        try {
            String value = getStringRecursive(key + ".text");
            return value;
        } catch (MissingResourceException e) {
            if (isVerbose) {
                System.err.println("Warning Resources[" + baseName + "] \"" + key + ".text\" not found.");
                //e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Translate a String defining a {@code javax.swing.KeyStroke} into a String
     * for {@code javafx.input.KeyCombination}.
     *
     * @param s The KeyStroke String
     * @return The KeyCombination String
     */
    protected String translateKeyStrokeToKeyCombination(String s) {
        if (s != null) {
            s = s.replace("ctrl ", "Ctrl+");
            s = s.replace("meta ", "Meta+");
            s = s.replace("alt ", "Alt+");
            s = s.replace("shift ", "Shift+");
        }
        return s;
    }

    /**
     * Get a KeyStroke from the ResourceBundle.
     * <BR>Convenience method.
     *
     * @param key The key of the property.
     * @return <code>javax.swing.KeyStroke.getKeyStroke(value)</code>. Returns
     * null if the property is missing.
     */
    public KeyCombination getKeyCombination(String key) {
        KeyCombination ks = null;
        String s = getStringRecursive(key);
        try {
            ks = (s == null || s.isEmpty()) ? (KeyCombination) null : KeyCombination.valueOf(translateKeyStrokeToKeyCombination(s));
        } catch (NoSuchElementException | StringIndexOutOfBoundsException e) {
            throw new InternalError(key + "=" + s, e);
        }
        return ks;
    }

    /**
     * Gets a KeyStroke for a JavaBeans "accelerator" property from the
     * ResourceBundle.
     * <BR>Convenience method.
     *
     * @param key The key of the property. This method adds ".accelerator" to
     * the key.
     * @return <code>javax.swing.KeyStroke.getKeyStroke(value)</code>. Returns
     * null if the property is missing.
     */
    public KeyCombination getAcceleratorProperty(String key) {
        return getKeyCombination(key + ".accelerator");
    }

    /**
     * Get the appropriate ResourceBundle subclass.
     *
     * @param baseName the base name
     * @return the resource bundle
     * @see java.util.ResourceBundle
     */
    public static Resources getResources(String baseName)
            throws MissingResourceException {
        return getResources(baseName, LocaleUtil.getDefault());
    }

    public void setBaseClass(Class<?> baseClass) {
        this.baseClass = baseClass;
    }

    public Class<?> getBaseClass() {
        return baseClass;
    }

    public void configureAction(Action action, String argument) {
        configureAction(action, argument, getBaseClass());
    }

    public void configureAction(Action action, String argument, Class<?> baseClass) {
        action.set(Action.LABEL, getTextProperty(argument));
        String shortDescription = getToolTipTextProperty(argument);
        if (shortDescription != null && shortDescription.length() > 0) {
            action.set(Action.SHORT_DESCRIPTION, shortDescription);
        }
        action.set(Action.ACCELERATOR_KEY, getAcceleratorProperty(argument));
        action.set(Action.MNEMONIC_KEY, getMnemonicProperty(argument));
        action.set(Action.SMALL_ICON, getSmallIconProperty(argument, baseClass));
        action.set(Action.LARGE_ICON_KEY, getLargeIconProperty(argument, baseClass));
    }

    public void configureButton(ButtonBase button, String argument) {
        configureButton(button, argument, getBaseClass());
    }

    public void configureButton(ButtonBase button, String argument, Class<?> baseClass) {
        button.setText(getTextProperty(argument));
        //button.setACCELERATOR_KEY, getAcceleratorProperty(argument));
        //action.putValue(Action.MNEMONIC_KEY, new Integer(getMnemonicProperty(argument)));
        button.setGraphic(getLargeIconProperty(argument, baseClass));
        button.setTooltip(new Tooltip(getToolTipTextProperty(argument)));
    }

    public void configureToolBarButton(ButtonBase button, String argument) {
        configureToolBarButton(button, argument, getBaseClass());
    }

    public void configureToolBarButton(ButtonBase button, String argument, Class<?> baseClass) {
        Node icon = getLargeIconProperty(argument, baseClass);
        if (icon != null) {
            button.setGraphic(getLargeIconProperty(argument, baseClass));
            button.setText(null);
        } else {
            button.setGraphic(null);
            button.setText(getTextProperty(argument));
        }
        button.setTooltip(new Tooltip(getToolTipTextProperty(argument)));
    }

    /**
     * Configures a menu item with a text, an accelerator, a mnemonic and a menu
     * icon.
     *
     * @param menu the menu
     * @param argument the argument
     */
    public void configureMenu(Menu menu, String argument) {
        menu.setText(getTextProperty(argument));
        menu.setText(getTextProperty(argument));
        menu.setAccelerator(getAcceleratorProperty(argument));
        menu.setGraphic(getSmallIconProperty(argument, baseClass));
    }

    /**
     * Get the appropriate ResourceBundle subclass.
     *
     * @param baseName the base name
     * @param locale the locale
     * @return the resource bundle
     * @see java.util.ResourceBundle
     */
    public static Resources getResources(String baseName, Locale locale)
            throws MissingResourceException {
        Resources r;
        r = new Resources(baseName, locale);
        return r;
    }

    @Override
    public String toString() {
        return super.toString() + "[" + baseName + ", " + resource + "]";
    }

    public static void setVerbose(boolean newValue) {
        isVerbose = newValue;
    }

    public static boolean isVerbose() {
        return isVerbose;
    }

    /**
     * Puts a property name modifier along with a fallback chain.
     *
     * @param name The name of the modifier.
     * @param fallbackChain The fallback chain of the modifier.
     */
    public static void putPropertyNameModifier(String name, String... fallbackChain) {
        propertyNameModifiers.put(name, fallbackChain);
    }

    /**
     * Removes a property name modifier.
     *
     * @param name The name of the modifier.
     */
    public static void removePropertyNameModifier(String name) {
        propertyNameModifiers.remove(name);
    }

    @Override
    protected Object handleGetObject(String key) {
        Object obj = null;
        try {
            obj = resource.getObject(key);
        } catch (MissingResourceException e) {
            if (parent != null) {
                return parent.getObject(key);
            } else {
                throw e;
            }
        }
        if (obj instanceof String) {
            obj = getStringRecursive(key);
        }
        return obj;
    }

    @Override
    public Enumeration<String> getKeys() {
        return resource.getKeys();
    }

    /**
     * Adds a decoder.
     *
     * @param decoder the resource decoder
     */
    public static void addDecoder(ResourceDecoder decoder) {
        decoders.add(decoder);
    }

    /**
     * Removes a decoder.
     *
     * @param decoder the resource decoder
     */
    public static void removeDecoder(ResourceDecoder decoder) {
        decoders.remove(decoder);
    }
}

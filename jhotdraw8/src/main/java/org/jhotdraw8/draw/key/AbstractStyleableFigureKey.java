/* @(#)NullableObjectFigureKey.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.key;

import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.collection.ObjectKey;
import org.jhotdraw8.styleable.ReadableStyleableMapAccessor;

/**
 * NullableObjectFigureKey.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class AbstractStyleableFigureKey<T> extends ObjectKey<T> implements FigureKey<T> {

    final static long serialVersionUID = 1L;

    private final DirtyMask dirtyMask;

    /**
     * Creates a new instance with the specified name, type token class, default
     * value null, and allowing null values.
     *
     * @param key       The name of the name.
     * @param clazz     The type of the value.
     * @param dirtyMask the dirty mask
     */
    public AbstractStyleableFigureKey(String key, Class<T> clazz, DirtyMask dirtyMask) {
        this(key, clazz, null, dirtyMask, null);
    }

    /**
     * Creates a new instance with the specified name, type token class, default
     * value.
     *
     * @param key          The name of the name.
     * @param clazz        The type of the value.
     * @param defaultValue The default value.
     * @param dirtyMask    the dirty bits
     */
    public AbstractStyleableFigureKey(String key, Class<T> clazz, DirtyMask dirtyMask, T defaultValue) {
        this(key, clazz, null, dirtyMask, defaultValue);
    }

    /**
     * Creates a new instance with the specified name, type token class, default
     * value, and allowing null values.
     *
     * @param name           The name of the key.
     * @param clazz          The type of the value.
     * @param typeParameters The type parameters of the class. Specify "" if no
     *                       type parameters are given. Otherwise specify them in arrow brackets.
     * @param defaultValue   The default value.
     * @param dirtyMask      the dirty bits
     */
    public AbstractStyleableFigureKey(String name, Class<?> clazz, Class<?>[] typeParameters, DirtyMask dirtyMask, T defaultValue) {
        this(null, name, clazz, typeParameters, true, dirtyMask, defaultValue);
    }

    /**
     * Creates a new instance with the specified name, type token class, default
     * value, and allowing or disallowing null values.
     *
     * @param namespace The namespace
     * @param name         The name of the key.
     * @param clazz        The type of the value.
     * @param isNullable   Whether the value may be set to null
     * @param dirtyMask    the dirty bits
     * @param defaultValue The default value.
     */
    public AbstractStyleableFigureKey(@Nullable String namespace, String name, Class<?> clazz, boolean isNullable, DirtyMask dirtyMask, T defaultValue) {
        this(namespace, name, clazz, null, isNullable, dirtyMask, defaultValue);
    }

    /**
     * Creates a new instance with the specified name, type token class, default
     * value, and allowing or disallowing null values.
     *
     * @param namespace The namespace
     * @param name           The name of the key.
     * @param clazz          The type of the value.
     * @param typeParameters The type parameters of the class. Specify "" if no
     *                       type parameters are given. Otherwise specify them in arrow brackets.
     * @param isNullable     Whether the value may be set to null
     * @param dirtyMask      the dirty bits
     * @param defaultValue   The default value.
     */
    public AbstractStyleableFigureKey(@Nullable String namespace, String name, Class<?> clazz, Class<?>[] typeParameters, boolean isNullable, DirtyMask dirtyMask, T defaultValue) {
        super(name, clazz, typeParameters, isNullable, defaultValue);
        this.dirtyMask = dirtyMask;
        this.cssName = ReadableStyleableMapAccessor.toCssName(name);
        this.namespace = namespace;
    }

    @Nonnull
    private final String cssName;
    @Nullable
    private final String namespace;

    public DirtyMask getDirtyMask() {
        return dirtyMask;
    }

    @Nonnull
    public String getCssName() {
        return cssName;
    }

    @Nullable
    public String getCssNamespace() {
        return namespace;
    }


}

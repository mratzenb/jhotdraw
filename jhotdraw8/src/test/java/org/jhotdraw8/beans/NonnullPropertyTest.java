/* @(#)NonnullPropertyTest.java
 * Copyright (c) 2015 The authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.beans;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author wr
 */
public class NonnullPropertyTest {

    @Test
    public void testBind() {
        double minv = 0;
        double maxv = 1;

        NonnullProperty<String> p1 = new NonnullProperty<>(null, null, "hello");
        ObjectProperty<String> p2 = new SimpleObjectProperty<>(null);
        p1.addListener((o, oldv, newv) -> {
            Objects.requireNonNull(newv);
        });

        // must not set a null value
        p1.set(null);
        String s = p1.get();
        assert s != null;

    }
}

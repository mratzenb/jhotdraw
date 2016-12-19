/* @(#)StyleablePropertyMapNGTest.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw.css;

import org.jhotdraw8.styleable.StyleableMap;
import java.util.Map;
import javafx.css.StyleOrigin;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author werni
 */
public class StyleablePropertyMapNGTest {
    
    public StyleablePropertyMapNGTest() {
    }

    

    @Test
    public void testPut() {
        StyleableMap<String,String> map = new StyleableMap<>();
        
        map.put("1","user");
        map.put(StyleOrigin.AUTHOR,"1","author");
        
        assertEquals(map.size(), 1);
        assertEquals(map.get("1"),"user");
        assertEquals(map.get(StyleOrigin.USER,"1"),"user");
        assertEquals(map.get(StyleOrigin.AUTHOR,"1"),"author");
        assertEquals(map.get(StyleOrigin.AUTHOR,"1"),"author");
    }
    @Test
    public void testIterator1() {
        StyleableMap<String,String> map = new StyleableMap<>();
        
        map.put("1","user1");
        map.put("2","user2");
        map.put("3","user3");
        map.put("4","user4");
        
        map.put(StyleOrigin.AUTHOR,"2","author2");
        map.put(StyleOrigin.AUTHOR,"3","author3");
        
        for (Map.Entry<String,String> e:map.entrySet()) {
            System.out.println(e);
        }
        System.out.println("---");
    }
    @Test
    public void testIterator2() {
        StyleableMap<String,String> map = new StyleableMap<>();
        
        map.put(StyleOrigin.AUTHOR,"1","author1");
        map.put(StyleOrigin.AUTHOR,"2","author2");
        map.put(StyleOrigin.AUTHOR,"3","author3");
        map.put(StyleOrigin.AUTHOR,"4","author4");
        
        map.put(StyleOrigin.USER,"2","user2");
        map.put(StyleOrigin.USER,"3","user3");
        
        for (Map.Entry<String,String> e:map.entrySet()) {
            System.out.println(e);
        }
        System.out.println("---");
    }
    
}

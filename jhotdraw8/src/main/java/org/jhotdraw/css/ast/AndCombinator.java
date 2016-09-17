/* @(#)AndCombinator.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw.css.ast;

import org.jhotdraw.css.SelectorModel;

/**
 * An "and combinator" matches an element if both its first selector and its
 * second selector match the element.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class AndCombinator extends Combinator {

    public AndCombinator(SimpleSelector simpleSelector, Selector selector) {
        super(simpleSelector, selector);
    }

    @Override
    public String toString() {
        return "(" + firstSelector + " && " + secondSelector + ")";
    }

    @Override
    public <T> MatchResult<T> match(SelectorModel<T> model, T element) {
      MatchResult<T> firstResult = firstSelector.match(model, element);
      MatchResult<T> secondResult = secondSelector.match(model, element);
return (firstResult != null && secondResult!=null) ? new MatchResult<>(element, this) : null;
    }

  @Override
  public int getSpecificity() {
    return firstSelector.getSpecificity()+secondSelector.getSpecificity();
  }
}

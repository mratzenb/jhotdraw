/* @(#)Rectangle2DConverter.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.css.text;

import org.jhotdraw8.css.CssPoint2D;
import org.jhotdraw8.css.CssSize;
import org.jhotdraw8.css.CssToken;
import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.css.CssTokenizer;
import org.jhotdraw8.io.IdFactory;

import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;
import java.io.IOException;
import java.text.ParseException;
import java.util.function.Consumer;

/**
 * Converts a {@code javafx.geometry.CssPoint2D} into a {@code String} and vice
 * versa.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class CssPoint2DConverter extends AbstractCssConverter<CssPoint2D> {
    private final boolean withSpace;
    private final boolean withComma;

    public CssPoint2DConverter(boolean nullable) {
        this(nullable, true, false);
    }

    public CssPoint2DConverter(boolean nullable, boolean withSpace, boolean withComma) {
        super(nullable);
        this.withSpace = withSpace || !withComma;
        this.withComma = withComma;
    }

    @Nonnull
    @Override
    public CssPoint2D parseNonnull(@Nonnull CssTokenizer tt, @Nullable IdFactory idFactory) throws ParseException, IOException {
        final CssSize x, y;
        x = parseDimension(tt,"x");
        tt.skipIfPresent(CssTokenType.TT_COMMA);
        y = parseDimension(tt,"y");

        return new CssPoint2D(x, y);
    }

    private CssSize parseDimension(CssTokenizer tt, String variable) throws ParseException, IOException {
        switch (tt.next()) {
            case CssTokenType.TT_NUMBER:
                return new CssSize(tt.currentNumber().doubleValue());
            case CssTokenType.TT_DIMENSION:
                return new CssSize(tt.currentNumber().doubleValue(),tt.currentString());
            case CssTokenType.TT_IDENT:
                switch (tt.currentStringNonnull()) {
                    case "NaN":
                        return new CssSize(Double.NaN);
                    case "INF":
                        return new CssSize(Double.POSITIVE_INFINITY);
                    case "-INF":
                        return new CssSize(Double.NEGATIVE_INFINITY);
                    default:
                        throw new ParseException(" ⟨CssPoint2D⟩: ⟨"+variable+"⟩ expected.",tt.getStartPosition());
                }
            default:
                throw new ParseException(" ⟨CssPoint2D⟩: ⟨"+variable+"⟩ expected.",tt.getStartPosition());
        }
    }

    @Override
    protected <TT extends CssPoint2D> void produceTokensNonnull(@Nonnull TT value, @Nullable IdFactory idFactory, @Nonnull Consumer<CssToken> out) {
        CssSize x = value.getX();
        out.accept(new CssToken(CssTokenType.TT_DIMENSION, x.getValue(), x.getUnits()));
        if (withComma) {
            out.accept(new CssToken(CssTokenType.TT_COMMA));
        }
        if (withSpace) {
            out.accept(new CssToken(CssTokenType.TT_S, " "));
        }
        CssSize y = value.getY();
        out.accept(new CssToken(CssTokenType.TT_DIMENSION, y.getValue(), y.getUnits()));
    }

    @Override
    public String getHelpText() {
        return "Format of ⟨CssPoint2D⟩: ⟨x⟩ ⟨y⟩";
    }
}

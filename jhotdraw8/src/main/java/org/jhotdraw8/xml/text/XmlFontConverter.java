/* @(#)XmlFontConverter.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.xml.text;

import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.text.ParseException;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;

import org.jhotdraw8.css.CssTokenType;
import org.jhotdraw8.css.StreamCssTokenizer;
import org.jhotdraw8.css.CssTokenizer;
import org.jhotdraw8.io.IdFactory;
import javafx.scene.text.Font;
import org.jhotdraw8.text.Converter;

/**
 * XmlFontConverter.
 * <p>
 * Parses the following EBNF from the
 * <a href="https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html">JavaFX
 * CSS Reference Guide</a>.
 * </p>
 * <pre>
 * Font := [FontStyle] [FontWeight] FontSize FontFamily ;
 * FontStyle := normal|italic|oblique;
 * FontWeight := normal|bold|bolder|lighter|100|200|300|400|500|600|700|800|900;
 * FontSize := Size;
 * FontFamily := Word|Quoted;
 * </pre>
 * <p>
 * FIXME currently only parses the Color production
 * </p>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class XmlFontConverter implements Converter<Font> {

    private final XmlNumberConverter doubleConverter = new XmlNumberConverter();

    @Override
    public void toString(@Nonnull Appendable out, IdFactory idFactory, @Nonnull Font font) throws IOException {
        double fontSize = font.getSize();
        String fontFamily = font.getFamily();
        FontPosture posture = FontPosture.REGULAR;
        FontWeight weight = FontWeight.NORMAL;
        switch (posture) {
            case ITALIC:
                out.append("italic");
                break;
            case REGULAR:
                out.append("normal");
                break;
            default:
                throw new InternalError("Unknown fontPosture:" + posture);
        }
        out.append(' ');
        switch (weight) {
            case NORMAL:
                out.append("normal");
                break;
            case BOLD:
                out.append("bold");
                break;
            default:
                out.append(Integer.toString(weight.getWeight()));
                break;
        }
        out.append(' ');
        doubleConverter.toString(out, fontSize);
        out.append(' ');
        if (fontFamily.contains(" ") || fontFamily.contains("\'") || fontFamily.contains("\"")) {
            out.append('\"');
            out.append(fontFamily.replace("\"", "\\\""));
            out.append('\"');
        } else {
            out.append(fontFamily);
        }
    }

    @Override
    public Font fromString(@Nullable CharBuffer buf, IdFactory idFactory) throws ParseException, IOException {
        // XXX should not use Css Tokenizer in XML!!
        CssTokenizer tt = new StreamCssTokenizer(new StringReader(buf.toString()));

        FontPosture fontPosture = FontPosture.REGULAR;
        FontWeight fontWeight = FontWeight.NORMAL;
        double fontSize = 12.0;
        String fontFamily = "System";

        // parse FontStyle
        if (tt.next() == CssTokenType.TT_IDENT) {
            switch (tt.currentString().toLowerCase()) {
                case "normal":
                    fontPosture = FontPosture.REGULAR;
                    break;
                case "italic":
                case "oblique":
                    fontPosture = FontPosture.ITALIC;
                    break;
                default:
                    tt.pushBack();
                    break;
            }
        } else {
            tt.pushBack();
        }

        // parse FontWeight
        boolean fontWeightConsumed = false;
        if (tt.next() == CssTokenType.TT_IDENT) {
            switch (tt.currentString().toLowerCase()) {
                case "normal":
                    fontWeight = FontWeight.NORMAL;
                    fontWeightConsumed = true;
                    break;
                case "bold":
                    fontWeight = FontWeight.BOLD;
                    fontWeightConsumed = true;
                    break;
                case "bolder":
                    // FIXME weight should be relative to parent font
                    fontWeight = FontWeight.BOLD;
                    fontWeightConsumed = true;
                    break;
                case "lighter":
                    // FIXME weight should be relative to parent font
                    fontWeight = FontWeight.LIGHT;
                    fontWeightConsumed = true;
                    break;
                default:
                    tt.pushBack();
                    break;
            }
        } else {
            tt.pushBack();
        }

        double fontWeightOrFontSize = 0.0;
        boolean fontWeightOrFontSizeConsumed = false;
        if (!fontWeightConsumed) {
            if (tt.next() == CssTokenType.TT_NUMBER) {
                fontWeightOrFontSize = tt.currentNumber().doubleValue();
                fontWeightOrFontSizeConsumed = true;
            } else {
                tt.pushBack();
            }
        }

        // parse FontSize
        if (tt.next() == CssTokenType.TT_NUMBER) {
            fontSize = tt.currentNumber().doubleValue();

            if (fontWeightOrFontSizeConsumed) {
                switch ((int) fontWeightOrFontSize) {
                    case 100:
                        fontWeight = FontWeight.THIN;
                        break;
                    case 200:
                        fontWeight = FontWeight.EXTRA_LIGHT;
                        break;
                    case 300:
                        fontWeight = FontWeight.LIGHT;
                        break;
                    case 400:
                        fontWeight = FontWeight.NORMAL;
                        break;
                    case 500:
                        fontWeight = FontWeight.MEDIUM;
                        break;
                    case 600:
                        fontWeight = FontWeight.SEMI_BOLD;
                        break;
                    case 700:
                        fontWeight = FontWeight.BOLD;
                        break;
                    case 800:
                        fontWeight = FontWeight.EXTRA_BOLD;
                        break;
                    case 900:
                        fontWeight = FontWeight.BLACK;
                        break;
                    default:
                        throw new ParseException("illegal font weight " + fontWeightOrFontSize, buf.position() + tt.getStartPosition());
                }
            }

        } else if (fontWeightOrFontSizeConsumed) {
            tt.pushBack();
            fontSize = fontWeightOrFontSize;
        } else {
            tt.pushBack();
        }

        if (tt.next() == CssTokenType.TT_IDENT || tt.current() == CssTokenType.TT_STRING) {
            fontFamily = tt.currentString();
            // consume buffer
            buf.position(buf.limit());
        } else {
            throw new ParseException("font family expected", buf.position() + tt.getStartPosition());
        }

        Font font = Font.font(fontFamily, fontWeight, fontPosture, fontSize);
        if (font == null) {
            font = Font.font(null, fontWeight, fontPosture, fontSize);
        }
        return font;
    }

    @Nullable
    @Override
    public Font getDefaultValue() {
        return null;
    }
}

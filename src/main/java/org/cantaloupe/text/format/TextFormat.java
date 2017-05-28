package org.cantaloupe.text.format;

public class TextFormat {
    private TextColor color = TextColors.NONE;
    private TextStyle style = TextStyles.NONE;

    private TextFormat(TextColor color, TextStyle style) {
        this.color = color;
        this.style = style;
    }

    public static TextFormat of() {
        return new TextFormat(TextColors.NONE, TextStyles.NONE);
    }

    public static TextFormat of(TextColor color) {
        return new TextFormat(color, TextStyles.NONE);
    }

    public static TextFormat of(TextStyle style) {
        return new TextFormat(TextColors.NONE, style);
    }

    public static TextFormat of(TextColor color, TextStyle style) {
        return new TextFormat(color, style);
    }

    public void color(TextColor color) {
        this.color = color;
    }

    public void style(TextStyle style) {
        this.style = style;
    }

    public TextColor getColor() {
        return this.color;
    }

    public TextStyle getStyle() {
        return this.style;
    }
}
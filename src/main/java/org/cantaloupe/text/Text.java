package org.cantaloupe.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.cantaloupe.text.action.ClickAction;
import org.cantaloupe.text.action.HoverAction;
import org.cantaloupe.text.action.ShiftClickAction;
import org.cantaloupe.text.action.TextAction;
import org.cantaloupe.text.format.TextColor;
import org.cantaloupe.text.format.TextFormat;
import org.cantaloupe.text.format.TextStyle;
import org.cantaloupe.text.format.TextStyles;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Text {
    private ArrayList<Text>          children        = null;
    private ArrayList<TextAction<?>> actions         = null;

    private TextComponent            component       = null;
    private String                   content         = null;
    private TextFormat               format          = null;

    private static final String      NEW_LINE_STRING = "\n";
    public static final Text         NEW_LINE        = Text.of(NEW_LINE_STRING);

    private Text(TextFormat format) {
        this.component = new TextComponent();
        this.component.setText("");

        this.children = new ArrayList<Text>();
        this.actions = new ArrayList<TextAction<?>>();

        this.format = format;
    }

    private Text(String content, TextFormat format) {
        this.component = new TextComponent();
        this.component.setText(content);

        this.children = new ArrayList<Text>();
        this.actions = new ArrayList<TextAction<?>>();

        this.content = content;
        this.format = format;
    }

    private Text(TextFormat format, String... contents) {
        this.component = new TextComponent();
        this.component.setText(contents[0]);

        this.children = new ArrayList<Text>();
        this.actions = new ArrayList<TextAction<?>>();

        this.content = contents[0];
        this.format = format;

        for (String content : Arrays.copyOfRange(contents, 1, contents.length)) {
            this.addChild(Text.of(content, format));
        }
    }

    private Text(BaseComponent[] components) {
        this.component = (TextComponent) components[0];
        this.children = new ArrayList<Text>();
        this.actions = new ArrayList<TextAction<?>>();

        this.content = this.component.getText();
        this.format = TextFormat.of();

        if (this.component.isBold()) {
            this.style(TextStyles.BOLD);
        } else if (this.component.isItalic()) {
            this.style(TextStyles.ITALIC);
        } else if (this.component.isObfuscated()) {
            this.style(TextStyles.OBFUSCATED);
        } else if (this.component.isStrikethrough()) {
            this.style(TextStyles.STRIKETHROUGH);
        } else if (this.component.isUnderlined()) {
            this.style(TextStyles.UNDERLINE);
        } else {
            this.style(TextStyles.NONE);
        }

        if (components.length > 1) {
            for (BaseComponent component : Arrays.copyOfRange(components, 1, components.length)) {
                this.addChild(new Text(component));
            }
        }
    }

    private Text(BaseComponent component) {
        this.component = (TextComponent) component;
        this.children = new ArrayList<Text>();
        this.actions = new ArrayList<TextAction<?>>();

        this.content = this.component.getText();
        this.format = TextFormat.of();

        if (component.isBold()) {
            this.style(TextStyles.BOLD);
        } else if (component.isItalic()) {
            this.style(TextStyles.ITALIC);
        } else if (component.isObfuscated()) {
            this.style(TextStyles.OBFUSCATED);
        } else if (component.isStrikethrough()) {
            this.style(TextStyles.STRIKETHROUGH);
        } else if (component.isUnderlined()) {
            this.style(TextStyles.UNDERLINE);
        } else {
            this.style(TextStyles.NONE);
        }
    }

    public static Text of() {
        return new Text(TextFormat.of());
    }

    public static Text of(String content) {
        return new Text(content, TextFormat.of());
    }

    public static Text of(String... contents) {
        return new Text(TextFormat.of(), contents);
    }

    public static Text of(String content, TextFormat format) {
        return new Text(content, format);
    }

    public static Text of(TextFormat format, String... contents) {
        return new Text(format, contents);
    }

    public static Text of(String content, TextColor color) {
        return new Text(content, TextFormat.of(color));
    }

    public static Text of(TextColor color, String... contents) {
        return new Text(TextFormat.of(color), contents);
    }

    public static Text of(String content, TextStyle style) {
        return new Text(content, TextFormat.of(style));
    }

    public static Text of(TextStyle style, String... contents) {
        return new Text(TextFormat.of(style), contents);
    }

    public Text onClick(ClickAction<?> action) {
        if (this.actions == null) {
            this.actions = new ArrayList<TextAction<?>>();
        }

        this.actions.add(action);

        if (action instanceof ClickAction.RunCommand) {
            this.component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, (String) action.getResult()));
        } else if (action instanceof ClickAction.SuggestCommand) {
            this.component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, (String) action.getResult()));
        } else if (action instanceof ClickAction.ChangePage) {
            this.component.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "" + action.getResult()));
        } else if (action instanceof ClickAction.OpenUrl) {
            this.component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, (String) action.getResult()));
        }

        return this;
    }

    public Text onHover(HoverAction<?> action) {
        if (this.actions == null) {
            this.actions = new ArrayList<TextAction<?>>();
        }

        this.actions.add(action);

        if (action instanceof HoverAction.ShowItem) {
            // this.component.setHoverEvent(
            // new HoverEvent(HoverEvent.Action.SHOW_ITEM, (BaseComponent[])
            // action.getResult()));
        } else if (action instanceof HoverAction.ShowItem) {
            this.component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (BaseComponent[]) action.getResult()));
        }

        return this;
    }

    public Text onShiftHover(ShiftClickAction<?> action) {
        if (this.actions == null) {
            this.actions = new ArrayList<TextAction<?>>();
        }

        this.actions.add(action);

        if (action instanceof ShiftClickAction.InsertText) {
            this.component.setInsertion((String) action.getResult());
        }

        return this;
    }

    public Text addChild(Text child) {
        this.children.add(child);
        this.component.addExtra(child.getComponent());

        return this;
    }

    public Text content(String content) {
        this.content = content;
        this.component.setText(content);

        return this;
    }

    public Text color(TextColor color) {
        this.format.color(color);
        this.component.setColor(color.getHandle());

        return this;
    }

    public Text style(TextStyle style) {
        this.format.style(style);

        if (style.getHandle() != null) {
            switch (style.getHandle()) {
                case MAGIC:
                    this.component.setObfuscated(true);

                    break;
                case BOLD:
                    this.component.setBold(true);

                    break;
                case STRIKETHROUGH:
                    this.component.setStrikethrough(true);

                    break;
                case UNDERLINE:
                    this.component.setUnderlined(true);

                    break;
                case ITALIC:
                    this.component.setItalic(true);

                    break;
                case RESET:
                    this.component.setColor(ChatColor.WHITE);
                    this.component.setBold(false);
                    this.component.setObfuscated(false);
                    this.component.setItalic(false);
                    this.component.setUnderlined(false);
                    this.component.setStrikethrough(false);

                    break;
                default:
                    break;
            }
        } else {
            this.component.setBold(false);
            this.component.setObfuscated(false);
            this.component.setItalic(false);
            this.component.setUnderlined(false);
            this.component.setStrikethrough(false);
        }

        return this;
    }

    public Text format(TextFormat format) {
        this.format = format;
        this.component.setColor(format.getColor().getHandle());

        if (format.getStyle().getHandle() != null) {
            switch (format.getStyle().getHandle()) {
                case MAGIC:
                    this.component.setObfuscated(true);

                    break;
                case BOLD:
                    this.component.setBold(true);

                    break;
                case STRIKETHROUGH:
                    this.component.setStrikethrough(true);

                    break;
                case UNDERLINE:
                    this.component.setUnderlined(true);

                    break;
                case ITALIC:
                    this.component.setItalic(true);

                    break;
                case RESET:
                    this.component.setColor(ChatColor.WHITE);
                    this.component.setBold(false);
                    this.component.setObfuscated(false);
                    this.component.setItalic(false);
                    this.component.setUnderlined(false);
                    this.component.setStrikethrough(false);

                    break;
                default:
                    break;
            }
        } else {
            this.component.setBold(false);
            this.component.setObfuscated(false);
            this.component.setItalic(false);
            this.component.setUnderlined(false);
            this.component.setStrikethrough(false);
        }

        return this;
    }

    public Text append(Text... children) {
        for (Text child : children) {
            this.children.add(child);
            this.component.addExtra(child.getComponent());
        }

        return this;
    }

    public Text append(Collection<? extends Text> children) {
        for (Text child : children) {
            this.children.add(child);
            this.component.addExtra(child.getComponent());
        }

        return this;
    }

    public Text append(Iterable<? extends Text> children) {
        for (Text child : children) {
            this.children.add(child);
            this.component.addExtra(child.getComponent());
        }

        return this;
    }

    public Text append(Iterator<? extends Text> children) {
        while (children.hasNext()) {
            Text child = children.next();

            this.children.add(child);
            this.component.addExtra(child.getComponent());
        }

        return this;
    }

    public static Text fromLegacy(String content) {
        return new Text(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', content)));
    }

    public String toLegacy() {
        return this.component.toLegacyText();
    }

    public String toPlain() {
        return this.component.toPlainText();
    }

    public TextComponent getComponent() {
        return this.component;
    }

    public String getContent() {
        return this.content;
    }

    public TextFormat getFormat() {
        return this.format;
    }

    public TextColor getColor() {
        return this.format.getColor();
    }

    public TextStyle getStyle() {
        return this.format.getStyle();
    }

    public ArrayList<Text> getChildren() {
        return this.children;
    }

    public ArrayList<TextAction<?>> getActions() {
        return this.actions;
    }
}
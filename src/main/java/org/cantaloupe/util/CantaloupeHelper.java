package org.cantaloupe.util;

import org.cantaloupe.text.Text;

public class CantaloupeHelper {
	public static Text t(String key, Object... args) {
        return Text.of(String.format(key, args));
    }
}
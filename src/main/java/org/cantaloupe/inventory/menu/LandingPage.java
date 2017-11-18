package org.cantaloupe.inventory.menu;

import org.cantaloupe.text.Text;

/**
 * A class used to create a page.
 * 
 * @author Dylan Scheltens
 *
 */
public class LandingPage extends Page {
    protected LandingPage(Text name, int size) {
        super("landing", name, size, null);
    }

    /**
     * Creates and returns a new landing page.
     * 
     * @param name
     *            The name
     * @return The page
     */
    public static LandingPage of() {
        return new LandingPage(Text.of("Page"), 54);
    }

    /**
     * Creates and returns a new landing page.
     * 
     * @param name
     *            The name
     * @return The page
     */
    public static LandingPage of(Text name) {
        return new LandingPage(name, 54);
    }

    /**
     * Creates and returns a new landing page.
     * 
     * @param size
     *            The size
     * @return The page
     */
    public static LandingPage of(int size) {
        return new LandingPage(Text.of("Page"), size);
    }

    /**
     * Creates and returns a new landing page.
     * 
     * @param name
     *            The name
     * @param size
     *            The size
     * @return The page
     */
    public static LandingPage of(Text name, int size) {
        return new LandingPage(name, size);
    }
}
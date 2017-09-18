package org.cantaloupe.inject;

/**
 * A class used to distinguish between consumers in an injector.
 * 
 * @author Dylan
 *
 */
public class Scope {
    private String namespace = null;
    private String name      = null;

    private Scope(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Creates and returns a new scope.
     * 
     * @param namespace
     *            The namespace
     * @param name
     *            The name
     * @return The scope
     */
    public static Scope of(String namespace, String name) {
        return new Scope(namespace, name);
    }

    /**
     * Gets the namespace of the sound.
     * 
     * @return The namespace
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Gets the name of the sound.
     * 
     * @return The name
     */
    public String getName() {
        return this.name;
    }
}
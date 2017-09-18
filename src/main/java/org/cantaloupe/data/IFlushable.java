package org.cantaloupe.data;

/**
 * An interface implemented by a class with data printable to the console.
 * 
 * @author Dylan Scheltens
 *
 */
public interface IFlushable {
    /**
     * Flushes the object to console.
     */
    default public void flush() {
        System.out.println(this.toString());
    }
}
package org.cantaloupe.network.packet;

/**
 * An interface containing basic packet methods.
 * 
 * @author Dylan Scheltens
 *
 */
public interface IPacket {
    /**
     * Reads the packet from the incoming data.
     * 
     * @param data
     *            The data
     */
    public void read(Object data);

    /**
     * Writes the data to be sent.
     * 
     * @return The data
     */
    public Object write();

    /**
     * Gets the packet ID.
     * 
     * @return The packet ID
     */
    public byte getID();
}
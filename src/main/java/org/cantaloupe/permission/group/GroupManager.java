package org.cantaloupe.permission.group;

import java.util.HashMap;
import java.util.Optional;

import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;

/**
 * A class used to manage groups.
 * 
 * @author Dylan Scheltens
 *
 */
public class GroupManager {
    private static HashMap<String, Group> registeredGroups = new HashMap<String, Group>();

    /**
     * Registers a group to the group manager.
     * 
     * @param group
     *            The group
     */
    public static void registerGroup(Group group) {
        group.initialize();

        registeredGroups.put(group.getID(), group);
    }

    /**
     * Unregisters a group from the group manager.
     * 
     * @param ID
     *            The ID of a group
     */
    public static void unregisterGroup(String ID) {
        registeredGroups.remove(ID);
    }

    /**
     * Unregisters a group from the group manager.
     * 
     * @param group
     *            The group
     */
    public static void unregisterGroup(Group group) {
        registeredGroups.remove(group.getID());
    }

    /**
     * Gets a group from the group manager.
     * 
     * @param ID
     *            The ID of the group
     * @return An optional containing the group if it's present, an empty
     *         optional if not
     */
    public static Optional<Group> getGroup(String ID) {
        return Optional.ofNullable(registeredGroups.get(ID));
    }

    /**
     * Gets the prefix for a player.
     * 
     * @param player
     *            The player
     * @return The prefix
     */
    public static Text getPrefixFor(Player player) {
        Text prefix = Text.of();

        for (Group group : player.getGroups()) {
            if (group.showPrefix()) {
                prefix.addChild(Text.of("[").addChild(group.getPrefix()).addChild(Text.of("]")));
            }
        }

        return prefix;
    }
}
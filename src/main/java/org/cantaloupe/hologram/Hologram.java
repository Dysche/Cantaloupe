package org.cantaloupe.hologram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.cantaloupe.entity.EntityType;
import org.cantaloupe.entity.FakeEntity;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;
import org.cantaloupe.world.World;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;
import org.joml.Vector3d;

/**
 * A class used to create holograms.
 * 
 * @author Dylan Scheltens
 *
 */
public class Hologram extends WorldObject {
    private ImmutableLocation      location;
    private List<Text>             lines;
    private final List<FakeEntity> entities;
    private final List<Player>     players;

    private Hologram(ImmutableLocation location, List<Text> lines) {
        this.location = location;
        this.lines = lines;
        this.entities = new ArrayList<FakeEntity>();
        this.players = new ArrayList<Player>();
    }

    /**
     * Creates and returns a new builder.
     * 
     * @return The builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Places the hologram.
     */
    public void place() {
        this.location.getWorld().place(this);
    }

    /**
     * Removes the hologram.
     */
    public void remove() {
        this.location.getWorld().remove(this);
    }

    private void create() {
        int count = 0;

        for (Text line : this.lines) {
            this.entities.add(FakeEntity.builder()
                    .type(EntityType.ARMOR_STAND)
                    .world(this.location.getWorld())
                    .position(new Vector3d(this.location.getPosition().x + 0.5, (this.location.getPosition().y - 2.0) - (count * 0.25), this.location.getPosition().z + 0.5))
                    .customName(line)
                    .customNameVisible(true)
                    .invisible(true)
                    .build());

            count++;
        }
    }

    /**
     * Places the hologram for a player.
     * 
     * @param player
     *            The player
     */
    public void placeFor(Player player) {
        if (!this.isPlacedFor(player)) {
            for (FakeEntity entity : this.entities) {
                entity.spawn(player);
            }

            this.players.add(player);
        }
    }

    /**
     * Removes the hologram for a player.
     * 
     * @param player
     *            The player
     */
    public void removeFor(Player player) {
        if (this.isPlacedFor(player)) {
            for (FakeEntity entity : this.entities) {
                entity.despawn(player);
            }

            this.players.remove(player);
        }
    }

    @Override
    public void tickFor(Player player) {
        if (player.isDirty()) {
            this.removeFor(player);
        } else {
            if (player.getLocation().getPosition().distance(this.getLocation().getPosition()) <= 48 && player.getWorld() == this.getLocation().getWorld()) {
                this.placeFor(player);
            } else {
                this.removeFor(player);
            }
        }
    }

    /**
     * Sets the position of the hologram.
     * 
     * @param position
     *            The position
     */
    public void setPosition(Vector3d position) {
        this.setLocation(ImmutableLocation.of(this.location.getWorld(), position));
    }

    /**
     * Sets the location of the hologram.
     * 
     * @param location
     *            The location
     */
    public void setLocation(ImmutableLocation location) {
        int count = 0;
        for (FakeEntity entity : this.entities) {
            entity.setPosition(this.players, new Vector3d(location.getPosition().x + 0.5, (location.getPosition().y - 2.0) - (count * 0.25), location.getPosition().z + 0.5));

            count++;
        }

        this.location = location;
    }

    /**
     * Sets a line of the hologram.
     * 
     * @param index
     *            The index
     * @param line
     *            The line
     */
    public void setLine(int index, Text line) {
        Text oldLine = this.lines.get(index);

        if (!line.toLegacy().equalsIgnoreCase(oldLine.toLegacy())) {
            this.entities.get(index).setCustomName(this.players, line.toLegacy());
        }

        this.lines.set(index, line);
    }

    /**
     * Sets the lines of the hologram.
     * 
     * @param lines
     *            An array of lines
     */
    public void setLines(Text... lines) {
        this.setLines(Arrays.asList(lines));
    }

    /**
     * Sets the lines of the hologram.
     * 
     * @param lines
     *            A collection of lines
     */
    public void setLines(Collection<Text> lines) {
        this.setLines(new ArrayList<Text>(lines));
    }

    /**
     * Sets the lines of the hologram.
     * 
     * @param lines
     *            A list of lines
     */
    public void setLines(List<Text> lines) {
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).toLegacy().equalsIgnoreCase(this.lines.get(i).toLegacy())) {
                this.entities.get(i).setCustomName(this.players, lines.get(i).toLegacy());
            }
        }

        this.lines = lines;
    }

    @Override
    protected void onPlaced() {
        for (Player player : this.location.getWorld().getPlayers()) {
            this.tickFor(player);
        }
    }

    @Override
    protected void onRemoved() {
        for (Player player : this.players) {
            for (FakeEntity entity : this.entities) {
                entity.despawn(player);
            }
        }

        this.players.clear();
    }

    /**
     * Checks if the hologram is placed for a player.
     * 
     * @param player
     *            The player
     * @return True if it is, false if not
     */
    public boolean isPlacedFor(Player player) {
        return this.players.contains(player);
    }

    @Override
    public ImmutableLocation getLocation() {
        return this.location;
    }

    /**
     * Gets the lines of the hologram
     * 
     * @return A list of lines
     */
    public List<Text> getLines() {
        return this.lines;
    }

    public static final class Builder {
        private Vector3d          position = null;
        private World             world    = null;
        private ImmutableLocation location = null;
        private List<Text>        lines    = null;

        private Builder() {
            this.lines = new ArrayList<Text>();
        }

        /**
         * Sets the location of the builder.
         * 
         * @param location
         *            The location
         * @return The builder
         */
        public Builder location(ImmutableLocation location) {
            this.location = location;

            return this;
        }

        /**
         * Sets the position of the builder.
         * 
         * @param position
         *            The position
         * @return The builder
         */
        public Builder position(Vector3d position) {
            this.position = position;

            return this;
        }

        /**
         * Sets the world of the builder.
         * 
         * @param world
         *            The world
         * @return The builder
         */
        public Builder world(World world) {
            this.world = world;

            return this;
        }

        /**
         * Adds a line to the builder.
         * 
         * @param line
         *            The line
         * @return The builder
         */
        public Builder line(Text line) {
            this.lines.add(line);

            return this;
        }

        /**
         * Adds an array of lines to the builder.
         * 
         * @param lines
         *            An array of lines
         * @return The builder
         */
        public Builder lines(Text... lines) {
            this.lines.addAll(Arrays.asList(lines));

            return this;
        }

        /**
         * Adds a list of lines to the builder.
         * 
         * @param lines
         *            A list of lines
         * @return The builder
         */
        public Builder lines(List<Text> lines) {
            this.lines.addAll(lines);

            return this;
        }

        /**
         * Adds a collection of lines to the builder.
         * 
         * @param lines
         *            A collection of lines
         * @return The builder
         */
        public Builder lines(Collection<Text> lines) {
            this.lines.addAll(lines);

            return this;
        }

        /**
         * Creates and returns a new hologram from the builder.
         * 
         * @return The hologram
         */
        public Hologram build() {
            if (this.location == null) {
                this.location = ImmutableLocation.of(this.world, this.position);
            }

            Hologram hologram = new Hologram(this.location, this.lines);
            hologram.create();

            return hologram;
        }
    }
}
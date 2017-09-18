package org.cantaloupe.audio.source;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;

import org.cantaloupe.audio.sources.LocalSource;
import org.cantaloupe.audio.sources.PositionedSource;
import org.cantaloupe.audio.sources.SharedLocalSource;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.util.ReflectionHelper;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;

/**
 * A class used to manage sources.
 * 
 * @author Dylan Scheltens
 *
 */
public class SourceManager {
    private DataContainer<String, ISource> sources = null;

    private SourceManager() {
        this.sources = DataContainer.of();
    }

    /**
     * Creates and returns a new source manager.
     * 
     * @return The source manager
     */
    public static SourceManager of() {
        return new SourceManager();
    }

    /**
     * Creates a new local source.
     * 
     * @param ID
     *            The ID of the source
     * @param settings
     *            The settings of the source
     * @return The source
     */
    public LocalSource createLocalSource(String ID, SourceSettings settings) {
        LocalSource source = null;

        try {
            source = (LocalSource) ReflectionHelper.callDeclaredConstructor(LocalSource.class, new Class<?>[] {
                    String.class
            }, ID);

            source.setSettings(settings);

            this.addSource(source);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return source;
    }

    /**
     * Creates a new shared local source.
     * 
     * @param ID
     *            The ID of the source
     * @param settings
     *            The settings of the source
     * @return The source
     */
    public SharedLocalSource createSharedLocalSource(String ID, SourceSettings settings) {
        SharedLocalSource source = null;

        try {
            source = (SharedLocalSource) ReflectionHelper.callDeclaredConstructor(SharedLocalSource.class, new Class<?>[] {
                    String.class
            }, ID);

            source.setSettings(settings);

            this.addSource(source);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return source;
    }

    /**
     * Creates a new positioned source.
     * 
     * @param ID
     *            The ID of the source
     * @param location
     *            The location of the source
     * @param settings
     *            The settings of the source
     * @return The source
     */
    public PositionedSource createPositionedSource(String ID, ImmutableLocation location, SourceSettings settings) {
        PositionedSource source = null;

        try {
            source = (PositionedSource) ReflectionHelper.callDeclaredConstructor(PositionedSource.class, new Class<?>[] {
                    String.class, ImmutableLocation.class
            }, ID, location);

            source.setSettings(settings);
            source.place();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        return source;
    }

    /**
     * Adds a source to the source manager.
     * 
     * @param source
     *            The source
     */
    public void addSource(ISource source) {
        if (this.sources.containsKey(source.getID())) {
            this.removeSource(source);
        }

        this.sources.put(source.getID(), source);
    }

    /**
     * Removes a source from the source manager.
     * 
     * @param source
     *            The source
     */
    public void removeSource(ISource source) {
        if (this.sources.containsKey(source.getID())) {
            source.clear();

            this.sources.remove(source.getID());

            if (source instanceof WorldSource) {
                ((WorldSource) source).getWorld().remove((WorldObject) source);
            }
        }
    }

    /**
     * Removes a source from the source manager.
     * 
     * @param sourceID
     *            The ID of a source
     */
    public void removeSource(String sourceID) {
        if (this.sources.containsKey(sourceID)) {
            ISource source = this.sources.get(sourceID);
            source.clear();

            this.sources.remove(sourceID);

            if (source instanceof WorldSource) {
                ((WorldSource) source).getWorld().remove((WorldObject) source);
            }
        }
    }

    protected void removeSourceInt(ISource source) {
        if (this.sources.containsKey(source.getID())) {
            source.clear();

            this.sources.remove(source.getID());
        }
    }

    protected void removeSourceInt(String sourceID) {
        if (this.sources.containsKey(sourceID)) {
            ISource source = this.sources.get(sourceID);
            source.clear();

            this.sources.remove(sourceID);
        }
    }

    /**
     * Gets a sound from the source manager.
     * 
     * @param sourceID
     *            The ID of a source
     * @return An optional containing the source if it's present, an empty
     *         optional if not
     */
    public Optional<ISource> getSource(String sourceID) {
        return Optional.ofNullable(this.sources.get(sourceID));
    }

    /**
     * Gets a collection of sources from the source manager.
     * 
     * @return The collection of sources
     */
    public Collection<ISource> getSources() {
        return this.sources.valueSet();
    }
}
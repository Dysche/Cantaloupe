package org.cantaloupe.player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.permissions.PermissionAttachment;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.entity.FakeEntity;
import org.cantaloupe.inject.IInjectable;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inventory.PlayerInventory;
import org.cantaloupe.inventory.menu.Menu;
import org.cantaloupe.permission.Allowable;
import org.cantaloupe.permission.IPermissionHolder;
import org.cantaloupe.permission.IPermittable;
import org.cantaloupe.permission.group.Group;
import org.cantaloupe.permission.group.GroupManager;
import org.cantaloupe.player.PlayerManager.Scopes;
import org.cantaloupe.protocol.PacketAccessor;
import org.cantaloupe.scoreboard.Scoreboard;
import org.cantaloupe.screen.SignInput;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.text.Text;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.cantaloupe.world.location.Location;
import org.cantaloupe.world.objects.Seat;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 * A class containing the methods for a player.
 * 
 * @author Dylan Scheltens
 *
 */
public class Player implements IPermittable, IPermissionHolder, IInjectable<Player> {
    private final org.bukkit.entity.Player                                     handle;
    private final Injector<Player>                                             injector;
    private final DataContainer<Class<? extends PlayerWrapper>, PlayerWrapper> wrappers;
    private final DataContainer<String, Object>                                data;
    private final DataContainer<String, List<String>>                          permissions;
    private final PermissionAttachment                                         permissionAttachment;
    private final List<Group>                                                  groups;
    private final List<Allowable>                                              allowables;
    private final PlayerInventory                                              inventory;
    private Menu                                                               currentMenu       = null;
    private Scoreboard                                                         currentScoreboard = null;
    private Seat                                                               currentSeat       = null;
    private SignInput                                                          currentSignInput  = null;

    private PacketService                                                      packetService     = null;
    private boolean                                                            dirty             = false;

    private Player(org.bukkit.entity.Player handle) {
        this.handle = handle;
        this.injector = Injector.of();
        this.wrappers = DataContainer.of();
        this.data = DataContainer.of();

        this.permissions = DataContainer.of();
        this.permissionAttachment = this.handle.addAttachment(Cantaloupe.getInstance());
        this.groups = new ArrayList<Group>();
        this.allowables = new ArrayList<Allowable>();

        this.inventory = PlayerInventory.of(handle.getInventory());
        this.currentScoreboard = Scoreboard.of();
    }

    /**
     * Creates and returns a new player.
     * 
     * @param handle
     *            The handle
     * @return The player
     */
    public static Player of(org.bukkit.entity.Player handle) {
        return new Player(handle);
    }

    public void onJoin() {
        // Wrappers
        this.wrappers.forEach((wrapperClass, wrapper) -> wrapper.onJoin());

        // Tick Player
        this.getWorld().tickPlayer(this);

        // Join
        this.getInjector().accept(Scopes.JOIN, this);
    }

    public void onLoad() {
        // Services
        this.packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        // Protocol
        PacketAccessor.addFor(this);

        // Wrappers
        this.wrappers.forEach((wrapperClass, wrapper) -> wrapper.onLoad());

        // Injector
        this.getInjector().accept(Scopes.LOAD, this);

        // Default
        for (Allowable allowable : Allowable.values()) {
            this.allow(allowable);
        }
    }

    public void onPostLoad() {
        // Wrappers
        this.wrappers.forEach((wrapperClass, wrapper) -> wrapper.onPostLoad());

        // Injector
        this.getInjector().accept(Scopes.POST_LOAD, this);
    }

    public void onLeave() {
        // Wrappers
        this.wrappers.forEach((wrapperClass, wrapper) -> wrapper.onLeave());

        // Injector
        this.getInjector().accept(Scopes.LEAVE, this);

        // Mark Dirty
        this.dirty = true;

        // Tick Player
        this.getWorld().tickPlayer(this);
    }

    public void onUnload() {
        // Wrappers
        this.wrappers.forEach((wrapperClass, wrapper) -> wrapper.onUnload());

        // Injector
        this.getInjector().accept(Scopes.UNLOAD, this);

        // Clear
        this.getInjector().clear();
        this.wrappers.clear();
        this.permissions.clear();
        this.permissionAttachment.remove();
        this.data.clear();
        this.allowables.clear();

        // Services
        this.packetService = null;

        // Seat
        if (this.isSitting()) {
            this.unsit();
        }

        // Protocol
        PacketAccessor.removeFor(this);
    }

    public void onWorldSwitch(World old) {
        // Update Permissions
        this.updatePermissionsWorld();

        // Tick Player
        old.tickPlayer(this);

        // Injector
        this.getInjector().accept(Scopes.WORLD_SWITCH, this);
    }

    public void onFirstJoin() {
        this.getInjector().accept(Scopes.FIRST_JOIN, this);
    }

    /**
     * Adds an allowable to the player.
     * 
     * @param allowable
     *            The allowable
     */
    public void allow(Allowable allowable) {
        this.allowables.add(allowable);
    }

    /**
     * Removes an allowable from the player.
     * 
     * @param allowable
     *            The allowable
     */
    public void disallow(Allowable allowable) {
        this.allowables.remove(allowable);
    }

    /**
     * Check if the player has an allowable.
     * 
     * @param allowable
     *            The allowable
     * @return True if it does, false if not
     */
    public boolean isAllowed(Allowable allowable) {
        return this.allowables.contains(allowable);
    }

    protected void addWrapper(Class<? extends PlayerWrapper> wrapperClass) {
        try {
            this.wrappers.put(wrapperClass, wrapperClass.getConstructor(Player.class).newInstance(this));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the player has a wrapper.
     * 
     * @param wrapperClass
     *            The wrapper type
     * @return True if it does, false if not
     */
    public boolean hasWrapper(Class<? extends PlayerWrapper> wrapperClass) {
        return this.wrappers.containsKey(wrapperClass);
    }

    /**
     * Kicks the player.
     * 
     * @param reason
     *            The reason
     */
    public void kick(Text reason) {
        this.toHandle().kickPlayer(reason.toLegacy());

        this.dirty = true;
    }

    /**
     * Teleports the player to a location.
     * 
     * @param location
     *            The location
     */
    public void teleport(org.bukkit.Location location) {
        this.handle.teleport(location);
    }

    /**
     * Teleports the player to a location.
     * 
     * @param location
     *            The location
     */
    public void teleport(Location location) {
        this.handle.teleport(location.toHandle());
    }

    /**
     * Teleports the player to a position.
     * 
     * @param position
     *            The position
     */
    public void teleport(Vector3d position) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position).toHandle());
    }

    /**
     * Teleports the player to a location.
     * 
     * @param position
     *            The position
     * @param rotation
     *            The rotation
     */
    public void teleport(Vector3d position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position, rotation).toHandle());
    }

    /**
     * Teleports the player to a position in a world.
     * 
     * @param world
     *            The world
     * @param position
     *            The position
     */
    public void teleport(World world, Vector3d position) {
        this.handle.teleport(ImmutableLocation.of(world, position).toHandle());
    }

    /**
     * Teleports the player to location in a world.
     * 
     * @param world
     *            The world
     * @param position
     *            The position
     * @param rotation
     *            The rotation
     */
    public void teleport(World world, Vector3d position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(world, position, rotation).toHandle());
    }

    /**
     * Teleports the player to a position.
     * 
     * @param position
     *            The position
     */
    public void teleport(Vector3f position) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position).toHandle());
    }

    /**
     * Teleports the player to a location.
     * 
     * @param position
     *            The position
     * @param rotation
     *            The rotation
     */
    public void teleport(Vector3f position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position, rotation).toHandle());
    }

    /**
     * Teleports the player to a position in a world.
     * 
     * @param world
     *            The world
     * @param position
     *            The position
     */
    public void teleport(World world, Vector3f position) {
        this.handle.teleport(ImmutableLocation.of(world, position).toHandle());
    }

    /**
     * Teleports the player to location in a world.
     * 
     * @param world
     *            The world
     * @param position
     *            The position
     * @param rotation
     *            The rotation
     */
    public void teleport(World world, Vector3f position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(world, position, rotation).toHandle());
    }

    /**
     * Teleports the player to a position.
     * 
     * @param position
     *            The position
     */
    public void teleport(Vector3i position) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position).toHandle());
    }

    /**
     * Teleports the player to a location.
     * 
     * @param position
     *            The position
     * @param rotation
     *            The rotation
     */
    public void teleport(Vector3i position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position, rotation).toHandle());
    }

    /**
     * Teleports the player to a position in a world.
     * 
     * @param world
     *            The world
     * @param position
     *            The position
     */
    public void teleport(World world, Vector3i position) {
        this.handle.teleport(ImmutableLocation.of(world, position).toHandle());
    }

    /**
     * Teleports the player to location in a world.
     * 
     * @param world
     *            The world
     * @param position
     *            The position
     * @param rotation
     *            The rotation
     */
    public void teleport(World world, Vector3i position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(world, position, rotation).toHandle());
    }

    /**
     * Sends a message to the player.
     * 
     * @param message
     *            The message
     */
    public void sendMessage(Text message) {
        this.handle.spigot().sendMessage(message.getComponent());
    }

    /**
     * Sends a message to the player.
     * 
     * @param message
     *            The message
     */
    public void sendMessage(String message) {
        this.handle.sendMessage(message);
    }

    /**
     * Sends a formatted message to the player.
     * 
     * @param message
     *            The message
     */
    public void sendLegacyMessage(String message) {
        this.handle.spigot().sendMessage(Text.fromLegacy(message).getComponent());
    }

    /**
     * Checks if the player is in a group.
     * 
     * @param name
     *            The name of the group
     * @return True if it is, false if not
     */
    public boolean isInGroup(String ID) {
        for (Group group : this.groups) {
            if (group.getID().equals(ID)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the player is in a group.
     * 
     * @param group
     *            The group
     * @return True if it is, false if not
     */
    public boolean isInGroup(Group group) {
        return this.groups.contains(group);
    }

    /**
     * Adds the group to the player.
     * 
     * @param name
     *            The name of the group
     */
    public void joinGroup(String ID) {
        if (this.isInGroup(ID)) {
            return;
        }

        Optional<Group> groupOpt = GroupManager.getGroup(ID);
        if (groupOpt.isPresent()) {
            Group group = groupOpt.get();

            for (String node : group.getPermissions(null)) {
                this.permissionAttachment.setPermission(node, node.startsWith("-") ? false : true);
            }

            this.groups.add(group);
        }
    }

    /**
     * Adds the group to the player.
     * 
     * @param group
     *            The group
     */
    public void joinGroup(Group group) {
        for (String node : group.getPermissions(null)) {
            this.permissionAttachment.setPermission(node, node.startsWith("-") ? false : true);
        }

        this.groups.add(group);
    }

    /**
     * Removes the group from the player.
     * 
     * @param name
     *            The name of the group
     */
    public void leaveGroup(String ID) {
        Optional<Group> groupOpt = GroupManager.getGroup(ID);

        if (groupOpt.isPresent()) {
            Group group = groupOpt.get();

            for (String node : group.getPermissions(null)) {
                boolean hasPermission = false;

                for (Group g : this.groups) {
                    if (g.hasPermission(node)) {
                        hasPermission = true;
                    }
                }

                if (!hasPermission && !this.hasPermissionGlobal(node) && !this.hasPermissionWorld(node)) {
                    this.permissionAttachment.unsetPermission(node);
                }
            }

            this.groups.remove(group);
        }
    }

    /**
     * Removes the group from the player.
     * 
     * @param group
     *            The group
     */
    public void leaveGroup(Group group) {
        for (String node : group.getPermissions(null)) {
            boolean hasPermission = false;

            for (Group g : this.groups) {
                if (g.hasPermission(node)) {
                    hasPermission = true;
                }
            }

            if (!hasPermission && !this.hasPermissionGlobal(node) && !this.hasPermissionWorld(node)) {
                this.permissionAttachment.unsetPermission(node);
            }
        }

        this.groups.remove(group);
    }

    public void sit(ImmutableLocation location, BlockFace blockFace) {
        if (this.currentSeat != null) {
            this.unsit();
        }

        this.currentSeat = Seat.of(location, blockFace);
        this.currentSeat.place();
        this.currentSeat.seatPlayer(this);
    }

    public void sit(ImmutableLocation location) {
        if (this.currentSeat != null) {
            this.unsit();
        }

        this.currentSeat = Seat.of(location);
        this.currentSeat.place();
        this.currentSeat.seatPlayer(this);
    }

    public void unsit() {
        this.currentSeat.unseatPlayer();
        this.currentSeat.remove();
        this.currentSeat = null;
    }

    @Override
    public void setPermission(String node) {
        this.permissions.get("_global_").add(node);
        this.permissionAttachment.setPermission(node, node.startsWith("-") ? false : true);
    }

    @Override
    public void setPermission(World world, String node) {
        if (!this.permissions.containsKey(world.getName())) {
            this.permissions.put(world.getName(), new ArrayList<String>());
        }

        this.permissions.get(world.getName()).add(node);
        this.permissionAttachment.setPermission(node, node.startsWith("-") ? false : true);
    }

    @Override
    public void unsetPermission(String node) {
        this.permissions.get("_global_").remove(node);

        if (!this.hasPermissionGroup(node) && !this.hasPermissionWorld(node)) {
            this.permissionAttachment.unsetPermission(node);
        }
    }

    @Override
    public void unsetPermission(World world, String node) {
        if (this.permissions.containsKey(world.getName())) {
            this.permissions.get(world.getName()).remove(node);

            if (!this.hasPermissionGlobal(node) && !this.hasPermissionGroup(node)) {
                this.permissionAttachment.unsetPermission(node);
            }
        }
    }

    @Override
    public boolean hasPermission(String node) {
        return this.handle.hasPermission(node);
    }

    private boolean hasPermissionGlobal(String node) {
        return this.permissions.get("_global_").contains(node);
    }

    private boolean hasPermissionGroup(String node) {
        for (Group group : this.groups) {
            if (group.hasPermission(node)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasPermissionWorld(String node) {
        for (String world : this.permissions.keySet()) {
            if (this.toHandle().getWorld().getName().equals(world)) {
                if (this.permissions.get(world).contains(node)) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    private void updatePermissionsWorld() {
        if (this.permissions.containsKey(this.handle.getWorld().getName())) {
            for (String node : this.permissions.get(this.handle.getWorld().getName())) {
                this.permissionAttachment.setPermission(node, node.startsWith("-") ? false : true);
            }
        }
    }

    /**
     * Opens a menu for the player.
     * 
     * @param menu
     *            The menu
     */
    public void openMenu(Menu menu) {
        if (menu.getLandingPage() != null) {
            menu.showLandingPage();

            this.currentMenu = menu;
        }
    }

    /**
     * Closes the currently opened menu.
     */
    public void closeMenu() {
        this.handle.closeInventory();
        this.currentMenu = null;
    }

    public void resetMenu() {
        this.currentMenu = null;
    }

    /**
     * Checks if the player has a menu opened.
     * 
     * @return True if it does, false if not
     */
    public boolean hasMenuOpened() {
        return this.currentMenu != null;
    }

    /**
     * Plays a sound for the player.
     * 
     * @param location
     *            The location
     * @param sound
     *            The sound
     * @param volume
     *            The volume
     * @param pitch
     *            The pitch
     */
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        this.handle.playSound(location.toHandle(), sound, volume, pitch);
    }

    /**
     * Plays a sound for the player.
     * 
     * @param location
     *            The location
     * @param sound
     *            The sound
     * @param volume
     *            The volume
     */
    public void playSound(Location location, Sound sound, float volume) {
        this.handle.playSound(location.toHandle(), sound, volume, 1f);
    }

    /**
     * Plays a sound for the player.
     * 
     * @param location
     *            The location
     * @param sound
     *            The sound
     */
    public void playSound(Location location, Sound sound) {
        this.handle.playSound(location.toHandle(), sound, 1f, 1f);
    }

    /**
     * Plays a sound for the player.
     * 
     * @param sound
     *            The sound
     * @param volume
     *            The volume
     * @param pitch
     *            The pitch
     */
    public void playSound(Sound sound, float volume, float pitch) {
        this.handle.playSound(this.getLocation().toHandle(), sound, volume, pitch);
    }

    /**
     * Plays a sound for the player.
     * 
     * @param sound
     *            The sound
     * @param volume
     *            The volume
     */
    public void playSound(Sound sound, float volume) {
        this.handle.playSound(this.getLocation().toHandle(), sound, volume, 1f);
    }

    /**
     * Plays a sound for the player.
     * 
     * @param sound
     *            The sound
     */
    public void playSound(Sound sound) {
        this.handle.playSound(this.getLocation().toHandle(), sound, 1f, 1f);
    }

    public void spectate(FakeEntity entity) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            this.sendPacket(nmsService.NMS_PACKET_OUT_CAMERA_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS).newInstance(entity.toHandle()));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void spectate(Player player) {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            this.sendPacket(nmsService.NMS_PACKET_OUT_CAMERA_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS).newInstance(nmsService.getPlayerHandle(player)));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public void unspectate() {
        NMSService nmsService = Cantaloupe.getServiceManager().provide(NMSService.class);

        try {
            this.sendPacket(nmsService.NMS_PACKET_OUT_CAMERA_CLASS.getConstructor(nmsService.NMS_ENTITY_CLASS).newInstance(nmsService.getPlayerHandle(this)));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a packet to the player.
     * 
     * @param packet
     *            The player
     */
    public void sendPacket(Object packet) {
        this.packetService.sendPacket(this, packet);
    }

    /**
     * Sets the scoreboard of the player.
     * 
     * @param scoreboard
     *            The scoreboard
     */
    public void setScoreboard(Scoreboard scoreboard) {
        this.handle.setScoreboard(scoreboard.toHandle());
        this.currentScoreboard = scoreboard;
    }

    public void setSignInput(SignInput signInput) {
        this.currentSignInput = signInput;
    }

    /**
     * Checks if the player is sitting.
     * 
     * @return True if it is, false if not
     */
    public boolean isSitting() {
        return this.currentSeat != null;
    }

    /**
     * Checks if the player is marked for removal.
     * 
     * @return True if it is, false if not
     */
    public boolean isDirty() {
        return this.dirty;
    }

    /**
     * Returns the handle of the player.
     * 
     * @return The handle
     */
    public org.bukkit.entity.Player toHandle() {
        return this.handle;
    }

    /**
     * Gets the UUID of the player.
     * 
     * @return The UUID
     */
    public UUID getUUID() {
        return this.handle.getUniqueId();
    }

    @Override
    public Injector<Player> getInjector() {
        return this.injector;
    }

    /**
     * Gets a list of wrappers from the player.
     * 
     * @return The list of wrappers
     */
    public Collection<PlayerWrapper> getWrappers() {
        return this.wrappers.valueSet();
    }

    protected DataContainer<Class<? extends PlayerWrapper>, PlayerWrapper> getWrapperMap() {
        return this.wrappers.clone();
    }

    /**
     * Gets a wrapper from the player.
     * 
     * @param <T>
     *            The type of the wrapper
     * @param wrapperClass
     *            The type of the wrapper
     * 
     * @return The wrapper
     */
    @SuppressWarnings("unchecked")
    public <T extends PlayerWrapper> T getWrapper(Class<T> wrapperClass) {
        return (T) this.wrappers.get(wrapperClass);
    }

    /**
     * Gets the name of the player.
     * 
     * @return The name
     */
    public String getName() {
        return this.handle.getName();
    }

    /**
     * Gets the world the player is in.
     * 
     * @return The world
     */
    public World getWorld() {
        return Cantaloupe.getWorldManager().getWorld(this.handle.getWorld().getName());
    }

    /**
     * Gets the location of the player.
     * 
     * @return The location
     */
    public ImmutableLocation getLocation() {
        return ImmutableLocation.of(this.handle.getLocation());
    }

    /**
     * Gets the eye location of the player.
     * 
     * @return The eye location
     */
    public ImmutableLocation getEyeLocation() {
        return ImmutableLocation.of(this.handle.getEyeLocation());
    }

    /**
     * Gets the bed location of the player.
     * 
     * @return The bed location
     */
    public ImmutableLocation getBedSpawnLocation() {
        return ImmutableLocation.of(this.handle.getBedSpawnLocation());
    }

    /**
     * Gets the position of the player.
     * 
     * @return The position
     */
    public Vector3d getPosition() {
        return this.getLocation().getPosition();
    }

    /**
     * Gets the eye position of the player.
     * 
     * @return The eye position
     */
    public Vector3d getEyePosition() {
        return this.getEyeLocation().getPosition();
    }

    /**
     * Gets the bed position of the player.
     * 
     * @return The bed position
     */
    public Vector3d getBedSpawnPosition() {
        return this.getBedSpawnLocation().getPosition();
    }

    /**
     * Gets the rotation of the player.
     * 
     * @return The rotation
     */
    public Vector2f getRotation() {
        return this.getLocation().getRotation();
    }

    /**
     * Gets the block face of the player.
     * 
     * @return The block face
     */
    public BlockFace getBlockFace() {
        return this.getLocation().getBlockFace();
    }

    /**
     * Gets a collection of groups the player is in.
     * 
     * @return The collection of groups
     */
    public Collection<Group> getGroups() {
        return this.groups;
    }

    /**
     * Gets a map of permissio
     * 
     * @return The container of permissions.
     */
    public DataContainer<String, List<String>> getPermissions() {
        return this.permissions.clone();
    }

    /**
     * Gets a list of permission from the group for a world.
     * 
     * @param world
     *            The world
     * @return The list of permissions
     */
    public List<String> getPermissions(World world) {
        return world != null != this.permissions.containsKey(world.getName()) ? this.permissions.get(world.getName()) : this.permissions.get("_global_");
    }

    /**
     * Gets the inventory of the player.
     * 
     * @return The inventory
     */
    public PlayerInventory getInventory() {
        return this.inventory;
    }

    /**
     * Gets the current scoreboard of the player.
     * 
     * @return The scoreboard
     */
    public Scoreboard getCurrentScoreboard() {
        return this.currentScoreboard;
    }

    /**
     * Gets the current menu of the player.
     * 
     * @return The menu
     */
    public Menu getCurrentMenu() {
        return this.currentMenu;
    }

    /**
     * Gets the current seat of the player.
     * 
     * @return The seat
     */
    public Seat getCurrentSeat() {
        return this.currentSeat;
    }

    public SignInput getCurrentSignInput() {
        return this.currentSignInput;
    }

    /**
     * Gets the public data container of the player.
     * 
     * @return The container
     */
    public DataContainer<String, Object> getData() {
        return this.data;
    }
}
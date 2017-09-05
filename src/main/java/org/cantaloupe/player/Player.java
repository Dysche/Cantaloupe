package org.cantaloupe.player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.permissions.PermissionAttachment;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.data.DataContainer;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inventory.menu.Menu;
import org.cantaloupe.permission.Allowable;
import org.cantaloupe.permission.IPermissionHolder;
import org.cantaloupe.permission.IPermittable;
import org.cantaloupe.permission.group.Group;
import org.cantaloupe.permission.group.GroupManager;
import org.cantaloupe.player.PlayerManager.Scopes;
import org.cantaloupe.protocol.PacketAccessor;
import org.cantaloupe.scoreboard.Scoreboard;
import org.cantaloupe.service.services.PacketService;
import org.cantaloupe.text.Text;
import org.cantaloupe.world.World;
import org.cantaloupe.world.location.ImmutableLocation;
import org.cantaloupe.world.location.Location;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Player implements IPermittable, IPermissionHolder {
    private final org.bukkit.entity.Player                                     handle;
    private final Injector<Player>                                             injector;
    private final DataContainer<Class<? extends PlayerWrapper>, PlayerWrapper> wrappers;
    private final DataContainer<String, Object>                                data;
    private final DataContainer<String, List<String>>                          permissions;
    private final PermissionAttachment                                         permissionAttachment;
    private final List<Group>                                                  groups;
    private final List<Allowable>                                              allowables;
    private Menu                                                               currentMenu       = null;
    private Scoreboard                                                         currentScoreboard = null;
    private boolean                                                            dirty             = false;

    private PacketService                                                      packetService     = null;

    private Player(org.bukkit.entity.Player handle) {
        this.handle = handle;
        this.injector = Injector.of();
        this.wrappers = DataContainer.of();
        this.data = DataContainer.of();

        this.permissions = DataContainer.of();
        this.permissionAttachment = this.handle.addAttachment(Cantaloupe.getInstance());
        this.groups = new ArrayList<Group>();
        this.allowables = new ArrayList<Allowable>();
    }

    public static Player of(org.bukkit.entity.Player handle) {
        return new Player(handle);
    }

    public void onJoin() {
        // Tick Player
        this.getWorld().tickPlayer(this);

        // Join
        this.getInjector().accept(Scopes.JOIN, this);
    }

    public void onLoad() {
        // Services
        this.packetService = Cantaloupe.getServiceManager().provide(PacketService.class);

        // Packet
        PacketAccessor.addFor(this);

        // Wrappers
        this.wrappers.forEach((wrapperClass, wrapper) -> wrapper.onLoad());
        
        // Injector
        this.getInjector().accept(Scopes.LOAD, this);
    }

    public void onLeave() {
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

        // Packet
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

    public void allow(Allowable allowable) {
        this.allowables.add(allowable);
    }

    public void disallow(Allowable allowable) {
        this.allowables.remove(allowable);
    }

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

    public boolean hasWrapper(Class<? extends PlayerWrapper> wrapperClass) {
        return this.wrappers.containsKey(wrapperClass);
    }

    public void teleport(org.bukkit.Location handle) {
        this.handle.teleport(handle);
    }

    public void teleport(Location location) {
        this.handle.teleport(location.toHandle());
    }

    public void teleport(Vector3d position) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position).toHandle());
    }

    public void teleport(Vector3d position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position, rotation).toHandle());
    }

    public void teleport(World world, Vector3d position) {
        this.handle.teleport(ImmutableLocation.of(world, position).toHandle());
    }

    public void teleport(World world, Vector3d position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(world, position, rotation).toHandle());
    }

    public void teleport(Vector3f position) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position).toHandle());
    }

    public void teleport(Vector3f position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position, rotation).toHandle());
    }

    public void teleport(World world, Vector3f position) {
        this.handle.teleport(ImmutableLocation.of(world, position).toHandle());
    }

    public void teleport(World world, Vector3f position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(world, position, rotation).toHandle());
    }

    public void teleport(Vector3i position) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position).toHandle());
    }

    public void teleport(Vector3i position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(this.getWorld(), position, rotation).toHandle());
    }

    public void teleport(World world, Vector3i position) {
        this.handle.teleport(ImmutableLocation.of(world, position).toHandle());
    }

    public void teleport(World world, Vector3i position, Vector2f rotation) {
        this.handle.teleport(ImmutableLocation.of(world, position, rotation).toHandle());
    }

    public void sendMessage(Text text) {
        this.handle.spigot().sendMessage(text.getComponent());
    }

    public void sendMessage(String message) {
        this.handle.sendMessage(message);
    }

    public void sendLegacyMessage(String message) {
        this.handle.spigot().sendMessage(Text.fromLegacy(message).getComponent());
    }

    public boolean isInGroup(String name) {
        for (Group group : this.groups) {
            if (group.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public boolean isInGroup(Group group) {
        return this.groups.contains(group);
    }

    public void joinGroup(String name) {
        if (this.isInGroup(name)) {
            return;
        }

        Group group = GroupManager.getGroup(name);
        for (String node : group.getPermissions(null)) {
            this.permissionAttachment.setPermission(node, node.startsWith("-") ? false : true);
        }

        this.groups.add(group);
    }

    public void joinGroup(Group group) {
        for (String node : group.getPermissions(null)) {
            this.permissionAttachment.setPermission(node, node.startsWith("-") ? false : true);
        }

        this.groups.add(group);
    }

    public void leaveGroup(String name) {
        Group group = GroupManager.getGroup(name);

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

    public void openMenu(Menu menu) {
        this.currentMenu = menu;
        this.currentMenu.open();
    }

    public void closeMenu() {
        this.currentMenu = null;
        this.handle.closeInventory();
    }

    public void playSound(Location location, Sound sound, float volume, float pitch) {
        this.handle.playSound(location.toHandle(), sound, volume, pitch);
    }

    public void playSound(Location location, Sound sound, float volume) {
        this.handle.playSound(location.toHandle(), sound, volume, 1f);
    }

    public void playSound(Location location, Sound sound) {
        this.handle.playSound(location.toHandle(), sound, 1f, 1f);
    }

    public void playSound(Sound sound, float volume, float pitch) {
        this.handle.playSound(this.getLocation().toHandle(), sound, volume, pitch);
    }

    public void playSound(Sound sound, float volume) {
        this.handle.playSound(this.getLocation().toHandle(), sound, volume, 1f);
    }

    public void playSound(Sound sound) {
        this.handle.playSound(this.getLocation().toHandle(), sound, 1f, 1f);
    }

    public void sendPacket(Object packet) {
        this.packetService.sendPacket(this, packet);
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.handle.setScoreboard(scoreboard.toHandle());
        this.currentScoreboard = scoreboard;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public org.bukkit.entity.Player toHandle() {
        return this.handle;
    }

    public UUID getUUID() {
        return this.handle.getUniqueId();
    }

    public Injector<Player> getInjector() {
        return this.injector;
    }

    public DataContainer<Class<? extends PlayerWrapper>, PlayerWrapper> getWrappers() {
        return this.wrappers.clone();
    }

    @SuppressWarnings("unchecked")
    public <T extends PlayerWrapper> T getWrapper(Class<? extends PlayerWrapper> wrapperClass) {
        return (T) this.wrappers.get(wrapperClass);
    }

    public String getName() {
        return this.handle.getName();
    }

    public World getWorld() {
        return Cantaloupe.getWorldManager().getWorld(this.handle.getWorld().getName());
    }

    public ImmutableLocation getLocation() {
        return ImmutableLocation.of(this.handle.getLocation());
    }

    public ImmutableLocation getEyeLocation() {
        return ImmutableLocation.of(this.handle.getEyeLocation());
    }

    public ImmutableLocation getBedSpawnLocation() {
        return ImmutableLocation.of(this.handle.getBedSpawnLocation());
    }

    public Vector3d getPosition() {
        return this.getLocation().getPosition();
    }

    public Vector3d getEyePosition() {
        return this.getEyeLocation().getPosition();
    }

    public Vector3d getBedSpawnPosition() {
        return this.getBedSpawnLocation().getPosition();
    }

    public Vector2f getRotation() {
        return this.getLocation().getRotation();
    }

    public Collection<Group> getGroups() {
        return this.groups;
    }

    public DataContainer<String, List<String>> getPermissions() {
        return this.permissions.clone();
    }

    public List<String> getPermissions(World world) {
        return world != null != this.permissions.containsKey(world.getName()) ? this.permissions.get(world.getName()) : this.permissions.get("_global_");
    }

    public Scoreboard getCurrentScoreboard() {
        return this.currentScoreboard;
    }

    public Menu getCurrentMenu() {
        return this.currentMenu;
    }

    public DataContainer<String, Object> getData() {
        return this.data;
    }
}
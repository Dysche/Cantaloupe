package org.cantaloupe.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.permissions.PermissionAttachment;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.inject.Injector;
import org.cantaloupe.inventory.menu.Menu;
import org.cantaloupe.permission.IPermissionHolder;
import org.cantaloupe.permission.IPermittable;
import org.cantaloupe.permission.group.Group;
import org.cantaloupe.permission.group.GroupManager;
import org.cantaloupe.player.PlayerManager.Scopes;
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
    private final org.bukkit.entity.Player  handle;
    private final Injector<Player>          injector;
    private final PermissionAttachment      permissionAttachment;
    private final ArrayList<Group>          groups;
    private final Map<String, List<String>> permissions;
    private Menu                            currentMenu   = null;

    private PacketService                   packetService = null;

    private Player(org.bukkit.entity.Player handle) {
        this.handle = handle;
        this.injector = new Injector<Player>();

        this.permissions = new HashMap<String, List<String>>();
        this.permissionAttachment = this.handle.addAttachment(Cantaloupe.getInstance());
        this.groups = new ArrayList<Group>();
    }

    public static Player of(org.bukkit.entity.Player handle) {
        return new Player(handle);
    }

    public void onJoin() {
        Optional<List<Consumer<Player>>> consumers = this.getInjector().getConsumers(Scopes.JOIN);
        if (consumers.isPresent()) {
            for (Consumer<Player> consumer : consumers.get()) {
                consumer.accept(this);
            }
        }
        
        this.getWorld().tickPlayer(this);
    }

    public void onLoad() {
        Optional<List<Consumer<Player>>> consumers = this.getInjector().getConsumers(Scopes.LOAD);
        if (consumers.isPresent()) {
            for (Consumer<Player> consumer : consumers.get()) {
                consumer.accept(this);
            }
        }

        // Services
        this.packetService = Cantaloupe.getServiceManager().provide(PacketService.class);
    }

    public void onLeave() {
        Optional<List<Consumer<Player>>> consumers = this.getInjector().getConsumers(Scopes.LEAVE);
        if (consumers.isPresent()) {
            for (Consumer<Player> consumer : consumers.get()) {
                consumer.accept(this);
            }
        }
        
        this.getWorld().tickPlayer(this);
    }

    public void onUnload() {
        Optional<List<Consumer<Player>>> consumers = this.getInjector().getConsumers(Scopes.UNLOAD);
        if (consumers.isPresent()) {
            for (Consumer<Player> consumer : consumers.get()) {
                consumer.accept(this);
            }
        }

        this.permissionAttachment.remove();
        this.getInjector().clear();

        // Services
        this.packetService = null;
    }

    public void onWorldSwitch(World old) {
        this.updatePermissionsWorld();

        Optional<List<Consumer<Player>>> consumers = this.getInjector().getConsumers(Scopes.WORLD_SWITCH);
        if (consumers.isPresent()) {
            for (Consumer<Player> consumer : consumers.get()) {
                consumer.accept(this);
            }
        }
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

    public void sendPacket(Object packet) {
        this.packetService.sendPacket(this, packet);
    }

    public org.bukkit.entity.Player toHandle() {
        return this.handle;
    }

    public UUID getUUID() {
        return this.handle.getUniqueId();
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

    public Collection<Group> getGroups() {
        return this.groups;
    }

    public Map<String, List<String>> getPermissions() {
        return this.permissions;
    }

    public List<String> getPermissions(World world) {
        return world != null != this.permissions.containsKey(world.getName()) ? this.permissions.get(world.getName()) : this.permissions.get("_global_");
    }

    public Menu getCurrentMenu() {
        return this.currentMenu;
    }

    public Injector<Player> getInjector() {
        return this.injector;
    }
}
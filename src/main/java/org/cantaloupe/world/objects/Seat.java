package org.cantaloupe.world.objects;

import org.bukkit.block.BlockFace;
import org.cantaloupe.player.Player;
import org.cantaloupe.statue.ArmorStandStatue;
import org.cantaloupe.world.RemoveCause;
import org.cantaloupe.world.WorldObject;
import org.cantaloupe.world.location.ImmutableLocation;

public class Seat extends WorldObject {
    private final ArmorStandStatue armorStand;
    private Player                 seatedPlayer = null;

    private Seat(ImmutableLocation location, BlockFace blockFace) {
        this.armorStand = ArmorStandStatue.builder().location(location).facing(blockFace).invisible(true).build();
    }

    public static Seat of(ImmutableLocation location, BlockFace blockFace) {
        return new Seat(location, blockFace);
    }

    public static Seat of(ImmutableLocation location) {
        return new Seat(location, null);
    }

    @Override
    protected void onPlaced() {
        this.armorStand.place();
    }

    @Override
    protected void onRemoved(RemoveCause cause) {
        if (cause == RemoveCause.GENERAL) {
            this.armorStand.remove();
        }
    }

    public void seatPlayer(Player player) {
        this.armorStand.getEntity().setPassenger(this.armorStand.getPlayers(), player);
        this.seatedPlayer = player;
    }

    public void unseatPlayer() {
        this.armorStand.getEntity().removePassenger(this.armorStand.getPlayers());
        this.seatedPlayer = null;
    }

    public boolean isOccupied() {
        return this.seatedPlayer != null;
    }

    public void setLocation(ImmutableLocation location) {
        this.armorStand.setLocation(location);
    }

    @Override
    public ImmutableLocation getLocation() {
        return this.armorStand.getLocation();
    }

    public ArmorStandStatue getArmorStand() {
        return this.armorStand;
    }

    public Player getSeatedPlayer() {
        return this.seatedPlayer;
    }
}
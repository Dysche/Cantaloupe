package org.cantaloupe.service.services;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.service.Service;
import org.cantaloupe.user.User;
import org.cantaloupe.world.location.Location;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class ParticleService implements Service {
    private PacketService packetService = null;

    @Override
    public void load() {
        this.packetService = Cantaloupe.getServiceManager().getService(PacketService.class);
    }

    @Override
    public void unload() {
        this.packetService = null;
    }

    public void display(User user, ParticleType type, Vector3d position, Vector3f offset, boolean longDistance, float speed, int amount) {
        this.packetService.sendParticlePacket(user, type, position, offset, longDistance, speed, amount);
    }

    public void display(User user, ParticleType type, Location location, Vector3f offset, boolean longDistance, float speed, int amount) {
        this.packetService.sendParticlePacket(user, type, new Vector3d(location.getPosition().x, location.getPosition().y, location.getPosition().z), offset, longDistance, speed, amount);
    }

    @Override
    public String getName() {
        return "particle";
    }

    public enum ParticleType {
        // Values
        REDSTONE("reddust", 30, -1, true), 
        ITEM_CRACK("iconcrack", 36, -1, false),
        BLOCK_CRACK("blockcrack", 37, -1, false);

        // Enum Body
        private final String  type;
        private final int     ID;
        private final int     requiredVersion;
        private final boolean colorable;

        private ParticleType(String type, int ID, int requiredVersion, boolean colorable) {
            this.type = type;
            this.ID = ID;
            this.requiredVersion = requiredVersion;
            this.colorable = colorable;
        }

        public boolean isColorable() {
            return this.colorable;
        }

        public String getName() {
            return this.type;
        }

        public int getID() {
            return this.ID;
        }
        
        public int getRequiredVersion() {
            return this.requiredVersion;
        }
    }
}
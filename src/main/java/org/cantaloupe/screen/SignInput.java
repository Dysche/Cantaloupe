package org.cantaloupe.screen;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Material;
import org.cantaloupe.Cantaloupe;
import org.cantaloupe.player.Player;
import org.cantaloupe.service.services.NMSService;
import org.cantaloupe.text.Text;
import org.cantaloupe.util.ReflectionHelper;
import org.joml.Vector3i;

public class SignInput {
    private final Player                     player;
    private final List<Text>                 lines;
    private BiConsumer<Player, List<String>> inputConsumer = null;
    private Vector3i                         position      = null;

    private SignInput(Player player, List<Text> lines) {
        this.player = player;
        this.lines = lines;
    }

    public static SignInput of(Player player) {
        return new SignInput(player, null);
    }

    public static SignInput of(Player player, List<Text> lines) {
        return new SignInput(player, lines);
    }

    public void show() {
        NMSService service = Cantaloupe.getServiceManager().provide(NMSService.class);
        List<String> lines = new ArrayList<String>();

        this.position = this.player.getLocation().getBlockPosition().mul(1, 0, 1);

        for (Text line : this.lines) {
            lines.add(line.toLegacy());
        }

        if (this.lines != null) {
            try {
                Object packet = service.NMS_PACKET_OUT_BLOCKCHANGE_CLASS.newInstance();

                ReflectionHelper.setDeclaredField("a", packet, service.getBlockPosition(this.position));
                ReflectionHelper.setDeclaredField("block", packet, service.getBlockData(Material.SIGN_POST, (byte) 0));

                this.player.sendPacket(packet);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            this.player.toHandle().sendSignChange(this.player.getLocation().mult(1, 0, 1).toHandle(), lines.toArray(new String[0]));
        }

        try {
            this.player.sendPacket(service.NMS_PACKET_OUT_OPENSIGNEDITOR_CLASS.getConstructor(service.NMS_BLOCKPOSITION_CLASS).newInstance(service.getBlockPosition(this.position)));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        if (this.lines != null) {
            try {
                Object packet = service.NMS_PACKET_OUT_BLOCKCHANGE_CLASS.newInstance();

                ReflectionHelper.setDeclaredField("a", packet, service.getBlockPosition(this.position));
                ReflectionHelper.setDeclaredField("block", packet, service.getBlockData(Material.BEDROCK, (byte) 0));

                this.player.sendPacket(packet);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        this.player.setSignInput(this);
    }

    public void onInput(List<String> lines) {
        if (this.inputConsumer != null) {
            this.inputConsumer.accept(this.player, lines);
            
            this.player.setSignInput(null);
        }
    }

    public SignInput setInputConsumer(BiConsumer<Player, List<String>> inputConsumer) {
        this.inputConsumer = inputConsumer;

        return this;
    }

    public List<Text> getLines() {
        return this.lines;
    }

    public Vector3i getPosition() {
        return this.position;
    }
}
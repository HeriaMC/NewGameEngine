package fr.heriamc.games.engine.player;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.user.HeriaPlayer;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.games.engine.point.SinglePoint;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseGamePlayer {

    private final UUID uuid;

    private final Player player;

    private final HeriaPlayer heriaPlayer;
    private final CraftPlayer craftPlayer;

    private boolean spectator;

    public BaseGamePlayer(UUID uuid, boolean spectator) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.heriaPlayer = HeriaAPI.get().getPlayerManager().get(uuid);
        this.craftPlayer = (CraftPlayer) player;
        this.spectator = spectator;
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    public void sendMessage(String... messages) {
        Arrays.asList(messages).forEach(this::sendMessage);
    }

    public void teleport(Location location) {
        player.teleport(location);
    }

    public void teleport(SinglePoint point) {
        point.teleport(player);
    }

    public String getName() {
        return player.getName();
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public boolean isAlive() {
        return !spectator;
    }

    public void sendTitle(int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        MultiThreading.execute(() ->
                sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut),
                        new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}")),
                        new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}"))));
    }

    public void sendActionBar(String message) {
        MultiThreading.execute(() -> sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2)));
    }

    public void sendPacket(PlayerConnection connection, Packet<?> packet) {
        connection.sendPacket(packet);
    }

    public void sendPacket(PlayerConnection connection, Packet<?>... packets) {
        Arrays.asList(packets)
                .forEach(connection::sendPacket);
    }

    public void sendPacket(Packet<?> packet) {
        sendPacket(craftPlayer.getHandle().playerConnection, packet);
    }

    public void sendPacket(Packet<?>... packets) {
        sendPacket(craftPlayer.getHandle().playerConnection, packets);
    }

}
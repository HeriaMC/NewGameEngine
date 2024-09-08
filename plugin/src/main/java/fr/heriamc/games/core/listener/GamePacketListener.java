package fr.heriamc.games.core.listener;

import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.messaging.packet.HeriaPacketReceiver;
import fr.heriamc.bukkit.game.packet.GameJoinPacket;
import fr.heriamc.games.api.pool.GamePoolManager;

public record GamePacketListener(GamePoolManager gamePoolManager) implements HeriaPacketReceiver {

    @Override
    public void execute(String s, HeriaPacket heriaPacket) {
        if (heriaPacket instanceof GameJoinPacket packet)
            gamePoolManager.getJoinPacketCache().put(packet.getUuid(), packet);
    }

}
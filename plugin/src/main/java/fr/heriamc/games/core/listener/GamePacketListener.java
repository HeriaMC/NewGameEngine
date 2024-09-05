package fr.heriamc.games.core.listener;

import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.messaging.packet.HeriaPacketReceiver;
import fr.heriamc.bukkit.game.packet.GameJoinPacket;
import fr.heriamc.games.core.GameEngine;

public record GamePacketListener(GameEngine plugin) implements HeriaPacketReceiver {

    @Override
    public void execute(String s, HeriaPacket heriaPacket) {
        if (heriaPacket instanceof GameJoinPacket packet)
            plugin.getGameApi().getGamePoolManager().getJoinPacketCache().put(packet.getUuid(), packet);
    }

}
package fr.heriamc.games.core.listener;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.game.packet.GameCreatedPacket;
import fr.heriamc.api.game.packet.GameCreationRequestPacket;
import fr.heriamc.api.game.packet.GameJoinPacket;
import fr.heriamc.api.messaging.packet.HeriaPacket;
import fr.heriamc.api.messaging.packet.HeriaPacketReceiver;
import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.games.api.pool.GamePoolManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GamePacketListener implements HeriaPacketReceiver {

    private final GamePoolManager gamePoolManager;
    private final HeriaAPI heriaAPI;

    private final String serverName;

    public GamePacketListener(GamePoolManager gamePoolManager, HeriaAPI heriaAPI) {
        this.gamePoolManager = gamePoolManager;
        this.heriaAPI = heriaAPI;
        this.serverName = HeriaBukkit.get().getInstanceName();
    }

    @Override
    public void execute(String s, HeriaPacket heriaPacket) {
        switch (heriaPacket) {
            case GameJoinPacket packet -> {
                gamePoolManager.getJoinPacketCache().put(packet.getUuid(), packet);
                log.info("[GamePacketListener] Received a join packet from '{}' for game '{}'", packet.getUuid().toString(), packet.getGameName());
            }
            case GameCreationRequestPacket packet -> {
                if (!packet.getServer().equals(serverName)) return;

                gamePoolManager.getGamePool(packet.getServerType()).ifPresent(pool -> pool.addGame(packet));
            }
            case GameCreatedPacket packet -> {}
            default -> log.error("[GamePacketListener] Unexpected packet received {}", heriaPacket);
        }
    }

}
package fr.heriamc.games.engine.waitingroom;

import fr.heriamc.games.engine.player.BaseGamePlayer;
import org.bukkit.event.Listener;

public interface WaitingRoom extends Listener {

    <G extends BaseGamePlayer> void onJoin(G gamePlayer);
    <G extends BaseGamePlayer> void onLeave(G gamePlayer);

}
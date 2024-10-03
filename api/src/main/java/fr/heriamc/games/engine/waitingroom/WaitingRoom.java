package fr.heriamc.games.engine.waitingroom;

import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.map.Map;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import fr.heriamc.games.engine.utils.task.countdown.CountdownTask;
import org.bukkit.event.Listener;

public interface WaitingRoom extends Listener {

    Map getMap();

    CountdownTask getCountdownTask();

    <M extends MiniGame> M getGame();

    <G extends BaseGamePlayer> void onJoin(G gamePlayer);
    <G extends BaseGamePlayer> void onLeave(G gamePlayer);

    <G extends BaseGamePlayer> void giveItems(G gamePlayer);

}
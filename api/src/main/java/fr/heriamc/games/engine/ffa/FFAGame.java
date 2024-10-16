package fr.heriamc.games.engine.ffa;

import fr.heriamc.api.data.PersistentDataManager;
import fr.heriamc.games.engine.GameSettings;
import fr.heriamc.games.engine.SimpleGame;
import fr.heriamc.games.engine.event.player.GamePlayerJoinEvent;
import fr.heriamc.games.engine.event.player.GamePlayerSpectateEvent;
import fr.heriamc.games.engine.ffa.lobby.FFALobby;
import fr.heriamc.games.engine.ffa.player.FFAGamePlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public abstract class FFAGame<G extends FFAGamePlayer, S extends GameSettings<?>> extends SimpleGame<G, S> {

    protected final PersistentDataManager<?, ?> dataManager;
    protected final FFALobby ffaLobby;

    public FFAGame(String name, S settings, PersistentDataManager<?, ?> dataManager, FFALobby ffaLobby) {
        super(name, settings);
        this.dataManager = dataManager;
        this.ffaLobby = ffaLobby;
    }

    @Override
    public void joinGame(Player player, boolean spectator) {
        final var uuid = player.getUniqueId();

        if (!players.containsKey(uuid)) {
            final var gamePlayer = players.put(uuid, defaultGamePlayer(uuid, spectator));

            this.playerCount += 1;
            settings.addBoardViewer(this, gamePlayer);
            ffaLobby.onJoin(this, gamePlayer);

            Bukkit.getPluginManager().callEvent(new GamePlayerJoinEvent<>(this, gamePlayer));

            if (spectator)
                Bukkit.getPluginManager().callEvent(new GamePlayerSpectateEvent<>(this, gamePlayer));

            log.info("[{}] {} {} game.", getFullName(), player.getName(), spectator ? "spectate" : "joined");
        }
    }

    public void play(G gamePlayer) {
        ffaLobby.onPlay(this, gamePlayer);
    }

}
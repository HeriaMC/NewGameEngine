package fr.heriamc.games.core.test;

import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.engine.GameSize;
import fr.heriamc.games.engine.SimpleGame;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ExampleGame extends SimpleGame<ExampleGamePlayer, ExampleGameSettings> {

    public ExampleGame(GameSize gameSize) {
        super("rush", new ExampleGameSettings(gameSize));
    }

    public ExampleGame(UUID uuid, GameSize gameSize) {
        this(gameSize);
    }

    @Override
    public ExampleGamePlayer defaultGamePlayer(UUID uuid, boolean spectator) {
        return new ExampleGamePlayer(uuid, 0, 0, spectator);
    }

    @Override
    public void load() {
        MultiThreading.schedule(() -> setState(GameState.WAIT), 6, TimeUnit.SECONDS);
    }

}

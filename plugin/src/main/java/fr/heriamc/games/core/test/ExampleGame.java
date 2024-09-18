package fr.heriamc.games.core.test;

import fr.heriamc.games.engine.GameSize;
import fr.heriamc.games.engine.SimpleGame;
import fr.heriamc.games.engine.utils.GameSizeTemplate;

import java.util.UUID;

public class ExampleGame extends SimpleGame<ExampleGamePlayer, ExampleGameSettings> {

    public ExampleGame(GameSize gameSize) {
        super("ffa", new ExampleGameSettings(GameSizeTemplate.FFA.toGameSize()));
    }

    public ExampleGame(UUID uuid, GameSize gameSize) {
        this(GameSizeTemplate.FFA.toGameSize());
    }

    @Override
    public ExampleGamePlayer defaultGamePlayer(UUID uuid, boolean spectator) {
        return new ExampleGamePlayer(uuid, 0, 0, spectator);
    }

    @Override
    public void load() {
        settings.getGameMapManager().setup();
    }

}

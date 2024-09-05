package fr.heriamc.games.core.test;

import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.games.api.DirectConnectStrategy;
import fr.heriamc.games.api.pool.GamePool;
import fr.heriamc.games.engine.GameSize;
import fr.heriamc.games.engine.MiniGame;
import fr.heriamc.games.engine.utils.GameSizeTemplate;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class ExampleGamePool extends GamePool<ExampleGame> {

    public ExampleGamePool() {
        super(ExampleGame.class,"Rush Pool", HeriaServerType.ONESHOT,1, 5, DirectConnectStrategy.RANDOM);
    }

    @Override
    public Supplier<ExampleGame> newGame() {
        return () -> new ExampleGame(GameSizeTemplate.SIZE_1V1.toGameSize());
    }

    @Override
    public Supplier<ExampleGame> newGame(Object... objects) {
        return () -> new ExampleGame(getArg(objects, GameSize.class));
    }

    @Override
    public Supplier<ExampleGame> newGame(UUID uuid, Object... objects) {
        return () -> new ExampleGame(uuid, getArg(objects, GameSize.class));
    }

    @Override
    public void useCustomStrategy(Player player) {
        Optional.ofNullable(gamesManager.getEmptyGames().getFirst())
                .filter(MiniGame::canJoin)
                .ifPresent(game -> game.joinGame(player));
    }

}
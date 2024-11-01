package fr.heriamc.games.core.test;

import fr.heriamc.api.game.size.GameSize;
import fr.heriamc.api.game.size.GameSizeTemplate;
import fr.heriamc.api.server.HeriaServerType;
import fr.heriamc.games.api.DirectConnectStrategy;
import fr.heriamc.games.api.pool.GamePool;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Supplier;

public class ExampleGamePool extends GamePool<ExampleGame> {

    /*
        Class permetant de créer les games sur demande
        le 1 correspond au minimum de game qui doit être lancer sur le serveur
        le 5 correspond au maxium de game qui peux être lancer sur le serveur
        la strategy correspond a ce qui se passe quand un joueur rejoint le serveur
     */
    public ExampleGamePool() {
        super(ExampleGame.class,"HikaBrain Pool", HeriaServerType.HIKABRAIN,1, 5, DirectConnectStrategy.FILL_GAME);
    }

    /*
        Nouvelle game sans argument
     */
    @Override
    public Supplier<ExampleGame> newGame() {
        return () -> new ExampleGame(GameSizeTemplate.SIZE_1V1.toGameSize());
    }

    /*
        Pour plussieurs arguments
     */
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

    }

}
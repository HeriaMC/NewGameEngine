package fr.heriamc.games.engine.event;

import fr.heriamc.games.engine.MiniGame;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Listener;

@Getter
@AllArgsConstructor
public abstract class GameListener<G extends MiniGame> implements Listener {

    protected final G game;

}
package fr.heriamc.games.core.test;

import fr.heriamc.games.engine.map.GameMapManager;
import fr.heriamc.games.engine.map.slime.SlimeMap;
import fr.heriamc.games.engine.map.slime.SlimeWorldLoader;

public class ExampleGameManager extends GameMapManager<ExampleGame, SlimeMap, SlimeWorldLoader> {

    public ExampleGameManager(ExampleGame game, SlimeWorldLoader mapLoader) {
        super(game, mapLoader);
    }

    @Override
    public void setup() {

    }

    @Override
    public void end() {

    }

}
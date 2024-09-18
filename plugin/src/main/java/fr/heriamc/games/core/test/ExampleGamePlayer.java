package fr.heriamc.games.core.test;

import fr.heriamc.games.engine.player.SimpleGamePlayer;

import java.util.UUID;

public class ExampleGamePlayer extends SimpleGamePlayer {

    public ExampleGamePlayer(UUID uuid, int kills, int deaths, boolean spectator) {
        super(uuid, kills, deaths, spectator);
    }

}
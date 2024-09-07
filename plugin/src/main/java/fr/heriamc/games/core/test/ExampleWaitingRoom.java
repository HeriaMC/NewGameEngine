package fr.heriamc.games.core.test;

import fr.heriamc.games.engine.waitingroom.GameWaitingRoom;

public class ExampleWaitingRoom extends GameWaitingRoom<ExampleGame, ExampleGamePlayer> {

    public ExampleWaitingRoom(ExampleGame game) {
        super(game);
    }

    @Override
    public void onJoin(ExampleGamePlayer gamePlayer) {

    }

    @Override
    public void onLeave(ExampleGamePlayer gamePlayer) {

    }

    @Override
    public void giveItems(ExampleGamePlayer gamePlayer) {

    }

}
package fr.heriamc.games.core.test;

import fr.heriamc.games.api.addon.GameAddon;

public class ExampleAddon extends GameAddon<ExampleGamePool> {

    public ExampleAddon(ExampleGamePool pool) {
        super(pool);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

}
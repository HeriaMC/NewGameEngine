package fr.heriamc.games.core.test;

import fr.heriamc.games.api.addon.GameAddon;

public class ExampleAddon extends GameAddon<ExampleGamePool> {

    /*
        Cette class extends de JavaPlugin
     */
    public ExampleAddon() {
        super(new ExampleGamePool());
    }

    @Override
    public void enable() {
        /*
            Permet de load un nombre prédéfini de game par défault
            Complétement optionel vous pouvez charger vos propres games par défault
         */
        pool.loadDefaultGames();

        registerListener();
        registerCommand();
    }

    @Override
    public void disable() {

    }

}
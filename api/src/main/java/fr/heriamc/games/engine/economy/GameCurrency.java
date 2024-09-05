package fr.heriamc.games.engine.economy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class GameCurrency<N extends Number> implements GameEconomy<N> {

    protected final String name;
    protected final String symbol;

    protected N wallet;

}
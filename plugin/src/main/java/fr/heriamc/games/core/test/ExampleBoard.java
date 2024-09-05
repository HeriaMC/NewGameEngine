package fr.heriamc.games.core.test;

import fr.heriamc.games.engine.board.GameBoard;

public class ExampleBoard extends GameBoard<ExampleGame, ExampleGamePlayer> {

    private int playerCount, kills, deaths;
    private String ratio;

    public ExampleBoard(ExampleGame game, ExampleGamePlayer gamePlayer) {
        super(game, gamePlayer);
    }

    @Override
    public void reloadData() {
        this.playerCount = game.getSize();
        this.kills = gamePlayer.getKills();
        this.deaths = gamePlayer.getDeaths();
        this.ratio = gamePlayer.getRatio();
    }

    @Override
    public void setLines(String ip) {
        objectiveSign.setDisplayName("§e§l» §6§lJUMP SCADE §e§l«");

        objectiveSign.setLine(2, "§3");
        objectiveSign.setLine(3, " §8■ §7Tué(s) : §c" + kills);
        objectiveSign.setLine(4, " §8■ §7Mort(s) : §c" + deaths);
        objectiveSign.setLine(5, " §8■ §7Ratio : §c" + ratio);
        objectiveSign.setLine(6, "§7");
        objectiveSign.setLine(7, " §8■ §7Connectés : §c" + playerCount);
        objectiveSign.setLine(8, "§1");
        objectiveSign.setLine(9, ip);

        objectiveSign.updateLines();
    }

}
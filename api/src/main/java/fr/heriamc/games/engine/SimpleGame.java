package fr.heriamc.games.engine;

import fr.heriamc.bukkit.game.GameState;
import fr.heriamc.games.api.GameApi;
import fr.heriamc.games.engine.event.player.GamePlayerJoinEvent;
import fr.heriamc.games.engine.event.player.GamePlayerLeaveEvent;
import fr.heriamc.games.engine.player.BaseGamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class SimpleGame<G extends BaseGamePlayer, S extends GameSettings<?>> implements MiniGame {

    protected final String name, id;
    protected final S settings;

    protected final ConcurrentMap<UUID, G> players;

    protected final GameSize gameSize;

    protected GameState state;
    protected AtomicInteger playerCount;

    public SimpleGame(String name, S settings) {
        this.name = name;
        this.id = RandomStringUtils.randomAlphanumeric(8);
        this.settings = settings;
        this.gameSize = settings.getGameSize();
        this.players = new ConcurrentHashMap<>(gameSize.calculateMapCapacity());
        this.state = GameState.LOADING;
        this.playerCount = new AtomicInteger(0);
        this.preload();
    }

    public abstract G defaultGamePlayer(UUID uuid, boolean spectator);

    @Override
    public void preload() {

    }

    @Override
    public void unload() {
        //getEventBus().unregisterAll();
        settings.endMapManager();
        GameApi.getInstance().getGamePoolManager().getGamePool(getClass()).getGamesManager().removeGame(this);
    }

    @Override
    public void endGame() {
        players.values().forEach(this::leaveGame);
        settings.disableBoard();
        unload();
    }

    @Override
    public void joinGame(Player player, boolean spectator) {
        players.computeIfAbsent(player.getUniqueId(), uuid -> {
            final var gamePlayer = defaultGamePlayer(uuid, spectator);

            playerCount.incrementAndGet();
            settings.addBoardViewer(this, gamePlayer);

            Bukkit.getOnlinePlayers().stream()
                    .filter(Predicate.not(this::containsPlayer))
                    .forEach(player::hidePlayer);

            Bukkit.getPluginManager().callEvent(new GamePlayerJoinEvent<>(this, gamePlayer));

            log.info("[{}] {} joined game.", getFullName(), player.getName());
            return gamePlayer;
        });
    }

    @Override
    public void leaveGame(UUID uuid) {
        ifContainsPlayer(uuid, gamePlayer -> {
            playerCount.decrementAndGet();
            settings.removeBoardViewer(uuid);

            Bukkit.getPluginManager().callEvent(new GamePlayerLeaveEvent<>(this, gamePlayer));
            players.remove(uuid);

            log.info("[{}] {} leave game.", getFullName(), uuid.toString());
        });
    }

    public void leaveGame(BaseGamePlayer gamePlayer) {
        leaveGame(gamePlayer.getUuid());
    }

    public List<G> getAlivePlayers() {
        return players.values().stream().filter(BaseGamePlayer::isAlive).toList();
    }

    public List<G> getSpectators() {
        return players.values().stream().filter(BaseGamePlayer::isSpectator).toList();
    }

    public Optional<G> getPlayer(UUID uuid) {
        return Optional.ofNullable(players.get(uuid));
    }

    public Optional<G> getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    public G getNullablePlayer(UUID uuid) {
        return players.get(uuid);
    }

    public G getNullablePlayer(Player player) {
        return getNullablePlayer(player.getUniqueId());
    }

    public void checkSetting(boolean condition, Runnable trueRunnable, Runnable falseRunnable) {
        if (condition) trueRunnable.run();
        else falseRunnable.run();
    }

    public void checkSetting(boolean condition, Runnable trueRunnable) {
        checkSetting(condition, trueRunnable, () -> {});
    }

    public void checkGameState(GameState state, Runnable runnable) {
        if (this.state.equals(state))
            runnable.run();
    }

    public void checkGameState(GameState state, Runnable runnable, Runnable elseRunnable) {
        if (this.state.equals(state)) runnable.run();
        else elseRunnable.run();
    }

    public void checkGameState(Function<GameState, Runnable> function) {
        function.apply(state).run();
    }

    public void ifContainsPlayer(UUID uuid, Consumer<G> consumer) {
        final var gamePlayer = players.get(uuid);

        if (gamePlayer != null)
            consumer.accept(gamePlayer);
    }

    public void ifContainsPlayer(Player player, Consumer<G> consumer) {
        ifContainsPlayer(player.getUniqueId(), consumer);
    }

    public void ifContainsPlayer(BaseGamePlayer gamePlayer, Consumer<G> consumer) {
        ifContainsPlayer(gamePlayer.getUuid(), consumer);
    }

    @Override
    public void broadcast(String message) {
        players.values().forEach(gamePlayer -> gamePlayer.sendMessage(message));
    }

    @Override
    public void broadcast(String... message) {
        players.values().forEach(gamePlayer -> gamePlayer.sendMessage(message));
    }

    public void broadcast(Predicate<G> filter, String message) {
        players.values().stream().filter(filter).forEach(gamePlayer -> gamePlayer.sendMessage(message));
    }

    public void broadcast(Predicate<G> filter, String... messages) {
        players.values().stream().filter(filter).forEach(gamePlayer -> gamePlayer.sendMessage(messages));
    }

    @Override
    public boolean containsPlayer(UUID uuid) {
        return players.containsKey(uuid);
    }

    @Override
    public boolean canStart() {
        return /*getAlivePlayersCount()*/ playerCount.get() >= gameSize.getMinPlayer();
    }

    @Override
    public boolean isFull() {
        return /*getAlivePlayersCount()*/ playerCount.get() == gameSize.getMaxPlayer();
    }

    @Override
    public boolean canJoin() {
        return /*getAlivePlayersCount()*/ playerCount.get() < gameSize.getMaxPlayer();
    }

    @Override
    public int getAlivePlayersCount() {
        return getAlivePlayers().size();
    }

    @Override
    public int getSpectatorsCount() {
        return getSpectators().size();
    }

    @Override
    public int getSize() {
        return playerCount.get();
    }

    @Override
    public void sendDebugInfoMessage(CommandSender sender) {
        sender.sendMessage("§m-----------------------------");
        sender.sendMessage("Game: " + getFullName());
        sender.sendMessage("Size: type=" + gameSize.getName() + ", min=" + gameSize.getMinPlayer() + ", max=" + gameSize.getMaxPlayer() + ", tn=" + gameSize.getTeamNeeded() + ", tm=" + gameSize.getTeamMaxPlayer());
        sender.sendMessage("Condition: cj=" + canJoin() + ", cs=" + canStart() + ", isf=" + isFull());
        sender.sendMessage("State: " + state);

        sender.sendMessage("Locations: ");
        //getSettings().getSpawnPoints().stream().map(SpawnPoint::getDebugMessage).forEach(sender::sendMessage);

        sender.sendMessage("Players: " + playerCount + " (" + getAlivePlayersCount() + "|" + getSpectatorsCount() + ")");
        sender.sendMessage("Alive players: " + getAlivePlayers().stream().map(BaseGamePlayer::getPlayer).map(Player::getName).collect(Collectors.joining(", ")));
        sender.sendMessage("Spectator players: " + getSpectators().stream().map(BaseGamePlayer::getPlayer).map(Player::getName).collect(Collectors.joining(", ")));
        sender.sendMessage("§m-----------------------------");
    }

}
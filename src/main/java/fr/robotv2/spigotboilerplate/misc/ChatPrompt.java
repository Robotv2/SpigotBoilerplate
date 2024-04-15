package fr.robotv2.spigotboilerplate.misc;

import fr.robotv2.spigotboilerplate.SpigotBoilerplate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ChatPrompt implements Listener {

    private static final String CANCEL_TEXT = "cancel";
    private static final Map<Player, Prompt> prompts = new HashMap<>();

    static {
        new ChatPrompt();
    }

    /**
     * Prompts a player with callbacks for player response and cancelling
     *
     * @param player            The player to prompt
     * @param prompt            The prompt to send to the player, or null for no prompt
     * @param onResponse        The callback for when the player responds
     * @param onCancel          The callback for when the prompt is cancelled
     */
    public static void prompt(Player player, String prompt, Consumer<String> onResponse, Consumer<CancelReason> onCancel) {

        Prompt removed = prompts.remove(player);

        if (removed != null) {
            removed.cancel(CancelReason.PROMPT_OVERRIDDEN);

        }
        prompts.put(player, new Prompt(onResponse, onCancel));
        Optional.ofNullable(prompt).ifPresent(player::sendMessage);
    }

    /**
     * Prompts a player with callbacks for player response and cancelling
     *
     * @param player            The player to prompt
     * @param prompt            The prompt to send to the player, or null for no prompt
     * @param onResponse        The callback for when the player responds
     */
    public static void prompt(Player player, String prompt, Consumer<String> onResponse) {
        ChatPrompt.prompt(player, prompt, onResponse, (reason) -> { });
    }

    private ChatPrompt() {
        Bukkit.getPluginManager().registerEvents(this, SpigotBoilerplate.INSTANCE.getPlugin());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {

        Prompt p = prompts.remove(e.getPlayer());

        if (p == null) {
            return;
        }

        e.setCancelled(true);

        if (e.getMessage().equalsIgnoreCase(CANCEL_TEXT)) {
            p.cancel(CancelReason.PLAYER_CANCELLED);
            return;
        }

        p.respond(e.getMessage());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Prompt p = prompts.remove(event.getPlayer());
        if (p != null) {
            p.cancel(CancelReason.PLAYER_LEFT);
        }
    }

    private record Prompt(Consumer<String> onResponse, Consumer<CancelReason> onCancel) {

        public void respond(String response) {
            onResponse.accept(response);
        }

        public void cancel(CancelReason reason) {
            onCancel.accept(reason);
        }

    }

    public enum CancelReason {
        /**
         * Passed when the player was given another prompt. This prompt is removed and cancelled.
         */
        PROMPT_OVERRIDDEN,
        /**
         * Passed when the prompt was cancelled because the player typed 'cancel'.
         */
        PLAYER_CANCELLED,
        /**
         * Passed when the prompt was cancelled because the player left the server.
         */
        PLAYER_LEFT
    }
}

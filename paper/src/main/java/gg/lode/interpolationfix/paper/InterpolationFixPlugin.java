package gg.lode.interpolationfix.paper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Opts this server in to Interpolation Fix. Listening on a channel is what makes the server list
 * it in the {@code minecraft:register} packet each player gets on join, and that list is all the
 * mod checks. Nothing is ever sent or received on these channels.
 *
 * <p>Clients using the fix register the channel back, so an anticheat can call
 * {@link #usesLinearInterpolation(Player)} to know which players see other entities the 26.2 way.
 */
public final class InterpolationFixPlugin extends JavaPlugin implements Listener {
    public static final String CHANNEL = "interpolationfix:linear_3";
    public static final String ANTISTUTTER_CHANNEL = "antistutter:linear_3";

    private final List<String> channels = new ArrayList<>();
    private boolean logPlayers;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.logPlayers = this.getConfig().getBoolean("log-players", false);
        if (this.getConfig().getBoolean("channels.interpolationfix", true)) {
            this.channels.add(CHANNEL);
        }
        if (this.getConfig().getBoolean("channels.antistutter", true)) {
            this.channels.add(ANTISTUTTER_CHANNEL);
        }
        for (String channel : this.channels) {
            this.getServer().getMessenger().registerIncomingPluginChannel(this, channel, (name, player, message) -> {
            });
        }
        if (this.channels.isEmpty()) {
            this.getLogger().warning("Both channels are disabled in config.yml, so no client will opt in.");
        } else if (!this.getServer().getOnlinePlayers().isEmpty()) {
            this.getLogger().info("Players already online opt in the next time they join.");
        }
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
        this.channels.clear();
    }

    /** Whether the player's client is using the 26.2 interpolation on this server. */
    public static boolean usesLinearInterpolation(Player player) {
        Set<String> listening = player.getListeningPluginChannels();
        return listening.contains(CHANNEL) || listening.contains(ANTISTUTTER_CHANNEL);
    }

    @EventHandler
    public void onRegister(PlayerRegisterChannelEvent event) {
        if (this.logPlayers && this.channels.contains(event.getChannel())) {
            this.getLogger().info(event.getPlayer().getName() + " is using 26.2 interpolation (" + event.getChannel() + ")");
        }
    }

    @EventHandler
    public void onUnregister(PlayerUnregisterChannelEvent event) {
        if (this.logPlayers && this.channels.contains(event.getChannel())) {
            this.getLogger().info(event.getPlayer().getName() + " stopped using 26.2 interpolation (" + event.getChannel() + ")");
        }
    }
}

package gg.lode.interpolationfix;

import com.mojang.blaze3d.platform.InputConstants;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the mode and its keybind. The mode is saved to {@code config/interpolationfix.properties}
 * so it survives a restart.
 */
public final class InterpolationFix implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("Interpolation Fix");
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("interpolationfix.properties");

    private static Mode mode = load();
    private static KeyMapping cycleKey;

    @Override
    public void onInitializeClient() {
        // Unbound by default so it never steals a key from the player or another mod.
        cycleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.interpolationfix.toggle", InputConstants.UNKNOWN.getValue(), KeyMapping.Category.MISC));
        ClientTickEvents.END_CLIENT_TICK.register(InterpolationFix::handleKeybind);
        ServerOptIn.register();
    }

    public static Mode getMode() {
        return mode;
    }

    public static void setMode(Mode newMode) {
        if (newMode == mode) {
            return;
        }
        mode = newMode;
        save();
        ServerOptIn.sync();
    }

    /** Whether entities should use the 26.2 smoothing right now. */
    public static boolean isEnabled() {
        return switch (mode) {
            case ALWAYS -> true;
            case OFF -> false;
            case SERVER -> ServerOptIn.isOptedIn();
        };
    }

    private static void handleKeybind(Minecraft minecraft) {
        while (cycleKey.consumeClick()) {
            setMode(mode.next());
            if (minecraft.player != null) {
                minecraft.player.sendOverlayMessage(Component.translatable("interpolationfix.mode.changed", mode.label()));
            }
        }
    }

    private static Mode load() {
        if (!Files.exists(CONFIG)) {
            return Mode.SERVER;
        }
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(CONFIG)) {
            properties.load(reader);
        } catch (IOException e) {
            LOGGER.warn("Could not read {}, defaulting to server opt-in", CONFIG, e);
            return Mode.SERVER;
        }
        String id = properties.getProperty("mode");
        if (id == null) {
            // 1.1.0 saved an on/off switch. Someone who left it on chose it everywhere.
            String enabled = properties.getProperty("enabled");
            return enabled == null ? Mode.SERVER : Boolean.parseBoolean(enabled) ? Mode.ALWAYS : Mode.OFF;
        }
        return Mode.fromId(id, Mode.SERVER);
    }

    private static void save() {
        Properties properties = new Properties();
        properties.setProperty("mode", mode.id());
        try (Writer writer = Files.newBufferedWriter(CONFIG)) {
            properties.store(writer, "Interpolation Fix: mode is server, always or off");
        } catch (IOException e) {
            LOGGER.warn("Could not write {}", CONFIG, e);
        }
    }
}

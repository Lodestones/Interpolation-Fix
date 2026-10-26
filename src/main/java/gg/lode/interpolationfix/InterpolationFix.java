package gg.lode.interpolationfix;

import com.mojang.blaze3d.platform.InputConstants;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the on/off switch and its keybind. The switch is saved to
 * {@code config/interpolationfix.properties} so it survives a restart.
 */
public final class InterpolationFix {
    private static final Logger LOGGER = LoggerFactory.getLogger("Interpolation Fix");
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("interpolationfix.properties");

    // Unbound by default so it never steals a key from the player or another mod.
    public static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "key.interpolationfix.toggle", InputConstants.UNKNOWN.getValue(), KeyMapping.Category.MISC);

    private static boolean enabled = load();

    private InterpolationFix() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void handleKeybind(Minecraft minecraft) {
        while (TOGGLE_KEY.consumeClick()) {
            enabled = !enabled;
            save();
            if (minecraft.player != null) {
                minecraft.player.sendOverlayMessage(Component.translatable(
                        enabled ? "interpolationfix.toggle.on" : "interpolationfix.toggle.off"));
            }
        }
    }

    private static boolean load() {
        if (!Files.exists(CONFIG)) {
            return true;
        }
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(CONFIG)) {
            properties.load(reader);
        } catch (IOException e) {
            LOGGER.warn("Could not read {}, defaulting to enabled", CONFIG, e);
            return true;
        }
        return Boolean.parseBoolean(properties.getProperty("enabled", "true"));
    }

    private static void save() {
        Properties properties = new Properties();
        properties.setProperty("enabled", Boolean.toString(enabled));
        try (Writer writer = Files.newBufferedWriter(CONFIG)) {
            properties.store(writer, "Interpolation Fix");
        } catch (IOException e) {
            LOGGER.warn("Could not write {}", CONFIG, e);
        }
    }
}

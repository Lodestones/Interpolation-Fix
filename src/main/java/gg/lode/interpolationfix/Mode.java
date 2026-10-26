package gg.lode.interpolationfix;

import java.util.Locale;
import net.minecraft.network.chat.Component;

/** When the 26.2 smoothing is used. */
public enum Mode {
    /** Only on servers that opt in over a plugin channel, and in singleplayer. */
    SERVER,
    /** Everywhere, including servers whose anticheat expects 26.3. */
    ALWAYS,
    /** Never; vanilla 26.3 smoothing. */
    OFF;

    public String id() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public Component label() {
        return Component.translatable("interpolationfix.mode." + this.id());
    }

    public Component description() {
        return Component.translatable("interpolationfix.mode." + this.id() + ".description");
    }

    public Mode next() {
        Mode[] modes = values();
        return modes[(this.ordinal() + 1) % modes.length];
    }

    public static Mode fromId(String id, Mode fallback) {
        for (Mode mode : values()) {
            if (mode.id().equalsIgnoreCase(id)) {
                return mode;
            }
        }
        return fallback;
    }
}

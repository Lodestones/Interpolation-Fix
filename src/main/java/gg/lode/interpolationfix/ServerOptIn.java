package gg.lode.interpolationfix;

import java.util.List;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ServerboundPlayChannelEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The server opt-in handshake. A server opts in by registering one of the channels through
 * {@code minecraft:register}. While the fix is in use the client registers the same channel back,
 * so the server's anticheat can tell which players to model with 26.2 interpolation. Nothing is
 * ever sent on either channel.
 *
 * <p>{@code antistutter:linear_3} is LokaMC's AntiStutter channel, accepted so servers that already
 * support AntiStutter work without changes. Fabric forgets the server's channels on disconnect and
 * on every proxy server switch, so opting in never carries over to another server.
 */
public final class ServerOptIn {
    private static final Logger LOGGER = LoggerFactory.getLogger("Interpolation Fix");
    public static final Identifier CHANNEL = Identifier.fromNamespaceAndPath("interpolationfix", "linear_3");
    public static final Identifier ANTISTUTTER_CHANNEL = Identifier.fromNamespaceAndPath("antistutter", "linear_3");

    private static final List<CustomPacketPayload.Type<OptInPayload>> TYPES = List.of(
            new CustomPacketPayload.Type<>(CHANNEL),
            new CustomPacketPayload.Type<>(ANTISTUTTER_CHANNEL));

    private ServerOptIn() {
    }

    static void register() {
        for (CustomPacketPayload.Type<OptInPayload> type : TYPES) {
            PayloadTypeRegistry.clientboundPlay().register(type, StreamCodec.unit(new OptInPayload(type)));
        }
        // Velocity sends its channels before the login packet, when there's no connection to
        // register back on yet, so JOIN catches up on anything the REGISTER event had to skip.
        ServerboundPlayChannelEvents.REGISTER.register((listener, sender, client, channels) -> sync());
        ServerboundPlayChannelEvents.UNREGISTER.register((listener, sender, client, channels) -> sync());
        ClientPlayConnectionEvents.JOIN.register((listener, sender, client) -> sync());
    }

    /** Whether the server we're connected to opted in. Singleplayer has no anticheat, so it counts. */
    public static boolean isOptedIn() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.isLocalServer()) {
            return true;
        }
        return ClientPlayNetworking.canSend(CHANNEL) || ClientPlayNetworking.canSend(ANTISTUTTER_CHANNEL);
    }

    /** Registers back each channel the server opted in with while the fix is in use, and drops it otherwise. */
    static void sync() {
        if (Minecraft.getInstance().getConnection() == null) {
            return;
        }
        boolean active = InterpolationFix.isEnabled();
        for (CustomPacketPayload.Type<OptInPayload> type : TYPES) {
            boolean wanted = active && ClientPlayNetworking.canSend(type.id());
            boolean registered = ClientPlayNetworking.getReceived().contains(type.id());
            if (wanted && !registered) {
                LOGGER.info("Server opted in on {}, using 26.2 interpolation", type.id());
                ClientPlayNetworking.registerReceiver(type, (payload, context) -> {
                });
            } else if (!wanted && registered) {
                LOGGER.info("Stopped using 26.2 interpolation, unregistering {}", type.id());
                ClientPlayNetworking.unregisterReceiver(type.id());
            }
        }
    }

    private record OptInPayload(CustomPacketPayload.Type<OptInPayload> type) implements CustomPacketPayload {
    }
}

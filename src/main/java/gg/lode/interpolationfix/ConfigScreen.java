package gg.lode.interpolationfix;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/** The Mod Menu settings screen: the mode, and whether the current server opted in. */
public class ConfigScreen extends Screen {
    private static final Component TITLE = Component.translatable("interpolationfix.config.title");
    private static final int WIDTH = 260;

    private final Screen lastScreen;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public ConfigScreen(Screen lastScreen) {
        super(TITLE);
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(TITLE, this.font);
        LinearLayout content = this.layout.addToContents(LinearLayout.vertical()).spacing(8);
        content.defaultCellSetting().alignHorizontallyCenter();
        content.addChild(CycleButton.builder(Mode::label, InterpolationFix.getMode())
                .withValues(Mode.values())
                .withTooltip(mode -> Tooltip.create(mode.description()))
                .create(0, 0, WIDTH, 20, Component.translatable("interpolationfix.config.mode"),
                        (button, mode) -> InterpolationFix.setMode(mode)));
        content.addChild(new MultiLineTextWidget(this.serverStatus(), this.font).setMaxWidth(WIDTH).setCentered(true));
        content.addChild(new MultiLineTextWidget(Component.translatable("interpolationfix.config.warning"), this.font)
                .setMaxWidth(WIDTH).setCentered(true));
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
        this.layout.arrangeElements();
        this.layout.visitWidgets(this::addRenderableWidget);
    }

    private Component serverStatus() {
        if (this.minecraft.getConnection() == null) {
            return Component.translatable("interpolationfix.config.status.offline");
        }
        if (this.minecraft.isLocalServer()) {
            return Component.translatable("interpolationfix.config.status.singleplayer");
        }
        return Component.translatable(ServerOptIn.isOptedIn()
                ? "interpolationfix.config.status.opted_in"
                : "interpolationfix.config.status.not_opted_in");
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.lastScreen);
    }
}

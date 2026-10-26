package gg.lode.interpolationfix;

import net.minecraft.core.PositionAndRotation;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.LinearInterpolationHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PositionPath;
import net.minecraft.world.entity.SteppedInterpolationHandler;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Entities build their interpolation handler once, so this one keeps both the 26.2 (linear) and
 * 26.3 (stepped) handlers and forwards to whichever the toggle picks. On a switch the old handler
 * is cancelled; the entity holds still until the next move packet, at most a couple of ticks.
 */
public final class ToggleableInterpolationHandler implements InterpolationHandler {
    private final InterpolationHandler linear;
    private final InterpolationHandler stepped;
    private InterpolationHandler active;

    public ToggleableInterpolationHandler(LivingEntity entity) {
        this.linear = LinearInterpolationHandler.create(entity);
        this.stepped = SteppedInterpolationHandler.create(entity);
        this.active = this.selected();
    }

    private InterpolationHandler selected() {
        return InterpolationFix.isEnabled() ? this.linear : this.stepped;
    }

    private InterpolationHandler active() {
        InterpolationHandler selected = this.selected();
        if (selected != this.active) {
            this.active.cancel();
            this.active = selected;
        }
        return this.active;
    }

    @Override
    public @Nullable PositionAndRotation target() {
        return this.active().target();
    }

    @Override
    public boolean interpolateTo(@Nullable PositionPath position, float yRot, float xRot, boolean hasRotation) {
        return this.active().interpolateTo(position, yRot, xRot, hasRotation);
    }

    @Override
    public void interpolate() {
        this.active().interpolate();
    }

    @Override
    public void applyPredictedMovement(Vec3 delta) {
        this.active().applyPredictedMovement(delta);
    }

    @Override
    public boolean hasActiveInterpolation() {
        return this.active().hasActiveInterpolation();
    }

    @Override
    public void cancel() {
        this.active().cancel();
    }
}

package com.ref.moremorelang.mixin.emi;

import com.ref.moremorelang.MoremorelangConfig;
import com.ref.moremorelang.lang.ComponentTranslator;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.FluidEmiStack;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = FluidEmiStack.class, remap = false)
@OnlyIn(Dist.CLIENT)
public abstract class FluidEmiStackMixin {

  @Shadow
  public abstract Component getName();

  @Inject(
      method = "getTooltip()Ljava/util/List;",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Ldev/emi/emi/api/render/EmiTooltipComponents;appendModName(Ljava/util/List;Ljava/lang/String;)V"),
      locals = LocalCapture.CAPTURE_FAILHARD,
      remap = false,
      require = 0)
  private void addFluidTooltip(
      CallbackInfoReturnable<List<ClientTooltipComponent>> cir, List<ClientTooltipComponent> list) {
    for (String langCode : MoremorelangConfig.moreLanguages) {
      list.add(
          EmiTooltipComponents.of(
              ComponentTranslator.translateComponent(this.getName(), langCode)));
    }
  }
}

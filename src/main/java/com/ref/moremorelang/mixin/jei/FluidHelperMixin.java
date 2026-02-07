package com.ref.moremorelang.mixin.jei;

import com.ref.moremorelang.MoremorelangConfig;
import com.ref.moremorelang.lang.ComponentTranslator;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.forge.platform.FluidHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FluidHelper.class, remap = false)
@OnlyIn(Dist.CLIENT)
public abstract class FluidHelperMixin {

  @Shadow
  public abstract Component getDisplayName(FluidStack ingredient);

  @Inject(
      method =
          "getTooltip(Lmezz/jei/api/gui/builder/ITooltipBuilder;Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraft/world/item/TooltipFlag;)V",
      at = @At("TAIL"),
      remap = false,
      require = 0)
  private void FluidTooltips(
      ITooltipBuilder tooltip, FluidStack ingredient, TooltipFlag tooltipFlag, CallbackInfo ci) {
    if (MoremorelangConfig.shouldHide(tooltipFlag.isAdvanced())) {
      return;
    }
    for (String langCode : MoremorelangConfig.moreLanguages) {
      tooltip.add(
          ComponentTranslator.translateComponent(this.getDisplayName(ingredient), langCode));
    }
  }
}

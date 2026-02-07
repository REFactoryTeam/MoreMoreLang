package com.ref.moremorelang.event;

import com.ref.moremorelang.Moremorelang;
import com.ref.moremorelang.MoremorelangConfig;
import com.ref.moremorelang.lang.ComponentTranslator;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = Moremorelang.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.FORGE,
    value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class MoremorelangClientForgeEvent {
  @SubscribeEvent
  public static void onItemTooltip(ItemTooltipEvent event) {
    if (MoremorelangConfig.shouldHide(event.getFlags().isAdvanced())) {
      return;
    }
    ItemStack stack = event.getItemStack();
    List<Component> tooltips = event.getToolTip();
    for (String langCode : MoremorelangConfig.moreLanguages) {
      tooltips.add(ComponentTranslator.translateComponent(stack.getHoverName(), langCode));
    }
  }
}

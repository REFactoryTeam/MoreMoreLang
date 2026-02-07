package com.ref.moremorelang.integration.jade;

import appeng.api.integrations.igtooltip.TooltipContext;
import appeng.api.parts.IPartHost;
import appeng.integration.modules.igtooltip.parts.PartHostTooltips;
import com.ref.moremorelang.Moremorelang;
import com.ref.moremorelang.MoremorelangConfig;
import com.ref.moremorelang.lang.ComponentTranslator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public enum MoremorelangProvider implements IBlockComponentProvider, IEntityComponentProvider {
  INSTANCE;

  public final ResourceLocation ID =
      ResourceLocation.fromNamespaceAndPath(Moremorelang.MOD_ID, "moremorelang_provider");

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    if (MoremorelangConfig.shouldHide(config.getEnum(this.ID))) {
      return;
    }
    Component translator = accessor.getBlock().getName();
    if (Moremorelang.IS_AE2_LOADED) {
      Component ae2Name = IPartHost_Name(accessor);
      if (ae2Name != null) {
        translator = ae2Name;
      }
    }
    for (String langCode : MoremorelangConfig.moreLanguages) {
      tooltip.add(ComponentTranslator.translateComponent(translator, langCode));
    }
  }

  private Component IPartHost_Name(BlockAccessor accessor) {
    if (accessor.getBlockEntity() instanceof IPartHost partHost) {
      return PartHostTooltips.getName(
          partHost,
          new TooltipContext(
              accessor.getServerData(),
              accessor.getHitResult().getLocation(),
              accessor.getPlayer()));
    }
    return null;
  }

  @Override
  public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
    if (MoremorelangConfig.shouldHide(config.getEnum(this.ID))) {
      return;
    }
    if (accessor.getEntity() instanceof Player) {
      return;
    }
    Component translator;
    if (accessor.getEntity() instanceof ItemEntity itemEntity) {
      translator = itemEntity.getItem().getHoverName();
    } else {
      translator = accessor.getEntity().getName();
    }
    for (String langCode : MoremorelangConfig.moreLanguages) {
      tooltip.add(ComponentTranslator.translateComponent(translator, langCode));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return this.ID;
  }

  public boolean isRequired() {
    return true;
  }
}

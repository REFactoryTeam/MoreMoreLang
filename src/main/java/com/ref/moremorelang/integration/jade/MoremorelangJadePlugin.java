package com.ref.moremorelang.integration.jade;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MoremorelangJadePlugin implements IWailaPlugin {
  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerBlockComponent(MoremorelangProvider.INSTANCE, Block.class);
    registration.registerEntityComponent(MoremorelangProvider.INSTANCE, Entity.class);
  }
}

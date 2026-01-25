package com.ref.moremorelang;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Moremorelang.MOD_ID)
public class Moremorelang {

  public static final String MOD_ID = "moremorelang";

  private static final Logger LOGGER = LogUtils.getLogger();

  public static final boolean IS_AE2_LOADED = ModList.get().isLoaded("ae2");

  public Moremorelang(FMLJavaModLoadingContext context) {
    context.registerConfig(ModConfig.Type.CLIENT, MoremorelangConfig.SPEC);
  }
}

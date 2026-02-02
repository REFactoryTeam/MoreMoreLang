package com.ref.moremorelang;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Moremorelang.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoremorelangConfig {
  private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

  private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MORE_LANGUAGES =
      BUILDER
          .comment(
              "A list of language codes to display in tooltips, in order. E.g., [\"en_us\", \"zh_cn\"]")
          .defineListAllowEmpty(
              "MoreLanguages", List.of(), MoremorelangConfig::validateLanguageCode);

  private static final ForgeConfigSpec.ConfigValue<Boolean> IS_MIXIN =
      BUILDER
          .comment("Whether to use Mixin for language control.[Requires Game Restart]")
          .define("UseMixinTranslator", true);

  static final ForgeConfigSpec SPEC = BUILDER.build();

  public static List<? extends String> moreLanguages;

  public static boolean useMixinTranslator;

  public static boolean resourcesLoaded;

  private static boolean validateLanguageCode(final Object obj) {
    if (!(obj instanceof final String langCode) || langCode.isBlank()) {
      return false;
    }
    return isLanguageAvailable(langCode);
  }

  private static boolean isLanguageAvailable(String langCode) {
    if (!resourcesLoaded) {
      return true;
    }
    return Minecraft.getInstance().getLanguageManager().getLanguage(langCode) != null;
  }

  @SubscribeEvent
  static void onConfigLoad(final ModConfigEvent.Loading event) {
    if (event.getConfig().getSpec() != SPEC) return;
    useMixinTranslator = IS_MIXIN.get();
  }

  @SubscribeEvent
  static void onConfigUpdate(final ModConfigEvent event) {
    if (event.getConfig().getSpec() != SPEC) return;
    moreLanguages = MORE_LANGUAGES.get();
  }

  @SubscribeEvent
  public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
    event.registerReloadListener(
        (ResourceManagerReloadListener) resourceManager -> resourcesLoaded = true);
  }
}

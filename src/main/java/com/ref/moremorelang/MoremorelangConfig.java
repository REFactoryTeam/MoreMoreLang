package com.ref.moremorelang;

import com.ref.moremorelang.lang.DisplayMode;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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

  private static final ForgeConfigSpec.EnumValue<DisplayMode> DISPLAY_MODE =
      BUILDER
          .comment("When to show: ALWAYS, ONLY_SHIFT, ONLY_ADVANCED, SHIFT_AND_ADVANCED")
          .defineEnum("DisplayMode", DisplayMode.ONLY_ADVANCED);

  static final ForgeConfigSpec SPEC = BUILDER.build();

  public static List<? extends String> moreLanguages;

  public static boolean useMixinTranslator;

  public static boolean resourcesLoaded;

  public static DisplayMode displayMode;

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

  public static boolean shouldHide(DisplayMode mode) {
    return shouldHide(Minecraft.getInstance().options.advancedItemTooltips, mode);
  }

  public static boolean shouldHide(boolean advanced) {
    final DisplayMode mode = displayMode != null ? displayMode : DisplayMode.ONLY_ADVANCED;
    return shouldHide(advanced, mode);
  }

  public static boolean shouldHide(boolean advanced, DisplayMode mode) {
    final boolean shiftDown = Screen.hasShiftDown();
    return shouldHide(shiftDown, advanced, mode);
  }

  public static boolean shouldHide(boolean shiftDown, boolean advanced, DisplayMode mode) {
    return !switch (mode) {
      case ALWAYS -> true;
      case ONLY_SHIFT -> shiftDown;
      case ONLY_ADVANCED -> advanced;
      case SHIFT_AND_ADVANCED -> shiftDown && advanced;
    };
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
    displayMode = DISPLAY_MODE.get();
  }

  @SubscribeEvent
  public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
    event.registerReloadListener(
        (ResourceManagerReloadListener) resourceManager -> resourcesLoaded = true);
  }
}

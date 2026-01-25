package com.ref.moremorelang.lang;

import com.ref.moremorelang.Moremorelang;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.locale.Language;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(
    modid = Moremorelang.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class LanguageUtils {

  private static final Map<String, Language> LANGUAGE_CACHE = new ConcurrentHashMap<>();

  public static Language getLanguage(String langCode) {
    Language language =
        LANGUAGE_CACHE.computeIfAbsent(langCode, LanguageUtils::loadLanguageInternal);
    return Objects.requireNonNullElseGet(language, Language::getInstance);
  }

  private static @NotNull Language loadLanguageInternal(String langCode) {
    Minecraft mc = Minecraft.getInstance();
    ResourceManager resourceManager = mc.getResourceManager();
    LanguageManager languageManager = mc.getLanguageManager();

    LanguageInfo languageInfo = languageManager.getLanguage(langCode);
    boolean isBidirectional = languageInfo != null && languageInfo.bidirectional();

    return ClientLanguage.loadFrom(
        resourceManager, Collections.singletonList(langCode), isBidirectional);
  }

  @SubscribeEvent
  public static void onRegisterReloadListeners(@NotNull RegisterClientReloadListenersEvent event) {
    event.registerReloadListener(
        (ResourceManagerReloadListener) (resourceManager) -> LANGUAGE_CACHE.clear());
  }
}

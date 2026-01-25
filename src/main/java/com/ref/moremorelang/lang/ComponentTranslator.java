package com.ref.moremorelang.lang;

import com.ref.moremorelang.MoremorelangConfig;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.NotNull;

public class ComponentTranslator {

  public static @NotNull Component translateComponent(Component original, String targetLangCode) {
    return translateComponent(original, LanguageUtils.getLanguage(targetLangCode));
  }

  public static @NotNull Component translateComponent(Component original, Language targetLanguage) {
    return processComponent(original, targetLanguage);
  }

  private static @NotNull MutableComponent processComponent(
      @NotNull Component original, @NotNull Language targetLanguage) {
    ComponentContents originalContents = original.getContents();
    ComponentContents newContents;
    if (originalContents instanceof TranslatableContents translatable) {
      Object[] originalArgs = translatable.getArgs();
      Object[] newArgs = originalArgs;
      boolean argsModified = false;
      if (originalArgs.length > 0) {
        Object[] tempArgs = new Object[originalArgs.length];
        for (int i = 0; i < originalArgs.length; i++) {
          Object arg = originalArgs[i];
          if (arg instanceof Component componentArg) {
            tempArgs[i] = processComponent(componentArg, targetLanguage);
            argsModified = true;
          } else {
            tempArgs[i] = arg;
          }
        }
        if (argsModified) {
          newArgs = tempArgs;
        }
      }
      TranslatableContents baseTranslatable =
          new TranslatableContents(translatable.getKey(), translatable.getFallback(), newArgs);
      if (MoremorelangConfig.useMixinTranslator) {
        ((ControllableLanguageComponent) baseTranslatable)
            .moreMoreLang$setTargetLanguage(targetLanguage);
        newContents = baseTranslatable;
      } else {
        newContents = new ControllableTranslatableContents(baseTranslatable, targetLanguage);
      }
    } else {
      newContents = originalContents;
    }

    MutableComponent result = MutableComponent.create(newContents);
    result.setStyle(original.getStyle());
    for (Component sibling : original.getSiblings()) {
      result.append(processComponent(sibling, targetLanguage));
    }
    return result;
  }
}

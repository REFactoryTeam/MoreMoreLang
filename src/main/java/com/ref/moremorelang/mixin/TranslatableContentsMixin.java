package com.ref.moremorelang.mixin;

import com.ref.moremorelang.lang.ControllableLanguageComponent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableContents.class)
@OnlyIn(Dist.CLIENT)
public class TranslatableContentsMixin implements ControllableLanguageComponent {
  @Unique private Language moreMoreLang$targetLanguage;

  @Override
  public void moreMoreLang$setTargetLanguage(@NotNull Language language) {
    this.moreMoreLang$targetLanguage = language;
  }

  /**
   * Intercepts the local 'language' variable within the decompose() method. If a target language
   * has been set on this specific instance, we substitute it. Otherwise, we let it proceed with the
   * default (Language.getInstance()).
   *
   * @param originalLanguage The original value of the local variable (result of
   *     Language.getInstance()).
   * @return The language to use for decomposition.
   */
  @ModifyVariable(method = "decompose()V", at = @At(value = "STORE"), ordinal = 0)
  private Language modifyDecompositionLanguage(Language originalLanguage) {
    if (this.moreMoreLang$targetLanguage != null) {
      return this.moreMoreLang$targetLanguage;
    }
    return originalLanguage;
  }
}

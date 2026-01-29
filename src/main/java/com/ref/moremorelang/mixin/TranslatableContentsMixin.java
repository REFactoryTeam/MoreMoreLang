package com.ref.moremorelang.mixin;

import com.ref.moremorelang.lang.ControllableLanguageComponent;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TranslatableContents.class)
@OnlyIn(Dist.CLIENT)
public abstract class TranslatableContentsMixin implements ControllableLanguageComponent {
  @Shadow
  public abstract String getKey();

  @Shadow
  @Nullable
  public abstract String getFallback();

  @Shadow
  public abstract Object[] getArgs();

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

  /**
   * @author remakefactory
   * @reason Overwrite equals to include the custom targetLanguage field, preventing incorrect cache
   *     hits.
   */
  @Overwrite
  public boolean equals(Object pOther) {
    if (this == pOther) {
      return true;
    }
    if (!(pOther instanceof TranslatableContents that)) {
      return false;
    }
    TranslatableContentsMixin thatMixin = (TranslatableContentsMixin) (Object) that;
    return Objects.equals(this.getKey(), that.getKey())
        && Objects.equals(this.getFallback(), that.getFallback())
        && Arrays.equals(this.getArgs(), that.getArgs())
        && Objects.equals(this.moreMoreLang$targetLanguage, thatMixin.moreMoreLang$targetLanguage);
  }

  /**
   * @author remakefactory
   * @reason Overwrite hashCode to include the custom targetLanguage field, ensuring consistency
   *     with the modified equals method.
   */
  @Overwrite
  public int hashCode() {
    int result = Objects.hashCode(this.getKey());
    result = 31 * result + Objects.hashCode(this.getFallback());
    result = 31 * result + Arrays.hashCode(this.getArgs());
    result = 31 * result + Objects.hashCode(this.moreMoreLang$targetLanguage);
    return result;
  }
}

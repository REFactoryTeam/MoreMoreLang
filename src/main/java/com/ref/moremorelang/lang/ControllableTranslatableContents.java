package com.ref.moremorelang.lang;

import com.google.common.collect.ImmutableList;
import java.util.Objects;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.chat.contents.TranslatableFormatException;
import org.jetbrains.annotations.NotNull;

public class ControllableTranslatableContents extends TranslatableContents {

  private final Language targetLanguage;

  public ControllableTranslatableContents(
      @NotNull TranslatableContents original, @NotNull Language targetLanguage) {
    super(original.getKey(), original.getFallback(), original.getArgs());
    this.targetLanguage = targetLanguage;
  }

  @Override
  protected void decompose() {
    if (this.targetLanguage == this.decomposedWith) {
      return;
    }
    this.decomposedWith = this.targetLanguage;
    var formatTemplate =
        this.targetLanguage.getOrDefault(
            this.getKey(), Objects.requireNonNullElse(this.getFallback(), this.getKey()));

    try {
      ImmutableList.Builder<FormattedText> builder = ImmutableList.builder();
      this.decomposeTemplate(formatTemplate, builder::add);
      this.decomposedParts = builder.build();
    } catch (TranslatableFormatException translatableformatexception) {
      this.decomposedParts = ImmutableList.of(FormattedText.of(formatTemplate));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ControllableTranslatableContents that) || !super.equals(o)) {
      return false;
    }
    return Objects.equals(this.targetLanguage, that.targetLanguage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.targetLanguage);
  }
}

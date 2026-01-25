package com.ref.moremorelang.mixin;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import net.minecraftforge.fml.loading.FMLPaths;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MoremorelangMixinConfigPlugin implements IMixinConfigPlugin {

  private boolean isMixinEnabled = true;

  @Override
  public void onLoad(String mixinPackage) {
    final Path configPath = FMLPaths.CONFIGDIR.get().resolve("moremorelang-client.toml");
    try {
      final CommentedFileConfig configData = CommentedFileConfig.builder(configPath).build();
      configData.load();
      this.isMixinEnabled = configData.getOrElse("UseMixinTranslator", true);
      configData.close();
    } catch (Exception e) {
      this.isMixinEnabled = true;
    }
  }

  @Override
  public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
    if ("com.ref.moremorelang.mixin.TranslatableContentsMixin".equals(mixinClassName)) {
      return this.isMixinEnabled;
    }
    return true;
  }

  @Override
  public String getRefMapperConfig() {
    return null;
  }

  @Override
  public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

  @Override
  public List<String> getMixins() {
    return null;
  }

  @Override
  public void preApply(
      String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

  @Override
  public void postApply(
      String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}

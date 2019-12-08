package migrami.core.interfaces;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MigramiCategoryScriptLoader {
  private HashMap<MigramiCategory, MigramiScriptLoader> scriptLoaders;

  public MigramiCategoryScriptLoader() {
    this.scriptLoaders = new HashMap<>();
  }

  public void add(MigramiCategory category, MigramiScriptLoader loader) {
    this.scriptLoaders.put(category, loader);
  }

  public void get(MigramiCategory category, Consumer<MigramiScriptLoader> loaderConsumer) {
    loaderConsumer.accept(this.scriptLoaders.get(category));
  }

  public void foreach(BiConsumer<MigramiCategory, MigramiScriptLoader> loaderConsumer) {
    this.scriptLoaders.forEach(loaderConsumer);
  }

  public Boolean empty() {
    return this.scriptLoaders.isEmpty();
  }
}

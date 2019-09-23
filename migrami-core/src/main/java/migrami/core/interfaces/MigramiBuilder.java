package migrami.core.interfaces;

import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class MigramiBuilder<B extends MigramiBuilder<B>> {
  protected MigramiCategoryScriptLoader scriptLoaders;
  
  protected Optional<MigramiSnapshotRepository> repository;
  
  protected MigramiBuilder() {
    this.scriptLoaders = new MigramiCategoryScriptLoader();
    this.repository = Optional.empty();
  }
  
  public final B withClasspathScriptLoader() {
    return this.withScriptLoader(new ClassPathScriptLoader());
  }
  
  public final B withScriptLoader(MigramiScriptLoader loader) {
    return (B) this.addScriptLoader(Category.DEFAULT, loader);
  }
  
  public final B addScriptLoader(MigramiCategory category, MigramiScriptLoader loader) {
    this.scriptLoaders.add(category, loader);
    return (B) this;
  }
  
  public final B withInMemorySnapshotRepository() {
    return this.withSnapshotRepository(new InMemorySnapshotRepository());
  }
  
  public final B withSnapshotRepository(MigramiSnapshotRepository repository) {
    this.repository = Optional.ofNullable(repository);
    return (B) this;
  }
  
  public abstract Migrami build();
}

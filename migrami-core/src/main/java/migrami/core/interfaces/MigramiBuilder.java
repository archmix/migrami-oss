package migrami.core.interfaces;

import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class MigramiBuilder<B extends MigramiBuilder<B>> {
  protected MigramiCategoryScriptLoader scriptLoaders;

  protected Optional<MigramiSnapshotRepository> repository;

  protected Optional<MigramiChecksumFactory> checksumFactory;

  protected MigramiBuilder() {
    this.scriptLoaders = new MigramiCategoryScriptLoader();
    this.repository = Optional.empty();
    this.checksumFactory = Optional.of(MD5ChecksumFactory.valueOf());
  }

  public final B withChecksumfactory(MigramiChecksumFactory checksumFactory) {
    this.checksumFactory = Optional.ofNullable(checksumFactory);
    return (B) this;
  }

  public final B withClasspathScriptLoader() {
    return this.withClasspathScriptLoader("migration", Category.DEFAULT);
  }

  public final B withClasspathScriptLoader(MigramiCategory category) {
    return this.withClasspathScriptLoader("migration", category);
  }

  public final B withClasspathScriptLoader(String rootPath) {
    return this.withClasspathScriptLoader(rootPath, Category.DEFAULT);
  }

  public final B withClasspathScriptLoader(String rootPath, MigramiCategory category) {
    return this.addScriptLoader(category, new ClassPathScriptLoader(rootPath));
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

  protected void validate() {
    final String snapshotRepositoryMessage = "Snapshot repository cannot be null, it is mandatory. Do implement your own or use the built-in through Builder class";
    repository.orElseThrow(() -> new IllegalStateException(snapshotRepositoryMessage));

    final String checksumFactoryMessage = "Checksum Factory cannot be null, it is mandatory. Migrami has it own built-in md5 checksum implementation, just do not configure it in Builder interface.";
    this.checksumFactory.orElseThrow(() -> new IllegalStateException(checksumFactoryMessage));
  }

  public abstract Migrami build();
}

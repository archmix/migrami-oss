package migrami.core.interfaces;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MigramiEngine implements Migrami {
  protected final MigramiSnapshotRepository repository;
  private final MigramiCategoryScriptLoader loader;
  private final MigramiChecksumFactory checksumFactory;

  @Override
  public final void migrate() {
    this.before();
    this.loader.foreach((category, loader) -> {
      loader.load(category, this.checksumFactory).forEach(this::baseline);
    });
    this.after();
  }

  private void baseline(MigramiScript script) {
    MigramiSnapshot snapshot = this.repository.load(script);
    if (!snapshot.checksum().value().equals(script.checksum().value())) {
      String message =
        String.format("Checksum aplied to script %s is different from a stored migrated version",
          script.name().value());
      throw new IllegalStateException(message);
    }
    snapshot.execute(this::migrate);
    this.repository.save(snapshot);
  }

  protected void before() {
    return;
  }

  protected abstract void migrate(MigramiScript script);

  protected void after() {
    return;
  }
}

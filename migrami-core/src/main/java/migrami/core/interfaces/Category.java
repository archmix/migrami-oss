package migrami.core.interfaces;

enum Category implements MigramiCategory {
  DEFAULT;

  @Override
  public String path() {
    return this.name().toLowerCase();
  }
}

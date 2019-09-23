package migrami.core.interfaces;

enum Category implements MigramiCategory {
  DEFAULT;

  @Override
  public String location() {
    return this.name().toLowerCase();
  }
}

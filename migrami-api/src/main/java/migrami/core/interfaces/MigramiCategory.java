package migrami.core.interfaces;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

public interface MigramiCategory {
  String name();

  String path();

  @RequiredArgsConstructor(staticName = "create")
  @EqualsAndHashCode(of = {"name"})
  public static class MigramiCategoryAdapter implements MigramiCategory {
    private final String name;

    private final String path;

    @Override
    public String path() {
      return this.path;
    }

    @Override
    public String name() {
      return this.name;
    }
  }
}

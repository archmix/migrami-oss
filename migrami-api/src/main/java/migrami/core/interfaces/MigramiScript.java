package migrami.core.interfaces;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"category", "name"})
public class MigramiScript {
  private final MigramiCategory category;

  private final MigramiScriptName name;

  private final String content;

  private final MigramiChecksum checksum;

  public static MigramiScript create(MigramiCategory category, MigramiChecksumFactory checksumFactory, String nameURI, String content) {
    MigramiChecksum checksum = checksumFactory.create(content);
    MigramiScriptName name = MigramiScriptName.create(nameURI);

    return new MigramiScript(category, name, content, checksum);
  }

  public MigramiCategory category() {
    return category;
  }

  public String content() {
    return content;
  }

  public MigramiChecksum checksum() {
    return checksum;
  }

  public String description() {
    return name.description();
  }

  public MigramiVersion version() {
    return name.version();
  }

  public MigramiScriptName name() {
    return name;
  }

  @Override
  public String toString() {
    return this.name().toString();
  }
}

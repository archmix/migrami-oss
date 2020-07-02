package migrami.core.interfaces;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import toolbox.resources.interfaces.ResourceName;

import java.io.InputStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"category", "name"})
public class MigramiScript {
  private final MigramiCategory category;

  private final MigramiScriptName name;

  private final MigramiScriptBody body;

  private final MigramiChecksum checksum;

  public static MigramiScript create(MigramiCategory category, MigramiChecksumFactory checksumFactory, ResourceName resourceName, InputStream content) {
    MigramiScriptBody body = MigramiScriptBody.create(content);
    MigramiChecksum checksum = checksumFactory.create(body.toString());
    MigramiScriptName name = MigramiScriptName.create(resourceName);

    return new MigramiScript(category, name, body, checksum);
  }

  public MigramiCategory category() {
    return category;
  }

  public MigramiScriptBody body() {
    return this.body;
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

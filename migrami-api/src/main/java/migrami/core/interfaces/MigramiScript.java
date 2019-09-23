package migrami.core.interfaces;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MigramiScript {
  private final MigramiCategory category;
  
  private final MigramiScriptName name;

  private final byte[] content;

  private final MigramiChecksum checksum;

  public static MigramiScript create(MigramiCategory category, MigramiScriptName name, byte[] content, MigramiChecksum checksum) {
    return new MigramiScript(category, name, content, checksum);
  }
  
  public MigramiCategory category() {
    return category;
  }

  public InputStream content() {
    return new ByteArrayInputStream(content);
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
}

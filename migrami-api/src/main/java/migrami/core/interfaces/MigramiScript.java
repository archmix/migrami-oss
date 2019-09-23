package migrami.core.interfaces;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MigramiScript {
  private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\r\n");
  
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

  public byte[] content() {
    return content;
  }
  
  public String contentAsString() {
    StringBuilder content = new StringBuilder();

    try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.content)))){
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line).append(LINE_SEPARATOR);
      }
    } catch(Exception e) {
      throw new IllegalStateException(e);
    }

    return content.toString();
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

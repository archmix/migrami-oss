package migrami.core.infra;

import migrami.core.interfaces.ResourceName;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceStream {
  public static String read(Path path, ResourceName resourceName) {
    String resourceLocation = Paths.get(path.toString(), resourceName.toString()).toString();

    try (InputStream input = ResourceStream.class.getClassLoader().getResourceAsStream(resourceLocation)) {
      byte[] buffer = new byte[input.available()];
      input.read(buffer);

      return new String(buffer);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}

package migrami.core.infra;

import migrami.core.interfaces.ResourceName;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceStream {
  public static String read(Path path, ResourceName resourceName) {
    String resourceLocation = Paths.get(path.toString(), resourceName.toString()).toString();

    try (InputStream input = stream(path, resourceName)) {
      byte[] buffer = new byte[input.available()];
      input.read(buffer);

      return new String(buffer);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static byte[] readBytes(InputStream inputStream) {
    try {
      byte[] buffer = new byte[inputStream.available()];
      inputStream.read(buffer);
      return buffer;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static InputStream stream(Path path, ResourceName resourceName) {
    String resourceLocation = Paths.get(path.toString(), resourceName.toString()).toString();
    return ResourceStream.class.getClassLoader().getResourceAsStream(resourceLocation);
  }
}

package migrami.core.infra;

import java.io.IOException;
import java.io.InputStream;

public class Streams {
  public static String read(String resourceName) {
    try (InputStream input = Streams.class.getClassLoader().getResourceAsStream(resourceName)){
      byte[] buffer = new byte[input.available()];
      input.read(buffer);

      return new String(buffer);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}

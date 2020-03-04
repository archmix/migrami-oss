package migrami.core.infra;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import migrami.core.interfaces.MigramiLogger;
import migrami.core.interfaces.ResourceName;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import static migrami.core.interfaces.ResourceName.SLASH;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class JarResourceResolver implements ResourceResolver {
  private static final String FILE_PROTOCOL = "file:";
  private static final String JRE_LIB = "/jre/lib/";

  private static final String FOLDER_REGEX = "(.*)!\\/";
  private static final String JAR = "jar";

  private final Logger logger = MigramiLogger.logger();
  private static final JarResourceResolver valueOf = new JarResourceResolver();

  public static JarResourceResolver valueOf() {
    return valueOf;
  }

  @Override
  public boolean accept(URL location) {
    if(location == null){
      return false;
    }

    return JAR.equals(location.getProtocol())
      && !location.getPath().matches(".*" + Pattern.quote(JRE_LIB) + ".*");
  }

  @Override
  public Iterable<ResourceName> resolve(URL location) {
    List<ResourceName> names = new ArrayList<>();

    try(JarFile jarFile = this.jarFile(location)) {
      Enumeration<JarEntry> entries = jarFile.entries();
      String folderPath = location.getPath().replaceAll(FOLDER_REGEX, "").concat(SLASH);

      while (entries.hasMoreElements()) {
        String name = entries.nextElement().getName();

        if (!name.equals(folderPath) && name.startsWith(folderPath)) {
          ResourceName resourceName = ResourceName.create(name.substring(folderPath.length()));
          names.add(resourceName);
        }
      }
    } catch (IOException | SecurityException e) {
      logger.warn("Skipping unloadable jar file: " + location + " (" + e.getMessage() + ")");
    }

    return names;
  }

  private JarFile jarFile(URL location) throws IOException {
    return new JarFile(getClass().getProtectionDomain()
      .getCodeSource().getLocation().toString().substring(FILE_PROTOCOL.length()));
  }
}

package migrami.core.interfaces;

import lombok.RequiredArgsConstructor;
import migrami.core.infra.ResourceResolver;
import migrami.core.infra.ResourceStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RequiredArgsConstructor
class ClassPathScriptLoader implements MigramiScriptLoader {
  private final String path;

  public ClassPathScriptLoader() {
    this.path = "migration";
  }

  @Override
  public Iterable<MigramiScript> load(MigramiCategory category,
                                      MigramiChecksumFactory checksumFactory) {

    try {
      Path path = Paths.get(this.path, category.path());
      Enumeration<URL> resources = getClassLoader().getResources(path.toString());

      List<MigramiScript> scripts = new ArrayList<MigramiScript>();
      if (resources.hasMoreElements()) {
        URL location = resources.nextElement();
        ResourceResolver.resolver(location).resolve().forEach(resourceName -> {
          InputStream content = ResourceStream.stream(path, resourceName);
          MigramiScript script = this.toMigramiScript(category, checksumFactory, resourceName, content);
          scripts.add(script);
        });
      }
      return scripts;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private ClassLoader getClassLoader() {
    return this.getClass().getClassLoader();
  }

  private MigramiScript toMigramiScript(MigramiCategory category,
                                        MigramiChecksumFactory checksumFactory, ResourceName resourceName, InputStream content) {
    return MigramiScript.create(category, checksumFactory, resourceName, content);
  }
}

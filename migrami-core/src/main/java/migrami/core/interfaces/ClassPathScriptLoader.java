package migrami.core.interfaces;

import lombok.RequiredArgsConstructor;
import migrami.core.infra.Streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
      Enumeration<URL> resources = ClassLoader.getSystemResources(path.toString());

      List<MigramiScript> scripts = new ArrayList<MigramiScript>();
      if (resources.hasMoreElements()) {
        URL resource = resources.nextElement();
        this.getScripts(resource).forEach(scriptName -> {
          String content = this.getContent(path, scriptName);
          MigramiScript script = this.toMigramiScript(category, checksumFactory, scriptName, content);
          scripts.add(script);
        });
      }
      return scripts;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private String getContent(Path path, String scriptName) {
    String filename = Paths.get(path.toString(), scriptName).toString();
    return Streams.read(filename);
  }

  private Iterable<String> getScripts(URL url) throws IOException {
    Set<String> scripts = new TreeSet<>();
    try (InputStream input = url.openConnection().getInputStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

      String resource = null;
      while ((resource = reader.readLine()) != null) {
        scripts.add(resource);
      }
    }

    return scripts;
  }

  private MigramiScript toMigramiScript(MigramiCategory category,
                                        MigramiChecksumFactory checksumFactory, String name, String content) {
    return MigramiScript.create(category, checksumFactory, name, content);
  }
}

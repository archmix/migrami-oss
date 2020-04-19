package migrami.core.interfaces;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.jar.JarEntry;

@RequiredArgsConstructor(staticName = "create")
@EqualsAndHashCode(of = "value")
public class ResourceName {
  public static final String SLASH = "/";
  private final String value;

  public static ResourceName create(JarEntry jarEntry) {
    String entryName = jarEntry.getName();
    entryName = entryName.substring(0, entryName.lastIndexOf(SLASH) + 1);
    return ResourceName.create(entryName);
  }

  public String value(){
    return this.value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
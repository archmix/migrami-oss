package migrami.core.interfaces;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of="value")
public class MigramiScriptName implements Comparable<MigramiScriptName>{
  private final String value;
  
  private final MigramiVersion version;
  
  private final String description;
  
  public static MigramiScriptName create(String nameURI) {
    if(!nameURI.startsWith("V")) {
      throw new MigramiScriptNameViolationException("Script name should start with V.");
    }
    
    if(!nameURI.contains("_")) {
      throw new MigramiScriptNameViolationException("Script name should have the description in its name.");
    }
    
    int underlineIndex = nameURI.indexOf("_");
    
    String version = nameURI.substring(0, underlineIndex);
    String description = nameURI.substring(underlineIndex + 1);
  
    return new MigramiScriptName(nameURI, MigramiVersion.create(version), description);
  }
  
  @Override
  public int compareTo(MigramiScriptName name) {
    return this.version.compareTo(name.version);
  }
  
  public MigramiVersion version() {
    return this.version;
  }
  
  public String description() {
    return this.description;
  }
  
  public String value() {
    return value;
  }
  
  @Override
  public String toString() {
    return this.value;
  }
}
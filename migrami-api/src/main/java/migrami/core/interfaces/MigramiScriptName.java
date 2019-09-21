package migrami.core.interfaces;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MigramiScriptName implements Comparable<MigramiScriptName>{
  private final String fullName;
  
  private final MigramiVersion version;
  
  private final String description;
  
  public static MigramiScriptName create(String fullName) {
    if(!fullName.startsWith("V")) {
      throw new MigramiScriptNameViolationException("Script name should start with V.");
    }
    
    if(!fullName.contains("_")) {
      throw new MigramiScriptNameViolationException("Script name should have the description in its name.");
    }
    
    int underlineIndex = fullName.indexOf("_");
    
    String version = fullName.substring(0, underlineIndex);
    String description = fullName.substring(underlineIndex + 1);
  
    return new MigramiScriptName(fullName, MigramiVersion.create(version), description);
  }
  
  @Override
  public int compareTo(MigramiScriptName name) {
    return this.version.compareTo(name.version);
  }
  
  public MigramiVersion version() {
    return this.version;
  }
  
  String description() {
    return this.description;
  }
  
  String fullName() {
    return fullName;
  }
}
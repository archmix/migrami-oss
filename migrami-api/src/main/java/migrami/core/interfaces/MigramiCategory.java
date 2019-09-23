package migrami.core.interfaces;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

public interface MigramiCategory {
  String name();
  
  String location();
  
  @RequiredArgsConstructor(staticName = "create")
  @EqualsAndHashCode(of = {"name"})
  public static class MigramiCategoryAdapter implements MigramiCategory{
    private final String name;
    
    private final String location;
    
    @Override
    public String location() {
      return this.location;
    }
    
    @Override
    public String name() {
      return this.name;
    }
  }
}

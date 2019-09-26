package migrami.core.interfaces;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.Setter;

@Setter
public class MigramiSnapshot {
  private final MigramiCategory category;
  
  private final MigramiChecksum checksum;

  private final MigramiScriptName scriptName;
  
  private final Optional<MigramiScript> script;

  MigramiSnapshot(MigramiCategory category, MigramiChecksum checksum, MigramiScriptName scriptName) {
    this(category, checksum, scriptName, Optional.empty());
  }
  
  MigramiSnapshot(MigramiCategory category, MigramiChecksum checksum, MigramiScriptName scriptName, Optional<MigramiScript> script) {
    this.category = category;
    this.checksum = checksum;
    this.scriptName = scriptName;
    this.script = script;
  }
  
  public static MigramiSnapshot valueOf(MigramiCategory category, MigramiChecksum checksum, MigramiScriptName scriptName) {
    return new MigramiSnapshot(category, checksum, scriptName);
  }

  public static MigramiSnapshot create(MigramiScript script) {
    return new MigramiSnapshot(script.category(), script.checksum(), script.name(), Optional.of(script));
  }
  
  public void execute(Consumer<MigramiScript> execution) {
    this.script.ifPresent(execution::accept);
  }
  
  public void visit(Consumer<MigramiScript> persist) {
    this.script.ifPresent(persist);
  }
  
  public MigramiCategory category() {
    return category;
  }
  
  public MigramiChecksum checksum() {
    return checksum;
  }

  public String description() {
    return scriptName.description();
  }

  public MigramiScriptName scriptName() {
    return scriptName;
  }

  public MigramiVersion version() {
    return scriptName.version();
  }
}

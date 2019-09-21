package migrami.core.interfaces;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.Setter;

@Setter
public class MigramiSnapshot {
  private final MigramiChecksum checksum;

  private final MigramiScriptName scriptName;
  
  private final Optional<MigramiScript> script;

  MigramiSnapshot(MigramiChecksum checksum, MigramiScriptName scriptName) {
    this(checksum, scriptName, Optional.empty());
  }
  
  MigramiSnapshot(MigramiChecksum checksum, MigramiScriptName scriptName, Optional<MigramiScript> script) {
    this.checksum = checksum;
    this.scriptName = scriptName;
    this.script = script;
  }

  public static MigramiSnapshot create(MigramiScript script) {
    return new MigramiSnapshot(script.checksum(), script.name(), Optional.of(script));
  }
  
  public void execute(Consumer<MigramiScript> execution) {
    this.script.ifPresent(execution::accept);
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

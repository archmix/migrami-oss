package migrami.core.interfaces;

import java.util.HashMap;
import java.util.Map;

class InMemorySnapshotRepository implements MigramiSnapshotRepository {
  private Map<String, MigramiSnapshot> scripts;

  public InMemorySnapshotRepository() {
    this.scripts = new HashMap<>();
  }

  @Override
  public MigramiSnapshot load(MigramiScript script) {
    MigramiSnapshot snapshot = this.scripts.get(this.key(script.name()));

    if (snapshot == null) {
      snapshot = MigramiSnapshot.create(script);
    }

    return snapshot;
  }

  @Override
  public void save(MigramiSnapshot snapshot) {
    this.scripts.put(this.key(snapshot.scriptName()), snapshot);
  }

  private String key(MigramiScriptName name) {
    return name.value();
  }
}

package migrami.core.interfaces;

public interface MigramiSnapshotRepository {
  MigramiSnapshot load(MigramiScript script);
  
  void save(MigramiSnapshot snapshot);
}

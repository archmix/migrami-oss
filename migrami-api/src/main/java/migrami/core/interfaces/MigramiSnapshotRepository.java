package migrami.core.interfaces;

public interface MigramiSnapshotRepository {
  Iterable<MigramiSnapshot> load(Iterable<MigramiScript> scripts);
}

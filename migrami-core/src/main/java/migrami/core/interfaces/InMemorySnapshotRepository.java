package migrami.core.interfaces;

class InMemorySnapshotRepository implements MigramiSnapshotRepository {
  @Override
  public MigramiSnapshot load(MigramiScript script) {
    return null;
  }
  
  @Override
  public void save(MigramiSnapshot snapshot) {
    
  }
}

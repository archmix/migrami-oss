package migrami.core.interfaces;

public interface MigramiScriptLoader {
  Iterable<MigramiScript> load(MigramiCategory category, MigramiChecksumFactory checksumFactory);
}

package migrami.sql.interfaces;

import org.junit.Test;
import migrami.core.interfaces.Migrami;

public class MigramiSQLEngineTest {
  @Test
  public void givenSQLEngineWhenMigrateThenApplyScripts() {
    Migrami migrami = MigramiSQLEngineBuilder.create().withDatasource("jdbc:h2:mem:test", "sa", "sa")
        .withClasspathScriptLoader().withTableSnapshotRepository().build();
  
    migrami.migrate();
  }
}

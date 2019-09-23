package migrami.sql.interfaces;

import migrami.core.interfaces.MigramiCategoryScriptLoader;
import migrami.core.interfaces.MigramiEngine;
import migrami.core.interfaces.MigramiScript;
import migrami.core.interfaces.MigramiSnapshotRepository;

class MigramiSQLEngine extends MigramiEngine {
  private final DatabaseConfiguration databaseConfiguration;
  
  MigramiSQLEngine(DatabaseConfiguration databaseConfiguration, MigramiCategoryScriptLoader loader,
      MigramiSnapshotRepository repository) {
    super(loader, repository);
    this.databaseConfiguration = databaseConfiguration;
  }
  
  @Override
  protected void before() {
    
  }

  @Override
  protected void migrate(MigramiScript script) {
    
  }
  
  @Override
  protected void after() {
    
  }
}
